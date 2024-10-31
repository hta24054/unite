package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmpInfo {
    private String empId;
    private String mobile2;
    private String hiredate;
    private String hiretype; //"경력", "신입", "인턴"
    private String birthday;
    private String school; 
    private String major;
    private String bank;
    private String account;
    private String address;
    private boolean married ;
    private boolean child;
    private boolean etype; //"정규직" , "계약직"
    private int vacation_count; 
}
