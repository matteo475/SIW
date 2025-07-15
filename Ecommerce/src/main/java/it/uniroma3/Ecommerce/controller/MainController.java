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
import it.uniroma3.Ecommerce.service.CredentialsService;
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

	@Autowired
	CredentialsService credentialsService;

	@Autowired private SessionData sessionData;

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
		model.addAttribute("userDetails", this.sessionData.getUserDetails());
		model.addAttribute("products", products); 
		return "index.html"; 
	}

	//visualizzazione del singolo prodotto
	@GetMapping("/prodotto/{id}")
	public String visualizza_prodotto(@PathVariable("id") Integer id, Model model) throws ProductNotFoundException {
		Product product = this.productService.get(id);	
		int quantita = this.productService.numeroProdotti(id);
		model.addAttribute("product",product);
		model.addAttribute("disponibilita", quantita);
		return "viewproduct.html";
	}

	/*
	@GetMapping("/userProfile/{id}")
	public String showUserProfile(@PathVariable Long id, Model model) throws Exception {
	  //log.debug("Chiamato showUserProfile con id = {}", id);
	  User user = userService.getUser(id);
	  //log.debug("Trovato user = {}", user);
	  if(user==null) throw new Exception("Utente non trovato");
	  model.addAttribute("user", user);
	  return "userProfile";
	}*/
	/*
	@GetMapping("/userProfile/{id}")
	  public String showUserProfile(@PathVariable("id") Long id, Model model) {
	    User user = userService.getUser(id);
	    if (user == null) {
	      // gestisci l’errore: utente non trovato
	      return "error/404";
	    }
	    model.addAttribute("user", this.userRepository.findById(id).get());
	    return "userProfile.html";
	  }*/


	//visualizzazione profilo utente

	@GetMapping("/userProfile/{id}")
	public String showUserProfile(@PathVariable("id") Long id, Model model) {
		User user = this.userService.getUser(id);
		model.addAttribute("user",user);
		return "userProfile";
	}

	/*
	@GetMapping("/profile")
	public String myProfile(@AuthenticationPrincipal Credentials creds, Model model) {
	  if(creds == null) {
		  System.out.println("Le credenziali sono nulle");
	  }
	  model.addAttribute("user", creds.getUser());
	  return "userProfile";
	}*/

	@GetMapping("/profile")
	public String showMyProfile(Authentication authentication, Model model) {
		// 1) utente non autenticato → login
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/login";
		}

		// 2) ricava lo username dal Principal (qualsiasi tipo sia)
		Object principal = authentication.getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			// fallback: usa toString() o gestisci come errore
			username = principal.toString();
		}

		// 3) recupera sempre le credentials dal repo
		Credentials creds = credentialsService.getCredentials(username);
		if (creds == null) {
			// log di debug: non trovato in DB?
			// logger.warn("Nessuna credentials per username=" + username);
			return "redirect:/login?error";
		}

		// 4) usa creds.getUser() solo se non null
		User user = creds.getUser();
		if (user == null) {
			// impossibile ma difensivo
			return "redirect:/login?error";
		}

		model.addAttribute("user", user);
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
	public String ricercaf(Model model) {
		model.addAttribute("userDetails", this.sessionData.getUserDetails());
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

