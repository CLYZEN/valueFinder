package com.ezen.valuefinder.service;

import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.CategoryRepository;
import com.ezen.valuefinder.repository.ItemRepository;
import com.ezen.valuefinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
}
