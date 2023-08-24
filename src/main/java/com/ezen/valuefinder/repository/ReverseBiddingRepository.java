package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.ReverseBidding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReverseBiddingRepository extends JpaRepository<ReverseBidding,Long> {

    Page<ReverseBidding> findAllByOrderByReverseBiddingExpireDate(Pageable pageable);
}
