package it.uniroma3.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.CarrelloItem;
import it.uniroma3.Ecommerce.model.Product;

public interface CarrelloItemRepository extends CrudRepository<CarrelloItem, Long>{
 
	/*cercare prodotto assocciato al carrelloItem ed dal carrello corrispondente, il carrelloItem fa riferimento ad un prodotto*/
	@Query("select ci from CarrelloItem ci where  ci.carrello.id=?1 and ci.prodotto.id =?2")
    public Optional<CarrelloItem> getProdottoDalCarrello(Long carrello, Integer productId);
	
	/*questa restituisce proprio il prodotto*/
	@Query("select ci.prodotto from CarrelloItem ci where ci.carrello =?1 and ci.prodotto.id=?2")
	public Optional<Product> getProductReal(Carrello carrello, Integer productId);
	
	@Query("select ci from CarrelloItem ci where ci.carrello =?1")
	public List<CarrelloItem> getAllItemFromCart(Carrello carrello);
	
	@Modifying
	@Query("delete from CarrelloItem ci where ci.carrello.id =?1 and ci.prodotto.id =?2")
	public void cancellaProdottoDalCarrello(Long carrello, Integer productId);
	
	public boolean existsByCarrelloIdAndProdottoId(Long carrelloId, Integer prodottoId);
	
}
