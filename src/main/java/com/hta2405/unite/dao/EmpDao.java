package com.hta2405.unite.dao;

import com.hta2405.unite.dto.Emp;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmpDao {
    private DataSource ds;

    public EmpDao() {
        try {
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception e) {
            System.out.println("DB연결 실패 " + e.getMessage());
        }
    }

    public Emp getEmpById(String id) {
        String sql = """
                    SELECT * FROM EMP
                    WHERE EMP_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return makeEmp(rs);
            }
        } catch (SQLException e) {
            System.out.println("회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return null;
    }

    public List<Emp> getSubEmpListByEmp(Emp emp) {
        List<Emp> list = new ArrayList<>();
        if (emp.getEmpId().contains("admin")) {
            return getAllEmpList(list);
        }
        String deptToString = emp.getDeptId().toString();
        // 쿼리에 들어갈 deep 구함 ex) 1110 -> 1 / 1100 -> 2 / 1000 -> 3
        int deep = deptToString.length();
        for (int i = 0; i < deptToString.length(); i++) {
            if (deptToString.charAt(i) != '0') {
                deep--;
            } else {
                break;
            }
        }
        String sql = """
                    SELECT *
                    FROM EMP e NATURAL JOIN JOB j
                    where ? = floor(dept_id / power(10,?)) * power(10,?)
                    ORDER BY j.JOB_RANK, e.emp_id
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, emp.getDeptId());
            ps.setLong(2, deep);
            ps.setLong(3, deep);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(makeEmp(rs));
            }
        } catch (SQLException e) {
            System.out.println("부서 회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return list;
    }

    public List<Emp> getAllEmpList(List<Emp> list) {
        String sql = """
                    SELECT *
                    FROM EMP e NATURAL JOIN JOB j
                    ORDER BY j.JOB_RANK, e.emp_id
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(makeEmp(rs));
            }
        } catch (SQLException e) {
            System.out.println("부서 회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return list;
    }

    public int updateEmp(Emp emp) {
        String sql = """
                    UPDATE EMP SET
                    PASSWORD = ?, ENAME = ?, DEPT_ID = ?, JOB_ID = ?, GENDER = ?,
                    EMAIL = ?, TEL = ?, MOBILE = ?, MOBILE2 =?, IMG_PATH = ?, IMG_ORIGINAL = ?,
                    IMG_UUID = ?, IMG_TYPE = ?, HIREDATE =?, HIRETYPE = ?, BIRTHDAY = ?, BIRTHDAY_TYPE = ?,
                    SCHOOL = ?, MAJOR = ?, BANK =?, ACCOUNT = ?, ADDRESS = ?, MARRIED = ?, CHILD = ?, ETYPE = ?,
                    VACATION_COUNT = ?, HIRED = ?
                    WHERE EMP_ID = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getPassword());
            ps.setString(2, emp.getEname());
            ps.setLong(3, emp.getDeptId());
            ps.setLong(4, emp.getJobId());
            ps.setString(5, emp.getGender());
            ps.setString(6, emp.getEmail());
            ps.setString(7, emp.getTel());
            ps.setString(8, emp.getMobile());
            ps.setString(9, emp.getMobile2());
            ps.setString(10, emp.getImgPath());
            ps.setString(11, emp.getImgOriginal());
            ps.setString(12, emp.getImgUUID());
            ps.setString(13, emp.getImgType());
            ps.setDate(14, Date.valueOf(emp.getHireDate()));
            ps.setString(15, emp.getHireType());
            ps.setDate(16, Date.valueOf(emp.getBirthday()));
            ps.setString(17, emp.getBirthdayType());
            ps.setString(18, emp.getSchool());
            ps.setString(19, emp.getMajor());
            ps.setString(20, emp.getBank());
            ps.setString(21, emp.getAccount());
            ps.setString(22, emp.getAddress());
            ps.setInt(23, emp.isMarried() ? 1 : 0);
            ps.setInt(24, emp.isChild() ? 1 : 0);
            ps.setString(25, emp.getEtype());
            ps.setInt(26, emp.getVacationCount());
            ps.setInt(27, emp.isHired() ? 1 : 0);
            ps.setString(28, emp.getEmpId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("회원정보 변경 오류");
            e.printStackTrace();
            return 0;
        }
    }

    public List<Emp> getHiredEmpByDeptId(Long deptId) {
        List<Emp> list = new ArrayList<>();
        String sql = """
                	SELECT * FROM EMP e JOIN JOB j
                	ON e.JOB_ID = j.JOB_ID
                	WHERE e.DEPT_ID = ? AND e.HIRED = 1
                	ORDER BY j.JOB_RANK, e.emp_id
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, deptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(makeEmp(rs));
            }
        } catch (SQLException e) {
            System.out.println("부서 회원 정보 가져오기 오류");
            e.printStackTrace();
        }
        return list;
    }

    public int updateMyEmp(Emp emp) {
        String sql = """
                UPDATE emp
                SET email = ?, tel = ?, mobile = ?,
                mobile2 = ?, address = ?, married = ?
                WHERE emp_id = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getEmail());
            ps.setString(2, emp.getTel());
            ps.setString(3, emp.getMobile());
            ps.setString(4, emp.getMobile2());
            ps.setString(5, emp.getAddress());
            ps.setInt(6, emp.isMarried() ? 1 : 0);
            ps.setString(7, emp.getEmpId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("나의 인사정보 수정 오류");
            e.printStackTrace();
            return 0;
        }
    }

    public int fireEmpById(String empId) {
        String sql = """
                UPDATE emp
                SET hired = 0
                WHERE emp_id = ?
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("회원 퇴사처리 오류");
            e.printStackTrace();
            return 0;
        }
    }
    
    public HashMap<String, String> getIdToENameMap() {
		HashMap<String, String> map = new HashMap<>();
		String sql = """
				    SELECT emp_id, ename from EMP
				""";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("emp_id"), rs.getString("ename"));
			}
		} catch (SQLException e) {
			System.out.println("empMap 불러오기 에러");
			e.printStackTrace();
		}
		return map;
	}

    private static Emp makeEmp(ResultSet rs) throws SQLException {
        return new Emp(rs.getString("emp_id"),
                rs.getString("password"),
                rs.getString("ename"),
                rs.getLong("dept_id"),
                rs.getLong("job_id"),
                rs.getString("gender"),
                rs.getString("email"),
                rs.getString("tel"),
                rs.getString("mobile"),
                rs.getString("mobile2"),
                rs.getString("img_path"),
                rs.getString("img_original"),
                rs.getString("img_uuid"),
                rs.getString("img_type"),
                rs.getDate("hiredate").toLocalDate(),
                rs.getString("hiretype"),
                rs.getDate("birthday").toLocalDate(),
                rs.getString("birthday_type"),
                rs.getString("school"),
                rs.getString("major") == null ? null : rs.getString("major"),
                rs.getString("bank"),
                rs.getString("account"),
                rs.getString("address"),
                rs.getInt("married") == 1,
                rs.getInt("child") == 1,
                rs.getString("etype"),
                rs.getInt("vacation_count"),
                rs.getBoolean("hired"));
    }
}