package it.uniroma3.Ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.Ecommerce.model.Address;
import it.uniroma3.Ecommerce.model.AddressDTO;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.repository.AddressRepository;
import it.uniroma3.Ecommerce.repository.UserRepository;

@Service
public class AddressService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	/**
	 * aggiunge un nuovo indirizzo per l'utente specificato.
	 * 
	 * @exception se l'utente non esiste
	 **/
	public Address addAddress(Long userId, AddressDTO addressDto) {

		// recupero l'utente
		Optional<User> optUser = userRepository.findById(userId);

		// se non è presente
		if (!optUser.isPresent()) {
			throw new RuntimeException("Utente con id = " + userId + " non trovato");
		}

		User user = optUser.get();

		// DTO --> ENTITY
		Address address = addressDto.toEntity();

		// collego l'indirizzo all'utente
		address.setUser(user);
		user.getAllAddress().add(address);

		// salvo l'utente che persiste anche nell'indirizzo (CASCADE ALL)
		userRepository.save(user);
		return address;
	}

	/**
	 * rimuove un indirizzo esistente dall'utente.
	 * 
	 * @exception se l'indirizzo o l'utente non esistono
	 **/
	public void removeAddress(Long userId, Long addressId) {

		// recupero l'indirizzo
		Optional<Address> optAddress = addressRepository.findById(addressId);
		if (!optAddress.isPresent() || optAddress.get().getUser() == null
				|| !optAddress.get().getUser().getId().equals(userId)) {
			throw new RuntimeException("Indirizzo con id = " + addressId + " non trovato");
		}

		Address address = optAddress.get();

		// verifico che appartiene all'utente
		User user = address.getUser();
		if (user == null || !user.getId().equals(userId)) {
			throw new RuntimeException("Indirizzo non appartiene all'utente id = " + userId);
		}

		// rimuovo l'associazione e poi salvo
		user.getAllAddress().remove(address);
		address.setUser(null);
		userRepository.save(user);
	}

	/**
	 * restituisce la lista di ogni indirizzo di un utente
	 * 
	 * @exception se l'utente non esiste
	 **/
	public List<AddressDTO> getAllAddresses(Long userId) {

		// verifico se esiste l'utente
		Optional<User> optUser = userRepository.findById(userId);
		if (!optUser.isPresent()) {
			throw new RuntimeException("User con id = " + userId + " non trovato");
		}

		List<Address> entita = addressRepository.findByUserId(userId);
		List<AddressDTO> dto = new ArrayList<>();
		for (Address a : entita) {
			dto.add(AddressDTO.fromEntity(a));
		}

		// utilizzo la funzione della repository
		return dto;
	}

	/**
	 * aggiorna l'indirizzo esistente
	 * 
	 * @return l'indirizzo DTO aggiornato
	 * @exception se non esiste l'utente
	 * @exception se l'indirizzo non esiste
	 * 
	 **/
	public AddressDTO updateAdress(Long userId, Long addressId, AddressDTO dto) {

		// verifico l'utente
		Optional<User> optUser = userRepository.findById(userId);
		if (!optUser.isPresent()) {
			throw new RuntimeException("User con id = " + userId + " non esiste");
		}

		// prendo l'utente
		User user = optUser.get();

		// cerco l'indirizzo
		Optional<Address> optAddress = addressRepository.findById(addressId);
		if (!optAddress.isPresent() || !optAddress.get().getUser().getId().equals(userId)) {
			throw new RuntimeException("Address id" + addressId);
		}

		// prendo l'indirizzo
		Address address = optAddress.get();

		// applico le modifiche dal DTO
		address.setCity(dto.getCity());
		address.setWay(dto.getWay());
		address.setHouse_number(dto.getHouseNumber());
		address.setCap(dto.getCap());

		//salvo le modifiche
		addressRepository.save(address);
		return AddressDTO.fromEntity(address);

	}

}
