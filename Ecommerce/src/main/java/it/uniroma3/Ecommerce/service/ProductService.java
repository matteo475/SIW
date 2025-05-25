package it.uniroma3.Ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import it.uniroma3.Ecommerce.authentication.ProductNotFoundException;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.repository.ProductRepository;



@Service
public class ProductService {
	
	@Autowired 
	private ProductRepository productRepository;
	
	//funzione che mi ritorna la lista di tutti i prodotti
	public List<Product> listAll(){
		return (List<Product>) productRepository.findAll();
	}
	
	//funzione per salvare il prodotto 
	public void save(Product product) {
		productRepository.save(product);
	}
	
	
	//funzione che mi fa vedere tutti i prodotti per id
	public Product get(Integer id) throws ProductNotFoundException{
		Optional<Product> result = productRepository.findById(id);
		if(result.isPresent()) {
			return result.get();		
		}
		throw new ProductNotFoundException("Non troviamo nessun prodotto con ID " + id);	//gestione errore
	}
	
	//funzione per cancellare il prodotto 
	/*
	public void delete(Long id) throws ProductNotFoundException{
		Long count = productRepository.countById(id); 
		if(count == null || count == 0) {
			throw new ProductNotFoundException("Non troviamo nessun prodotto con ID " + id);	//gestione errore
		}
		productRepository.deleteById(id);
	}
	*/


}

