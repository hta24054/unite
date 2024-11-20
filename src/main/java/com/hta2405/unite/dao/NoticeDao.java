package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Notice;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDao {
    private DataSource ds;

    public NoticeDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public List<Notice> getAllNotice() {
        List<Notice> list = new ArrayList<>();
        String sql = """
                SELECT * FROM NOTICE
                ORDER BY NOTICE_END_DATE
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Notice(rs.getLong("notice_id"),
                        rs.getString("notice_subject"),
                        rs.getString("notice_content"),
                        rs.getDate("notice_end_date").toLocalDate()));
            }
        } catch (SQLException e) {
            System.out.println("공지사항 가져오기 오류");
            e.printStackTrace();
        }
        return list;
    }


    public int deleteById(Long noticeId) {
        String sql = """
                DELETE NOTICE
                WHERE NOTICE_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, noticeId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("공지사항 가져오기 오류");
            e.printStackTrace();
        }
        return 0;
    }

    public int updateNotice(Notice notice) {
        String sql = """
                UPDATE NOTICE
                SET NOTICE_SUBJECT =?, NOTICE_CONTENT = ?, NOTICE_END_DATE =?
                WHERE NOTICE_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notice.getNoticeSubject());
            ps.setString(2, notice.getNoticeContent());
            ps.setDate(3, Date.valueOf(notice.getNoticeEndDate()));
            ps.setLong(4, notice.getNoticeId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("공지사항 수정 오류");
            e.printStackTrace();
        }
        return 0;
    }

    public int insertNotice(Notice notice) {
        String sql = """
                INSERT INTO NOTICE(NOTICE_SUBJECT, NOTICE_CONTENT, NOTICE_END_DATE)
                VALUES(?, ?, ?)
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notice.getNoticeSubject());
            ps.setString(2, notice.getNoticeContent());
            ps.setDate(3, Date.valueOf(notice.getNoticeEndDate()));
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("공지사항 등록 오류");
            e.printStackTrace();
        }
        return 0;
    }

    public List<Notice> getAliveNotice() {List<Notice> list = new ArrayList<>();
        String sql = """
                SELECT * FROM NOTICE
                where NOTICE_END_DATE >= sysdate-1
                ORDER BY NOTICE_ID desc
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Notice(rs.getLong("notice_id"),
                        rs.getString("notice_subject"),
                        rs.getString("notice_content"),
                        rs.getDate("notice_end_date").toLocalDate()));
            }
        } catch (SQLException e) {
            System.out.println("공지사항 가져오기 오류");
            e.printStackTrace();
        }
        return list;
    }
}
