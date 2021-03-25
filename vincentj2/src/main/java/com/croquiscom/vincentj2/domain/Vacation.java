package com.croquiscom.vincentj2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter @Setter
public class Vacation {
    @Id @GeneratedValue
    @Column(name = "vacation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDate startDate;

    private LocalDate endDate;

    private String vacationType;

    private VacationStatus status;

    private String comment;

    private double period;

    public void cancel() {
        LocalDate now = LocalDate.from(LocalDateTime.now());
        if((this.getStartDate()).isAfter(now)){
            this.setStatus(VacationStatus.CANCEL);
        }else{
            throw new IllegalStateException("휴가가 이미 진행중입니다");
        }
    }
}
