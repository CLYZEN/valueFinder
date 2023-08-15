package com.ezen.valuefinder.service;

import com.ezen.valuefinder.dto.MemberFindPwDto;
import com.ezen.valuefinder.dto.MemberFormDto;
import com.ezen.valuefinder.dto.MemberModifyDto;
import com.ezen.valuefinder.entity.Bank;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.BankRepository;
import com.ezen.valuefinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
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
}
