package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.mybatis.mapper.BoardPostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardPostService {
    private final BoardPostMapper boardPostMapper;
    private final EmpService empService;

    public Optional<BoardPostEmpDTO> getBoardListAll(String empId) {
        Emp emp = empService.getEmpById(empId).orElseThrow(() -> new RuntimeException("Emp not found"));
        Long deptId = emp.getDeptId();

        Optional<BoardPostEmpDTO> boardPostEmp = boardPostMapper.getBoardListAll(deptId);

        if (boardPostEmp.isEmpty()) {
            log.warn("No board data found for deptId: " + deptId);
        } else {
            log.info("Board data: {}", boardPostEmp.get());
        }

        log.info("결과 : "+ boardPostEmp.isEmpty());
        return boardPostEmp;
    }
}
