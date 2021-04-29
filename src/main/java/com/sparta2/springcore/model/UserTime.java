package com.sparta2.springcore.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class UserTime { // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private long totalTime;

    @ColumnDefault("0")
    private int totalCnt;


    public UserTime(User user, long totalTime , int totalCnt) {
        this.user = user;
        this.totalTime = totalTime;
        this.totalCnt = totalCnt;
    }

    public void updateTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
    public void updateTotalCnt(int totalCnt){
        this.totalCnt = totalCnt;
    }

}