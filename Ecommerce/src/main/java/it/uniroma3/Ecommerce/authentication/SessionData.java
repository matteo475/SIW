package it.uniroma3.Ecommerce.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import it.uniroma3.Ecommerce.model.Credentials;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.repository.CredentialsRepository;
import it.uniroma3.Ecommerce.repository.UserRepository;

/**
 * QUESTA CLASSE E' UTILE PER OTTENERE I DATI DALL'UTENTE AUTENTICATO QUESTO CI
 * SERVIRA' PER ASPETTI LOGICI COME USARLO? NEI VARI CONTROLLER INSERIRE COME
 * VARIABILI D'ISTANZA
 * 
 * @Autowired private SessionData session;
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionData {

	@Autowired
	private CredentialsRepository credRepo;
	
	private User user;
	private Credentials credenziali;

	public Credentials getLoggedCredentials() {
		if (this.credenziali == null)
			this.updtade();
		return this.credenziali;
	}

	public User getLoggedUser() {
		if (this.user == null)
			this.updtade();
		return this.user;
	}

	private void updtade() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userLoggato = (UserDetails) obj;

		this.credenziali = this.credRepo.findByUsername(userLoggato.getUsername()).get();
		this.credenziali.setPassword("[PROTECTED]");
		this.user = this.credenziali.getUser();
	}

	public UserDetails getUserDetails() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userLoggato = (UserDetails) obj;
		return userLoggato;
	}

	public Object getUserDetailsObject() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return obj;
	}

}
