package it.uniroma3.Ecommerce.controller;

import java.security.Principal;
import java.util.List;

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
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.service.AddressService;
import jakarta.validation.Valid;

@Controller
//@RequestMapping("/user/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;
/*
	@GetMapping
	public String listAddress(Model model, Principal principal) {
		List<AddressDTO> list = addressService.getAllAddresses(principal.getName());
		model.addAttribute("addresses", list);
		return "address/showAddress";
	}*/

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("addressDTO", new AddressDTO());
		return "address/newAddress";
	}

	/*
	@PostMapping
	public String createAddress(@ModelAttribute @Valid AddressDTO dto, BindingResult br, Principal principal) {

		if (br.hasErrors()) {
			return "address/newAddress";
		}

		addressService.addAddress(principal.getName(), dto);
		return "redirect:/user/addresses";
	}*/
}
