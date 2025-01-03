package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Job;
import com.hta2405.unite.domain.Project;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface ProjectService {
    public List<Emp> getHiredEmpByDeptId(long deptId);
    List<Map<Long, String>> getIdToJobNameMap();
    int createProject(Project projectDTO);
    void addProjectMember(int projectId, String memberName, String memberId, String role);
    void addProjectMembers(int projectId, String memberName, String memberId, String role);
    void createTask(int projectId, String empId, String memberName);
    int mainCountList(String userid, int favorite);
    int doneCountList(String userid, int finish, int cancel);
    List<Project> getmainList(String userid, int favorite, int page, int limit);
    List<Project> getDoneList(String userid, int page, int limit);
    void projectFavorite(int projectId);
    void projectColor(String userid, int projectId,String bgColor,String textColor);
    boolean updateProjectStatus(int projectId, String status);
}