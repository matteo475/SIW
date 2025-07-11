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


	@GetMapping("/carrello")
	public String showCart(Model model) {
		User loggato = this.userLogged.getLoggedUser();
		Carrello cart = this.carrelloService.getCarrello(loggato.getCarrello().getId());
		model.addAttribute("cartItems", cart.getProdotti());
		model.addAttribute("total", cart.calcolaSpesaTotale());
		return "carrello.html";
	}

	/*
	@GetMapping("/carrello/carrelloItem/{id}")
	public String aggiungiProdotto(Model model) {
		model.addAttribute("cartItems", this.userLogged.getLoggedUser().getCarrello().getProdotti());		return "carrello.html";
	}*/


	@GetMapping("/carrello/carrelloItem/{id}")
	public String antiRefresh(@PathVariable("id") Integer id,Model model) {
		Carrello cart = this.carrelloService.getCarrello(this.userLogged.getLoggedUser().getCarrello().getId());
		this.carrelloService.addProdottoAlCarrello(cart.getId(), id, 1);
		model.addAttribute("cartItems", cart.getProdotti());
		model.addAttribute("total", cart.calcolaSpesaTotale());
		return "redirect:/";
	}


	@GetMapping("/carrello/carrelloItem/cancella/{id}")
	public String rimuoviDalCarrello(@PathVariable("id") Integer id,Model model) throws Exception {
		Carrello cart = this.carrelloService.getCarrello(this.userLogged.getLoggedUser().getCarrello().getId());
		this.carrelloService.cancellaProdottoDalCarrello(cart.getId(), id);
		model.addAttribute("cartItems", cart.getProdotti());
		model.addAttribute("total", cart.calcolaSpesaTotale());
		return "redirect:/carrello";
	}

	@GetMapping("/{carrelloId}/clear")
	public String clearCarrello(@PathVariable Long CarrelloId) {
		this.carrelloService.deleateCart(CarrelloId);
		return "index.html";
	}
}
