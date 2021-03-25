package com.croquiscom.vincentj2.service;


import com.croquiscom.vincentj2.domain.Account;
import com.croquiscom.vincentj2.domain.Vacation;
import com.croquiscom.vincentj2.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacationService {

    private final VacationRepository vacationRepository;

    @Transactional
    public void saveVacation(Vacation vacation, Account account){
        if(account.getAnnualCnt() - vacation.getPeriod() <0){
            throw new IllegalStateException("사용가능 연차 일수가 부족합니다");
        }else {
            vacationRepository.save(vacation);
        }
    }
    public Vacation findById(Long vacationId){
        return vacationRepository.findById(vacationId);
    }

    public List<Vacation> findVacations(Account account) {
        return vacationRepository.findAll(account);
    }

    @Transactional
    public void cancelVacation(Long vacationId) {

        Vacation vacation = vacationRepository.findOne(vacationId);

        vacation.cancel();
    }
}
