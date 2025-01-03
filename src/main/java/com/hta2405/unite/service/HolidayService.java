package com.hta2405.unite.service;

import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.mybatis.mapper.HolidayMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
@Slf4j
public class HolidayService {
    private final HolidayMapper holidayMapper;
    private final String apiKey;

    public HolidayService(HolidayMapper holidayMapper,
                          @Value("${holiday.apiKey}") String apiKey) {
        this.holidayMapper = holidayMapper;
        this.apiKey = apiKey;
    }

    public List<Holiday> getHolidayList(LocalDate startDate, LocalDate endDate) {
        return holidayMapper.getHolidayList(startDate, endDate);
    }

    public String addHoliday(Holiday holiday) {
        if (holidayMapper.getHolidayName(holiday.getHolidayDate()) != null) {
            return "휴일 등록을 실패하였습니다. 이미 등록된 휴일입니다.";
        }
        int result = holidayMapper.insertHoliday(holiday);
        if (result != 1) {
            return "휴일 등록 실패";
        }
        return "휴일 등록 성공";
    }

    public String deleteHoliday(LocalDate date) {
        int result = holidayMapper.deleteHoliday(date);
        if (result != 1) {
            return "휴일 삭제를 실패하였습니다.";
        }
        return "휴일을 삭제하였습니다.";
    }

    public void addWeekend(LocalDate startDate, LocalDate endDate) {
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (isWeekend(dayOfWeek) && holidayMapper.getHolidayName(date) == null) {
                String holidayName = (dayOfWeek == DayOfWeek.SATURDAY) ? "토요일" : "일요일";
                Holiday holiday = Holiday.builder().holidayDate(date).holidayName(holidayName).build();
                holidayMapper.insertHoliday(holiday);
            }
            date = date.plusDays(1); // 날짜를 하루씩 증가
        }
    }

    private boolean isWeekend(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public void insertYearlyHoliday() {
        TreeMap<LocalDate, String> map = getYearlyHolidays(apiKey);

        for (LocalDate date : map.keySet()) {
            String savedHolidayName = holidayMapper.getHolidayName(date); //이미 저장된 휴일 명 DB에서 확인
            Holiday holiday = Holiday.builder().holidayDate(date).holidayName(map.get(date)).build();

            if (savedHolidayName == null || !savedHolidayName.equals(map.get(date))) {
                holidayMapper.insertHoliday(holiday);
            }
        }
    }

    private static TreeMap<LocalDate, String> getYearlyHolidays(String apiKey) {
        TreeMap<LocalDate, String> holidays = new TreeMap<>();

        for (int i = 0; i < 12; i++) { //당월부터 12개월 간
            String year = LocalDate.now().plusMonths(i).getYear() + "";
            String month = String.format("%02d", LocalDate.now().plusMonths(i).getMonthValue());
            getHoliday(apiKey, year, month, holidays);
        }
        return holidays;
    }

    private static void getHoliday(String apiKey, String year, String month, TreeMap<LocalDate, String> holidays) {

        // URL 생성
        String urlString = buildUrl(apiKey, year, month);
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // API 호출 및 응답 처리
        String response;
        try {
            response = getApiResponse(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // XML 응답을 파싱하여 공휴일 정보를 추출
        parseXmlResponse(response, holidays);
    }

    private static String buildUrl(String apiKey, String year, String month) {
        return "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo" + "?" + "serviceKey=" + apiKey +
                "&" + URLEncoder.encode("solYear", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(year, StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("solMonth", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(month, StandardCharsets.UTF_8);
    }

    // API 응답을 처리하는 메서드
    private static String getApiResponse(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

    // XML 응답을 파싱하는 메서드
    private static void parseXmlResponse(String response, TreeMap<LocalDate, String> holidays) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(response.getBytes()));

            // XML에서 공휴일 날짜와 이름 추출
            NodeList dateNodes = doc.getElementsByTagName("locdate");
            NodeList nameNodes = doc.getElementsByTagName("dateName");

            for (int i = 0; i < dateNodes.getLength(); i++) {
                String dateStr = dateNodes.item(i).getTextContent();
                String name = nameNodes.item(i).getTextContent();

                // 날짜 포맷을 LocalDate로 변환
                LocalDate date = LocalDate.parse(dateStr, ofPattern("yyyyMMdd"));
                holidays.put(date, name);
            }
        } catch (Exception e) {
            log.info("Error parsing XML response = {}", e.getMessage());
        }
    }
}
