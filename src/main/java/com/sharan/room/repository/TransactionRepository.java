package com.sharan.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharan.room.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	public Transaction findById(Long id);

}
