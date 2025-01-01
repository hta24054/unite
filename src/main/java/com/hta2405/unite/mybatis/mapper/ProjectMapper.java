package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Job;
import com.hta2405.unite.domain.Project;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectMapper {
    public List<Emp> getHiredEmpByDeptId(long deptId); //글의 갯수 구하기
    List<Map<Long, String>> getIdToJobNameMap();
    int createProject(Project project);
    void addProjectMember(int projectId, String memberName, String memberId, String role);
    void addProjectMembers(int projectId, String memberName, String memberId, String role);
    void createTask(int projectId, String empId, String memberName);
}
