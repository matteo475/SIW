package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Credentials;
import com.example.demo.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired private CredentialsRepository credentialsRepo;

	public Credentials getCredenziali(Long id) {
		return this.credentialsRepo.findById(id).get();
	}

	public Credentials getCredenziali(String username) {
		Optional<Credentials> credenziali = this.credentialsRepo.findByUsername(username);
		return credenziali.orElseThrow();
	}
	
	public Credentials saveCredenziali(Credentials credentials) {
		return this.credentialsRepo.save(credentials);
		
	}

}
