package it.uniroma3.Ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.CarrelloItem;

public interface CarrelloRepository extends CrudRepository<Carrello,Long> {
     
}
