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
        if (connection == null) {
            throw new IllegalArgumentException("Database connection cannot be null for UtilisateurDAO");
        }
        this.connection = connection;
    }

    public void ajouterUtilisateur(Utilisateur utilisateur){
        String roleToInsert = (utilisateur.getRole() == null || utilisateur.getRole().trim().isEmpty()) ? "client" : utilisateur.getRole();

        String sql="insert into users (username,email,password,adress,role,created_at) values (?,?,?,?,?,NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getUsername());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4,utilisateur.getAdress());
            stmt.setString(5,roleToInsert);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur ajouté avec succès !");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                 if (e.getMessage().contains("username")) {
                    throw new RuntimeException("Username '" + utilisateur.getUsername() + "' already exists.", e);
                 } else if (e.getMessage().contains("email")) {
                     throw new RuntimeException("Email '" + utilisateur.getEmail() + "' already exists.", e);
                 }
            }
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
	            utilisateur = mapResultSetToUtilisateur(rs);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Erreur lors de la récupération de l'utilisateur par ID", e);
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
                utilisateur = mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur par username et password", e);
        }

        return utilisateur;
      }

      public boolean usernameExists(String username) {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la vérification de l'existence du username", e);
            }
            return false;
      }

      public boolean emailExists(String email) {
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la vérification de l'existence de l'email", e);
            }
            return false;
      }


      public List<Utilisateur> getTousLesUtilisateurs() {
	    List<Utilisateur> utilisateurs = new ArrayList<>();
	    String sql = "SELECT * FROM users";

	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            utilisateurs.add(mapResultSetToUtilisateur(rs));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Erreur lors de la récupération des utilisateurs", e);
	    }

	    return utilisateurs;
	}


      public void mettreAJourUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, adress = ?, role = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getUsername());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4, utilisateur.getAdress());
            stmt.setString(5, utilisateur.getRole());
            stmt.setInt(6, utilisateur.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                 if (e.getMessage().contains("username")) {
                    throw new RuntimeException("Cannot update: Username '" + utilisateur.getUsername() + "' already exists for another user.", e);
                 } else if (e.getMessage().contains("email")) {
                     throw new RuntimeException("Cannot update: Email '" + utilisateur.getEmail() + "' already exists for another user.", e);
                 }
            }
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

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
         java.sql.Date createdAtSql = rs.getDate("created_at");
         java.time.LocalDate createdAt = (createdAtSql != null) ? createdAtSql.toLocalDate() : null;

        return new Utilisateur(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("adress"),
                createdAt
        );
    }
}