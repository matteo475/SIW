package it.uniroma3.Ecommerce.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;


/**
 * gestire quantità o permettere che lo stesso Prodotto sia in più carrelli, 
 * si usa un’entità CarrelloItem con una relazione unidirezionale @OneToMany da Carrello a CarrelloItem. 
 * Ecco come: (guarda codice classe)
 * 
 * Vantaggi:
 * Permette di gestire la quantità (quantita in CarrelloItem).
 * Un Prodotto può essere in più carrelli, perché la relazione è gestita da CarrelloItem,
 * non direttamente da Prodotto.
 * La tabella prodotto non ha una colonna carrello_id, quindi i prodotti sono indipendenti dai carrelli.
 * piccola spiegazione del perché ho implementato carrelloItem.
 */

@Entity
public class CarrelloItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product prodotto;
    @ManyToOne
    @JoinColumn(name = "carrello_id")
	private Carrello carrello;
	private double prezzoPerUnita;
	private int quantita;

	// Getters e setters
	public Long getId() { 
		return id; 
	}
	public void setId(Long id) {
		this.id = id; 
	}
	public Product getProduct() { 
		return this.prodotto; 
	}
	public void setProduct(Product product) { 
		this.prodotto = product; 
	}
	public int getQuantita() { 
		return quantita; 
	}
	public void setQuantita(int quantita) { 
		this.quantita = quantita; 
	}

	public Carrello getCarrello() {
		return carrello;
	}
	public void setCarrello(Carrello carrello) {
		this.carrello = carrello;
	}
	
	public double getPrezzoPerUnita() {
		return prezzoPerUnita;
	}
	public void setPrezzoPerUnita(double prezzoPerUnita) {
		this.prezzoPerUnita = prezzoPerUnita;
	}
	
	/*carrello item è un prodotto quindi 2 prodotti sono diversi se gli ID dei prodotti
	 * sono diversi */
	@Override 
	public boolean equals(Object obj) {
		if(obj == null || this.getClass() != obj.getClass())
			return false;
		CarrelloItem that = (CarrelloItem) obj;
		return this.prodotto.getId() == that.getProduct().getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getProduct().getId());
	}
}