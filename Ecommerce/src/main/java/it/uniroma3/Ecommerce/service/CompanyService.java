package it.uniroma3.Ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.Ecommerce.model.Company;
import it.uniroma3.Ecommerce.repository.CompanyRepository;
import it.uniroma3.Ecommerce.repository.ProductRepository;

@Service
public class CompanyService {

	@Autowired private CompanyRepository companyRepository;
	@Autowired private ProductRepository prodRepository;
	
	public Company creaCompany() {
		Company azienda = new Company();
		azienda.setName("amazzon");
		azienda.setNum_employee(3);
		azienda.setP_iva(1010);
		azienda.setProdotti(this.prodRepository.findAll());
		this.companyRepository.save(azienda);
		return azienda;
	}
	
	
	
}
