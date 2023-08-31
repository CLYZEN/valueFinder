package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.MemberOut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberOutRepository extends JpaRepository<MemberOut, Long> {
	MemberOut findByMember(Member member);
}
