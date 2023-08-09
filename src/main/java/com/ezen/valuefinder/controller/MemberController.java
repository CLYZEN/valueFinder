package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

	 @GetMapping(value = "member/login2")
	    public String login() {
	        return "member/login2";
	    }

}
