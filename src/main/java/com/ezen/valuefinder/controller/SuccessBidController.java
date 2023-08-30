package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.service.BiddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SuccessBidController {

    private final BiddingService biddingService;

    @PostMapping("/member/mypage/updateStatus")
    public @ResponseBody ResponseEntity updateStatus(@RequestBody Map<String, Object> requestBody) {
        Long id = Long.parseLong(requestBody.get("no").toString());
        String status = requestBody.get("status").toString();

        biddingService.updateBidStatus(id,status);
        return new ResponseEntity("ok", HttpStatus.OK);
    }
}
