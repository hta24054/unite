package com.hta2405.unite.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarDateTimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 문자열 형태의 날짜에서 "T"를 제거하고 LocalDateTime으로 변환
     *
     * @param dateTime 입력 날짜 문자열
     * @return LocalDateTime 객체
     */
    public static LocalDateTime parseDateTimeWithoutT(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty()) {
            try {
                // "T"를 공백으로 교체
                String formattedDateTime = dateTime.replace("T", " ");
                return LocalDateTime.parse(formattedDateTime, FORMATTER);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * LocalDateTime 객체를 문자열 형식으로 변환
     *
     * @param dateTime LocalDateTime 객체
     * @return yyyy-MM-dd HH:mm 형식의 문자열
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            return dateTime.format(FORMATTER);
        }
        return null;
    }
}
