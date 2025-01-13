package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.ProjectTask;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface ProjectBoardMapper {
    public boolean insertOrUpdate(String title, String content, String userid, int projectId, MultipartFile file);

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
    int insertComment(String userid, int taskId, String content, int ref, int lev, int seq);
}
