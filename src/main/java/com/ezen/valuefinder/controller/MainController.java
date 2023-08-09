package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = "/")
    public String main() {
        return "index";
    }
    
    @GetMapping(value = "/inquiry")
    public String inquiry() {
        return "inquirys/inquiry";
    }
    
    @GetMapping(value = "/login2")
    public String login() {
        return "member/login";
    }

    @GetMapping(value = "/inquiryView")
    public String upinquiry() {
        return "inquirys/inquiryView";
    }
    
    @GetMapping(value = "/report")
    public String report() {
        return "inquirys/report";
    }

    @GetMapping(value = "/publicbidDetails")
    public String publicbidDetails() {
    	return "details/publicbidDetails";
    }
}
