package it.uniroma3.Ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.Ecommerce.authentication.SessionData;
import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.service.CarrelloService;

@Controller
public class CarrelloController {

	@Autowired
	private CarrelloService carrelloService;
	@Autowired SessionData userLogged;

	@GetMapping("/carrello")
	public String showCart(Model model) {
		User loggato = this.userLogged.getLoggedUser();
		Carrello cart = this.carrelloService.getCarrello(loggato.getCarrello().getId());
		model.addAttribute("cartItems", cart.getProdotti());
		model.addAttribute("total", cart.calcolaSpesaTotale());
		return "carrello.html";
	}
}
