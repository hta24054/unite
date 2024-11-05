package com.hta2405.unite.dao;

import com.hta2405.unite.dto.EmpInfo;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
				SELECT e.emp_id, e.password, e.ename,
				e.dept_id, e.job_id, e.gender, e.email,
				e.tel, e.mobile, e.img_path, e.img_original,
				e.img_uuid, e.img_type, e.hired, ei.mobile2,
				ei.hiredate, ei.hiretype, ei.birthday,
				ei.birthdaytype,
				ei.school, ei.major, ei.bank, ei.account,
				ei.address, ei.married, ei.child, ei.etype,
				ei.vacation_count FROM emp e JOIN emp_info
				ei ON e.emp_id = ei.emp_id WHERE e.emp_id = ?
				""";

		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, empId);
			ResultSet rs = ps.executeQuery();

			System.out.println("Executing query with emp_id: " + empId); // 디버그 출력

			if (rs.next()) {
				empInfo = new EmpInfo();
				empInfo.setEmpId(rs.getString("emp_id"));
				empInfo.setEname(rs.getString("ename"));
				empInfo.setGender(rs.getString("gender"));
				empInfo.setEmail(rs.getString("email"));
				empInfo.setTel(rs.getString("tel"));
				empInfo.setDeptId(rs.getString("dept_id"));
				empInfo.setJobName(rs.getString("job_id"));
				empInfo.setMobile(rs.getString("mobile"));
				empInfo.setHireDate(rs.getDate("hiredate"));
				empInfo.setHireType(rs.getString("hiretype"));
				empInfo.setBirthDay(rs.getDate("birthday"));
				empInfo.setBirthdayType(rs.getString("birthdayType"));
				empInfo.setSchool(rs.getString("school"));
				empInfo.setMajor(rs.getString("major"));
				empInfo.setBank(rs.getString("bank"));
				empInfo.setAccount(rs.getString("account"));
				empInfo.setAddress(rs.getString("address"));
				empInfo.setMarried(rs.getBoolean("married"));
				empInfo.setEtype(rs.getString("etype"));
				empInfo.setMobile2(rs.getString("mobile2"));
				// empInfo.setCertName(rs.getString("cert_name"));
				// empInfo.setLangName(rs.getString("lang_name"));
				empInfo.setChild(rs.getBoolean("child"));
			} else {
				System.out.println("No employee found with ID: " + empId); // 디버그 출력
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empInfo;
	}

	public List<String> getCertNamesByEmpId(String empId) {
		List<String> certNames = new ArrayList<>();
		String sql = "SELECT cert_name FROM cert WHERE emp_id = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, empId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				certNames.add(rs.getString("cert_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return certNames;
	}

	public boolean updateEmpInfo(EmpInfo empInfo) throws SQLException {
		boolean update = false;
		String sqlEmp = """
				UPDATE emp
				SET email = ?, tel = ?, mobile = ?
				WHERE emp_id = ?
				""";
		String sqlEmpInfo = """
				UPDATE emp_info
				SET mobile2 = ?, address = ?, married = ?
				WHERE emp_id = ?
				""";

		try (Connection conn = ds.getConnection()) {
			conn.setAutoCommit(false);

			try (PreparedStatement pstmtEmp = conn.prepareStatement(sqlEmp);
					PreparedStatement pstmtEmpInfo = conn.prepareStatement(sqlEmpInfo)) {
				// emp 테이블 업데이트
				pstmtEmp.setString(1, empInfo.getEmail());
				pstmtEmp.setString(2, empInfo.getTel());
				pstmtEmp.setString(3, empInfo.getMobile());
				pstmtEmp.setString(4, empInfo.getEmpId());
				int rowsEmp = pstmtEmp.executeUpdate();

				// emp_info 테이블 업데이트
				pstmtEmpInfo.setString(1, empInfo.getMobile2());
				pstmtEmpInfo.setString(2, empInfo.getAddress());
				pstmtEmpInfo.setInt(3, empInfo.getMarried() ? 1 : 0); // boolean을 NUMBER로 변환
				pstmtEmpInfo.setString(4, empInfo.getEmpId());
				int rowsEmpInfo = pstmtEmpInfo.executeUpdate();

				if (rowsEmp > 0 && rowsEmpInfo > 0) {
					conn.commit();
					update = true;
				} else {
					conn.rollback();
				}
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
			}
		}
		return update;
	}

}
