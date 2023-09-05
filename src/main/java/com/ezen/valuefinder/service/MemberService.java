package com.ezen.valuefinder.service;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.Status;
import com.ezen.valuefinder.dto.MemberFindPwDto;
import com.ezen.valuefinder.dto.MemberFormDto;
import com.ezen.valuefinder.dto.MemberModifyDto;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BankRepository bankRepository;
    private final MemberOutRepository memberOutRepository;
    private final WishRepository wishRepository;
    private final MemberReportRepository memberReportRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }
        return new PrincipalDetails(member);
        /*
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

         */
    }

    public List<Bank> getBankList() {
        return bankRepository.findAll();
    }
    
    public Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

        Bank bank = bankRepository.findById(memberFormDto.getBankCode()).orElseThrow();
        Member member = Member.createMember(memberFormDto,passwordEncoder,bank);
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member;
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void updateMember(MemberModifyDto memberModifyDto,String email) {
        Bank bank = bankRepository.findById(memberModifyDto.getBankCode()).orElseThrow();
        Member member = memberRepository.findByEmail(email);
        member.updateMember(memberModifyDto,bank);
    }

    public Member findPwChkMember(MemberFindPwDto memberFindPwDto) {
        return memberRepository.findByEmailAndPhone(memberFindPwDto.getEmail(),memberFindPwDto.getPhone());
    }

    public boolean checkPwd(String password,String email,PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(email);
        return passwordEncoder.matches(password,member.getPassword());
    }

    public void updatePwd(String password, String email, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(email);
        member.updatePassword(password,passwordEncoder);
    }

    public void memberOut(String email, String detail) {
        Member outMember = memberRepository.findByEmail(email);

        MemberOut memberOut = new MemberOut();

        memberOut.setMember(outMember);
        memberOut.setMemberOutDetail(detail);
        memberOutRepository.save(memberOut);
        outMember.setStatus(Status.DISABLE);
    }

    public void repairMember(String email) {
        Member member = memberRepository.findByEmail(email);
        member.setStatus(Status.ACTIVE);
    }
    public Page<Wish> getMemberWishList(Member member, Pageable pageable) {
        return wishRepository.findByMember(member,pageable);
    }
    public List<Member> getMemberList() {
        return memberRepository.findByStatus(Status.ACTIVE);
    }
    
    public List<Member> getMemberdropList() {
        return memberRepository.findByStatus(Status.DISABLE);
    }
    public void memberCaution(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.setCaution(member.getCaution() + 1);

        MemberOut memberOut = new MemberOut();

        if (member.getCaution() == 3) {

            member.setStatus(Status.DISABLE);

            memberOut.setMember(member);
            memberOut.setMemberOutDetail("경고누적");

            memberOutRepository.save(memberOut);
        }

    }
    
    
    public void memberdropCaution(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.setCaution(member.getCaution() - 1);


        if (member.getCaution() < 3) {
   
    		MemberOut memberOut =  memberOutRepository.findByMember(member);
    	
    		memberOutRepository.delete(memberOut);
        	member.setStatus(Status.ACTIVE);

        }

    }
    
    public List<MemberReport> getMemberReportList() {
        return memberReportRepository.findAll();
    }

    public void deleteMemberOut(Member member) {
        MemberOut dropMember = memberOutRepository.findByMember(member);

        memberOutRepository.delete(dropMember);
    }
}