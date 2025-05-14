package it.uniroma3.Ecommerce.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Credentials {	//gestisce le credenziali di accesso al sito 

	//definisco i vari ruoli o le varie authorities per accedere al sito
	public static final String DEFAULT_ROLE = "UTENTE";
	public static final String PROVIDER_ROLE = "PROVIDER";
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	private String username; 
	private String password; 
	private String role; 
	private String gender;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	private User user;

	public String getUsername() {
		return username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
