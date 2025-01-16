package com.hta2405.unite.service;

import com.hta2405.unite.domain.ProjectComment;
import com.hta2405.unite.domain.ProjectTask;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import com.hta2405.unite.mybatis.mapper.ProjectBoardMapper;
import com.hta2405.unite.util.ConfigUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProjectBoardServiceImpl implements ProjectBoardService {
    private static final String FILE_DIR = ConfigUtil.getProperty("project.upload.directory");
    private final ProjectBoardMapper dao;
    private final EmpMapper empMapper;
    private final FileService fileService;

    public ProjectBoardServiceImpl(FileService fileService, ProjectBoardMapper dao, EmpMapper empMapper) {
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

    public List<ProjectTask> getTask(int projectId, String userid, int taskId){
        return dao.getTask(projectId, userid, taskId);
    }

    public boolean deleteTask(int projectId, String memberId, int taskId){
        List<ProjectTask> taskInfo = dao.getTask(projectId, memberId, taskId);
        ProjectTask currentTask = taskInfo.get(0);
        String uuid = currentTask.getTaskFileUuid();
        if(uuid != null) fileService.deleteFile(uuid, FILE_DIR, currentTask.getTaskFileOriginal());
        return dao.deleteTask(projectId, memberId, taskId);
    }

    public ResponseEntity<Resource> fileDown(String fileuuid, String originalFilename){
        if (fileuuid == null || fileuuid.isEmpty()) {
            return fileService.downloadFile(FILE_DIR, fileuuid, originalFilename);
        } else {
            return fileService.downloadFile(FILE_DIR, fileuuid, originalFilename);
        }
    }
//    public void modifyProcess(int projectId, String memberId, int taskId, MultipartFile file, String board_subject, String board_content){
//        List<ProjectTask> taskInfo = dao.getTask(projectId, memberId, taskId);
//        String uuid = taskInfo.get(0).getTaskFileUuid();
//        FileDTO taskFile = null;
//        if (file != null && !file.isEmpty()) {
//            taskFile = fileService.uploadFile(file, FILE_DIR);
//        }
//        dao.modifyProcess(projectId, memberId, taskId, taskFile, board_subject, board_content);
//    }
public void modifyProcess(int projectId, String memberId, int taskId, MultipartFile file, String board_subject, String board_content) {
    List<ProjectTask> taskInfo = dao.getTask(projectId, memberId, taskId);
    if (taskInfo.isEmpty()) {
        throw new IllegalArgumentException("Task 정보가 존재하지 않습니다.");
    }

    // 현재 Task의 파일 정보 가져오기
    ProjectTask currentTask = taskInfo.get(0);
    String uuid = currentTask.getTaskFileUuid();
    FileDTO taskFile = null;

    if (uuid == null && file != null && !file.isEmpty()) {
        taskFile = fileService.uploadFile(file, FILE_DIR);
    } else if (uuid != null && (file == null || file.isEmpty())) {
        fileService.deleteFile(uuid, FILE_DIR, currentTask.getTaskFileOriginal());
    } else if (uuid != null && file != null && !file.isEmpty()) {
        fileService.deleteFile(uuid, FILE_DIR, currentTask.getTaskFileOriginal());
        taskFile = fileService.uploadFile(file, FILE_DIR);
    }

    // DB 업데이트 실행
    dao.modifyProcess(projectId, memberId, taskId, taskFile, board_subject, board_content);
}


    public int commentAdd(ProjectComment.ProjectCommentBuilder projectCommentBuilder){
        int insert = dao.insertComment(projectCommentBuilder);
        int update = dao.updateRef(projectCommentBuilder.build().getTaskCommentId());
        return insert > 0 && update > 0 ? 1 : 0;
    }
    public int replyAdd(ProjectComment.ProjectCommentBuilder projectCommentBuilder){
        return dao.replyAdd(projectCommentBuilder);
    }
    public int getListCount(int taskId){
        return dao.getListCount(taskId);
    }

    public List<ProjectComment> getCommentList(int taskId, int state){
        return dao.getCommentList(taskId, state);
    }

    public int updateComment(ProjectComment.ProjectCommentBuilder projectCommentBuilder){
        return dao.updateComment(projectCommentBuilder);
    }

    public int deleteComment(int taskId){
        return dao.deleteComment(taskId);
    }
}
