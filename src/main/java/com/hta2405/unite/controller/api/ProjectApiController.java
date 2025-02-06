package com.hta2405.unite.controller.api;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.dto.ProjectTodoDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProjectBoardService;
import com.hta2405.unite.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@Slf4j
public class ProjectApiController {
    private final ProjectService projectService;
    private final EmpService empService;
    private final ProjectBoardService projectBoardService;

    public ProjectApiController(ProjectService projectService, EmpService empService, ProjectBoardService projectBoardService) {
        this.projectService = projectService;
        this.empService = empService;
        this.projectBoardService = projectBoardService;
    }

    @GetMapping(value = "/empTree")
    public Map<String, Object> empTree(@RequestParam("deptId") long deptId) {
        List<Emp> empList = projectService.getHiredEmpByDeptId(deptId);
        List<Map<Long, String>> jobName = projectService.getIdToJobNameMap();
        log.info("empList = ", empList.size());
        log.info("jobName = ", jobName.size());
        Map<String, Object> map = new HashMap<>();
        map.put("empList", empList);
        map.put("jobName", jobName);
        return map;
    }

    @GetMapping("/getProjects")
    public Map<String, Object> getProjects(@AuthenticationPrincipal UserDetails user) {
        String userid = user.getUsername();
        List<Project> mainList = projectService.getmainList(userid);
        log.info("mainList={}", mainList.size());
        Map<String, Object> response = new HashMap<>();
        response.put("boardlist", mainList);
        response.put("listCount", mainList.size());
        return response;
    }


    @PostMapping("/toggleFavorite")
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

    @PostMapping("/updateStatus")
    public Map<String, Object> updateProjectStatus(@RequestParam("projectId") int projectId,
                                                   @RequestParam("status") String status,
                                                   MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isUpdated = projectService.updateProjectStatus(projectId, status, file);

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

    @GetMapping("/done")
    public ModelAndView done(@AuthenticationPrincipal UserDetails user, ModelAndView mv, int state) {
        mv.setViewName("project/endProject");
        mv.addObject("boardlist", projectService.getDoneList(user.getUsername(), state));
        mv.addObject("memberName", empService.getEmpById(user.getUsername()).getEname());
        mv.addObject("state", state);
        if (state == 1) {
            mv.addObject("doneName", "취소");
            mv.addObject("message", "취소된 프로젝트들을 불러옵니다");
        }
        if (state == 2) {
            mv.addObject("doneName", "완료");
            mv.addObject("message", "완료된 프로젝트들을 불러옵니다");
        }
        return mv;
    }

    @PostMapping("/updatetaskdesign")
    public void updatetaskdesign(int projectId, String memberId, String taskContent, HttpServletResponse resp) throws IOException {
        boolean success = projectService.updateTaskContent(projectId, memberId, taskContent);
        sendJsonResponse(success, resp);
    }

    @PostMapping("/updateprogress")
    public void updateprogress(int projectId, String memberId, int memberProgressRate, HttpServletResponse resp) throws IOException {
        boolean success = projectService.updateProgressRate(projectId, memberId, memberProgressRate);
        sendJsonResponse(success, resp);
    }

    public void sendJsonResponse(boolean success, HttpServletResponse resp) throws IOException {
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

    @GetMapping("/todoList")
    public List<ProjectTodoDTO> getTodoList(@RequestParam("projectId") int projectId,
                                            @RequestParam("memberId") String memberId) {
        List<ProjectTodoDTO> todos = projectService.getTodoList(projectId, memberId);
        return todos;
    }
    @PostMapping("/todoprogress")
    public void todoprogress(int projectId, int todoId, int memberProgressRate, HttpServletResponse resp, @AuthenticationPrincipal UserDetails user) throws IOException {
        String userid = user.getUsername();
        boolean success = projectService.todoProgressRate(projectId, userid, todoId, memberProgressRate);
        sendJsonResponse(success, resp);
    }

    @PostMapping("/write")
    public ResponseEntity<Map<String, Object>> write(int projectId, String title, String content, MultipartFile file, String category, @AuthenticationPrincipal UserDetails user) {
        String userId = user.getUsername();

        if (file == null || file.isEmpty()) {
            System.out.println("첨부 파일이 없습니다.");
        }

        projectBoardService.insertOrUpdate(title, content, userId, projectId, file, category);

        List<ProjectTaskDTO> recentPosts = projectBoardService.getRecentPosts(projectId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("posts", recentPosts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/color")
    public ResponseEntity<Map<String, String>> getProjectColor(@RequestParam int projectId, @RequestParam String memberId) {
        Map<String, String> colorInfo = projectService.getColor(projectId, memberId);
        return ResponseEntity.ok(colorInfo);
    }

    @PostMapping("/updateTodoOrder")
    public String updateTodoOrder(@RequestParam List<Long> orderedIds, int projectId, @AuthenticationPrincipal UserDetails user) {
        log.info("--------------------hello={}", projectId);
        projectService.updateTodoOrder(orderedIds, projectId, user.getUsername());
        return "success";
    }
}
