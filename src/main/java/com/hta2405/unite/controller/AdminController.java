package com.hta2405.unite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.domain.Notice;
import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.EmpRegisterDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.HolidayService;
import com.hta2405.unite.service.NoticeService;
import com.hta2405.unite.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final HolidayService holidayService;
    private final ResourceService resourceService;
    private final NoticeService noticeService;
    private final EmpService empService;

    public AdminController(HolidayService holidayService, ResourceService resourceService, NoticeService noticeService, EmpService empService) {
        this.holidayService = holidayService;
        this.resourceService = resourceService;
        this.noticeService = noticeService;
        this.empService = empService;
    }

    @GetMapping("/holiday")
    public String showHolidayPage() {
        return "/admin/holiday";
    }

    @PostMapping("/holiday")
    @ResponseBody
    public String addCustomHoliday(LocalDate date, String holidayName) {
        Holiday holiday = Holiday.builder().holidayDate(date).holidayName(holidayName).build();
        return holidayService.addHoliday(holiday);
    }

    @DeleteMapping("/holiday")
    @ResponseBody
    public String deleteHoliday(LocalDate date) {
        return holidayService.deleteHoliday(date);
    }

    @GetMapping("/holiday/list")
    @ResponseBody
    public List<Holiday> getHolidayList(String start, String end) {
        String[] startString = start.split("-");
        String[] endString = end.split("-");

        LocalDate startDate = LocalDate.of(Integer.parseInt(startString[0]), Integer.parseInt(startString[1]), 1);
        YearMonth endYearMonth = YearMonth.of(Integer.parseInt(endString[0]), Integer.parseInt(endString[1]));
        LocalDate endDate = endYearMonth.atEndOfMonth();

        return holidayService.getHolidayList(startDate, endDate);
    }

    @PostMapping("/holiday/weekend")
    @ResponseBody
    public String addWeekend() {
        LocalDate nowDate = LocalDate.now();
        LocalDate endDate = nowDate.plusYears(1);
        holidayService.addWeekend(nowDate.minusYears(1), endDate);
        return "주말을 등록하였습니다.";
    }

    @PostMapping("/holiday/api")
    @ResponseBody
    public String addHolidayWithApi() {
        holidayService.insertYearlyHoliday();
        return "공휴일을 등록하였습니다.";
    }

    @GetMapping("/resource")
    public String showResourcePage() {
        return "/admin/resource2";
    }

    @GetMapping("/resource/list")
    @ResponseBody
    public Map<String, Object> getAllResource() {
        List<Resource> resourceList = resourceService.getResourceList();

        // DataTables가 요구하는 JSON 구조 생성
        Map<String, Object> response = new HashMap<>();
        response.put("data", resourceList);

        return response;
    }

    @PostMapping("/resource")
    @ResponseBody
    public String addResource(Resource resource) {
        int result = resourceService.addResource(resource);
        if (result != 1) {
            return "자원 등록 실패";
        }
        return "자원 등록 성공";
    }

    @PatchMapping("/resource")
    @ResponseBody
    public String updateResource(Resource resource) {
        int result = resourceService.updateResource(resource);
        if (result != 1) {
            return "자원 수정 실패";
        }
        return "자원 수정 성공";
    }

    @DeleteMapping("/resource")
    @ResponseBody
    public String deleteResource(@RequestParam("selectedIds[]") long[] selectedIds) {
        List<Long> list = Arrays.stream(selectedIds).boxed().toList();
        int result = resourceService.deleteResource(list);
        if (result < 1) {
            return "자원 삭제 실패";
        }
        return "자원 삭제 성공";
    }

    @GetMapping("/notice")
    public ModelAndView showNoticePage(ModelAndView mv) {
        mv.addObject("noticeList", noticeService.getAllNotice());
        mv.setViewName("/admin/notice");
        return mv;
    }

    @PostMapping("/notice")
    @ResponseBody
    public String addNotice(Notice notice) {
        int result = noticeService.addNotice(notice);
        if (result != 1) {
            return "공지사항 등록 실패";
        }
        return "공지사항 등록 성공";
    }

    @PatchMapping("/notice")
    @ResponseBody
    public String updateNotice(Notice notice) {
        int result = noticeService.updateNotice(notice);
        if (result != 1) {
            return "공지사항 수정 실패";
        }
        return "공지사항 수정 성공";
    }

    @DeleteMapping("/notice")
    @ResponseBody
    public String deleteResource(Long noticeId) {
        int result = noticeService.deleteNotice(noticeId);
        if (result < 1) {
            return "공지사항 삭제 실패";
        }
        return "공지사항 삭제 성공";
    }

    @GetMapping("/emp-manage")
    public String showEmpManage() {
        return "/admin/empManage";
    }

    @GetMapping("/emp-manage/info")
    public String showEmpInfo(String empId) {
        return "redirect:/emp/info?empId=" + empId;
    }

    @GetMapping("/emp-manage/register")
    public ModelAndView showEmpRegister(ModelAndView mv) {
        mv.setViewName("/emp/empRegister");
        mv.addObject("registerDTO", empService.getRegisterPageData());
        return mv;
    }

    @PostMapping("/emp-manage")
    @ResponseBody
    public String registerEmp(@RequestPart(value = "file", required = false) MultipartFile file,
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

        return String.format("%s(%s) 직원을 등록하였습니다.", emp.getEname(), emp.getEmpId());
    }

    @DeleteMapping("/emp-manage")
    @ResponseBody
    public String fireEmp(String empId) {
        empService.resignEmp(empId);
        return "퇴사처리 완료";
    }
}
