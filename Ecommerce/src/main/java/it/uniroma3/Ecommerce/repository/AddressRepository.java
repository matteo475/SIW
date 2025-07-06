package it.uniroma3.Ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.Ecommerce.model.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{
	List<Address> findByUserId(Long userId);
}
