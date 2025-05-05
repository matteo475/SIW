package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Azienda;
import com.example.demo.repository.AziendaRepository;

@Service
public class AziendaService {

	@Autowired private AziendaRepository aziendaRepository;
	
	public Azienda getById(String id) {
		return this.aziendaRepository.findById(id).get();
	}
	
	
}
