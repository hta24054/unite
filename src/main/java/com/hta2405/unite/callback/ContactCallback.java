package com.hta2405.unite.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.dto.ai.AiContactDTO;
import com.hta2405.unite.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContactCallback implements FunctionCallback {
    private final EmpService empService;

    @Override
    public String getName() {
        return "findUserInformation";
    }

    @Override
    public String getDescription() {
        return "get a user information for the user";
    }

    @Override
    public String getInputTypeSchema() {
        return """
                {
                  "type": "object",
                  "properties": {
                    "name": {"type": "string"}
                  },
                  "required": ["name"]
                }
                """;
    }

    @Override
    public String call(String functionArguments) {
        log.info("functionArguments = {}", functionArguments);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            AiContactDTO dto = objectMapper.readValue(functionArguments, AiContactDTO.class);
            String name = dto.getName();
            List<EmpListDTO> empList = empService.getAllEmpListDTO();
            EmpListDTO targetEmp = null;
            for (EmpListDTO empListDTO : empList) {
                if (empListDTO.getEname().equals(name)) {
                    targetEmp = empListDTO;
                    break;
                }
            }
            return objectMapper.writeValueAsString(targetEmp);

        } catch (
                JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON input: " + functionArguments, e);
        }
    }
}