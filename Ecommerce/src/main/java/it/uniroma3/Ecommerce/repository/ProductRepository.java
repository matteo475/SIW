package it.uniroma3.Ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import it.uniroma3.Ecommerce.model.Product;

/*le repository ci aiutano a leggere e inserire i dati nel database*/
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	public List<Product> findByName(String name);
	public Long countById(Long id);
	
	//qui implementeremo le query per cercare i prodotti con una keyword
	
}
