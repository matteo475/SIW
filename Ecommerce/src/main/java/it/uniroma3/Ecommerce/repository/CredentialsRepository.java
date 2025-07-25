package it.uniroma3.Ecommerce.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.Ecommerce.model.Credentials;


public interface CredentialsRepository extends CrudRepository<Credentials,Long>{
	
	public Optional<Credentials> findByUsername(String username);
	
	public Optional<Credentials> findByResetPasswordToken(String passwordToken);
	
	

	
}
