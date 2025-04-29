package Projetjava;

public class Order_items {
	 private int id;
	    private int order_id;
	    private int product_id;
	    private int quantity;
	    private int price;

	    public int getId() {
	        return id;
	    }

	    public int getOrder_id() {
	        return order_id;
	    }

	    public int getProduct_id() {
	        return product_id;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public int getPrice() {
	        return price;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public void setOrder_id(int order_id) {
	        this.order_id = order_id;
	    }

	    public void setProduct_id(int product_id) {
	        this.product_id = product_id;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }

	    public void setPrice(int price) {
	        this.price = price;
	    }

	    public Order_items(int id, int order_id, int product_id, int quantity, int price) {
	        this.id = id;
	        this.order_id = order_id;
	        this.product_id = product_id;
	        this.quantity = quantity;
	        this.price = price;
	    }

	    @Override
	    public String toString() {
	        return "order_items{" + "id=" + id + ", order_id=" + order_id + ", product_id=" + product_id + ", quantity=" + quantity + ", price=" + price + '}';
	    }

}
