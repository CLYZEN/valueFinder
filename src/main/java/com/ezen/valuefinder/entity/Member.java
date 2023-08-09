package com.ezen.valuefinder.entity;

import com.ezen.valuefinder.constant.Role;
import com.ezen.valuefinder.constant.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private String phone; // 휴대폰번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_code")
    private Bank bank;

    @Column(nullable = false)
    private String bankAddress; // 계좌번호

    private LocalDateTime birthday; // 생일

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // 계정상태 (ACTIVE, DISABLE)

    @Column(nullable = false)
    private Integer caution; // 경고횟수
}
