package com.hta2405.unite;

import com.hta2405.unite.mybatis.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ReservationTest {
    private static Logger logger = LoggerFactory.getLogger(ReservationTest.class);

    @Autowired
    private ReservationMapper dao;

    @Test
    public void cancelReservation() {
        String reservationId = "190";
        String userId = "241001";
        int result = dao.cancelReservation(Long.valueOf(reservationId), userId);
        if (result == 1) {
            logger.info("=== " + Long.valueOf(reservationId), userId + " 삭제 완료 ===");
        }

        assertTrue(result == 1);
    }


}
