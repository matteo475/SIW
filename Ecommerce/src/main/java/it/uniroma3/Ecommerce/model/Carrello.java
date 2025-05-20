package it.uniroma3.Ecommerce.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
public class Carrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToMany
	@JoinColumn(name ="carrello_id")
	private List<CarrelloItem> prodotti;
	private double spesaTotale;

	/*come passo l'utente tramite l'oggetto SessionData che mi da l'utente corrente in quella sessione*/
	public Carrello() {
		this.prodotti = new ArrayList<CarrelloItem>();
		this.spesaTotale = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getSpesaTotale() {
		return spesaTotale;
	}

	public void setSpesaTotale(double spesaTotale) {
		this.spesaTotale = spesaTotale;
	}

	public List<CarrelloItem> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<CarrelloItem> prodotti) {
		this.prodotti = prodotti;
	}

	public void addProdottoCarrello(CarrelloItem prodotto) {
		this.prodotti.add(prodotto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || this.getClass() != obj.getClass()) 
			return false;
		Carrello other = (Carrello) obj;
		return this.id == other.getId();
	}



} 
