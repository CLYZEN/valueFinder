package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.Member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item,Long> {


	@Query("select count(i) from Item i where i.member.id = :memberId")
	    int countItemsByMemberId(@Param("memberId") Long memberId);
	
	@Query("select i.itemName from Item i where i.member.id = :memberId")
	List<String> findItemNamesByMemberId(@Param("memberId") Long memberId);
	





}
