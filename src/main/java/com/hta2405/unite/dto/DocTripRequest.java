package com.hta2405.unite.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class DocTripRequest {
    private Long docId;
    private String writer;
    private Long docTripId;
    private String tripStart;
    private String tripEnd;
    private String tripLoc;
    private String tripPhone;
    private String tripInfo;
    private String cardStart;
    private String cardEnd;
    private String cardReturn;
    private List<String> signers;
}
