package com.croquiscom.vincentj2.controller;

import com.croquiscom.vincentj2.domain.Account;
import com.croquiscom.vincentj2.domain.Vacation;
import com.croquiscom.vincentj2.domain.VacationStatus;
import com.croquiscom.vincentj2.repository.AccountRepository;
import com.croquiscom.vincentj2.repository.AccountRepositoryImpl;
import com.croquiscom.vincentj2.repository.VacationRepository;
import com.croquiscom.vincentj2.service.AccountService;
import com.croquiscom.vincentj2.service.VacationService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class VacationControllerTest {

    @Autowired VacationRepository vacationRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountRepositoryImpl accountRepositoryImpl;
    @Autowired VacationService vacationService;
    @Autowired AccountService accountService;
    @Autowired MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        SignUpForm account = new SignUpForm();
        account.setUsername("vincentj");
        account.setPassword("111");
        account.setName("정상원");
        account.setTeam("개발팀");
        accountService.signUp(account);
    }


    @Test
    @WithMockUser
    @DisplayName("휴가 신청 화면 접속 테스트")
    void Request_Vacation_CreateForm() throws Exception{
        mockMvc.perform(get("/request-vacation"))
                .andExpect(status().isOk())
                .andExpect(view().name("request-vacation"));
    }

    @Test
    @WithMockUser
    @DisplayName("휴가 신청 성공 테스트")
    void Request_Vacation_Success() {
        LocalDate startdate = LocalDate.of(2022,3,10);
        LocalDate enddate = LocalDate.of(2022,3,12);
        Account account = accountRepository.findByUsername("vincentj");
        Vacation vacation = new Vacation();
        vacation.setAccount(account);
        vacation.setVacationType("연차");
        vacation.setStartDate(startdate);
        vacation.setEndDate(enddate);
        vacation.setPeriod(3);
        vacation.setComment("test");
        vacation.setStatus(VacationStatus.REQUEST);

        vacationService.saveVacation(vacation,account);
        account.setAnnualCnt(account.getAnnualCnt()- vacation.getPeriod());
        accountService.saveAccount(account);

        List<Vacation> vacations = vacationService.findVacations(account);

        Assert.assertEquals("사용자 휴가일수는 휴가 신청일수만큼 줄어들어야 한다.",12.0, account.getAnnualCnt(),0.0);
        Assert.assertEquals("휴가 신청시 휴가종류는 1건만 신청되어야한다.",1, vacations.size());
        Assert.assertEquals("휴가신청 요청자에게 저장되어야 한다",account, vacation.getAccount());
    }


    @Test
    @WithMockUser
    @DisplayName("휴가 취소 성공 테스트")
    void Request_Vacation_Cancel_Success(){
        LocalDate startdate = LocalDate.of(2022,3,9);
        LocalDate enddate = LocalDate.of(2022,3,12);
        Account account = accountRepository.findByUsername("vincentj");
        Vacation vacation = new Vacation();
        vacation.setAccount(account);
        vacation.setVacationType("연차");
        vacation.setStartDate(startdate);
        vacation.setEndDate(enddate);
        vacation.setPeriod(3);
        vacation.setComment("test");
        vacation.setStatus(VacationStatus.REQUEST);
        //휴가 신청
        vacationService.saveVacation(vacation,account);
        account.setAnnualCnt(account.getAnnualCnt()- vacation.getPeriod());
        accountService.saveAccount(account);
        List<Vacation> vacations = vacationService.findVacations(account);

        Assert.assertEquals("사용자 휴가일수는 휴가 신청일수만큼 줄어들어야 한다.",12.0, account.getAnnualCnt(),0.0);
        Assert.assertEquals("휴가 신청시 휴가종류는 1건만 신청되어야한다.",1, vacations.size());
        Assert.assertEquals("휴가신청 요청자에게 저장되어야 한다",account, vacation.getAccount());
        //휴가 취소
        Long vacation_id = vacation.getId();
        vacationService.cancelVacation(vacation_id);
        account.setAnnualCnt(account.getAnnualCnt()+vacation.getPeriod());
        accountService.saveAccount(account);

        Assert.assertEquals("사용자 휴가일수는 취소 휴가일수만큼 증가해야 한다.",15.0, account.getAnnualCnt(),0.0);
        Assert.assertEquals("휴가 취소시 휴가종류는 1건만 취소되어야한다.",1, vacations.size());
        Assert.assertEquals("휴가취소 요청자에게 취소되어야 한다",account, vacation.getAccount());
        Assert.assertEquals("휴가취소 시 휴가상태가 CANCEL로 변경되어야한다",VacationStatus.CANCEL, vacation.getStatus());
    }
}