package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.entity.Coupon;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CouponService couponService;

    @GetMapping(value = "/")
    public String main(Authentication authentication,Model model) {

        if(isAuthenticated()) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Member member = principalDetails.getMember();
            List<Coupon> couponList = couponService.getMemberCouponList(member);
            if(couponList.size() == 0) {
                couponService.setFirstLoginCoupon(member);
                model.addAttribute("message", "첫 로그인 쿠폰이 발급되었습니다!");
                return "index";
            }
        }

        return "index";
    }


    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }

}
