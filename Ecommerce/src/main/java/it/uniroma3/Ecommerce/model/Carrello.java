package it.uniroma3.Ecommerce.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	@OneToMany(mappedBy = "carrello", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
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
		prodotto.setCarrello(this);
		this.prodotti.add(prodotto);
	}


	public double calcolaSpesaTotale() {
		double app = 0;
		for(CarrelloItem ci : this.prodotti) {
			app += ci.getPrezzoPerUnita() * ci.getQuantita();
		}
		this.spesaTotale = app;
		return this.spesaTotale;
	}
	
	//metodo che funziona solo in locale
	public CarrelloItem getProdottoDalCarrello(Long id) {
		 CarrelloItem daCercare = null;
		 Iterator<CarrelloItem> it = this.prodotti.iterator();
		 while(it.hasNext()) {
			 daCercare = it.next();
			 if(daCercare.getProduct().getId().equals(id)) {
				 return daCercare;
			 }
		 }
		 return null;
	}
	
	//metodo che funziona solo in locale
	public void rimuoviProdottoDalCarrello(CarrelloItem item) {
		this.prodotti.remove(item);
		item.setCarrello(null);
		this.calcolaSpesaTotale();
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
