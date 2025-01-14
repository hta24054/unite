package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.enums.NotificationCategory;
import com.hta2405.unite.mybatis.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectMapper dao;
    private final NotificationService notificationService;
    @Autowired
    public ProjectServiceImpl(ProjectMapper dao, NotificationService notificationService) {
        this.dao = dao;
        this.notificationService = notificationService;
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
    public int createProject(Project project) {
        // 매니저 ID 처리: 괄호 안의 ID만 추출
        String managerInfo = project.getManagerId();
        String managerId = managerInfo.substring(managerInfo.indexOf("(") + 1, managerInfo.indexOf(")")).trim();
        String managername = managerInfo.replace("()", "").trim();
        project.setManagerName(managername);
        project.setManagerId(managerId);
        dao.createProject(project);

        return project.getProjectId();
    }


    @Override
    @Transactional
    public void addProjectMember(int projectId, String memberName, String memberId, String role) {
        dao.addProjectMember(projectId, memberName, memberId, role);
    }

    @Override
    @Transactional
    public void addProjectMembers(int projectId, String memberName, String memberId, String role) {
        dao.addProjectMembers(projectId, memberName, memberId, role);
    }

    @Override
    @Transactional
    public void createTask(int projectId, String empId, String memberName) {
        dao.createTask(projectId, empId, memberName);
    }


    public int doneCountList(String userid, int finish, int cancel) {
        return dao.doneCountList(userid, finish, cancel);
    }

    public void projectFavorite(int projectId, String userid) {
        dao.projectFavorite(projectId, userid);
    }

    public void projectColor(String userid, int projectId, String bgColor, String textColor) {
        dao.projectColor(userid, projectId, bgColor, textColor);
    }

    @Override
    public boolean updateProjectStatus(int projectId, String status) {
        if ("completed".equals(status)) {
            return dao.projectStatus(projectId, 1, 0);
        } else if ("canceled".equals(status)) {
            return dao.projectStatus(projectId, 0, 1);
        }
        return false;
    }

    public List<Project> getDoneList(String userid, int page, int limit) {
        HashMap<String, Object> map = new HashMap<>();
        int startrow = (page - 1) * limit;
        int endrow = startrow + limit;
        map.put("userid", userid);
        map.put("start", startrow);
        map.put("end", endrow);
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
//        List<ProjectTodoDTO> todoList = dao.getTodoList(projectId, userid);
//        List<ProjectDetailDTO> user = dao.getProjectDetail1(projectId, userid);
//        NotificationDTO notification = NotificationDTO.builder()
//                .category(NotificationCategory.DOC)
//                .title(dao.getProjectName(projectId) + '-' + todoList.get(0).getTodoSubject())
//                .message(user.get(0).getParticipantNames() + "님이 글을 작성하셨습니다")
//                .recipientId(user.get(1).getMemberId())
//                .targetUrl("/project/detail?projectId=" + user.get(1).getProjectId())
//                .isRead(false)
//                .createdAt(LocalDateTime.now().toString()).build();
//        notificationService.sendNotification(notification);
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
}
