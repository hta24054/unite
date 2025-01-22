package com.hta2405.unite.controller;

import com.hta2405.unite.controller.api.ProjectApiController;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.dto.ProjectDetailDTO;
import com.hta2405.unite.dto.ProjectRoleDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.dto.ProjectTodoDTO;
import com.hta2405.unite.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/project")
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectApiController projectApiController;

    public ProjectController(ProjectService projectService, ProjectApiController projectApiController) {
        this.projectService = projectService;
        this.projectApiController = projectApiController;
    }

    @GetMapping(value = "/main")
    public String main() {
        return "project/project_main";
    }

    @GetMapping(value = "/create")
    public String create() {
        return "project/project_create";
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute Project project, Model model) {
        try {
            projectService.createProject(project);
            model.addAttribute("message", "프로젝트가 성공적으로 생성되었습니다.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "redirect:/project/create";
        }
        return "redirect:/project/main";
    }


    @PostMapping("/saveProjectColor")
    public String saveColorSettings(
            @RequestParam int projectId,
            @RequestParam String bgColor,
            @RequestParam String textColor,
            @AuthenticationPrincipal UserDetails user) {
        String userid = user.getUsername();
        projectService.projectColor(userid, projectId, bgColor, textColor);
        return "redirect:/project/main";
    }

    @GetMapping("/complete")
    public String complete() {
        return "project/project_complete";
    }

    @GetMapping("/detail")
    public ModelAndView detail(int projectId, ModelAndView mv, @AuthenticationPrincipal UserDetails user) {
        String userid = user.getUsername();

        List<ProjectDetailDTO> detail_Progress = projectService.getProjectDetail1(projectId, userid);
        for (ProjectDetailDTO projectDetail : detail_Progress) {
            if (projectDetail.getManagerId() != null && projectDetail.getManagerId().equals(userid)) projectDetail.setIsManager(true);
            else projectDetail.setIsManager(false);
        }

        mv.setViewName("project/project_detail");
        mv.addObject("left", projectService.getProjectName(projectId));
        mv.addObject("project", detail_Progress);
        mv.addObject("projectContent", projectService.getProjectContent(projectId));
        mv.addObject("project2", projectService.getProjectDetail2(projectId));
        mv.addObject("role", projectService.getRole(projectId, userid));
        mv.addObject("projectId", projectId);
        mv.addObject("memberId", userid);
        return mv;
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

    @PostMapping("/updateTodo")
    public void updateTodo(int projectId, int todoId, String newSubject, @AuthenticationPrincipal UserDetails user, HttpServletResponse resp) throws IOException {
        String userid = user.getUsername();
        boolean success = projectService.todoUpdate(projectId, userid, todoId, newSubject);
        projectApiController.sendJsonResponse(success, resp);
    }

    @PostMapping("/deleteTodo")
    public void deleteTodo(int todoId, HttpServletResponse resp) throws IOException {
        boolean success = projectService.deleteTodo(todoId);
        projectApiController.sendJsonResponse(success, resp);
    }
}
