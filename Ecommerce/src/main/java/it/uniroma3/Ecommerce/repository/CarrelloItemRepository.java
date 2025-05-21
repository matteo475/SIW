package it.uniroma3.Ecommerce.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.Ecommerce.model.CarrelloItem;

public interface CarrelloItemRepository extends CrudRepository<CarrelloItem, Long>{
     
}
