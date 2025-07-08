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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Carrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "carrello", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<CarrelloItem> prodotti = new ArrayList<>();

	private double spesaTotale = 0;

	// Nuovo mapping bidirezionale verso User
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	public Carrello() {
	}

	public Long getId() {
		return id;
	}

	public List<CarrelloItem> getProdotti() {
		return prodotti;
	}

	public double getSpesaTotale() {
		return spesaTotale;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void addProdottoCarrello(CarrelloItem prodotto) {
		prodotto.setCarrello(this);
		this.prodotti.add(prodotto);
		this.calcolaSpesaTotale();
	}

	public void rimuoviProdottoDalCarrello(CarrelloItem item) {
		this.prodotti.remove(item);
		item.setCarrello(null);
		this.calcolaSpesaTotale();
	}

	public double calcolaSpesaTotale() {
		this.spesaTotale = prodotti.stream().mapToDouble(ci -> ci.getPrezzoPerUnita() * ci.getQuantita()).sum();
		return spesaTotale;
	}

	public CarrelloItem getProdottoDalCarrello(Long id) {
		return prodotti.stream().filter(ci -> ci.getProduct().getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Carrello))
			return false;
		Carrello other = (Carrello) o;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
