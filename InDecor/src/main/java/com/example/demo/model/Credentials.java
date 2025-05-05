package com.example.demo.model;

import java.util.Objects;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "credenziali")
public class Credentials {

    // Definisco i vari ruoli o le varie authorities per accedere al sito
    public static final String USER_ROLE = "Utente";
    public static final String ADMIN_ROLE = "Admin";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 45)
    private String username;

    @NotBlank
    @Column(nullable = false, unique = true, length = 45)
    private String password;

    private String ruolo;

    // Aggiunti i nuovi campi richiesti
    @NotNull
    @Column(nullable = false)
    private String gender;  // Sesso (es. "Maschio", "Femmina", "Altro")

    @Past
    @NotNull
    @Column(nullable = false)
    private Date dateOfBirth;  // Data di nascita

    @NotBlank
    @Column(nullable = false, length = 100)
    private String luogo;  // Luogo di nascita o di residenza
   
    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

 
	@Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Credentials that = (Credentials) obj;
        return this.id == that.getId() && this.username.equals(that.getUsername()) &&
               this.password.equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, username);
    }
}
