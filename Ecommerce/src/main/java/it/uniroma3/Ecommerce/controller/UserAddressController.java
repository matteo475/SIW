package it.uniroma3.Ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.Ecommerce.model.Address;
import it.uniroma3.Ecommerce.model.AddressDTO;
import it.uniroma3.Ecommerce.model.Credentials;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.service.AddressService;
import it.uniroma3.Ecommerce.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
//@RequestMapping("/profile/addresses")
public class UserAddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private CredentialsService credentialsService;

	
	@GetMapping("/profile/addresses")
	public String showMyAddresses(Authentication authentication, Model model) {

		// 1) Reindirizza al login se non autenticato
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/login";
		}

		// 2) Estrai lo username
		Object principal = authentication.getPrincipal();
		String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername()
				: principal.toString();

		// 3) Recupera le Credentials e la User
		Credentials creds = credentialsService.getCredentials(username);
		if (creds == null || creds.getUser() == null) {
			return "redirect:/login?error";
		}
		User user = creds.getUser();

		// 4) Chiama il service per ottenere i DTO degli indirizzi
		List<AddressDTO> addresses = addressService.getAllAddresses(user.getId());

		// 5) Aggiungi tutto al Model e ritorna la view
		model.addAttribute("user", user);
		model.addAttribute("addresses", addresses);
		return "address/showAddress";
	}
	
	@GetMapping("/profile/newAddress")
	public String showCreateForm(Model model) {
		AddressDTO addressDTO = new AddressDTO();
		model.addAttribute("addressDTO", addressDTO);
		return "/address/newAddress.html";
	}
	
	/*metodo per gestire la creazione di un oggetto address*/
	@PostMapping("/profile/newAddress")
	public String createAddress(@Valid @ModelAttribute AddressDTO addressDTO, BindingResult result,Authentication authentication) {
		
		//se ci sono errori 
		if(result.hasErrors()) {
			return "/address/showAddress.html";
		}
		
		/*passo i valori di addressDTO a address in modo da creare l'oggetto vero e proprio*/
		Address address = new Address();
		address.setWay(addressDTO.getWay());
		address.setCap(addressDTO.getCap());
		address.setHouse_number(addressDTO.getHouseNumber());
		address.setCity(addressDTO.getCity());
		
		Object principal = authentication.getPrincipal();
		String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
		Credentials creds = credentialsService.getCredentials(username);
		if (creds == null || creds.getUser() == null) {
			return "redirect:/login?error";
		}
		User user = creds.getUser();
		address.setUser(user);
		addressService.save(address);
		
		return "redirect:/profile/addresses";		
	}
}
