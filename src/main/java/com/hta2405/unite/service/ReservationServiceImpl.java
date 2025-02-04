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
    public List<ReservationDTO> getReservationsByResourceType(String resourceType) {
        List<Resource> resources = reservationDAO.resourceSelectChange(resourceType);
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
                List<ReservationDTO> resourceReservations = getReservationsByResourceId(resource.getResourceId());

                for (ReservationDTO reservation : resourceReservations) {
                    ReservationDTO reservationDetails = new ReservationDTO();
                    reservationDetails.setReservationId(reservation.getReservationId());
                    reservationDetails.setResourceId(reservation.getResourceId());
                    reservationDetails.setResourceType(reservation.getResourceType());
                    reservationDetails.setResourceName(reservation.getResourceName());
                    reservationDetails.setResourceUsable(reservation.isResourceUsable());
                    reservationDetails.setReservationStart(reservation.getReservationStart());
                    reservationDetails.setReservationEnd(reservation.getReservationEnd());
                    reservationDetails.setReservationInfo(reservation.getReservationInfo());
                    reservationDetails.setReservationAllDay(reservation.getReservationAllDay());

                    reservationDTO.addReservation(reservationDetails);
                }

                reservations.add(reservationDTO);
            }
        }

        return reservations;
    }

    @Override
    public List<ReservationDTO> getAllReservation() {
        return reservationDAO.getAllReservation();
    }

    // 자원 ID와 이름에 해당하는 예약 목록 가져오기
    @Override
    public List<ReservationDTO> getReservationByResourceIdAndName(Long resourceId, String resourceName, String name) {
        return reservationDAO.getReservationByResourceIdAndName(resourceId, resourceName, name);
    }

    @Override
    public List<ReservationDTO> getReservationList(Long resourceId, String resourceName, String empId) {
        List<ReservationDTO> reservations;

        if (resourceId != null && resourceName != null) {
            // 선택된 자원 예약 목록 불러오기
            reservations = getReservationByResourceIdAndName(resourceId, resourceName, empId);
        } else {
            // 자원 ID가 없으면 모든 예약 목록 불러오기
            reservations = getAllReservation();
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
