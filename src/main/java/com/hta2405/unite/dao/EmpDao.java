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

    public List<Emp> getSubEmpListByEmp(Emp emp) {
        List<Emp> list = new ArrayList<>();
        if (emp.getEmpId().contains("admin")) {
            return getAllEmpList(list);
        }
        String deptToString = emp.getDeptId().toString();
        //쿼리에 들어갈 deep 구함 ex) 1110 -> 1 / 1100 -> 2 / 1000 -> 3
        int deep = deptToString.length();
        for (int i = 0; i < deptToString.length(); i++) {
            if (deptToString.charAt(i) != '0') {
                deep--;
            } else {
                break;
            }
        }
        String sql = """
                    SELECT *
                    FROM EMP e NATURAL JOIN JOB j
                    where ? = floor(dept_id / power(10,?)) * power(10,?)
                    ORDER BY j.JOB_RANK, e.emp_id
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, emp.getDeptId());
            ps.setLong(2, deep);
            ps.setLong(3, deep);
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

    public List<Emp> getAllEmpList(List<Emp> list) {
        String sql = """
                    SELECT *
                    FROM EMP e NATURAL JOIN JOB j
                    ORDER BY j.JOB_RANK, e.emp_id
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
