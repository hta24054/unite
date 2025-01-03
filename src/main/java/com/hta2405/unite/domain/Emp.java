package com.hta2405.unite.domain;

import com.hta2405.unite.dto.EmpAdminUpdateDTO;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;
import com.hta2405.unite.dto.FileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
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

    public void updateByAdmin(EmpAdminUpdateDTO dto, FileDTO fileDTO) {
        this.ename = dto.getEname();
        this.gender = dto.getGender();
        this.email = dto.getEmail();
        this.tel = dto.getTel();
        this.mobile = dto.getMobile();
        this.mobile2 = dto.getMobile2();
        this.hireDate = dto.getHireDate();
        this.bank = dto.getBank();
        this.account = dto.getAccount();
        this.hireType = dto.getHireType();
        this.etype = dto.getEtype();
        this.birthday = dto.getBirthday();
        this.address = dto.getAddress();
        this.married = dto.isMarried();
        this.major = dto.getMajor();
        this.child = dto.isChild();
        this.school = dto.getSchool();
        this.imgOriginal = fileDTO.getOriginalFileName();
        this.imgPath = fileDTO.getFilePath();
        this.imgType = fileDTO.getFileType();
        this.imgUUID = fileDTO.getFileUUID();
    }

    public void updateBySelf(EmpSelfUpdateDTO dto, FileDTO fileDTO) {
        this.gender = dto.getEmail();
        this.email = dto.getEmail();
        this.tel = dto.getTel();
        this.mobile = dto.getMobile();
        this.mobile2 = dto.getMobile2();
        this.bank = dto.getBank();
        this.account = dto.getAccount();
        this.address = dto.getAddress();
        this.married = dto.isMarried();
        this.imgOriginal = fileDTO.getOriginalFileName();
        this.imgPath = fileDTO.getFilePath();
        this.imgType = fileDTO.getFileType();
        this.imgUUID = fileDTO.getFileUUID();
    }
}
