package com.croquiscom.vincentj2.controller;

import com.croquiscom.vincentj2.account.*;
import com.croquiscom.vincentj2.domain.Account;
import com.croquiscom.vincentj2.repository.AccountRepository;
import com.croquiscom.vincentj2.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    /*
    회원가입
     */
    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm",new SignUpForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors, RedirectAttributes attributes){
        if(errors.hasErrors()){
            return "sign-up";
        }
        attributes.addFlashAttribute("message","회원가입 성공!");
        accountService.signUp(signUpForm);
        return "redirect:/";
    }

    /*
    로그인
     */
    @GetMapping("/log-in")
    public String logInDisp(Model model){
        model.addAttribute(new LoginForm());
        return "log-in";
    }

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if(account != null){
            model.addAttribute(account);
        }
        return "index";
    }
}
