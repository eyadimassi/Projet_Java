package Projetjava;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientInterface extends JFrame {

    private Utilisateur loggedInUser;
    private Connection connection;
    private ProduitDAO produitDAO;
    private CartDAO cartDAO;
    private Cart_itemsDAO cartItemsDAO;
    private OrderDAO orderDAO;

    private int currentCartId = -1; 


    private JLabel welcomeLabel;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JScrollPane productScrollPane;
    private JTextField searchField;
    private JButton searchButton;
    private JSpinner quantitySpinner; 
    private JButton addToCartButton;


    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JScrollPane cartScrollPane;
    private JSpinner updateQuantitySpinner; 
    private JButton updateCartItemButton;
    private JButton removeCartItemButton;
    private JLabel totalLabel;
    private JComboBox<String> deliveryComboBox;
    private JButton placeOrderButton;


    private List<CartItemDetails> currentCartDetails = new ArrayList<>();


    private static class CartItemDetails {
        int cartItemId;
        int productId;
        String productName;
        int quantity;
        int pricePerItem;
        int lineTotal;

        CartItemDetails(Cart_items item, Produit product) {
            this.cartItemId = item.getId();
            this.productId = item.getProduct_id();
            this.productName = product.getNom();
            this.quantity = item.getQuantite();
            this.pricePerItem = product.getPrix();
            this.lineTotal = this.quantity * this.pricePerItem;
        }
    }


    public ClientInterface(Utilisateur user, Connection conn) {
        super("Welcome, Client!");
        this.loggedInUser = user;
        this.connection = conn;

        if (this.connection == null) {
             JOptionPane.showMessageDialog(this, "Database connection is not available.", "Connection Error", JOptionPane.ERROR_MESSAGE);
             System.exit(1);
        }


        this.produitDAO = new ProduitDAO(this.connection);
        this.cartDAO = new CartDAO(this.connection);
        this.cartItemsDAO = new Cart_itemsDAO(this.connection);
        this.orderDAO = new OrderDAO(this.connection);


        try {
            this.currentCartId = cartDAO.getOrCreateCartByUserId(loggedInUser.getId());
            if (this.currentCartId <= 0) {
                 throw new RuntimeException("Could not get or create a cart for the user.");
            }
        } catch (RuntimeException e) {
             showError("Error initializing cart: " + e.getMessage());
             
             e.printStackTrace();
             
        }


        setTitle("Cosmetics Website - Welcome " + loggedInUser.getUsername() + " (Cart ID: " + currentCartId + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750); 
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();

        loadProductData();
        loadCartItems(); 

        
        boolean cartReady = this.currentCartId > 0;
        addToCartButton.setEnabled(cartReady);
        updateCartItemButton.setEnabled(cartReady);
        removeCartItemButton.setEnabled(cartReady);
        placeOrderButton.setEnabled(cartReady);
    }

    private void initComponents() {
        welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

       
        productTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Price"}, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        productTable = new JTable(productTableModel);
        productScrollPane = new JScrollPane(productTable);
        searchField = new JTextField(20);
        searchButton = new JButton("Search Products");
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); 
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.setEnabled(false); 

        
        cartTableModel = new DefaultTableModel(new Object[]{"Item ID", "Product", "Qty", "Price/Item", "Line Total"}, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.getColumnModel().getColumn(0).setMinWidth(0);
        cartTable.getColumnModel().getColumn(0).setMaxWidth(0);
        cartTable.getColumnModel().getColumn(0).setWidth(0);

        cartScrollPane = new JScrollPane(cartTable);
        updateQuantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        updateCartItemButton = new JButton("Update Qty");
        removeCartItemButton = new JButton("Remove Item");
        totalLabel = new JLabel("Cart Total: TND 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        deliveryComboBox = new JComboBox<>(new String[]{"a domicile", "express", "pickup"});
        placeOrderButton = new JButton("Place Order");

        
        updateCartItemButton.setEnabled(false);
        removeCartItemButton.setEnabled(false);
    }

    private void layoutComponents() {
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.6); 

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));

        JPanel productSearchAddPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productSearchAddPanel.add(new JLabel("Search:"));
        productSearchAddPanel.add(searchField);
        productSearchAddPanel.add(searchButton);
        productSearchAddPanel.add(new JSeparator(SwingConstants.VERTICAL));
        productSearchAddPanel.add(new JLabel("Qty:"));
        productSearchAddPanel.add(quantitySpinner);
        productSearchAddPanel.add(addToCartButton);

        leftPanel.add(productSearchAddPanel, BorderLayout.NORTH);
        leftPanel.add(productScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Your Shopping Cart"));

        JPanel cartControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cartControlPanel.add(new JLabel("Selected Qty:"));
        cartControlPanel.add(updateQuantitySpinner);
        cartControlPanel.add(updateCartItemButton);
        cartControlPanel.add(removeCartItemButton);

        JPanel orderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        orderPanel.add(totalLabel);
        orderPanel.add(new JLabel("Delivery:"));
        orderPanel.add(deliveryComboBox);
        orderPanel.add(placeOrderButton);

        JPanel cartBottomPanel = new JPanel(new BorderLayout());
        cartBottomPanel.add(cartControlPanel, BorderLayout.NORTH);
        cartBottomPanel.add(orderPanel, BorderLayout.SOUTH);


        rightPanel.add(cartScrollPane, BorderLayout.CENTER);
        rightPanel.add(cartBottomPanel, BorderLayout.SOUTH);

        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);

        
        setLayout(new BorderLayout(10, 10));
        add(welcomeLabel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER); 

        
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    private void attachListeners() {
        searchButton.addActionListener(e -> searchProducts());
        searchField.addActionListener(e -> searchProducts()); 

        
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                addToCartButton.setEnabled(productTable.getSelectedRow() != -1 && currentCartId > 0);
            }
        });

       
        addToCartButton.addActionListener(e -> addItemToCart());

       
        cartTable.getSelectionModel().addListSelectionListener(e -> {
             if (!e.getValueIsAdjusting()) {
                boolean selected = cartTable.getSelectedRow() != -1;
                updateCartItemButton.setEnabled(selected && currentCartId > 0);
                removeCartItemButton.setEnabled(selected && currentCartId > 0);
                if (selected) {
                    int selectedRow = cartTable.getSelectedRow();
                    int currentQty = (int) cartTableModel.getValueAt(selectedRow, 2);
                    updateQuantitySpinner.setValue(currentQty);
                }
            }
        });

        updateCartItemButton.addActionListener(e -> updateCartItem());

        removeCartItemButton.addActionListener(e -> removeCartItem());

        placeOrderButton.addActionListener(e -> placeOrder());
    }


    private void loadProductData() {
        try {
            List<Produit> products = produitDAO.listerTousLesProduits();
            displayProducts(products);
        } catch (RuntimeException e) {
            showError("Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

     private void displayProducts(List<Produit> products) {
         productTableModel.setRowCount(0);
            for (Produit prod : products) {
                productTableModel.addRow(new Object[]{
                        prod.getId(),
                        prod.getNom(),
                        prod.getDescription(),
                        prod.getPrix() 
                });
            }
            productTable.clearSelection();
            addToCartButton.setEnabled(false);
    }

    private void loadCartItems() {
        if (currentCartId <= 0) return;

        currentCartDetails.clear(); 
        cartTableModel.setRowCount(0); 
        int totalCartPrice = 0;

        try {
            List<Cart_items> items = cartItemsDAO.getItemsByCartId(currentCartId);
            for (Cart_items item : items) {
                try {
                    
                    Produit product = produitDAO.getProductById(item.getProduct_id());
                    if (product != null) {
                        CartItemDetails detail = new CartItemDetails(item, product);
                        currentCartDetails.add(detail); 
                        cartTableModel.addRow(new Object[]{
                                detail.cartItemId, 
                                detail.productName,
                                detail.quantity,
                                detail.pricePerItem,
                                detail.lineTotal
                        });
                        totalCartPrice += detail.lineTotal;
                    } else {
                         System.err.println("Warning: Product with ID " + item.getProduct_id() + " not found for cart item " + item.getId());
       
                    }
                } catch (RuntimeException prodEx) {
                     System.err.println("Error fetching product details for cart item: " + prodEx.getMessage());
                     
                }
            }
        } catch (RuntimeException e) {
            showError("Error loading cart items: " + e.getMessage());
            e.printStackTrace();
        }

      
        totalLabel.setText(String.format("Cart Total: TND%d.00", totalCartPrice)); 

    
        cartTable.clearSelection();
        updateCartItemButton.setEnabled(false);
        removeCartItemButton.setEnabled(false);
    }


  

    private void searchProducts() {
        String searchTerm = searchField.getText().trim();
        try {
            List<Produit> products;
            if (searchTerm.isEmpty()) {
                products = produitDAO.listerTousLesProduits();
            } else {
                List<Produit> all = produitDAO.listerTousLesProduits();
                products = all.stream()
                              .filter(p -> p.getNom().toLowerCase().contains(searchTerm.toLowerCase()))
                              .collect(Collectors.toList());
            }
             displayProducts(products);
        } catch (RuntimeException e) {
            showError("Error searching products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addItemToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1 || currentCartId <= 0) {
            showError("Please select a product to add to the cart.");
            return;
        }

        try {
            int productId = (int) productTableModel.getValueAt(selectedRow, 0);
            int quantity = (int) quantitySpinner.getValue();

            if (quantity <= 0) {
                showError("Please enter a positive quantity.");
                return;
            }

            cartItemsDAO.addItemOrUpdateQuantity(currentCartId, productId, quantity);
            showSuccess(quantity + " x " + productTableModel.getValueAt(selectedRow, 1) + " added/updated in cart.");

            loadCartItems();
            quantitySpinner.setValue(1); 
            productTable.clearSelection(); 

        } catch (NumberFormatException nfe) {
             showError("Invalid quantity specified.");
        } catch (RuntimeException ex) {
            showError("Error adding item to cart: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateCartItem() {
         int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1 || currentCartId <= 0) {
            showError("Please select an item from the cart to update.");
            return;
        }

        try {
            int cartItemId = (int) cartTableModel.getValueAt(selectedRow, 0);
            int newQuantity = (int) updateQuantitySpinner.getValue();

             if (newQuantity <= 0) {
                 int confirm = JOptionPane.showConfirmDialog(this,
                        "Setting quantity to 0 or less will remove the item. Continue?",
                        "Confirm Remove", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return; 
                }
            }

            cartItemsDAO.updateItemQuantity(cartItemId, newQuantity);
            showSuccess("Cart item quantity updated.");
            loadCartItems(); 

        } catch (NumberFormatException nfe) {
             showError("Invalid quantity specified for update.");
        } catch (RuntimeException ex) {
            showError("Error updating cart item: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void removeCartItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1 || currentCartId <= 0) {
            showError("Please select an item from the cart to remove.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove '" + cartTableModel.getValueAt(selectedRow, 1) + "' from the cart?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int cartItemId = (int) cartTableModel.getValueAt(selectedRow, 0);
                cartItemsDAO.removeItemFromCart(cartItemId);
                showSuccess("Item removed from cart.");
                loadCartItems(); 
            } catch (RuntimeException ex) {
                showError("Error removing item from cart: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void placeOrder() {
        if (currentCartId <= 0) {
             showError("Cannot place order: Cart is not initialized.");
             return;
        }
        if (currentCartDetails.isEmpty()) {
             showError("Cannot place order: Your cart is empty.");
             return;
        }

        try {
            int totalOrderPrice = 0;
            int totalProductCount = 0;
            for (CartItemDetails detail : currentCartDetails) {
                totalOrderPrice += detail.lineTotal;
                totalProductCount += detail.quantity; 
            }

            String deliveryMethod = (String) deliveryComboBox.getSelectedItem();
            int userId = loggedInUser.getId();

            int orderId = orderDAO.createOrder(userId, currentCartId, totalOrderPrice, totalProductCount, deliveryMethod);

            if (orderId > 0) {
                showSuccess("Order placed successfully! Order ID: " + orderId);

                cartItemsDAO.clearCart(currentCartId);

                loadCartItems();


            } else {
                showError("Failed to place order. Please try again.");
            }

        } catch (RuntimeException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("cart is empty")) {
                 showError("Cannot place order: Your cart is empty.");
            } else {
                showError("Error placing order: " + ex.getMessage());
            }
            ex.printStackTrace();
        }
    }


    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}