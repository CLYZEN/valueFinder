package com.ezen.valuefinder.service;

import com.ezen.valuefinder.entity.AuctionQuery;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.AuctionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionQueryService {
    private final AuctionQueryRepository auctionQueryRepository;

    public Page<AuctionQuery> getAuctionQueryPage(Pageable pageable, Member member){
        return auctionQueryRepository.findByMember(pageable,member);
    }

    public Page<AuctionQuery> getReceiveQueryPage(Pageable pageable, Member member) {
        return auctionQueryRepository.findByAuctionItemMember(pageable,member);
    }
}
