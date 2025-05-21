package it.uniroma3.Ecommerce.service;

import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.CarrelloItem;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.repository.CarrelloItemRepository;
import it.uniroma3.Ecommerce.repository.CarrelloRepository;
import it.uniroma3.Ecommerce.repository.ProductRepository;
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
		return this.carrelloRepository.findById(id).get();
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
		CarrelloItem cartitem = carrello.getProdotti()
				.stream()
				.filter(item -> item.getProduct().equals(prodottoId))
				.findFirst().orElse(null);
		if(cartitem.getId() == null) {
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
		Carrello carrello = this.carrelloRepository.findById(prodottoId).get();
		CarrelloItem daRimuovere = carrello.getProdottoDalCarrello(prodottoId);

		if(daRimuovere == null)
			throw new Exception("Prodotto non trovato");

		carrello.rimuoviProdottoDalCarrello(daRimuovere);
		this.carrelloRepository.save(carrello);
	}

	public void updateProductQuantity(Long carrelloId, Long prodottoId, int quantita) {
		Carrello carrello = this.carrelloRepository.findById(carrelloId).get();
		CarrelloItem daAggiornare = carrello.getProdottoDalCarrello(prodottoId);
		daAggiornare.setQuantita(quantita);
		daAggiornare.setPrezzoPerUnita(daAggiornare.getProdotto().getPrice());
		carrello.calcolaSpesaTotale();
		this.carrelloRepository.save(carrello);
	}

}