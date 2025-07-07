package it.uniroma3.Ecommerce.model;

/*classe usata per disaccoppiare l'entità jpa dal REST*/
public class AddressDTO {

	private String city;
	private String way;
	private Integer houseNumber;
	private Integer cap;
	
	
	public AddressDTO() { }

    public AddressDTO(String city, String way, Integer houseNumber, Integer cap) {
        this.city = city;
        this.way = way;
        this.houseNumber = houseNumber;
        this.cap = cap;
    }

	// getter e setter
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
		return houseNumber;
	}

	public void setHouseNumber(Integer houseNumber) {
		this.houseNumber = houseNumber;
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
        a.setHouse_number(this.houseNumber);
        a.setCap(this.cap);
        return a;
    }

    /** Crea una DTO a partire da un’Address */
    public static AddressDTO fromEntity(Address a) {
        return new AddressDTO(
            a.getCity(),
            a.getWay(),
            a.getHouse_number(),
            a.getCap()
        );
    }
}
