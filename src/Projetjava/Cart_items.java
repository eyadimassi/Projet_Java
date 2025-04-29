package Projetjava;

public class Cart_items {
    private int id;
    private int cart_id;
    private int product_id;
    private int quantite;

    public int getId() {
        return id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Cart_items(int id, int cart_id, int product_id, int quantite) {
        this.id = id;
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "Cart_items{" + "id=" + id + ", cart_id=" + cart_id + ", product_id=" + product_id + ", quantite=" + quantite + '}';
    }
    
}
