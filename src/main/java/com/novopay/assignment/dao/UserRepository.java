package com.novopay.assignment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novopay.assignment.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUserId(int userId);

}
