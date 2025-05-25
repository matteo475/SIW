package it.uniroma3.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.Ecommerce.model.Product;

/*le repository ci aiutano a leggere e inserire i dati nel database*/
public interface ProductRepository extends JpaRepository<Product, Integer>{

	public List<Product> findByName(String name);
	public Long countById(Long id);
	public Optional<Product> findById(Long id);

	/*Query per trovare una lista di prodotti con nome simile a questa stringa*/
	@Query("select p from Product p where " 
			+ " CONCAT(p.name,'',p.brand,'',p.id)" 
			+ "like %?1")
	public List<Product> findAllWithThatKeyword(String keyword);

}
