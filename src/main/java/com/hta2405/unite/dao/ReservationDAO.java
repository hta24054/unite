package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
		/*
		String sql = """
				SELECT resc_name, resc_type
                FROM resc
				WHERE resc_type = ? AND resc_usable = '1'
				GROUP BY resc_name, resc_type
            	""";
        */
		
		String sql = """
	            SELECT MIN(resc_id) AS resc_id, resc_type, resc_name, resc_usable
	            FROM resc
	            WHERE resc_type = ? AND resc_usable = '1'
	            GROUP BY resc_type, resc_name, resc_usable
	        """;
		
		try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);) {
			
			 pstmt.setString(1, resourceType);

			 try (ResultSet rs = pstmt.executeQuery()) {
				 while (rs.next()) {
					 Resource resource = new Resource();
					 resource.setResourceId(rs.getLong("resc_id"));
					 resource.setResourceType(rs.getString("resc_type"));
					 resource.setResourceName(rs.getString("resc_name"));
					 resource.setResourceUsable(rs.getBoolean("resc_usable"));
	                 list.add(resource);
	            }
			 }
	    } catch (Exception e) {
	         e.printStackTrace();
	         System.out.println("resourceSelectChange()에러 :" + e);
	    }
		
		return list;
	} //resourceSelectChange end
	
	// 자원명으로 resc_id 가져오기
	public List<Resource> getRescIdByName(String resourceName) {
		List<Resource> list = new ArrayList<>();
        String sql = """
            SELECT MIN(resc_id) AS resc_id, resc_name
            FROM resc
            WHERE resc_name = ? AND resc_usable = '1'
            GROUP BY resc_name
        """;
	
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resourceName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Resource resource = new Resource();
                    resource.setResourceId(rs.getLong("resc_id"));
                    resource.setResourceName(rs.getString("resc_name"));
                    list.add(resource);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getRescIdByName() 에러: " + e);
        }
        return list;
	} // getRescIdByName end
	
	// 자원 예약
	public int insertReservation(Reservation reservation) {
		 int result = 0;

	    // 중복 확인 
	    String check_sql = """
	            SELECT COUNT(*) FROM reservation 
	            WHERE resource_id = ? 
	            AND (
	                (reservation_start < ? AND reservation_end > ?)  -- 새 예약의 시작 시간이 기존 예약 기간에 겹침
	                OR 
	                (reservation_start < ? AND reservation_end > ?)  -- 새 예약의 종료 시간이 기존 예약 기간에 겹침
	                OR 
	                (reservation_start >= ? AND reservation_end <= ?) -- 새 예약이 기존 예약 내부에 완전히 포함됨
	            )
	    """;
	    
	    String insert_sql = """
	            INSERT INTO reservation 
	            (resource_id, emp_id, reservation_start, reservation_end, reservation_info, reservation_allDay)
	            VALUES (?, ?, ?, ?, ?, ?)
	    """;

	    try (Connection conn = ds.getConnection();
	         PreparedStatement checkStmt = conn.prepareStatement(check_sql);
	         PreparedStatement insertStmt = conn.prepareStatement(insert_sql);) {

	        // 중복확인
	    	checkStmt.setInt(1, reservation.getResourceId());
	        checkStmt.setTimestamp(2, Timestamp.valueOf(reservation.getReservationEnd())); // 새 예약 종료 시간
	        checkStmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationStart())); // 새 예약 시작 시간
	        checkStmt.setTimestamp(4, Timestamp.valueOf(reservation.getReservationEnd())); // 새 예약 종료 시간
	        checkStmt.setTimestamp(5, Timestamp.valueOf(reservation.getReservationStart())); // 새 예약 시작 시간
	        checkStmt.setTimestamp(6, Timestamp.valueOf(reservation.getReservationStart())); // 새 예약 시작 시간
	        checkStmt.setTimestamp(7, Timestamp.valueOf(reservation.getReservationEnd())); // 새 예약 종료 시간
	        
	        try (ResultSet rs = checkStmt.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                System.out.println("이미 예약된 자원입니다.");
	                return 0;
	            }
	        }

	        // 중복 없을 경우 데이터 삽입
	        insertStmt.setInt(1, reservation.getResourceId());
	        insertStmt.setString(2, reservation.getEmpId());
	        insertStmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationStart()));
	        insertStmt.setTimestamp(4, Timestamp.valueOf(reservation.getReservationEnd()));
	        insertStmt.setString(5, reservation.getReservationInfo());
	        insertStmt.setInt(6, reservation.getReservationAllDay());

	        result = insertStmt.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("insertResourceBooking() 에러: " + e);
	    }
	    return result;
	} 
	
	// 자원 예약 목록 불러오기
	public JsonArray getReservationList(String resourceId) {
	    String sql = """
	        SELECT * 
	        FROM reservation
	    """;
	    JsonArray array = new JsonArray();
	    
	    try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
        	
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
            	 JsonObject resourceObj = new JsonObject();
            	 
            	 resourceObj.addProperty("reservation_id", rs.getInt("reservation_id"));
            	 resourceObj.addProperty("resource_id", rs.getInt("resource_id"));
            	 resourceObj.addProperty("emp_id", rs.getInt("emp_id"));
            	 resourceObj.addProperty("reservation_start", rs.getString("reservation_start"));
            	 resourceObj.addProperty("reservation_end", rs.getString("reservation_end"));
            	 resourceObj.addProperty("reservation_info",rs.getString("reservation_info"));
            	 resourceObj.addProperty("reservation_allDay", rs.getInt("reservation_allDay"));
            	 
            	 array.add(resourceObj); 
             }
             
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getResourceBookingList()에러 :" + e);
        }
        return array;
	}

	// 자원예약 상세정보 팝업
	public List<Map<String, Object>> getReservationModal(String reservationId) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    
	    String sql = """
	            SELECT 
	                resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable, 
	                reservation.reservation_id,
	                reservation.emp_id,
	                reservation.reservation_start,
	                reservation.reservation_end,
	                reservation.reservation_info,
	                reservation.reservation_allDay
	            FROM reservation
	            LEFT JOIN resc resc
	            ON reservation.resource_id = resc.resc_id
	            WHERE reservation.reservation_id = ?
	    """;
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, reservationId);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> result = new HashMap<>();
	                Resource resource = new Resource();
	                
	                resource.setResourceId(rs.getLong("resc_id"));
	                resource.setResourceType(rs.getString("resc_type"));
	                resource.setResourceName(rs.getString("resc_name"));
	                resource.setResourceInfo(rs.getString("resc_info"));
	                resource.setResourceUsable(rs.getInt("resc_usable") == 1);
	                
	                result.put("resource", resource);
	                result.put("empId", rs.getString("emp_id"));
	                
	                resultList.add(result);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("getReservationModal() 에러: " + e);
	    }

	    System.out.println("자원예약 상세정보 팝업: " + resultList);
	    return resultList;
	}

	// 예약 취소
	public int cancelReservation(String reservationId, String empId) {
		int result = 0;
		String sql = """
				DELETE 
				FROM reservation 
				WHERE reservation_id = ? AND emp_id = ?
			""";
       
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, reservationId); 
            pstmt.setString(2, empId);
            
            result = pstmt.executeUpdate(); // 삭제 성공 시 1, 실패 시 0

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("cancelReservation()에러 :" + e);
        }
        
        return result;
	}

	// 나의 자원 예약목록
	public List<Map<String, Object>> getMyReservationList(String empId) {
	    List<Map<String, Object>> list = new ArrayList<>();
	    
	    /*
	    String sql  = """
	            SELECT  rs.resc_type, rs.resc_name, rs.resc_info,
	    		         
	    		        (select ename from emp where emp_id  = ?) ename,
	            
					    reservation.reservation_start, reservation.reservation_end, 
					    reservation.reservation_info, reservation.reservation_id
				FROM reservation reservation
				JOIN resc rs 
				ON reservation.resource_id = rs.resc_id
				WHERE reservation.emp_id = ?
				ORDER BY reservation.reservation_id
	    	""";
	    */

	    String sql  = """
	            SELECT  rs.resc_type, rs.resc_name, rs.resc_info,
					    reservation.reservation_start, reservation.reservation_end, 
					    reservation.reservation_info, reservation.reservation_id
				FROM reservation reservation
				JOIN resc rs 
				ON reservation.resource_id = rs.resc_id
				WHERE reservation.emp_id = ?
				ORDER BY reservation.reservation_id
	    	""";

	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);) {

	        // empId와 reservationId 파라미터 설정
	        pstmt.setString(1, empId);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                Reservation reservation = new Reservation();
	                Resource resource = new Resource();

	                resource.setResourceType(rs.getString("resc_type"));
	                resource.setResourceName(rs.getString("resc_name"));
	                resource.setResourceInfo(rs.getString("resc_info"));
	                reservation.setReservationStart(rs.getTimestamp("reservation_start").toLocalDateTime());
	                reservation.setReservationEnd(rs.getTimestamp("reservation_end").toLocalDateTime());
	                reservation.setReservationInfo(rs.getString("reservation_info"));
	                reservation.setReservationId(rs.getInt("reservation_id"));
	                
	                map.put("reservation", reservation);
	                map.put("resource", resource);

	                list.add(map);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("getMyReservationList() 오류: " + e);
	    }
	    
	    System.out.println("나의 자원 예약목록" + list);
	    return list;
	}
}
