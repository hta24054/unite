package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectMapper {
    List<Emp> getHiredEmpByDeptId(long deptId); //글의 갯수 구하기
    List<Map<Long, String>> getIdToJobNameMap();
    int createProject(Project project);
    void addProjectMember(int projectId, String memberName, String memberId, String role);
    void addProjectMembers(int projectId, String memberName, String memberId, String role);
    void createTask(int projectId, String empId, String memberName);
    int mainCountList(String userid, int favorite);
    int doneCountList(String userid, int finish, int cancel);
    List<Project> getmainList(HashMap<String, Object> map);
    List<Project> getDoneList(HashMap<String, Object> map);
    void projectFavorite(int projectId);
    void projectColor(String userid, int projectId, String bgColor,String textColor);
    boolean projectStatus(int projectId, int complete, int cancel);
}
