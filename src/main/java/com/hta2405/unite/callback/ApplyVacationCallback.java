package com.hta2405.unite.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocVacation;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.ai.AiVacationDTO;
import com.hta2405.unite.enums.AttendType;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.enums.Role;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplyVacationCallback implements FunctionCallback {
    private final EmpService empService;
    private final DocService docService;

    @Override
    public String getName() {
        return "applyVacation";
    }

    @Override
    public String getDescription() {
        return """
                --apply vacation for the user
                --"vacationType" 은 ['연차', '병가', '공가', '경조']중 하나를 선택합니다. 별도의 언급이 없는 경우 '연차' 로 선택합니다.
                -- 휴가는 시간 단위가 없이 일 단위로 신청합니다.
                --"vacationInfo"는 "vacationType + '신청'" 문자열을 반환합니다.
                """;
    }

    @Override
    public String getInputTypeSchema() {
        return """
                {
                  "type": "object",
                  "properties": {
                    "vacationType": {"type": "string"},
                    "vacationInfo": {"type": "string"},
                    "vacationStart": {"type": "string", "format": "date-time"},
                    "vacationEnd": {"type": "string", "format": "date-time"}
                  },
                  "required": ["vacationType", "vacationInfo", "vacationStart", "vacationEnd"]
                }
                """;
    }

    @Override
    public String call(String functionArguments) {
        log.info("functionArguments = {}", functionArguments);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            AiVacationDTO dto = objectMapper.readValue(functionArguments, AiVacationDTO.class);
            String loginEmpId = SecurityContextHolder.getContext().getAuthentication().getName();
            Emp emp = empService.getEmpById(loginEmpId);
            if (emp.getRole() == Role.ROLE_ADMIN || emp.getEmpId().equals("241001")) {
                return "현재 사용자는 사용할 수 없는 기능";
            }


            LocalDate startDate = dto.getVacationStart();
            LocalDate endDate = dto.getVacationEnd();
            AttendType vacationType = AttendType.fromString(dto.getVacationType());

            int vacationCount = docService.countVacation(startDate, endDate);

            Doc doc = Doc.builder().docWriter(loginEmpId)
                    .docType(DocType.VACATION)
                    .docTitle("휴가신청서(" + loginEmpId + ")")
                    .docContent(dto.getVacationInfo())
                    .docCreateDate(LocalDateTime.now())
                    .signFinish(false).build();

            DocVacation.DocVacationBuilder docVacationBuilder = DocVacation.builder()
                    .vacationStart(startDate)
                    .vacationEnd(endDate)
                    .vacationCount(vacationCount)
                    .vacationType(vacationType);

            List<String> signers = new ArrayList<>();
            signers.add(loginEmpId);
            String managerEmpId = empService.findManager(loginEmpId);
            signers.add(managerEmpId);

            docService.saveVacationDoc(doc, docVacationBuilder, signers, null);

            return "--휴가 신청 정보를 요약해서 답변해주면 됩니다.";

        } catch (
                JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON input: " + functionArguments, e);
        }
    }
}