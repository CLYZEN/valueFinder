package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item_img")
@ToString
@Getter
@Setter
public class ItemImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemImageNo; // 물품이미지식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_no")
    private Item item; // 물품

    @Column(nullable = false)
    private String itemImageName; // 물품이미지명

    @Column(nullable = false)
    private String itemOriImageName; // 물품원본이미지명

    @Column(nullable = false)
    private String itemImageUrl; // 물품이미지경로

    @Column(nullable = false)
    private boolean repImageYn; // 대표이미지여부

    public void saveItem(Item item) {
        this.item = item;
    }

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.itemOriImageName = oriImgName;
        this.itemImageName = imgName;
        this.itemImageUrl = imgUrl;
    }
}
