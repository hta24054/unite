package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.dto.ScheduleDTO;
import com.hta2405.unite.mybatis.mapper.ScheduleMapper;
import com.hta2405.unite.util.CalendarDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleMapper scheduleDAO;

    @Autowired
    public ScheduleServiceImpl(ScheduleMapper scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    @Override
    public List<Schedule> getListSchedule(String empId, String startDate, String endDate) {
        return scheduleDAO.getListSchedule(empId, startDate, endDate);
    }

    @Override
    public List<Schedule> getDailyScheduleList(String empId, LocalDate date) {
        return scheduleDAO.getDailyScheduleList(empId, date);
    }

    @Override
    public int insertSchedule(Schedule schedule) {
        return scheduleDAO.insertSchedule(schedule);
    }

    @Override
    public int updateSchedule(Schedule schedule) {
        return scheduleDAO.updateSchedule(schedule);
    }

    @Override
    public int dragUpdateSchedule(Schedule schedule) {
        return scheduleDAO.dragUpdateSchedule(schedule);
    }

    @Override
    public int deleteSchedule(int scheduleId) {
        return scheduleDAO.deleteSchedule(scheduleId);
    }

    @Override
    public int insertScheduleShare(ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleDTO.getSchedule();

        // CalendarDateTimeUtil을 사용하여 날짜 변환
        if (schedule != null) {
            String scheduleStartStr = scheduleDTO.getScheduleStart();
            String scheduleEndStr = scheduleDTO.getScheduleEnd();

            // "T" 제거 후 LocalDateTime으로 변환
            schedule.setScheduleStart(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleStartStr));
            schedule.setScheduleEnd(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleEndStr));
        }

        scheduleDAO.insertScheduleShare(schedule);

        // 공유 직원 저장
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("scheduleId", scheduleDTO.getSchedule().getScheduleId());
        hashMap.put("shareEmp", scheduleDTO.getScheduleShare().getShareEmp());

        return scheduleDAO.insertScheduleShareUsers(hashMap);
    }

    @Override
    public List<ScheduleDTO> getSharedSchedules(String empId) {
        List<Schedule> sharedSchedules = scheduleDAO.getListSharedSchedule(empId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : sharedSchedules) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setScheduleId((long) schedule.getScheduleId());
            scheduleDTO.setEmpId(schedule.getEmpId());

            // Schedule 객체에서 직접 shareEmp 값을 가져오기 위해 ScheduleShare 조회
            List<ScheduleShare> scheduleShares = getScheduleSharesByScheduleId(schedule.getScheduleId());
            scheduleDTO.setShareEmp(scheduleShares.isEmpty() ? "" : scheduleShares.get(0).getShareEmp());

            scheduleDTO.setScheduleName(schedule.getScheduleName());
            scheduleDTO.setScheduleContent(String.valueOf(schedule.getScheduleContent()));
            scheduleDTO.setScheduleStart(String.valueOf(schedule.getScheduleStart()));
            scheduleDTO.setScheduleEnd(String.valueOf(schedule.getScheduleEnd()));
            scheduleDTO.setScheduleColor(schedule.getScheduleColor());
            scheduleDTO.setScheduleAllDay(schedule.isScheduleAllDay());

            scheduleDTO.setShareEmpNames(getShareEmpNames(schedule.getScheduleId())); // 공유된 직원들 이름 조회
            scheduleDTO.setEmpIdName(getEmpIdName(schedule.getEmpId()));

            scheduleDTOList.add(scheduleDTO);
        }

        return scheduleDTOList;
    }

    @Override
    public List<ScheduleShare> getScheduleSharesByScheduleId(int scheduleId) {
        return scheduleDAO.getScheduleSharesByScheduleId(scheduleId);
    }

    @Override
    public String getShareEmpNames(int scheduleId) {
        return scheduleDAO.getShareEmpNames(scheduleId);
    }

    @Override
    public String getEmpIdName(String empId) {
        return scheduleDAO.getEmpIdName(empId);
    }

    public int insertScheduleDept(ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleDTO.getSchedule();

        if (schedule != null) {
            String scheduleStartStr = scheduleDTO.getScheduleStart();
            String scheduleEndStr = scheduleDTO.getScheduleEnd();

            schedule.setScheduleStart(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleStartStr));
            schedule.setScheduleEnd(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleEndStr));
        }

        schedule.setEmpId(scheduleDTO.getEmpId());
        scheduleDAO.insertScheduleDept(schedule);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("scheduleId", scheduleDTO.getSchedule().getScheduleId());
        hashMap.put("deptId", scheduleDTO.getDept().getDeptId());

        return scheduleDAO.insertScheduleShareWithDept(hashMap);
    }

    // 사용자의 부서 ID 조회
    @Override
    public String getDeptIdByEmpId(String empId) {
        return scheduleDAO.getDeptIdByEmpId(empId);
    }

    @Override
    public List<Schedule> getListDeptSchedule(String deptId, String empId) {
        List<Schedule> deptSchedule = scheduleDAO.getScheduleForDept(deptId, empId);
        return deptSchedule;
    }

    public List<ScheduleDTO> getScheduleDTOList(String empId) {
        // 부서 ID를 얻기 위해 사용자 정보로부터 부서 조회
        String deptId = getDeptIdByEmpId(empId);  // 로그인한 사용자의 부서 ID

        // 부서 일정 조회, 등록자 제외
        List<Schedule> deptSchedules = getListDeptSchedule(deptId, empId); // 등록자 empId도 전달

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        // 각 일정 객체 DTO로 변환
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
        return scheduleDTOList;
    }
}
