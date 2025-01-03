package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Cert;
import com.hta2405.unite.domain.Lang;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmpAdminUpdateDTO {
    private String ename;                // 이름
    private String gender;               // 성별 (남/여)
    private String email;                // 이메일
    private String tel;                  // 전화번호
    private Long deptId;                 // 부서 ID
    private String empId;                // 사원 ID
    private Long jobId;                  // 직무 ID
    private String mobile;               // 휴대전화 번호
    private LocalDate hireDate;          // 입사일
    private String bank;                 // 은행
    private String account;              // 계좌번호
    private String mobile2;              // 추가 모바일 번호
    private String hireType;             // 입사 유형 (신입/경력 등)
    private String etype;                // 고용 형태 (정규직/계약직 등)
    private LocalDate birthday;          // 생일
    private String birthdayType;         // 생일 유형 (양력/음력)
    private String address;              // 주소
    private List<Cert> cert;           // 자격증 (중복 가능)
    private String school;               // 학교명
    private boolean married;             // 결혼 여부 (1/0)
    private List<Lang> lang;           // 언어 능력 (중복 가능)
    private String major;                // 전공
    private boolean child;               // 자녀 여부 (1/0)
    private String beforeFileName;       // 이전 파일 이름
}
