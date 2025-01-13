package com.hta2405.unite.scheduler;

import com.hta2405.unite.mybatis.mapper.EmpMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VacationCountUpdateScheduler {
    private final EmpMapper empMapper;

    //발생 연차 갯수 초기화하는 메서드, 매월 1일 실시
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateVacationCount() {
        empMapper.updateVacationCount();
    }
}
