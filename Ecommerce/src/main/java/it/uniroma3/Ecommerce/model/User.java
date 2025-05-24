package it.uniroma3.Ecommerce.model;

import java.util.List;
import java.util.Objects;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

@Entity 
@Table(name = "users") //cambio il nome perchè in postgres user è una parola riservata
public class User {

	/*sto modellando i parametri di un utente tipo del nostro sito*/

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name; 
	@NotBlank
	private String surname;	//è il cognome
	@NotBlank
	private String email;
    
	//ogni utente ha un proprio carrello
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "carrello_fk") // whenever a Project is retrieved, always retrieve its cart too
	private Carrello carrello;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "utente_id")
	private List<Ordine> ordini;

	//metodi getter e setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	//metodi hash e equals
	@Override
	public int hashCode() {
		/**
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
		**/
		return Objects.hash(this.name,this.email,this.surname);
	}

	@Override
	public boolean equals(Object obj) {
		/**
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
		**/
		if(obj == null || this.getClass() != obj.getClass())
			return false;
		User that = (User) obj;
		return this.name.equals(that.getName()) && this.email.equals(that.getEmail()) && this.surname.equals(that.getSurname());
	}
}
