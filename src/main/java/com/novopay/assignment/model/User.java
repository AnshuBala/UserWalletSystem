package com.novopay.assignment.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {
	@Id
	private int userId;
	private String name;
	private String password;

	public User(int id, String name, String password) {
		this.userId = id;
		this.name = name;
		this.password = password;
	}

	public User() {

	}

	public int getId() {
		return userId;
	}

	public void setId(int id) {
		this.userId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
