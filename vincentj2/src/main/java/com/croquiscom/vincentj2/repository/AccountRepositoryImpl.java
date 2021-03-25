package com.croquiscom.vincentj2.repository;

import com.croquiscom.vincentj2.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl {

    private final EntityManager em;

    //계정 수정
    public void save(Account account){
        if(account.getId() == null){
            em.persist(account);
        }else{
            em.merge(account);
        }
    }
}
