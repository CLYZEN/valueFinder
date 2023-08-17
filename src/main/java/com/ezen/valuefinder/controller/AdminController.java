package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.dto.CreateCouponDto;
import com.ezen.valuefinder.entity.CouponList;
import com.ezen.valuefinder.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

	private final CouponService couponService;
	@GetMapping(value = "/admin")
	public String adminMain() {

		return "admin/main";
	}

	@GetMapping(value = "/admin/itemList")
	public String adminitemlist() {
		return "admin/itemList";
	}

	@GetMapping(value = "/admin/customerList")
	public String admincustomerList() {
		return "admin/customerList";
	}

	@GetMapping(value = "/admin/itemreportList")
	public String adminitemreportList() {
		return "admin/itemreportList";
	}

	@GetMapping(value = "/admin/customerReportList")
	public String admincustomerReportList() {
		return "admin/customerReportList";
	}

	@GetMapping(value = "/admin/customerStatus")
	public String admincustomerStatus() {
		return "admin/customerStatus";
	}

	@GetMapping(value = "/admin/itemStatus")
	public String adminitemStatus() {
		return "admin/itemStatus";
	}

	@GetMapping(value = "/admin/coupon")
	public String couponManage(Model model) {
		List<CouponList> couponList = couponService.getCouponList();
		model.addAttribute("couponList", couponList);
		return "admin/coupon";
	}

	@GetMapping(value = "/admin/coupon/create")
	public String createCoupon(Model model) {
		model.addAttribute("createCouponDto", new CreateCouponDto());
		return "admin/createcoupon";
	}

	@PostMapping(value = "/admin/coupon/create")
	public String createCoupon(@Valid CreateCouponDto createCouponDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("createCouponDto", new CreateCouponDto());
			return "admin/createcoupon";
		}
		couponService.createCoupon(createCouponDto);

		return "redirect:/admin";
	}
}
