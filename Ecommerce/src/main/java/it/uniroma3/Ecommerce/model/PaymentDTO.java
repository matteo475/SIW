package it.uniroma3.Ecommerce.model;

import java.util.Objects;

public class PaymentDTO {

	// variabili di istanza
	private Long id;
	private String expiration;
	private String cardnumber;
	private Integer cvv;
	private String cardholder;
	
	
	public PaymentDTO() {}
	
	public PaymentDTO(Long id, String expiration, String cardnumber, Integer cvv, String cardholder) {
		this.id = id; 
		this.expiration = expiration; 
		this.cardnumber = cardnumber; 
		this.cvv = cvv; 
		this.cardholder = cardholder;
	}
	
	
	
	//metodi getter e setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}
	public Integer getCvv() {
		return cvv;
	}
	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}
	public String getCardholder() {
		return cardholder;
	}
	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}
	
	
	/*Crea un DTO a partire da un Payament*/
	public static PaymentDTO fromEntity(Payment p) {
		return new PaymentDTO(p.getId(),p.getExpiration(),p.getCardnumber(), p.getCvv(), p.getCardholder() );
	}
	
	/**
	 * converte questo DTO in entità Payment 
	 **/
	public Payment toEntity() {
		Payment p = new Payment();
		p.setCardholder(this.cardholder);
		p.setCardnumber(this.cardnumber);
		p.setCvv(this.cvv);
		p.setExpiration(this.expiration);
		return p;
	}
	
	
	
}
