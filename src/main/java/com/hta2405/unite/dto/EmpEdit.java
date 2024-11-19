package com.hta2405.unite.dto;

import lombok.Getter;

import java.time.LocalDate;
@Getter
public class EmpEdit {
    private String empId;
    private String ename;
    private Long deptId;
    private Long jobId;
    private String gender;
    private String email;
    private String tel;
    private String mobile;
    private String mobile2;
    private String imgOriginal;
    private String hireDate;
    private String hireType;
    private String birthday;
    private String birthdayType;
    private String school;
    private String major;
    private String bank;
    private String account;
    private String address;
    private boolean married;
    private boolean child;
    private String etype;
    
    private String[] cert;
    private String[] lang;
    //비밀번호, 연차갯수, hired 빼고 다 들어옴
}
