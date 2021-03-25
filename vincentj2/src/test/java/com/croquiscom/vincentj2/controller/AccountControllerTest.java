package com.croquiscom.vincentj2.controller;

import com.croquiscom.vincentj2.domain.Account;
import com.croquiscom.vincentj2.repository.AccountRepository;
import com.croquiscom.vincentj2.service.AccountService;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AccountRepository accountRepository;
    @Autowired private AccountService accountService;

    @Test
    @DisplayName("회원가입 화면 접속 테스트")
    void SignUpForm() throws  Exception{
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void SignUp() throws Exception{
        mockMvc.perform(post("/sign-up")
                .param("username","vincentj")
                .param("name", "정상원")
                .param("password", "1111")
                .param("team", "개발팀"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        assertTrue(accountRepository.existsByUsername("vincentj"));
        Account account = accountRepository.findByUsername("vincentj");
        Assert.assertEquals("회원가입시 연차 일수는 15일이다",15.0,account.getAnnualCnt(),0.0);
    }


    @WithMockUser
    @Test
    @DisplayName("로그인 성공 테스트")
    void loginCheck() throws Exception{
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername("vincentj");
        signUpForm.setPassword("11111");
        signUpForm.setName("정상원");
        signUpForm.setTeam("개발팀");
        accountService.signUp(signUpForm);

        mockMvc.perform(post("/log-in")
                .param("username", "vincentj")
                .param("password", "11111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("vincentj"));
    }

    @WithMockUser
    @Test
    @DisplayName("로그인 실패 테스트")
    void loginFail() throws Exception{
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername("vincentj");
        signUpForm.setPassword("11111");
        signUpForm.setName("정상원");
        signUpForm.setTeam("개발팀");
        accountService.signUp(signUpForm);

        mockMvc.perform(post("/log-in")
                .param("username", "vincentj2")
                .param("password", "11111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/log-in?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser
    @Test
    @DisplayName("로그아웃 테스트")
    void logout() throws Exception{
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername("vincentj");
        signUpForm.setPassword("11111");
        signUpForm.setName("정상원");
        signUpForm.setTeam("개발팀");
        accountService.signUp(signUpForm);

        mockMvc.perform(post("/log-out"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}