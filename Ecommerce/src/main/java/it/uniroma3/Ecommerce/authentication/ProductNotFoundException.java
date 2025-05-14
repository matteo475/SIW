package it.uniroma3.Ecommerce.authentication;

public class ProductNotFoundException extends Throwable{

	public ProductNotFoundException(String message) {
		super(message);
	}
}
