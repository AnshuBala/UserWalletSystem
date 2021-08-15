package com.novopay.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.novopay.assignment.dao.AccountBalanceRepository;
import com.novopay.assignment.model.AccountBalance;

@RestController
public class AccountBalanceController {
	
	@Autowired
	private AccountBalanceRepository accBalRepo;
	
	@GetMapping("/accountBalance/{userId}")
	public AccountBalance getAccountBalanceForUser(@PathVariable int userId) {
		return accBalRepo.findByUserId(userId);
	}

}
