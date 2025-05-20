package it.uniroma3.Ecommerce.service;

import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.CarrelloItem;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.repository.CarrelloRepository;
import it.uniroma3.Ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrelloService {
    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private ProductRepository prodottoRepository;

    public Carrello addProdottoToCarrello(Long carrelloId, Long prodottoId,int quantita) {
        Carrello carrello = this.carrelloRepository.findById(carrelloId).get();
        Product prodotto = this.prodottoRepository.findById(prodottoId).get();
        CarrelloItem item = new CarrelloItem();
        item.setProduct(prodotto);
        item.setQuantita(quantita);
        carrello.addProdottoCarrello(item);
        return this.carrelloRepository.save(carrello);
    }
}