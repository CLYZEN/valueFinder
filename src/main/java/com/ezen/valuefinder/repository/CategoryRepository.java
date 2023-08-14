package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
