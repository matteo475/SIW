package it.uniroma3.Ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.Ecommerce.repository.OrdineRepository;
import it.uniroma3.Ecommerce.repository.VoceOrdineRepository;

@Service
public class OrdineService {

	@Autowired private OrdineRepository ordineRepository;
	@Autowired private VoceOrdineRepository voceOrdineRepository;
}
