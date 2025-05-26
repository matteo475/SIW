package it.uniroma3.Ecommerce.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class VoceOrdine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "prodotto_id")
	private Product prodotto;
	private int quantita;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Product getProdotto() {
		return prodotto;
	}
	public void setProdotto(Product prodotto) {
		this.prodotto = prodotto;
	}
	public int getQuantita() {
		return quantita;
	}
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj || getClass() != obj.getClass())
			return false;
		VoceOrdine other = (VoceOrdine) obj;
		return this.id == other.getId();
	}



}
