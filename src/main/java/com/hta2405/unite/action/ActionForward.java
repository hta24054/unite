package com.hta2405.unite.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ActionForward {
    private boolean redirect;  //포워딩 vs 리다이렉트 결정
    private String path;        //경로 설정
}