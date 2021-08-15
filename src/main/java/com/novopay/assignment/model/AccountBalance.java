package com.novopay.assignment.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.novopay.assignment.exception.InsufficientBalanceException;

@Entity
public class AccountBalance {

	@Id
	private int userId;
	private double currentBalance;



	public int getUserId() {
		return userId;
	}
	
	public AccountBalance() {
		
	}

	public AccountBalance(int userId) {

		this.userId = userId;
		this.currentBalance = 0;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public double creditAmount(double amount) {
		currentBalance += amount;
		return currentBalance;
	}

	public double debitAmount(double amount) {
		if (currentBalance < amount)
			throw new InsufficientBalanceException("Insufficient balance in account for this transaction.");
		currentBalance -= amount;
		return currentBalance;
	}

}
