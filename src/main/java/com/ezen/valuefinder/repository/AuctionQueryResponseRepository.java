package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.AuctionQueryResponse;
import com.ezen.valuefinder.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionQueryResponseRepository extends JpaRepository<AuctionQueryResponse,Long> {
    Page<AuctionQueryResponse> findByMember(Pageable pageable , Member member);
}
