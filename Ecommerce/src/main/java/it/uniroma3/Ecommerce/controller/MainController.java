package it.uniroma3.Ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.Ecommerce.authentication.ProductNotFoundException;
import it.uniroma3.Ecommerce.model.*;
import it.uniroma3.Ecommerce.repository.ProductRepository;
import it.uniroma3.Ecommerce.repository.UserRepository;
import it.uniroma3.Ecommerce.service.ProductService;
import it.uniroma3.Ecommerce.service.UserService;

@Controller
public class MainController {

	/*in questo controller vado a mettere quelle richieste che sono globali ovvero che non sono specifiche di una sola entità*/

	@Autowired
	ProductService productService;

	@Autowired 
	ProductRepository productRepository;

	@Autowired 
	UserRepository userRepository;

	@Autowired
	UserService userService;

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
	public String showHomepage(Model model) {
		List<Product> products = productRepository.findAll();	//per visualizzare la lista
		//dei prodotti ordinata per id, in modo che i prodotti più recenti siano in alto
		if(products.isEmpty()) {
			return "redirect:/login.html"; 
		}

		model.addAttribute("products", products); 
		return "index.html"; 
	}

	//visualizzazione del singolo prodotto
	@GetMapping("/prodotto/{id}")
	public String visualizza_prodotto(@PathVariable("id") Integer id, Model model) throws ProductNotFoundException {
		Product product=this.productService.get(id);	
		model.addAttribute("product",product);
		return "viewproduct.html";
	}

	//visualizzazione profilo utente
	@GetMapping("/userProfile/{id}")
	public String showUserProfile(@PathVariable("id") Long id, Model model) {
		User user = this.userService.getUser(id);
		model.addAttribute("user",user);
		return "userProfile";
	}
	//ricerca filtrata

	@PostMapping("/ricercaconfiltro")
	public String ricerca_con_filtro( Model model, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "brand", required = false) String brand,@RequestParam(name = "category", required = false) String category) {

		List<Product> products = productRepository.searchProducts(name, brand, category);
		model.addAttribute("products", products);
		return "prodottofiltrato.html";
	}
	//pagina di ricerca filtrata
	@GetMapping("/ricercafiltro")
	public String ricercaf(Model mode) {

		return "ricercafiltro";
	}

	@PostMapping("/ricercaHome")
	public String ricercaBarraHome(Model model, @Param("keyword") String keyword) {
		List<Product> products = this.productService.listAll(keyword);
		model.addAttribute("products", products);
		model.addAttribute("keyword", keyword);
		return "prodottoFiltrato.html";
	}
}

