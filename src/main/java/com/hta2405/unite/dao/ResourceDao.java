package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Resource;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDao {
    private DataSource ds;

    public ResourceDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public List<Resource> getAllResource() {
        List<Resource> list = new ArrayList<>();
        String sql = """
                    SELECT * FROM RESC
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resource resource = new Resource(rs.getLong("resc_id"),
                        rs.getString("resc_type"),
                        rs.getString("resc_name"),
                        rs.getString("resc_info") == null ?
                                null : rs.getString("resc_info"),
                        rs.getInt("resc_usable") == 1);
                list.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("자원 가져오기 오류");
        }
        return list;
    }

    public int insertResource(Resource resource) {
        String sql = """
                   INSERT INTO RESC (RESC_TYPE, RESC_NAME, RESC_INFO, RESC_USABLE)
                   VALUES (?,?,?,?)
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resource.getResourceType());
            ps.setString(2, resource.getResourceName());
            ps.setString(3, resource.getResourceInfo());
            ps.setInt(4, resource.isResourceUsable() ? 1 : 0);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("자원 등록 오류");
        }
        return 0;
    }

    public int deleteResources(List<Long> resourceIds) {
        String sql = "DELETE FROM RESC WHERE RESC_ID = ?";
        int deleteCount = 0;

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false); //트랜잭션 시작
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Long resourceId : resourceIds) {
                    ps.setLong(1, resourceId);
                    deleteCount += ps.executeUpdate();
                }
                conn.commit(); //모든 삭제가 성공
            } catch (SQLException e) {
                conn.rollback(); //오류발생시 롤백
                System.out.println("자원 삭제 오류, 롤백되었습니다: " + e.getMessage());
                deleteCount = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 트랜잭션 오류: " + e.getMessage());
        }
        return deleteCount;
    }

    public int updateResource(Resource resource) {
        String sql = """
                    UPDATE RESC
                    SET RESC_TYPE = ?, RESC_NAME =?, RESC_INFO =?, RESC_USABLE =?
                    WHERE RESC_ID=?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resource.getResourceType());
            ps.setString(2, resource.getResourceName());
            ps.setString(3, resource.getResourceInfo());
            ps.setInt(4, resource.isResourceUsable() ? 1 : 0);
            ps.setLong(5, resource.getResourceId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("자원 수정 오류");
        }
        return 0;
    }
}
