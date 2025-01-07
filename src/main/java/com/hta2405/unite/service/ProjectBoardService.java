package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.dto.ProjectDetailDTO;
import com.hta2405.unite.dto.ProjectRoleDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface ProjectBoardService {
    boolean insertOrUpdate(String title, String content, String userid, int projectId, MultipartFile file);
    List<ProjectTaskDTO> getRecentPosts(int projectId);
}