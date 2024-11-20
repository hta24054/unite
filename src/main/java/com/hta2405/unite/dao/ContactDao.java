package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Emp;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDao {
	private DataSource ds;

	public ContactDao() {
		try {
			InitialContext init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB연결 실패 " + e.getMessage());
		}
	}

// ContactDao 클래스의 일부
	public List<Emp> getAllContactsOrderByName() {
		List<Emp> list = new ArrayList<>();
		String sql = """
				    SELECT *
				    FROM emp e
				    JOIN dept d ON e.dept_id = d.dept_id
				    JOIN job j ON e.job_id = j.job_id
				    ORDER BY e.ename , j.job_rank
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));
				emp.setJobId(rs.getLong("job_id"));
				emp.setDeptId(rs.getLong("dept_id"));
				list.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Emp> getAllContactsOrderByJob() {
		List<Emp> list = new ArrayList<>();
		String sql = """
				    SELECT *
				    FROM emp e
				    JOIN dept d ON e.dept_id = d.dept_id
				    JOIN job j ON e.job_id = j.job_id
				    ORDER BY j.job_rank , e.ename
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));
				emp.setJobId(rs.getLong("job_id"));
				emp.setDeptId(rs.getLong("dept_id"));
				list.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Emp> getContactsByDeptOrderByJob(Long deptId) {
		List<Emp> list = new ArrayList<>();
		String sql = """
					SELECT * FROM EMP e
					JOIN JOB j
					ON e.JOB_ID = j.JOB_ID
					JOIN DEPT d
					ON e.DEPT_ID = d.DEPT_ID
					WHERE e.DEPT_ID = ?
					ORDER BY j.JOB_RANK , e.ENAME
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, deptId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));
				emp.setJobId(rs.getLong("job_id"));
				emp.setDeptId(rs.getLong("dept_id"));
				list.add(emp);
			}
		} catch (SQLException e) {
			System.out.println("부서 회원 정보 가져오기 오류");
			e.printStackTrace();
		}
		return list;
	}

	public List<Emp> getContactsByName(String name) {
		List<Emp> list = new ArrayList<>();
		String sql = """
				    SELECT *
				    FROM emp e
				    JOIN dept d ON e.dept_id = d.dept_id
				    JOIN job j ON e.job_id = j.job_id
				    WHERE e.ename LIKE ?
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + name + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));
				emp.setJobId(rs.getLong("job_id"));
				emp.setDeptId(rs.getLong("dept_id"));
				list.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
