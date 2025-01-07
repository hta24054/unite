package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.PaginationResult;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProjectDetailDTO;
import com.hta2405.unite.dto.ProjectRoleDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.mybatis.mapper.ProjectBoardMapper;
import com.hta2405.unite.mybatis.mapper.ProjectMapper;
import com.hta2405.unite.service.ProjectBoardService;
import com.hta2405.unite.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projectBoard")
@Slf4j
public class ProjectBoardController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectBoardController.class);

    private final ProjectBoardService projectBoardService;

    public ProjectBoardController(ProjectBoardService projectBoardService) {
        this.projectBoardService = projectBoardService;
    }

    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> write(int projectId, String title, String content, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

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

    @GetMapping("/list")
    public String list() {
        return "project/project_task_list";
    }


}
