package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.CouponList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponListRepository extends JpaRepository<CouponList, Long> {
    List<CouponList> findAll();
}
