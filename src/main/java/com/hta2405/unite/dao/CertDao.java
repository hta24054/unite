package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.hta2405.unite.dto.Cert;

public class CertDao {
	private DataSource ds;

	public CertDao() {
		try {
			InitialContext init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB연결 실패 " + e.getMessage());
		}
	}

    public List<Cert> getCertByEmpId(String empId) {
        List<Cert> certs = new ArrayList<>();
        String sql = "SELECT * FROM cert WHERE emp_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cert cert = new Cert();
                cert.setCertId(rs.getLong("cert_id"));
                cert.setCertName(rs.getString("cert_name"));
                cert.setEmpId(rs.getString("emp_id"));
                certs.add(cert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return certs;
    }
}
