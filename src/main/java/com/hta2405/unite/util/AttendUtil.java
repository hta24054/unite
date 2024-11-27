package com.hta2405.unite.util;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.HolidayDao;
import com.hta2405.unite.dto.Attend;
import com.hta2405.unite.dto.DocVacation;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Holiday;
import com.hta2405.unite.enums.AttendType;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.hta2405.unite.enums.AttendType.*;

public class AttendUtil {
    HolidayDao holidayDao = new HolidayDao();
    AttendDao attendDao = new AttendDao();
    EmpDao empDao = new EmpDao();

    public ActionForward getAttendDetail(HttpServletRequest req, Emp targetEmp) {
        int year = Integer.parseInt(req.getParameter("year"));
        int month = Integer.parseInt(req.getParameter("month"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        //한달 날짜 모두 리스트화(Attend 객체에는 날짜정보만)
        List<Attend> allDate = insertAllDays(startDate, endDate);

        //휴일 업데이트(Attend객체 내 attendType에 휴일 정보 추가)
        List<Holiday> holidayList = holidayDao.getHoliday(startDate, endDate);
        updateHoliday(allDate, holidayList);

        //한달간의 해당 직원 근태목록 불러와 업데이트
        updateEmpAttend(startDate, endDate, targetEmp, allDate);

        int allWorkDate = endDate.getDayOfMonth() - holidayList.size();
        int myWorkDate = 0; //내 총 근무일
        int vacation = 0; //휴가일
        int absent = 0; //결근일
        int lateOrLeaveEarly = 0; //지각, 또는 조퇴일

        // attendType ={일반, 출장, 외근, 휴가, 결근, 휴일}
        // 지각 및 내 근무일 카운트
        for (Attend attend : allDate) {
            if (attend.getAttendType() == null) {
                continue;
            }
            if (attend.getAttendType().equals(HOLIDAY)) {
                continue;
            }
            if (attend.getAttendType().isSubTypeOf(VACATION)) {
                vacation++;
                continue;
            }
            if (attend.getAttendType().equals(ABSENT)) {
                absent++;
                continue;
            }
            if (attend.getAttendDate().isBefore(LocalDate.now()) &&
                    isLateOrLeaveEarly(attend)) {
                lateOrLeaveEarly++;
            }
            myWorkDate++;
        }

        req.setAttribute("targetEmp", targetEmp); //근태확인할 직원정보
        req.setAttribute("attendList", allDate); //전체 근태
        req.setAttribute("allWorkDate", allWorkDate); //당월 총 근무일수
        req.setAttribute("myWorkDate", myWorkDate); //근무일수
        req.setAttribute("vacation", vacation); //휴가일수
        req.setAttribute("absent", absent); //결근일수
        req.setAttribute("lateOrLeaveEarly", lateOrLeaveEarly); //지각, 조퇴일수
        return new ActionForward(false, "/WEB-INF/views/attend/attendDetail.jsp");
    }

    private void updateEmpAttend(LocalDate startDate, LocalDate endDate, Emp emp, List<Attend> allDate) {
        // 출근 기록을 날짜별로 매핑
        List<Attend> attendList = new AttendDao().getAttendByEmpId(emp, startDate, endDate);
        //휴가, 휴일이 가징 뒤로 정렬되면서,
        attendList.sort(Comparator.comparing(attend ->
                attend.getAttendType().isSubTypeOf(AttendType.VACATION) ? 1 : 0));

        Map<LocalDate, Attend> attendMap = new HashMap<>();
        for (Attend attend : attendList) {
            attendMap.put(attend.getAttendDate(), attend);
        }
        // allDate를 순회하며 매칭
        for (Attend date : allDate) {
            Attend matchedAttend = attendMap.get(date.getAttendDate());
            if (matchedAttend != null) {
                date.setEmpId(matchedAttend.getEmpId());
                date.setAttendIn(matchedAttend.getAttendIn());
                date.setAttendOut(matchedAttend.getAttendOut());
                date.setAttendType(matchedAttend.getAttendType());

                // 근무 시간 계산
                if (matchedAttend.getAttendIn() != null && matchedAttend.getAttendOut() != null) {
                    Duration workDuration = Duration.between(matchedAttend.getAttendIn(), matchedAttend.getAttendOut());
                    date.setWorkTime(workDuration);
                }
            }
        }

        // 결근 처리
        LocalDate today = LocalDate.now();
        for (Attend attend : allDate) {
            AttendType attendType = Optional.ofNullable(attend.getAttendType()).orElse(ABSENT);
            if (attend.getAttendDate().isBefore(today)
                    && attend.getAttendOut() == null
                    && !attendType.equals(HOLIDAY)
                    && !attendType.isSubTypeOf(VACATION)) {
                attend.setAttendType(ABSENT);
            }
        }
    }

    private List<Attend> insertAllDays(LocalDate startDate, LocalDate endDate) {
        List<Attend> list = new ArrayList<>();

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            list.add(new Attend(date));
            date = date.plusDays(1);
        }
        return list;
    }

    private void updateHoliday(List<Attend> allDate, List<Holiday> holidayList) {
        if (!holidayList.isEmpty()) {
            int holidayIdx = 0;
            int dateIdx = 0;
            while (holidayIdx < holidayList.size()) {
                Holiday holiday = holidayList.get(holidayIdx);
                Attend attend = allDate.get(dateIdx);
                if (holiday.getHolidayDate().equals(attend.getAttendDate())) {
                    attend.setAttendType(AttendType.HOLIDAY);
                    holidayIdx++;
                }
                dateIdx++;
            }
        }
    }

    //조퇴 또는 지각 판별, 9 to 6 기업으로 가정
    public boolean isLateOrLeaveEarly(Attend attend) {
        LocalDateTime attendIn = attend.getAttendIn();
        LocalDateTime attendOut = attend.getAttendOut();

        if (attendIn == null || attendOut == null) {
            return false;
        }

        LocalTime inTime = attendIn.toLocalTime();
        LocalTime outTime = attendOut.toLocalTime();
        return inTime.isAfter(LocalTime.of(9, 0)) || outTime.isBefore(LocalTime.of(18, 0));
    }

    public ActionForward getVacationDetail(HttpServletRequest req, String empId) {
        int year = Integer.parseInt(req.getParameter("year"));
        Emp targetEmp = empDao.getEmpById(empId);
        //총 연차 부여일
        req.setAttribute("allVacCount", targetEmp.getVacationCount());
        req.setAttribute("emp", targetEmp);
        int usedAnnualVacation = attendDao.getUsedAnnualVacationCount(targetEmp.getEmpId(), year);
        List<DocVacation> vacList = attendDao.getUsedVacationList(targetEmp.getEmpId(), year);

        req.setAttribute("vacList", vacList);
        req.setAttribute("givenVacCount", targetEmp.getVacationCount());
        req.setAttribute("usedAnnualVacation", usedAnnualVacation);
        return new ActionForward(false, "/WEB-INF/views/attend/vacationDetail.jsp");
    }

    public static void checkAttendRole(Emp emp, HttpServletRequest req) {
        boolean hrDept = EmpUtil.isHrDept(emp);
        boolean manager = EmpUtil.isManager(emp);
        req.setAttribute("hrDept", hrDept);
        req.setAttribute("manager", manager);
    }
}
