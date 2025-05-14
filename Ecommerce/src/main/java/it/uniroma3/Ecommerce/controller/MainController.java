package it.uniroma3.Ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	/*in questo controller vado a mettere quelle richieste che sono globali ovvero che non sono specifiche di una sola entità*/

	/*
	@GetMapping(""/")
	public String index() {
		return "index.html"; 
	}*/
	
	@GetMapping("/homepage")
	public String showHomepage() {
		return "index.html";
	}
}

