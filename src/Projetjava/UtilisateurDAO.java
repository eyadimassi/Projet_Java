package Projetjava;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class UtilisateurDAO {
    private Connection connection;
    
    public UtilisateurDAO(Connection connection) {
        this.connection = connection;
    }
    
    public void ajouterUtilisateur(Utilisateur utilisateur){
        String sql="insert into users (username,email,password,adress,role,created_at) values (?,?,?,?,?,NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getUsername());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4,utilisateur.getAdress());
            stmt.setString(5,utilisateur.getRole());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur ajouté avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur", e);
        }
    }
    
    	public Utilisateur getUtilisateurById(int id) {
	    String sql = "SELECT * FROM users WHERE id = ?";
	    Utilisateur utilisateur = null;

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            utilisateur = new Utilisateur(
	                rs.getInt("id"),
	                rs.getString("username"),
	                rs.getString("email"),
	                rs.getString("password"),
                    rs.getString("role"),
	                rs.getString("adress"),
                    rs.getDate("created_at").toLocalDate()
                        
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Erreur lors de la récupération de l'utilisateur", e);
	    }

	    return utilisateur;
	}
    
        
        
      public Utilisateur getUtilisateurByUsernameAndPassword(String username, String password) {
    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
    Utilisateur utilisateur = null;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            utilisateur = new Utilisateur(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("adress"),
                rs.getDate("created_at").toLocalDate()
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la récupération de l'utilisateur par username et password", e);
    }

    return utilisateur;
}
      
      
      public List<Utilisateur> getTousLesUtilisateurs() {
	    List<Utilisateur> utilisateurs = new ArrayList<>();
	    String sql = "SELECT * FROM users";

	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Utilisateur utilisateur = new Utilisateur(
	                rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("adress"),
                        rs.getDate("created_at").toLocalDate()
	            );
	            utilisateurs.add(utilisateur);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Erreur lors de la récupération des utilisateurs", e);
	    }

	    return utilisateurs;
	}
      
      
      public void mettreAJourUtilisateur(Utilisateur utilisateur) {
    String sql = "UPDATE users SET username = ?, email = ?, password = ?, adress = ?, role = ?, created_at = ? WHERE id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, utilisateur.getUsername());
        stmt.setString(2, utilisateur.getEmail());
        stmt.setString(3, utilisateur.getPassword());
        stmt.setString(4, utilisateur.getAdress());
        stmt.setString(5, utilisateur.getRole());
        stmt.setDate(6, java.sql.Date.valueOf(utilisateur.getCreated_at())); // LocalDate to java.sql.Date
        stmt.setInt(7, utilisateur.getId());

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", e);
    }
}

      
      public void supprimerUtilisateur(int id) {
    String sql = "DELETE FROM users WHERE id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
    }
}
      
   public boolean utilisateurExiste(String username, String password) {
    Utilisateur utilisateur = getUtilisateurByUsernameAndPassword(username, password);
    return utilisateur != null;
}   
}
