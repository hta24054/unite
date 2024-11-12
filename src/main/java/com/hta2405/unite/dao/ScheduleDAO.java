package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hta2405.unite.dto.Schedule;

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


	
	



	


	
	
	
	
	
	


}
