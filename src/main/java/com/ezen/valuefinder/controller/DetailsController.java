package com.ezen.valuefinder.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class DetailsController {
	
	@GetMapping(value = "/publicbidDetails")
    public String publicbidDetails() {
    	return "details/publicbidDetails";
    }
}
