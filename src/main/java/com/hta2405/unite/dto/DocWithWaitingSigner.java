package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocWithWaitingSigner {
    private Doc doc;
    private String signerId; // 현재 결재 대기자의 정보
    private String signerName; // 현재 결재 대기자의 정보
}
