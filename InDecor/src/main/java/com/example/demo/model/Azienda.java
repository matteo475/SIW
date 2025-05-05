package com.example.demo.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Azienda {

	@Id
	private String partitaIva;
	private String nome;
	private String regione;
	private String citta;
	//@OneToOne
  //  private Utente admin;
	@OneToMany
	@JoinTable
    private List<User> clienti;
    

	
	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || this.getClass() != obj.getClass())
			return false;
		Azienda that = (Azienda) obj;
		return this.partitaIva.equals(that.partitaIva);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.partitaIva);
	}

}
