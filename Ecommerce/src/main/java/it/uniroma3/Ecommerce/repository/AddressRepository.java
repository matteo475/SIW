package it.uniroma3.Ecommerce.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.Ecommerce.model.Address;

public interface AddressRepository extends CrudRepository<Address,Long>{
}
