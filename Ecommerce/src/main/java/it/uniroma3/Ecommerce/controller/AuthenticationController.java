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

import it.uniroma3.Ecommerce.authentication.CustomOauth2User;
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

	@Autowired
	private UserService userService;

	/**
	 * metodo per visualizzare la pagina di login
	 * @return login.html 
	 **/
	@GetMapping("/login") 
	public String showLogin(Model model) {
		return "login.html"; // la pagina html che deve ritornare
	}
	

	/**
	 * metodo per visualizzare la pagina per la registrazione
	 * @return register.html  
	 **/
	@GetMapping("/register") // imposto cosa deve fare quando incotro /register
	public String showRegister(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "register.html"; // la pagina html che deve ritornare
	}
	
	/**
	 * metodo che gestisce cosa succede dopo la registrazione
	 * @param le credenziali prese dal form 
	 * @return la pagina registrationSuccessful che mi dice che la 
	 * 		   registrazione è andata a buon fine 
	 **/
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult bindingResult,
			Model model) { 
		
		//se ci sono errori
		if (bindingResult.hasErrors()) {
			return "register";
		}

		//salvo le credenziali
		credentialsService.saveCredentials(credentials);
		model.addAttribute("user", credentials.getUser());
		return "registrationSuccessful";
	}

	/**
	 * metodo per visualizzare la pagina iniziale in maniera differenziata a seconda del ruolo dell'utente
	 * @return indexCompany.html se l'utente ha PROVIDER_ROLE 
	 * @return index.html se l'utente ha DEFAULT_ROLE
	 **/
	@GetMapping(value = "/registrazioneeffettuata")
	public String index(Model model) {
		
		//prendo l'autenticazione corrente
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "index.html"; // mi ritorna l'homepage
		} else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

			// se il ruolo dell'utente è quello di PROVIDER vuol dire che ci troviamo davanti a una azienda
			if (credentials.getRole().equals(Credentials.PROVIDER_ROLE)) {
				return "company/indexCompany.html"; // ritorno l'area riservata alle sole aziende
			}
		}
		return "index.html"; // torno alla homepage
	}

	
	/**
	 * metodo che gestisce cosa succede dopo che il login ha avuto successo
	 * @return indexCompany.html se l'utente ha PROVIDER_ROLE 
	 * @return index.html se l'utente ha DEFAULT_ROLE
	 **/
	@GetMapping(value = "/success") 
	public String defaultAfterLogin(Model model) {
		
		//recupera l'oggetto che identifica il tipo di login salvato nella sessione
		Object tipoDiLogin = this.sessionData.getUserDetailsObject();
		UserDetails userDetails = null;

		//verifico se l'utente ha fatto il login tramite OAuth2
		if (tipoDiLogin instanceof CustomOauth2User) {
			CustomOauth2User oauth = (CustomOauth2User) tipoDiLogin;
			
			//consente di registare/processare l'utente OAuth2 al primo accesso
			this.userService.processOAuthPostLogin(oauth);
			
			//carico tutti i prodotti per mostrarli nella home
			List<Product> a = productRepository.findAll();
			model.addAttribute("products", a);
			
			//aggiunge al model il nome proveniente da Google
			model.addAttribute("googleDetails", oauth.getName());
			return "index.html";
		} else {
			
			//faccio il login tradizionale 
			userDetails = (UserDetails) tipoDiLogin;
			
			//recupero le credenziali dal database
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			
	        //se il ruolo è PROVIDER (azienda), lo manda nella sua area riservata
			if (credentials.getRole().equalsIgnoreCase(Credentials.PROVIDER_ROLE)) {
				model.addAttribute("userDetails", userDetails);
				return "company/indexCompany.html"; // se ho permessi speciali allora posso accedere ad un'altra area
				
			} else {
	            //utente “normale”: mostra la home con la lista prodotti
				model.addAttribute("userDetails", userDetails);
				List<Product> a = productRepository.findAll();
				model.addAttribute("products", a);
				return "index.html";
			}
		}
	}
}
