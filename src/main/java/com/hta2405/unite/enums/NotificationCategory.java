package com.hta2405.unite.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum NotificationCategory {
    DOC("전자문서", "doc", "fas fa-book-open"),
    PROJECT("프로젝트", "project", "fa-solid fa-flag"),
    SCHEDULE("스케줄", "schedule", "fa-solid fa-calendar-days"),
    BOARD("게시판", "board", "fa-solid fa-comments");

    private final String typeKor;
    private final String typeEng;
    private final String icon;

    @JsonValue
    public Map<String, String> toJson() {
        Map<String, String> json = new HashMap<>();
        json.put("typeKor", typeKor);
        json.put("typeEng", typeEng);
        json.put("icon", icon);
        return json;
    }
}
