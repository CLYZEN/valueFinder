package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.SuccessBidding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuccessBiddingRepository extends JpaRepository<SuccessBidding,Long> {

    SuccessBidding findByAuction(Auction auction);
}
