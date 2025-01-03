package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpSelfUpdateDTO {
    private String email;
    private String tel;
    private String mobile;
    private String mobile2;
    private String bank;
    private String account;
    private String address;
    private boolean married;
    private String beforeFileName;       // 이전 파일 이름
}
