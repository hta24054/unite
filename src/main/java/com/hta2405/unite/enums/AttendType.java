package com.hta2405.unite.enums;

import lombok.Getter;

@Getter
public enum AttendType {
    GENERAL("일반"), WORK_OUTSIDE("외근"), TRIP("출장"), VACATION("휴가"), HOLIDAY("휴일"), ABSENT("결근");
    private final String type;

    AttendType(String type) {
        this.type = type;
    }
}