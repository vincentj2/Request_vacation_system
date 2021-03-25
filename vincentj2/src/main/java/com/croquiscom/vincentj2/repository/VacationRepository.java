package com.croquiscom.vincentj2.repository;


import com.croquiscom.vincentj2.domain.Account;
import com.croquiscom.vincentj2.domain.Vacation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacationRepository {

    private final EntityManager em;

    public Vacation findOne(Long vacationId) {
        return em.find(Vacation.class, vacationId);
    }

    //휴가 저장
    public void save(Vacation vacation){
        if(vacation.getId() == null){
            em.persist(vacation);
        }else{
            em.merge(vacation);
        }
    }

    public Vacation findById(Long id){
        return em.find(Vacation.class,id);
    }

    public List<Vacation> findAll(Account account) {
        return em.createQuery("select v from Vacation v where v.account = :account", Vacation.class)
                .setParameter("account", account)
                .getResultList();
    }
}
