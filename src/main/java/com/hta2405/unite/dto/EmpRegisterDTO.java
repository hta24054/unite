package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Cert;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Lang;
import com.hta2405.unite.enums.Role;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmpRegisterDTO {
    private String empId;
    private String ename;
    private String gender;
    private String email;
    private String tel;
    private Long deptId;
    private Long jobId;
    private String mobile;
    private LocalDate hireDate;
    private String bank;
    private String account;
    private String mobile2;
    private String hireType;
    private String etype;
    private LocalDate birthday;
    private String birthdayType;
    private String address;
    private boolean child;
    private boolean married;
    private String school;
    private String major;
    private List<Cert> cert;
    private List<Lang> lang;

    public Emp toEntity(FileDTO fileDTO) {
        String defaultPassword = "1234";
        String encodedPassword = new BCryptPasswordEncoder().encode(defaultPassword);

        return Emp.builder()
                .empId(this.empId)
                .password(encodedPassword)
                .ename(this.ename)
                .role(Role.ROLE_MEMBER)
                .deptId(this.deptId)
                .jobId(this.jobId)
                .gender(this.gender)
                .email(this.email)
                .tel(this.tel)
                .mobile(this.mobile)
                .mobile2(this.mobile2)
                .imgPath(fileDTO.getFilePath())
                .imgOriginal(fileDTO.getFileOriginal())
                .imgUUID(fileDTO.getFileUUID())
                .imgType(fileDTO.getFileType())
                .hireDate(this.hireDate)
                .hireType(this.hireType)
                .birthday(this.birthday)
                .birthdayType(this.birthdayType)
                .school(this.school)
                .major(this.major)
                .bank(this.bank)
                .account(this.account)
                .address(this.address)
                .married(this.married)
                .child(this.child)
                .etype(this.etype)
                .vacationCount(0L)
                .hired(true).build();
    }
}
