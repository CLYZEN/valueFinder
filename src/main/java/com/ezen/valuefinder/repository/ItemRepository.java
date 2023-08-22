package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
	
}
