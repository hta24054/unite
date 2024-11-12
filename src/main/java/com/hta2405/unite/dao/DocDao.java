package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Doc;
import com.hta2405.unite.dto.DocWithWaitingSigner;
import com.hta2405.unite.dto.Sign;
import com.hta2405.unite.enums.DocType;

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

    public List<DocWithWaitingSigner> getWaitingListByEmpId(String empId) {
        List<DocWithWaitingSigner> list = new ArrayList<>();
        String sql = """
                  SELECT d.doc_id AS id,
                         d.DOC_WRITER AS writer,
                         d.DOC_TYPE AS type,
                         d.DOC_TITLE AS title,
                         d.DOC_CONTENT AS content,
                         d.DOC_CREATE_DATE AS createDate,
                         s.EMP_ID AS signer,
                         e.ENAME AS signerName
                  FROM doc d
                           JOIN sign s ON d.DOC_ID = s.DOC_ID
                           JOIN emp e ON s.EMP_ID = e.EMP_ID
                  WHERE s.SIGN_ORDER = (SELECT MIN(SIGN_ORDER)
                                        FROM sign
                                        WHERE DOC_ID = DOC_ID
                                          AND SIGNED = 0)
                    AND s.SIGNED = 0
                    AND s.EMP_ID = ?
                  ORDER BY createDate
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Doc doc = new Doc(rs.getLong("id"),
                        rs.getString("writer"),
                        DocType.fromString(rs.getString("type")),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("createDate").toLocalDateTime(),
                        false);
                list.add(new DocWithWaitingSigner(doc, rs.getString("signer"), rs.getString("signername")));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int insertGeneralDoc(Doc doc, String[] signArr) {
        String sql = """
                INSERT INTO DOC (DOC_WRITER, DOC_TYPE, DOC_TITLE, DOC_CONTENT, DOC_CREATE_DATE)
                VALUES (?,?,?,?,?)
                """;
        long docId = -1L;

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, doc.getDocWriter());
                ps.setString(2, doc.getDocType().getType()); // Enum의 문자열 값 사용
                ps.setString(3, doc.getDocTitle());
                ps.setString(4, doc.getDocContent());
                ps.setTimestamp(5, Timestamp.valueOf(doc.getDocCreateDate()));
                int result = ps.executeUpdate();

                // DOC_ID 가져오기
                if (result == 1) {
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

                // SIGN 테이블에 서명 리스트 삽입
                List<Sign> list = makeSignList(signArr, docId);
                int signResult = insertSign(list, conn);
                if (signResult == list.size()) {
                    conn.commit();
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
