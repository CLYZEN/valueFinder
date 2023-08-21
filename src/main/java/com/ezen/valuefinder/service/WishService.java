package com.ezen.valuefinder.service;

public interface WishService {
    void addWish(Long auctionNo, Long memberId);

    void deleteWish(Long auctionNo, Long memberId);

	boolean checkWish(Long auctionNo, Long memberId);  //이미 체크 되어있는지 아닌지 확인
	 
}
