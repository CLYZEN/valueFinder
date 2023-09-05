package com.ezen.valuefinder.service;


import com.ezen.valuefinder.constant.AuctionStatus;

import com.ezen.valuefinder.constant.ReversebidAuctionStatus;
import com.ezen.valuefinder.dto.ReversebidEnterDto;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.ItemImgRepository;
import com.ezen.valuefinder.repository.ItemRepository;
import com.ezen.valuefinder.repository.ReverseBiddingJoinRepository;
import com.ezen.valuefinder.repository.ReverseBiddingRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReversebidService {
    private final ReverseBiddingRepository reverseBiddingRepository;
    private final ReverseBiddingJoinRepository reverseBiddingJoinRepository;
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    public ReverseBidding getReversebidById(Long id) {
        return reverseBiddingRepository.findById(id).orElseThrow();
    }


    @Transactional(readOnly = true)
    public ReversebidEnterDto getReversebidDtl(Long reverseBiddingJoinNo) {
    	ReverseBiddingJoin reverseBiddingJoin = reverseBiddingJoinRepository.findById(reverseBiddingJoinNo).orElseThrow(EntityNotFoundException::new);
    	
    	ReversebidEnterDto reversebidEnterDto = ReversebidEnterDto.of(reverseBiddingJoin);
    	
    	return reversebidEnterDto;
    }
   
    @Transactional
    public Long updateReverseBiddingJoin(ReversebidEnterDto reversebidEnterDto, List<MultipartFile> itemImgFiles) throws Exception {
	
        ReverseBiddingJoin reverseBiddingJoin = reverseBiddingJoinRepository.findById(reversebidEnterDto.getReverseBiddingJoinNo()).orElseThrow(EntityNotFoundException::new);       
        // 엔티티를 업데이트합니다.
        reverseBiddingJoin.updateReverseBiddingJoin(reversebidEnterDto);
        // 변경된 엔티티를 저장합니다.
        reverseBiddingJoinRepository.save(reverseBiddingJoin);      
        
        
        return reverseBiddingJoin.getReverseBiddingJoinNo();
    }
    
    

    
    
    public void saveReversebidEnter(ReversebidEnterDto reversebidEnterDto, Member member, Long bidno, List<MultipartFile> itemImgFiles) throws Exception {
        ReverseBidding reverseBidding = reverseBiddingRepository.findById(bidno).orElseThrow();

        Item item = new Item();
        item.setMember(member);
        item.setCategory(reverseBidding.getCategory());
        itemRepository.save(item);

        ReverseBiddingJoin reverseBiddingJoin = new ReverseBiddingJoin();
        reverseBiddingJoin.setMember(member);
        reverseBiddingJoin.setReverseBidding(reverseBidding);
        reverseBiddingJoin.setItem(item);

        reverseBiddingJoinRepository.save(reverseBiddingJoin);

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
    }
    
    public void addReverseBiddingView(Long id) {
        ReverseBidding reverseBidding= reverseBiddingRepository.findById(id).orElseThrow();
        reverseBidding.setReverseBiddingCount(reverseBidding.getReverseBiddingCount()+1);
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
    
    public void deleteReverseBiddingJoin(Long bidno) {
    	ReverseBiddingJoin reverseBiddingJoin = reverseBiddingJoinRepository.findById(bidno).orElseThrow();
    	
    	reverseBiddingJoinRepository.delete(reverseBiddingJoin);
    }

    public Page<ReverseBiddingJoin> getReverseJoinList(Pageable pageable, Long reverseBiddingJoinNo) {
    	return reverseBiddingJoinRepository.findByReverseBiddingReverseBiddingNoOrderByReverseBiddingJoinNo(pageable, reverseBiddingJoinNo);
    }
    
    public void updateAuctionStatusToEnd(Long reverseBiddingNo) {

        ReverseBidding reverseBidding = reverseBiddingRepository.findById(reverseBiddingNo).orElse(null);
        if (!reverseBidding.getReversebidAuctionStatus().equals(ReversebidAuctionStatus.END)) {
            if (reverseBidding.getReverseBiddingExpireDate().isBefore(LocalDateTime.now())) {
            	reverseBidding.setReversebidAuctionStatus(ReversebidAuctionStatus.END);;
                reverseBiddingRepository.save(reverseBidding);
            }
        }
    }
}
