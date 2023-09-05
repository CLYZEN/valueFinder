package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Coupon;
import com.ezen.valuefinder.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
    List<Coupon> findByMember(Member member);

    Page<Coupon> findByMember(Member member, Pageable pageable);
}
