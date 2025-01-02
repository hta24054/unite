package com.hta2405.unite.command;

import com.hta2405.unite.domain.Emp;

public interface UpdateEmpCommand {
    Emp apply(Emp emp);
}
