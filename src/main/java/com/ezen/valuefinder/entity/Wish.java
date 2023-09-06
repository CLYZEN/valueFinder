package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "wish")
public class Wish extends BaseEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "wish_no")
    private Long wishNo; // 찜 식별자

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auction_no")
    private Auction auction; // 경매

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //회원
    

}
