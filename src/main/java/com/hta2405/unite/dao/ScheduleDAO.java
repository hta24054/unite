package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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
	public int scheduleInsert(Schedule s) {
		int result = 0;
		String sql = """
				INSERT INTO schedule
				(schedule_id, emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color) 
				VALUES(SEQ_schedule.NEXTVAL, ?, ?, ?, ?, ?, ?)
				""";
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			pstmt.setString(1, s.getEmpId());
			pstmt.setString(2, s.getScheduleName());
			pstmt.setString(3, s.getScheduleContent());
			pstmt.setTimestamp(4, Timestamp.valueOf(s.getScheduleStart())); 
	        pstmt.setTimestamp(5, Timestamp.valueOf(s.getScheduleEnd()));
			pstmt.setString(6, s.getScheduleColor());
			
			result = pstmt.executeUpdate();//삽입 성공시 result는 1
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("scheduleInsert() 에러 : " + e);
		}
		return result;
	}//scheduleInsert end

	public JsonArray getListSchedule(String empId) {
		String sql = """
				select * 
				from schedule
				where emp_id = ?
				""";
		JsonArray array = new JsonArray();
		
		try(Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, empId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					JsonObject obj  = new JsonObject();
					obj.addProperty("schedule_name", rs.getString(1));
					obj.addProperty("schedule_content", rs.getString(2));
					
					Timestamp scheduleStart = rs.getTimestamp(3);
	                if (scheduleStart != null) {
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
	                    obj.addProperty("schedule_start", sdf.format(scheduleStart));
	                }
	                Timestamp scheduleEnd = rs.getTimestamp(4);
	                if (scheduleEnd != null) {
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                    obj.addProperty("schedule_end", sdf.format(scheduleEnd));
	                }
	                obj.addProperty("schedule_color", rs.getString(5));
	                array.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getListSchedule() 에러 : " + e);
		}
		return array;
	}//getListSchedule end
	
	
	
	
	
	
}
