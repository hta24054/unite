package com.hta2405.unite.service;

import com.hta2405.unite.domain.ProjectComment;
import com.hta2405.unite.domain.ProjectTask;
import com.hta2405.unite.dto.ProjectTaskDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface ProjectBoardService {
    boolean insertOrUpdate(String title, String content, String userid, int projectId, MultipartFile file, String category);
    List<ProjectTaskDTO> getRecentPosts(int projectId);
    List<ProjectTask> getTaskList(int projectId, String userid);
    List<ProjectTask> getTask(int projectId, String userid, int taskId);
    boolean deleteTask(int projectId, String memberId, int taskId);
    ResponseEntity<Resource> fileDown(String fileuuid, String originalFilename);
    void modifyProcess(int projectId, String memberId, int taskId, MultipartFile file, String board_subject, String board_content);
    int commentAdd(ProjectComment.ProjectCommentBuilder projectCommentBuilder);
    int replyAdd(ProjectComment.ProjectCommentBuilder projectCommentBuilder);
    int getListCount(int taskId);
    List<ProjectComment> getCommentList(int taskId, int state);
    int updateComment(ProjectComment.ProjectCommentBuilder projectCommentBuilder);
    int deleteComment(int taskId);
    Map<Long, Long> commentCount(int projectId, String userid);
}