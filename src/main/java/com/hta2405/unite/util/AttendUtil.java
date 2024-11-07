package com.hta2405.unite.util;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.EmpInfoDao;
import com.hta2405.unite.dao.HolidayDao;
import com.hta2405.unite.dto.Attend;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.EmpInfo;
import com.hta2405.unite.dto.Holiday;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendUtil {
    public ActionForward getAttendDetail(HttpServletRequest req, Emp targetEmp) {
        int year = Integer.parseInt(req.getParameter("year"));
        int month = Integer.parseInt(req.getParameter("month"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        //한달 날짜 모두 리스트화(Attend 객체에는 날짜정보만)
        List<Attend> allDate = insertAllDays(startDate, endDate);

        List<Holiday> holidayList = new HolidayDao().getHoliday(startDate, endDate);
        System.out.println(holidayList);
        //휴일 업데이트(Attend객체 내 attendType에 휴일 정보 추가)
        updateHoliday(allDate, holidayList);

        //한달간의 해당 직원 근태목록 불러와 업데이트
        updateEmpAttend(startDate, endDate, targetEmp, allDate);

        int allWorkDate = endDate.getDayOfMonth() - holidayList.size();
        int myWorkDate = 0;
        int vacation = 0;
        int absent = 0;

        // attendType ={일반, 출장, 외근, 휴가, 결근, 휴일}
        for (Attend attend : allDate) {
            if (attend.getAttendType() == null || attend.getAttendType().equals("휴일")) {
                continue;
            }
            if (attend.getAttendType().equals("휴가")) { //휴일이 아니면
                vacation++;
                continue;
            }
            if (attend.getAttendType().equals("결근")) {
                absent++;
                continue;
            }
            myWorkDate++;
        }

        //TODO 결근일 추가 로직. 전 직원 한번에 될 수 있도록 모아서 처리해야 할듯.

        req.setAttribute("attendList", allDate);
        req.setAttribute("allWorkDate", allWorkDate); //당월 총 근무일
        req.setAttribute("myWorkDate", myWorkDate); //근무일
        req.setAttribute("vacation", vacation); //휴가일
        req.setAttribute("absent", absent); //결근일
        return new ActionForward(false, "/WEB-INF/views/attend/attendDetail.jsp");
    }

    private void updateEmpAttend(LocalDate startDate, LocalDate endDate, Emp emp, List<Attend> allDate) {
        ArrayList<Attend> attendList = new AttendDao().getAttendByEmpId(emp, startDate, endDate);
        System.out.println(attendList);
        int attendIdx = 0;
        int dateIdx = 0;
        while (attendIdx < attendList.size()) {
            Attend attend = attendList.get(attendIdx);
            Attend date = allDate.get(dateIdx);
            if (date.getAttendDate()
                    .equals(attend.getAttendDate())) {
                date.setEmpId(attend.getEmpId());
                date.setAttendIn(attend.getAttendIn());
                date.setAttendOut(attend.getAttendOut());
                date.setAttendType(attend.getAttendType());

                System.out.println(date);
                // 근무 시간 계산 및 설정
                if (attend.getAttendIn() != null && attend.getAttendOut() != null) {
                    Duration workDuration = Duration.between(attend.getAttendIn(), attend.getAttendOut());
                    date.setWorkTime(workDuration);
                }
                attendIdx++;
            }
            dateIdx++;
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
                    attend.setAttendType("휴일");
                    holidayIdx++;
                }
                dateIdx++;
            }
        }
    }

    public ActionForward getVacationDetail(HttpServletRequest req, Emp targetEmp) {
        int year = Integer.parseInt(req.getParameter("year"));
        //총 연차 부여일
        req.setAttribute("allVacCount", targetEmp.getVacationCount());

        //모든 특정 년도 휴가 목록 가져옴
        List<Attend> vacList = new AttendDao().getYearlyVacationByEmpId(targetEmp.getEmpId(), year);

        //전체 휴가 중 '연차'사용 갯수만 카운트
        int privateVacCount = 0;
        for (Attend attend : vacList) {
            if (attend.getAttendType().equals("연차")) {
                privateVacCount++;
            }
        }

        req.setAttribute("vacList", vacList);
        System.out.println("vacList = " + vacList);
        req.setAttribute("givenVacCount", targetEmp.getVacationCount());
        System.out.println(targetEmp.getVacationCount());
        req.setAttribute("privateVacCount", privateVacCount);
        return new ActionForward(false, "/WEB-INF/views/attend/vacationDetail.jsp");
    }
}
