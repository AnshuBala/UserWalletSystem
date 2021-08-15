package com.novopay.assignment.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Transaction {
	@Id
	@GeneratedValue
	private int transactionId;
	private int fromUserId;
	private int toUserId;
	private TransactionStatus tranStatus;

	private double chargeAmount;

	public double getChargeAmount() {
		return chargeAmount;
	}

	public double getCommisionAmount() {
		return commisionAmount;
	}

	private double commisionAmount;
	private boolean chargeCommDeductedFromSource = true;

	public TransactionStatus getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(TransactionStatus tranStatus) {
		this.tranStatus = tranStatus;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public void setCommisionAmount(double commisionAmount) {
		this.commisionAmount = commisionAmount;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	private double amount;

	public Transaction(int toUserId, int fromUserId, double amount, boolean chargeCommDeductedFromSource) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.amount = amount;
		this.chargeCommDeductedFromSource = chargeCommDeductedFromSource;

	}

	public Transaction() {

	}

	public Transaction(int toUserId, int fromUserId, double amount) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.amount = amount;
	}

	public Transaction(int toUserId, double amount) {
		this.toUserId = toUserId;
		this.amount = amount;
	}

	public Transaction(int toUserId) {
		this.toUserId = toUserId;

	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public int getToUserId() {
		return toUserId;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public boolean isChargeCommDeductedFromSource() {
		return chargeCommDeductedFromSource;
	}

}
