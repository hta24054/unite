package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hta2405.unite.dto.Schedule;
import com.hta2405.unite.dto.ScheduleShare;

public class ScheduleDAO {
	private DataSource ds;
	
	public ScheduleDAO() {
		try {
			Context init = new InitialContext();
			this.ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
		}
	}

	//일정 등록
	public int insertSchedule(Schedule s) {
		int result = 0;
		String sql = """
			    INSERT INTO schedule
			    (schedule_id, emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color, schedule_allDay) 
			    VALUES (SEQ_schedule.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
			    """;
		
	try(Connection con = ds.getConnection();
		PreparedStatement pstmt = con.prepareStatement(sql);) {
		
		pstmt.setString(1, s.getEmpId());
		pstmt.setString(2, s.getScheduleName());
		pstmt.setString(3, s.getScheduleContent());
		pstmt.setTimestamp(4, Timestamp.valueOf(s.getScheduleStart())); 
        pstmt.setTimestamp(5, Timestamp.valueOf(s.getScheduleEnd()));
		pstmt.setString(6, s.getScheduleColor());
		pstmt.setInt(7, s.getScheduleAllDay());
		
		result = pstmt.executeUpdate();//삽입 성공시 result는 1
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("scheduleInsert() 에러 : " + e);
	}
		return result;
	}//scheduleInsert end
	
	// 일정 리스트
	public JsonArray getListSchedule(String id) {
		String sql = """
				select * 
				from schedule
				where emp_id = ?
				""";
		JsonArray array = new JsonArray();
		System.out.println("getListSchedule id 값" + id);
		
		try (Connection con = ds.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql);) {
				
				pstmt.setString(1, id);
				
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						JsonObject scheduleObj  = new JsonObject();
						
						scheduleObj.addProperty("schedule_id", rs.getInt("schedule_id"));
		                scheduleObj.addProperty("schedule_name", rs.getString("schedule_name"));
		                scheduleObj.addProperty("schedule_content", rs.getString("schedule_content"));
		                scheduleObj.addProperty("schedule_start", rs.getString("schedule_start"));
		                scheduleObj.addProperty("schedule_end", rs.getString("schedule_end"));
		                scheduleObj.addProperty("schedule_color", rs.getString("schedule_color"));
		                scheduleObj.addProperty("schedule_allDay", rs.getInt("schedule_allDay"));

		                array.add(scheduleObj);
					}
				}
		} catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("getListSchedule() 에러: " + e);
	    }
		return array;
	}

	// 일정 수정
	public int updateSchedule(Schedule s) {
		int result = 0;
		String update_sql = """
				UPDATE schedule
				SET    schedule_name = ?, schedule_start = ?, schedule_end = ?, 
				       schedule_color = ?, schedule_content = ?, schedule_allDay = ?  
				WHERE  schedule_id = ? AND emp_id = ?
				""";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(update_sql);) {
			
			pstmt.setString(1, s.getScheduleName());
	        pstmt.setTimestamp(2, Timestamp.valueOf(s.getScheduleStart()));
	        pstmt.setTimestamp(3, Timestamp.valueOf(s.getScheduleEnd()));
	        pstmt.setString(4, s.getScheduleColor());
	        pstmt.setString(5, s.getScheduleContent());
	        pstmt.setInt(6, s.getScheduleAllDay());
	        pstmt.setInt(7, s.getScheduleId());
	        pstmt.setString(8, s.getEmpId()); 
	        
	        result = pstmt.executeUpdate();
	        if (result == 1)
				System.out.println("일정 데이터 수정 되었습니다.");
		} catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("updateSchedule() 에러 : " + e);
	    }
	    
	    return result;
	}//updateSchedule end

	// 일정 수정(드래그)
	public int dragUpdateSchedule(Schedule s) {
		int result = 0;
		String drag_update_sql = """
				UPDATE schedule
				SET    schedule_start = ?, schedule_end = ?, schedule_allDay = ?
				WHERE  schedule_id = ? AND emp_id = ?
				""";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(drag_update_sql);) {
			
			pstmt.setTimestamp(1, Timestamp.valueOf(s.getScheduleStart()));
	        pstmt.setTimestamp(2, Timestamp.valueOf(s.getScheduleEnd()));
	        pstmt.setInt(3, s.getScheduleAllDay());
	        pstmt.setInt(4, s.getScheduleId());
	        pstmt.setString(5, s.getEmpId());

            result = pstmt.executeUpdate();
            if (result == 1)
				System.out.println("drag 일정 데이터 수정 되었습니다.");
		} catch (Exception e) {
            e.printStackTrace();
            System.out.println("dragUpdateSchedule() 에러 : " + e);
        }

		return result;
	}//dragUpdateSchedule

	// 일정 삭제
	public int deleteSchedule(int scheduleId) {
		int result = 0;
		String del_sql = "delete schedule where schedule_id = ?";
		
		try (Connection con = ds.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(del_sql);) {
			 
			pstmt.setInt(1, scheduleId);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}//deleteSchedule end
	
	public int insertScheduleShare(Schedule s, ScheduleShare share) {
	    int result = 0;
	    int scheduleId = 0;

	    String sql = """
	        INSERT INTO schedule
	        (schedule_id, emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color, schedule_allDay)
	        VALUES (SEQ_schedule.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
	    """;

	    try (Connection con = ds.getConnection()) {
	        // 트랜잭션 시작 - 자동 커밋 해제
	        con.setAutoCommit(false);

	        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

	            pstmt.setString(1, s.getEmpId());
	            pstmt.setString(2, s.getScheduleName());
	            pstmt.setString(3, s.getScheduleContent());
	            pstmt.setTimestamp(4, Timestamp.valueOf(s.getScheduleStart()));
	            pstmt.setTimestamp(5, Timestamp.valueOf(s.getScheduleEnd()));
	            pstmt.setString(6, s.getScheduleColor());
	            pstmt.setInt(7, s.getScheduleAllDay());

	            result = pstmt.executeUpdate();

	            // 새로 생성된 schedule_id를 가져오기
	            String idSql = """
	                SELECT SEQ_schedule.CURRVAL FROM dual
	            """;

	            if (result > 0) {
	                try (PreparedStatement idStmt = con.prepareStatement(idSql);
	                     ResultSet rs = idStmt.executeQuery()) {

	                    if (rs.next()) {
	                        scheduleId = rs.getInt(1); // 새로 생성된 schedule_id
	                    }
	                }
	            }

	            // 공유자 정보 추가
	            if (scheduleId > 0) {
	                String shareSql = """
	                    INSERT INTO schedule_share
	                    (schedule_share_id, share_emp, schedule_id)
	                    VALUES (SEQ_schedule_share.NEXTVAL, ?, ?)
	                """;

	                try (PreparedStatement pstmtShare = con.prepareStatement(shareSql)) {
	                    pstmtShare.setString(1, share.getShareEmp());
	                    pstmtShare.setInt(2, scheduleId); // 새로 생성된 schedule_id 사용

	                    result = pstmtShare.executeUpdate(); // 일정 공유 정보 삽입
	                    System.out.println("schedule_share 테이블에 삽입: " + share.getShareEmp() + ", schedule_id: " + scheduleId);
	                }
	            }

	            // 모든 작업이 성공하면 커밋
	            con.commit();
	        } catch (Exception e) {
	            // 예외 발생 시 롤백
	            con.rollback();
	            e.printStackTrace();
	            System.out.println("insertScheduleShare() 에러 : " + e);
	            result = 0;
	        } finally {
	            // 자동 커밋을 다시 활성화
	            con.setAutoCommit(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

	
	/*
	// 공유 일정 등록
	public int insertScheduleShare(Schedule s, ScheduleShare share) {
	    int result = 0;
	    int scheduleId = 0;

	    String sql = """
	        INSERT INTO schedule
	        (schedule_id, emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color, schedule_allDay)
	        VALUES (SEQ_schedule.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
	    """;

	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        pstmt.setString(1, s.getEmpId());
	        pstmt.setString(2, s.getScheduleName());
	        pstmt.setString(3, s.getScheduleContent());
	        pstmt.setTimestamp(4, Timestamp.valueOf(s.getScheduleStart()));
	        pstmt.setTimestamp(5, Timestamp.valueOf(s.getScheduleEnd()));
	        pstmt.setString(6, s.getScheduleColor());
	        pstmt.setInt(7, s.getScheduleAllDay());

	        result = pstmt.executeUpdate();
	        
	        String id_sql = """
	        		SELECT SEQ_schedule.CURRVAL FROM dual
	        		""";

	        if (result > 0) {
	        	 try (PreparedStatement idStmt = con.prepareStatement(id_sql);
                      ResultSet rs = idStmt.executeQuery()) {

                    if (rs.next()) {
                        return rs.getInt(1); // 새로 생성된 schedule_id 반환
                    }
                }
	        }

	        // schedule_id가 생성되었을 경우 공유자 추가
	        if (scheduleId > 0) {
	            String share_sql = """
	                INSERT INTO schedule_share
	                (schedule_share_id, share_emp, schedule_id)
	                VALUES (SEQ_schedule_share.NEXTVAL, ?, ?)
	            """;
	            
	            try (PreparedStatement pstmtShare = con.prepareStatement(share_sql)) {
	            	pstmt.setString(1, share.getShareEmp());
	            	pstmt.setInt(2, scheduleId); // 새로 생성된 schedule_id 사용

                    result = pstmtShare.executeUpdate(); // 일정 공유 정보 삽입
                    System.out.println("schedule_share 테이블에 삽입: " + share.getShareEmp() + ", schedule_id: " + scheduleId);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("insertScheduleShare() 에러 : " + e);
	    }

	    return result;
	}*/
}
