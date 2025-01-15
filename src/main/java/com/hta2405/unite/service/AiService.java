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

    public int addSchedule(String message, String empId) {
        String template = """ 
                1. AiScheduleResponseDTO 객체의 구성은 다음과 같습니다.
                String scheduleName, String scheduleContent, LocalDateTime scheduleStart, LocalDateTime scheduleEnd
                2. 오늘 날짜는 다음과 같습니다. :""" + LocalDate.now() +
                """
                        3. 일정 내용(scheduleContent)은 전체문장을 작성하기보단, 키워드 중심으로 작성하세요
                        4. 몇시에 끝나는지, 시간이 얼마나 걸리는지 언급이 없으면 1시간을 기본으로 합니다.
                        5. 다음 입력을 받고 추출해서 AiScheduleResponseDTO로 객체화 해서 반환하세요 : {message}
                        """;
        AiScheduleResponseDTO aiScheduleResponseDTO = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(template).param("message", message))
                .call()
                .entity(AiScheduleResponseDTO.class);

        if (aiScheduleResponseDTO == null) {
            return 0;
        }

        Schedule schedule = new Schedule();
        schedule.setEmpId(empId);
        schedule.setScheduleName(aiScheduleResponseDTO.getScheduleName());
        schedule.setScheduleContent(aiScheduleResponseDTO.getScheduleContent());
        schedule.setScheduleStart(LocalDateTime.parse(aiScheduleResponseDTO.getScheduleStart()));
        schedule.setScheduleEnd(LocalDateTime.parse(aiScheduleResponseDTO.getScheduleEnd()));
        schedule.setScheduleColor("#dc2626");

        return scheduleService.insertSchedule(schedule);
    }
}
