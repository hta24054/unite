package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.mybatis.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectMapper dao;

    @Autowired
    public ProjectServiceImpl(ProjectMapper dao) {
        this.dao = dao;
    }

    public List<Emp> getHiredEmpByDeptId(long deptId) {
        return dao.getHiredEmpByDeptId(deptId);
    }

    @Override
    public List<Map<Long, String>> getIdToJobNameMap() {
        return dao.getIdToJobNameMap();
    }

    @Transactional
    public int createProject(Project project) {
        // 매니저 ID 처리: 괄호 안의 ID만 추출
        String managerInfo = project.getManagerId();
        String managerId = managerInfo.substring(managerInfo.indexOf("(") + 1, managerInfo.indexOf(")")).trim();
        String managername = managerInfo.replace("()", "").trim();
        project.setManagerName(managername);
        project.setManagerId(managerId);
        dao.createProject(project);

        return project.getProjectId();
    }


    @Override
    @Transactional
    public void addProjectMember(int projectId, String memberName, String memberId, String role) {
        dao.addProjectMember(projectId, memberName, memberId, role);
    }

    @Override
    @Transactional
    public void addProjectMembers(int projectId, String memberName, String memberId, String role) {
        dao.addProjectMembers(projectId, memberName, memberId, role);
    }

    @Override
    @Transactional
    public void createTask(int projectId, String empId, String memberName) {
        dao.createTask(projectId, empId, memberName);
    }

    public int mainCountList(String userid, int favorite) {
        return dao.mainCountList(userid, favorite);
    }
    public int doneCountList(String userid, int finish, int cancel){
        return dao.doneCountList(userid, finish, cancel);
    }
    @Override
    public List<Project> getmainList(String userid, int favorite, int page, int limit) {
        HashMap<String, Object> map = new HashMap<>();
        int startrow = (page - 1) * limit;
        int endrow = startrow + limit;
        map.put("favorite", favorite);
        map.put("userid", userid);
        map.put("start", startrow);
        map.put("end", endrow);
        return dao.getmainList(map);
    }

    public void projectFavorite(int projectId) {
        dao.projectFavorite(projectId);
    }
    public void projectColor(String userid, int projectId,String bgColor,String textColor){
        dao.projectColor(userid, projectId, bgColor, textColor);
    }

    @Override
    public boolean updateProjectStatus(int projectId, String status) {
        if ("completed".equals(status)) {
            return dao.projectStatus(projectId, 1, 0);
        } else if ("canceled".equals(status)) {
            return dao.projectStatus(projectId, 0, 1);
        }
        return false;
    }

    public List<Project> getDoneList(String userid, int page, int limit){
        HashMap<String, Object> map = new HashMap<>();
        int startrow = (page - 1) * limit;
        int endrow = startrow + limit;
        map.put("userid", userid);
        map.put("start", startrow);
        map.put("end", endrow);
        return dao.getDoneList(map);
    }
}
