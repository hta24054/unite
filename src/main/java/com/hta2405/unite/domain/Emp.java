package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Emp {
    private String empId;
    private String password;
    private String ename;
    private String role;
    private Long deptId;
    private Long jobId;
    private String gender; //"남", "여"
    private String email;
    private String tel;
    private String mobile;
    private String mobile2;
    private String imgPath;
    private String imgOriginal;
    private String imgUUID;
    private String imgType;
    private LocalDate hireDate;
    private String hireType;
    private LocalDate birthday;
    private String birthdayType;
    private String school;
    private String major;
    private String bank;
    private String account;
    private String address;
    private boolean married;
    private boolean child;
    private String etype;
    private Long vacationCount;
    private boolean hired;
}
