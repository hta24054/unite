package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Contact {
    private Long id;
    private String name;
    private String position;
    private String company;
    private String mobile;
    private String tel;
    private String email;
    private String info;
}
