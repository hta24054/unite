package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.dao.*;
import java.sql.ResultSet;

public class EmpInfoDao {
	private DataSource ds;

	public EmpInfoDao() {

		try {
			InitialContext init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB연결 실패 " + e.getMessage());
		}
	}

	public EmpInfo getEmpInfoById(String empId) {
		EmpInfo empInfo = null;
		String sql = """
				SELECT * FROM emp_info
				WHERE emp_id=?
					""";

		try (Connection conn = ds.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, empId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				empInfo = new EmpInfo();
				empInfo.setEname(rs.getString("ename"));
				empInfo.setGender(rs.getString("gender"));
				empInfo.setEmail(rs.getString("email"));
				empInfo.setTel(rs.getString("tel"));
				empInfo.setDeptId(rs.getString("deptId"));
				empInfo.setJobId(rs.getString("jobId"));
				empInfo.setMobile(rs.getString("mobile"));
				empInfo.setCompany(rs.getString("company"));
				empInfo.setHiredate(rs.getDate("hiredate"));
				empInfo.setHiretype(rs.getString("hiretype"));
				empInfo.setBank(rs.getString("bank"));
				empInfo.setAccount(rs.getString("account"));
				empInfo.setMobile2(rs.getString("mobile2"));
				empInfo.setEtype(rs.getString("etype"));
				empInfo.setBirthDate(rs.getDate("birth_date"));
				empInfo.setAddress(rs.getString("address"));
				empInfo.setSchool(rs.getString("school"));
				empInfo.setMarried(rs.getBoolean("married"));
				//empInfo.setCertName(rs.getString("certName"));
				empInfo.setMajor(rs.getString("major"));
				//empInfo.setLangName(rs.getString("langName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empInfo;
	}

	public boolean updateEmpInfo(EmpInfo empInfo) throws SQLException {
		boolean update = false;
		String sql = """
				UPDATE emp_info
				SET email=?, tel=?, mobile=?,
				mobile2=?, address=?, married=?
				""";

		try (Connection conn = ds.getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, empInfo.getEmail());
			pstmt.setString(2, empInfo.getTel());
			pstmt.setString(3, empInfo.getMobile());
			pstmt.setString(4, empInfo.getMobile2());
			pstmt.setString(5, empInfo.getAddress());
			pstmt.setBoolean(6, empInfo.getMarried());

		}
		return update;
	}
}
