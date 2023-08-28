package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.ReverseBiddingJoin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReverseBiddingJoinRepository extends JpaRepository<ReverseBiddingJoin,Long> {

	Page<ReverseBiddingJoin> findByReverseBiddingReverseBiddingNoOrderByReverseBiddingJoinNo(Pageable pageable, Long reverseBiddingJoinNo);
}
