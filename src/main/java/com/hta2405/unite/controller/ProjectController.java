package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.PaginationResult;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.mybatis.mapper.ProjectMapper;
import com.hta2405.unite.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @GetMapping(value = "/main")
    public String main() {
        return "project/project_main";
    }

    @GetMapping(value = "/create")
    public String create() {
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
            name = project.getManagerName().replace("(" + project.getManagerId() + ")", "").trim();
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("manager_id 형식이 올바르지 않습니다. 값: {}", project.getManagerId(), e);
            throw new IllegalArgumentException("manager_id 형식이 올바르지 않습니다. 값: " + project.getManagerId());
        }

        if (projectId > 0) {
            //String[] manager = project.getManager_id().split(","); // 매니저 ID 분리
            //if (manager.length > 0) {
            projectService.addProjectMember(projectId, name, project.getManagerId(), "MANAGER");
            projectService.createTask(projectId, project.getManagerId(), name);
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

    @GetMapping("/getProjects")
    @ResponseBody
    public Map<String, Object> getProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int favorite) {  // 0: 진행 중, 1: 즐겨찾기
        // 현재 인증된 사용자의 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        int limit = 4; // 페이지 당 표시할 데이터 개수
        int listcount = projectService.mainCountList(userid, favorite); // 총 프로젝트 개수 (favorite 파라미터를 넘겨서 처리)
        List<Project> mainList = projectService.getmainList(userid, favorite, page, limit); // 프로젝트 목록 가져오기

        PaginationResult result = new PaginationResult(page, limit, listcount); // 페이지네이션 결과 계산
        logger.info("mainList size = {}", mainList.size());

        Map<String, Object> response = new HashMap<>();
        response.put("page", page);
        response.put("maxpage", result.getMaxpage());
        response.put("startpage", result.getStartpage());
        response.put("endpage", result.getEndpage());
        response.put("listcount", listcount);
        response.put("boardlist", mainList);
        response.put("limit", limit);

        return response;
    }

    @PostMapping("/toggleFavorite")
    @ResponseBody
    public Map<String, Object> toggleFavorite(@RequestParam int projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            projectService.projectFavorite(projectId);
            response.put("success", true); // 성공 상태 전달
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false); // 실패 상태 전달
            response.put("message", "즐겨찾기 상태를 업데이트할 수 없습니다.");
        }
        return response; // JSON 형태로 반환
    }
 @PostMapping("/saveProjectColor")
    public String saveColorSettings(
            @RequestParam int projectId,
            @RequestParam String bgColor,
            @RequestParam String textColor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        logger.info(String.format("User: %s, ProjectId: %d, BgColor: %s, TextColor: %s", userid, projectId, bgColor, textColor));
        projectService.projectColor(userid, projectId, bgColor, textColor);
        return "redirect:/project/main";
    }

    // 프로젝트 상태 업데이트 처리
    @PostMapping("/updateStatus")
    @ResponseBody
    public Map<String, Object> updateProjectStatus(@RequestParam("projectId") int projectId,
                                                   @RequestParam("status") String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isUpdated = projectService.updateProjectStatus(projectId, status);  // 서비스 메서드 호출

            if (isUpdated) {
                response.put("success", true);
                response.put("message", "프로젝트 상태가 업데이트되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "프로젝트 상태 업데이트 실패.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류 발생.");
        }
        return response;
    }
    @GetMapping("/cancel")
    public String cancel() {return "project/project_cancel";}
    @GetMapping("/cancelList")
    @ResponseBody
    public Map<String, Object> cancel(@RequestParam(defaultValue = "1") int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        int limit = 10; // 페이지 당 표시할 데이터 개수
        int listcount = projectService.doneCountList(userid, 0, 1); // 총 프로젝트 개수 (favorite 파라미터를 넘겨서 처리)
        List<Project> mainList = projectService.getDoneList(userid, page, limit); // 프로젝트 목록 가져오기

        PaginationResult result = new PaginationResult(page, limit, listcount); // 페이지네이션 결과 계산
        logger.info("mainList size = {}", mainList.size());
        logger.info("startpage = {}", result.getStartpage());
        logger.info("endpage = {}", result.getEndpage());
        Map<String, Object> response = new HashMap<>();
        response.put("page", page);
        response.put("maxpage", result.getMaxpage());
        response.put("startpage", result.getStartpage());
        response.put("endpage", result.getEndpage());
        response.put("listcount", listcount);
        response.put("boardlist", mainList);
        response.put("limit", limit);

        return response;
    }
    @GetMapping("/complete")
    public String complete() {return "project/project_complete";}
}
