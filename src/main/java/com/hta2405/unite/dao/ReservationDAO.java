package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	public int insertResourceBooking(Reservation reservation) {
		 int result = 0;

	    // 중복 확인 
	    String check_sql = """
	        SELECT COUNT(*) FROM reservation WHERE resource_id = ?
	    """;
	    
	    String insert_sql = """
	        INSERT INTO reservation 
	        (resource_id, emp_id, reservation_start, reservation_end, reservation_info, reservation_allDay)
	        VALUES (?, ?, ?, ?, ?, ?)
	    """;

	    try (Connection conn = ds.getConnection();
	         PreparedStatement checkStmt = conn.prepareStatement(check_sql);
	         PreparedStatement insertStmt = conn.prepareStatement(insert_sql);) {

	        // 중복 확인
	        checkStmt.setInt(1, reservation.getResourceId());  // reservation에서 resourceId를 가져옴
	        
	        try (ResultSet rs = checkStmt.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                System.out.println("이미 예약된 자원입니다.");
	                return 0;
	            }
	        }

	        // 중복이 없을 경우 데이터 삽입
	        insertStmt.setInt(1, reservation.getResourceId());  // reservation에서 resourceId를 가져옴
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
	} // insertResourceBooking end
	


	// 자원 예약 목록 불러오기
	public JsonArray getResourceBookingList(String resourceId) {
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
	    System.out.println("자원 예약 목록" + array);
        return array;
	}// getResourceBookingList() end

	
	// 자원 예약 정보 팝업
	public HashMap<String, String> getResourceBookingDetail(Reservation reservation, Resource resource) {
		
		
		HashMap<String, String> resourceMap = new HashMap<>();  // rescType을 key로, rescName을 value로 저장할 Map
		/*
	    String sql = """
	    			SELECT resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable
	    			FROM resc
	    			WHERE resc.resc_id = ?
	    		""";
	    */
		
		String sql = """
				SELECT 
				    resc.resc_id, resc.resc_type, resc.resc_name, resc.resc_info, resc.resc_usable,
				    res.reservation_id,
				    res.emp_id,
				    res.reservation_start,
				    res.reservation_end,
				    res.reservation_info,
				    res.reservation_allDay
				FROM resc
				LEFT JOIN reservation res
				ON  resc.resc_id = res.resource_id
				WHERE resc.resc_id = ?
				""";
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);) {
	    	
	    	 pstmt.setInt(1, reservation.getResourceId());  // 예약에서 받은 resourceId 사용
	    	 
	    	 try (ResultSet rs = pstmt.executeQuery()) {

	    	        if (rs.next()) {
	    	            // 조회한 리소스 정보를 Resource 객체에 설정
	    	        	resource.setResourceId(rs.getLong("resc_id"));
						resource.setResourceType(rs.getString("resc_type"));
						resource.setResourceName(rs.getString("resc_name"));
	    	            resource.setResourceInfo(rs.getString("resc_info"));
	    	            resource.setResourceUsable(rs.getBoolean("resc_usable"));

	    	            // rescType을 key로, rescName을 value로 HashMap에 저장
	    	            resourceMap.put(rs.getString("resc_type"), rs.getString("resc_name"));
	    	        }
	    	 }
	    	
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("getResourceBookingDetail()에러 :" + e);
	    } 

	    System.out.println("자원 예약 정보 팝업 resourceMap" + resourceMap);
	    return resourceMap;  
	}





}
