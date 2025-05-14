package it.uniroma3.Ecommerce.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.Ecommerce.model.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
}
