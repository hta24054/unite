package com.hta2405.unite.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.dto.ai.AiGetScheduleDTO;
import com.hta2405.unite.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetScheduleCallback implements FunctionCallback {

    private final ScheduleService scheduleService;

    @Override
    public String getName() {
        return "getSchedule";
    }

    @Override
    public String getDescription() {
        return "get a schedule for the user";
    }

    @Override
    public String getInputTypeSchema() {
        return """
                {
                  "type": "object",
                  "properties": {
                    "date": {"type": "string", "format": "date-time"}
                  },
                  "required": ["date"]
                }
                """;
    }

    @Override
    public String call(String functionArguments) {
        log.info("functionArguments = {}", functionArguments);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            AiGetScheduleDTO dto = objectMapper.readValue(functionArguments, AiGetScheduleDTO.class);

            String loginEmpId = SecurityContextHolder.getContext().getAuthentication().getName();

            List<Schedule> dailyScheduleList = scheduleService.getDailyScheduleList(loginEmpId, dto.getDate());

            return "--함수 호출이 완료되었습니다. 다음의 정보를 포함해서 답변하세요 : "+ objectMapper.writeValueAsString(dailyScheduleList);

        } catch (
                JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON input: " + functionArguments, e);
        }
    }
}