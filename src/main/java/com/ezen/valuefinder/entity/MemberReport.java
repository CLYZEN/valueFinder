package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_report")
@ToString
@Getter
@Setter
public class MemberReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberReportId; // 회원신고식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 신고당한회원

    @Column(nullable = false)
    private String memberReportTitle; // 신고 제목

    @Column(nullable = false)
    private String memberReportDetail; // 신고 내용
}
