package com.ezen.valuefinder.handler;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.Status;
import com.ezen.valuefinder.dto.MemberLoginDto;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        response.sendRedirect(targetUrl(member,request));
    }

    private String targetUrl(Member member, HttpServletRequest request) {
        if(member.getStatus().equals(Status.DISABLE)) {
            HttpSession session = request.getSession(false);
            if(session != null) {
                session.invalidate();
            }
            return "/repair";
        } else {
            return "/";
        }
    }

}
