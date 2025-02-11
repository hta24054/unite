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
        return reservationService.resourceSelectDTOList(resourceType);
    }

    // 자원목록 선택 시 해당 자원에 대한 예약 목록 조회
    @ResponseBody
    @GetMapping("/resourceSelectReservation")
    public List<ReservationDTO> resourceSelectReservation(@RequestParam("resourceType") String resourceType) {
        return reservationService.getReservationsByResourceType(resourceType);
    }

    // 자원 예약
    @ResponseBody
    @PostMapping("/resourceReservation")
    public int resourceReservation(@RequestBody ReservationDTO reservationDTO) {
        return reservationService.resourceReservation(reservationDTO);
    }

    // 자원 예약 목록 불러오기
    @ResponseBody
    @GetMapping("/getReservationList")
    public List<ReservationDTO> getReservationByResourceIdAndName(@RequestParam(required = false) Long resourceId,
                                                                  @RequestParam(required = false) String resourceName,
                                                                  @AuthenticationPrincipal UserDetails user
    ) {
        String empId = user.getUsername();
        return reservationService.getReservationList(resourceId, resourceName, empId);
    }

    @ResponseBody
    @GetMapping("/getReservationModal")
    public ReservationDTO getReservationModal(@RequestParam Long reservationId) {
        return reservationService.getReservationModal(reservationId);
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

        List<ReservationDTO> reservationList = reservationService.getMyReservationList(empId);
        model.addAttribute("reservationList", reservationList);
        return model;
    }
}
