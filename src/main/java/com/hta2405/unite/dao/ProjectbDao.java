package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hta2405.unite.dto.ProjectDetail;
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
	public boolean insertOrUpdatePost(String title, String content, List<ProjectDetail> taskFile, String empId, int projectId) {
	    String checkSql = "SELECT task_subject, task_id FROM task WHERE emp_id = ? AND project_id = ?";
	    String insertSql = """
	            INSERT INTO task(emp_id, project_id, task_subject, task_content, task_date, 
	                task_file_path, task_file_original, task_file_uuid, task_file_type) VALUES
	                (?, ?, ?, ?, sysdate, ?, ?, ?, ?)
	    """;
	    String updateSql = "UPDATE task SET task_subject = ?, task_content = ?, task_update_date = sysdate, task_file_path = ?, task_file_original = ?, task_file_uuid = ?, task_file_type = ? WHERE emp_id = ? AND project_id = ?";

	    try (Connection conn = ds.getConnection()) {
	        conn.setAutoCommit(false);  // 트랜잭션 시작

	        // 기존 데이터 체크
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
	            checkStmt.setString(1, empId);
	            checkStmt.setInt(2, projectId);
	            ResultSet rs = checkStmt.executeQuery();
	            if (rs.next() && rs.getString("task_subject") == null) {
	                // task_subject가 null이면 업데이트
	                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
	                    updateStmt.setString(1, title);
	                    updateStmt.setString(2, content);

	                    // 파일 처리: taskFile이 비어 있는 경우 null 값 처리
	                    if (taskFile != null && !taskFile.isEmpty()) {
	                        ProjectDetail file = taskFile.get(0);  // 첫 번째 파일 처리
	                        updateStmt.setString(3, file.getTask_file_path());
	                        updateStmt.setString(4, file.getTask_file_original());
	                        updateStmt.setString(5, file.getTask_file_uuid());
	                        updateStmt.setString(6, file.getTask_file_type());
	                    } else {
	                        // 파일이 없을 경우 null 값 처리
	                        updateStmt.setNull(3, java.sql.Types.VARCHAR);
	                        updateStmt.setNull(4, java.sql.Types.VARCHAR);
	                        updateStmt.setNull(5, java.sql.Types.VARCHAR);
	                        updateStmt.setNull(6, java.sql.Types.VARCHAR);
	                    }

	                    updateStmt.setString(7, empId);
	                    updateStmt.setInt(8, projectId);
	                    int result = updateStmt.executeUpdate();
	                    
	                    if (result > 0) {
	                        conn.commit();  // 트랜잭션 커밋
	                        return true;
	                    } else {
	                        conn.rollback();  // 실패 시 롤백
	                        return false;
	                    }
	                }
	            } else {
	                // task_subject가 null이 아니거나 레코드가 없는 경우 삽입
	                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
	                    insertStmt.setString(1, empId);
	                    insertStmt.setInt(2, projectId);
	                    insertStmt.setString(3, title);
	                    insertStmt.setString(4, content);

	                    // 파일 처리: taskFile이 비어 있는 경우 null 값 처리
	                    if (taskFile != null && !taskFile.isEmpty()) {
	                        ProjectDetail file = taskFile.get(0);  // 첫 번째 파일 처리
	                        insertStmt.setString(5, file.getTask_file_path());
	                        insertStmt.setString(6, file.getTask_file_original());
	                        insertStmt.setString(7, file.getTask_file_uuid());
	                        insertStmt.setString(8, file.getTask_file_type());
	                    } else {
	                        // 파일이 없을 경우 null 값 처리
	                        insertStmt.setNull(5, java.sql.Types.VARCHAR);
	                        insertStmt.setNull(6, java.sql.Types.VARCHAR);
	                        insertStmt.setNull(7, java.sql.Types.VARCHAR);
	                        insertStmt.setNull(8, java.sql.Types.VARCHAR);
	                    }

	                    int result = insertStmt.executeUpdate();
	                    
	                    if (result > 0) {
	                        conn.commit();  // 트랜잭션 커밋
	                        return true;
	                    } else {
	                        conn.rollback();  // 실패 시 롤백
	                        return false;
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            conn.rollback();  // 예외 발생 시 롤백
	            e.printStackTrace();
	            return false;
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
				        task_content,  
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
	            task.setMemberId(rs.getString("member_id"));
	            task.setProjectTitle(rs.getString("task_subject"));
	            task.setProjectContent(rs.getString("task_content"));
	            if(rs.getString("task_date") != null) task.setProjectUpdateDate(rs.getString("task_date").substring(0,10));//날짜만
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
	                    task.setMemberId(rs.getString("emp_id"));
	                    if(rs.getString("task_date") != null) task.setProjectDate(rs.getString("task_date").substring(0,16));
	                    else task.setProjectDate(rs.getString("task_date"));
	                    if(rs.getString("task_update_date") != null) task.setProjectUpdateDate(rs.getString("task_update_date").substring(0,16));
	                    else task.setProjectUpdateDate(rs.getString("task_update_date"));

	                    task.setBoard_file(rs.getString("task_file_path"));
	                    taskList.add(task);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return taskList;
	}

	public int getListCount(String userid, int projectid) {
		String sql = "select count(*) from task where emp_id = ? and project_id = ?";
		int x = 0; 
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, userid);
			pstmt.setInt(2, projectid);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) x = rs.getInt(1);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러 : " + ex);
		}
		return x;
	}

	public List<ProjectTask> getBoardList(int page, int limit, String userid, int projectid) {
	    List<ProjectTask> tasklist = new ArrayList<>();
	    
	    String task_list_sql = """
	        SELECT * FROM (
	            SELECT rownum rnum, t.* FROM (
	                SELECT *
	                FROM task t
	                WHERE t.project_id = ? AND t.emp_id = ?
	                ORDER BY t.task_date DESC
	            ) t
	            WHERE rownum <= ?
	        ) 
	        WHERE rnum >= ? AND rnum <= ?
	    """;
	    
	    // 시작 row와 끝 row 계산
	    int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호
	    int endrow = startrow + limit - 1; // 읽을 마지막 row 번호
	    
	    String count_comment_sql = "SELECT COUNT(*) FROM task_comment WHERE task_id = ?";
	    
	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement(task_list_sql)) {
	        
	        pstmt.setInt(1, projectid);
	        pstmt.setString(2, userid);
	        pstmt.setInt(3, endrow);
	        pstmt.setInt(4, startrow);
	        pstmt.setInt(5, endrow);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                ProjectTask task = new ProjectTask();
	                task.setProjectTitle(rs.getString("task_subject"));
	                task.setProjectContent(rs.getString("task_content"));
	                task.setProjectDate(rs.getString("task_date") != null ? rs.getString("task_date").substring(0, 16) : "");
	                task.setProjectUpdateDate(rs.getString("task_update_date") != null ? rs.getString("task_update_date").substring(0, 16) : "");
	                task.setTask_file_original(rs.getString("task_file_original"));
	                task.setTask_file_uuid(rs.getString("task_file_uuid"));
	                task.setTask_file_type(rs.getString("task_file_type"));
	                
	                task.setTaskNum(rs.getInt("task_id")); // 글번호
	                task.setMemberId(userid);
	                
	                // 댓글 수를 가져오는 쿼리 실행
	                try (PreparedStatement countPstmt = con.prepareStatement(count_comment_sql)) {
	                    countPstmt.setInt(1, rs.getInt("task_id"));
	                    try (ResultSet countRs = countPstmt.executeQuery()) {
	                        if (countRs.next()) {
	                            task.setBoard_cnt(countRs.getInt(1)); // 댓글 수 설정
	                        }
	                    }
	                }
	                
	                tasklist.add(task);
	            }
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        System.out.println("getBoardList() 에러: " + ex);
	    }
	    
	    return tasklist;
	}


	public ProjectDetail getFileByUUID(String fileUUID, String userid, int projectid) {
		String sql = "select task_file_uuid from task where fileUUID = ? and userid = ? and projectid = ?";
		try (Connection con = ds.getConnection();
		         PreparedStatement pstmt = con.prepareStatement(sql)) {
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public List<ProjectTask> getUserTaskDetail(String userid, int projectid, int task_num) {
		List<ProjectTask> taskList = new ArrayList<>();
	    String sql = "SELECT t.*, e.ename FROM task t join emp e on t.emp_id = e.emp_id WHERE t.emp_id = ? AND t.project_id = ? and t.task_id = ?";

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	    	pstmt.setString(1, userid);
	        pstmt.setInt(2, projectid);
	        pstmt.setInt(3, task_num);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                ProjectTask task = new ProjectTask();
	                task.setMemberName(rs.getString("ename"));
	                task.setProjectTitle(rs.getString("task_subject"));
	                task.setProjectContent(rs.getString("task_content"));
	                task.setMemberId(rs.getString("emp_id"));
	                task.setTaskNum(rs.getInt("task_id"));
	                
	                taskList.add(task);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return taskList;
	}

	public boolean modify(ProjectTask modify) {
		String update_sql = "update task set task_subject = ?, task_content = ?, task_update_date = sysdate where task_id = ? and project_id = ?";
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(update_sql);){
			pstmt.setString(1, modify.getProjectTitle());
			pstmt.setString(2, modify.getProjectContent());
			pstmt.setInt(3, modify.getTaskNum());
			pstmt.setInt(4, modify.getProjectId());
			int result = pstmt.executeUpdate();
			if(result == 1) {
				System.out.println("성공 업데이트");
				return true;
			}
		}catch(Exception ex) {
			System.out.println("boardModify() 에러 : " + ex);
		}
		return false;
	}

	public boolean boardDelete(int task_num) {
	    String delete_sql = "DELETE FROM task WHERE task_id = ?";
	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement(delete_sql)) {
	        pstmt.setInt(1, task_num);  
	        int result = pstmt.executeUpdate();  
	        if (result == 1) {
	            System.out.println("게시물 삭제 성공");
	            return true;
	        } else {
	            System.out.println("삭제된 게시물이 없습니다.");
	            return false;
	        }
	    } catch (Exception ex) {
	        System.out.println("boardDelete() 에러 : " + ex);
	        ex.printStackTrace();
	    }
	    return false;
	}

	/*public int commentsInsert(ProjectTask task_comment) {
	    int result = 0;
	    String insert_sql = """
	        INSERT INTO task_comment (task_comment_writer, task_id, task_comment_content, task_comment_re_ref, task_comment_re_seq, task_comment_re_lev, task_comment_date)
	        VALUES (?, ?, ?, ?, ?, seq_task_comment.nextval, sysdate)
	    """;

	    // re_ref, re_seq, re_lev 값을 설정하는 로직
	    int re_ref = task_comment.getBoard_re_lev() == 0 ? task_comment.getTaskNum() : task_comment.getBoard_re_lev(); // 댓글이 새로 생성될 때 re_ref 설정
	    int re_seq = task_comment.getBoard_re_seq();
	    int re_lev = task_comment.getBoard_re_lev() + 1;

	    try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(insert_sql)) {
	        pstmt.setString(1, task_comment.getMemberId());
	        pstmt.setInt(2, task_comment.getTaskNum());
	        pstmt.setString(3, task_comment.getTaskContent());
	        pstmt.setInt(4, re_ref);  // re_ref 값 설정
	        pstmt.setInt(5, re_seq);  // re_seq 값 설정

	        result = pstmt.executeUpdate();
	        if (result == 1) System.out.println("데이터 삽입 완료되었습니다");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}*/


	//comment 개수
	public int getListCount(int comment_board_num) {
		String sql = "select count(*) from task_comment where task_id = ?";
		int x = 0; 
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setInt(1, comment_board_num);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) x = rs.getInt(1);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러 : " + ex);
		}
		return x;
	}

	public JsonArray getCommentList(int comment_board_num, int state) {
		JsonArray ja = new JsonArray();
		String select = """
				select task_comment_id, task_comment_writer, task_comment_content, task_comment_date, task_comment_re_lev, task_comment_re_seq, task_comment_re_ref, emp.img_original, emp.ename
				from task_comment join emp on task_comment.task_comment_writer = emp.emp_id 
				where task_id = ? order by task_comment_re_ref %s, task_comment_re_seq asc
				""".formatted(state==1 ? "asc" : "desc");
		try(Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(select);){
			pstmt.setInt(1, comment_board_num);
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					JsonObject object = new JsonObject();
					object.addProperty("task_comment_id", rs.getInt(1));
					object.addProperty("task_comment_writer", rs.getString(2));
					object.addProperty("task_comment_content", rs.getString(3));
					object.addProperty("task_comment_date", rs.getString(4));
					object.addProperty("task_comment_re_lev", rs.getInt(5));
					object.addProperty("task_comment_re_seq", rs.getInt(6));
					object.addProperty("task_comment_re_ref", rs.getInt(7));
					object.addProperty("img_original", rs.getString(8));
					object.addProperty("ename", rs.getString(9));
					ja.add(object);
				}
			}
		}catch(Exception e) {
			System.out.println("getCommentList()에러 : " + e);
		}
		return ja;
	}

	public int getParentLev(int taskNum, int parentCommentId) {
	    int parentLev = 0;

	    String query = "SELECT task_comment_re_lev FROM task_comment WHERE task_id = ? AND task_comment_id = ?";
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement pstmt = con.prepareStatement(query)) {
	        pstmt.setInt(1, taskNum);
	        pstmt.setInt(2, parentCommentId);

	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            parentLev = rs.getInt("task_comment_re_lev");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return parentLev;
	}
	
	public int getNextReSeq(int taskNum, int parentCommentId) {
	    int nextReSeq = 0;

	    String query = "SELECT MAX(task_comment_re_seq) AS max_seq FROM task_comment WHERE task_id = ? AND task_comment_re_ref = ?";
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement pstmt = con.prepareStatement(query)) {
	        pstmt.setInt(1, taskNum);
	        pstmt.setInt(2, parentCommentId); // 부모 댓글 ID

	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            nextReSeq = rs.getInt("max_seq") + 1;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return nextReSeq;
	}
	
	public int commentsInsert(ProjectTask task_comment) {
	    int result = 0;
	    String insert_sql = """
	            insert into task_comment (task_comment_writer, task_id, task_comment_content, 
	                                      task_comment_re_ref, task_comment_re_seq, task_comment_re_lev, task_comment_date)
	            values(?, ?, ?, ?, ?, ?, sysdate)
	            """;
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement pstmt = con.prepareStatement(insert_sql)) {
	        pstmt.setString(1, task_comment.getMemberId());
	        pstmt.setInt(2, task_comment.getTaskNum());
	        pstmt.setString(3, task_comment.getTaskContent());
	        pstmt.setInt(4, task_comment.getBoard_re_ref()); // comment_re_ref
	        pstmt.setInt(5, task_comment.getBoard_re_seq()); // comment_re_seq
	        pstmt.setInt(6, task_comment.getBoard_re_lev()); // comment_re_lev

	        result = pstmt.executeUpdate();
	        if (result == 1) {
	            System.out.println("댓글이 성공적으로 삽입되었습니다.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}


	
}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	/*public static boolean insertOrUpdatePost(String title, String content, String filePath, String originalFileName, String fileUuid, String fileType, String empId, int projectId) {
	    String checkSql = "SELECT task_subject FROM task WHERE emp_id = ? AND project_id = ?";
	    String insertSql = "INSERT INTO task (emp_id, project_id, task_subject, task_content, task_date, task_update_date, task_file_path, task_file_original, task_file_uuid, task_file_type) VALUES (?, ?, ?, ?, sysdate, sysdate, ?, ?, ?, ?)";
	    String updateSql = "UPDATE task SET task_subject = ?, task_content = ?, task_update_date = sysdate, task_file_path = ?, task_file_original = ?, task_file_uuid = ?, task_file_type = ? WHERE emp_id = ? AND project_id = ?";

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
	                    updateStmt.setString(4, originalFileName);
	                    updateStmt.setString(5, fileUuid);
	                    updateStmt.setString(6, fileType);
	                    updateStmt.setString(7, empId);
	                    updateStmt.setInt(8, projectId);
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
	                    insertStmt.setString(6, originalFileName);
	                    insertStmt.setString(7, fileUuid);
	                    insertStmt.setString(8, fileType);
	                    int result = insertStmt.executeUpdate();
	                    return result > 0;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}*/



