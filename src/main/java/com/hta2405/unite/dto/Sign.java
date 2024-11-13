package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sign {
    private Long signId;
    private String EmpId;
    private Long docId;
    private int signOrder;
    private boolean signed;
}
