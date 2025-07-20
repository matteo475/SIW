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
	 * metodo che aggiunge un nuovo indirizzo all'utente indicato.
	 * 
	 * @exception ResponseStatusException 404 se l'utente non esiste.
	 * @param id dell'utente
	 * @param addressDTO
	 * @return address aggiunto all'utente
	 */
	public AddressDTO addAddress(Long userId, AddressDTO dto) {

		// recuper l'utente
		Optional<User> userOpt = userRepo.findById(userId);

		// Se non esiste, lancio l'eccezione
		if (!userOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato: " + userId);
		}

		// Estraggo l'utente dall'Optional
		User user = userOpt.get();

		// DTO → Entity
		Address address = dto.toEntity();
		address.setUser(user);

		// Salva direttamente l'entità indirizzo
		Address saved = addrRepo.save(address);
		return AddressDTO.fromEntity(saved);
	}

	/**
	 * metodo per salvare un indirizzo
	 * 
	 * @parame address da salvare
	 * @return address salvato grazie alla Repository
	 */
	public void save(Address address) {
		addrRepo.save(address);
	}

	/**
	 * Elenca tutti gli indirizzi dell'utente.
	 * 
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
	 * metodo per oggiornare un indirizzo esistente
	 * 
	 * @param id dell'utente
	 * @param id dell'address associato all'utente
	 * @param oggetto transiente addressDTO
	 */
	public AddressDTO updateAddress(Long userId, Long addressId, AddressDTO dto) {
		
		// Recupera l'Optional<Address>
		Optional<Address> addrOpt = addrRepo.findById(addressId);

		// Se non esiste, lancio l'eccezione NOT_FOUND
		if (!addrOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Indirizzo non trovato: " + addressId);
		}

		// Estraggo l'entità Address
		Address addr = addrOpt.get();
		
		// Controllo che l'indirizzo appartenga all'utente
	    if (!addr.getUser().getId().equals(userId)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Indirizzo non appartiene all'utente");
	    }

		// Applica modifiche
		addr.setCity(dto.getCity());
		addr.setWay(dto.getWay());
		addr.setHouse_number(dto.getHouseNumber());
		addr.setCap(dto.getCap());

		//salvo le modifiche
		Address updated = addrRepo.save(addr);
		return AddressDTO.fromEntity(updated);
	}

	/**
	 * metodo per rimuovere un indirrizzo dato il suo id
	 * 
	 * @param id dell'utente
	 * @param id dell'indirizzo da rimuovere
	 * @return rimozione dell'indirizzo
	 */
	public void removeAddress(Long userId, Long addressId) {
		//recupera l'Optional<Address>
	    Optional<Address> addrOpt = addrRepo.findById(addressId);

	    //se non esiste, lancio l'eccezione NOT_FOUND
	    if (!addrOpt.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Indirizzo non trovato: " + addressId);
	    }

	    //estrai l'entità dall'Optional
	    Address addr = addrOpt.get();

	    //verifica che l'indirizzo appartenga all'utente
	    if (!addr.getUser().getId().equals(userId)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Indirizzo non appartiene all'utente");
	    }

		addrRepo.delete(addr);
	}
}
