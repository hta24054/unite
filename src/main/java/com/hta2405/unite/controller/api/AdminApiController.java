package com.hta2405.unite.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.domain.Notice;
import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.EmpRegisterDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.HolidayService;
import com.hta2405.unite.service.NoticeService;
import com.hta2405.unite.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {
    private final HolidayService holidayService;
    private final ResourceService resourceService;
    private final NoticeService noticeService;
    private final EmpService empService;

    @GetMapping("/holiday")
    public List<Holiday> getHolidayList(String start, String end) {
        String[] startString = start.split("-");
        String[] endString = end.split("-");

        LocalDate startDate = LocalDate.of(Integer.parseInt(startString[0]), Integer.parseInt(startString[1]), 1);
        YearMonth endYearMonth = YearMonth.of(Integer.parseInt(endString[0]), Integer.parseInt(endString[1]));
        LocalDate endDate = endYearMonth.atEndOfMonth();

        return holidayService.getHolidayList(startDate, endDate);
    }

    @PostMapping("/holiday")
    public int addCustomHoliday(LocalDate date, String holidayName) {
        Holiday holiday = Holiday.builder().holidayDate(date).holidayName(holidayName).build();
        return holidayService.addHoliday(holiday);
    }

    @DeleteMapping("/holiday")
    public int deleteHoliday(LocalDate date) {
        return holidayService.deleteHoliday(date);
    }


    @PostMapping("/holiday/weekend")
    public boolean addWeekend() {
        LocalDate nowDate = LocalDate.now();
        LocalDate endDate = nowDate.plusYears(1);
        return holidayService.addWeekend(nowDate.minusYears(1), endDate);
    }

    @PostMapping("/holiday/api")
    public boolean addHolidayWithApi() {
        return holidayService.insertYearlyHoliday();
    }

    @GetMapping("/resource")
    public Map<String, Object> getAllResource() {
        List<Resource> resourceList = resourceService.getResourceList();

        // DataTables가 요구하는 JSON 구조 생성
        Map<String, Object> response = new HashMap<>();
        response.put("data", resourceList);

        return response;
    }

    @PostMapping("/resource")
    public int addResource(Resource resource) {
        return resourceService.addResource(resource);
    }

    @PatchMapping("/resource")
    public int updateResource(Resource resource) {
        return resourceService.updateResource(resource);
    }

    @DeleteMapping("/resource")
    public int deleteResource(@RequestParam("selectedIds[]") long[] selectedIds) {
        List<Long> list = Arrays.stream(selectedIds).boxed().toList();
        return resourceService.deleteResource(list);
    }

    @PostMapping("/notice")
    public int addNotice(Notice notice) {
        return noticeService.addNotice(notice);
    }

    @PatchMapping("/notice")
    public int updateNotice(Notice notice) {
        return noticeService.updateNotice(notice);
    }

    @DeleteMapping("/notice")
    public int deleteResource(Long noticeId) {
        return noticeService.deleteNotice(noticeId);
    }

    @PostMapping("/emp-manage")
    public Map<String, Object> registerEmp(@RequestPart(value = "file", required = false) MultipartFile file,
                                           @RequestPart(value = "dto") String dtoJson
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        EmpRegisterDTO empRegisterDTO;
        try {
            empRegisterDTO = objectMapper.readValue(dtoJson, EmpRegisterDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 데이터 변환 실패", e);
        }
        Emp emp = empService.registerEmp(empRegisterDTO, file);
        HashMap<String, Object> map = new HashMap<>();
        map.put("empId", emp.getEmpId());
        map.put("ename", emp.getEname());
        return map;
    }

    @DeleteMapping("/emp-manage")
    public int fireEmp(String empId) {
        return empService.resignEmp(empId);
    }
}
