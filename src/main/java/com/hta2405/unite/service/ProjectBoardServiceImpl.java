package com.hta2405.unite.service;

import com.hta2405.unite.domain.ProjectTask;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import com.hta2405.unite.mybatis.mapper.ProjectBoardMapper;
import com.hta2405.unite.util.ConfigUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProjectBoardServiceImpl implements ProjectBoardService {
    private static final String FILE_DIR = ConfigUtil.getProperty("project.upload.directory");
    private final ProjectBoardMapper dao;
    private final EmpMapper empMapper;
    private final FileService fileService;

    public ProjectBoardServiceImpl(FileService fileService,ProjectBoardMapper dao, EmpMapper empMapper) {
        this.dao = dao;
        this.fileService = fileService;
        this.empMapper = empMapper;
    }
    public boolean insertOrUpdate(String title, String content, String userId, int projectId, MultipartFile file, String category) {
        String isNull = dao.getTaskCount(userId, projectId);
        String name = empMapper.getEmpById(userId).getEname();
        int num;
        FileDTO taskFile = null;
        if (file != null && !file.isEmpty()) {
            taskFile = fileService.uploadFile(file, FILE_DIR);
        }
        if (isNull == null)  num = dao.updateTask(title, content, userId, projectId, taskFile, category);
        else num = dao.insertTask(title, content, userId, name, projectId, taskFile, category);

        return num == 1;
    }

    public List<ProjectTaskDTO> getRecentPosts(int projectId) {
        return dao.getRecentPosts(projectId);
    }

    public List<ProjectTask> getTaskList(int projectId, String userid){
        return dao.getTaskList(projectId, userid);
    }


}
