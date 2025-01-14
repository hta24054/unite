package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.ProjectComment;
import com.hta2405.unite.domain.ProjectTask;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectBoardMapper {
    String getTaskCount(String userid, int projectId);
    int updateTask(String title, String content, String userid, int projectId, FileDTO taskFile, String category);
    int insertTask(String title, String content, String userid, String name, int projectId, FileDTO taskFile, String category);
    List<ProjectTaskDTO> getRecentPosts(int projectId);
    List<ProjectTask> getTaskList(int projectId, String userid);
    List<ProjectTask> getTask(int projectId, String userid, int taskId);
    boolean deleteTask(int projectId, String memberId, int taskId);
    void modifyProcess(int projectId, String memberId, int taskId, FileDTO taskFile, String board_subject, String board_content);
    int getTaskCommentId(int taskId);
    int getParentSeq(int taskId, int parentCommentId);
    int getMaxSeq(int taskId, int ref);
    int insertComment(ProjectComment.ProjectCommentBuilder projectCommentBuilder);
    int replyAdd(ProjectComment.ProjectCommentBuilder projectCommentBuilder);
    int updateRef(long ref);
    int getListCount(int taskId);
    List<ProjectComment> getCommentList(int taskId, int state);
    int updateComment(ProjectComment.ProjectCommentBuilder projectCommentBuilder);
    int deleteComment(int taskId);
}
