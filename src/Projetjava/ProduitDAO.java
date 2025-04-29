package Projetjava;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProduitDAO {
	private Connection connection;

	public ProduitDAO(Connection connection) {
        this.connection = connection;
    }
        
    public void ajouterProduct(Produit product) {
    String sql = "INSERT INTO produit (nom, description, prix, category_id, marque_id, created_at, image_path, imagehover_path, ordered, shade) VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, product.getNom());
        stmt.setString(2, product.getDescription());
        stmt.setInt(3, product.getPrix());
        stmt.setInt(4, product.getCategorie_id());
        stmt.setInt(5, product.getMarque_id());
        stmt.setString(6, product.getImage_path());
        stmt.setString(7, product.getImagehover_path());
        stmt.setInt(8, product.getOrdered());
        stmt.setString(9, product.getShade());

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de l'ajout du produit", e);
    }
}

    public Produit getProductById(int id) {
    String sql = "SELECT * FROM produit WHERE id = ?";
    Produit produit = null;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
                produit = new Produit(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("prix"),
                rs.getInt("category_id"),
                rs.getInt("marque_id"),
                rs.getDate("created_at").toLocalDate(),
                rs.getString("image_path"),
                rs.getString("imagehover_path"),
                rs.getInt("ordered"),
                rs.getString("shade")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la récupération du produit", e);
    }

    return produit;
}
    
    public void mettreAJourProduct(Produit product) {
    String sql = "UPDATE produit SET nom = ?, description = ?, prix = ?, category_id = ?, marque_id = ?, image_path = ?, imagehover_path = ?, ordered = ?, shade = ? WHERE id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, product.getNom());
        stmt.setString(2, product.getDescription());
        stmt.setInt(3, product.getPrix());
        stmt.setInt(4, product.getCategorie_id());
        stmt.setInt(5, product.getMarque_id());
        stmt.setString(6, product.getImage_path());
        stmt.setString(7, product.getImagehover_path());
        stmt.setInt(8, product.getOrdered());
        stmt.setString(9, product.getShade());
        stmt.setInt(10, product.getId());

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la mise à jour du produit", e);
    }
}
    public void supprimerProduct(int id) {
    String sql = "DELETE FROM produit WHERE id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la suppression du produit", e);
    }
}
    
    public List<Produit> listerTousLesProduits() {
    List<Produit> produits = new ArrayList<>();
    String sql = "SELECT * FROM produit";

    try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
        

        while (rs.next()) {
                Produit produit = new Produit(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("prix"),
                rs.getInt("category_id"),
                rs.getInt("marque_id"),
                rs.getDate("created_at").toLocalDate(),
                rs.getString("image_path"),
                rs.getString("imagehover_path"),
                rs.getInt("ordered"),
                rs.getString("shade")
            );
            produits.add(produit);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la récupération des produits", e);
    }

    return produits;
}
    public int getProductPrice(int productId) {
        String sql = "SELECT prix FROM produit WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("prix");
            } else {
                throw new RuntimeException("Product not found with ID: " + productId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting price for product ID: " + productId, e);
        }
    }
    
    public List<Produit> getProductsByBrandName(String brandName) {
    List<Produit> produits = new ArrayList<>();
    String sql = "SELECT p.* FROM produit p " +
                 "INNER JOIN marque b ON p.marque_id = b.id " +
                 "WHERE b.nom = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, brandName);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Produit produit = new Produit(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("prix"),
                rs.getInt("category_id"),
                rs.getInt("marque_id"),
                rs.getDate("created_at").toLocalDate(),
                rs.getString("image_path"),
                rs.getString("imagehover_path"),
                rs.getInt("ordered"),
                rs.getString("shade")
            );
            produits.add(produit);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la récupération des produits par marque", e);
    }

    return produits;
}

    public List<Produit> getProductsByCategoryName(String categoryName) {
    List<Produit> produits = new ArrayList<>();
    String sql = "SELECT p.* FROM produit p " +
                 "INNER JOIN categories c ON p.category_id = c.id " +
                 "WHERE c.name = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, categoryName);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Produit produit = new Produit(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("prix"),
                rs.getInt("category_id"),
                rs.getInt("marque_id"),
                rs.getDate("created_at").toLocalDate(),
                rs.getString("image_path"),
                rs.getString("imagehover_path"),
                rs.getInt("ordered"),
                rs.getString("shade")
            );
            produits.add(produit);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la récupération des produits par catégorie", e);
    }

    return produits;
}

    public List<Produit> getProductsByPrice(boolean ascending) {
    List<Produit> produits = new ArrayList<>();
    String orderBy = ascending ? "ASC" : "DESC";  // Choisir l'ordre en fonction du paramètre

    String sql = "SELECT * FROM produit ORDER BY prix " + orderBy;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Produit produit = new Produit(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("prix"),
                rs.getInt("category_id"),
                rs.getInt("marque_id"),
                rs.getDate("created_at").toLocalDate(),
                rs.getString("image_path"),
                rs.getString("imagehover_path"),
                rs.getInt("ordered"),
                rs.getString("shade")
            );
            produits.add(produit);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la récupération des produits triés par prix", e);
    }

    return produits;
}

    public void incrementProductOrdered(int productId) {
    String sql = "UPDATE produit SET ordered = ordered + 1 WHERE id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, productId);
        int rowsAffected = stmt.executeUpdate();
        
        if (rowsAffected == 0) {
            throw new RuntimeException("Produit non trouvé avec l'ID " + productId);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de l'actualisation du nombre de commandes pour le produit", e);
    }
    }

}
