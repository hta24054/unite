package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Emp {
    private String empId;
    private String password;
    private String ename;
    private int deptId;
    private int jobId;
    private String gender; //"남", "여"
    private String email;
    private String tel;
    private String mobile;
    private String imgPath;
    private String imgOriginal;
    private String imgUUID;
    private String imgType;
    private boolean hired;
}
