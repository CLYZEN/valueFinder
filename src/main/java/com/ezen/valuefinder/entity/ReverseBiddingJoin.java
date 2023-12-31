package com.ezen.valuefinder.entity;

import com.ezen.valuefinder.dto.ReversebidEnterDto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reverse_bidding_join")
@ToString
@Getter
@Setter
public class ReverseBiddingJoin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reverseBiddingJoinNo; // 역경매참가식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_bidding_no")
    private ReverseBidding reverseBidding; // 역경매

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_no")
    private Item item; // 물품

    @Column(nullable = false)
    private Integer suggestPrice; // 제시가격
    
    public void updateReverseBiddingJoin(ReversebidEnterDto reversebidEnterDto) {
    	this.suggestPrice = reversebidEnterDto.getSuggestPrice();
    	this.item.setItemImgList(reversebidEnterDto.getItemImgDtoList());
    	this.item.setItemDetail(reversebidEnterDto.getItemDetail());
    	this.item.setItemName(reversebidEnterDto.getItemName());
    	this.item.setItemImgList(reversebidEnterDto.getItemImgDtoList());
    }







    
    public void deleteReverseBiddingJoin(ReversebidEnterDto reversebidEnterDto) {
    	deleteReverseBiddingJoin(reversebidEnterDto);
    }
    
}
