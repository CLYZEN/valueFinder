package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.SuccessBidding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuccessBiddingRepository extends JpaRepository<SuccessBidding,Long> {

    SuccessBidding findByAuction(Auction auction);

    Page<SuccessBidding> findByMember(Member member, Pageable pageable);

}
