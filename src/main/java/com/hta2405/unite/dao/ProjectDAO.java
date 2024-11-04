package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.ProjectComplete;
import com.hta2405.unite.dto.ProjectDetail;
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

    //책임자(project_
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
    
    //성공하면 메인(나중에 다시 확인 필요.중복확인)
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
                order by p.project_id desc
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
    
    public boolean updateProjectStatus(int projectId, boolean finished, boolean canceled) {
        String sql = "UPDATE project SET project_finished = ?, project_canceled = ?, project_end_date = ? WHERE project_id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, finished ? 1 : 0);
            pstmt.setInt(2, canceled ? 1 : 0);
            // 취소된 경우 현재 날짜를 설정, 그렇지 않으면 NULL로 설정
            if (canceled) {
                pstmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }
            pstmt.setInt(4, projectId);
            
            int updatedRows = pstmt.executeUpdate();
            return updatedRows > 0; // 성공 여부 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //완료 프로젝트(ProjectCompleteAction. project_main에서 관리자가 완료한 프로젝트. project_complete.jsp에서 뜨는)
    public List<ProjectComplete> getCompletedProjects() {
        List<ProjectComplete> completedProjects = new ArrayList<>();
        String sql = """
                SELECT p.project_id, p.project_name, m.member_id, e.ename, m.member_role, p.project_start_date, p.project_end_date, p.project_file_path FROM project p 
                JOIN project_member m ON p.project_id = m.project_id
                JOIN emp e ON m.member_id = e.ename
                WHERE p.project_finished = 1
                order by p.project_id desc
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            Map<Integer, ProjectComplete> projectMap = new HashMap<>();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");
                ProjectComplete project = projectMap.get(projectId);

                if (project == null) {
                    project = new ProjectComplete();
                    project.setProjectId(projectId);
                    project.setProjectName(rs.getString("project_name"));
                    project.setProjectStartDate(rs.getDate("project_start_date"));
                    project.setProjectEndDate(rs.getDate("project_end_date"));
                    project.setProjectFilePath(rs.getString("project_file_path"));
                    project.setParticipantNames(new ArrayList<>());
                    projectMap.put(projectId, project);
                }

                // 책임자 또는 참여자 추가
                String memberRole = rs.getString("member_role");
                String empName = rs.getString("ename");

                if ("MANAGER".equalsIgnoreCase(memberRole)) {
                    project.setEmpName(empName); // 책임자 설정
                } else {
                    project.getParticipantNames().add(empName); // 참여자 추가
                }
            }

            completedProjects.addAll(projectMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedProjects;
    }

    //취소 프로젝트(ProjectCancelAction. project_main에서 관리자가 취소한 프로젝트. project_cancel.jsp에서 뜨는) 
    public List<ProjectComplete> getCancelProjects() {
    	List<ProjectComplete> cancelProjects = new ArrayList<>();
        String sql = """
                SELECT p.project_id, p.project_name, m.member_id, e.ename, m.member_role, p.project_start_date, p.project_end_date, p.project_file_path FROM project p 
                JOIN project_member m ON p.project_id = m.project_id
                JOIN emp e ON m.member_id = e.ename
                WHERE p.project_canceled = 1
                order by p.project_id desc
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            Map<Integer, ProjectComplete> projectMap = new HashMap<>();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");
                ProjectComplete project = projectMap.get(projectId);

                if (project == null) {
                    project = new ProjectComplete();
                    project.setProjectId(projectId);
                    project.setProjectName(rs.getString("project_name"));
                    project.setProjectStartDate(rs.getDate("project_start_date"));
                    project.setProjectEndDate(rs.getDate("project_end_date"));
                    project.setProjectFilePath(rs.getString("project_file_path"));
                    project.setParticipantNames(new ArrayList<>());
                    projectMap.put(projectId, project);
                }

                // 책임자 또는 참여자 추가
                String memberRole = rs.getString("member_role");
                String empName = rs.getString("ename");

                if ("MANAGER".equalsIgnoreCase(memberRole)) {
                    project.setEmpName(empName); // 책임자 설정
                } else {
                    project.getParticipantNames().add(empName); // 참여자 추가
                }
            }

            cancelProjects.addAll(projectMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cancelProjects;
	}
    
    //프로젝트 ID에 따른 상세 정보를 가져오는 메서드 추가(project
    public List<ProjectDetail> getProjectDetails(int projectId) {
        List<ProjectDetail> project = new ArrayList<>(); 
        String sql = """
                select e.ename, m.member_designated, m.member_progress_rate
                from project p join project_member m on p.project_id = m.project_id
                join emp e on m.member_id = e.ename
                where p.project_id = ?
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId); // 프로젝트 ID 설정
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                ProjectDetail projectDetail = new ProjectDetail();
                projectDetail.setParticipantNames(rs.getString(1));
                projectDetail.setMemberDesign(rs.getString(2));
                projectDetail.setMemberProgressRate(rs.getInt(3));
                project.add(projectDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project; // 프로젝트 정보 반환
    }

	

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    /*글 개수 -- project_main 밑에 하단 글 개수
	public int getListCount() {
		String sql = "select count(*) from project";
		int x = 0; 
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) x = rs.getInt(1);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러 : " + ex);
		}
		return x;
	}

	public List<Project> getBoardList(int page, int limit) {
		String board_list_sql = """
				select * from 
					(select rownum rnum, j.* from
						(select project.*, nvl(cnt, 0) as cnt from project
					where rownum <= ?) 
				where rnum>=? and rnum <= ?
				""";
		List<Project> list = new ArrayList<Project>();
		
		//한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ....
		int startrow = (page -1) * limit + 1; //읽기 시작할 row 번호(1  11  21 31 ...)
		int endrow = startrow + limit -1; //읽을 마지막 row 번호(10 20 30 40 ...)
		
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(board_list_sql);){
			pstmt.setInt(1, endrow);
			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					Project project = new Project();
					project.setProject_id(rs.getInt("project_id"));
					project.setManager_id(rs.getString("manager_id"));
					project.setProject_name(rs.getString("project_name"));
					project.setProject_start_date(rs.getDate("project_start_date"));
					project.setProject_end_date(rs.getDate("project_end_date"));
					project.setProject_content(rs.getString("project_content"));
					project.setProject_file_path(rs.getString("project_file_path"));
					project.setProject_file_original(rs.getString("project_file_original"));
					project.setProject_file_uuid(rs.getString("project_file_uuid"));
					project.setProject_file_type(rs.getString("project_file_type"));
					project.setProject_finished(rs.getInt("project_finished"));
					project.setProject_canceled(rs.getInt("project_canceled"));
					
					
					list.add(project);
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("getBoardList()에러"+ex);
		}
		return list;
	}*/


} 