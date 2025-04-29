package Projetjava;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart_itemsDAO {

    private Connection connection;

    public Cart_itemsDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Database connection cannot be null for Cart_itemsDAO");
        }
        this.connection = connection;
    }

    
    public void addItemOrUpdateQuantity(int cartId, int productId, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity to add must be positive.");
        }

        String selectSql = "SELECT id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
        String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setInt(1, cartId);
            selectStmt.setInt(2, productId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int existingItemId = rs.getInt("id");
                int existingQuantity = rs.getInt("quantity");
                int newQuantity = existingQuantity + quantityToAdd;
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, newQuantity);
                    updateStmt.setInt(2, existingItemId);
                    updateStmt.executeUpdate();
                    System.out.println("Updated quantity for product " + productId + " in cart " + cartId);
                }
            } else {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, cartId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantityToAdd);
                    insertStmt.executeUpdate();
                    System.out.println("Added product " + productId + " to cart " + cartId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding/updating item in cart (cartId: " + cartId + ", productId: " + productId + ")", e);
        }
    }

 
    public List<Cart_items> getItemsByCartId(int cartId) {
        List<Cart_items> items = new ArrayList<>();
        String sql = "SELECT * FROM cart_items WHERE cart_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new Cart_items(
                        rs.getInt("id"),
                        rs.getInt("cart_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving items for cartId: " + cartId, e);
        }
        return items;
    }

    
    public void updateItemQuantity(int cartItemId, int newQuantity) {
         if (newQuantity <= 0) {
             removeItemFromCart(cartItemId);
             return;
        }
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, cartItemId);
            int rowsAffected = stmt.executeUpdate();
             if (rowsAffected == 0) {
                System.err.println("Warning: No cart item found with ID " + cartItemId + " to update.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating quantity for cart item ID: " + cartItemId, e);
        }
    }


    public void removeItemFromCart(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartItemId);
             int rowsAffected = stmt.executeUpdate();
             if (rowsAffected == 0) {
                System.err.println("Warning: No cart item found with ID " + cartItemId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error removing cart item ID: " + cartItemId, e);
        }
    }

    public void clearCart(int cartId) {
         String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error clearing cart ID: " + cartId, e);
        }
    }


    public boolean isCartEmpty(int cartId) {
        String sql = "SELECT COUNT(*) FROM cart_items WHERE cart_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                } else {
                    throw new SQLException("Unexpected error: COUNT(*) query did not return any rows for cartId: " + cartId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking if cart is empty for cartId: " + cartId, e);
        }
    }
}