package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Emp;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpDao {
    private DataSource ds;

    public EmpDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public Emp getEmpById(String id) {
        System.out.println(1234);
        String sql = """
                    SELECT * FROM EMP
                    WHERE EMP_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Emp(rs.getString("emp_id"),
                        rs.getString("password"),
                        rs.getString("ename"),
                        rs.getLong("job_id"),
                        rs.getLong("dept_id"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("tel"),
                        rs.getString("mobile"),
                        rs.getString("img_path"),
                        rs.getString("img_original"),
                        rs.getString("img_uuid"),
                        rs.getString("img_type"),
                        rs.getBoolean("hired"));
            }
        } catch (SQLException e) {
            System.out.println("회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return null;
    }
    
    
}
