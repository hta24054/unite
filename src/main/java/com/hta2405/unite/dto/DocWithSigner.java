package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocWithSigner {
    private Doc doc;
    private String signerId;
    private String signerName;
}
