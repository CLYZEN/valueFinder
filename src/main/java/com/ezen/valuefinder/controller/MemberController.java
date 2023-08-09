package com.ezen.valuefinder.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class MemberController {

	 @GetMapping(value = "/login2")
	    public String login() {
	        return "member/login2";
	    }

}
