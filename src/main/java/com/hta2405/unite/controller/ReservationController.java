package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.ReservationDTO;
import com.hta2405.unite.service.ReservationService;
import com.hta2405.unite.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;
    private ResourceService resourceService;

    @Autowired
    public ReservationController(ReservationService reservationService, ResourceService resourceService) {
        this.reservationService = reservationService;
        this.resourceService = resourceService;
    }

    @GetMapping("/reservationCalender")
    public String reservationCalender() {
        return "/reservation/reservationCalender";
    }

    @ResponseBody
    @GetMapping("/getResourceList")
    public List<Resource> getResourceList() {
        return resourceService.getResourceList();
    }

    @ResponseBody
    @GetMapping("/resourceSelectChange")
    public List<ReservationDTO> resourceSelectChange(@RequestParam("resourceType") String resourceType) {

        List<Resource> resources = reservationService.resourceSelectChange(resourceType);
        List<ReservationDTO> reservationDTOList = new ArrayList<>();

        // 리소스마다 DTO 생성하여 리스트에 추가
        for (Resource resource : resources) {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setResourceId(resource.getResourceId());
            reservationDTO.setResourceType(resource.getResourceType());
            reservationDTO.setResourceName(resource.getResourceName());
            reservationDTO.setResourceUsable(resource.isResourceUsable());

            reservationDTOList.add(reservationDTO);
        }
        return reservationDTOList;
    }

    @ResponseBody
    @PostMapping("/resourceReservation")
    public int resourceReservation(@RequestBody ReservationDTO reservationDTO) {
        // allDay가 1일 경우: 예약 시작시간과 종료시간을 00:00부터 23:59로 설정
        if (reservationDTO.getReservationAllDay() == 1) {
            // 예약 시작 시간을 00:00으로 설정
            reservationDTO.setReservationStart(reservationDTO.getReservationStart().withHour(0).withMinute(0).withSecond(0).withNano(0));
            // 예약 종료 시간을 23:59로 설정
            reservationDTO.setReservationEnd(reservationDTO.getReservationEnd().withHour(23).withMinute(59).withSecond(59).withNano(0));
        }

        // 중복 예약 체크
        int overlapCount = reservationService.checkReservationOverlap(reservationDTO);
        if (overlapCount > 0) {
            System.out.println("이미 예약된 자원입니다.");
            return 0; // 중복 예약이 있는 경우
        }

        // 중복이 없다면 예약 추가
        return reservationService.resourceReservation(reservationDTO);
    }


    @ResponseBody
    @GetMapping("/getReservationList")
    public List<ReservationDTO> getReservationByResourceIdAndName(@RequestParam(required = false) Long resourceId,
                                                                  @RequestParam(required = false) String resourceName,
                                                                  @AuthenticationPrincipal UserDetails user
    ) {
        List<ReservationDTO> reservations;
        String empId = user.getUsername();


        if (resourceId != null && resourceName != null) {
            // 선택된 자원 예약 목록 불러오기
            reservations = reservationService.getReservationByResourceIdAndName(resourceId, resourceName, empId);
        } else {
            // 자원 ID가 없으면 모든 예약 목록 불러오기
            reservations = reservationService.getAllReservation();
        }

        // 예약 목록을 순회하면서 로그인한 사용자의 예약 여부를 설정
        for (ReservationDTO reservation : reservations) {
            // 예약을 추가한 사용자가 로그인한 사용자와 동일하면 isMyReservation을 true로 설정
            if (reservation.getEmpId().equals(empId)) {
                reservation.setIsMyReservation(true);  // 본인 예약
            } else {
                reservation.setIsMyReservation(false);
            }
        }

        return reservations;
    }

    @ResponseBody
    @GetMapping("/getReservationModal")
    public ReservationDTO getReservationModal(@RequestParam Long reservationId) {
        // 서비스 메서드 호출
        ReservationDTO reservation = reservationService.getReservationModal(reservationId);

        // 반환된 reservationDTO 객체 반환
        return reservation;
    }

    @ResponseBody
    @PostMapping("/cancelReservation")
    public int cancelReservation(@RequestParam Long reservationId, @AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();
        return reservationService.cancelReservation(reservationId, empId);
    }

    @GetMapping("/myReservationList")
    public Model getMyReservationList(@AuthenticationPrincipal UserDetails user, Model model) {
        String empId = user.getUsername();

        // 예약 목록 가져오기
        List<ReservationDTO> reservationList = reservationService.getMyReservationList(empId);
        model.addAttribute("reservationList", reservationList);
        return model;
    }
}
