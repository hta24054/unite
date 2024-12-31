package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Job;
import com.hta2405.unite.domain.Project;
import com.hta2405.unite.mybatis.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectMapper dao;

    @Autowired
    public ProjectServiceImpl(ProjectMapper dao) {
        this.dao = dao;
    }
    public List<Emp> getHiredEmpByDeptId(long deptId){
        return dao.getHiredEmpByDeptId(deptId);
    };

    @Override
    public List<Map<Long, String>> getIdToJobNameMap() {
        return dao.getIdToJobNameMap();
    }

    @Transactional
    public int createProject(Project project) {
        // 매니저 ID 처리: 괄호 안의 ID만 추출
        String managerInfo = project.getManager_id();
        String managerId = managerInfo.substring(managerInfo.indexOf("(") + 1, managerInfo.indexOf(")")).trim();
        String managername = managerInfo.replace("()", "").trim();
        project.setManager_name(managername);
        project.setManager_id(managerId);
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

}
