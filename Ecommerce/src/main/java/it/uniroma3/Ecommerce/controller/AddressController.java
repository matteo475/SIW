package it.uniroma3.Ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.Ecommerce.model.AddressDTO;
import it.uniroma3.Ecommerce.service.AddressService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/{userId}/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	/**
	 * mostro il form per creare un nuovo indirizo
	 **/
	@GetMapping("/new")
	public String showCreateForm(@PathVariable("userId") Long userId, Model model) { // da vedere, non ricordo se è
																						// userId o solo id

		// preparo DTO vuoto da riempire
		model.addAttribute("addressDTO", new AddressDTO());
		model.addAttribute("userId", userId);
		return "address/form"; // Thymeleaf view: src/main/resources/templates/address/form.htm
	}

	/**
	 * gestisce il submit del form di creazione
	 **/
	@PostMapping
	public String processCreateForm(@PathVariable("userId") Long userId,
			@ModelAttribute("addressDTO") @Valid AddressDTO addressDTO, BindingResult bindingResult, Model model) {

		//se la validazione fallsice rimando al form 
		if(bindingResult.hasErrors()) {
			model.addAttribute("userId", userId); 
			return "address/form";
		}
		
		//creo l'indirizzo collegato all'utente 
		try {
			addressService.addAddress(userId, addressDTO);
		}catch (Exception ex){
			
			//vuol dire che l'utente non esiste e allora mostro un errore
			model.addAttribute("errore", ex.getMessage());
			return "error/404";
		}
		
		//redirct alla liste degli indirizzi per evitare un doppio submit
		return "redirect:/users/" + userId + "/address";
	}
	
	/**
	 * mostra la lista degli indirizzi esistenti per l'utente 
	 **/
	@GetMapping
	public String listAddress(@PathVariable("userId") Long userId, Model model) {
		model.addAttribute("addressses", addressService.getAllAddresses(userId));
		model.addAttribute("userId", userId); 
		return "address/list"; // Thymeleaf view per elenco indirizzi
	}
	
}
