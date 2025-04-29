package Projetjava;
import java.time.*;

public class Order {
	private int id;
    private int user_id;
    private int cart_id;
    private String statut;
    private LocalDate created_at;
    private int total_price;
    private int total_products;
    private String delivery;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public String getStatut() {
        return statut;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public int getTotal_price() {
        return total_price;
    }

    public int getTotal_products() {
        return total_products;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public void setTotal_products(int total_products) {
        this.total_products = total_products;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", user_id=" + user_id + ", cart_id=" + cart_id + ", statut=" + statut + ", created_at=" + created_at + ", total_price=" + total_price + ", total_products=" + total_products + ", delivery=" + delivery + '}';
    }

    public Order(int id, int user_id, int cart_id, String statut, LocalDate created_at, int total_price, int total_products, String delivery) {
        this.id = id;
        this.user_id = user_id;
        this.cart_id = cart_id;
        this.statut = statut;
        this.created_at = created_at;
        this.total_price = total_price;
        this.total_products = total_products;
        this.delivery = delivery;
    }

}
