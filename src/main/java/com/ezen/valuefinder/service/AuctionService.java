package com.ezen.valuefinder.service;

import com.ezen.valuefinder.constant.AuctionQueryDistinction;
import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.constant.ReversebidAuctionStatus;
import com.ezen.valuefinder.dto.ItemImgDto;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;

import com.ezen.valuefinder.dto.AuctionQueryDto;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.AuctionQueryRepository;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.CategoryRepository;
import com.ezen.valuefinder.repository.ItemRepository;
import com.ezen.valuefinder.repository.MemberRepository;

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
    private final AuctionQueryRepository auctionQueryRepository;
    private final ItemImgRepository itemImgRepository;
    private final ReverseBiddingRepository reverseBiddingRepository;

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public Long createAuction(NormalAuctionFormDto normalAuctionFormDto, List<MultipartFile> itemImgFiles, String email) throws Exception {
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
        if (normalAuctionFormDto.getAuctionDistinction() == 1) {
            auction.setAuctionType(AuctionType.REALTIME);
        } else if (normalAuctionFormDto.getAuctionDistinction() == 2) {
            auction.setAuctionType(AuctionType.PUBLIC);
        } else if (normalAuctionFormDto.getAuctionDistinction() == 3) {
            auction.setAuctionType(AuctionType.SEALED);
        }
        auction.setAuctionStartPrice(normalAuctionFormDto.getAuctionStartPrice());
        auction.setAuctionNowPrice(normalAuctionFormDto.getAuctionStartPrice());
        auction.setAuctionStartTime(normalAuctionFormDto.getAuctionStartTime());
        auction.setBiddingCount(0);
        if (normalAuctionFormDto.getAuctionDistinction() == 1) {
            auction.setAuctionEndTime(normalAuctionFormDto.getAuctionStartTime().plusMinutes(5));
        } else {
            auction.setAuctionEndTime(normalAuctionFormDto.getAuctionEndTime());
        }
        if (normalAuctionFormDto.getAuctionStartTime().isAfter(LocalDateTime.now())
                || normalAuctionFormDto.getAuctionStartTime().isEqual(LocalDateTime.now())) {
            auction.setAuctionStatus(AuctionStatus.PROGRESS);
        } else {
            auction.setAuctionStatus(AuctionStatus.PENDING);
        }
        auction.setAuctionCount(0);

        auctionRepository.save(auction);

        for (int i = 0; i < itemImgFiles.size(); i++) {

            ItemImg itemImg = new ItemImg();
            itemImg.saveItem(item);


            if (i == 0) {
                itemImg.setRepImageYn(true);
            } else {
                itemImg.setRepImageYn(false);
            }

            itemImgService.saveItemImg(itemImg, itemImgFiles.get(i));
        }

        return auction.getAuctionNo();
    }


    public Long createdQuery(AuctionQueryDto auctionQueryDto, String email) throws Exception {

        Member member = memberRepository.findByEmail(email);


        AuctionQuery auctionQuery = new AuctionQuery();

        auctionQuery.setAuctionQueryDetail(auctionQueryDto.getAuctionQueryDtail());
        auctionQuery.setAuctionQueryTitle(auctionQueryDto.getAuctionQueryTitle());


        if (auctionQueryDto.getAuctionQueryDistinction() == 1) {
            auctionQuery.setAuctionQueryDistinction(AuctionQueryDistinction.ETC);
        } else if (auctionQueryDto.getAuctionQueryDistinction() == 2) {
            auctionQuery.setAuctionQueryDistinction(AuctionQueryDistinction.ITEM);
        } else if (auctionQueryDto.getAuctionQueryDistinction() == 3) {
            auctionQuery.setAuctionQueryDistinction(AuctionQueryDistinction.SHIPPING);
        }

        auctionQueryRepository.save(auctionQuery);

        return auctionQuery.getAuctionQueryNo();


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
        reverseBidding.setReversebidAuctionStatus(ReversebidAuctionStatus.PROGRESS);

        Member member = memberRepository.findByEmail(email);

        reverseBidding.setMember(member);
        reverseBiddingRepository.save(reverseBidding);

        return reverseBidding.getReverseBiddingNo();
    }


    public Auction getAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        return auction;
    }

    public void addAuctionView(Long id) {
        Auction auction = auctionRepository.findById(id).orElseThrow();
        auction.setAuctionCount(auction.getAuctionCount()+1);
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

    private void updateAuctionStatus(Auction auction) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = auction.getAuctionEndTime();
        Duration duration = Duration.between(now, endTime);


        if (auction.getAuctionStatus() == AuctionStatus.PENDING) {

            if (auction.getAuctionStartTime().isBefore(LocalDateTime.now())) {
                auction.setAuctionStatus(AuctionStatus.PROGRESS);
            }

        }
        if (!auction.getAuctionStatus().equals(AuctionStatus.END)) {

            if (auction.getAuctionEndTime().isBefore(LocalDateTime.now())) {
                auction.setAuctionStatus(AuctionStatus.END);
            }
            if (duration.getSeconds() <= 60) { // 남은 시간이 1분 이하일 경우
                auction.setAuctionStatus(AuctionStatus.LAST);
            }
        }

    }

    public void updateAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        updateAuctionStatus(auction);
        updateAuctionReaminTime(auction);
    }

    private void updateAuctionReaminTime(Auction auction) {
        Duration remainingDuration = Duration.between(LocalDateTime.now(), auction.getAuctionEndTime());
        long hours = remainingDuration.toHours();
        long minutes = remainingDuration.minusHours(hours).toMinutes();
        long seconds = remainingDuration.minusHours(hours).minusMinutes(minutes).getSeconds();
        if (remainingDuration.isNegative() || remainingDuration.isZero()) {
            auction.setRemainingTime("종료된 경매입니다.");
        } else if (hours == 0) {
            if (minutes == 0) {
                auction.setRemainingTime(seconds + "초");
            } else {
                auction.setRemainingTime(minutes + "분 " + seconds + "초");
            }
        } else {
            auction.setRemainingTime(hours + "시간 " + minutes + "분 " + seconds + "초");
        }
    }


    public Page<Auction> getAuctionList(Pageable pageable, AuctionType auctionType, Long categoryCode) {
        if(categoryCode == 0) {
            return auctionRepository.findByAuctionTypeOrderByAuctionEndTimeDescAuctionCountDesc(auctionType,pageable);
        }
        Category category = categoryRepository.findById(categoryCode).orElseThrow();
        if(category == null) {
            return auctionRepository.findByAuctionTypeOrderByAuctionEndTimeDesc(auctionType, pageable);
        } else if (category != null) {
            return auctionRepository.findByAuctionTypeAndItemCategoryOrderByAuctionEndTimeDesc(auctionType,pageable,category);
        }
        return auctionRepository.findByAuctionTypeOrderByAuctionEndTimeDesc(auctionType, pageable);
    }
    public Page<Auction> getMemberAuctionList(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        return auctionRepository.findByItemMember(member, pageable);
    }


}

