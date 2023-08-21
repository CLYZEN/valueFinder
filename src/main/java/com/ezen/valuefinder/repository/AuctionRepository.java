package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction,Long> {

}
