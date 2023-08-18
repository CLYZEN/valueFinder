package com.ezen.valuefinder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.ezen.valuefinder.dto.ItemSearchDto;
import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

	private JPAQueryFactory queryfactory;
	
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryfactory = new JPAQueryFactory(em);
	}
	
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("auctionNo", searchBy)) {
			//searchBy 가 itemNm이라면 -> 등록자로 검색 시
			return QItem.item.itemName.like("%" + searchQuery + "%"); //item_nm like %검색어%
		} else if(StringUtils.equals("createBy", searchBy)) {
			return QItem.item.createdBy.like("%" + searchQuery + "%"); //createBy like %검색어%
		}
		
		return null; //쿼리문을 실행하지 않는다.
	}
	
	
	@Override
	public Page<Item> getAuctionPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		List<Item> content = queryfactory
				.selectFrom(QItem.item)
				.where(
						searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
				.orderBy(QItem.item.itemName.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = queryfactory.select(Wildcard.count).from(QItem.item)
				.where(
						searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
				.fetchOne();
					
		return new PageImpl<>(content, pageable, total);
				
	}
	
}
