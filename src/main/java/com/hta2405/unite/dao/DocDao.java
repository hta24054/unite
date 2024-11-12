package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Doc;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Sign;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocDao {
    private DataSource ds;

    public DocDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public List<Doc> getWaitingListByEmp(Emp emp) {
        return null;
    }

    public int insertGeneralDoc(Doc doc, String[] signArr) {
        String sql = """
                	INSERT INTO DOC (DOC_WRITER, DOC_TYPE, DOC_TITLE, DOC_CONTENT, DOC_CREATE_DATE)
                    VALUES (?,?,?,?,?)
                """;
        long docId = -1L;
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false); //트랜잭션 시작
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                ps.setString(1, doc.getDocWriter());
                ps.setString(2, doc.getDocType());
                ps.setString(3, doc.getDocTitle());
                ps.setString(4, doc.getDocContent());
                ps.setTimestamp(5, Timestamp.valueOf(doc.getDocCreateDate()));
                int result = ps.executeUpdate();
                if (result == 1) {
                    //자동 생성된 doc_id 시퀀스 currval로 가져오기
                    String idSql = "SELECT seq_doc.CURRVAL FROM dual";
                    try (PreparedStatement idStmt = conn.prepareStatement(idSql);
                         ResultSet rs = idStmt.executeQuery()) {
                        if (rs.next()) {
                            docId = rs.getLong(1);
                        } else {
                            throw new SQLException("생성된 키를 가져오지 못했습니다.");
                        }
                    }
                }
                List<Sign> list = makeSignList(signArr, docId);
                int signResult = insertSign(list, conn);
                if (signResult == list.size()) {
                    conn.commit();
                    conn.setAutoCommit(true);
                    return 1;
                } else {
                    conn.rollback();
                    return 0;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("문서 또는 서명 삽입 오류", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 오류", e);
        }
    }

    public int insertSign(List<Sign> list, Connection conn) throws SQLException {
        String sql = """
                    INSERT INTO SIGN (EMP_ID, DOC_ID, SIGN_ORDER, SIGNED)
                    VALUES (?,?,?,?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Sign sign : list) {
                ps.setString(1, sign.getEmpId());
                ps.setLong(2, sign.getDocId());
                ps.setInt(3, sign.getSignOrder());
                ps.setInt(4, sign.isSigned() ? 1 : 0);
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length;
        }
    }

    private List<Sign> makeSignList(String[] signArr, Long docId) {
        if (docId == -1L) {
            return null;
        }
        List<Sign> list = new ArrayList<>();
        for (int i = 0; i < signArr.length; i++) {
            if (i == 0) {
                list.add(new Sign(null, signArr[i], docId, i, true)); //기안자는 결재완료처리
            } else {
                list.add(new Sign(null, signArr[i], docId, i, false));
            }
        }
        return list;
    }
}
