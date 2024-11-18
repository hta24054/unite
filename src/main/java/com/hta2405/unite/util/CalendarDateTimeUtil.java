package com.hta2405.unite.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarDateTimeUtil {
	public static LocalDateTime parseDateTimeWithoutT(String dateTime) {
        // T 공백으로 변경
        if (dateTime != null && !dateTime.isEmpty()) {
        	
            dateTime = dateTime.replace("T", " "); // "T" 공백으로 교체
            System.out.println("s =" + dateTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
           
            try {
            	System.out.println("s2=" + LocalDateTime.parse(dateTime, formatter));
                return LocalDateTime.parse(dateTime, formatter);
            } catch (Exception e) {
            	e.printStackTrace();
                return null;
            }
        }
        return null; // 입력 값이 null이거나 비어 있으면 null 반환
    }
}
