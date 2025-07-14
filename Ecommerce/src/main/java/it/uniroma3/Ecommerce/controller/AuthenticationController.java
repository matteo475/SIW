package it.uniroma3.Ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.Ecommerce.authentication.SessionData;
import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.Credentials;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.repository.ProductRepository;
import it.uniroma3.Ecommerce.service.CarrelloService;
import it.uniroma3.Ecommerce.service.CredentialsService;
import it.uniroma3.Ecommerce.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	@Autowired 
	ProductRepository productRepository;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private CarrelloService carrelloService;
	@Autowired 
	private SessionData sessionData;

	//@Autowired
	// private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@GetMapping("/login")	//praticamente gli sto dicendo cosa fare quando incotra lo /login
	public String showLogin(Model model) {
		return "login.html"; //la pagina html che deve ritornare
	}

	@GetMapping("/register") //imposto cosa deve fare quando incotro /register
	public String showRegister(Model model) {
		model.addAttribute("user", new User()); 
		model.addAttribute("credentials", new Credentials());

		return "register.html"; //la pagina html che deve ritornare
	}

	@GetMapping(value = "/registrazioneeffettuata")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "index.html";	//mi ritorna l'homepage
		}
		else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

			//se il ruolo dell'utente è quello di PROVIDER vuol dire che ci dovriamo davanti a una azienda
			if (credentials.getRole().equals(Credentials.PROVIDER_ROLE)) {
				return "company/indexCompany.html";	//ritorno l'area riservata alle sole aziende
			}
			/**
			if(credentials.getRole().equals(Credentials.NORMAL_ROLE)) {
				return "index.html";
			}
			 */
		}

		return "index.html"; //torno alla homepage
	}

	@GetMapping(value = "/success")	//il login ha avuto successo
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equalsIgnoreCase(Credentials.PROVIDER_ROLE)) {
			model.addAttribute("userDetails", userDetails);
			return "company/indexCompany.html";	//se ho permessi speciali allora posso accedere ad un'altra area
		}else {
			model.addAttribute("userDetails", userDetails);
			List <Product> a=productRepository.findAll();
			model.addAttribute("products", a);
			return "index.html"; 
		}

		/**
		//implementazione ruolo di defoult 
		if(credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
			return "index.html";
		}
		 **/
		//	return "index.html"; //se mi sono autenticato e sono un utente normale torno alla homepage
	}

	/*
	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the
		// Credentials nel DB
		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			userService.saveUser(user);
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			model.addAttribute("user", user);
			return "registrationSuccessful.html";	//torna la pagina che mi dice che la registrazione è avvenuta con successo
		}
		return "register.html";	//pagina di ritorno
	}*/

	/*
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {

		 if (userBindingResult.hasErrors() || credentialsBindingResult.hasErrors()) {
	            return "register";
	        }

	        // 1) Imposta la password codificata
	        //credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));

	        // 2) Collega i due oggetti l’uno all’altro
	        credentials.setUser(user);
	        user.setCredentials(credentials);

	        // 3) Salva SOLO le credentials (cascade ALL persiste anche User)
	        credentialsService.saveCredentials(credentials);

	        model.addAttribute("user", user);
	        return "registrationSuccessful";

	}
	 */

	/*
	@PostMapping("/register")
	public String registerUser(BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, 
			Model model) {

		 if (userBindingResult.hasErrors()) {
	            return "register";
	        }

	        // 1) Imposta la password codificata
	        //credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));

	        // 2) Collega i due oggetti l’uno all’altro


	        // 3) Salva SOLO le credentials (cascade ALL persiste anche User)
	        credentialsService.saveCredentials(credentials);

	        model.addAttribute("user", credentials.getUser());
	        return "registrationSuccessful";

	}
	 */
	@PostMapping("/register")
	public String registerUser(
			@Valid 
			@ModelAttribute("credentials") Credentials credentials,  // 1) il modello
			BindingResult bindingResult,                             // 2) subito dopo il BindingResult
			Model model) {                                           // 3) gli altri parametri
		if (bindingResult.hasErrors()) {
			return "register";
		}

		// codifica e persisti
		// credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
		credentialsService.saveCredentials(credentials);
		model.addAttribute("user", credentials.getUser());
		return "registrationSuccessful";
	}


}
