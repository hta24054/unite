package com.hta2405.unite.enums;

import lombok.Getter;

@Getter
public enum AttendType {
    GENERAL("일반"),
    WORK_OUTSIDE("외근"),
    TRIP("출장"),
    VACATION("휴가"),
    ANNUAL_VACATION("연차", VACATION),
    SICK_VACATION("병가", VACATION),
    OFFICIAL_VACATION("공가", VACATION),
    EVENT_VACATION("경조", VACATION),
    ETC_VACATION("기타)", VACATION),
    HOLIDAY("휴일"),
    ABSENT("결근");

    private final String typeName;
    private final AttendType parentType;

    AttendType(String typeName) {
        this(typeName, null); // 부모 타입이 없는 경우
    }

    AttendType(String typeName, AttendType parentType) {
        this.typeName = typeName;
        this.parentType = parentType;
    }

    public boolean isSubTypeOf(AttendType parentType) {
        return this.parentType != null && this.parentType == parentType;
    }

    public static AttendType fromString(String type) {
        for (AttendType attendType : AttendType.values()) {
            if (attendType.getTypeName().trim().equalsIgnoreCase(type.trim())) {
                return attendType;
            }
        }
        throw new IllegalArgumentException("알수없는 타입명: " + type);
    }
}