package it.uniroma3.Ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.Ecommerce.authentication.ProductNotFoundException;
import it.uniroma3.Ecommerce.model.Carrello;
import it.uniroma3.Ecommerce.model.Credentials;
import it.uniroma3.Ecommerce.model.Product;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.repository.CarrelloRepository;
import it.uniroma3.Ecommerce.repository.CredentialsRepository;
import it.uniroma3.Ecommerce.repository.UserRepository;



/**
* The UserService handles logic for Users.
*/
@Service
public class UserService {

	@Autowired
	protected UserRepository userRepository;
	@Autowired
	private CarrelloRepository carrelloRepository;
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	
	/**
	 * This method retrieves a User from the DB based on its ID.
	 * 
	 * @param id the id of the User to retrieve from the DB
	 * @return the retrieved User, or null if no User with the passed ID could be
	 *         found in the DB
	 */
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	
	@Transactional
	public User getUser(String email) {
	   Optional<User> user = this.userRepository.findByEmail(email);
	   return user.orElse(null);
	}
	
	/**
	 * This method saves a User in the DB.
	 * 
	 * @param user the User to save into the DB
	 * @return the saved User
	 * @throws DataIntegrityViolationException if a User with the same username as
	 *                                         the passed User already exists in the
	 *                                         DB
	 */
	@Transactional
	public User saveUser(User user) {
	/*in questo metodo creiamo sia l'utente che il suo carrello associato*/
		Carrello carrello = new Carrello();
		user.setCarrello(carrello);
		this.carrelloRepository.save(carrello);
		return this.userRepository.save(user);
	}

	/**
	 * This method retrieves all Users from the DB.
	 * 
	 * @return a List with all the retrieved Users
	 */
	@Transactional
	public List<User> getAllUsers() {
		List<User> result = new ArrayList<>();
		Iterable<User> iterable = this.userRepository.findAll();
		for (User user : iterable)
			result.add(user);
		return result;
	}
	
	@Transactional
	public void updateResetPassword(String email,String token) throws UserNotFoundException {
		User user = this.getUser(email);
		if(user != null) {
			Credentials credenziali = user.getCredentials();
			credenziali.setResetPasswordToken(token);
			this.credentialsRepository.save(credenziali);
		}else {
			throw new UserNotFoundException("non abbiamo trovato alcun credenziali associata a: " + email);
		}
	}
	
	@Transactional
	public User getByResetPasswordToken(String resetPasswordToken) {
		  Credentials credenziali = this.credentialsRepository.findByResetPasswordToken(resetPasswordToken).orElse(null);
		  User user = credenziali.getUser();
		  return user;
	}

	@Transactional
	public void updatePassword(String email, String newPassword) throws UserNotFoundException {
		BCryptPasswordEncoder codificatore = new BCryptPasswordEncoder();
		String encodePassword = codificatore.encode(newPassword);
	    User user = this.getUser(email);
		if(user != null) {
			Credentials credenziali = user.getCredentials();
			credenziali.setPassword(encodePassword);
			credenziali.setResetPasswordToken(null);
			this.credentialsRepository.save(credenziali);
		}else {
			throw new UserNotFoundException("non abbiamo trovato alcun credenziali associata a: " + email);
		}
	}
	
}

