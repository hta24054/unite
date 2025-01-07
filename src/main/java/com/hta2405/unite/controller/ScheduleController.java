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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@Slf4j
@RequestMapping("/schedule")
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

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


    /*
    @ResponseBody
    @GetMapping("/sharedScheduleList")
    public List<Schedule> getSharedSchedule(ScheduleDTO scheduleDTO) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ArrayList<>();
        }
        String empId = authentication.getName(); // 로그인한 사용자의 ID (username)

        // scheduleShare가 null인지 체크
        ScheduleShare scheduleShare = scheduleDTO.getScheduleShare();
        if (scheduleShare == null) {
            return new ArrayList<>(); // null일 경우 빈 리스트 반환 (또는 다른 처리를 할 수 있음)
        }

        // scheduleShare가 null이 아닐 때 계속 진행
        List<Schedule> result = scheduleService.getSharedSchedule(empId, scheduleDTO);
        System.out.println("공유 일정 " + result);
        return result;
    }*/



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

            scheduleDTOList.add(scheduleDTO);
        }

        System.out.println("공유 일정 DTO 리스트: " + scheduleDTOList);
        return scheduleDTOList;
    }

//    @ResponseBody
//    @GetMapping("/sharedScheduleList")
//    public List<ScheduleDTO> getListSharedSchedule() {
//        // 현재 인증된 사용자 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return new ArrayList<>();
//        }
//        String empId = authentication.getName(); // 로그인한 사용자의 ID (username)
//
//        List<Schedule> sharedSchedules = scheduleService.getListSharedSchedule(empId);
//        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
//
//        for (Schedule schedule : sharedSchedules) {
//            ScheduleDTO scheduleDTO = new ScheduleDTO();
//            scheduleDTO.setScheduleId((long) schedule.getScheduleId());
//            scheduleDTO.setEmpId(schedule.getEmpId());
//
//            // scheduleShare가 null이 아니면 shareEmp 값을 설정
//            if (scheduleDTO.getScheduleShare() != null) {
//                scheduleDTO.setShareEmp(scheduleDTO.getScheduleShare().getShareEmp());  // 실제로 shareEmp 설정
//            } else {
//                scheduleDTO.setShareEmp("");  // 혹은 null로 설정
//            }
//
//            scheduleDTO.setScheduleName(schedule.getScheduleName());
//            scheduleDTO.setScheduleContent(String.valueOf(schedule.getScheduleContent()));
//            scheduleDTO.setScheduleStart(String.valueOf(schedule.getScheduleStart()));
//            scheduleDTO.setScheduleEnd(String.valueOf(schedule.getScheduleEnd()));
//            scheduleDTO.setScheduleColor(schedule.getScheduleColor());
//            scheduleDTO.setScheduleAllDay(schedule.isScheduleAllDay());
//            scheduleDTOList.add(scheduleDTO);
//        }
//
//        System.out.println("공유 일정 DTO 리스트: " + scheduleDTOList);
//        return scheduleDTOList;
//    }

    /*
    @ResponseBody
    @GetMapping("/sharedScheduleList")
    public List<Schedule> getListSharedSchedule() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ArrayList<>();
        }
        String empId = authentication.getName(); // 로그인한 사용자의 ID (username)
        //System.out.println("공유 일정 empId " + empId);

        List<Schedule> sharedSchedules = scheduleService.getListSharedSchedule(empId);
        System.out.println("공유 일정 " + sharedSchedules);

        return sharedSchedules;
    }*/

    @ResponseBody
    @PostMapping("/scheduleShareAdd")
    public int insertScheduleShare(@RequestBody ScheduleDTO scheduleDTO) {

        // ScheduleDTO에서 schedule, scheduleShare 객체 추출
        Schedule schedule = scheduleDTO.getSchedule();
        ScheduleShare scheduleShare = scheduleDTO.getScheduleShare();
        String shareEmp = scheduleShare.getShareEmp();

        //System.out.println("Schedule ID: " + schedule.getScheduleId());
        //System.out.println("shareEmp" + shareEmp);
        //System.out.println("scheduleShare" + scheduleShare);

        // CalendarDateTimeUtil을 사용하여 날짜 변환
        if (schedule != null) {
            String scheduleStartStr = scheduleDTO.getScheduleStart();
            String scheduleEndStr = scheduleDTO.getScheduleEnd();

            // "T" 제거 후 LocalDateTime으로 변환
            schedule.setScheduleStart(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleStartStr));
            schedule.setScheduleEnd(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleEndStr));
        }

        int updatedScheduleDTO = scheduleService.insertScheduleShare(scheduleDTO);
        System.out.println("\n scheduleShare.getScheduleId() = " + schedule.getScheduleId());
        return updatedScheduleDTO;
    }
}
