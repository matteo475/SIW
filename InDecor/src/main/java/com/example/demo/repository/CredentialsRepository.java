package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials,Long>{
    
	public Optional<Credentials> findByUsername(String Username);
	
}
