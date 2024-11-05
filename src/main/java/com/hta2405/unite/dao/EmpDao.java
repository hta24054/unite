package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Emp;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String sql = """
                    SELECT * FROM EMP
                    WHERE EMP_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return makeEmp(rs);
            }
        } catch (SQLException e) {
            System.out.println("회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return null;
    }

    public Emp getEmpByNameAndEmail(String name,String email) {
        String sql = """
                    SELECT * FROM EMP
                    WHERE ENAME = ?
                    AND EMAIL = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return makeEmp(rs);
            }
        } catch (SQLException e) {
            System.out.println("회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Emp> getEmpListByDeptId(Long deptId) {
        List<Emp> list = new ArrayList<>();
        String sql = """
                    SELECT *
                    FROM EMP e NATURAL JOIN JOB j
                    WHERE e.DEPT_ID = ?
                    ORDER BY j.JOB_RANK, e.emp_id
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, deptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(makeEmp(rs));
            }
        } catch (SQLException e) {
            System.out.println("부서 회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return list;
    }

    private static Emp makeEmp(ResultSet rs) throws SQLException {
        return new Emp(rs.getString("emp_id"),
                rs.getString("password"),
                rs.getString("ename"),
                rs.getLong("dept_id"),
                rs.getLong("job_id"),
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



}
