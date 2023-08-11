package com.ezen.valuefinder.entity;

import com.ezen.valuefinder.constant.Role;
import com.ezen.valuefinder.constant.Status;
import com.ezen.valuefinder.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId; // 회원식별자

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false, unique = true)
    private String email; // 이메일(아이디)

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 역할 (USER, ADMIN)

    @Column(nullable = false)
    private Integer addressNo; // 우편번호

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String addressDetail; // 상세주소

    @Column(nullable = false)
    private String phone; // 휴대폰번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_code")
    private Bank bank;

    @Column(nullable = false)
    private String bankAddress; // 계좌번호

    private LocalDate birthday; // 생일

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // 계정상태 (ACTIVE, DISABLE)

    @Column(nullable = false)
    private Integer caution; // 경고횟수

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder,Bank bank) {
        Member member = new Member();

        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
        member.setNickname(memberFormDto.getNickname());
        member.setRole(Role.USER);
        member.setAddressNo(memberFormDto.getAddressNo());
        member.setAddress(memberFormDto.getAddress());
        member.setAddressDetail(memberFormDto.getAddressDetail());
        member.setPhone(memberFormDto.getPhone());
        member.setBank(bank);
        member.setBankAddress(memberFormDto.getBankAddress());
        member.setBirthday(memberFormDto.getBirthday());
        member.setStatus(Status.ACTIVE);
        member.setCaution(0);

        return member;
    }
}
