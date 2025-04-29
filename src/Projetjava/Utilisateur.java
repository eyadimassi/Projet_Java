package Projetjava;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Utilisateur {
	private int id;
    private String username;
    private String email;
    private String password;
    private String role="client";
    private String adress;
    private LocalDate created_at;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getAdress() {
        return adress;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
	    if (validerEmail(email)) {
	        this.email = email;
	    } else {
	        throw new IllegalArgumentException("Invalid email format!");
	    }
	}

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }
    
    public static boolean validerEmail(String email) {
		String regex = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$";
		return Pattern.matches(regex, email);
		}
    
    public Utilisateur(int id, String username, String email, String password, String role, String adress, LocalDate created_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.adress = adress;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", role=" + role + ", adress=" + adress + ", created_at=" + created_at + '}';
    }
    

}
