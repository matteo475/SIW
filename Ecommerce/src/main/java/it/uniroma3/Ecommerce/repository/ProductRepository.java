package it.uniroma3.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import it.uniroma3.Ecommerce.model.Product;

/*le repository ci aiutano a leggere e inserire i dati nel database*/
public interface ProductRepository extends JpaRepository<Product, Integer>{

	public List<Product> findByName(String name);
	public Long countById(Long id);
	public Optional<Product> findById(Long id);

	//qui implementeremo le query per cercare i prodotti con una keyword

	@Query("SELECT p FROM Product p WHERE " +"(:name IS NULL OR :name = '' OR p.name = :name) AND " +"(:brand IS NULL OR :brand = '' OR p.brand = :brand) AND " + "(:category IS NULL OR :category = '' OR p.category = :category)")

	List<Product> searchProducts(@Param("name") String name, @Param("brand") String brand,@Param("category") String category);



	/*Query per trovare una lista di prodotti con nome simile a questa stringa*/
	
	@Query("select p from Product p where CONCAT(p.name,'', p.brand,'',p.id) LIKE %?1%")
	public List<Product> findAllWithThatKeyword(String keyword);


}
