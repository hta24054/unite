package com.hta2405.unite.service;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocVacation;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.dto.AiResponseDTO;
import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.enums.AttendType;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.enums.Role;
import com.hta2405.unite.strategy.doc.VacationDocWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
    private final ChatClient chatClient;
    private final ScheduleService scheduleService;
    private final EmpService empService;
    private final String SUCCESS = "Success";
    private final String FAIL = "Fail";
    private final DocService docService;

    public Map<String, Object> findService(String message, String empId) {
        String template = """
                -- 오늘의 날짜는 {today} 입니다.
                -- 다음 입력에서 적절한 응답을 추출해서 AiResponseDTO 형태로 객체화 해서 반환하세요
                -- 이제 사용자의 요청입니다. : {message}
                """;
        AiResponseDTO aiResponseDTO = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(template)
                        .param("today", LocalDate.now())
                        .param("message", message))
                .call()
                .entity(AiResponseDTO.class);

        log.info("Ai 응답 : {}", aiResponseDTO);
        if (aiResponseDTO == null) {
            throw new RuntimeException("AI 응답 오류");
        }

        String typeName = aiResponseDTO.getTypeName();
        Map<String, Object> aiResponse = aiResponseDTO.getResponse();

        if (typeName.equals("addSchedule")) {
            return addSchedule(aiResponse, empId);
        } else if (typeName.equals("contact")) {
            return searchContact(aiResponse);
        } else if (typeName.equals("summarize")) {
            return summarize(aiResponse);
        } else if (typeName.equals("getSchedule")) {
            return getSchedule(aiResponse, empId);
        } else if (typeName.equals("vacationApply")) {
            return applyVacation(aiResponse, empId);
        }
        return null;
    }

    private Map<String, Object> applyVacation(Map<String, Object> aiResponse, String empId) {
        Map<String, Object> map = new HashMap<>();

        Emp emp = empService.getEmpById(empId);
        if (emp.getRole() == Role.ROLE_ADMIN || emp.getEmpId().equals("241001")) {
            map.put("resultMessage", "관리자(admin) 또는 대표는 이 기능을 사용할 수 없습니다.");
            return map;
        }

        LocalDate startDate = LocalDate.parse(aiResponse.get("vacationStart").toString());
        LocalDate endDate = LocalDate.parse(aiResponse.get("vacationEnd").toString());
        AttendType vacationType = AttendType.fromString(aiResponse.get("vacationType").toString());

        int vacationCount = docService.countVacation(startDate, endDate);

        Doc doc = Doc.builder().docWriter(empId)
                .docType(DocType.VACATION)
                .docTitle("휴가신청서(" + empId + ")")
                .docContent(aiResponse.get("vacationInfo").toString())
                .docCreateDate(LocalDateTime.now())
                .signFinish(false).build();

        DocVacation.DocVacationBuilder docVacationBuilder = DocVacation.builder()
                .vacationStart(startDate)
                .vacationEnd(endDate)
                .vacationCount(vacationCount)
                .vacationType(vacationType);

        List<String> signers = new ArrayList<>();
        signers.add(empId);
        String managerEmpId = empService.findManager(empId);
        signers.add(managerEmpId);

        docService.saveVacationDoc(doc, docVacationBuilder, signers, null);
        String sb = "휴가 신청 결과는 다음과 같습니다.<br>"
                + String.format("* 휴가 종류 : %s<br>휴가 시작 : %s<br>휴가 종료 : %s<br>휴가 일수 : %s<br><br>"
                , vacationType.getTypeName(), startDate, endDate, vacationCount);
        map.put("resultMessage", sb);
        return map;
    }

    private Map<String, Object> getSchedule(Map<String, Object> aiResponse, String empId) {
        Map<String, Object> map = new HashMap<>();
        LocalDate date = LocalDate.parse(aiResponse.get("date").toString());
        List<Schedule> dailyScheduleList = scheduleService.getDailyScheduleList(empId, date);

        StringBuilder sb = new StringBuilder(date.toString().replaceAll("T", " ") + "<br>일정은 다음과 같습니다.<br><br>");
        if (dailyScheduleList.isEmpty()) {
            sb.append("일정이 없습니다.");
            map.put("resultMessage", sb.toString());
            return map;
        }
        for (Schedule schedule : dailyScheduleList) {
            sb.append(String.format("* 일정명 : %s<br>일정내용 : %s<br>시작일시 : %s<br>종료일시 : %s<br><br>",
                    schedule.getScheduleName(),
                    schedule.getScheduleContent(),
                    dateTimeToString(schedule.getScheduleStart()),
                    dateTimeToString(schedule.getScheduleEnd())));
        }
        System.out.println(dailyScheduleList);
        map.put("resultMessage", sb.toString());
        return map;
    }

    private static String dateTimeToString(LocalDateTime dateTime) {
        return dateTime.toString().replaceAll("T", " ").trim();
    }

    private Map<String, Object> summarize(Map<String, Object> aiResponse) {
        Map<String, Object> map = new HashMap<>();

        String summarizedText = aiResponse.get("summary").toString();
        map.put("resultMessage", summarizedText);
        map.put("status", SUCCESS);
        return map;
    }

    public Map<String, Object> addSchedule(Map<String, Object> aiResponse, String empId) {
        String status = SUCCESS;
        if (aiResponse == null) {
            status = FAIL;
        }

        Schedule schedule = new Schedule();
        schedule.setEmpId(empId);
        schedule.setScheduleName(aiResponse.get("scheduleName").toString());
        schedule.setScheduleContent(aiResponse.get("scheduleContent").toString());
        schedule.setScheduleStart(LocalDateTime.parse(aiResponse.get("scheduleStart").toString()));
        schedule.setScheduleEnd(LocalDateTime.parse(aiResponse.get("scheduleEnd").toString()));
        schedule.setScheduleColor("#dc2626");

        int result = scheduleService.insertSchedule(schedule);
        if (result != 1) {
            status = FAIL;
        }

        Map<String, Object> map = new HashMap<>();

        map.put("status", status);
        String resultMessage = String.format(
                "일정을 추가했습니다.<br>* 일정명 : %s<br>* 일정내용 : %s<br>* 일정시작 : %s<br>* 일정종료 : %s",
                schedule.getScheduleName(),
                schedule.getScheduleContent(),
                dateTimeToString(schedule.getScheduleStart()),
                dateTimeToString(schedule.getScheduleEnd()));
        map.put("resultMessage", resultMessage);
        return map;
    }

    public Map<String, Object> searchContact(Map<String, Object> aiResponse) {
        Map<String, Object> map = new HashMap<>();

        String name = aiResponse.get("name").toString().replaceAll(" ", "").trim();
        List<EmpListDTO> empList = empService.getAllEmpListDTO();
        EmpListDTO targetEmp = null;
        for (EmpListDTO empListDTO : empList) {
            if (empListDTO.getEname().equals(name)) {
                targetEmp = empListDTO;
                break;
            }
        }
        if (targetEmp == null) {
            map.put("status", FAIL);
            return map;
        }

        map.put("status", SUCCESS);
        String resultMessage = String.format(
                "%s 님의 연락처는 다음과 같습니다.<br>* 휴대전화 : %s<br>* 내선전화 : %s<br>* 이메일 : %s",
                targetEmp.getEname(),
                targetEmp.getMobile(),
                targetEmp.getTel(),
                targetEmp.getEmail());
        map.put("resultMessage", resultMessage);
        return map;
    }
}
