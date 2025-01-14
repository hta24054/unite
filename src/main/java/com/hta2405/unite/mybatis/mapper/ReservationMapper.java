package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.dto.ReservationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    public List<Resource> resourceSelectChange(String resourceType);

    public int resourceReservation(ReservationDTO reservationDTO);

    public int checkReservationOverlap(ReservationDTO reservationDTO);

    public List<ReservationDTO> getAllReservation();

    public List<ReservationDTO> getReservationByResourceIdAndName(Long resourceId, String resourceName, String name);

    public ReservationDTO getReservationModal(Long reservationId);
}
