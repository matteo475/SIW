package it.uniroma3.Ecommerce.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.Ecommerce.authentication.ProductNotFoundException;
import it.uniroma3.Ecommerce.authentication.SessionData;
import it.uniroma3.Ecommerce.model.*;
import it.uniroma3.Ecommerce.repository.ProductRepository;
import it.uniroma3.Ecommerce.repository.UserRepository;
import it.uniroma3.Ecommerce.service.CarrelloService;
import it.uniroma3.Ecommerce.service.CredentialsService;
import it.uniroma3.Ecommerce.service.ProductService;
import it.uniroma3.Ecommerce.service.UserService;

@Controller
public class MainController {

	@Autowired
	ProductService productService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	CarrelloService carrelloService;

	@Autowired
	private SessionData sessionData;

	
	/**
	 * metodo di default che di permette di visualizzare la HomePage
	 * @return index.html (HomePage del sito)
	 **/
	@GetMapping("/")
	public String index(Model model) {

		// per visualizzare la lista dei prodotti ordinata per id, in modo che i
		// prodotti più recenti siano in alto
		List<Product> products = productRepository.findAll();

		if (products.isEmpty()) {
			return "redirect:/login.html";
		}

		model.addAttribute("products", products);
		return "index.html";
	}

	/**
	 * metodo che di permette di visualizzare la HomePage
	 * 
	 * @return index.html (HomePage del sito)
	 **/
	@GetMapping("/index")
	public String showHomepage(Model model) {
		List<Product> products = productRepository.findAll(); // per visualizzare la lista
		// dei prodotti ordinata per id, in modo che i prodotti più recenti siano in
		// alto
		if (products.isEmpty()) {
			return "redirect:/login.html";
		}
		model.addAttribute("userDetails", this.sessionData.getUserDetails());
		model.addAttribute("products", products);
		return "index.html";
	}

	/**
	 * metodo che ci permette di visualizzare il singolo prodotto situato nella home
	 * page
	 * 
	 * @param id del prodotto
	 * @return pagina del prodotto inserito relativo al suo id
	 **/
	@GetMapping("/prodotto/{id}")
	public String visualizza_prodotto(@PathVariable("id") Integer id, Model model) throws ProductNotFoundException {
		Product product = this.productService.get(id);
		int quantita = this.productService.numeroProdotti(id);
		model.addAttribute("product", product);
		model.addAttribute("disponibilita", quantita);
		return "viewproduct.html";
	}

	@GetMapping("/profile")
	public String showMyProfile(Authentication authentication, Model model) {

		// verifico se l'utente è autenticato, altrimenti vado al login
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/login";
		}

		// ricava lo username dal Principal (qualsiasi tipo sia)
		Object principal = authentication.getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			// fallback: usa toString() o gestisci come errore
			username = principal.toString();
		}

		// recupera sempre le credentials dal repository
		Credentials credentials = credentialsService.getCredentials(username);
		if (credentials == null) {
			return "redirect:/login?error";
		}

		// uso credentials.getUser() solo se non null
		User user = credentials.getUser();

		// verifico se esiste l'utente
		if (user == null) {
			return "redirect:/login?error";
		}

		model.addAttribute("user", user);
		return "userProfile";
	}

	/**
	 * metodo che ci permette di vedere la pagina per effettuare la ricerca con
	 * criteri vari
	 * 
	 * @return la pagina ricercafiltro.html
	 **/
	@GetMapping("/ricercafiltro")
	public String ricercaf(Model model) {
		model.addAttribute("userDetails", this.sessionData.getUserDetails());
		return "ricercafiltro";
	}

	/**
	 * metodo che permette di fare la ricerca con vari filtri
	 */
	@PostMapping("/ricercaconfiltro")
	public String ricerca_con_filtro(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "brand", required = false) String brand,
			@RequestParam(name = "category", required = false) String category) {

		List<Product> products = productRepository.searchProducts(name, brand, category);
		model.addAttribute("products", products);
		return "prodottofiltrato.html";
	}

	/**
	 * metodo per fare ricerca con la barra
	 * 
	 * @param String da usare per fare la ricerca
	 * @return la pagina con i prodotti filtrati
	 **/
	@PostMapping("/ricercaHome")
	public String ricercaBarraHome(Model model, @Param("keyword") String keyword) {
		List<Product> products = this.productService.listAll(keyword);
		model.addAttribute("products", products);
		model.addAttribute("keyword", keyword);
		return "prodottoFiltrato.html";
	}

}
