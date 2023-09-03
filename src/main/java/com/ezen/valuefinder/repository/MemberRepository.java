package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.constant.Status;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.SuccessBidding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);

    Member findByEmailAndPhone(String email, String phone);

    List<Member> findByStatus(Status status);

}
