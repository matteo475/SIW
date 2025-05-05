package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Credentials;
import com.example.demo.model.User;
import com.example.demo.service.CredentialsService;
import com.example.demo.service.UserService;
import static com.example.demo.model.Credentials.ADMIN_ROLE;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {


	@Autowired
	private CredentialsService credentialsService;  // Il servizio per gestire le credenziali
	@Autowired
	private UserService userService;  // Il servizio per gestire gli utenti


	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/home")
	public String smistamentoPaginePerRuolo() {
		UserDetails user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth instanceof AnonymousAuthenticationToken) {
			return "index";
		}else {
			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credenziali = this.credentialsService.getCredenziali(user.getUsername());
			if(credenziali.getRuolo().equals(ADMIN_ROLE)) {
				return "areaPersonale.html";
			}else {
				return "index";
			}
		}
	}

	@GetMapping("/register")
	public String ShowRegisterUser(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user,
	        BindingResult userBindingResult,
	        @Valid @ModelAttribute("credentials") Credentials credentials,
	        BindingResult credentialsBindingResult,
	        Model model) {

	    // Verifica se ci sono errori nei dati inseriti
	    if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {

	        // Imposta il ruolo scelto (dal form)
	        String role = credentials.getRuolo();  // Il ruolo viene passato nel modulo HTML
	        // Se il ruolo non è stato specificato, impostiamo un ruolo di default (USER)
	        if (role == null || role.isEmpty()) {
	            role = Credentials.USER_ROLE;
	        }
	        System.out.println("Ruolo selezionato: " + role);
	        credentials.setRuolo(role);  // Imposta il ruolo su Credentials

	        // Salva l'utente e le credenziali nel database
	        try {
	            user.setCredentials(credentials);
	            this.userService.saveUser(user);
	            this.credentialsService.saveCredenziali(credentials);  // Salva le credenziali
	            System.out.println("Utente e credenziali salvati nel database");

	            // Aggiungi l'utente al modello per conferma
	            model.addAttribute("user", user);

	            return "registrationSuccessful";  // La pagina di conferma
	        } catch (Exception e) {
	            System.out.println("Errore nel salvataggio dell'utente: " + e.getMessage());
	            return "register";  // In caso di errore, ritorna alla pagina di registrazione
	        }
	    } else {
	        // Se ci sono errori nei dati del form, ritorna alla pagina di registrazione
	        System.out.println("Errore di validazione: " + userBindingResult.getAllErrors());
	        return "register";  // Ritorna alla pagina di registrazione se ci sono errori
	    }
	}



}
