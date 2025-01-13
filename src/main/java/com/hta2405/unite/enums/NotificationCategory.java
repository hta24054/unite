package com.hta2405.unite.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationCategory {
    DOC("전자문서", "doc"),
    PROJECT("프로젝트", "project"),
    SCHEDULE("스케줄", "schedule"),
    BOARD("게시판", "board");

    private final String typeKor;
    private final String typeEng;

    public static NotificationCategory fromString(String type) {
        for (NotificationCategory category : NotificationCategory.values()) {
            if (category.getTypeKor().equalsIgnoreCase(type) ||
                    category.getTypeEng().equalsIgnoreCase(type)) {
                return category;
            }
        }
        throw new IllegalArgumentException("알 수 없는 타입명: " + type);
    }
}
