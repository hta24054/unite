package com.hta2405.unite.domain;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cert {
    private Long certId;
    private String certName;
    private String empId;
}
