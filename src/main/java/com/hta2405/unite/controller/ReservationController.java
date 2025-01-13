package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.ReservationDTO;
import com.hta2405.unite.service.ReservationService;
import com.hta2405.unite.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        System.out.println("\n ***** reservationDTO ===== " + reservationDTO.getReservationStart());
        int reservation = reservationService.resourceReservation(reservationDTO);
        return reservation;
    }

}
