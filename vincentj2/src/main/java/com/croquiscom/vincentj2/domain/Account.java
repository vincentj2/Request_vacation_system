package com.croquiscom.vincentj2.domain;


import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
@EqualsAndHashCode(of = "id")
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String username;

    private String password;

    private String name;

    private String team;

    private double annualCnt;

    @OneToMany(mappedBy = "account")
    @Nullable
    private List<Vacation> vacations = new ArrayList<>();

    public void addAnnualCnt(double period) {
        this.annualCnt += period;

    }
}
