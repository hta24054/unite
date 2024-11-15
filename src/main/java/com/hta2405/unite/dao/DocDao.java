package com.hta2405.unite.dao;

import com.hta2405.unite.dto.*;
import com.hta2405.unite.enums.DocType;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
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

    public List<DocWithSigner> getWaitingListByEmpId(String empId) {
        List<DocWithSigner> list = new ArrayList<>();
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
                                          AND SIGN_TIME IS NULL)
                    AND s.SIGN_TIME IS NULL
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
                list.add(new DocWithSigner(doc, rs.getString("signer"), rs.getString("signername")));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int insertBuyDoc(DocBuy docBuy, List<String> signArr) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            int docResult = insertDoc(docBuy, signArr, conn);
            if (docResult == 1) {
                long docId = -1L;
                String idSql = "SELECT SEQ_DOC.CURRVAL FROM dual";
                try (PreparedStatement idStmt = conn.prepareStatement(idSql);
                     ResultSet rs = idStmt.executeQuery()) {
                    if (rs.next()) {
                        docId = rs.getLong(1);
                    } else {
                        throw new SQLException("생성된 키를 가져오지 못했습니다.");
                    }
                }
                String sql = """
                            INSERT INTO DOC_BUY(DOC_ID)
                            VALUES (?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, docId);
                    int result = ps.executeUpdate();
                    if (result == 1) {
                        long docBuyId = -1L;
                        String idSql2 = "SELECT SEQ_DOC_BUY.CURRVAL FROM dual";
                        try (PreparedStatement idStmt = conn.prepareStatement(idSql2);
                             ResultSet rs = idStmt.executeQuery()) {
                            if (rs.next()) {
                                docBuyId = rs.getLong(1);
                            } else {
                                throw new SQLException("생성된 키를 가져오지 못했습니다.");
                            }
                        }
                        int itemResult = insertBuyItem(docBuy, docBuyId, conn);
                        if (itemResult == docBuy.getBuyList().size()) {
                            conn.commit();
                            conn.setAutoCommit(true);
                            return 1;
                        }
                    } else {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private int insertBuyItem(DocBuy docBuy, Long docBuyId, Connection conn) {
        String sql = """
                INSERT INTO BUY_ITEM (DOC_BUY_ID, PRODUCT_NAME, STANDARD, QUANTITY, PRICE)
                VALUES (?,?,?,?,?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DocBuyItem item : docBuy.getBuyList()) {
                ps.setLong(1, docBuyId);
                ps.setString(2, item.getProductName());
                ps.setString(3, item.getStandard());
                ps.setLong(4, item.getQuantity());
                ps.setLong(5, item.getPrice());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertVacationDoc(DocVacation docVacation, List<String> signList) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            int docResult = insertDoc(docVacation, signList, conn);
            if (docResult == 1) {
                long docId = -1L;
                String idSql = "SELECT seq_doc.CURRVAL FROM dual";
                try (PreparedStatement idStmt = conn.prepareStatement(idSql);
                     ResultSet rs = idStmt.executeQuery()) {
                    if (rs.next()) {
                        docId = rs.getLong(1);
                    } else {
                        throw new SQLException("생성된 키를 가져오지 못했습니다.");
                    }
                }
                String sql = """
                            INSERT INTO DOC_VACATION(DOC_ID, VACATION_START, VACATION_END, VACATION_TYPE, 
                                                    VACATION_COUNT, VACATION_FILE_PATH, VACATION_FILE_ORIGINAL,
                                                    VACATION_FILE_UUID, VACATION_FILE_TYPE)
                            VALUES (?,?,?,?,?,?,?,?,?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, docId);
                    ps.setDate(2, Date.valueOf(docVacation.getVacationStart()));
                    ps.setDate(3, Date.valueOf(docVacation.getVacationEnd()));
                    ps.setString(4, docVacation.getVacationType());
                    ps.setInt(5, docVacation.getVacationCount());
                    ps.setString(6, docVacation.getVacationFilePath());
                    ps.setString(7, docVacation.getVacationFileOriginal());
                    ps.setString(8, docVacation.getVacationFileUUID());
                    ps.setString(9, docVacation.getVacationFileType());
                    int result = ps.executeUpdate();
                    if (result == 1) {
                        conn.commit();
                        conn.setAutoCommit(true);
                        return 1;
                    } else {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int insertTripDoc(DocTrip docTrip, List<String> signList) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            int docResult = insertDoc(docTrip, signList, conn);
            if (docResult == 1) {
                long docId = -1L;
                String idSql = "SELECT seq_doc.CURRVAL FROM dual";
                try (PreparedStatement idStmt = conn.prepareStatement(idSql);
                     ResultSet rs = idStmt.executeQuery()) {
                    if (rs.next()) {
                        docId = rs.getLong(1);
                    } else {
                        throw new SQLException("생성된 키를 가져오지 못했습니다.");
                    }
                }
                String sql = """
                            INSERT INTO DOC_TRIP(DOC_ID, TRIP_START, TRIP_END, TRIP_LOC, TRIP_PHONE,
                                                TRIP_INFO, CARD_START, CARD_END, CARD_RETURN)
                            VALUES (?,?,?,?,?,?,?,?,?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, docId);
                    ps.setDate(2, Date.valueOf(docTrip.getTripStart()));
                    ps.setDate(3, Date.valueOf(docTrip.getTripEnd()));
                    ps.setString(4, docTrip.getTripLoc());
                    ps.setString(5, docTrip.getTripPhone());
                    ps.setString(6, docTrip.getTripInfo());
                    ps.setDate(7, Date.valueOf(docTrip.getCardStart()));
                    ps.setDate(8, Date.valueOf(docTrip.getCardEnd()));
                    ps.setDate(9, Date.valueOf(docTrip.getCardReturn()));
                    int result = ps.executeUpdate();
                    if (result == 1) {
                        conn.commit();
                        conn.setAutoCommit(true);
                        return 1;
                    } else {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int insertGeneralDoc(Doc doc, List<String> signList) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            int result = insertDoc(doc, signList, conn);
            if (result == 1) {
                conn.commit();
                conn.setAutoCommit(true);
                return 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int insertDoc(Doc doc, List<String> signList, Connection conn) throws SQLException {
        String sql = """
                INSERT INTO DOC (DOC_WRITER, DOC_TYPE, DOC_TITLE, DOC_CONTENT, DOC_CREATE_DATE)
                VALUES (?,?,?,?,?)
                """;
        long docId = -1L;

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
            List<Sign> list = makeSignListByParam(signList, docId);
            assert list != null;
            int signResult = insertSign(list, conn);
            if (signResult == list.size()) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("문서 또는 서명 삽입 오류", e);
        }
    }

    public int insertSign(List<Sign> list, Connection conn) throws SQLException {
        String sql = """
                    INSERT INTO SIGN (EMP_ID, DOC_ID, SIGN_ORDER, SIGN_TIME)
                    VALUES (?,?,?,?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Sign sign : list) {
                ps.setString(1, sign.getEmpId());
                ps.setLong(2, sign.getDocId());
                ps.setInt(3, sign.getSignOrder());
                if (sign.getSignTime() == null) {
                    ps.setNull(4, Types.DATE);
                } else {
                    ps.setTimestamp(4, Timestamp.valueOf(sign.getSignTime()));
                }
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length;
        }
    }

    public Doc getGeneralDocByDocId(Long docId) {
        String sql = """
                    SELECT * FROM DOC
                    WHERE DOC_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, docId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Doc(rs.getLong("doc_id"),
                        rs.getString("doc_writer"),
                        DocType.fromString(rs.getString("doc_type")),
                        rs.getString("doc_title"),
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        rs.getInt("sign_finish") == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("문서 가져오기 오류");
        }
        return null;
    }

    public List<Sign> getSignListByDocId(Long docId) {
        List<Sign> list = new ArrayList<>();
        String sql = """
                    SELECT * FROM SIGN
                    WHERE DOC_ID = ?
                    ORDER BY SIGN_ORDER
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, docId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Sign(rs.getLong("sign_id"),
                        rs.getString("emp_id"),
                        rs.getLong("doc_id"),
                        rs.getInt("sign_order"),
                        rs.getTimestamp("sign_time") == null
                                ? null : rs.getTimestamp("sign_time").toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public DocBuy getBuyDoc(Long docId) {
        String sql = """
                select *
                from DOC d join DOC_BUY b
                    on d.DOC_ID = b.DOC_ID
                WHERE d.DOC_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, docId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                List<DocBuyItem> items = getBuyItemListById(rs.getLong("doc_buy_id"));

                return new DocBuy(rs.getLong("doc_id"),
                        rs.getString("doc_writer"),
                        DocType.fromString(rs.getString("doc_type")),
                        rs.getString("doc_title"),
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        rs.getInt("sign_finish") == 1,
                        rs.getLong("doc_buy_id"),
                        items);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DocBuyItem> getBuyItemListById(Long docBuyId) {
        List<DocBuyItem> list = new ArrayList<>();
        String sql = """
                    select *
                    from BUY_ITEM
                    WHERE DOC_BUY_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, docBuyId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DocBuyItem(rs.getLong("buy_item_id"),
                        rs.getLong("doc_buy_id"),
                        rs.getString("product_name"),
                        rs.getString("standard"),
                        rs.getLong("quantity"),
                        rs.getLong("price")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public DocTrip getTripDocById(Long docId) {
        String sql = """
                    select *
                    from doc d join DOC_TRIP t
                    on d.doc_id = t.DOC_ID
                    WHERE D.DOC_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, docId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DocTrip(rs.getLong("doc_id"),
                        rs.getString("doc_writer"),
                        DocType.fromString(rs.getString("doc_type")),
                        rs.getString("doc_title"),
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        rs.getInt("sign_finish") == 1,
                        rs.getLong("doc_trip_id"),
                        rs.getDate("trip_start").toLocalDate(),
                        rs.getDate("trip_end").toLocalDate(),
                        rs.getString("trip_loc"),
                        rs.getString("trip_phone"),
                        rs.getString("trip_info"),
                        rs.getDate("card_start").toLocalDate(),
                        rs.getDate("card_end").toLocalDate(),
                        rs.getDate("card_return").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DocVacation getVacationDoc(Long docId) {
        String sql = """
                    select *
                    from doc d join DOC_VACATION v
                    on d.doc_id = v.DOC_ID
                    WHERE D.DOC_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, docId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DocVacation(rs.getLong("doc_id"),
                        rs.getString("doc_writer"),
                        DocType.fromString(rs.getString("doc_type")),
                        rs.getString("doc_title"),
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        rs.getInt("sign_finish") == 1,
                        rs.getDate("doc_create_date").toLocalDate(),
                        rs.getDate("vacation_start").toLocalDate(),
                        rs.getDate("vacation_end").toLocalDate(),
                        rs.getInt("vacation_count"),
                        rs.getString("vacation_type"),
                        rs.getString("vacation_file_Path"),
                        rs.getString("vacation_file_original"),
                        rs.getString("vacation_file_uuid"),
                        rs.getString("vacation_file_type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Sign> makeSignListByParam(List<String> signList, Long docId) {
        if (docId == -1L) {
            return null;
        }
        List<Sign> list = new ArrayList<>();
        for (int i = 0; i < signList.size(); i++) {
            if (i == 0) {
                list.add(new Sign(null, signList.get(i), docId, i, LocalDateTime.now())); //기안자는 결재완료처리
            } else {
                list.add(new Sign(null, signList.get(i), docId, i, null));
            }
        }
        return list;
    }

    public List<DocWithSigner> getInProgressDocByEmpId(String empId) {
        List<DocWithSigner> list = new ArrayList<>();
        String sql = """
                    SELECT DOC.*, EMP.EMP_ID, EMP.ENAME
                    FROM DOC
                             JOIN SIGN ON DOC.DOC_ID = SIGN.DOC_ID
                             JOIN EMP ON SIGN.EMP_ID = EMP.EMP_ID
                             JOIN (
                        SELECT DOC_ID, MIN(SIGN_ORDER) AS min_sign_order
                        FROM SIGN
                        WHERE SIGN_TIME IS NULL
                        GROUP BY DOC_ID
                    ) min_sign ON SIGN.DOC_ID = min_sign.DOC_ID AND SIGN.SIGN_ORDER = min_sign.min_sign_order
                    WHERE doc.SIGN_FINISH = 0 AND SIGN.SIGN_TIME IS NULL AND DOC_WRITER = ?
                    ORDER BY DOC_CREATE_DATE
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Doc doc = new Doc(rs.getLong("doc_id"),
                        rs.getString("doc_writer"),
                        DocType.fromString(rs.getString("doc_type")),
                        rs.getString("doc_title"),
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        rs.getInt("sign_finish") == 1
                );
                list.add(new DocWithSigner(doc,
                        rs.getString("emp_id"),
                        rs.getString("ename"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Doc> getFinishedSignedDocListByEmpId(String empId) {
        List<Doc> list = new ArrayList<>();
        String sql = """
                    select *
                    from DOC JOIN SIGN
                    ON DOC.DOC_ID = SIGN.DOC_ID
                    WHERE EMP_ID = ? and SIGN_FINISH = 1
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Doc(rs.getLong("doc_id"),
                        rs.getString("doc_writer"),
                        DocType.fromString(rs.getString("doc_type")),
                        rs.getString("doc_title"),
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        rs.getInt("sign_finish") == 1
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Doc> getFinishedDeptDocListByEmpId(String empId) {
        List<Doc> list = new ArrayList<>();
        String sql = """
                    SELECT *
                    FROM DOC
                    JOIN EMP ON EMP.EMP_ID = DOC.DOC_WRITER
                    WHERE EMP.DEPT_ID = (SELECT DEPT_ID FROM EMP WHERE EMP_ID = ?)
                    AND SIGN_FINISH = 1
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Doc(rs.getLong("doc_id"),
                        rs.getString("doc_writer"),
                        DocType.fromString(rs.getString("doc_type")),
                        rs.getString("doc_title"),
                        rs.getString("doc_content"),
                        rs.getTimestamp("doc_create_date").toLocalDateTime(),
                        rs.getInt("sign_finish") == 1
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
