package it.uniroma3.Ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.Ecommerce.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
}
