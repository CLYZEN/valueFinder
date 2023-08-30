package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
