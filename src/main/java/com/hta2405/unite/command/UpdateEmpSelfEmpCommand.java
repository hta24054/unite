package com.hta2405.unite.command;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;

public class UpdateEmpSelfEmpCommand implements UpdateEmpCommand {
    private final EmpSelfUpdateDTO dto;

    public UpdateEmpSelfEmpCommand(EmpSelfUpdateDTO dto) {
        this.dto = dto;
    }

    @Override
    public Emp apply(Emp emp) {
        return emp.toBuilder()
                .email(dto.getEmail())
                .tel(dto.getTel())
                .mobile(dto.getMobile())
                .mobile2(dto.getMobile2())
                .hireDate(dto.getHireDate())
                .birthday(dto.getBirthday())
                .address(dto.getAddress())
                .married(dto.isMarried()).build();
    }
}
