package com.ezen.valuefinder.service;


import com.ezen.valuefinder.dto.CreateCouponDto;
import com.ezen.valuefinder.entity.Coupon;
import com.ezen.valuefinder.entity.CouponList;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.CouponListRepository;
import com.ezen.valuefinder.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {

    private final CouponListRepository couponListRepository;
    private final CouponRepository couponRepository;
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

    public List<Coupon> getMemberCouponList(Member member) {
        return couponRepository.findByMember(member);
    }

    public void setFirstLoginCoupon(Member member) {
        Coupon coupon = new Coupon();
        coupon.setCouponList(couponListRepository.findById(1L).orElseThrow());
        coupon.setMember(member);
        coupon.setCouponRegistDate(LocalDateTime.now());

        couponRepository.save(coupon);
    }

    public Page<Coupon> getMemberCouponPage(Member member, Pageable pageable) {
        return couponRepository.findByMember(member,pageable);
    }
}