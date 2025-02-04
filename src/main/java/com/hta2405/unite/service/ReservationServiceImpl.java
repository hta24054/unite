package com.hta2405.unite.service;

import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.ReservationDTO;
import com.hta2405.unite.mybatis.mapper.ReservationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<ReservationDTO> resourceSelectDTOList(String resourceType) {
        List<Resource> resources = reservationDAO.resourceSelectChange(resourceType);

        List<ReservationDTO> reservationDTOList = new ArrayList<>();
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

    @Override
    public int resourceReservation(ReservationDTO reservationDTO) {
        // allDay가 1일 경우: 예약 시작시간과 종료시간을 00:00부터 23:59로 설정
        if (reservationDTO.getReservationAllDay() == 1) {
            // 예약 시작 시간을 00:00으로 설정
            reservationDTO.setReservationStart(reservationDTO.getReservationStart().withHour(0).withMinute(0).withSecond(0).withNano(0));
            // 예약 종료 시간을 23:59로 설정
            reservationDTO.setReservationEnd(reservationDTO.getReservationEnd().withHour(23).withMinute(59).withSecond(59).withNano(0));
        }

        // 중복 예약 체크
        int overlapCount = checkReservationOverlap(reservationDTO);
        if (overlapCount > 0) {
            System.out.println("이미 예약된 자원입니다.");
            return 0; // 중복 예약이 있는 경우
        }

        // 중복이 없다면 예약 추가
        return reservationDAO.resourceReservation(reservationDTO);
    }

    @Override
    public int checkReservationOverlap(ReservationDTO reservationDTO) {
        // 새 예약 시작 시간과 종료 시간
        LocalDateTime start = reservationDTO.getReservationStart();
        LocalDateTime end = reservationDTO.getReservationEnd();

        // 해당 자원의 기존 예약 목록을 DB에서 조회
        List<ReservationDTO> existingReservations = reservationDAO.getReservationsByResourceId(reservationDTO.getResourceId());

        // 기존 예약들과 새 예약의 시간 비교
        for (ReservationDTO existingReservation : existingReservations) {
            LocalDateTime existingStart = existingReservation.getReservationStart();
            LocalDateTime existingEnd = existingReservation.getReservationEnd();

            // 중복 예약 체크: 새 예약의 시간이 기존 예약과 겹치는지 확인
            if ((start.isBefore(existingEnd) || start.isEqual(existingEnd)) &&
                    (end.isAfter(existingStart) || end.isEqual(existingStart))) {
                return 1; // 중복 예약 있는 경우
            }
        }

        return 0;
    }

    @Override
    public List<ReservationDTO> getAllReservation() {
        return reservationDAO.getAllReservation();
    }

    @Override
    public List<ReservationDTO> getReservationByResourceIdAndName(Long resourceId, String resourceName, String name) {
        return reservationDAO.getReservationByResourceIdAndName(resourceId, resourceName, name);
    }

    @Override
    public ReservationDTO getReservationModal(Long reservationId) {
        return reservationDAO.getReservationModal(reservationId);
    }

    @Override
    public int cancelReservation(Long reservationId, String empId) {
        return reservationDAO.cancelReservation(reservationId, empId);
    }

    @Override
    public List<ReservationDTO> getMyReservationList(String empId) {
        return reservationDAO.getMyReservationList(empId);
    }

    @Override
    public List<ReservationDTO> getReservationsByResourceId(Long resourceId) {
        return reservationDAO.getReservationsByResourceId(resourceId);
    }
}
