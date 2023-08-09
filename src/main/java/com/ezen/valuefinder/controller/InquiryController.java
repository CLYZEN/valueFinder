package com.ezen.valuefinder.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class InquiryController {

	@GetMapping(value = "/inquiry")
    public String inquiry() {
        return "inquirys/inquiry";
    }
	
	@GetMapping(value = "/inquiryView")
    public String upinquiry() {
        return "inquirys/inquiryView";
    }
    
    @GetMapping(value = "/report")
    public String report() {
        return "inquirys/report";
    }
}
