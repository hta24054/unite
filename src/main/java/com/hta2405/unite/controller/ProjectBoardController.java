package com.hta2405.unite.controller;

import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProjectBoardService;
import com.hta2405.unite.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projectBoard")
@Slf4j
public class ProjectBoardController {
    private final ProjectService projectService;
    private final ProjectBoardService projectBoardService;
    private final EmpService empService;

    public ProjectBoardController(ProjectBoardService projectBoardService, ProjectService projectService, EmpService empService) {
        this.projectBoardService = projectBoardService;
        this.projectService = projectService;
        this.empService = empService;
    }

    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> write(int projectId, String title, String content, MultipartFile file, @AuthenticationPrincipal UserDetails user) {
        String userId = user.getUsername();

        if (file == null || file.isEmpty()) {
            System.out.println("첨부 파일이 없습니다.");
        }

        projectBoardService.insertOrUpdate(title, content, userId, projectId, file);

        List<ProjectTaskDTO> recentPosts = projectBoardService.getRecentPosts(projectId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("posts", recentPosts);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/lists")
//    @ResponseBody
//    public Map<String, Object> list(@RequestParam int projectId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userid = authentication.getName();
//
//        List<ProjectTask> taskList = projectBoardService.getTaskList(projectId, userid);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("boardlist", taskList);
//        response.put("projectName", projectService.getProjectName(projectId));
//
//        return response; // JSON 형식으로 반환
//    }
    @GetMapping("/list")
    public ModelAndView tasklist(ModelAndView mv, int projectId, String memberId) {
        mv.addObject("projectName", projectService.getProjectName(projectId));
        mv.addObject("memberName", empService.getEmpById(memberId).getEname());
        mv.addObject("boardlist", projectBoardService.getTaskList(projectId, memberId));
        mv.addObject("message", "진행 과정 게시판");
        mv.setViewName("project/project_task_list");
        return mv;
    }

}
