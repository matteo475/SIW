package it.uniroma3.Ecommerce.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.Ecommerce.model.Company;


public interface CompanyRepository extends CrudRepository <Company, Long>{
	
	
}
