package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.PaginationResult;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.dto.ProjectDetailDTO;
import com.hta2405.unite.dto.ProjectRoleDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.dto.ProjectTodoDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/project")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;
    private final EmpService empService;

    public ProjectController(ProjectService projectService, EmpService empService) {
        this.projectService = projectService;
        this.empService = empService;
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
            projectService.addProjectMember(projectId, name, project.getManagerId(), "MANAGER");
            projectService.createTask(projectId, project.getManagerId(), name);
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
    public Map<String, Object> getProjects(@AuthenticationPrincipal UserDetails user) {
        String userid = user.getUsername();
        List<Project> mainList = projectService.getmainList(userid);
        logger.info("mainList={}", mainList.size());
        Map<String, Object> response = new HashMap<>();
        response.put("boardlist", mainList);
        response.put("listCount", mainList.size());
        return response;
    }


    @PostMapping("/toggleFavorite")
    @ResponseBody
    public Map<String, Object> toggleFavorite(@RequestParam int projectId, @AuthenticationPrincipal UserDetails user) {
        String userid = user.getUsername();
        Map<String, Object> response = new HashMap<>();
        try {
            projectService.projectFavorite(projectId, userid);
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
            @RequestParam String textColor,
            @AuthenticationPrincipal UserDetails user) {
        String userid = user.getUsername();
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
    @ResponseBody
    public ModelAndView cancel(@AuthenticationPrincipal UserDetails user, ModelAndView mv, int dowhat) {
        mv.setViewName("project/project_cancel");
        mv.addObject("boardlist", projectService.getDoneList(user.getUsername(), dowhat));
        mv.addObject("memberName", empService.getEmpById(user.getUsername()).getEname());
        if (dowhat == 1) mv.addObject("message", "취소된 프로젝트들을 불러옵니다");
        if (dowhat == 2) mv.addObject("message", "완료된 프로젝트들을 불러옵니다");
        return mv;
    }
//    @GetMapping("/complete")
//    @ResponseBody
//    public ModelAndView complete(@AuthenticationPrincipal UserDetails user, ModelAndView mv, int status) {
//        mv.setViewName("project/project_complete");
//        mv.addObject("boardlist", projectService.getCompleteList(user.getUsername()));
//        mv.addObject("memberName", empService.getEmpById(user.getUsername()).getEname());
//        mv.addObject("message", "취소된 프로젝트들을 불러옵니다");
//        return mv;
//    }

    @GetMapping("/complete")
    public String complete() {
        return "project/project_complete";
    }

    @GetMapping("/detail")
    public ModelAndView detail(int projectId, ModelAndView mv, @AuthenticationPrincipal UserDetails user) {
        String userid = user.getUsername();

        String left = projectService.getProjectName(projectId);
        String projectContent = projectService.getProjectContent(projectId);
        List<ProjectDetailDTO> detail_Progress = projectService.getProjectDetail1(projectId, userid);
        for (ProjectDetailDTO projectDetail : detail_Progress) {
            if (projectDetail.getManagerId() != null && projectDetail.getManagerId().equals(userid)) {
                projectDetail.setIsManager(true);
            } else {
                projectDetail.setIsManager(false);
            }
        }
        List<ProjectTaskDTO> project = projectService.getProjectDetail2(projectId);
        List<ProjectRoleDTO> role = projectService.getRole(projectId, userid);

        mv.setViewName("project/project_detail");
        mv.addObject("left", left);
        mv.addObject("project", detail_Progress);
        mv.addObject("project2", project);
        mv.addObject("role", role);
        mv.addObject("projectId", projectId);
        mv.addObject("memberId", userid);
        mv.addObject("projectContent", projectContent);

        return mv;
    }

    @PostMapping("/updatetaskdesign")
    @ResponseBody
    public void updatetaskdesign(int projectId, String memberId, String taskContent, HttpServletResponse resp) throws IOException {
        boolean success = projectService.updateTaskContent(projectId, memberId, taskContent);
        sendJsonResponse(success, resp);
    }

    @PostMapping("/updateprogress")
    @ResponseBody
    public void updateprogress(int projectId, String memberId, int memberProgressRate, HttpServletResponse resp) throws IOException {
        boolean success = projectService.updateProgressRate(projectId, memberId, memberProgressRate);
        sendJsonResponse(success, resp);
    }

    private void sendJsonResponse(boolean success, HttpServletResponse resp) throws IOException {
        // JSON 응답 생성
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{");
        jsonResponse.append("\"success\":").append(success);
        jsonResponse.append("}");

        // 응답 타입 및 인코딩 설정
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // PrintWriter로 JSON 응답 보내기
        PrintWriter out = resp.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }

    @GetMapping("/todo")
    public String todo(int projectId, Model model, @AuthenticationPrincipal UserDetails user){
        String userid = user.getUsername();
        List<ProjectTodoDTO> todos = projectService.getTodoList(projectId, userid);
        model.addAttribute("projectName",projectService.getProjectName(projectId));
        model.addAttribute("todos", todos);
        model.addAttribute("projectId", projectId);
        return "project/Todo";
    }
    @PostMapping("/todoForm")
    public String todoForm(String task, int projectId, Model model, @AuthenticationPrincipal UserDetails user){
        String userid = user.getUsername();
        projectService.insertToDo(task, userid, projectId);
        model.addAttribute("projectId", projectId);
        return "redirect:/project/todo?projectId="+projectId;
    }
    @GetMapping("/todoList")
    @ResponseBody
    public List<ProjectTodoDTO> getTodoList(@RequestParam("projectId") int projectId,
                                            @RequestParam("memberId") String memberId) {
        // 해당 프로젝트와 멤버에 맞는 투두리스트 가져오기
        List<ProjectTodoDTO> todos = projectService.getTodoList(projectId, memberId);
        return todos;  // JSON 형태로 자동 변환되어 클라이언트로 반환됨
    }
    @PostMapping("/todoprogress")
    @ResponseBody
    public void todoprogress(int projectId, int todoId, int memberProgressRate, HttpServletResponse resp, @AuthenticationPrincipal UserDetails user) throws IOException {
        String userid = user.getUsername();
        boolean success = projectService.todoProgressRate(projectId, userid, todoId, memberProgressRate);
        sendJsonResponse(success, resp);
    }
    @PostMapping("/updateTodo")
    public void updateTodo(int projectId, int todoId, String newSubject, @AuthenticationPrincipal UserDetails user, HttpServletResponse resp) throws IOException {
        String userid = user.getUsername();
        boolean success = projectService.todoUpdate(projectId, userid, todoId, newSubject);
        sendJsonResponse(success, resp);
    }
    @PostMapping("/deleteTodo")
    public void deleteTodo(int todoId, HttpServletResponse resp) throws IOException {
        boolean success = projectService.deleteTodo(todoId);
        sendJsonResponse(success, resp);
    }
}
