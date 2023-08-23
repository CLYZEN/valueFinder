package com.ezen.valuefinder.service;

import com.ezen.valuefinder.dto.ReversebidEnterDto;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.ItemRepository;
import com.ezen.valuefinder.repository.ReverseBiddingJoinRepository;
import com.ezen.valuefinder.repository.ReverseBiddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public ReverseBidding getReversebidById(Long id) {
        return reverseBiddingRepository.findById(id).orElseThrow();
    }

    public void saveReversebidEnter(ReversebidEnterDto reversebidEnterDto, Member member, Long bidno, List<MultipartFile> itemImgFiles) throws Exception {
        ReverseBidding reverseBidding = reverseBiddingRepository.findById(bidno).orElseThrow();

        Item item = new Item();
        item.setMember(member);
        item.setItemDetail(reversebidEnterDto.getDetail());
        item.setItemName(reversebidEnterDto.getTitle());
        item.setCategory(reverseBidding.getCategory());
        itemRepository.save(item);

        ReverseBiddingJoin reverseBiddingJoin = new ReverseBiddingJoin();
        reverseBiddingJoin.setMember(member);
        reverseBiddingJoin.setSuggestPrice(reversebidEnterDto.getPrice());
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
}