package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<Emp> getEmployeesByDepartment(int deptId) {
        List<Emp> empList = new ArrayList<>();
        String sql = "SELECT emp_id, ename, e.dept_id, gender, email, tel, mobile, "
                + "j.job_name, d.dept_name "
                + "FROM emp e "
                + "JOIN job j ON e.job_id = j.job_id "
                + "JOIN dept d ON e.dept_id = d.dept_id "
                + "WHERE e.dept_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deptId);  // deptId는 int 타입으로 전달
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Emp emp = new Emp();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setEname(rs.getString("ename"));
                emp.setDeptId(rs.getLong("dept_id"));  // dept_id를 int로 받아 설정
                emp.setSchool(rs.getString("job_name"));  // job_name을 String으로 설정
                emp.setGender(rs.getString("gender"));
                emp.setEmail(rs.getString("email"));
                emp.setTel(rs.getString("tel"));
                emp.setMobile(rs.getString("dept_name"));
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
    public String getEmpIdByEname(String ename) {
        String empId = null;
        String sql = "SELECT emp_id FROM emp WHERE ename = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ename); // ename을 사용하여 emp_id를 조회

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    empId = rs.getString("emp_id"); // emp_id 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empId; // emp_id가 없으면 null 반환
    }

    // 프로젝트 생성
    public int createProject(String managerId, String projectName, String startDate, String endDate, String description) {
        String projectSql = "INSERT INTO project (manager_id, project_name, project_start_date, project_end_date, project_content) "
                          + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(projectSql)) {

            pstmt.setString(1, managerId);
            pstmt.setString(2, projectName);
            pstmt.setString(3, startDate);
            pstmt.setString(4, endDate);
            pstmt.setString(5, description);

            pstmt.executeUpdate();

            // 두 번째 쿼리를 사용하여 새로 생성된 project_id를 가져오기
            String idSql = "SELECT SEQ_PROJECT.CURRVAL FROM dual";
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

    // 책임자 추가
    public void addProjectMember(int projectId, String memberId, String role) {
        String memberSql = "INSERT INTO project_member (member_id, project_id, member_role, member_date) "
                         + "VALUES (?, ?, ?, SYSDATE)";

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

    // 여러명일 때 추가
    public void addProjectMembers(int projectId, String members, String role) {
        String memberSql = "INSERT INTO project_member (member_id, project_id, member_role, member_date) "
                         + "VALUES (?, ?, ?, SYSDATE)";

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

    // 작업 생성
    public void createTask(int projectId, String empId) {
        String taskSql = "INSERT INTO task (emp_id, project_id) "
                       + "VALUES (?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(taskSql)) {
            pstmt.setString(1, empId);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //프로젝트 이름 가져오기(left_bar)
    public String getProjectName(int projectId) {
    	String sql = "select project_name from project where project_id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId); // projectId를 SQL 문의 ?에 설정
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("project_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 해당 ID의 프로젝트가 없을 경우
    }
    
    public boolean updateProjectStatus(int projectId, boolean finished, boolean canceled) {
        String getEndDateSql = "SELECT project_end_date FROM project WHERE project_id = ?";
        String updateSql = "UPDATE project SET project_finished = ?, project_canceled = ?, project_end_date = ? WHERE project_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement getEndDatePstmt = conn.prepareStatement(getEndDateSql);
             PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {

            // 기존 종료 날짜 조회
            getEndDatePstmt.setInt(1, projectId);
            ResultSet rs = getEndDatePstmt.executeQuery();

            java.sql.Date existingEndDate = null;
            if (rs.next()) {
                existingEndDate = rs.getDate("project_end_date"); // 기존 종료 날짜 가져오기
            }

            // 업데이트 쿼리 설정
            updatePstmt.setInt(1, finished ? 1 : 0);
            updatePstmt.setInt(2, canceled ? 1 : 0);

            if (canceled) {
                // 취소된 경우 현재 날짜 설정
                updatePstmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            } else {
                // 취소가 아닐 경우 기존 종료 날짜로 설정
                updatePstmt.setDate(3, existingEndDate);
            }
            
            updatePstmt.setInt(4, projectId);

            int updatedRows = updatePstmt.executeUpdate();
            return updatedRows > 0; // 성공 여부 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //프로젝트 ID에 따른 상세 정보를 가져오는 메서드 추가(project
    public List<ProjectDetail> getProjectDetail1(int projectId, String userid) {
        List<ProjectDetail> project = new ArrayList<>(); 
        String sql = """
                select e.ename, m.member_id, m.member_designated, m.member_progress_rate, p.project_id,
                       (select m2.member_id
                        from project_member m2
                        where m2.project_id = p.project_id
                        and m2.member_role = 'MANAGER') as manager_id
                from project p 
                join project_member m on p.project_id = m.project_id
                join emp e on m.member_id = e.emp_id
                where p.project_id = ? and m.member_role != 'VIEWER'
                order by e.ename
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId); // 프로젝트 ID 설정
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                ProjectDetail projectDetail = new ProjectDetail();
                projectDetail.setParticipantNames(rs.getString(1));
                projectDetail.setMemberId(rs.getString(2));
                projectDetail.setMemberDesign(rs.getString(3));
                projectDetail.setMemberProgressRate(rs.getInt(4));
                projectDetail.setProjectId(rs.getInt(5));

                String managerId = rs.getString(6); // MANAGER의 ID 가져오기
                projectDetail.setIsManager(managerId != null && managerId.equals(userid)); // 로그인 ID가 MANAGER인지 확인
                
                project.add(projectDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project; // 프로젝트 정보 반환
    }


    public List<ProjectDetail> getProjectDetail2(int projectId) {
		List<ProjectDetail> project = new ArrayList<>(); 
        String sql = """
			SELECT 
			    e.ename,
			    NVL(t.task_subject, '') AS task_subject,
			    COALESCE(t.task_update_date, t.task_date) AS task_date,
			    m.member_id
			FROM 
			    project p
			JOIN 
			    project_member m ON p.project_id = m.project_id
			JOIN 
			    emp e ON m.member_id = e.emp_id
			LEFT JOIN (
			    SELECT 
			        task_subject, 
			        project_id, 
			        emp_id, 
			        task_date, 
			        task_update_date,
			        ROW_NUMBER() OVER (
			            PARTITION BY emp_id 
			            ORDER BY 
			                -- 우선 task_date로 내림차순 정렬, 그다음 task_update_date로 내림차순 정렬
			                task_date DESC NULLS LAST, 
			                task_update_date DESC NULLS LAST
			        ) AS rn
			    FROM 
			        task
			    WHERE 
			        project_id = ?
			) t ON p.project_id = t.project_id 
			    AND m.member_id = t.emp_id 
			    AND t.rn = 1  
			WHERE 
			    p.project_id = ? and m.member_role != 'VIEWER'
			ORDER BY 
			    e.ename
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId); // 프로젝트 ID 설정
            pstmt.setInt(2, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { 
                ProjectDetail projectDetail = new ProjectDetail();
                projectDetail.setTaskWriter(rs.getString(1));
                projectDetail.setTaskTitle(rs.getString(2));
                if(rs.getString(3) != null) projectDetail.setTaskUpdateDate(rs.getString(3).substring(0,10));//날짜만
                else projectDetail.setTaskUpdateDate(rs.getString(3));
                projectDetail.setMemberId(rs.getString(4));
                project.add(projectDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project; // 프로젝트 정보 반환
	}

	public boolean updateProgressRate(int projectId, String memberId, int memberProgressRate) {
		String sql = """
		        UPDATE project_member
		        SET member_progress_rate = ?
		        WHERE project_id = ? AND member_id = ?
		    """;

		    try (Connection conn = ds.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        
		        pstmt.setInt(1, memberProgressRate);
		        pstmt.setInt(2, projectId);
		        pstmt.setString(3, memberId);

		        int updatedRows = pstmt.executeUpdate();
		        return updatedRows > 0; // 업데이트 성공 여부 반환
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
	}


	public boolean updateTaskContent(int projectId, String memberId, String taskContent) {
	    String sql;

	    // 해당 project_id와 member_id로 레코드가 있는지 확인
	    String checkSql = "SELECT COUNT(*) FROM project_member WHERE project_id = ? AND member_id = ?";

	    try (Connection conn = ds.getConnection();
	         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

	        checkStmt.setInt(1, projectId);
	        checkStmt.setString(2, memberId);

	        try (ResultSet rs = checkStmt.executeQuery()) {
	            rs.next();
	            int count = rs.getInt(1);
	            
	            // 레코드가 존재하면 UPDATE, 없으면 INSERT
	            if (count > 0) {
	                sql = "UPDATE project_member SET member_designated = ? WHERE project_id = ? AND member_id = ?";
	            } else {
	                sql = "INSERT INTO project_member (member_designated, project_id, member_id) VALUES (?, ?, ?)";
	            }

	            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	                stmt.setString(1, taskContent);
	                stmt.setInt(2, projectId);
	                stmt.setString(3, memberId);

	                int rowsAffected = stmt.executeUpdate();
	                return rowsAffected > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public int getListCount(String userid, int projectid) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<ProjectComplete> getBoardList(int page, int limit, String userid, int projectid) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMemberRole(int projectId, String userId) {
	    String role = "VIEWER"; // 기본값을 'VIEWER'로 설정 (없는 경우)
	    String sql = "SELECT m.member_role FROM project_member m WHERE m.project_id = ? AND m.member_id = ?";

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, projectId); // 프로젝트 ID 설정
	        pstmt.setString(2, userId);     // 사용자 ID 설정
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            role = rs.getString("member_role");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return role;
	}

	public List<ProjectComplete> getCompletedProjectsList(int page, int limit, String userid) {
	    List<ProjectComplete> completedProjects = new ArrayList<>();
	    String sql = """
	            SELECT * FROM (
				    SELECT rownum rnum, t.*
				    FROM (
				        SELECT
				            p.project_id,
				            p.project_name,
				            p.project_start_date,
				            p.project_end_date,
				            p.project_file_original,
				            p.project_file_uuid,
				            p.project_file_type,
				            COUNT(pm.project_member_id) AS member_count,
				            SUM(pm.MEMBER_PROGRESS_RATE) / COUNT(pm.project_member_id) AS avg_progress,
				            (SELECT e.ename
				             FROM emp e
				             WHERE e.emp_id = (
				                 SELECT m.member_id
				                 FROM project_member m
				                 WHERE m.project_id = p.project_id
				                 AND m.member_role = 'MANAGER'
				                 AND ROWNUM = 1
				             )) AS emp_name,
				             (SELECT m.member_id
				             FROM project_member m
				             WHERE m.project_id = p.project_id
				             AND m.member_role = 'MANAGER') AS manager_id,
				            LISTAGG(CASE WHEN pm.member_role = 'PARTICIPANT' THEN e.ename END, ', ')
				            WITHIN GROUP (ORDER BY e.ename) AS participants,
				            LISTAGG(CASE WHEN pm.member_role = 'VIEWER' THEN e.ename END, ', ')
				            WITHIN GROUP (ORDER BY e.ename) AS viewers
				        FROM
				            project p
				        JOIN
				            project_member pm ON p.project_id = pm.project_id
				        LEFT JOIN
				            emp e ON pm.member_id = e.emp_id
				        WHERE
				            p.project_id IN (
				                SELECT project_id
				                FROM project_member
				                WHERE member_id = ?
				            )
				            AND p.project_finished = 1  -- 완료된 프로젝트만
				        GROUP BY
				            p.project_id, p.project_name, p.project_start_date, p.project_end_date, p.project_file_original, p.project_file_uuid, p.project_file_type
				        ORDER BY 
				            p.project_id
				    ) t
				    WHERE rownum <= ?
				)
				WHERE rnum >= ? AND rnum <= ?
	            """;

	    int startRow = (page - 1) * limit + 1;
	    int endRow = startRow + limit - 1;

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, userid);
	        pstmt.setInt(2, endRow);
	        pstmt.setInt(3, startRow);
	        pstmt.setInt(4, endRow);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                ProjectComplete project = new ProjectComplete();
	                project.setProjectId(rs.getInt("project_id"));
	                project.setProjectName(rs.getString("project_name"));
	                project.setEmpName(rs.getString("emp_name"));
	                
	                // 참여자 및 열람자 목록 처리
	                String participants = rs.getString("participants");
	                if (participants != null) {
	                    project.setParticipantNames(Arrays.asList(participants.split(", ")));
	                }

	                String viewers = rs.getString("viewers");
	                if (viewers != null) {
	                    project.setViewers(Arrays.asList(viewers.split(", ")));
	                }

	                project.setProjectStartDate(rs.getString("project_start_date").substring(0,10));
	                project.setProjectEndDate(rs.getString("project_end_date").substring(0,10));
	                
	                project.setProject_file_original(rs.getString("project_file_original"));
	                project.setProject_file_uuid(rs.getString("project_file_uuid"));
	                project.setProject_file_type(rs.getString("project_file_type"));
	                completedProjects.add(project);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return completedProjects;
	}

	public int getCompleteCountList(String userid) {
		String sql = "select count(*) from project p join project_member m on p.project_id = m.project_id  where m.member_id = ? and project_finished = 1 order by p.project_id";
		int x = 0; 
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, userid);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) x = rs.getInt(1);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러 : " + ex);
		}
		return x;
	}

	public List<ProjectComplete> getCancelProjectsList(int page, int limit, String userid) {
		List<ProjectComplete> cancelProjects = new ArrayList<>();
	    String sql = """
	            SELECT * FROM (
				    SELECT rownum rnum, t.*
				    FROM (
				        SELECT
				            p.project_id,
				            p.project_name,
				            p.project_start_date,
				            p.project_end_date,
				            p.project_file_original,
				            p.project_file_uuid,
				            p.project_file_type,
				            COUNT(pm.project_member_id) AS member_count,
				            SUM(pm.MEMBER_PROGRESS_RATE) / COUNT(pm.project_member_id) AS avg_progress,
				            (SELECT e.ename
				             FROM emp e
				             WHERE e.emp_id = (
				                 SELECT m.member_id
				                 FROM project_member m
				                 WHERE m.project_id = p.project_id
				                 AND m.member_role = 'MANAGER'
				                 AND ROWNUM = 1
				             )) AS emp_name,
				             (SELECT m.member_id
				             FROM project_member m
				             WHERE m.project_id = p.project_id
				             AND m.member_role = 'MANAGER') AS manager_id,
				            LISTAGG(CASE WHEN pm.member_role = 'PARTICIPANT' THEN e.ename END, ', ')
				            WITHIN GROUP (ORDER BY e.ename) AS participants,
				            LISTAGG(CASE WHEN pm.member_role = 'VIEWER' THEN e.ename END, ', ')
				            WITHIN GROUP (ORDER BY e.ename) AS viewers
				        FROM
				            project p
				        JOIN
				            project_member pm ON p.project_id = pm.project_id
				        LEFT JOIN
				            emp e ON pm.member_id = e.emp_id
				        WHERE
				            p.project_id IN (
				                SELECT project_id
				                FROM project_member
				                WHERE member_id = ?
				            )
				            AND p.project_canceled = 1  -- 완료된 프로젝트만
				        GROUP BY
				            p.project_id, p.project_name, p.project_start_date, p.project_end_date, p.project_file_original, p.project_file_uuid, p.project_file_type
				        ORDER BY 
				            p.project_id
				    ) t
				    WHERE rownum <= ?
				)
				WHERE rnum >= ? AND rnum <= ?
	            """;

	    int startRow = (page - 1) * limit + 1;
	    int endRow = startRow + limit - 1;

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, userid);
	        pstmt.setInt(2, endRow);
	        pstmt.setInt(3, startRow);
	        pstmt.setInt(4, endRow);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                ProjectComplete project = new ProjectComplete();
	                project.setProjectId(rs.getInt("project_id"));
	                project.setProjectName(rs.getString("project_name"));
	                project.setEmpName(rs.getString("emp_name"));
	                
	                // 참여자 및 열람자 목록 처리
	                String participants = rs.getString("participants");
	                if (participants != null) {
	                    project.setParticipantNames(Arrays.asList(participants.split(", ")));
	                }

	                String viewers = rs.getString("viewers");
	                if (viewers != null) {
	                    project.setViewers(Arrays.asList(viewers.split(", ")));
	                }

	                project.setProjectStartDate(rs.getString("project_start_date").substring(0,10));
	                project.setProjectEndDate(rs.getString("project_end_date").substring(0,10));
	                
	                project.setProject_file_original(rs.getString("project_file_original"));
	                project.setProject_file_uuid(rs.getString("project_file_uuid"));
	                project.setProject_file_type(rs.getString("project_file_type"));
	                cancelProjects.add(project);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return cancelProjects;
	}

	public int getCancelCountList(String userid) {
		String sql = "select count(*) from project p join project_member m on p.project_id = m.project_id  where m.member_id = ? and project_canceled = 1 order by p.project_id";
		int x = 0; 
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, userid);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) x = rs.getInt(1);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러 : " + ex);
		}
		return x;
	}

	public List<ProjectInfo> getOngoingProjectsList(int page, int limit, String userid) {
		List<ProjectInfo> projectList = new ArrayList<>();
        String sql = """
                SELECT * FROM (
				    SELECT rownum rnum, t.*
				    FROM (
			SELECT
                    p.project_id,
                    p.project_name,
                    p.project_end_date,
                    COUNT(pm.project_member_id) AS member_count,  
                    SUM(pm.MEMBER_PROGRESS_RATE) / COUNT(pm.project_member_id) AS avg_progress,
                    (SELECT e.ename
                     FROM emp e 
                     WHERE e.emp_id = (
                         SELECT m.member_id
                         FROM project_member m
                         WHERE m.project_id = p.project_id 
                         AND m.member_role = 'MANAGER'
                         AND ROWNUM = 1
                     )) AS emp_name,
                     (SELECT m.member_id
                     FROM project_member m 
                     WHERE m.project_id = p.project_id 
                     AND m.member_role = 'MANAGER') AS manager_id,
                    LISTAGG(CASE WHEN pm.member_role = 'PARTICIPANT' THEN e.ename END, ', ') 
                    WITHIN GROUP (ORDER BY e.ename) AS participants,
                    LISTAGG(CASE WHEN pm.member_role = 'VIEWER' THEN e.ename END, ', ') 
                    WITHIN GROUP (ORDER BY e.ename) AS viewers
                FROM
                    project p
                JOIN
                    project_member pm ON p.project_id = pm.project_id
                LEFT JOIN
                    emp e ON pm.member_id = e.emp_id
                WHERE
                    p.project_id IN (
                        SELECT project_id
                        FROM project_member
                        WHERE member_id = ?
                    ) 
                    AND p.project_finished = 0 
                    AND p.project_canceled = 0
                GROUP BY
                    p.project_id, p.project_name, p.project_end_date
                ORDER BY 
                    p.project_id
                     ) t
				    WHERE rownum <= ?
				)
				WHERE rnum >= ? AND rnum <= ?
        """;
        int startRow = (page - 1) * limit + 1;
	    int endRow = startRow + limit - 1;
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userid);
            pstmt.setInt(2, endRow);
	        pstmt.setInt(3, startRow);
	        pstmt.setInt(4, endRow);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ProjectInfo project = new ProjectInfo();
                    project.setProjectId(rs.getInt("project_id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setEndDate(rs.getString("project_end_date").substring(0,10));
                    project.setMemberCount(rs.getInt("member_count"));
                    project.setProgressRate(rs.getInt("avg_progress"));

                    // 책임자 이름
                    project.setEmpName(rs.getString("emp_name"));

                    String participants = rs.getString("participants");
	                if (participants != null) {
	                    project.setParticipantNames(Arrays.asList(participants.split(", ")));
	                }

	                String viewers = rs.getString("viewers");
	                if (viewers != null) {
	                    project.setViewers(Arrays.asList(viewers.split(", ")));
	                }

                    
                    String managerId = rs.getString("manager_id");
                    if(managerId != null && managerId.equals(userid)) {
                        project.setIsManager(true);
                    } else {
                        project.setIsManager(false);
                    }

                    projectList.add(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
	}

	public int getOngoingCountList(String userid) {
		String sql = "select count(*) from project p join project_member m on p.project_id = m.project_id  where m.member_id = ? and project_canceled = 0 and project_finished = 0 order by p.project_id";
		int x = 0; 
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, userid);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) x = rs.getInt(1);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러 : " + ex);
		}
		return x;
	}

	public boolean saveUploadedFiles(int projectId, List<ProjectInfo> files, int num) {
	    String sql = "update project set project_file_path = ?, project_file_original = ?, project_file_uuid = ?, project_file_type = ? where project_id = ? and %s";

	    // 상태에 따라 WHERE 조건을 다르게 설정
	    String condition = (num == 1) ? "project_finished = 1" : "project_canceled = 1";

	    try (Connection conn = ds.getConnection()) {
	        conn.setAutoCommit(false);  // 트랜잭션 시작
	        
	        try (PreparedStatement pstmt = conn.prepareStatement(String.format(sql, condition))) {
	            if (files != null && !files.isEmpty()) {
	                for (ProjectInfo file : files) {
	                    pstmt.setString(1, file.getProject_file_path());
	                    pstmt.setString(2, file.getProject_file_original());
	                    pstmt.setString(3, file.getProject_file_uuid());
	                    pstmt.setString(4, file.getProject_file_type());
	                    pstmt.setInt(5, projectId);
	                }
	                int result = pstmt.executeUpdate();
	        		if (result > 0) {
	                    conn.commit();  // 트랜잭션 커밋
	                    return true;
	                } else {
	                    conn.rollback();  // 실패 시 롤백
	                    return false;
	                }
	            }
	        } catch (SQLException e) {
	            conn.rollback();  // 예외 발생 시 롤백
	            e.printStackTrace();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	public List<ProjectDetail> getNotice(String userid) {
		List<ProjectDetail> project = new ArrayList<>(); 
	    String sql = "SELECT p.project_name, t.task_date, t.task_update_date, e1.ename AS task_emp_name, e1.img_uuid AS pm_img_uuid, j.job_name "
	    		+ "FROM task t "
	    		+ "JOIN project p ON t.project_id = p.project_id "
	    		+ "JOIN project_member pm ON p.project_id = pm.project_id "
	    		+ "JOIN emp e1 ON t.emp_id = e1.emp_id "
	    		+ "JOIN emp e2 ON pm.member_id = e2.emp_id "
	    		+ "JOIN job j ON e2.job_id = j.job_id "
	    		+ "WHERE p.project_finished = 0 "
	    		+ "  AND p.project_canceled = 0 "
	    		+ "  AND pm.member_id = ? "
	    		+ "  AND t.emp_id != pm.member_id "
	    		+ "  AND e2.emp_id != t.emp_id "
	    		+ "ORDER BY NVL(t.task_update_date, t.task_date) DESC NULLS LAST";

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	    	pstmt.setString(1, userid);
	    	ResultSet rs = pstmt.executeQuery();
            while (rs.next()) { 
                ProjectDetail projectDetail = new ProjectDetail();
                projectDetail.setProjectName(rs.getString(1));
                projectDetail.setTaskDate(rs.getString(2));
                projectDetail.setTaskUpdateDate(rs.getString(3));
                projectDetail.setTaskWriter(rs.getString(4));
                projectDetail.setTask_file_uuid(rs.getString(5));
                projectDetail.setJobname(rs.getString(6));
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