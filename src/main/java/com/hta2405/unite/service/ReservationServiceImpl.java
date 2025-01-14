package com.hta2405.unite.service;

import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.ReservationDTO;
import com.hta2405.unite.mybatis.mapper.ReservationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationMapper reservationDAO;

    public ReservationServiceImpl(ReservationMapper reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @Override
    public List<Resource> resourceSelectChange(String resourceType) {
        return reservationDAO.resourceSelectChange(resourceType);
    }

    @Override
    public int resourceReservation(ReservationDTO reservationDTO) {
        return reservationDAO.resourceReservation(reservationDTO);
    }

    @Override
    public List<ReservationDTO> getReservationByResourceIdAndName(Long resourceId, String resourceName, String name) {
        return reservationDAO.getReservationByResourceIdAndName(resourceId, resourceName, name);
    }

    @Override
    public List<ReservationDTO> getAllReservation() {
        return reservationDAO.getAllReservation();
    }

    @Override
    public int checkReservationOverlap(ReservationDTO reservationDTO) {
        return reservationDAO.checkReservationOverlap(reservationDTO);
    }

    @Override
    public ReservationDTO getReservationModal(Long reservationId) {
        return reservationDAO.getReservationModal(reservationId);
    }

    @Override
    public int cancelReservation(Long reservationId, String empId) {
        return reservationDAO.cancelReservation(reservationId, empId);
    }


}
