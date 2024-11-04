package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

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
	public int insert(Schedule s) {
		int result = 0;
		String sql = """
				INSERT INTO schedule
				(schedule_id, emp_id, schedule_name, schedule_content, schedule_start, schedule_end, schedule_color)
				VALUES(SEQ_schedule.NEXTVAL, ?, ?, ?, ?, ?)
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
			System.out.println("삽입 성공: " + result); // 성공 메시지 출력
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("insert() 에러 : " + e);
		}
		return result;
	}//insert end
	
	
	
	
	
	
}
