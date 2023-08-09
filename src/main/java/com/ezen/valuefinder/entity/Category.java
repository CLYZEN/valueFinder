package com.ezen.valuefinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "category")
@ToString
@Getter
@Setter
public class Category {
    @Id
    private Long categoryCode; // 카테고리코드

    @Column(nullable = false)
    private String categoryName; // 카테고리명
}
