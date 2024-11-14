package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.EmpDetails;
import com.hta2405.unite.dto.Dept;
import com.hta2405.unite.dto.Job;

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

	public List<EmpDetails> getAllContacts(String orderBy) {
		List<EmpDetails> contactList = new ArrayList<>();
		String sql = "SELECT e.emp_id, e.ename, e.mobile, e.tel, d.dept_name, j.job_name " + "FROM emp e "
				+ "JOIN dept d ON e.dept_id = d.dept_id " + "JOIN job j ON e.job_id = j.job_id " + "ORDER BY "
				+ orderBy;
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));

				Dept dept = new Dept();
				dept.setDeptName(rs.getString("dept_name"));

				Job job = new Job();
				job.setJobName(rs.getString("job_name"));

				EmpDetails empDetails = new EmpDetails();
				empDetails.setEmp(emp);
				empDetails.setDept(dept);
				empDetails.setJob(job);

				contactList.add(empDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public List<EmpDetails> getContactsByDeptName(String deptName) {
		List<EmpDetails> contactList = new ArrayList<>();
		String sql = "SELECT e.emp_id, e.ename, e.mobile, e.tel, d.dept_name, j.job_name " + "FROM emp e "
				+ "JOIN dept d ON e.dept_id = d.dept_id " + "JOIN job j ON e.job_id = j.job_id "
				+ "WHERE d.dept_name = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, deptName);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));

				Dept dept = new Dept();
				dept.setDeptName(rs.getString("dept_name"));

				Job job = new Job();
				job.setJobName(rs.getString("job_name"));

				EmpDetails empDetails = new EmpDetails();
				empDetails.setEmp(emp);
				empDetails.setDept(dept);
				empDetails.setJob(job);

				contactList.add(empDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public List<EmpDetails> getContactsByDept(Long deptId) {
		List<EmpDetails> contactList = new ArrayList<>();
		String sql = "SELECT e.emp_id, e.ename, e.mobile, e.tel, d.dept_name, j.job_name " + "FROM emp e "
				+ "JOIN dept d ON e.dept_id = d.dept_id " + "JOIN job j ON e.job_id = j.job_id "
				+ "WHERE e.dept_id = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, deptId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));

				Dept dept = new Dept();
				dept.setDeptName(rs.getString("dept_name"));

				Job job = new Job();
				job.setJobName(rs.getString("job_name"));

				EmpDetails empDetails = new EmpDetails();
				empDetails.setEmp(emp);
				empDetails.setDept(dept);
				empDetails.setJob(job);

				contactList.add(empDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public List<EmpDetails> getContactsByName(String name) {
		List<EmpDetails> contactList = new ArrayList<>();
		String sql = "SELECT e.emp_id, e.ename, e.mobile, e.tel, d.dept_name, j.job_name " + "FROM emp e "
				+ "JOIN dept d ON e.dept_id = d.dept_id " + "JOIN job j ON e.job_id = j.job_id "
				+ "WHERE e.ename LIKE ?";

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + name + "%");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Emp emp = new Emp();
				emp.setEmpId(rs.getString("emp_id"));
				emp.setEname(rs.getString("ename"));
				emp.setMobile(rs.getString("mobile"));
				emp.setTel(rs.getString("tel"));

				Dept dept = new Dept();
				dept.setDeptName(rs.getString("dept_name"));

				Job job = new Job();
				job.setJobName(rs.getString("job_name"));

				EmpDetails empDetails = new EmpDetails();
				empDetails.setEmp(emp);
				empDetails.setDept(dept);
				empDetails.setJob(job);

				contactList.add(empDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
}
