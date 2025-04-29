package Projetjava;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
	private Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    
    
    // 1. Create Order
    public int createOrder(int userId, int cartId, int totalPrice, int totalProducts, String delivery) {
    Cart_itemsDAO cartItemsDAO = new Cart_itemsDAO(this.connection); // Create instance to use isCartEmpty

    if (cartItemsDAO.isCartEmpty(cartId)) {
        throw new RuntimeException("Cannot place an order: The cart is empty.");
    }

    String sql = "INSERT INTO `order` (user_id, cart_id, statut, created_at, total_price, total_products, delivery) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, userId);
        stmt.setInt(2, cartId);
        stmt.setString(3, "pending");
        stmt.setDate(4, Date.valueOf(LocalDate.now()));
        stmt.setInt(5, totalPrice);
        stmt.setInt(6, totalProducts);
        stmt.setString(7, delivery);
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // Return the generated order ID
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la création de la commande", e);
    }

    return -1;
}

    
    // 2. Get Order by ID
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Order(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("cart_id"),
                    rs.getString("statut"),
                    rs.getDate("created_at").toLocalDate(),
                    rs.getInt("total_price"),
                    rs.getInt("total_products"),
                    rs.getString("delivery")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving order by ID", e);
        }
        return null;
    }

    // 3. Get Orders by User ID
    public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT * FROM `order` WHERE user_id = ?";
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(new Order(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("cart_id"),
                    rs.getString("statut"),
                    rs.getDate("created_at").toLocalDate(),
                    rs.getInt("total_price"),
                    rs.getInt("total_products"),
                    rs.getString("delivery")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving orders by user ID", e);
        }
        return orders;
    }

    // 4. Update Order Status
    public void updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE `order` SET statut = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating order status", e);
        }
    }
    
    public List<Order> getAllOrders() {
    String sql = "SELECT * FROM `order`";  // Replace with your actual table name, if different
    List<Order> orders = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Order order = new Order(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("cart_id"),
                rs.getString("statut"),
                rs.getDate("created_at").toLocalDate(),
                rs.getInt("total_price"),
                rs.getInt("total_products"),
                rs.getString("delivery")
            );
            orders.add(order);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error retrieving orders", e);
    }

    return orders;
}

}
