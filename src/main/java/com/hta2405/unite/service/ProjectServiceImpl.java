package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.enums.NotificationCategory;
import com.hta2405.unite.mybatis.mapper.ProjectMapper;
import com.hta2405.unite.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final String FILE_DIR = ConfigUtil.getProperty("project.upload.directory");
    private final ProjectMapper dao;
    private final NotificationService notificationService;
    private final FileService fileService;

    @Autowired
    public ProjectServiceImpl(ProjectMapper dao, NotificationService notificationService, FileService fileService) {
        this.dao = dao;
        this.notificationService = notificationService;
        this.fileService = fileService;
    }

    public List<Project> getmainList(String userid) {
        return dao.getMainList(userid);
    }

    public List<Emp> getHiredEmpByDeptId(long deptId) {
        return dao.getHiredEmpByDeptId(deptId);
    }

    @Override
    public List<Map<Long, String>> getIdToJobNameMap() {
        return dao.getIdToJobNameMap();
    }

    @Transactional
    public void createProject(Project project) {
        String managerInfo = project.getManagerId();
        String managerId;
        String managerName;

        try {
            managerId = managerInfo.substring(managerInfo.indexOf("(") + 1, managerInfo.indexOf(")")).trim();
            managerName = managerInfo.replace("(" + managerId + ")", "").trim();
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("manager_id 형식이 올바르지 않습니다. 값: " + managerInfo, e);
        }

        project.setManagerName(managerName);
        project.setManagerId(managerId);

        // 프로젝트 생성
        dao.createProject(project);
        int projectId = project.getProjectId();

        if (projectId <= 0) {
            throw new IllegalArgumentException("프로젝트 생성에 실패했습니다.");
        }

        // 매니저 추가
        addProjectMemberAndTask(projectId, managerName, managerId, "MANAGER");

        // 참여자 추가
        if (project.getParticipants() != null && !project.getParticipants().isEmpty()) {
            String[] participantArray = project.getParticipants().split(",");
            for (String participant : participantArray) {
                String[] participantInfo = participant.trim().split("\\(");
                if (participantInfo.length == 2) {
                    String empName = participantInfo[0].trim();
                    String empId = participantInfo[1].replace(")", "").trim();
                    addProjectMemberAndTask(projectId, empName, empId, "PARTICIPANT");
                }
            }
        }

        // 열람자 추가
        if (project.getViewers() != null && !project.getViewers().isEmpty()) {
            String[] viewerArray = project.getViewers().split(",");
            for (String viewer : viewerArray) {
                String[] viewerInfo = viewer.trim().split("\\(");
                if (viewerInfo.length == 2) {
                    String empName = viewerInfo[0].trim();
                    String empId = viewerInfo[1].replace(")", "").trim();
                    if (!empId.equals(managerId)) { // 매니저가 아닌 경우만 추가
                        addProjectMemberAndTask(projectId, empName, empId, "VIEWER");
                    }
                }
            }
        }
    }

    private void addProjectMemberAndTask(int projectId, String memberName, String memberId, String role) {
        dao.addProjectMember(projectId, memberName, memberId, role);
        dao.createTask(projectId, memberId, memberName);
    }

    public void projectFavorite(int projectId, String userid) {
        dao.projectFavorite(projectId, userid);
    }

    public void projectColor(String userid, int projectId, String bgColor, String textColor) {
        dao.projectColor(userid, projectId, bgColor, textColor);
    }

    @Override
    public boolean updateProjectStatus(int projectId, String status, MultipartFile file) {
        FileDTO taskFile = null;
        if (file != null && !file.isEmpty()) {
            taskFile = fileService.uploadFile(file, FILE_DIR);
        }
        // 상태에 따라 업데이트 수행
        if ("completed".equals(status)) {
            return dao.projectStatus(projectId, taskFile, 1, 0);
        } else if ("canceled".equals(status)) {
            return dao.projectStatus(projectId, taskFile, 0, 1);
        }
        return false;
    }


    public List<Project> getDoneList(String userid, int dowhat) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        if(dowhat == 1) map.put("dowhat", "project_canceled");
        else if(dowhat == 2) map.put("dowhat", "project_finished");
        return dao.getDoneList(map);
    }

    public String getProjectName(int projectId) {
        return dao.getProjectName(projectId);
    }
    public String getProjectContent(int projectId){return dao.getProjectContent(projectId);}

    public List<ProjectDetailDTO> getProjectDetail1(int projectId, String userid) {
        return dao.getProjectDetail1(projectId, userid);
    }

    public boolean updateTaskContent(int projectId, String memberId, String taskContent) {
        return dao.updateTaskContent(projectId, memberId, taskContent);
    }

    public boolean updateProgressRate(int projectId, String memberId, int memberProgressRate) {
        return dao.updateProgressRate(projectId, memberId, memberProgressRate);
    }

    public List<ProjectTaskDTO> getProjectDetail2(int projectId) {
        return dao.getProjectDetail2(projectId);
    }

    public List<ProjectRoleDTO> getRole(int projectId, String userid) {
        return dao.getRole(projectId, userid);
    }

    public void insertToDo(String task, String userid, int projectId){
        dao.insertToDo(task, userid, projectId);
        List<ProjectDetailDTO> user = dao.getProjectDetail1(projectId, userid);

        NotificationDTO.NotificationDTOBuilder builder = NotificationDTO.builder()
                .category(NotificationCategory.PROJECT)
                .title(dao.getProjectName(projectId) + '-' + task)
                .message(user.get(0).getParticipantNames() + "님이 글을 작성하셨습니다")
                .targetUrl("/project/detail?projectId=" + user.get(0).getProjectId())
                .isRead(false)
                .createdAt(LocalDateTime.now().toString());

        for (ProjectDetailDTO projectDetailDTO : user) {
            String recipent = projectDetailDTO.getMemberId();
            if(!recipent.equals(userid)) {
                NotificationDTO notification = builder.recipientId(recipent).build();
                notificationService.sendNotification(notification);
            }
        }
    }

    public List<ProjectTodoDTO> getTodoList(int projectId, String userid){
        return dao.getTodoList(projectId, userid);
    }

    public boolean todoProgressRate(int projectId, String userid, int todoId, int memberProgressRate){
        return dao.updateTodoProgressRate(projectId, userid, todoId, memberProgressRate);
    }

    public boolean todoUpdate(int projectId, String userid, int todoId, String newSubject) {
        return dao.todoUpdate(projectId, userid, todoId, newSubject);
    }

    public boolean deleteTodo(int todoId) {
        return dao.deleteTodo(todoId);
    }

    public Map<String, String> getColor(int projectId, String memberId) {
        // 프로젝트와 회원에 맞는 색상 정보 가져오는 로직
        Project project = dao.findMemberInfoById(projectId, memberId);

        String bgColor = project.getBgColor();
        String textColor = project.getTextColor();

        Map<String, String> colorInfo = new HashMap<>();
        colorInfo.put("bgColor", bgColor);
        colorInfo.put("textColor", textColor);

        return colorInfo;
    }

}
