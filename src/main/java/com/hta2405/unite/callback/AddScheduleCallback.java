package com.hta2405.unite.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.dto.ai.AiAddScheduleDTO;
import com.hta2405.unite.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddScheduleCallback implements FunctionCallback {

    private final ScheduleService scheduleService;

    @Override
    public String getName() {
        return "addSchedule";
    }

    @Override
    public String getDescription() {
        return """
                -- 일정 제목(scheduleName)에는 날짜 정보를 포함하지 마세요
                -- 일정 내용(scheduleContent)은 제목을 작성하거나, 별도로 적절한 정보가 있으면 내용을 작성하세요
                -- 시작 날짜에 대한 언급이 없는 경우 기본값은 오늘입니다.
                -- 시작 시간에 대한 언급이 없는 경우 기본값은 오전 09시 입니다.
                -- 끝나는 시간에 대한 언급이 없는 경우 또는 얼마나 걸리는지 언급이 없는 경우 시작시간으로부터 1간을 기본값으로 합니다.
                """;
    }

    @Override
    public String getInputTypeSchema() {
        return """
                {
                  "type": "object",
                  "properties": {
                    "scheduleName": {"type": "string"},
                    "scheduleContent": {"type": "string"},
                    "scheduleStart": {"type": "string", "format": "date-time"},
                    "scheduleEnd": {"type": "string", "format": "date-time"}
                  },
                  "required": ["scheduleName", "scheduleStart", "scheduleEnd"]
                }
                """;
    }

    @Override
    public String call(String functionArguments) {
        log.info("functionArguments = {}", functionArguments);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            // JSON 데이터를 Schedule 객체로 변환
            AiAddScheduleDTO dto = objectMapper.readValue(functionArguments, AiAddScheduleDTO.class);

            String loginEmpId = SecurityContextHolder.getContext().getAuthentication().getName();

            Schedule schedule = new Schedule();
            schedule.setEmpId(loginEmpId);
            schedule.setScheduleStart(dto.getScheduleStart());
            schedule.setScheduleEnd(dto.getScheduleEnd());
            schedule.setScheduleName(dto.getScheduleName());
            schedule.setScheduleContent(dto.getScheduleContent());
            schedule.setScheduleColor("#dc2626");

            scheduleService.insertSchedule(schedule);

            return objectMapper.writeValueAsString(schedule);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON input: " + functionArguments, e);
        }
    }
}