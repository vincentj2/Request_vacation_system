package com.croquiscom.vincentj2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final EntityManager em;

    @Scheduled(cron = "0 0 0 1 1 ?")
    @Transactional
    public void VacationReset(){
        em.createQuery("update Account set annualCnt = 15").executeUpdate();
    }
}
