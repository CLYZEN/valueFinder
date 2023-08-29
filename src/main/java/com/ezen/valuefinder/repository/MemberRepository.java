package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);

    Member findByEmailAndPhone(String email, String phone);
    
  
    
    
}
