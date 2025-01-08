package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface ProjectBoardMapper {
    public boolean insertOrUpdate(String title, String content, String userid, int projectId, MultipartFile file);

    String getTaskCount(String userid, int projectId);

    int updateTask(String title, String content, String userid, int projectId, FileDTO taskFile);

    int insertTask(String title, String content, String userid, String name, int projectId, FileDTO taskFile);

    List<ProjectTaskDTO> getRecentPosts(int projectId);
}
