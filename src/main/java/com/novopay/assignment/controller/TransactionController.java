package com.novopay.assignment.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.novopay.assignment.dao.AccountBalanceRepository;
import com.novopay.assignment.dao.TransactionRepository;
import com.novopay.assignment.dao.UserRepository;
import com.novopay.assignment.exception.ResourceNotFoundException;
import com.novopay.assignment.exception.TransactionFailedException;
import com.novopay.assignment.model.AccountBalance;
import com.novopay.assignment.model.Transaction;
import com.novopay.assignment.model.TransactionStatus;

@RestController
public class TransactionController {

	private final double charge = 0.2;
	private final double commission = 0.05;

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountBalanceRepository accBalRepo;

	@Autowired
	private UserRepository userRepo;

	/**
	 * This method is used to save transaction
	 * 
	 * @param transaction
	 * @return message
	 */
	@PostMapping("/saveTransaction")
	public String saveTransaction(@RequestBody Transaction transaction) {
		int sourceUserid = transaction.getFromUserId();
		int targetUserid = transaction.getToUserId();
		if (userRepo.findById(sourceUserid) == null) {
			throw new ResourceNotFoundException("Invalid Source user.");
		}
		if (userRepo.findById(targetUserid) == null) {
			throw new ResourceNotFoundException("Invalid Target user.");
		}
		transaction.setTranStatus(TransactionStatus.PENDING);
		try {
			Transaction updatedTrans = transactionRepo.save(transaction);

			AccountBalance sourceAccBal = accBalRepo.findByUserId(sourceUserid);
			AccountBalance targetAccBal = accBalRepo.findByUserId(targetUserid);

			double amount = transaction.getAmount();
			double commissionAmount = (commission * amount) / 100;
			double chargeAmount = (charge * amount) / 100;
			double debitAmount = amount;
			double creditAmount = amount;
			if (transaction.isChargeCommDeductedFromSource()) {
				debitAmount = amount + commissionAmount + chargeAmount;
			} else {
				creditAmount = amount - commissionAmount - chargeAmount;
			}

			sourceAccBal.debitAmount(debitAmount);
			targetAccBal.creditAmount(creditAmount);
			transaction.setChargeAmount(chargeAmount);
			transaction.setCommisionAmount(commissionAmount);
			accBalRepo.save(sourceAccBal);
			accBalRepo.save(targetAccBal);
			transaction.setTranStatus(TransactionStatus.SUCCESS);
			transactionRepo.save(updatedTrans);
			return "Transaction saved. Transaction id is: " + updatedTrans.getTransactionId();
		} catch (Exception e) {
			transaction.setTranStatus(TransactionStatus.FAILED);
			throw new TransactionFailedException(
					"Transaction failed. Transaction id is: " + transaction.getTransactionId());
		}

	}

	/**
	 * This method is used to add money to wallet. The charge and commission amounts
	 * are calculated to be recorded in DB but are not deducted from the wallet on
	 * the assumption that these amounts would be deducted from the bank account or
	 * source.
	 * 
	 * @param transaction
	 * @return message
	 */
	@PostMapping("/addMoney")
	public String addMoneyToWallet(@RequestBody Transaction targetTransaction) {
		int targetUserid = targetTransaction.getToUserId();

		// Transaction targetTransaction =
		// transactionRepo.findByTransactionId(targetUserid);
		if (userRepo.findById(targetUserid) == null) {
			throw new ResourceNotFoundException("User is not registered.");
		}

		targetTransaction.setTranStatus(TransactionStatus.PENDING);

		try {
			transactionRepo.save(targetTransaction);

			AccountBalance targetAccBal = accBalRepo.findByUserId(targetUserid);

			double amount = targetTransaction.getAmount();
			targetAccBal.creditAmount(amount);
			double commissionAmount = (commission * amount) / 100;
			double chargeAmount = (charge * amount) / 100;
			targetTransaction.setChargeAmount(chargeAmount);
			targetTransaction.setCommisionAmount(commissionAmount);
			accBalRepo.save(targetAccBal);
			targetTransaction.setTranStatus(TransactionStatus.SUCCESS);
			transactionRepo.save(targetTransaction);
			return "Added money to wallet...";
		} catch (Exception e) {
			targetTransaction.setTranStatus(TransactionStatus.FAILED);
			throw new TransactionFailedException(
					"Transaction failed. Transaction id is: " + targetTransaction.getTransactionId());
		}

	}

	/**
	 * This method is used to reverse transaction for amount transferred between two
	 * accounts. It is not applicable for 'addMoney'
	 * 
	 * @param origTransaction
	 * @return message
	 */
	@PostMapping("/reverseTransaction")
	public String reverseTransaction(@RequestBody Transaction transaction) {
		int transId = transaction.getTransactionId();

		Transaction origTransaction = transactionRepo.findByTransactionId(transId);
		if (origTransaction != null) {
			if (origTransaction.getTranStatus() == TransactionStatus.SUCCESS) {
				try {
					int targetUserId = origTransaction.getToUserId();
					int sourceUserId = origTransaction.getFromUserId();
					double amount = origTransaction.getAmount();
					double chargeAmount = origTransaction.getChargeAmount();
					double commissionAmount = origTransaction.getCommisionAmount();
					double debitAmount = amount;
					double creditAmount = amount;
					if (origTransaction.isChargeCommDeductedFromSource())
						creditAmount = amount + chargeAmount + commissionAmount;
					else
						debitAmount = amount - (chargeAmount + commissionAmount);
					AccountBalance sourceAccBal = accBalRepo.findByUserId(sourceUserId);
					AccountBalance targetAccBal = accBalRepo.findByUserId(targetUserId);
					sourceAccBal.creditAmount(creditAmount);
					targetAccBal.debitAmount(debitAmount);
					accBalRepo.save(sourceAccBal);
					accBalRepo.save(targetAccBal);
					transaction.setTranStatus(TransactionStatus.REVERSED);
					transactionRepo.save(origTransaction);
					return "Transaction successfully reversed. Transaction id is: " + origTransaction.getTransactionId();
				} catch (Exception e) {
					// Rolling back transaction status to 'SUCCESS' as the reverse operation failed.
					// To avoid complexity of code, updating the account balance has been skipped in
					// case of failure of reversal operation.
					transaction.setTranStatus(TransactionStatus.SUCCESS);
					throw new TransactionFailedException(
							"Transaction failed. Transaction id is: " + origTransaction.getTransactionId());
				}

			} else {
				throw new TransactionFailedException("Invalid Transaction");
			}
		} else {
			throw new ResourceNotFoundException("Transaction ID is invalid.");
		}

	}

	/**
	 * This method is used to get transaction by passing the transaction id
	 * 
	 * @param transactionId
	 * @return transaction
	 */
	@GetMapping("/getTransaction/{id}")
	public Transaction getTransactionById(@PathVariable int transactionId) {
		return transactionRepo.getById(transactionId);
	}

	/**
	 * This method is used to get all transactions
	 * 
	 * @return all transactions
	 */
	@GetMapping("/getAllTransactions")
	public List<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}

	/**
	 * This method is used to get transaction status using transaction id
	 * 
	 * @param transactionId
	 * @return transaction status
	 */
	@GetMapping("/getTransactionStatus/{transactionId}")
	public TransactionStatus getTransactionStatus(@PathVariable int transactionId) {
		Transaction result = transactionRepo.findByTransactionId(transactionId);
		if (result == null)
			throw new ResourceNotFoundException("Transaction id " + transactionId + " does not exist.");

		else {
			return result.getTranStatus();
		}

	}

	/**
	 * This method is used to view passbook for given user id.
	 * 
	 * @param userId
	 * @return passbook
	 */
	@GetMapping("/viewPassbook/{userId}")
	public List<Transaction> viewPassbookForUser(@PathVariable int userId) {
		List<Transaction> creditList = transactionRepo.findByToUserId(userId);
		List<Transaction> debitList = transactionRepo.findByFromUserId(userId);

		List<Transaction> passBook = new ArrayList<>();
		passBook.addAll(creditList);
		passBook.addAll(debitList);

		Collections.sort(passBook, (left, right) -> left.getTransactionId() - right.getTransactionId());
		return passBook;
	}

}
