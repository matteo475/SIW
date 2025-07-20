package it.uniroma3.Ecommerce.DTOmodel;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDto {
	
	@NotEmpty(message = "Inserisci il nome")
	private String name; 
	
	@NotEmpty(message = "Inserisci il brand")
	private String brand; 
	
	@NotEmpty(message = "Inserisci la categoria")
	private String category; 
	
	@Min(0)
	private double price; 
	
	@Size(min = 10, message = "La descrizione deve contenere almeno 10 caratteri")
	@Size(max = 2000, message = "La descrizione non può contenere più di 2000 caratteri")
	private String description; 
	
	@NotNull(message = "Devi caricare almeno una immagine")
	private MultipartFile imageFile;

	
	//metodi getter e setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	
	
	
	
	
}
