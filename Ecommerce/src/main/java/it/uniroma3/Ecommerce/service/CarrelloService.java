package it.uniroma3.Ecommerce.service;

import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.CarrelloItem;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.repository.CarrelloItemRepository;
import it.uniroma3.Ecommerce.repository.CarrelloRepository;
import it.uniroma3.Ecommerce.repository.ProductRepository;

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


	public Carrello salvaCarrello(Carrello carrello) {
		return this.carrelloRepository.save(carrello);
	}

	/*questo carrello è quello dell' utente personale e verrà preso tramite l'oggetto SessionData*/
	public Carrello getCarrello(Long id) {
		return this.carrelloRepository.findById(id).orElseThrow(()-> new RuntimeException("Carrello non trovato con id:" + id));
	}

	public void addProdottoAlCarrello(Long carrelloId, Long prodottoId, int quantita) {
		/**
		 * 1- prendiamo il carrello
		 * 2-prendiamo il prodotto
		 * 3-vediamo se il prodotto già esiste nel carrello
		 * 4-se si incrementiamo la quantita con la quantità richiesta
		 * 5-se no inizializziamo un nuovo prodotto di tipo CartItem  
		 */

		//l'id del carrello riferito all'utente x lo prendiamo tramite l'oggetto SessionData.
		Carrello carrello = this.carrelloRepository.findById(carrelloId).get();
		Product prodotto = this.prodottoRepository.findById(prodottoId).get();
		CarrelloItem cartitem = this.carrelloItemRepository.getProdottoDalCarrello(carrello, prodottoId).orElseThrow();
		if(cartitem == null) {
			cartitem.setCarrello(carrello);
			cartitem.setProduct(prodotto);
			cartitem.setQuantita(quantita);
			cartitem.setPrezzoPerUnita(prodotto.getPrice());

		}
		else {
			cartitem.setQuantita(cartitem.getQuantita() + quantita);
		}
		carrello.addProdottoCarrello(cartitem);
		carrello.calcolaSpesaTotale();
		this.carrelloItemRepository.save(cartitem);
		this.carrelloRepository.save(carrello);
	}


	public void cancellaProdottoDalCarrello(Long cartId, Long prodottoId) throws Exception {
		Carrello carrello = this.carrelloRepository.findById(cartId).get();
		Optional<CarrelloItem> daRimuovere = this.carrelloItemRepository.getProdottoDalCarrello(carrello, prodottoId);
		if(daRimuovere.isPresent()) {
			carrello.rimuoviProdottoDalCarrello(daRimuovere.get());
			this.carrelloRepository.save(carrello);
		} else {
              throw new RuntimeException("Elemento da rimuovere non trovato");
		}
		this.carrelloRepository.save(carrello);
	}

	public void updateProductQuantity(Long carrelloId, Long prodottoId, int quantita) {
		Carrello carrello = this.carrelloRepository.findById(carrelloId).get();
		CarrelloItem daAggiornare = this.carrelloItemRepository.getProdottoDalCarrello(carrello, prodottoId).orElseThrow();
		daAggiornare.setQuantita(quantita);
		daAggiornare.setPrezzoPerUnita(daAggiornare.getProduct().getPrice());
		carrello.calcolaSpesaTotale();
		this.carrelloRepository.save(carrello);
	}

	public void deleateCart(Long cartId) {
		Carrello daEliminare = this.carrelloRepository.findById(cartId).get();
		this.carrelloRepository.delete(daEliminare);
	}
}