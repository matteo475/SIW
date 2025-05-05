package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Azienda;
import com.example.demo.repository.AziendaRepository;
import com.example.demo.service.AziendaService;

@Controller
public class AziendaController {

	@Autowired private AziendaService aziendaService;

	
	@GetMapping("/admin")
	private String home(Model model) {
		Azienda azienda = this.aziendaService.getById("FA53556TR");
		model.addAttribute("azienda", azienda);
		return "index/companyPrivate";
	}
	
	 
	
}
