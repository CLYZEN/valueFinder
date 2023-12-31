package com.ezen.valuefinder.service;

import com.ezen.valuefinder.constant.*;
import com.ezen.valuefinder.dto.*;

import com.ezen.valuefinder.entity.*;

import com.ezen.valuefinder.dto.ReverseAuctionFormDto;

import com.ezen.valuefinder.repository.AuctionQueryRepository;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.CategoryRepository;
import com.ezen.valuefinder.repository.ItemRepository;
import com.ezen.valuefinder.repository.MemberRepository;
import com.ezen.valuefinder.entity.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import com.ezen.valuefinder.repository.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final ReviewRepository reviewRepository;
    private final BiddingRepository biddingRepository;
    private final EntityManager entityManager;
    private final SuccessBiddingRepository successBiddingRepository;
    private final AuctionQueryResponseRepository auctionQueryResponseRepository;
    private final AuctionReviewRepository auctionReviewRepository;
    private final AuctionReportRepository auctionReportRepository;
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }
    
    public Auction findById(Long id) {
    	return auctionRepository.findById(id).orElseThrow();
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
        if (normalAuctionFormDto.getAuctionStartTime().isBefore(LocalDateTime.now())
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



    public Long createdQuery(AuctionQueryDto auctionQueryDto, String email , Long auctionNo ) throws Exception {

        Member member = memberRepository.findByEmail(email);
        Auction auction = auctionRepository.findById(auctionNo).orElseThrow();

        AuctionQuery auctionQuery = new AuctionQuery();

        auctionQuery.setAuction(auction);
        auctionQuery.setAuctionQueryDetail(auctionQueryDto.getAuctionQueryDetail());
        auctionQuery.setAuctionQueryTitle(auctionQueryDto.getAuctionQueryTitle());
        auctionQuery.setMember(member);
        auctionQuery.setReadOk(false);


        if (auctionQueryDto.getAuctionQueryDistinction() == 1) {
            auctionQuery.setAuctionQueryDistinction(AuctionQueryDistinction.ITEM);
        } else if (auctionQueryDto.getAuctionQueryDistinction() == 2) {
            auctionQuery.setAuctionQueryDistinction(AuctionQueryDistinction.SHIPPING);
        } else if (auctionQueryDto.getAuctionQueryDistinction() == 3) {
            auctionQuery.setAuctionQueryDistinction(AuctionQueryDistinction.ETC);
        }

        auctionQueryRepository.save(auctionQuery);

        return auctionQuery.getAuctionQueryNo();

    }
    public Long createdQueryResponse(AuctionQueryResponseDto auctionQueryResponseDto, String email , Long auctionQueryNo) throws Exception {
        Member member = memberRepository.findByEmail(email);
        AuctionQuery auctionQuery = auctionQueryRepository.findById(auctionQueryNo).orElseThrow();

        AuctionQueryResponse auctionQueryResponse = new AuctionQueryResponse();
        auctionQueryResponse.setAuctionQuery(auctionQuery);
        auctionQueryResponse.setAuctionQueryResponseTitle(auctionQueryResponseDto.getAuctionQueryResponseTitle());
        auctionQueryResponse.setAuctionQueryResponseDetail(auctionQueryResponseDto.getAuctionQueryResponseDetail());
        auctionQueryResponse.setMember(member);

        auctionQueryResponseRepository.save(auctionQueryResponse);

        return auctionQueryResponse.getAuctionQueryResponseNo();

    }

    public Page<Auction> getTodayViewAuctionList(List<Long> auctionNo, Pageable pageable) {
        return auctionRepository.findByAuctionNoIn(auctionNo,pageable);
    }

    public List<Auction> getItemAuctionList() {
        return auctionRepository.findAll();
    }

    public Long updateQuery(AuctionQueryDto auctionQueryDto, String email , Long auctionQueryNo) throws Exception {

        Member member = memberRepository.findByEmail(email);
        AuctionQuery auctionQuery = auctionQueryRepository.findById(auctionQueryNo).orElseThrow();



        auctionQuery.updateQuery(auctionQueryDto);
        auctionQuery.setMember(member);




        return auctionQuery.getAuctionQueryNo();
    }
    public Page<AuctionQuery> auctionQueryList(Pageable pageable, Member member) {

        return auctionQueryRepository.findByMember(pageable, member);

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
        reverseBidding.setReverseBiddingCount(0);
        reverseBidding.setReversebidAuctionStatus(ReversebidAuctionStatus.PROGRESS);
        reverseBidding.setReverseBiddingCount(0);
        Member member = memberRepository.findByEmail(email);

        reverseBidding.setMember(member);
        reverseBiddingRepository.save(reverseBidding);

        return reverseBidding.getReverseBiddingNo();
    }

    @Transactional
    public AuctionQuery getAuctionDtl(Long auctionQueryNo) {
        return auctionQueryRepository.findById(auctionQueryNo).orElseThrow();
    }

    public Auction getAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        return auction;
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

    public Page<AuctionQueryResponse> auctionQueryResponseList(Pageable pageable, Member member) {
        return auctionQueryResponseRepository.findByMember(pageable, member);
    }

	public int itemCount(Long memberId) {
		return itemRepository.countItemsByMemberId(memberId);	
	}

	public int reviewCount(Long memberId) {
		return reviewRepository.countAuctionReviewsByAuctionItemMember(memberId);	
	}

    public void deleteQuery(Long auctionQueryNo) {
        AuctionQuery auctionQuery = auctionQueryRepository.findById(auctionQueryNo).orElseThrow();

        auctionQueryRepository.delete(auctionQuery);
    }
    
    
    public void deleteAuctionReport(Long auctionNo) {
        Auction auction = auctionRepository.findById(auctionNo).orElseThrow();

        auctionRepository.delete(auction);
    }
	public Page<AuctionReview> getAuctionReviewList(Long memberId, Pageable pageable) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		return reviewRepository.findByAuctionItemMember(member, pageable);
	}


    private void successBidding(Auction auction) {
        Bidding bidding = biddingRepository.findTopByAuctionOrderByBiddingPriceDesc(auction);
        SuccessBidding findSuccessBidding = successBiddingRepository.findByAuction(auction);

        if (bidding != null && findSuccessBidding == null) {
            SuccessBidding successBidding = new SuccessBidding();
            successBidding.setAuction(bidding.getAuction());
            successBidding.setMember(bidding.getMember());
            successBidding.setBidStatus(BidStatus.PENDING);
            successBiddingRepository.save(successBidding);
        }
    }

    private void updateAuctionStatus(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
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

                successBidding(auction);
            }
            if (auction.getAuctionStatus() != AuctionStatus.END && duration.getSeconds() <= 60) { // 남은 시간이 1분 이하일 경우
                auction.setAuctionStatus(AuctionStatus.LAST);
            }
        }

    }

    public void updateAuction(Long auctionId) {

        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        updateAuctionStatus(auction.getAuctionNo());
        updateAuctionReaminTime(auction.getAuctionNo());
    }
    
    
    public List<AuctionReport> getAuctionReportList() {
        return auctionReportRepository.findAll();
    }

    private void updateAuctionReaminTime(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        Duration remainingDuration = Duration.between(LocalDateTime.now(), auction.getAuctionEndTime());
        long hours = remainingDuration.toHours();
        long minutes = remainingDuration.minusHours(hours).toMinutes();
        long seconds = remainingDuration.minusHours(hours).minusMinutes(minutes).getSeconds();


        if(auction.getAuctionStatus() == AuctionStatus.PENDING) {
            auction.setRemainingTime("대기중인 경매입니다.");
            return;
        }
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

    public void addAuctionView(Long id) {
        Auction auction = auctionRepository.findById(id).orElseThrow();
        auction.setAuctionCount(auction.getAuctionCount()+1);
    }


    public Page<ReverseBidding> getReverseAuctionList(Pageable pageable,  Long categoryCode) {
    	if(categoryCode == 0) {
    		return reverseBiddingRepository.findAllByOrderByReverseBiddingExpireDateDesc(pageable);
    	}
    	Category category = categoryRepository.findById(categoryCode).orElseThrow();
    	if(category == null) {
            return reverseBiddingRepository.findAllByOrderByReverseBiddingExpireDateDesc(pageable);
        } else if (category != null) {
            return reverseBiddingRepository.findByCategoryOrderByReverseBiddingExpireDate(pageable,category);
        }
    	
    	return reverseBiddingRepository.findAllByOrderByReverseBiddingExpireDateDesc(pageable);
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

    public Page<MemberMyAuctionDto> getMemberAuctionList(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        return auctionRepository.findAuctionsByMemberId(memberId, pageable);
    }
    
    public Page<Auction> getDetailPageAuctionList(Member member, Pageable pageable) {
        return auctionRepository.findByItemMemberOrderByAuctionEndTimeDesc(member, pageable);
    }

    public Page<Auction> getSearchList(Pageable pageable,Long categoryCode) {
        if(categoryCode==0) {
            return auctionRepository.findAllByOrderByAuctionEndTimeDescAuctionCountDesc(pageable);
        }
        Category category = categoryRepository.findById(categoryCode).orElseThrow();
        if(category == null) {
            return auctionRepository.findAllByOrderByAuctionEndTimeDescAuctionCountDesc(pageable);
        } else {
            return auctionRepository.findByItemCategoryOrderByAuctionEndTimeDesc(category,pageable);
        }

    }

    public Page<Auction> getSearchValList(Pageable pageable, Long categoryCode, String searchVal) {
        if (categoryCode == 0) {
            return auctionRepository.findByItemItemNameContainingOrderByAuctionEndTimeDesc(pageable,searchVal);
        }
        Category category = categoryRepository.findById(categoryCode).orElseThrow();

        return auctionRepository.findByItemItemNameContainingAndItemCategoryOrderByAuctionEndTimeDesc(pageable,searchVal,category);
    }

    public Integer getBiddingCount(Auction auction) {
        return biddingRepository.countByAuction(auction);
    }

    public Page<Auction> getPopularList(Pageable pageable) {
        return auctionRepository.findAllByOrderByAuctionEndTimeDescAuctionCountDesc(pageable);
    }

    public Page<Auction> getLastList(Pageable pageable) {
        return auctionRepository.findActiveAuctionsOrderByTimeLeft(pageable,AuctionStatus.END);
    }

    public Page<Auction> getNewList(Pageable pageable) {
        return auctionRepository.findAllByOrderByRegTime(pageable);
    }

    public void addAuctionReview(ReviewFormDto reviewFormDto, Long id, Member member) {
        Auction auction = auctionRepository.findById(id).orElseThrow();
        AuctionReview auctionReview = new AuctionReview();
        auctionReview.setAuction(auction);
        auctionReview.setAuctionReviewDetail(reviewFormDto.getAuctionReviewDetail());
        auctionReview.setAuctionReviewTitle(reviewFormDto.getAuctionReviewTitle());
        auctionReview.setAuctionReviewScore(reviewFormDto.getAuctionReviewSocre().longValue());
        auctionReview.setMember(member);
        auctionReviewRepository.save(auctionReview);
    }

    public boolean chkAuctionReview(Long id) {
        AuctionReview auctionReview = auctionReviewRepository.findByAuction(auctionRepository.findByAuctionNo(id));

        if (auctionReview != null) {
            return false;
        } else {
            return true;
        }

    }
}
