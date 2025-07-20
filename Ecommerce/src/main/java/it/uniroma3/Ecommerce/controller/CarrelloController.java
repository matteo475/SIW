package it.uniroma3.Ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.Ecommerce.authentication.SessionData;
import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.service.CarrelloService;
import it.uniroma3.Ecommerce.service.ProductService;

@Controller
public class CarrelloController {

	@Autowired
	private CarrelloService carrelloService;
	@Autowired 
	private SessionData userLogged;


	/**
	 * metodo per visualizzare la pagina relativa al carrello 
	 * @return carrello.html 
	 **/
	@GetMapping("/carrello")
	public String showCart(Model model) {
		
		//prendo l'utente loggato
		User loggato = this.userLogged.getLoggedUser();
		
		//prendo il carrello relativo all'utente loggato 
		Carrello cart = this.carrelloService.getCarrello(loggato.getCarrello().getId());
		
		model.addAttribute("userDetails", this.userLogged.getUserDetails());
		model.addAttribute("cartItems", cart.getProdotti());
		model.addAttribute("total", cart.calcolaSpesaTotale());
		return "carrello.html";
	}

	/**
	 * @param id del carrello
	 **/
	@GetMapping("/carrello/carrelloItem/{id}")
	public String antiRefresh(@PathVariable("id") Integer id,Model model) {
		
		//prendo il carrello relativo all'utente loggato
		Carrello cart = this.carrelloService.getCarrello(this.userLogged.getLoggedUser().getCarrello().getId());
		this.carrelloService.addProdottoAlCarrello(cart.getId(), id, 1);
		
		model.addAttribute("cartItems", cart.getProdotti());
		model.addAttribute("total", cart.calcolaSpesaTotale());
		return "redirect:/";
	}

	/**
	 * metodo per cancellare un prodotto all'interno del carrello 
	 * @param id del prodotto da cancellare
	 * @return torno alla pagina del carrello
	 **/
	@GetMapping("/carrello/carrelloItem/cancella/{id}")
	public String rimuoviDalCarrello(@PathVariable("id") Integer id,Model model) throws Exception {
		
		//prendo il carrello relativo all'utente loggato
		Carrello cart = this.carrelloService.getCarrello(this.userLogged.getLoggedUser().getCarrello().getId());
		
		//cancello il prodotto dal carrello
		this.carrelloService.cancellaProdottoDalCarrello(cart.getId(), id);
		
		model.addAttribute("cartItems", cart.getProdotti());
		model.addAttribute("total", cart.calcolaSpesaTotale());
		return "redirect:/carrello";
	}

	/**
	 * metodo per eliminare tutti i prodotti dal carrello
	 * @param id del carrello 
	 * @return la pagina index.html
	 **/
	@GetMapping("/{carrelloId}/clear")
	public String clearCarrello(@PathVariable Long CarrelloId) {
	
		//cancello tutto il carrello
		this.carrelloService.deleateCart(CarrelloId);
		return "index.html";
	}
}
