package com.croquiscom.vincentj2.account;

import com.croquiscom.vincentj2.controller.SignUpForm;
import com.croquiscom.vincentj2.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)o;
        if(accountRepository.existsByUsername(signUpForm.getUsername())){
            errors.rejectValue("username","invalid username","이미 사용중인 아이디입니다.");
        }
    }
}
