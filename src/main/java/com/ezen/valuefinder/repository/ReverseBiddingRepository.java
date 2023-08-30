package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.ReverseBidding;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReverseBiddingRepository extends JpaRepository<ReverseBidding,Long> {
	@Modifying
	@Query("update ReverseBidding r set r.reverseBiddingCount = r.reverseBiddingCount+1 where r.reverseBiddingNo = :id")
	int reverseBiddingCount(Long id);


    Page<ReverseBidding> findAllByOrderByReverseBiddingExpireDate(Pageable pageable);
    
    Page<ReverseBidding> findByCategoryOrderByReverseBiddingExpireDate(Pageable pageable, Category category);
}
