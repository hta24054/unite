package com.hta2405.unite.service;

import com.hta2405.unite.dto.ProjectTaskDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProjectBoardService {
    boolean insertOrUpdate(String title, String content, String userid, int projectId, MultipartFile file);

    List<ProjectTaskDTO> getRecentPosts(int projectId);
}