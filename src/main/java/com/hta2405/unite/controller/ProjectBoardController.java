package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.LoginDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProjectBoardService;
import com.hta2405.unite.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView prepareModelAndView(int projectId, String memberId, Integer taskId, String viewName) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectName", projectService.getProjectName(projectId));
        mv.addObject("projectId", projectId);
        mv.addObject("memberName", empService.getEmpById(memberId).getEname());
        mv.addObject("memberId", memberId);

        if (taskId != null) {
            mv.addObject("task", projectBoardService.getTask(projectId, memberId, taskId).get(0));
        }

        mv.setViewName(viewName);
        return mv;
    }

    @GetMapping("/list")
    public ModelAndView tasklist(int projectId, String memberId) {
        ModelAndView mv = prepareModelAndView(projectId, memberId, null, "project/project_task_list");
        mv.addObject("boardlist", projectBoardService.getTaskList(projectId, memberId));
        mv.addObject("message", "진행 과정 게시판");
        return mv;
    }

    @GetMapping("/comm")
    public ModelAndView comm(int projectId, String memberId, int taskId) {
        return prepareModelAndView(projectId, memberId, taskId, "project/project_comm");
    }

    @GetMapping("/modify")
    public ModelAndView modify(int projectId, String memberId, int taskId) {
        return prepareModelAndView(projectId, memberId, taskId, "project/project_modify");
    }

    @PostMapping("/delete")
    public String delete(int projectId, String memberId, int taskId, String board_pass, RedirectAttributes redirectAttributes) {
        Emp emp = empService.getEmpById(memberId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(board_pass, emp.getPassword())) {
            redirectAttributes.addFlashAttribute("delete_error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/projectBoard/list?projectId=" + projectId + "&memberId=" + memberId;
        }

        boolean isDeleted = projectBoardService.deleteTask(projectId, memberId, taskId);

        if (isDeleted) redirectAttributes.addFlashAttribute("delete_message", "삭제가 성공적으로 완료되었습니다.");
        else redirectAttributes.addFlashAttribute("delete_error", "삭제에 실패했습니다. 다시 시도해주세요.");

        return "redirect:/projectBoard/list?projectId=" + projectId + "&memberId=" + memberId;
    }

    @GetMapping("/down")
    public void filedown(String fileuuid, String originalFilename, HttpServletResponse response) throws Exception{
        projectBoardService.fileDown(fileuuid, originalFilename, response);
    }


    @PostMapping("/modifyProcess")
    public String modifyProcess(ModelAndView mv, int projectId, String memberId, int taskId, MultipartFile file, @AuthenticationPrincipal UserDetails user) {
        String userId = user.getUsername();

        return "redirect:/projectBoard/comm?projectId=" + projectId + "&memberId=" + memberId + "&taskId=" + taskId;
    }

}
