package it.uniroma3.Ecommerce.DTOmodel;

import it.uniroma3.Ecommerce.model.Address;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/*classe usata per disaccoppiare l'entità jpa dal REST*/
public class AddressDTO {

	private Long id;
	
	@NotBlank(message ="Il campo Citta non può essere vuoto")
	private String city;
	
	@NotBlank(message ="Il campo Via non può essere vuoto")
	private String way;
	
	@Min(0)
	private Integer house_number;
	
	@Min(0)
	private Integer cap;

	// costruttori
	public AddressDTO() {
	}

	public AddressDTO(Long id, String city, String way, Integer houseNumber, Integer cap) {
		this.id = id;
		this.city = city;
		this.way = way;
		this.house_number = houseNumber;
		this.cap = cap;
	}

	// getter e setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public Integer getHouseNumber() {
		return house_number;
	}

	public void setHouseNumber(Integer houseNumber) {
		this.house_number = houseNumber;
	}

	public Integer getCap() {
		return cap;
	}

	public void setCap(Integer cap) {
		this.cap = cap;
	}

	/** Converte questa DTO in entità Address */
	public Address toEntity() {
		Address a = new Address();
		a.setCity(this.city);
		a.setWay(this.way);
		a.setHouse_number(this.house_number);
		a.setCap(this.cap);
		return a;
	}

	/** Crea una DTO a partire da un’Address */
	public static AddressDTO fromEntity(Address a) {
		return new AddressDTO(a.getId(), a.getCity(), a.getWay(), a.getHouse_number(), a.getCap());
	}
}
