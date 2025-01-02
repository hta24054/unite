package com.hta2405.unite.command;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpAdminUpdateDTO;

public class UpdateEmpAdminCommand implements UpdateEmpCommand{
    private final EmpAdminUpdateDTO dto;

    public UpdateEmpAdminCommand(EmpAdminUpdateDTO dto) {
        this.dto = dto;
    }
//    .cert(dto.getCert())                // 자격증 수정
//            .lang(dto.getLang())                // 언어 능력 수정

    @Override
    public Emp apply(Emp emp) {
        return emp.toBuilder()
                .ename(dto.getEname())              // 이름 수정
                .gender(dto.getGender())            // 성별 수정
                .email(dto.getEmail())              // 이메일 수정
                .tel(dto.getTel())                  // 전화번호 수정
                .mobile(dto.getMobile())            // 휴대전화 수정
                .mobile2(dto.getMobile2())          // 추가 모바일 번호 수정
                .hireDate(dto.getHireDate())        // 입사일 수정
                .bank(dto.getBank())                // 은행 수정
                .account(dto.getAccount())          // 계좌번호 수정
                .hireType(dto.getHireType())        // 입사 유형 수정
                .etype(dto.getEtype())              // 고용 형태 수정
                .birthday(dto.getBirthday())        // 생일 수정
                .birthdayType(dto.getBirthdayType())// 생일 유형 수정
                .address(dto.getAddress())          // 주소 수정
                .married(dto.isMarried())           // 결혼 여부 수정
                .major(dto.getMajor())              // 전공 수정
                .child(dto.isChild())               // 자녀 여부 수정
                .school(dto.getSchool())            // 학교명 수정
                .build();                           // 새로운 Emp 객체 반환
    }
}
