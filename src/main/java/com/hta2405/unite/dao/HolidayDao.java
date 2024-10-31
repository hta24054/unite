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
                                       SELECT ?, ? FROM DUAL
                                       WHERE NOT EXISTS (
                                           SELECT 1 FROM HOLIDAY WHERE HOLIDAY_DATE = ?
                                       )
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(localDate));
            ps.setString(2, holidayName);
            ps.setDate(3, Date.valueOf(localDate));

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
            if(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
