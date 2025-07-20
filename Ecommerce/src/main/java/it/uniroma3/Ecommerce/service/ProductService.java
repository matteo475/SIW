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
	
	/**
	 * metodo per vedere tutti i prodotti 
	 * @return la lista contenente tutti i prodotti 
	 **/
	public List<Product> listAll(){
		return (List<Product>) productRepository.findAll();
	}
	
	/**
	 * metodo per salvare il prodotto 
	 * @param prodotto da salvare 
	 * @return il prodotto viene salvato grazie alla Repository 
	 **/
	public void save(Product product) {
		productRepository.save(product);
	}
	
	
	/**
	 * metodo che mi permette di avere i prodotti in base al loro id 
	 * @param id del prodotto che voglio
	 * @exception ProductNotFoundException se il prodotto cercato non esiste
	 * */
	public Product get(Integer id) throws ProductNotFoundException{
		Optional<Product> result = productRepository.findById(id);
		if(result.isPresent()) {
			return result.get();		
		}
		throw new ProductNotFoundException("Non troviamo nessun prodotto con ID " + id);	//gestione errore
	}
	
	/**
	 * metodo per avere tutti i prodotti dato una certa keyword
	 * @param keyword per cercare il prodotto
	 * @return lista con nome keyword
	 * */
	public List<Product> listAll(String keyword){
		 if(keyword != null) {
			 return this.productRepository.findAllWithThatKeyword(keyword);
		 }
		 return this.listAll();
	}
	
	/**
	 * metodo per contare tutti i prodotti dell'azienda
	 * @param id del prodotto
	 * */
	public int numeroProdotti(Integer id) {
		return this.productRepository.countById(id);
	}

}
