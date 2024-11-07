package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class JobDao {
    private DataSource ds;

    public JobDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public HashMap<Long, String> getIdToJobNameMap() {
        HashMap<Long, String> map = new HashMap<>();
        String sql = """
                    SELECT job_id, job_name from job
                    order by JOB_ID
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getLong("job_id"), rs.getString("job_name"));
            }
        } catch (SQLException e) {
            System.out.println("jobMap 불러오기 에러");
            e.printStackTrace();
        }
        return map;
    }
}
