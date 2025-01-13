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

        // 자원 예약 중복 체크
        int overlapCount = reservationDAO.checkReservationOverlap(reservationDTO);
        System.out.println("자원 예약 중복 체크" + overlapCount);

        if (overlapCount > 0) {
            System.out.println("이미 예약된 자원입니다.");
            return 0;
        }

        // 자원 예약 중복 아니면 예약
        System.out.println("자원 예약 중복 아니면 예약" + reservationDAO.resourceReservation(reservationDTO));
        return reservationDAO.resourceReservation(reservationDTO);
    }
}
