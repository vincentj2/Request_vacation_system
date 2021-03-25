package com.croquiscom.vincentj2.controller;

import com.croquiscom.vincentj2.domain.Account;
import com.croquiscom.vincentj2.account.CurrentUser;
import com.croquiscom.vincentj2.domain.VacationStatus;
import com.croquiscom.vincentj2.domain.Vacation;
import com.croquiscom.vincentj2.service.AccountService;
import com.croquiscom.vincentj2.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;
    private final AccountService accountService;


    /*
    휴가 신청
     */
    @GetMapping("/request-vacation")
    public String createForm(Model model){

        model.addAttribute("vacationForm",new VacationForm());

        return "request-vacation";
    }

    @PostMapping("/request-vacation")
    public String requestVacation(@CurrentUser Account account, VacationForm form, RedirectAttributes attributes){

        Vacation vacation = new Vacation();
        vacation.setAccount(account);
        vacation.setVacationType(form.getVacationType());
        vacation.setStartDate(form.getStartDate());
        vacation.setEndDate(form.getEndDate());
        vacation.setPeriod(form.getPeriod());
        vacation.setComment(form.getComment());
        vacation.setStatus(VacationStatus.REQUEST);
        try {
            vacationService.saveVacation(vacation, account);
            account.setAnnualCnt(account.getAnnualCnt()- form.getPeriod());
            accountService.saveAccount(account);
        }catch (IllegalStateException e){
            attributes.addFlashAttribute("message","사용 연차 일수가 부족합니다");
            return "redirect:/request-vacation";
        }
        return "redirect:/";
    }


    /*
    휴가신청내역
     */
    @GetMapping("/detail-vacation")
    public String detailVacation(@CurrentUser Account account, Model model){
        List<Vacation> vacations = vacationService.findVacations(account);
        model.addAttribute("vacations", vacations);
        return "detail-vacation";
    }
    /*
    휴가 취소
     */
    @PostMapping(value = "/detail-vacation/{vacationId}/cancel")
    public String cancelVacation(@CurrentUser Account account, @PathVariable("vacationId") Long vacationId, RedirectAttributes attributes){
        Vacation vacation = vacationService.findById(vacationId);
        try {
            vacationService.cancelVacation(vacationId);
            account.setAnnualCnt(account.getAnnualCnt()+vacation.getPeriod());
            accountService.saveAccount(account);
        }catch (IllegalStateException e) {
            attributes.addFlashAttribute("message","이미 진행중인 휴가 입니다");
            return "redirect:/detail-vacation";
        }
        return "redirect:/detail-vacation";
    }
}


