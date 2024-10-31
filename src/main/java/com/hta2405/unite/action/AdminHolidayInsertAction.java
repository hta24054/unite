package com.hta2405.unite.action;

import com.hta2405.unite.dao.HolidayDao;
import com.hta2405.unite.dto.Holiday;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

public class AdminHolidayInsertAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String apiKey = getApiKey();
        System.out.println("apiKey = " + apiKey);
        TreeMap<LocalDate, String> map = getYearlyHolidays(apiKey);

        HolidayDao holidayDao = new HolidayDao();

        //기존에 등록된 휴일은 추가 안함, 혹시 등록된 날짜여도 이름이 다른 휴일이면 등록함(일요일+공휴일 등)
        for (LocalDate localDate : map.keySet()) {
            String holidayName = holidayDao.getHolidayName(localDate);
            if (holidayName == null || !holidayName.equals(map.get(localDate))) {
                holidayDao.insertHoliday(localDate, map.get(localDate));
            }
        }
        return null;
    }

    /**
     * resource 하위 config.properties 파일 생성 후
     * apiKey=abcde 꼴로 작성한 apikey를 불러옴
     */
    private String getApiKey() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return null;
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("apiKey");
    }

    //순서 보장을 위해 TreeMap 선언
    private static TreeMap<LocalDate, String> getYearlyHolidays(String apiKey) throws IOException {
        TreeMap<LocalDate, String> holidays = new TreeMap<>();

        for (int i = 0; i < 12; i++) { //당월부터 12개월 간
            String year = LocalDate.now().plusMonths(i).getYear() + "";
            String month = String.format("%02d", LocalDate.now().plusMonths(i).getMonthValue());
            getHoliday(apiKey, year, month, holidays);
        }
        return holidays;
    }

    // 공휴일 데이터를 받아오는 메서드
    private static void getHoliday(String apiKey, String year, String month, TreeMap<LocalDate, String> holidays) throws IOException {

        // URL 생성
        String urlString = buildUrl(apiKey, year, month);
        URL url = new URL(urlString);

        // API 호출 및 응답 처리
        String response = getApiResponse(url);

        // XML 응답을 파싱하여 공휴일 정보를 추출
        parseXmlResponse(response, holidays);
    }

    // URL 빌더 메서드
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
//        System.out.println("Response code: " + conn.getResponseCode());

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
            Document doc = builder.parse(new java.io.ByteArrayInputStream(response.getBytes()));

            // XML에서 공휴일 날짜와 이름 추출
            NodeList dateNodes = doc.getElementsByTagName("locdate");
            NodeList nameNodes = doc.getElementsByTagName("dateName");

            for (int i = 0; i < dateNodes.getLength(); i++) {
                String dateStr = dateNodes.item(i).getTextContent();
                String name = nameNodes.item(i).getTextContent();

                // 날짜 포맷을 LocalDate로 변환
                LocalDate date = LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
                holidays.put(date, name);
            }
        } catch (Exception e) {
            System.err.println("Error parsing XML response: " + e.getMessage());
        }
    }
}
