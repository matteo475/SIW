package it.uniroma3.Ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


import it.uniroma3.Ecommerce.model.Credentials;
import it.uniroma3.Ecommerce.model.Payment;
import it.uniroma3.Ecommerce.model.PaymentDTO;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.service.CredentialsService;
import it.uniroma3.Ecommerce.service.PaymentService;
import jakarta.validation.Valid;

@Controller
public class UserPayamentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@GetMapping("/profile/payments")
	public String showMyPayment(Authentication authentication, Model model) {
		
		
		//reindirizza al login se non autenticato 
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/login";
		}
		
		//estraggo lo username
		Object principal = authentication.getPrincipal();
		String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
		
		//recupero le Credentials e lo User
		Credentials creds = credentialsService.getCredentials(username);
		if(creds == null || creds.getUser() == null) {
			return "redirect:/login?error";
		}
		User user = creds.getUser();
		
		
		//chiamo il service per ottenere i DTO dei payment
		List<PaymentDTO> payments = paymentService.getAllPayment(user.getId());
		
		//aggiungo tutto al Model e ritorno la view
		model.addAttribute("user",user); 
		model.addAttribute("payments", payments); 
		return "payment/showPayment";
	}
	
	@GetMapping("/profile/newPayment")
	public String showCreatePaymentFor(Model model) {
		PaymentDTO dto = new PaymentDTO();
		model.addAttribute("paymentDTO",dto);
		return "/payment/newPayment.html";
	}
	
	@PostMapping("/profile/newPayment")
	public String createPayment(@Valid @ModelAttribute PaymentDTO paymentDTO, BindingResult result, Authentication authentication) {
		
		//verifico se ci sono errori 
		if(result.hasErrors()) {
			return "/payment/showPayment.html";
		}
		
		//passo i valori di PaymentDTO a Payment in modo da creare l'oggetto vero e proprio
		Payment pay = new Payment();
		pay.setCardholder(paymentDTO.getCardholder());
		pay.setCardnumber(paymentDTO.getCardnumber());
		pay.setCvv(paymentDTO.getCvv());
		pay.setExpiration(paymentDTO.getExpiration()); 
		
		Object principal = authentication.getPrincipal();
		String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
		
		Credentials creds = credentialsService.getCredentials(username);
		if(creds == null || creds.getUser() == null) {
			return "redirect:/login?error";
		}
		
		User user = creds.getUser();
		pay.setUser(user);
		paymentService.save(pay);
		
		return "redirect:/profile/payments";
	}
	

}
