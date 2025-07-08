package it.uniroma3.Ecommerce.model;

/*classe usata per disaccoppiare l'entità jpa dal REST*/
public class AddressDTO {

	private Long id;
	private String city;
	private String way;
	private Integer house_number;
	private Integer cap;
	
	
	public AddressDTO() { }

    public AddressDTO(Long id, String city, String way, Integer houseNumber, Integer cap) {
        this.id = id;
    	this.city = city;
        this.way = way;
        this.house_number = houseNumber;
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
        return new AddressDTO(
        	a.getId(),
            a.getCity(),
            a.getWay(),
            a.getHouse_number(),
            a.getCap()
        );
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
