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
        return "reservation/reservationCalender";
    }

    // 자원목록
    @ResponseBody
    @GetMapping("/getResourceList")
    public List<Resource> getResourceList() {
        return resourceService.getResourceList();
    }

    // 자원목록 셀렉트 이벤트
    @ResponseBody
    @GetMapping("/resourceSelectChange")
    public List<ReservationDTO> resourceSelectChange(@RequestParam("resourceType") String resourceType) {
        List<ReservationDTO> reservationDTOList = reservationService.resourceSelectDTOList(resourceType);
        return reservationDTOList;
    }

    // 자원목록 선택 시 해당 자원에 대한 예약 목록 조회
    @ResponseBody
    @GetMapping("/resourceSelectReservation")
    public List<ReservationDTO> resourceSelectReservation(@RequestParam("resourceType") String resourceType) {
        List<Resource> resources = reservationService.resourceSelectChange(resourceType);
        List<ReservationDTO> reservations = new ArrayList<>();

        if (resourceType != null && !resources.isEmpty()) {
            for (Resource resource : resources) {
                // 예약 DTO 생성
                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setResourceId(resource.getResourceId());
                reservationDTO.setResourceType(resource.getResourceType());
                reservationDTO.setResourceName(resource.getResourceName());
                reservationDTO.setResourceUsable(resource.isResourceUsable());

                // 자원에 해당하는 예약 목록을 가져와서 DTO에 추가
                List<ReservationDTO> resourceReservations = reservationService.getReservationsByResourceId(resource.getResourceId());

                for (ReservationDTO reservation : resourceReservations) {
                    ReservationDTO reservationDetails = new ReservationDTO();
                    reservationDTO.setReservationId(reservation.getReservationId());
                    reservationDTO.setResourceId(reservation.getResourceId());
                    reservationDTO.setResourceType(reservation.getResourceType());
                    reservationDTO.setResourceName(reservation.getResourceName());
                    reservationDTO.setResourceUsable(reservation.isResourceUsable());
                    reservationDTO.setReservationStart(reservation.getReservationStart());
                    reservationDTO.setReservationEnd(reservation.getReservationEnd());
                    reservationDTO.setReservationInfo(reservation.getReservationInfo());
                    reservationDTO.setReservationAllDay(reservation.getReservationAllDay());

                    reservationDTO.addReservation(reservationDetails);
                }

                reservations.add(reservationDTO);
            }

            System.out.println(reservations);
        }

        return reservations;
    }

    // 자원 예약
    @ResponseBody
    @PostMapping("/resourceReservation")
    public int resourceReservation(@RequestBody ReservationDTO reservationDTO) {
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
