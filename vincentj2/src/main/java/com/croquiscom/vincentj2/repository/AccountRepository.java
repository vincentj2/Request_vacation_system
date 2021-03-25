package com.croquiscom.vincentj2.repository;

import com.croquiscom.vincentj2.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUsername(String username);
    Account findByUsername(String username);
}
