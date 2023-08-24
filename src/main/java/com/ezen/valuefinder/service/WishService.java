package com.ezen.valuefinder.service;

public interface WishService {
    void addWish(Long auctionNo, Long memberId);

    void deleteWish(Long auctionNo, Long memberId);

	boolean checkWish(Long auctionNo, Long memberId); 
	 
}
