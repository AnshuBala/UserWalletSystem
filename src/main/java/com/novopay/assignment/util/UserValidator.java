package com.novopay.assignment.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.novopay.assignment.dao.UserRepository;

public class UserValidator {
	
	@Autowired
	private UserRepository userRepo;
	
	public boolean validateUser(int userId) {
		return (userRepo.findById(userId) != null);
	}

}
