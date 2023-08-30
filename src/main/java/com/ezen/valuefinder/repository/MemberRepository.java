package com.ezen.valuefinder.repository;


import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.constant.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;




public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);

    Member findByEmailAndPhone(String email, String phone);
    
    List<Member> findByStatus(Status status);



	

}
