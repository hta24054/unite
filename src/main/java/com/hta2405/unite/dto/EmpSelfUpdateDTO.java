package com.hta2405.unite.dto;

import com.hta2405.unite.command.UpdateEmpCommand;
import com.hta2405.unite.command.UpdateEmpSelfEmpCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpSelfUpdateDTO implements EmpUpdateDTO{
    private String id;
    private String email;
    private String tel;
    private String mobile;
    private String mobile2;
    private LocalDate hireDate;
    private LocalDate birthday;
    private String address;
    private boolean married;

    @Override
    public UpdateEmpCommand createCommand() {
        return new UpdateEmpSelfEmpCommand(this);
    }
}
