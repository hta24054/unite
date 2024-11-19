package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.hta2405.unite.dto.Lang;

public class LangDao {


    private DataSource ds;

	public LangDao() {
		try {
			InitialContext init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB연결 실패 " + e.getMessage());
		}
	}
    public List<Lang> getLangByEmpId(String empId) {
        List<Lang> langs = new ArrayList<>();
        String sql = "SELECT * FROM lang WHERE emp_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Lang lang = new Lang();
                lang.setLangId(rs.getLong("lang_id"));
                lang.setLangName(rs.getString("lang_name"));
                lang.setEmpId(rs.getString("emp_id"));
                langs.add(lang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return langs;
    }
}
