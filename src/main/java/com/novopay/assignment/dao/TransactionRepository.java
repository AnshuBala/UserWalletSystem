package com.novopay.assignment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novopay.assignment.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	List<Transaction> findByToUserId(int id);

	List<Transaction> findByFromUserId(int id);

	Transaction findByTransactionId(int transactionId);

}
