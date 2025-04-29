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

    private int currentCartId = -1; // Store the user's cart ID

    // UI Components - Products
    private JLabel welcomeLabel;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JScrollPane productScrollPane;
    private JTextField searchField;
    private JButton searchButton;
    private JSpinner quantitySpinner; // For adding products
    private JButton addToCartButton;

    // UI Components - Cart
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JScrollPane cartScrollPane;
    private JSpinner updateQuantitySpinner; // For updating cart items
    private JButton updateCartItemButton;
    private JButton removeCartItemButton;
    private JLabel totalLabel;
    private JComboBox<String> deliveryComboBox;
    private JButton placeOrderButton;

    // Helper list to store detailed cart item info (including price/name)
    private List<CartItemDetails> currentCartDetails = new ArrayList<>();

    // Helper class to hold combined cart item and product info
    private static class CartItemDetails {
        int cartItemId; // ID from cart_items table
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

        // Initialize DAOs
        this.produitDAO = new ProduitDAO(this.connection);
        this.cartDAO = new CartDAO(this.connection);
        this.cartItemsDAO = new Cart_itemsDAO(this.connection);
        this.orderDAO = new OrderDAO(this.connection); // Initialize OrderDAO

        // Get or Create Cart for the logged-in user
        try {
            this.currentCartId = cartDAO.getOrCreateCartByUserId(loggedInUser.getId());
            if (this.currentCartId <= 0) {
                 throw new RuntimeException("Could not get or create a cart for the user.");
            }
        } catch (RuntimeException e) {
             showError("Error initializing cart: " + e.getMessage());
             // Decide how to handle this - maybe disable cart functionality?
             e.printStackTrace();
             // For now, let's disable cart buttons if cartId is invalid
        }


        setTitle("Cosmetics Website - Welcome " + loggedInUser.getUsername() + " (Cart ID: " + currentCartId + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750); // Increased size
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();

        loadProductData();
        loadCartItems(); // Load initial cart items

        // Disable cart buttons initially if cartId is invalid
        boolean cartReady = this.currentCartId > 0;
        addToCartButton.setEnabled(cartReady);
        updateCartItemButton.setEnabled(cartReady);
        removeCartItemButton.setEnabled(cartReady);
        placeOrderButton.setEnabled(cartReady);
    }

    private void initComponents() {
        welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // --- Product Components ---
        productTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Price"}, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        productTable = new JTable(productTableModel);
        productScrollPane = new JScrollPane(productTable);
        searchField = new JTextField(20);
        searchButton = new JButton("Search Products");
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Min 1, Max 100
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.setEnabled(false); // Disabled until a product is selected

        // --- Cart Components ---
        cartTableModel = new DefaultTableModel(new Object[]{"Item ID", "Product", "Qty", "Price/Item", "Line Total"}, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        cartTable = new JTable(cartTableModel);
        // Hide the Item ID column visually, but keep it in the model for reference
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

        // Disable cart modification buttons initially
        updateCartItemButton.setEnabled(false);
        removeCartItemButton.setEnabled(false);
    }

    private void layoutComponents() {
        // Main layout: Split Pane for Products | Cart
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.6); // Give products slightly more space initially

        // --- Left Panel (Products) ---
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

        // --- Right Panel (Cart) ---
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

        // Add panels to split pane
        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);

        // Add main components to frame
        setLayout(new BorderLayout(10, 10));
        add(welcomeLabel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER); // Add the split pane

        // Padding for the main frame content
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    private void attachListeners() {
        // Search Button
        searchButton.addActionListener(e -> searchProducts());
        searchField.addActionListener(e -> searchProducts()); // Also search on Enter

        // Product Table Selection
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                addToCartButton.setEnabled(productTable.getSelectedRow() != -1 && currentCartId > 0);
            }
        });

        // Add to Cart Button
        addToCartButton.addActionListener(e -> addItemToCart());

        // Cart Table Selection
        cartTable.getSelectionModel().addListSelectionListener(e -> {
             if (!e.getValueIsAdjusting()) {
                boolean selected = cartTable.getSelectedRow() != -1;
                updateCartItemButton.setEnabled(selected && currentCartId > 0);
                removeCartItemButton.setEnabled(selected && currentCartId > 0);
                if (selected) {
                    // Pre-fill update spinner with current quantity
                    int selectedRow = cartTable.getSelectedRow();
                    int currentQty = (int) cartTableModel.getValueAt(selectedRow, 2);
                    updateQuantitySpinner.setValue(currentQty);
                }
            }
        });

        // Update Cart Item Button
        updateCartItemButton.addActionListener(e -> updateCartItem());

        // Remove Cart Item Button
        removeCartItemButton.addActionListener(e -> removeCartItem());

        // Place Order Button
        placeOrderButton.addActionListener(e -> placeOrder());
    }

    // --- Data Loading and Display ---

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
                        prod.getPrix() // Display price directly
                });
            }
            // Clear selection and disable add button
            productTable.clearSelection();
            addToCartButton.setEnabled(false);
    }

    private void loadCartItems() {
        if (currentCartId <= 0) return; // Don't load if cart isn't ready

        currentCartDetails.clear(); // Clear the helper list
        cartTableModel.setRowCount(0); // Clear visual table
        int totalCartPrice = 0;

        try {
            List<Cart_items> items = cartItemsDAO.getItemsByCartId(currentCartId);
            for (Cart_items item : items) {
                try {
                    // Fetch product details for each cart item
                    Produit product = produitDAO.getProductById(item.getProduct_id());
                    if (product != null) {
                        CartItemDetails detail = new CartItemDetails(item, product);
                        currentCartDetails.add(detail); // Add to helper list
                        cartTableModel.addRow(new Object[]{
                                detail.cartItemId, // Keep ID in model
                                detail.productName,
                                detail.quantity,
                                detail.pricePerItem,
                                detail.lineTotal
                        });
                        totalCartPrice += detail.lineTotal;
                    } else {
                         System.err.println("Warning: Product with ID " + item.getProduct_id() + " not found for cart item " + item.getId());
                         // Optionally remove this orphaned cart item here
                         // cartItemsDAO.removeItemFromCart(item.getId());
                    }
                } catch (RuntimeException prodEx) {
                     System.err.println("Error fetching product details for cart item: " + prodEx.getMessage());
                     // Decide how to handle - skip item, show error?
                }
            }
        } catch (RuntimeException e) {
            showError("Error loading cart items: " + e.getMessage());
            e.printStackTrace();
        }

        // Update total label
        totalLabel.setText(String.format("Cart Total: TND%d.00", totalCartPrice)); // Simple integer format

        // Clear selections and disable buttons
        cartTable.clearSelection();
        updateCartItemButton.setEnabled(false);
        removeCartItemButton.setEnabled(false);
    }


    // --- Action Methods ---

    private void searchProducts() {
        String searchTerm = searchField.getText().trim();
        try {
            List<Produit> products;
            if (searchTerm.isEmpty()) {
                products = produitDAO.listerTousLesProduits();
            } else {
                // // Fallback (less efficient):
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

            loadCartItems(); // Refresh cart display
            quantitySpinner.setValue(1); // Reset spinner
            productTable.clearSelection(); // Clear product selection

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
            // Get the cart_items ID from the hidden first column of the cart table model
            int cartItemId = (int) cartTableModel.getValueAt(selectedRow, 0);
            int newQuantity = (int) updateQuantitySpinner.getValue();

             if (newQuantity <= 0) {
                // Ask confirmation before removing due to zero quantity
                 int confirm = JOptionPane.showConfirmDialog(this,
                        "Setting quantity to 0 or less will remove the item. Continue?",
                        "Confirm Remove", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return; // User cancelled
                }
                // Let the DAO handle removal if quantity is <= 0
            }

            cartItemsDAO.updateItemQuantity(cartItemId, newQuantity);
            showSuccess("Cart item quantity updated.");
            loadCartItems(); // Refresh cart display

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
                // Get the cart_items ID from the hidden first column
                int cartItemId = (int) cartTableModel.getValueAt(selectedRow, 0);
                cartItemsDAO.removeItemFromCart(cartItemId);
                showSuccess("Item removed from cart.");
                loadCartItems(); // Refresh cart display
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
            // Recalculate totals just before ordering for accuracy
            int totalOrderPrice = 0;
            int totalProductCount = 0;
            for (CartItemDetails detail : currentCartDetails) {
                totalOrderPrice += detail.lineTotal;
                totalProductCount += detail.quantity; // Sum of quantities of all items
            }

            String deliveryMethod = (String) deliveryComboBox.getSelectedItem();
            int userId = loggedInUser.getId();

            // Call the OrderDAO createOrder method
            int orderId = orderDAO.createOrder(userId, currentCartId, totalOrderPrice, totalProductCount, deliveryMethod);

            if (orderId > 0) {
                showSuccess("Order placed successfully! Order ID: " + orderId);

                // Clear the cart in the database after successful order
                cartItemsDAO.clearCart(currentCartId);

                // Refresh the cart display (should be empty now)
                loadCartItems();

                // Optionally, create a new cart for the user immediately
                // this.currentCartId = cartDAO.getOrCreateCartByUserId(loggedInUser.getId());
                // setTitle("Cosmetics Website - Welcome " + loggedInUser.getUsername() + " (Cart ID: " + currentCartId + ")");

            } else {
                showError("Failed to place order. Please try again.");
            }

        } catch (RuntimeException ex) {
             // Check if the error is due to an empty cart (OrderDAO might throw this)
            if (ex.getMessage() != null && ex.getMessage().contains("cart is empty")) {
                 showError("Cannot place order: Your cart is empty.");
            } else {
                showError("Error placing order: " + ex.getMessage());
            }
            ex.printStackTrace();
        }
    }


    // --- Utility Methods ---
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}