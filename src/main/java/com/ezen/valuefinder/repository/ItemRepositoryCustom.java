package com.ezen.valuefinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.valuefinder.dto.ItemSearchDto;
import com.ezen.valuefinder.entity.Item;

public interface ItemRepositoryCustom {
	Page<Item> getAuctionPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
