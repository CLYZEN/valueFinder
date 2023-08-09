package com.ezen.valuefinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bank")
@ToString
@Getter
@Setter
public class Bank {
    @Id
    private Long bankCode; // 은행코드

    @Column(nullable = false)
    private String bankName; // 은행명
}
