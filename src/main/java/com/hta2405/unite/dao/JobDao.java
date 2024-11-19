package com.hta2405.unite.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Dept;
import com.hta2405.unite.dto.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


	public List<Job> getAllJob() {
		String sql = """
                    SELECT * FROM JOB
                    WHERE JOB_NAME!='관리자' OR JOB_RANK!=99
                    ORDER BY JOB_RANK
                """;
		List<Job> list = new ArrayList<>();
		try (Connection conn = ds.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Job(rs.getInt("job_id"),
						rs.getString("job_name"),
						rs.getInt("job_rank")));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	public HashMap<Long, String> getIdToJobNameMap() {
		HashMap<Long, String> map = new HashMap<>();
		String sql = """
				    SELECT job_id, job_name from job
				    order by JOB_ID
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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

	public Job getJobByEmpId(String empId) {
		Job job = null;
		String sql = "SELECT * FROM job WHERE job_id = (SELECT job_id FROM emp WHERE emp_id = ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, empId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				job = new Job();
				job.setJobId(rs.getInt("job_id"));
				job.setJobName(rs.getString("job_name"));
				job.setJobRank(rs.getInt("job_rank"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return job;
	}

}
