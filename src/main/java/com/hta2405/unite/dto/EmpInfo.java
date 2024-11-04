package com.hta2405.unite.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class EmpInfo {
	private String empId; // 직원 ID
	private String ename; // 이름
	private String gender; // 성별 ("남", "여")
	private String email; // 이메일 주소
	private String tel; // 내선 번호
	private String deptId; // 부서id
	private String jobName; // 직위id
	private String mobile; // 휴대폰 번호
	private String company; // 회사명
	private Date hireDate; // 입사일
	private String hireType; // 채용 구분 (경력, 신입, 인턴 등)
	private String bank; // 은행
	private String account; // 계좌 번호
	private String mobile2; // 긴급 연락처
	private String etype; // 직원구분 (정규직, 계약직, 퇴직 등)
	private Date birthDate; // 생년월일
	private String address; // 주소
	private String school; // 최종 학력
	private Boolean married; // 혼인 여부
	private String certName; // 자격증 id
	private String major; // 전공
	private String langName;// 외국어 id
	private Integer child;
}