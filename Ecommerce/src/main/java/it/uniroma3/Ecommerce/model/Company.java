package it.uniroma3.Ecommerce.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name; 
	private Integer p_iva; 	//per la partita iva
	private Integer num_employee; //numero di dipendenti 
	
	//metodi getter e setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getP_iva() {
		return p_iva;
	}
	public void setP_iva(Integer p_iva) {
		this.p_iva = p_iva;
	}
	
	public Integer getNum_employee() {
		return num_employee;
	}
	public void setNum_employee(Integer num_employee) {
		this.num_employee = num_employee;
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(name,p_iva);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return Objects.equals(name, other.name) && Objects.equals(p_iva, other.p_iva);
	}
	
	
}
