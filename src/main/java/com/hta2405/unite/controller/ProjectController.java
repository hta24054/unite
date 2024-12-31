package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.mybatis.mapper.ProjectMapper;
import com.hta2405.unite.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/project")
@Slf4j
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectMapper projectMapper;

    private ProjectService projectService;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @GetMapping(value="/main")
    public String main(){
        return "project/project_main";
    }

    @GetMapping(value = "/create")
    public String create(){
        return "project/project_create";
    }

    @GetMapping(value = "/empTree")
    @ResponseBody
    public Map<String, Object> empTree(@RequestParam("deptId") long deptId) {
        List<Emp> empList = projectService.getHiredEmpByDeptId(deptId);
        List<Map<Long, String>> jobName = projectService.getIdToJobNameMap();
        logger.info("empList = ", empList.size());
        logger.info("jobName = ", jobName.size());
        Map<String, Object> map = new HashMap<>();
        map.put("empList", empList);
        map.put("jobName", jobName);
        return map;
    }

    @PostMapping("/doCreate")
    public String doCreate(@ModelAttribute Project project, Model model) {
        //String userid = (String) session.getAttribute("id"); // 현재 로그인한 사용자 ID
        int projectId = projectService.createProject(project);
        logger.info("projectId = ", projectId);
        String name;
        try {
            name = project.getManager_name().replace("("+project.getManager_id()+")", "").trim();
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("manager_id 형식이 올바르지 않습니다. 값: {}", project.getManager_id(), e);
            throw new IllegalArgumentException("manager_id 형식이 올바르지 않습니다. 값: " + project.getManager_id());
        }

        if (projectId > 0) {
            //String[] manager = project.getManager_id().split(","); // 매니저 ID 분리
            //if (manager.length > 0) {
            projectService.addProjectMember(projectId, name, project.getManager_id(), "MANAGER");
            projectService.createTask(projectId, project.getManager_id(), name);
            //}

            // 참여자 처리
            if (project.getParticipants() != null && !project.getParticipants().isEmpty()) {
                String[] participantArray = project.getParticipants().split(","); // 쉼표로 구분된 참여자들
                for (String participant : participantArray) {
                    String[] participantInfo = participant.trim().split("\\("); // 이름(ID) 형식으로 분리
                    if (participantInfo.length == 2) {
                        String empName = participantInfo[0].trim();  // 이름
                        String empId = participantInfo[1].replace(")", "").trim();  // ID (괄호 제거)

                        logger.info("participant = {} , empId = {}", empName, empId);

                        //if (empId != null && (userid.equals(empId) || !empId.equals(name))) {
                        if (empId != null) {
                            projectService.addProjectMembers(projectId, empName, empId, "PARTICIPANT");
                            projectService.createTask(projectId, empId, empName);
                        }
                    }
                }
            }

            // 열람자 처리
            if (project.getViewers() != null && !project.getViewers().isEmpty()) {
                String[] viewerArray = project.getViewers().split(","); // 쉼표로 구분된 열람자들
                for (String viewer : viewerArray) {
                    String[] viewerInfo = viewer.trim().split("\\("); // 이름(ID) 형식으로 분리
                    if (viewerInfo.length == 2) {
                        String empName = viewerInfo[0].trim();  // 이름
                        String empId = viewerInfo[1].replace(")", "").trim();  // ID (괄호 제거)

                        logger.info("viewer = {} , empId = {}", empName, empId);

                        if (empId != null && !empId.equals(name)) {  // 매니저가 아닌 경우만 추가
                            projectService.addProjectMembers(projectId, empName, empId, "VIEWER");
                            projectService.createTask(projectId, empId, empName);
                        }
                    }
                }
            }
            model.addAttribute("message", "프로젝트가 성공적으로 생성되었습니다.");
        } else {
            model.addAttribute("message", "프로젝트 생성에 실패했습니다.");
        }
        return "redirect:/project/main";
    }


}
