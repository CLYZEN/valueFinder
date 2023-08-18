package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.ItemImg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
	List<ItemImg> findByItemItemNo(Long itemNo);

}
