package com.ezen.valuefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.valuefinder.entity.Wish;

public interface PostLikeRepository extends JpaRepository<Wish,Long>{
	Wish findByPost
}
