package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
   
	@Autowired private UserRepository userRepo;
	
	 public User saveUser(User user) {
		 return this.userRepo.save(user);
	 }
	 
	 public User getUser(Long id) {
		 return this.userRepo.findById(id).get();
	 }
}
