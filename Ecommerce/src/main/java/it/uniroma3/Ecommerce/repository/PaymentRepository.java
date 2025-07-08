package it.uniroma3.Ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.Ecommerce.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long>{
	List<Payment> findByUserId(Long userId);
}
