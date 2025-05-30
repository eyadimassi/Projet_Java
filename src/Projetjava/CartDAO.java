
package Projetjava;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
public class CartDAO {
    private Connection connection;

    public CartDAO(Connection connection) {
        this.connection = connection;
    }

    
    public int getOrCreateCartByUserId(int userId) {
        String sql = "SELECT id FROM cart WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return createCartForUser(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching user cart", e);
        }
    }

    
    public int getOrCreateCartByGuestId(int guestId) {
        String sql = "SELECT id FROM cart WHERE guest_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return createCartForGuest(guestId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching guest cart", e);
        }
    }

    
    private int createCartForUser(int userId) {
        String sql = "INSERT INTO cart (user_id, created_at) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating user cart", e);
        }
        return -1;
    }

    
    private int createCartForGuest(int guestId) {
        String sql = "INSERT INTO cart (guest_id, created_at) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, guestId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating guest cart", e);
        }
        return -1;
    }
    
    
    
    public Cart getCartById(int cartId) {
        String sql = "SELECT * FROM cart WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cart(
                    rs.getInt("id"),
                    rs.getInt("guest_id"),
                    rs.getInt("user_id"),
                    rs.getDate("created_at").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving cart by ID", e);
        }
        return null;
    }
    
    
    public void transferGuestCartToUser(int guestId, int userId) {
        String sql = "UPDATE cart SET guest_id = NULL, user_id = ? WHERE guest_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, guestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error transferring guest cart to user", e);
        }
    }
}