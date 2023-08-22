package com.ezen.valuefinder.service;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.Status;
import com.ezen.valuefinder.dto.MemberFindPwDto;
import com.ezen.valuefinder.dto.MemberFormDto;
import com.ezen.valuefinder.dto.MemberModifyDto;
import com.ezen.valuefinder.entity.Bank;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.MemberOut;
import com.ezen.valuefinder.repository.AuctionQueryRepository;
import com.ezen.valuefinder.repository.BankRepository;
import com.ezen.valuefinder.repository.MemberOutRepository;
import com.ezen.valuefinder.repository.MemberRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	private final AuctionQueryRepository auctionQueryRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email);

		if (member == null) {
			throw new UsernameNotFoundException(email);
		}
		return new PrincipalDetails(member);
		/*
		 * return User.builder() .username(member.getEmail())
		 * .password(member.getPassword()) .roles(member.getRole().toString()) .build();
		 * 
		 */
	}

	public List<Bank> getBankList() {
		return bankRepository.findAll();
	}

	public Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

		Bank bank = bankRepository.findById(memberFormDto.getBankCode()).orElseThrow();
		Member member = Member.createMember(memberFormDto, passwordEncoder, bank);
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

	public void updateMember(MemberModifyDto memberModifyDto, String email) {
		Bank bank = bankRepository.findById(memberModifyDto.getBankCode()).orElseThrow();
		Member member = memberRepository.findByEmail(email);
		member.updateMember(memberModifyDto, bank);
	}

	public Member findPwChkMember(MemberFindPwDto memberFindPwDto) {
		return memberRepository.findByEmailAndPhone(memberFindPwDto.getEmail(), memberFindPwDto.getPhone());
	}

	public boolean checkPwd(String password, String email, PasswordEncoder passwordEncoder) {
		Member member = memberRepository.findByEmail(email);
		return passwordEncoder.matches(password, member.getPassword());
	}

	public void updatePwd(String password, String email, PasswordEncoder passwordEncoder) {
		Member member = memberRepository.findByEmail(email);
		member.updatePassword(password, passwordEncoder);
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
	
	
	
}
