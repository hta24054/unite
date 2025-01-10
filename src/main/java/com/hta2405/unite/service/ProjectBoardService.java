package com.hta2405.unite.service;

import com.hta2405.unite.domain.ProjectTask;
import com.hta2405.unite.dto.ProjectTaskDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProjectBoardService {
    boolean insertOrUpdate(String title, String content, String userid, int projectId, MultipartFile file, String category);
    List<ProjectTaskDTO> getRecentPosts(int projectId);
    List<ProjectTask> getTaskList(int projectId, String userid);
    List<ProjectTask> getTask(int projectId, String userid, int taskId);
    boolean deleteTask(int projectId, String memberId, int taskId);
    void fileDown(String fileuuid, String originalFilename, HttpServletResponse response);
    void modifyProcess(int projectId, String memberId, int taskId, MultipartFile file, String board_subject, String board_content);
}