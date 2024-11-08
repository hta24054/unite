package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Holiday;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayDao {
    private DataSource ds;

    public HolidayDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public int insertHoliday(LocalDate localDate, String holidayName) {
        String sql = """
                    INSERT INTO HOLIDAY (HOLIDAY_DATE, HOLIDAY_NAME)
                    VALUES (?, ?)
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(localDate));
            ps.setString(2, holidayName);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHolidayName(LocalDate localDate) {
        String sql = """
                    SELECT HOLIDAY_NAME FROM HOLIDAY
                    WHERE HOLIDAY_DATE = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(localDate));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int updateHoliday(String holidayName, LocalDate holidayDate) {
        String sql = """
                   UPDATE HOLIDAY
                   SET HOLIDAY_NAME = ?
                   WHERE HOLIDAY_DATE = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, holidayName);
            ps.setDate(2, Date.valueOf(holidayDate));

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Holiday> getHoliday(LocalDate startDate, LocalDate endDate) {
        ArrayList<Holiday> HolidayList = new ArrayList<>();
        String sql = """
                    SELECT * FROM HOLIDAY
                    WHERE HOLIDAY_DATE BETWEEN ? and ?
                    ORDER BY HOLIDAY_DATE
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Holiday holiday = new Holiday(rs.getLong("holiday_id"),
                        rs.getDate("holiday_date").toLocalDate(),
                        rs.getString("holiday_name"));
                HolidayList.add(holiday);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return HolidayList;
    }

    public List<Holiday> getHolidayWithPaging(LocalDate startDate, LocalDate endDate, int page) {
        ArrayList<Holiday> HolidayList = new ArrayList<>();
        String sql = """
                    SELECT * FROM HOLIDAY
                    WHERE HOLIDAY_DATE BETWEEN ? and ?
                    ORDER BY HOLIDAY_DATE
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Holiday holiday = new Holiday(rs.getLong("holiday_id"),
                        rs.getDate("holiday_date").toLocalDate(),
                        rs.getString("holiday_name"));
                HolidayList.add(holiday);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return HolidayList;
    }

    public int deleteHoliday(LocalDate date) {
        String sql = """
                    DELETE FROM HOLIDAY
                    WHERE HOLIDAY_DATE = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("휴일 삭제 실패");
            e.printStackTrace();
            return 0;
        }
    }
}
