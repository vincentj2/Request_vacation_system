package com.croquiscom.vincentj2.service;

import com.croquiscom.vincentj2.controller.SignUpForm;
import com.croquiscom.vincentj2.account.UserAccount;
import com.croquiscom.vincentj2.domain.Account;
import com.croquiscom.vincentj2.repository.AccountRepository;
import com.croquiscom.vincentj2.repository.AccountRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountRepositoryImpl accountRepositoryImpl;


    public void signUp(SignUpForm signUpForm) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        signUpForm.setPassword(encoder.encode(signUpForm.getPassword()));

        Account account = Account.builder().username(signUpForm.getUsername())
                .password(signUpForm.getPassword())
                .name(signUpForm.getName())
                .team(signUpForm.getTeam())
                .annualCnt(15)
                .build();
        accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException(username);
        }

        return new UserAccount(account);
    }

    @Transactional
    public void saveAccount(Account account){
        accountRepositoryImpl.save(account);
    }

    public List<Account> findAccounts() { return accountRepository.findAll();}

}
