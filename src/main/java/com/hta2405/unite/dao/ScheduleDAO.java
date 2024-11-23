package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hta2405.unite.dto.Holiday;
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
	public int insertSchedule(Schedule schedule) {
		int result = 0;
		String sql = """
			    INSERT INTO schedule
			    (schedule_id, emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color, schedule_allDay) 
			    VALUES (SEQ_schedule.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
			    """;
		
	try(Connection con = ds.getConnection();
		PreparedStatement pstmt = con.prepareStatement(sql);) {
		
		pstmt.setString(1, schedule.getEmpId());
		pstmt.setString(2, schedule.getScheduleName());
		pstmt.setString(3, schedule.getScheduleContent());
		pstmt.setTimestamp(4, Timestamp.valueOf(schedule.getScheduleStart())); 
        pstmt.setTimestamp(5, Timestamp.valueOf(schedule.getScheduleEnd()));
		pstmt.setString(6, schedule.getScheduleColor());
		pstmt.setInt(7, schedule.getScheduleAllDay());
		
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
	public int updateSchedule(Schedule schedule) {
		int result = 0;
		String update_sql = """
				UPDATE schedule
				SET    schedule_name = ?, schedule_start = ?, schedule_end = ?, 
				       schedule_color = ?, schedule_content = ?, schedule_allDay = ?  
				WHERE  schedule_id = ? AND emp_id = ?
				""";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(update_sql);) {
			
			pstmt.setString(1, schedule.getScheduleName());
	        pstmt.setTimestamp(2, Timestamp.valueOf(schedule.getScheduleStart()));
	        pstmt.setTimestamp(3, Timestamp.valueOf(schedule.getScheduleEnd()));
	        pstmt.setString(4, schedule.getScheduleColor());
	        pstmt.setString(5, schedule.getScheduleContent());
	        pstmt.setInt(6, schedule.getScheduleAllDay());
	        pstmt.setInt(7, schedule.getScheduleId());
	        pstmt.setString(8, schedule.getEmpId()); 
	        
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
	public int dragUpdateSchedule(Schedule schedule) {
		int result = 0;
		String drag_update_sql = """
				UPDATE schedule
				SET    schedule_start = ?, schedule_end = ?, schedule_allDay = ?
				WHERE  schedule_id = ? AND emp_id = ?
				""";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(drag_update_sql);) {
			
			pstmt.setTimestamp(1, Timestamp.valueOf(schedule.getScheduleStart()));
	        pstmt.setTimestamp(2, Timestamp.valueOf(schedule.getScheduleEnd()));
	        pstmt.setInt(3, schedule.getScheduleAllDay());
	        pstmt.setInt(4, schedule.getScheduleId());
	        pstmt.setString(5, schedule.getEmpId());

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
	
	// 공유 일정 등록
	public int insertScheduleShare(Schedule schedule, ScheduleShare share) {
	    int result = 0;
	    int scheduleId = 0;

	    String sql = """
	        INSERT INTO schedule
	        (schedule_id, emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color, schedule_allDay)
	        VALUES (SEQ_schedule.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
	    """;

	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement(sql);) {

	        pstmt.setString(1, schedule.getEmpId());
	        pstmt.setString(2, schedule.getScheduleName());
	        pstmt.setString(3, schedule.getScheduleContent());
	        pstmt.setTimestamp(4, Timestamp.valueOf(schedule.getScheduleStart()));
	        pstmt.setTimestamp(5, Timestamp.valueOf(schedule.getScheduleEnd()));
	        pstmt.setString(6, schedule.getScheduleColor());
	        pstmt.setInt(7, schedule.getScheduleAllDay());

	        result = pstmt.executeUpdate();
	        
	        String id_sql = """
	        		SELECT SEQ_schedule.CURRVAL FROM dual
	        		""";

	        if (result > 0) {
	        	 try (PreparedStatement idStmt = con.prepareStatement(id_sql);
                      ResultSet rs = idStmt.executeQuery()) {

                    if (rs.next()) {
                    	scheduleId = rs.getInt(1); // 새로 생성된 schedule_id 반환
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

	            // share_emp 값에서 여러 공유자 ID를 가져옴
	            String[] shareEmpIds = share.getShareEmp() != null ? share.getShareEmp().split(",") : new String[0];

	            // 공유자 각각을 개별 행으로 추가
	            try (PreparedStatement pstmtShare = con.prepareStatement(share_sql)) {
	                for (String empId : shareEmpIds) {
	                    pstmtShare.setString(1, empId.trim());
	                    pstmtShare.setInt(2, scheduleId); // 새로 생성된 schedule_id 사용
	                    pstmtShare.executeUpdate();
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("insertScheduleShare() 에러 : " + e);
	    }
	    
	    return result;
	}//insertScheduleShare end

	// 공유 일정 리스트
	public JsonArray getListSharedSchedule(String empId) {
		/*
		String share_sql = """
				SELECT *
				FROM schedule s
				JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
				WHERE s.emp_id = ? OR ss.share_emp = ?
				""";
		*/
		String share_sql = """
				SELECT s.schedule_id,
				       s.emp_id,
				       s.schedule_name,
				       s.schedule_content,
				       s.schedule_start,
				       s.schedule_end,
				       s.schedule_color,
				       s.schedule_allDay,
				       MIN(ss.share_emp) AS share_emp 
				       
				FROM schedule s
				JOIN schedule_share ss ON s.schedule_id = ss.schedule_id
				WHERE s.emp_id = ? OR ss.share_emp = ?
				GROUP BY s.schedule_id, s.emp_id, s.schedule_name, s.schedule_content,
						 s.schedule_start, s.schedule_end, s.schedule_color, s.schedule_allDay
				""";
		
		JsonArray array = new JsonArray();
		 
		try (Connection con = ds.getConnection();
		     PreparedStatement pstmt = con.prepareStatement(share_sql);) {
			 	
		        pstmt.setString(1, empId);
		        pstmt.setString(2, empId);
		        
		        try (ResultSet rs = pstmt.executeQuery()) {
		            while (rs.next()) {
		                JsonObject scheduleObj = new JsonObject();
		                scheduleObj.addProperty("schedule_id", rs.getInt("schedule_id"));
		                scheduleObj.addProperty("emp_id", rs.getString("emp_id"));
		                scheduleObj.addProperty("share_emp", rs.getString("share_emp"));
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
		        System.out.println("getListSharedSchedule() 에러: " + e);
		    }

		    return array;
	}//getListSharedSchedule end

	// 공휴일 리스트
	public List<Holiday> getHoliday(LocalDate startDate, LocalDate endDate) {
		ArrayList<Holiday> HolidayList = new ArrayList<>();
        String sql = """
                    SELECT * 
					FROM HOLIDAY
					WHERE HOLIDAY_DATE BETWEEN ? AND ?
					AND holiday_name NOT IN ('토요일', '일요일')
					ORDER BY HOLIDAY_DATE
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Holiday holiday = new Holiday(rs.getLong("holiday_id"),
                        rs.getDate("holiday_date").toLocalDate(),
                        rs.getString("holiday_name"));
                HolidayList.add(holiday);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        System.out.println("HolidayList " + HolidayList);
        return HolidayList;
	}	
}
