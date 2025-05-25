package it.uniroma3.Ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Classe che modella un'ordine su un ecommerce online da parte di un utente 
 * idea per il mapping, un utente può effettuare più ordini mentre
 * i prodotti dell'ordine sono tutti quelli che stanno nel carrello di quello
 * specifico utente.
 * Gli stati di un ordini sono descritti nella Enum StatoOrdine
 * @see StatoOrdine
 * @author Jacopo Orru'
 */

@Entity
public class Ordine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private LocalDate dataOrdine;
	@NotNull
	@PositiveOrZero
	private BigDecimal totale;
	@Enumerated(EnumType.STRING)
	private StatoOrdine statoDellOrdine;
	@ManyToOne
	@JoinColumn(name = "utente_id")
	private User utente;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ordine_id")
	private List<VoceOrdine> vociDellOrdine;

	public Ordine() {
		this.dataOrdine = LocalDate.now();
		this.statoDellOrdine = StatoOrdine.CONFERMATO;
		this.totale = BigDecimal.ZERO;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public LocalDate getDataOrdine() {
		return dataOrdine;
	}


	public void setDataOrdine(LocalDate dataOrdine) {
		this.dataOrdine = dataOrdine;
	}


	public BigDecimal getTotale() {
		return totale;
	}


	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}


	public StatoOrdine getStatoDellOrdine() {
		return statoDellOrdine;
	}


	public void setStatoDellOrdine(StatoOrdine statoDellOrdine) {
		this.statoDellOrdine = statoDellOrdine;
	}


	public User getUtente() {
		return utente;
	}


	public void setUtente(User utente) {
		this.utente = utente;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || this.getClass() != obj.getClass())
			return false;
		Ordine that = (Ordine) obj;
		return this.id == that.getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
