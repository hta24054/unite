package com.hta2405.unite.service;

import com.hta2405.unite.domain.Attend;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.dto.AttendDTO;
import com.hta2405.unite.dto.AttendInfoDTO;
import com.hta2405.unite.enums.AttendType;
import com.hta2405.unite.mybatis.mapper.AttendMapper;
import com.hta2405.unite.mybatis.mapper.HolidayMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hta2405.unite.enums.AttendType.*;

@Service
@RequiredArgsConstructor
public class AttendService {
    private final HolidayMapper holidayMapper;
    private final AttendMapper attendMapper;

    public AttendInfoDTO getAttendInfoDTO(int year, int month, Emp targetEmp) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        //한달 날짜 모두 리스트화(Attend 객체에는 날짜정보만)
        List<AttendDTO> attendDTOList = makeAllDays(startDate, endDate);

        //휴일 업데이트(AttendDTO 내 attendType에 휴일 정보 추가)
        List<Holiday> holidayList = holidayMapper.getHolidayList(startDate, endDate);
        updateDTOFromHolidayList(attendDTOList, holidayList);

        //한달간의 해당 직원 근태목록 불러와 업데이트
        updateDTOFromEntity(startDate, endDate, targetEmp, attendDTOList);

        int allWorkDate = endDate.getDayOfMonth() - holidayList.size();
        int myWorkDate = 0; //내 총 근무일
        int vacation = 0; //휴가일
        int absent = 0; //결근일
        int lateOrLeaveEarly = 0; //지각, 또는 조퇴일

        // attendType ={일반, 출장, 외근, 휴가, 결근, 휴일}
        // 지각 및 내 근무일 카운트
        for (AttendDTO attendDTO : attendDTOList) {
            if (attendDTO.getAttendType() == null) {
                continue;
            }
            if (attendDTO.getAttendType().equals(HOLIDAY)) {
                continue;
            }
            if (attendDTO.getAttendType().isSubTypeOf(VACATION)) {
                vacation++;
                continue;
            }
            if (attendDTO.getAttendType().equals(ABSENT)) {
                absent++;
                continue;
            }
            if (attendDTO.getAttendDate().isBefore(LocalDate.now()) &&
                    isLateOrLeaveEarly(attendDTO)) {
                lateOrLeaveEarly++;
            }
            myWorkDate++;
        }

        return new AttendInfoDTO(targetEmp.getEname(), attendDTOList, allWorkDate, myWorkDate, vacation, absent, lateOrLeaveEarly);
    }

    //DB에 있는 실제 근태기록을 DTO에 반영함
    private void updateDTOFromEntity(LocalDate startDate, LocalDate endDate, Emp emp, List<AttendDTO> attendDTOList) {
        List<Attend> attendEntityList = attendMapper.getAttendListByEmpId(emp.getEmpId(), startDate, endDate);

        //        //휴가, 휴일이 가장 뒤로 정렬되면서,
//        attendEntityList.sort(Comparator.comparing(attend ->
//                attend.getAttendType().isSubTypeOf(AttendType.VACATION) ? 1 : 0));

        Map<LocalDate, Attend> attendMap = attendEntityList.stream()
                .collect(Collectors.toMap(Attend::getAttendDate, attend -> attend));


        // allDate를 순회하며 매칭
        for (AttendDTO dto : attendDTOList) {
            Attend attendEntity = attendMap.get(dto.getAttendDate()); //
            if (attendEntity != null) {
                dto.setInTime(attendEntity.getInTime());
                dto.setOutTime(attendEntity.getOutTime());
                dto.setAttendType(attendEntity.getAttendType());

                // 근무 시간 계산
                if (attendEntity.getInTime() != null && attendEntity.getOutTime() != null) {
                    dto.setWorkTime(Duration.between(attendEntity.getInTime(), attendEntity.getOutTime()));
                }
            }
        }

        LocalDate today = LocalDate.now();
        //근무형태 기입
        for (AttendDTO attendDTO : attendDTOList) {

            //DTO에 기록된 사전에 예정된 근무형태(휴일, 휴가, 출장 등)을 가져옴, 없으면 '결근' 선택
            AttendType attendType = Optional.ofNullable(attendDTO.getAttendType()).orElse(ABSENT);

            //오늘보다 이전 날짜면서, 퇴근시간기록이 누락되었고, 휴일 및 휴가가 아닐 경우 결근 기록
            if (attendDTO.getAttendDate().isBefore(today)
                    && attendDTO.getOutTime() == null
                    && !attendType.equals(HOLIDAY)
                    && !attendType.isSubTypeOf(VACATION)) {
                attendDTO.setAttendType(ABSENT);
            }
        }
    }

    private List<AttendDTO> makeAllDays(LocalDate startDate, LocalDate endDate) {
        List<AttendDTO> list = new ArrayList<>();

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            list.add(new AttendDTO(date));
            date = date.plusDays(1);
        }
        return list;
    }

    private void updateDTOFromHolidayList(List<AttendDTO> attendDTOList, List<Holiday> holidayList) {
        // 공휴일 리스트를 Map으로 변환 (Key: 날짜, Value: 공휴일 이름)
        Map<LocalDate, String> holidayMap = holidayList.stream()
                .collect(Collectors.toMap(Holiday::getHolidayDate, Holiday::getHolidayName));

        // AttendDTO 리스트를 순회하며 attendType 업데이트
        attendDTOList.forEach(attendDTO -> {
            if (holidayMap.containsKey(attendDTO.getAttendDate())) {
                attendDTO.setAttendType(HOLIDAY);
            }
        });
    }

    //조퇴 또는 지각 판별, 9 to 6 기업으로 가정
    public boolean isLateOrLeaveEarly(AttendDTO attend) {
        LocalDateTime attendIn = attend.getInTime();
        LocalDateTime attendOut = attend.getOutTime();

        //아직 출근 또는 퇴근기록이 없으면(종결 아닌경우) 지각, 조퇴 X
        if (attendIn == null || attendOut == null) {
            return false;
        }

        LocalTime inTime = attendIn.toLocalTime();
        LocalTime outTime = attendOut.toLocalTime();
        return inTime.isAfter(LocalTime.of(9, 0)) || outTime.isBefore(LocalTime.of(18, 0));
    }


    public Map<String, Object> getTodayRecord(String empId, LocalDate date) {
        HashMap<String, Object> response = new HashMap<>();
        Attend attend = attendMapper.getAttendByEmpId(empId, date);
        String status = "empty";

        if (attend == null) {
            response.put("status", status);
            return response;
        }

        if (attend.getOutTime() == null) {
            status = "checkIn";
            response.put("status", status);
        } else {
            status = "checkOut";
            response.put("status", status);
        }
        response.put("attend", attend);
        return response;
    }

    public ResponseEntity<Object> attendIn(UserDetails user, String attendType) {
        AttendType enumType = AttendType.fromString(attendType);
        int result = attendMapper.attendIn(user.getUsername(), enumType);
        if (result != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
        return ResponseEntity.ok("정상 처리");
    }

    public ResponseEntity<Object> attendOut(UserDetails user) {
        Attend attend = attendMapper.getAttendByEmpId(user.getUsername(), LocalDate.now());
        if (attend == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
        int result = attendMapper.attendOut(user.getUsername());
        if (result != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
        return ResponseEntity.ok("정상 처리");
    }
}
