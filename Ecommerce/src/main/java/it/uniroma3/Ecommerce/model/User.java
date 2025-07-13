package it.uniroma3.Ecommerce.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String surname;

	@NotBlank
	@Email
	private String email;

	//mapping bidirezionale OneToOne verso Carrello
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Carrello carrello;

	@OneToMany(mappedBy = "utente", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Ordine> ordini;

	//mapping bidirezionale OneToOne verso Credentials
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Credentials credentials;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Address> indirizzi = new ArrayList<>();

	public User() {
	}

	// PrePersist: inizializza sempre un nuovo carrello
	@PrePersist
	private void initCarrello() {
		if (this.carrello == null) {
			Carrello c = new Carrello();
			c.setUser(this);
			this.carrello = c;
		}
	}

	// getter & setter essenziali
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Carrello getCarrello() {
		return carrello;
	}

	public void setCarrello(Carrello carrello) {
		this.carrello = carrello;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	public List<Address> getAllAddress(){
		return indirizzi;
	}
	
	public void setAddress(List<Address> indirizzi) {
		this.indirizzi = indirizzi;
	}
	
	//metodo per aggiungere indirizzo 
	public void addAddress(Address indirizzo) {
		indirizzo.setUser(this); 
		this.indirizzi.add(indirizzo);
	}
	
	//metodo per rimuovere un indirizzo
	public void removeAddress(Address address) {
		this.indirizzi.remove(address); 
		address.setUser(null);
	}
	

	// equals e hashCode su campi univoci
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.surname, this.email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return Objects.equals(this.name, other.name) && Objects.equals(this.surname, other.surname)
				&& Objects.equals(this.email, other.email);
	}
}
