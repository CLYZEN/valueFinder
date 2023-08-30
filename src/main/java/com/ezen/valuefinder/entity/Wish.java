package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "wish")
@ToString
@Getter
@Setter
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishNo; // 찜 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_no")
    private Auction auction; // 경매

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //회원
}
