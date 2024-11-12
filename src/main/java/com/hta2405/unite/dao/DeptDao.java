package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Dept;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class DeptDao {
	private DataSource ds;

	public DeptDao() {
		try {
			InitialContext init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB연결 실패 " + e.getMessage());
		}
	}

	public String getManagerIdByDeptId(Long deptId) {
		String sql = """
				    SELECT DEPT_MANAGER FROM DEPT
				    WHERE DEPT_ID = ?
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, deptId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public String getDeptNameById(Long deptId) {
		String sql = """
				    SELECT DEPT_NAME FROM DEPT
				    WHERE DEPT_ID = ?
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, deptId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public HashMap<Long, String> getIdToDeptNameMap() {
		HashMap<Long, String> map = new HashMap<>();
		String sql = """
				    SELECT dept_id, dept_name from DEPT
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getLong("dept_id"), rs.getString("dept_name"));
			}
		} catch (SQLException e) {
			System.out.println("deptMap 불러오기 에러");
			e.printStackTrace();
		}
		return map;
	}

	public Dept getDeptByEmpId(String empId) {
		Dept dept = null;
		String sql = "SELECT * FROM dept WHERE dept_id = (SELECT dept_id FROM emp WHERE emp_id = ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, empId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				dept = new Dept();
				dept.setDeptId(rs.getLong("dept_id"));
				dept.setDeptName(rs.getString("dept_name"));
				dept.setDeptManager(rs.getString("dept_manager"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dept;
	}

	public List<Emp> getDeptEmps(String empId) {
		String sql = """
				SELECT emp_id, ename, dept_id, job_id, email, tel
				FROM emp
				WHERE dept_id = (SELECT dept_id
								 FROM emp
								 WHERE emp_id = ?)
				""";
		List<Emp> empList = new ArrayList<>();
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, empId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Emp emp = new Emp();
					emp.setEmpId(rs.getString("emp_id"));
					emp.setEname(rs.getString("ename"));
					emp.setDeptId(rs.getLong("dept_id"));
					emp.setJobId(rs.getLong("job_id"));
					emp.setEmail(rs.getString("email"));
					emp.setTel(rs.getString("tel"));
					empList.add(emp);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empList;
	}

	public Long getDeptIdByDeptName(String deptName) {
		String sql = """
				select dept_id from dept
				where dept_name = ?
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, deptName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
