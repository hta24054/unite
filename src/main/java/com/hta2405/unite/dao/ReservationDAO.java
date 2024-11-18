package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Reservation;
import com.hta2405.unite.dto.Resource;

public class ReservationDAO {
	private DataSource ds;
	
	public ReservationDAO() {
		try {
			Context init = new InitialContext();
			this.ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
		}
	}

    // 자원 목록 불러오기
	public List<Resource> getResourceList() {
		List<Resource> list = new ArrayList<>();
        String sql = """
                    SELECT * 
                    FROM RESC
                	""";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
        	
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
            	 Resource resource = new Resource(
                         rs.getLong("resc_id"),
                         rs.getString("resc_type"),
                         rs.getString("resc_name"),
                         rs.getString("resc_info") == null ? null : rs.getString("resc_info"),
                         rs.getInt("resc_usable") == 1
                     );
                 list.add(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getResourceList()에러 :" + e);
        }
        return list;
	}// getResourceList end


	// 자원 선택 시 해당 자원명 불러오기
	public List<Resource> resourceSelectChange(String resourceType) {
		List<Resource> list = new ArrayList<>();
		String sql = """
				SELECT resc_name, resc_type
                FROM resc
				WHERE resc_type = ? AND resc_usable = '1'
				GROUP BY resc_name, resc_type
            	""";
		
		try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);) {
			
			 pstmt.setString(1, resourceType);

			 try (ResultSet rs = pstmt.executeQuery()) {
				 while (rs.next()) {
					 Resource resource = new Resource();
					 resource.setResourceType(rs.getString("resc_type"));
					 resource.setResourceName(rs.getString("resc_name"));
					 resource.setResourceUsable(true);
	                 list.add(resource);
	            }
			 }
	    } catch (Exception e) {
	         e.printStackTrace();
	         System.out.println("resourceSelectChange()에러 :" + e);
	    }
		
		return list;
	} //resourceSelectChange end

	// 자원 예약 
	public int insertResourceBooking(Reservation reservation, Resource resource) {
		int result = 0;
	    String sql = """
	        INSERT INTO reservation
	        (reservation_id, resource_id, emp_id, reservation_start, reservation_end, reservation_info, reservation_allDay, resc_id)
	        VALUES (SEQ_reservation.NEXTVAL, ?, ?, ?, ?, ?, ?, 
		            (SELECT resc_name 
		             FROM resc 
		             WHERE resc_id = ?)
		    )
	     """;
	    
	    System.out.println("resource: " + resource);
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);) {

	           pstmt.setInt(1, reservation.getReservationId()); 
	           pstmt.setString(2, reservation.getEmpId());   
	           pstmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationStart()));  
	           pstmt.setTimestamp(4, Timestamp.valueOf(reservation.getReservationEnd()));    
	           pstmt.setString(5, reservation.getReservationInfo());  
	           pstmt.setInt(6, reservation.getReservationAllDay());   
	           pstmt.setLong(7, resource.getResourceId());  

	           result = pstmt.executeUpdate();

       } catch (Exception e) {
           e.printStackTrace();
           System.out.println("insertResourceBooking() 에러: " + e);
       }

	    System.out.println("자원 예약 result" + result);
        return result;
	} //insertResourceBooking end


	

}
