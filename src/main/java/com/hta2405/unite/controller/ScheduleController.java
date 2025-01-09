package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.dto.ScheduleDTO;
import com.hta2405.unite.service.ScheduleService;
import com.hta2405.unite.util.CalendarDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @GetMapping("/calender")
    public String calender() {
        return "schedule/scheduleCalender";
    }

    @ResponseBody
    @GetMapping("/scheduleList")
    public List<Schedule> getListSchedule() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("로그인되지 않은 사용자입니다.");
        }

        String id = authentication.getName(); // 로그인한 사용자의 ID (username)
        List<Schedule> schedules = scheduleService.getListSchedule(id);
        //System.out.println("schedules " + schedules);

        return schedules;
    }

    @ResponseBody
    @PostMapping("/scheduleAdd")
    public int insertSchedule(Schedule schedule) {
        int result = scheduleService.insertSchedule(schedule);
        //System.out.println("schedule 등록 " + schedule);
        return result;
    }

    @ResponseBody
    @PostMapping("/scheduleUpdate")
    public int updateSchedule(Schedule schedule) {
        int result = scheduleService.updateSchedule(schedule);
        //System.out.println("schedule 수정 " + schedule);
        return result;
    }

    @ResponseBody
    @PostMapping("/scheduleDragUpdate")
    public int dragUpdateSchedule(Schedule schedule) {
        int result = scheduleService.dragUpdateSchedule(schedule);
        //System.out.println("schedule 드래그로 수정 " + schedule);
        return result;
    }

    @ResponseBody
    @PostMapping("/scheduleDelete")
    public int deleteSchedule(int scheduleId) {
        int result = scheduleService.deleteSchedule(scheduleId);
        //System.out.println("schedule 삭제 " + result);
        return result;
    }

    // 공유 일정 등록 페이지
    @GetMapping("/scheduleShare")
    public String shareSchedule(Schedule schedule) {
        return "schedule/scheduleShare";
    }

    @ResponseBody
    @GetMapping("/sharedScheduleList")
    public List<ScheduleDTO> getListSharedSchedule() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ArrayList<>();
        }
        String empId = authentication.getName(); // 로그인한 사용자의 ID (username)

        List<Schedule> sharedSchedules = scheduleService.getListSharedSchedule(empId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : sharedSchedules) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setScheduleId((long) schedule.getScheduleId());
            scheduleDTO.setEmpId(schedule.getEmpId());

            // Schedule 객체에서 직접 shareEmp 값을 가져오기 위해 ScheduleShare 조회
            List<ScheduleShare> scheduleShares = scheduleService.getScheduleSharesByScheduleId(schedule.getScheduleId());
            if (scheduleShares != null && !scheduleShares.isEmpty()) {
                // 첫 번째 공유 직원만 가져오는 예시 (다수의 공유 직원이 있을 수 있음)
                scheduleDTO.setShareEmp(scheduleShares.get(0).getShareEmp());
            } else {
                scheduleDTO.setShareEmp("");  // 공유 직원이 없으면 빈 값 설정
            }

            scheduleDTO.setScheduleName(schedule.getScheduleName());
            scheduleDTO.setScheduleContent(String.valueOf(schedule.getScheduleContent()));
            scheduleDTO.setScheduleStart(String.valueOf(schedule.getScheduleStart()));
            scheduleDTO.setScheduleEnd(String.valueOf(schedule.getScheduleEnd()));
            scheduleDTO.setScheduleColor(schedule.getScheduleColor());
            scheduleDTO.setScheduleAllDay(schedule.isScheduleAllDay());

            scheduleDTO.setShareEmpNames(scheduleService.getShareEmpNames(schedule.getScheduleId())); //공유된 직원들 이름조회
            scheduleDTO.setEmpIdName(scheduleService.getEmpIdName(schedule.getEmpId()));

            scheduleDTOList.add(scheduleDTO);
        }

        //System.out.println("공유 일정 DTO 리스트: " + scheduleDTOList);
        return scheduleDTOList;
    }

    @ResponseBody
    @PostMapping("/scheduleShareAdd")
    public int insertScheduleShare(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleDTO.getSchedule();
        ScheduleShare scheduleShare = scheduleDTO.getScheduleShare();

        // CalendarDateTimeUtil을 사용하여 날짜 변환
        if (schedule != null) {
            String scheduleStartStr = scheduleDTO.getScheduleStart();
            String scheduleEndStr = scheduleDTO.getScheduleEnd();

            // "T" 제거 후 LocalDateTime으로 변환
            schedule.setScheduleStart(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleStartStr));
            schedule.setScheduleEnd(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleEndStr));
        }

        int updatedScheduleDTO = scheduleService.insertScheduleShare(scheduleDTO);
        //System.out.println("\n scheduleShare.getScheduleId() = " + schedule.getScheduleId());
        return updatedScheduleDTO;
    }

    // 부서 일정 등록 페이지
    @GetMapping("/scheduleDept")
    public String scheduleDept(Schedule schedule) {
        return "schedule/scheduleDept";
    }

    @ResponseBody
    @PostMapping("/scheduleDeptAdd")
    public int insertScheduleDept(@RequestBody ScheduleDTO scheduleDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empId = authentication.getName();

        scheduleDTO.getSchedule().setEmpId(empId);
        Schedule schedule = scheduleDTO.getSchedule();

        if (schedule != null) {
            String scheduleStartStr = scheduleDTO.getScheduleStart();
            String scheduleEndStr = scheduleDTO.getScheduleEnd();

            schedule.setScheduleStart(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleStartStr));
            schedule.setScheduleEnd(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleEndStr));
        }

        int scheduleDeptDTO = scheduleService.insertScheduleDept(scheduleDTO);
        return scheduleDeptDTO;
    }

    @ResponseBody
    @GetMapping("/deptScheduleList")
    public List<ScheduleDTO> getListDeptSchedule() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ArrayList<>();
        }
        String empId = authentication.getName(); // 로그인한 사용자의 ID (username)

        // 부서 ID를 얻기 위해 사용자 정보로부터 부서 조회
        String deptId = scheduleService.getDeptIdByEmpId(empId);  // 로그인한 사용자의 부서 ID

        // 부서 일정을 조회, 등록자 제외
        List<Schedule> deptSchedules = scheduleService.getListDeptSchedule(deptId, empId);  // 등록자 empId도 전달
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        // 각 일정 객체를 DTO로 변환
        for (Schedule schedule : deptSchedules) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setScheduleId((long) schedule.getScheduleId());
            scheduleDTO.setEmpId(schedule.getEmpId());

            // 부서 일정을 등록한 직원의 정보를 가져옴
            scheduleDTO.setScheduleName(schedule.getScheduleName());
            scheduleDTO.setScheduleContent(String.valueOf(schedule.getScheduleContent()));
            scheduleDTO.setScheduleStart(String.valueOf(schedule.getScheduleStart()));
            scheduleDTO.setScheduleEnd(String.valueOf(schedule.getScheduleEnd()));
            scheduleDTO.setScheduleColor(schedule.getScheduleColor());
            scheduleDTO.setScheduleAllDay(schedule.isScheduleAllDay());

            scheduleDTOList.add(scheduleDTO);
        }

        System.out.println("\n *** Converted scheduleDTOList :" + scheduleDTOList);

        return scheduleDTOList;
    }




}
