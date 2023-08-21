package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.dto.ItemsListDto;
import com.ezen.valuefinder.entity.Item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item,Long>, ItemRepositoryCustom {

	@Query(value = "select A.item_no id,A.item_name name, B.item_image_url url, auction_start_price startPrice, auction_now_price nowPrice, left(auction_end_time,16) endTime, now() nowTime,\r\n"
			+ "(select count(auction_no) from bidding z where z.auction_no = C.auction_no ) as count, CASE when TIMESTAMPDIFF(SECOND,  NOW(), left(auction_end_time,19)) <= 0 then '경매종료' when TIMESTAMPDIFF(SECOND,  NOW(), left(auction_end_time,19)) <= 3600 then '마감임박' else '경매진행중' end 'status'\r\n"
			+ ", if (TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) > 0, TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) , 0) Day\r\n"
			+ ", case when TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) * 24 else 0 end hour\r\n"
			+ ", case when TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) * 60 else 0 end minute\r\n"
			+ ", case when TIMESTAMPDIFF(second,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(second,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) * 60 else 0 end second\r\n"
			+ "from item A \r\n"
			+ "left join item_img B\r\n"
			+ "on A.item_no = B.item_no\r\n"
			+ "left join auction C\r\n"
			+ "on A.item_no = C.item_no\r\n"
			+ "where B.rep_image_yn = '1' and C.auction_type = 'REALTIME'", nativeQuery = true)
	List<ItemsListDto> getRealtimeAuctionList();
	
	
	@Query(value = "select A.item_no id,A.item_name name, B.item_image_url url, auction_start_price startPrice, auction_now_price nowPrice, left(auction_end_time,16) endTime, now() nowTime,\r\n"
			+ "(select count(auction_no) from bidding z where z.auction_no = C.auction_no ) as count, CASE when TIMESTAMPDIFF(SECOND,  NOW(), left(auction_end_time,19)) <= 0 then '경매종료' when TIMESTAMPDIFF(SECOND,  NOW(), left(auction_end_time,19)) <= 3600 then '마감임박' else '경매진행중' end 'status'\r\n"
			+ ", if (TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) > 0, TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) , 0) Day\r\n"
			+ ", case when TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) * 24 else 0 end hour\r\n"
			+ ", case when TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) * 60 else 0 end minute\r\n"
			+ ", case when TIMESTAMPDIFF(second,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(second,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) * 60 else 0 end second\r\n"
			+ "from item A \r\n"
			+ "left join item_img B\r\n"
			+ "on A.item_no = B.item_no\r\n"
			+ "left join auction C\r\n"
			+ "on A.item_no = C.item_no\r\n"
			+ "where B.rep_image_yn = '1' and C.auction_type = 'PUBLIC'", nativeQuery = true)
	List<ItemsListDto> getPublicAuctionList();
	
	@Query(value = "select A.item_no id,A.item_name name, B.item_image_url url, auction_start_price startPrice, auction_now_price nowPrice, left(auction_end_time,19) endTime, left(now(),19) nowTime,\r\n"
			+ "(select count(auction_no) from bidding z where z.auction_no = C.auction_no ) as count, CASE when TIMESTAMPDIFF(SECOND,  NOW(), left(auction_end_time,19)) <= 0 then '경매종료' when TIMESTAMPDIFF(SECOND,  NOW(), left(auction_end_time,19)) <= 3600 then '마감임박' else '경매진행중' end 'status'\r\n"
			+ ", if (TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) > 0, TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) , 0) Day\r\n"
			+ ", case when TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(Day,  NOW(), left(auction_end_time,19)) * 24 else 0 end hour\r\n"
			+ ", case when TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(hour,  NOW(), left(auction_end_time,19)) * 60 else 0 end minute\r\n"
			+ ", case when TIMESTAMPDIFF(second,  NOW(), left(auction_end_time,19)) > 0 then TIMESTAMPDIFF(second,  NOW(), left(auction_end_time,19)) - TIMESTAMPDIFF(minute,  NOW(), left(auction_end_time,19)) * 60 else 0 end second\r\n"
			+ "from item A \r\n"
			+ "left join item_img B\r\n"
			+ "on A.item_no = B.item_no\r\n"
			+ "left join auction C\r\n"
			+ "on A.item_no = C.item_no\r\n"
			+ "where B.rep_image_yn = '1' and C.auction_type = 'SEALED'", nativeQuery = true)
	List<ItemsListDto> getSealedAuctionList();
	
}