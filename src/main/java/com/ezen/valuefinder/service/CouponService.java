package com.ezen.valuefinder.service;


import com.ezen.valuefinder.dto.CreateCouponDto;
import com.ezen.valuefinder.entity.CouponList;
import com.ezen.valuefinder.repository.CouponListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {

    private final CouponListRepository couponListRepository;

    public void createCoupon(CreateCouponDto createCouponDto) {
        CouponList couponList = new CouponList();
        couponList.setCouponTitle(createCouponDto.getCouponTitle());
        couponList.setCouponDetail(createCouponDto.getCouponDetail());
        couponList.setCouponDiscount(createCouponDto.getCouponDiscount());
        couponList.setStartDate(createCouponDto.getStartDate());
        couponList.setExpireDate(createCouponDto.getExpireDate());

        couponListRepository.save(couponList);
    }

    public List<CouponList> getCouponList() {
        return couponListRepository.findAll();
    }
}