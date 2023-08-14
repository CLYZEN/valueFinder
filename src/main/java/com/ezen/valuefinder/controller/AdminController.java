package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	@GetMapping(value = "/admin")
	public String adminM() {
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

}
