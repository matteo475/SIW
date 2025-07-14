package it.uniroma3.Ecommerce.model;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 

	private String city; 
	private String way;	//rappresenta la via 
	private Integer house_number;	//numero civico
	private Integer cap;
	
	// lato “molti” del rapporto verso User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

	//metodi setter e getter
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
	
	public Integer getHouse_number() {
		return house_number;
	}
	public void setHouse_number(Integer house_number) {
		this.house_number = house_number;
	}
	
	public Integer getCap() {
		return cap;
	}
	public void setCap(Integer cap) {
		this.cap = cap;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	//metodi hash e equals
	@Override
	public int hashCode() {
		return Objects.hash(cap, city, house_number, id, way);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(cap, other.cap) && Objects.equals(city, other.city)
				&& Objects.equals(house_number, other.house_number) && Objects.equals(id, other.id)
				&& Objects.equals(way, other.way);
	}
}
