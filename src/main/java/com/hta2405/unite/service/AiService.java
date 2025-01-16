package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.dto.AiScheduleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AiService {
    private final ChatClient chatClient;
    private final ScheduleService scheduleService;

    public boolean addSchedule(String message, String empId) {
        String template = """ 
                #객체의 구조
                AiScheduleResponseDTO = [
                        "scheduleName" : "일정 이름",
                        "scheduleContent" : "일정 내용",
                        "scheduleStart" : "일정 시작시각",
                        "scheduleEnd" : "일정 종료시각"
                    ]
                -- 오늘 날짜는 다음과 같습니다. : {today}
                -- '다음주 특정 요일'을 계산 할 때는 (오늘 날짜 + 7일) 다음 그 날짜부터 하루씩 빼나가면서 해당하는 요일일 경우에 해당 날짜로 선택합니다.
                -- '다다음주 특정 요일'의 경우에  (오늘 날짜 + 14일) 다음 그 날짜부터 하루씩 빼나가면서 해당하는 요일일 경우에 해당 날짜로 선택합니다.
                -- 일정 내용(scheduleContent)은 전체문장을 작성하기보단, 키워드 중심으로 작성하세요
                -- 시작 날짜에 대한 언급이 없는 경우 기본값은 오늘입니다.
                -- 시작 시간에 대한 언급이 없는 경우 기본값은 오전 09시 입니다.
                -- 끝나는 시간에 대한 언급이 없는 경우 또는 얼마나 걸리는지 언급이 없는 경우 시작시간으로부터 1시간을 기본값으로 합니다.
                -- 다음 입력을 받고 추출해서 AiScheduleResponseDTO로 객체화 해서 반환하세요
                : {message}
                """;

        AiScheduleResponseDTO aiScheduleResponseDTO = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(template)
                        .param("today", LocalDate.now())
                        .param("message", message))
                .call()
                .entity(AiScheduleResponseDTO.class);

        if (aiScheduleResponseDTO == null) {
            return false;
        }

        Schedule schedule = new Schedule();
        schedule.setEmpId(empId);
        schedule.setScheduleName(aiScheduleResponseDTO.getScheduleName());
        schedule.setScheduleContent(aiScheduleResponseDTO.getScheduleContent());
        schedule.setScheduleStart(LocalDateTime.parse(aiScheduleResponseDTO.getScheduleStart()));
        schedule.setScheduleEnd(LocalDateTime.parse(aiScheduleResponseDTO.getScheduleEnd()));
        schedule.setScheduleColor("#dc2626");
        return scheduleService.insertSchedule(schedule) == 1;
    }
}
