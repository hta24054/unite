package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DeptDao {
    private DataSource ds;

    public DeptDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public String getManagerIdByDeptId(Long deptId) {
        String sql = """
                    SELECT DEPT_MANAGER FROM DEPT
                    WHERE DEPT_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, deptId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getDeptNameById(Long deptId) {
        String sql = """
                    SELECT DEPT_NAME FROM DEPT
                    WHERE DEPT_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, deptId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public HashMap<Long, String> getIdToDeptNameMap() {
        HashMap<Long, String> map = new HashMap<>();
        String sql = """
                    SELECT dept_id, dept_name from DEPT
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getLong("dept_id"), rs.getString("dept_name"));
            }
        } catch (SQLException e) {
            System.out.println("deptMap 불러오기 에러");
            e.printStackTrace();
        }
        return map;
    }
}
