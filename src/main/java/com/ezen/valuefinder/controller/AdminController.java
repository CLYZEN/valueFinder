package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	
	
				@GetMapping(value = "admin/main")
				public String adminM() {
					return "admin/main";
				}
	
}
