package com.hta2405.unite.service;

import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {
    public List<Resource> resourceSelectChange(String resourceType);

    public int resourceReservation(ReservationDTO reservationDTO);

    public List<ReservationDTO> getReservationByResourceIdAndName(Long resourceId, String resourceName, String name);

    public List<ReservationDTO> getAllReservation();

    public int checkReservationOverlap(ReservationDTO reservationDTO);

    public ReservationDTO getReservationModal(Long reservationId);

    public int cancelReservation(Long reservationId, String empId);

    public List<ReservationDTO> getMyReservationList(String empId);
}
