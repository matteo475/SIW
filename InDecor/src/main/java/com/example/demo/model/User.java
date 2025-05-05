package com.example.demo.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(nullable = false)
	private String nome;
	@NotBlank
	@Column(nullable = false)
	private String cognome;
	@NotBlank
	private String email;
	@OneToOne // Mappiamo la relazione con la classe Credentials
	@JoinColumn(name = "credenziali_fk")
	private Credentials credentials;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != this.getClass())
			return false;
		User that = (User) obj;
		return this.cognome.equals(that.getCognome()) && this.id == that.getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, id);
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", credentials="
				+ credentials + "]";
	}
	

}
