package it.uniroma3.Ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.Ecommerce.DTOmodel.AddressDTO;
import it.uniroma3.Ecommerce.model.Address;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.repository.AddressRepository;
import it.uniroma3.Ecommerce.repository.UserRepository;

@Service
@Transactional
public class AddressService {

	private final UserRepository userRepo;
	private final AddressRepository addrRepo;

	public AddressService(UserRepository userRepo, AddressRepository addrRepo) {
		this.userRepo = userRepo;
		this.addrRepo = addrRepo;
	}

	/**
	 * Aggiunge un nuovo indirizzo all'utente indicato. Lancia
	 * ResponseStatusException 404 se l'utente non esiste.
	 */
	public AddressDTO addAddress(Long userId, AddressDTO dto) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato: " + userId));

		// DTO → Entity
		Address address = dto.toEntity();
		address.setUser(user);

		// Salva direttamente l'entità indirizzo
		Address saved = addrRepo.save(address);
		return AddressDTO.fromEntity(saved);
	}
	
	//funzione per salvare il prodotto 
		public void save(Address address) {
			addrRepo.save(address);
		}

	/**
	 * Elenca tutti gli indirizzi dell'utente.
	 * @return DTO mappati
	 */
	@Transactional(readOnly = true)
	public List<AddressDTO> getAllAddresses(Long userId) {
		// Verifica esistenza utente
		if (!userRepo.existsById(userId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato: " + userId);
		}

		return addrRepo.findByUserId(userId).stream().map(AddressDTO::fromEntity).toList();
	}

	/**
	 * Aggiorna un indirizzo esistente.
	 */
	public AddressDTO updateAddress(Long userId, Long addressId, AddressDTO dto) {
		Address addr = addrRepo.findById(addressId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indirizzo non trovato: " + addressId));

		if (!addr.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Indirizzo non appartiene all'utente");
		}

		// Applica modifiche
		addr.setCity(dto.getCity());
		addr.setWay(dto.getWay());
		addr.setHouse_number(dto.getHouseNumber());
		addr.setCap(dto.getCap());

		Address updated = addrRepo.save(addr);
		return AddressDTO.fromEntity(updated);
	}

	/**
	 * Rimuove un indirizzo esistente.
	 */
	public void removeAddress(Long userId, Long addressId) {
		Address addr = addrRepo.findById(addressId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indirizzo non trovato: " + addressId));

		if (!addr.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Indirizzo non appartiene all'utente");
		}

		addrRepo.delete(addr);
	}
}
