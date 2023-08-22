package com.ezen.valuefinder.service;

import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.ItemImgDto;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;
import com.ezen.valuefinder.dto.ReverseAuctionFormDto;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.CategoryRepository;
import com.ezen.valuefinder.repository.ItemImgRepository;
import com.ezen.valuefinder.repository.ItemRepository;
import com.ezen.valuefinder.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import com.ezen.valuefinder.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.valuefinder.dto.NormalAuctionFormDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ItemImgService itemImgService;
    private final ItemRepository itemRepository;
    private final AuctionRepository auctionRepository;
    private final ItemImgRepository itemImgRepository;
    private final ReverseBiddingRepository reverseBiddingRepository;
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public Long createAuction(NormalAuctionFormDto normalAuctionFormDto, List<MultipartFile> itemImgFiles, String email) throws Exception{
        Category category;

        Member member = memberRepository.findByEmail(email);

        // item entity create
        Item item = new Item();
        item.setCategory(categoryRepository.findById(normalAuctionFormDto.getCategory()).orElseThrow());
        item.setItemName(normalAuctionFormDto.getItemName());
        item.setItemDetail(normalAuctionFormDto.getItemDetail());
        item.setItemWidth(normalAuctionFormDto.getItemWidth());
        item.setItemDepth(normalAuctionFormDto.getItemDepth());
        item.setItemHeight(normalAuctionFormDto.getItemHeight());
        item.setMember(member);

        itemRepository.save(item);

        Auction auction = new Auction();
        auction.setItem(item);
        if(normalAuctionFormDto.getAuctionDistinction() == 1) {
            auction.setAuctionType(AuctionType.REALTIME);
        } else if (normalAuctionFormDto.getAuctionDistinction() == 2) {
            auction.setAuctionType(AuctionType.PUBLIC);
        } else if (normalAuctionFormDto.getAuctionDistinction() == 3) {
            auction.setAuctionType(AuctionType.SEALED);
        }
        auction.setAuctionStartPrice(normalAuctionFormDto.getAuctionStartPrice());
        auction.setAuctionNowPrice(normalAuctionFormDto.getAuctionStartPrice());
        auction.setAuctionStartTime(normalAuctionFormDto.getAuctionStartTime());
        auction.setAuctionEndTime(normalAuctionFormDto.getAuctionEndTime());
        if(normalAuctionFormDto.getAuctionStartTime().isAfter(LocalDateTime.now())
                || normalAuctionFormDto.getAuctionStartTime().isEqual(LocalDateTime.now())) {
            auction.setAuctionStatus(AuctionStatus.PROGRESS);
        } else {
            auction.setAuctionStatus(AuctionStatus.PENDING);
        }
        auction.setAuctionCount(0);

        auctionRepository.save(auction);

        for(int i=0; i<itemImgFiles.size(); i++) {

            ItemImg itemImg = new ItemImg();
            itemImg.saveItem(item);


            if(i == 0) {
                itemImg.setRepImageYn(true);
            } else {
                itemImg.setRepImageYn(false);
            }

            itemImgService.saveItemImg(itemImg, itemImgFiles.get(i));
        }

        return auction.getAuctionNo();
    }
    
    @Transactional(readOnly = true)
    public Auction getAuctionDetail(Long auctionNo) {
    	return auctionRepository.findById(auctionNo).orElseThrow();
    }


    public Long createReverseAuction(ReverseAuctionFormDto reverseAuctionFormDto, String email) {
        ReverseBidding reverseBidding = new ReverseBidding();
        reverseBidding.setReverseBiddingTitle(reverseAuctionFormDto.getReverseBiddingTitle());
        reverseBidding.setReverseBiddingDetail(reverseAuctionFormDto.getReverseBiddingDetail());
        reverseBidding.setHopePrice(reverseAuctionFormDto.getHopePrice());
        reverseBidding.setReverseBiddingExpireDate(reverseAuctionFormDto.getReverseBiddingExpireDate());
        reverseBidding.setCategory(reverseAuctionFormDto.getCategory());

        Member member = memberRepository.findByEmail(email);

        reverseBidding.setMember(member);
        reverseBiddingRepository.save(reverseBidding);

        return reverseBidding.getReverseBiddingNo();
    }
    
    @Transactional
    public int auctionCount(Long id) {
    	return auctionRepository.auctionCount(id);
    }
    
    public String getRemainTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration remainingDuration = Duration.between(now, dateTime);

        return formatDuration(remainingDuration);
    }

    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%d일 %d시간 %d분 %d초", days, hours, minutes, seconds);
    }

	public int itemCount(Long memberId) {
		return itemRepository.countItemsByMemberId(memberId);	
	}


	public Page<Auction> getAuctionList(Long memberId, Pageable pageable) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		return auctionRepository.findByItemMember(member, pageable);
	}
	

<<<<<<< HEAD
	 public void updateAuctionStatusToProgress(Long auctionId) {
	        Auction auction = auctionRepository.findById(auctionId).orElse(null);
	        if (auction.getAuctionStatus() == AuctionStatus.PENDING) {
	            if (auction.getAuctionStartTime().isBefore(LocalDateTime.now())) {
	                auction.setAuctionStatus(AuctionStatus.PROGRESS);
	                auctionRepository.save(auction);
	            }
	        }
	    }
	
	 public void updateAuctionStatusToEnd(Long auctionId) {
	        Auction auction = auctionRepository.findById(auctionId).orElse(null);
	        if (!auction.getAuctionStatus().equals(AuctionStatus.END)) {
	            if (auction.getAuctionEndTime().isBefore(LocalDateTime.now())) {
	                auction.setAuctionStatus(AuctionStatus.END);
	                auctionRepository.save(auction);
	            }
	        }
	    }
	 
	}

    
    

=======

    
    
}
>>>>>>> f9cd23f27d0bdc693f4412fa7dd68e577890ef5e
