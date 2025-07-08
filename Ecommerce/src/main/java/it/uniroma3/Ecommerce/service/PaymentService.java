package it.uniroma3.Ecommerce.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.Ecommerce.model.Address;
import it.uniroma3.Ecommerce.model.Payment;
import it.uniroma3.Ecommerce.model.PaymentDTO;
import it.uniroma3.Ecommerce.model.User;
import it.uniroma3.Ecommerce.repository.PaymentRepository;
import it.uniroma3.Ecommerce.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

	private final UserRepository userRepository;
	private final PaymentRepository paymentRepository;

	
	public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository) {
		this.userRepository = userRepository; 
		this.paymentRepository = paymentRepository;
	}
	
	/**
	 * salvo il metodo di pagamento 
	 **/
	public void save(Payment payment) {
		paymentRepository.save(payment);
	}
	

	/**
	 * aggiunge un nuovo metodo di pagamento all'utente indicato. Lancia una
	 * ResponseStatusException 404 se l'utente non esiste
	 **/
	public PaymentDTO addPayment(Long userId, PaymentDTO paymentDTO) {
		
		//verifico esistenza user
		User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Utente non trovato: " + userId));
		
		//DTO --> Entity 
		Payment payment = paymentDTO.toEntity();
		payment.setUser(user);
		
		//salvo direttamente nell'entita Payment
		Payment saved = paymentRepository.save(payment);
		return PaymentDTO.fromEntity(saved);
	}
	
	/**
	 * elenco tutti i metodi di pagamento dell'utente 
	 * @return DTO mappati
	 **/
	@Transactional(readOnly = true)
	public List<PaymentDTO> getAllPayment(Long userId){
		
		//verifica esistenza utente
		if(!userRepository.existsById(userId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato: " + userId);
		}
		return paymentRepository.findByUserId(userId).stream().map(PaymentDTO::fromEntity).toList();
	}
	
	
	/**
	 * aggiorna i metodi di pagamento dell'utente 
	 **/
	public PaymentDTO updatePayment(Long userId, Long paymentId, PaymentDTO dto) {
		
		Payment pay = paymentRepository.findById(paymentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento non trovato: " + paymentId));
		
		if(!pay.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Pagamento non appartiene all'utente");
		}
		
		//applico le modifiche
		pay.setCardholder(dto.getCardholder());
		pay.setCardnumber(dto.getCardnumber());
		pay.setExpiration(dto.getExpiration());
		pay.setCvv(dto.getCvv());
		
		Payment updated = paymentRepository.save(pay);
		return PaymentDTO.fromEntity(updated);
	}
	
	
	/**
	 * rimuove un pagamento esistente 
	 **/
	public void removePayment(Long userId, Long paymentId) {
		
		Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento non trovato: " + paymentId));
		
		if(!payment.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Pagamento non appartiene all'utente");
		}
		
		paymentRepository.delete(payment);
	}
	

}
