package it.uniroma3.Ecommerce.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.Ecommerce.model.Carrello;

public interface CarrelloRepository extends CrudRepository<Carrello,Long> {
     
}
