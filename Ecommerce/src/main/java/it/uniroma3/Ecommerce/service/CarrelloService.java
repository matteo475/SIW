package it.uniroma3.Ecommerce.service;

import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.CarrelloItem;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.repository.CarrelloItemRepository;
import it.uniroma3.Ecommerce.repository.CarrelloRepository;
import it.uniroma3.Ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrelloService {

	@Autowired
	private CarrelloItemRepository carrelloItemRepository;
	
	@Autowired
	private CarrelloRepository carrelloRepository;
	
	@Autowired
	private ProductRepository prodottoRepository;
	
	private final List<CarrelloItem> carrelloItem = new ArrayList<>();

	/**
	 * metodo per salvare l'oggetto carrello
	 * @param oggetto carrello da salvare
	 * @return l'oggetto carrello viene salvato
	 **/
	public Carrello salvaCarrello(Carrello carrello) {
		return this.carrelloRepository.save(carrello);
	}

	
	
	/*questo carrello è quello dell' utente personale e verrà preso tramite l'oggetto SessionData*/
	public Carrello getCarrello(Long id) {
		return this.carrelloRepository.findById(id).orElseThrow(()-> new RuntimeException("Carrello non trovato con id:" + id));
	}

	@Transactional
	public void addProdottoAlCarrello(Long carrelloId, Integer prodottoId, Integer quantita) {
		/**
		 * 1- prendiamo il carrello
		 * 2-prendiamo il prodotto
		 * 3-vediamo se il prodotto già esiste nel carrello
		 * 4-se si incrementiamo la quantita con la quantità richiesta
		 * 5-se no inizializziamo un nuovo prodotto di tipo CartItem  
		 */
		Carrello carrello = this.carrelloRepository.findById(carrelloId).orElseThrow(() -> new IllegalArgumentException("Carrello non trovato"));
		Product prodotto = this.prodottoRepository.findById(prodottoId).orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

		Optional<CarrelloItem> optionalCartItem = this.carrelloItemRepository.getProdottoDalCarrello(carrello.getId(), prodottoId);

		CarrelloItem cartitem;
		if (optionalCartItem.isPresent()) {
			cartitem = optionalCartItem.get();
			cartitem.setQuantita(cartitem.getQuantita() + quantita);
		} else {
			cartitem = new CarrelloItem(); // Assicurati che ci sia un costruttore senza argomenti
			cartitem.setCarrello(carrello);
			cartitem.setProduct(prodotto);
			cartitem.setQuantita(quantita);
			cartitem.setPrezzoPerUnita(prodotto.getPrice());
		}

		carrello.addProdottoCarrello(cartitem);
		carrello.calcolaSpesaTotale();
		this.carrelloItemRepository.save(cartitem);
		this.carrelloRepository.save(carrello);
	}


	@Transactional
	public void cancellaProdottoDalCarrello(Long cartId, Integer prodottoId) {
		Carrello carrello = this.carrelloRepository.findById(cartId)
				.orElseThrow(() -> new RuntimeException("Carrello non trovato"));

		Optional<CarrelloItem> daRimuovereOpt = this.carrelloItemRepository.getProdottoDalCarrello(carrello.getId(), prodottoId);

		if (daRimuovereOpt.isPresent()) {
			CarrelloItem daRimuovere = daRimuovereOpt.get();

			if (daRimuovere.getQuantita() > 1) {
				daRimuovere.setQuantita(daRimuovere.getQuantita() - 1);
				this.carrelloItemRepository.save(daRimuovere);
			} else {
				carrello.rimuoviProdottoDalCarrello(daRimuovere);
				this.carrelloItemRepository.cancellaProdottoDalCarrello(cartId, prodottoId);
			}

			carrello.calcolaSpesaTotale();
			this.carrelloRepository.save(carrello);

		} else {
			throw new RuntimeException("Elemento da rimuovere non trovato");
		}
	}

	public void updateProductQuantity(Long carrelloId, Integer prodottoId, int quantita) {
		Carrello carrello = this.carrelloRepository.findById(carrelloId).get();
		CarrelloItem daAggiornare = this.carrelloItemRepository.getProdottoDalCarrello(carrello.getId(), prodottoId).orElseThrow();
		daAggiornare.setQuantita(quantita);
		daAggiornare.setPrezzoPerUnita(daAggiornare.getProduct().getPrice());
		carrello.calcolaSpesaTotale();
		this.carrelloRepository.save(carrello);
	}

	public void deleateCart(Long cartId) {
		Carrello daEliminare = this.carrelloRepository.findById(cartId).get();
		this.carrelloRepository.delete(daEliminare);
	}

	public boolean prodottoGiaNelCarrello(Long id1,Integer id) {
		return this.carrelloItemRepository.existsByCarrelloIdAndProdottoId(id1, id);
	}
	/**
     * Restituisce la lista corrente di articoli nel carrello.
     */
    public List<CarrelloItem> getItems() {
        // restituisco una copia per non esporre la lista interna
        return new ArrayList<>(carrelloItem);
    }

}