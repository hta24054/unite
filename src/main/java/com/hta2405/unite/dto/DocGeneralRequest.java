package com.hta2405.unite.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DocGeneralRequest {
    private Long docId;
    private String writer;
    private String title;
    private String content;
    private List<String> signers;
}
