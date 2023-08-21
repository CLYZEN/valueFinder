package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.dto.ItemsListDto;
import com.ezen.valuefinder.entity.Item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item,Long>, ItemRepositoryCustom {

	@Query(value = "select A.item_no id,A.item_name name, B.item_image_url url, auction_start_price startPrice, auction_now_price nowPrice, left(auction_end_time,16) endTime, now() nowTime,\r\n"
			+ "(select count(auction_no) from bidding z where z.auction_no = C.auction_no ) as count, CASE when TIMESTAMPDIFF(SECOND,  NOW(), left(auction_end_time,19)) <= 0 then '경매종료' else '경매진행중' end 'status'\r\n"
			+ "from item A \r\n"
			+ "left join item_img B\r\n"
			+ "on A.item_no = B.item_no\r\n"
			+ "left join auction C\r\n"
			+ "on A.item_no = C.item_no\r\n"
			+ "where B.rep_image_yn = '1'", nativeQuery = true)
	List<ItemsListDto> getItemsList();
}
