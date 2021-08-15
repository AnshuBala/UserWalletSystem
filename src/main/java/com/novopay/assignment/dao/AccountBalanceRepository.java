package com.novopay.assignment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novopay.assignment.model.AccountBalance;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Integer>{

	AccountBalance findByUserId(int fromUserId);

}
