package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import com.ezen.valuefinder.dto.ItemImgDto;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemNo; // 물품식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code")
    private Category category; // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private String itemName; // 물품명

    @Column(nullable = false, columnDefinition = "longtext")
    @Lob
    private String itemDetail; // 물품설명

    private Integer itemWidth; // 가로길이

    private Integer itemDepth; // 세로길이

    private Integer itemHeight; // 높이

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ItemImg> itemImgList = new ArrayList<>();

	public void setItemImgList(List<ItemImgDto> itemImgList) {
	}
    
}
