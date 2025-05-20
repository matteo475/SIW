package it.uniroma3.Ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
public class Carrello {
     
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  @OneToOne(mappedBy = "carrello")
	  private User utente;
	  @OneToMany(mappedBy = "carrello")
	  private List<Product> prodotti;
	  private double spesaTotale;
	
	/*come passo l'utente tramite l'oggetto SessionData che mi da l'utente corrente in quella sessione*/
	  public Carrello(User utente) {
		  this.utente = utente;
		  this.prodotti = new ArrayList<Product>();
		  this.spesaTotale = 0;
	  }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUtente() {
		return utente;
	}

	public void setUtente(User utente) {
		this.utente = utente;
	}

	public List<Product> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<Product> prodotti) {
		this.prodotti = prodotti;
	}

	public double getSpesaTotale() {
		return spesaTotale;
	}

	public void setSpesaTotale(double spesaTotale) {
		this.spesaTotale = spesaTotale;
	}
	  
	public void addProdottoCarrello(Product prodotto) {
		 this.prodotti.add(prodotto);
	}
	
	public void calcolaSpesaTotale() {
		double app = 0;
		for(Product d : this.prodotti) {
			app += d.getPrice();
		}
		this.setSpesaTotale(app);
	}
	  
	//equals e hashCode
	
} 
