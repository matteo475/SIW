package it.uniroma3.Ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.Ecommerce.authentication.ProductNotFoundException;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.repository.ProductRepository;
import it.uniroma3.Ecommerce.service.ProductService;

@Controller
public class MainController {
	
	/*in questo controller vado a mettere quelle richieste che sono globali ovvero che non sono specifiche di una sola entità*/

	@Autowired
	ProductService productService;
	
	@Autowired 
	ProductRepository productRepository;
	
	//mapping  che permette di avere tutti i prodotti
	@GetMapping("/")
	public String index(Model model) {
		List<Product> products = productRepository.findAll();	//per visualizzare la lista
		//dei prodotti ordinata per id, in modo che i prodotti più recenti siano in alto
		if(products.isEmpty()) {
			return "redirect:/login.html"; 
		}
	
		model.addAttribute("products", products); 
		return "index.html"; 
	}
	
	
	 
	
	@GetMapping("/index")
	public String showHomepage() {
		return "index.html";
	}
	
	//visualizzazione del singolo prodotto
	 @GetMapping("/prodotto/{id}")
		public String visualizza_prodotto(@PathVariable("id") Integer id, Model model) throws ProductNotFoundException {
			Product product=this.productService.get(id);
			
			model.addAttribute("product",product );
		return "viewproduct.html";
			}
}

