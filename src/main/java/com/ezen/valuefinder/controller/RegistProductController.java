package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistProductController {

	@GetMapping(value = "/registproduct")
	public String registproduct() {
		return "/registproduct/registproduct";
	}
		
	@GetMapping(value = "/reverseregistproduct")
	public String reverseregistproduct() {
		return "/registproduct/reverseregistproduct";
	}
}
