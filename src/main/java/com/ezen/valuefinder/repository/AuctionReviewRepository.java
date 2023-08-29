package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionReviewRepository extends JpaRepository<AuctionReview,Long> {

    AuctionReview findByAuction(Auction auction);
}
