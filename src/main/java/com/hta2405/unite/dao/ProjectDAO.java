package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.ProjectInfo;

public class ProjectDAO {
    private DataSource ds;

    public ProjectDAO() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }

    //사원번호로 사원정보찾기
    public List<Emp> getEmployeesByDepartment(int i) {
        List<Emp> empList = new ArrayList<>();
        String sql = "SELECT * FROM emp WHERE dept_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, i);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Emp emp = new Emp();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setEname(rs.getString("ename"));
                emp.setDeptId(rs.getLong("dept_id"));
                emp.setJobId(rs.getLong("job_id"));
                emp.setGender(rs.getString("gender"));
                emp.setEmail(rs.getString("email"));
                emp.setTel(rs.getString("tel"));
                emp.setMobile(rs.getString("mobile"));
                empList.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empList;
    }

    //사원번호가져오기
    public Long getDepartmentIdByName(String deptName) {
        Long deptId = null;
        String sql = "SELECT dept_id FROM dept WHERE dept_name = ?"; // "dept"로 변경

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deptName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                deptId = rs.getLong("dept_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deptId;
    }
    
    //프로젝트 생성
    public int createProject(String managerId, String projectName, String startDate, String endDate, String description) {
        String projectSql = "INSERT INTO project (project_id, manager_id, project_name, project_start_date, project_end_date, project_content) "
                          + "VALUES (project_seq.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(projectSql)) {

            pstmt.setString(1, managerId);
            pstmt.setString(2, projectName);
            pstmt.setString(3, startDate);
            pstmt.setString(4, endDate);
            pstmt.setString(5, description);

            pstmt.executeUpdate();

            // 두 번째 쿼리를 사용하여 새로 생성된 project_id를 가져오기
            String idSql = "SELECT project_seq.CURRVAL FROM dual";
            try (PreparedStatement idStmt = conn.prepareStatement(idSql);
                 ResultSet rs = idStmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1); // 새로 생성된 project_id 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 실패 시 -1 반환
    }

    //책임자
    public void addProjectMember(int projectId, String memberId, String role) {
        String memberSql = "INSERT INTO project_member (project_member_id, member_id, project_id, member_role, member_date) "
                         + "VALUES (project_member_seq.NEXTVAL, ?, ?, ?, SYSDATE)";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(memberSql)) {

            pstmt.setString(1, memberId);
            pstmt.setInt(2, projectId);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //여러명일때
    public void addProjectMembers(int projectId, String members, String role) {
        String memberSql = "INSERT INTO project_member (project_member_id, member_id, project_id, member_role, member_date) "
                         + "VALUES (project_member_seq.NEXTVAL, ?, ?, ?, SYSDATE)";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(memberSql)) {

            String[] memberArray = members.split(",");
            for (String member : memberArray) {
                pstmt.setString(1, member.trim()); // 공백 제거
                pstmt.setInt(2, projectId);
                pstmt.setString(3, role); // 역할 지정
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //성공하면 메인
    public List<ProjectInfo> getOngoingProjects() {
        List<ProjectInfo> projectList = new ArrayList<>();
        String sql = """
                SELECT 
                    p.project_id, 
                    p.project_name, 
                    p.project_end_date, 
                    COUNT(pm.member_id) AS member_count, 
                    AVG(pm.member_progress_rate) AS average_progress_rate  -- 평균 참여율을 계산
                FROM 
                    project p
                LEFT JOIN 
                    project_member pm ON p.project_id = pm.project_id
                WHERE 
                    p.project_finished = 0 
                    AND p.project_canceled = 0
                GROUP BY 
                    p.project_id, 
                    p.project_name, 
                    p.project_end_date
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ProjectInfo project = new ProjectInfo();
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setEndDate(rs.getDate("project_end_date"));
                project.setMemberCount(rs.getInt("member_count"));
                project.setProgressRate(rs.getDouble("average_progress_rate")); // 평균 참여율로 수정
                projectList.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
    }


} 