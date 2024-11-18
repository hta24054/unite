package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Attend;
import com.hta2405.unite.dto.DocTrip;
import com.hta2405.unite.dto.DocVacation;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.enums.AttendType;
import com.hta2405.unite.enums.DocType;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public List<DocVacation> getUsedVacationList(String empId, int year) {
        String sql = """
                SELECT VACATION_COUNT, VACATION_TYPE, DOC_CREATE_DATE, DOC_CONTENT, VACATION_START, VACATION_END
                 FROM DOC
                 NATURAL JOIN DOC_VACATION
                 WHERE DOC_WRITER = ?
                 AND EXTRACT(YEAR FROM VACATION_START) = ?
                """;
        List<DocVacation> list = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DocVacation(null,
                        empId,
                        DocType.VACATION,
                        null,
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        true,
                        null,
                        rs.getTimestamp("doc_create_date").toLocalDateTime().toLocalDate(),
                        rs.getDate("vacation_start").toLocalDate(),
                        rs.getDate("vacation_end").toLocalDate(),
                        rs.getInt("vacation_count"),
                        AttendType.fromString(rs.getString("vacation_type")),
                        null,
                        null,
                        null,
                        null
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public int getUsedAnnualVacationCount(String empId, int year) {
        String sql = """
                SELECT SUM(VACATION_COUNT) FROM DOC NATURAL JOIN DOC_VACATION
                WHERE DOC_WRITER = ?
                AND VACATION_TYPE = '연차'
                AND EXTRACT(YEAR FROM VACATION_START) = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int attendOut(String empId, LocalDate date) {
        String sql = """
                    UPDATE ATTEND SET
                    ATTEND_OUT = ?
                    WHERE EMP_ID = ? AND ATTEND_DATE = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, empId);
            ps.setDate(3, Date.valueOf(date));
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("퇴근처리 오류");
        }
        return 0;
    }

    public boolean insertVacation(DocVacation doc) {
        String sql = """
                    INSERT INTO ATTEND(EMP_ID, ATTEND_DATE, ATTEND_TYPE)
                    VALUES(?, ?, ?)
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDate day = doc.getVacationStart();
            while (!day.isAfter(doc.getVacationEnd())) {
                ps.setString(1, doc.getDocWriter());
                ps.setString(2, String.valueOf(day));
                ps.setString(3, doc.getVacationType().getTypeName());
                day = day.plusDays(1);
                ps.addBatch();
            }
            int[] result = ps.executeBatch();
            return result.length == doc.getVacationEnd().compareTo(doc.getVacationStart()) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("휴가 반영 오류");
        }
        return false;
    }

    public boolean insertTrip(DocTrip doc) {
        String sql = """
                    INSERT INTO ATTEND(EMP_ID, ATTEND_DATE, ATTEND_IN, ATTEND_OUT, ATTEND_TYPE)
                    VALUES(?, ?, ?, ?, ?)
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDate day = doc.getTripStart();
            while (!day.isAfter(doc.getTripEnd())) {
                ps.setString(1, doc.getDocWriter());
                ps.setString(2, String.valueOf(day));
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(day, LocalTime.of(9, 0))));
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.of(day, LocalTime.of(18, 0))));
                ps.setString(5, AttendType.TRIP.getTypeName());
                day = day.plusDays(1);
                ps.addBatch();
            }
            int[] result = ps.executeBatch();
            return result.length == doc.getTripEnd().compareTo(doc.getTripStart()) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("휴가 반영 오류");
        }
        return false;
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
                AttendType.fromString(rs.getString("attend_type")
                ));
    }


}
