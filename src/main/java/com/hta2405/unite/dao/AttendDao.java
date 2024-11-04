package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Attend;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AttendDao {
    private DataSource ds;

    public AttendDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public ArrayList<Attend> getAttendByEmpId(String empId, LocalDate startDate, LocalDate endDate) {
        ArrayList<Attend> attendList = new ArrayList<>();
        System.out.println("startDate = " + startDate.toString());
        System.out.println("startDate = " + endDate.toString());
        String sql = """
            SELECT * FROM ATTEND
            WHERE EMP_ID = ? AND ATTEND_DATE BETWEEN ? AND ?
            """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Attend attend = new Attend(
                        rs.getInt("attend_id"),
                        rs.getString("emp_id"),
                        rs.getDate("attend_date").toLocalDate(),
                        rs.getTimestamp("attend_in").toLocalDateTime(),
                        rs.getTimestamp("attend_out").toLocalDateTime(),
                        null,
                        rs.getString("attend_type")
                );
                attendList.add(attend);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return attendList;
    }
}
