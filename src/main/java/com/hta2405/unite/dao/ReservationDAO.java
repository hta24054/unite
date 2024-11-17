package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

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
	
	public List<String> getResourceTypes() {
		List<String> list = new ArrayList<>();
        
        String sql = """
	        		SELECT DISTINCT resc_type 
	        		FROM resc
	        		""";  
        /*
         *  SELECT * FROM RESC;
         
            SELECT resc_type
			FROM resc;
			
			SELECT resc_name
			FROM resc 
			WHERE resc_type = '회의실' AND resc_usable = '1';
			
			SELECT resc_name
			FROM resc 
			WHERE resc_type = '차량' AND resc_usable = '1';
         * 
         * */
        
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
        	
        	 ResultSet rs = ps.executeQuery();
        	 while (rs.next()) {
        		 list.add(rs.getString("resc_type"));
             }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getResourceTypes()에러 :" + e);
        }
        
        return list;
	}

	public List<Resource> getResourceList() {
		List<Resource> list = new ArrayList<>();
        String sql = """
                    SELECT * FROM RESC
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
	}

}
