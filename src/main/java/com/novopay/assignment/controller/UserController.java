package com.novopay.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.novopay.assignment.dao.AccountBalanceRepository;
import com.novopay.assignment.dao.UserRepository;
import com.novopay.assignment.model.AccountBalance;
import com.novopay.assignment.model.User;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AccountBalanceRepository accBalRepo;

	@PostMapping("/saveUser")
	public String saveUser(@RequestBody User user) {
		userRepo.save(user);
		accBalRepo.save(new AccountBalance(user.getId()));
		return "User saved...";
	}
	
	@GetMapping("/getAllUsers")
	public List<User> getAllUsers(){
		return userRepo.findAll();
		
	}
	
	@GetMapping("/getUser/{userId}")
	public User getUserById(@PathVariable int userId) {
		return userRepo.findByUserId(userId);
	}

}
