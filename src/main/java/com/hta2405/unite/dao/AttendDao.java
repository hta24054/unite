package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Attend;
import com.hta2405.unite.dto.Emp;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<Attend> getAttendByEmpId(Emp emp, LocalDate startDate, LocalDate endDate) {
        ArrayList<Attend> attendList = new ArrayList<>();
        String sql = """
                SELECT * FROM ATTEND
                WHERE EMP_ID = ? AND ATTEND_DATE BETWEEN ? AND ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getEmpId());
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Attend attend = makeAttend(rs);
                attendList.add(attend);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return attendList;
    }

    public Attend getAttendByEmpId(String empId, LocalDate date) {
        String sql = """
                SELECT * FROM ATTEND
                WHERE EMP_ID = ? AND ATTEND_DATE = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ps.setDate(2, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return makeAttend(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("근태기록 찾기 오류");
        }
        return null;
    }


    public List<Attend> getYearlyVacationByEmpId(String empId, int year) {
        List<Attend> list = new ArrayList<>();
        String sql = """
                SELECT * FROM ATTEND
                WHERE EMP_ID = ? AND ATTEND_TYPE ='%휴가%'
                AND EXTRACT(YEAR FROM ATTEND_DATE) = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                list.add(makeAttend(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("휴가 목록 가져오기 오류");
        }
        return list;
    }

    public int attendIn(String empId, String attendType) {
        String sql = """
                    INSERT INTO ATTEND(EMP_ID, ATTEND_DATE, ATTEND_IN, ATTEND_TYPE)
                    VALUES(?, ?, ?, ?)
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, attendType);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("출근처리 오류");
        }
        return 0;
    }


    private static Attend makeAttend(ResultSet rs) throws SQLException {
        return new Attend(
                rs.getInt("attend_id"),
                rs.getString("emp_id"),
                rs.getDate("attend_date").toLocalDate(),
                rs.getTimestamp("attend_in") == null ?
                        null : rs.getTimestamp("attend_in").toLocalDateTime(),
                rs.getTimestamp("attend_out") == null ?
                        null : rs.getTimestamp("attend_out").toLocalDateTime(),
                null,
                rs.getString("attend_type")
        );
    }


    public int attendOut(String empId) {
        String sql = """
                    UPDATE ATTEND SET
                    ATTEND_OUT = ?
                    WHERE EMP_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, empId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("퇴근처리 오류");
        }
        return 0;
    }
}
