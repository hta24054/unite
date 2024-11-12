package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.ProjectInfo;
import com.hta2405.unite.dto.ProjectTask;

public class ProjectbDao {
    private static DataSource ds;

    public ProjectbDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }

	public boolean boardInsert(ProjectTask projectdata) {
		int result = 0;
		String max_sql = "(select nvl(max(board_num), 0) + 1 from task)";
		
		//원문글의 BOARD_RE_REF 필드는 자신의 글 번호이다
		//%1$s : 첫 번째 인자를 문자열로 출력
		String sql = """
				insert into task (board_num, board_name, board_pass, board_subject, board_content,
									board_file, board_re_ref, board_re_lev, board_re_seq, board_readcount)
				values (%1$s, ?, ?, ?, ?, ?, %1$s, ?, ?, ?)
				""".formatted(max_sql);
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			//새로운 글 등록
			pstmt.setInt(1, projectdata.getProjectId());
			pstmt.setString(2, projectdata.getMemberPass());
			pstmt.setString(3, projectdata.getProjectTitle());
			pstmt.setString(4, projectdata.getProjectContent());
			pstmt.setString(5, projectdata.getBoard_file());
			
			//원문의 경우 BOARD_RE_LEV, BOARD_RE_SEQ 필드 값은 0이다
			pstmt.setInt(6, 0); //board_re_lev 필드
			pstmt.setInt(7, 0); //board_re_seq 필드
			pstmt.setInt(8, 0); //board_readcount 필드
			result = pstmt.executeUpdate();
			
			if(result == 1) {
				System.out.println("데이터 삽입이 모두 완료 되었습니다");
				return true;
			}
		}catch(Exception ex) {
			System.out.println("boardInsert() 에러 : " + ex);
			ex.printStackTrace();
		}
		return false;
	}
	
	public static boolean insertOrUpdatePost(String title, String content, String filePath, String empId, int projectId) {
	    String checkSql = "SELECT task_subject FROM task WHERE emp_id = ? AND project_id = ?";
	    String insertSql = "INSERT INTO task (emp_id, project_id, task_subject, task_content, task_date, task_update_date, task_file_path) VALUES (?, ?, ?, ?, sysdate, sysdate, ?)";
	    String updateSql = "UPDATE task SET task_subject = ?, task_content = ?, task_update_date = sysdate, task_file_path = ? WHERE emp_id = ? AND project_id = ?";

	    try (Connection conn = ds.getConnection()) {
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
	            checkStmt.setString(1, empId);
	            checkStmt.setInt(2, projectId);
	            ResultSet rs = checkStmt.executeQuery();

	            if (rs.next() && rs.getString("task_subject") == null) {
	                // task_subject가 null이면 업데이트
	                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
	                    updateStmt.setString(1, title);
	                    updateStmt.setString(2, content);
	                    updateStmt.setString(3, filePath);
	                    updateStmt.setString(4, empId);
	                    updateStmt.setInt(5, projectId);
	                    int result = updateStmt.executeUpdate();
	                    return result > 0;
	                }
	            } else {
	                // task_subject가 null이 아니거나 레코드가 없는 경우 삽입
	                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
	                    insertStmt.setString(1, empId);
	                    insertStmt.setInt(2, projectId);
	                    insertStmt.setString(3, title);
	                    insertStmt.setString(4, content);
	                    insertStmt.setString(5, filePath);
	                    int result = insertStmt.executeUpdate();
	                    return result > 0;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public List<ProjectTask> getRecentPosts(String empId, int projectId) {
	    String sql = """
	    		SELECT 
				    e.ename,
				    NVL(t.task_subject, '') AS task_subject,
				    COALESCE(t.task_update_date, t.task_date) AS task_date,
				    t.task_content,  -- 추가된 부분
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
				        task_content,  -- 추가된 부분
				        project_id, 
				        emp_id, 
				        task_date, 
				        task_update_date,
				        ROW_NUMBER() OVER (PARTITION BY emp_id ORDER BY 
				            task_update_date DESC NULLS LAST, 
				            task_date DESC NULLS LAST) AS rn
				    FROM 
				        task
				    WHERE 
				        project_id = ?
				) t ON p.project_id = t.project_id 
				    AND m.member_id = t.emp_id 
				    AND t.rn = 1  
				WHERE 
				    p.project_id = ?
				ORDER BY 
				    e.ename
		 		""";

	    List<ProjectTask> recentPosts = new ArrayList<>();
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	System.out.println("empId: " + empId + ", projectId: " + projectId);

	        stmt.setInt(1, projectId);
	        stmt.setInt(2, projectId);
	        
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            ProjectTask task = new ProjectTask();
	            task.setMemberName(rs.getString(1));
	            task.setProjectTitle(rs.getString("task_subject"));
	            task.setProjectContent(rs.getString("task_content"));
	            task.setProjectUpdateDate(rs.getString("task_date"));
	            recentPosts.add(task);
	            System.out.println(task);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("SQLException: " + e.getMessage());
	    }
	    
	    return recentPosts;
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

	public String getUserName(int projectid, String userid) {
		String sql = "select e.ename from project_member m join emp e on m.member_id = e.emp_id where m.project_id = ? and m.member_id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectid);
            pstmt.setString(2, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ename");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 해당 ID의 프로젝트가 없을 경우
	}

	public List<ProjectTask> getUserTask(int projectid, String userid) {
		List<ProjectTask> taskList = new ArrayList<>();
		String sql = "select * from task where project_id = ? and emp_id = ?";
		try (Connection conn = ds.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, projectid);
	            pstmt.setString(2, userid);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    ProjectTask task = new ProjectTask();
	                    task.setProjectTitle(rs.getString("task_subject"));
	                    task.setProjectContent(rs.getString("task_content"));
	                    task.setProjectDate(rs.getString("task_date"));
	                    task.setProjectUpdateDate(rs.getString("task_update_date"));
	                    task.setBoard_file(rs.getString("task_file_path"));
	                    taskList.add(task);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return taskList;
	}


    /* 최근 작성된 게시글 목록 조회 메서드
    public List<ProjectTask> getRecentPosts() {
        List<ProjectTask> postList = new ArrayList<>();
        String sql = "SELECT post_id, title, content, file_path, created_at FROM posts ORDER BY created_at DESC";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ProjectTask post = new ProjectTask();
                post.setPostId(rs.getInt("post_id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setFilePath(rs.getString("file_path"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                postList.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;
    }*/

    


} 