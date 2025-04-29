package Projetjava;

import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class AdminDashboard extends JFrame {

    // --- Database Connection & DAOs ---
    private Connection connection;
    private UtilisateurDAO utilisateurDAO;
    private ProduitDAO produitDAO;
    // Add other DAOs if needed for future features (e.g., CategorieDAO, MarqueDAO)

    // --- UI Components ---
    private JTabbedPane tabbedPane;

    // --- User Management Components ---
    private JPanel userPanel;
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JScrollPane userScrollPane;
    private JTextField userIdField; // Usually hidden or read-only
    private JTextField userUsernameField;
    private JTextField userEmailField;
    private JPasswordField userPasswordField;
    private JTextField userAddressField;
    private JTextField userRoleField; // Could be a JComboBox if roles are fixed
    private JButton userAddButton;
    private JButton userUpdateButton;
    private JButton userDeleteButton;
    private JButton userClearButton;

    // --- Product Management Components ---
    private JPanel productPanel;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JScrollPane productScrollPane;
    private JTextField productIdField; // Usually hidden or read-only
    private JTextField productNomField;
    private JTextField productDescriptionField;
    private JTextField productPrixField;
    private JTextField productCategorieIdField; // Could be JComboBox if CategorieDAO is implemented
    private JTextField productMarqueIdField;    // Could be JComboBox if MarqueDAO is implemented
    private JTextField productImagePathField;
    private JTextField productImageHoverPathField;
    private JTextField productOrderedField;
    private JTextField productShadeField;
    private JButton productAddButton;
    private JButton productUpdateButton;
    private JButton productDeleteButton;
    private JButton productClearButton;

    // --- Status Bar ---
    private JLabel statusBar;

    public AdminDashboard() {
        super("Cosmetics Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Center the window

        // --- Initialize Database Connection and DAOs ---
        try {
            connection = CosmeticsWebsite.getConnection();
            utilisateurDAO = new UtilisateurDAO(connection);
            produitDAO = new ProduitDAO(connection);
            // Initialize other DAOs here
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection error: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if DB connection fails
        }

        // --- Create UI ---
        initComponents();
        layoutComponents();
        attachListeners();

        // --- Load Initial Data ---
        loadUserData();
        loadProductData();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        statusBar = new JLabel("Ready");

        // --- User Management Components ---
        userPanel = new JPanel();
        userTableModel = new DefaultTableModel(new Object[]{"ID", "Username", "Email", "Address", "Role", "Created At"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        userTable = new JTable(userTableModel);
        userScrollPane = new JScrollPane(userTable);
        userIdField = new JTextField(5);
        userIdField.setEditable(false); // ID is usually not manually set/edited
        userUsernameField = new JTextField(15);
        userEmailField = new JTextField(20);
        userPasswordField = new JPasswordField(15);
        userAddressField = new JTextField(20);
        userRoleField = new JTextField(10);
        userAddButton = new JButton("Add User");
        userUpdateButton = new JButton("Update User");
        userDeleteButton = new JButton("Delete User");
        userClearButton = new JButton("Clear Fields");

        // --- Product Management Components ---
        productPanel = new JPanel();
        productTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Price", "Category ID", "Brand ID", "Ordered", "Created At"}, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        }; // Simplified view for brevity
        productTable = new JTable(productTableModel);
        productScrollPane = new JScrollPane(productTable);
        productIdField = new JTextField(5);
        productIdField.setEditable(false);
        productNomField = new JTextField(20);
        productDescriptionField = new JTextField(30); // Consider JTextArea for longer text
        productPrixField = new JTextField(8);
        productCategorieIdField = new JTextField(5);
        productMarqueIdField = new JTextField(5);
        productImagePathField = new JTextField(25);
        productImageHoverPathField = new JTextField(25);
        productOrderedField = new JTextField(5);
        productShadeField = new JTextField(10);
        productAddButton = new JButton("Add Product");
        productUpdateButton = new JButton("Update Product");
        productDeleteButton = new JButton("Delete Product");
        productClearButton = new JButton("Clear Fields");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // --- User Panel Layout ---
        userPanel.setLayout(new BorderLayout(5, 5));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userPanel.add(userScrollPane, BorderLayout.CENTER);

        JPanel userFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcUser = new GridBagConstraints();
        gbcUser.insets = new Insets(2, 5, 2, 5); // Padding
        gbcUser.anchor = GridBagConstraints.WEST;

        // Row 0
        gbcUser.gridx = 0; gbcUser.gridy = 0; userFormPanel.add(new JLabel("ID:"), gbcUser);
        gbcUser.gridx = 1; gbcUser.gridy = 0; userFormPanel.add(userIdField, gbcUser);
        gbcUser.gridx = 2; gbcUser.gridy = 0; userFormPanel.add(new JLabel("Username:"), gbcUser);
        gbcUser.gridx = 3; gbcUser.gridy = 0; userFormPanel.add(userUsernameField, gbcUser);

        // Row 1
        gbcUser.gridx = 0; gbcUser.gridy = 1; userFormPanel.add(new JLabel("Email:"), gbcUser);
        gbcUser.gridx = 1; gbcUser.gridy = 1; userFormPanel.add(userEmailField, gbcUser);
        gbcUser.gridx = 2; gbcUser.gridy = 1; userFormPanel.add(new JLabel("Password:"), gbcUser);
        gbcUser.gridx = 3; gbcUser.gridy = 1; userFormPanel.add(userPasswordField, gbcUser);

        // Row 2
        gbcUser.gridx = 0; gbcUser.gridy = 2; userFormPanel.add(new JLabel("Address:"), gbcUser);
        gbcUser.gridx = 1; gbcUser.gridy = 2; userFormPanel.add(userAddressField, gbcUser);
        gbcUser.gridx = 2; gbcUser.gridy = 2; userFormPanel.add(new JLabel("Role:"), gbcUser);
        gbcUser.gridx = 3; gbcUser.gridy = 2; userFormPanel.add(userRoleField, gbcUser);


        JPanel userButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userButtonPanel.add(userAddButton);
        userButtonPanel.add(userUpdateButton);
        userButtonPanel.add(userDeleteButton);
        userButtonPanel.add(userClearButton);

        JPanel userSouthPanel = new JPanel(new BorderLayout());
        userSouthPanel.add(userFormPanel, BorderLayout.CENTER);
        userSouthPanel.add(userButtonPanel, BorderLayout.SOUTH);

        userPanel.add(userSouthPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("User Management", userPanel);


        // --- Product Panel Layout ---
        productPanel.setLayout(new BorderLayout(5, 5));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        productPanel.add(productScrollPane, BorderLayout.CENTER);

        JPanel productFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcProd = new GridBagConstraints();
        gbcProd.insets = new Insets(2, 5, 2, 5);
        gbcProd.anchor = GridBagConstraints.WEST;

        // Row 0
        gbcProd.gridx = 0; gbcProd.gridy = 0; productFormPanel.add(new JLabel("ID:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.gridy = 0; productFormPanel.add(productIdField, gbcProd);
        gbcProd.gridx = 2; gbcProd.gridy = 0; productFormPanel.add(new JLabel("Name:"), gbcProd);
        gbcProd.gridx = 3; gbcProd.gridy = 0; productFormPanel.add(productNomField, gbcProd);

        // Row 1
        gbcProd.gridx = 0; gbcProd.gridy = 1; productFormPanel.add(new JLabel("Price:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.gridy = 1; productFormPanel.add(productPrixField, gbcProd);
        gbcProd.gridx = 2; gbcProd.gridy = 1; productFormPanel.add(new JLabel("Category ID:"), gbcProd);
        gbcProd.gridx = 3; gbcProd.gridy = 1; productFormPanel.add(productCategorieIdField, gbcProd);

        // Row 2
        gbcProd.gridx = 0; gbcProd.gridy = 2; productFormPanel.add(new JLabel("Brand ID:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.gridy = 2; productFormPanel.add(productMarqueIdField, gbcProd);
        gbcProd.gridx = 2; gbcProd.gridy = 2; productFormPanel.add(new JLabel("Ordered Count:"), gbcProd);
        gbcProd.gridx = 3; gbcProd.gridy = 2; productFormPanel.add(productOrderedField, gbcProd);

        // Row 3
        gbcProd.gridx = 0; gbcProd.gridy = 3; productFormPanel.add(new JLabel("Image Path:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.gridy = 3; gbcProd.gridwidth = 3; productFormPanel.add(productImagePathField, gbcProd);
        gbcProd.gridwidth = 1; // Reset gridwidth

        // Row 4
        gbcProd.gridx = 0; gbcProd.gridy = 4; productFormPanel.add(new JLabel("Hover Image:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.gridy = 4; gbcProd.gridwidth = 3; productFormPanel.add(productImageHoverPathField, gbcProd);
        gbcProd.gridwidth = 1;

        // Row 5
        gbcProd.gridx = 0; gbcProd.gridy = 5; productFormPanel.add(new JLabel("Shade:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.gridy = 5; productFormPanel.add(productShadeField, gbcProd);
        gbcProd.gridx = 2; gbcProd.gridy = 5; productFormPanel.add(new JLabel("Description:"), gbcProd);
        gbcProd.gridx = 3; gbcProd.gridy = 5; productFormPanel.add(productDescriptionField, gbcProd);


        JPanel productButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        productButtonPanel.add(productAddButton);
        productButtonPanel.add(productUpdateButton);
        productButtonPanel.add(productDeleteButton);
        productButtonPanel.add(productClearButton);

        JPanel productSouthPanel = new JPanel(new BorderLayout());
        productSouthPanel.add(productFormPanel, BorderLayout.CENTER);
        productSouthPanel.add(productButtonPanel, BorderLayout.SOUTH);

        productPanel.add(productSouthPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Product Management", productPanel);

        // --- Add Tabbed Pane and Status Bar ---
        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    // --- Data Loading Methods ---

    private void loadUserData() {
        try {
            List<Utilisateur> users = utilisateurDAO.getTousLesUtilisateurs();
            userTableModel.setRowCount(0); // Clear existing data
            for (Utilisateur user : users) {
                userTableModel.addRow(new Object[]{
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAdress(),
                        user.getRole(),
                        user.getCreated_at() != null ? user.getCreated_at().toString() : ""
                });
            }
            statusBar.setText("Loaded " + users.size() + " users.");
        } catch (RuntimeException e) {
            showError("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadProductData() {
        try {
            List<Produit> products = produitDAO.listerTousLesProduits();
            productTableModel.setRowCount(0); // Clear existing data
            for (Produit prod : products) {
                productTableModel.addRow(new Object[]{
                        prod.getId(),
                        prod.getNom(),
                        prod.getPrix(),
                        prod.getCategorie_id(),
                        prod.getMarque_id(),
                        prod.getOrdered(),
                        prod.getCreated_at() != null ? prod.getCreated_at().toString() : ""
                        // Add more columns if needed (description, image paths, shade)
                });
            }
            statusBar.setText("Loaded " + products.size() + " products.");
        } catch (RuntimeException e) {
            showError("Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Event Listeners ---

    private void attachListeners() {
        // --- User Listeners ---
        userAddButton.addActionListener(e -> addUser());
        userUpdateButton.addActionListener(e -> updateUser());
        userDeleteButton.addActionListener(e -> deleteUser());
        userClearButton.addActionListener(e -> clearUserFields());

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateUserFieldsFromTable(selectedRow);
                }
            }
        });

        // --- Product Listeners ---
        productAddButton.addActionListener(e -> addProduct());
        productUpdateButton.addActionListener(e -> updateProduct());
        productDeleteButton.addActionListener(e -> deleteProduct());
        productClearButton.addActionListener(e -> clearProductFields());

         productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateProductFieldsFromTable(selectedRow);
                }
            }
        });
    }

    // --- User Action Methods ---

    private void populateUserFieldsFromTable(int row) {
        TableModel model = userTable.getModel();
        userIdField.setText(model.getValueAt(row, 0).toString());
        userUsernameField.setText(model.getValueAt(row, 1).toString());
        userEmailField.setText(model.getValueAt(row, 2).toString());
        userAddressField.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
        userRoleField.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
        userPasswordField.setText(""); // Don't show password
    }

    private void clearUserFields() {
        userIdField.setText("");
        userUsernameField.setText("");
        userEmailField.setText("");
        userPasswordField.setText("");
        userAddressField.setText("");
        userRoleField.setText("client"); // Default role
        userTable.clearSelection();
        statusBar.setText("User fields cleared.");
    }

    private void addUser() {
        // 1. Get data from fields
        String username = userUsernameField.getText().trim();
        String email = userEmailField.getText().trim();
        String password = new String(userPasswordField.getPassword());
        String address = userAddressField.getText().trim();
        String role = userRoleField.getText().trim();

        // 2. Validate data (Basic)
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Username, Email, and Password cannot be empty.");
            return;
        }
        if (!Utilisateur.validerEmail(email)) {
             showError("Invalid email format.");
             return;
        }
        if (role.isEmpty()) {
            role = "client"; // Default if empty
        }
        
        String jsonAddress = "\"" + address.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        // 3. Create Utilisateur object (ID is auto-generated, created_at is set by DB/DAO)
        // Note: The Utilisateur constructor expects an ID and created_at, which we don't have yet.
        // We might need a different constructor or set fields individually before passing to DAO.
        // Let's assume ajouterUtilisateur handles created_at and doesn't need ID.
        // We create a temporary user object for the DAO.
        Utilisateur newUser = new Utilisateur(0, username, email, password, role, jsonAddress, null); // ID 0 is placeholder

        // 4. Call DAO
        try {
            utilisateurDAO.ajouterUtilisateur(newUser);
            showSuccess("User '" + username + "' added successfully.");
            loadUserData(); // Refresh table
            clearUserFields();
        } catch (RuntimeException ex) {
            showError("Error adding user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateUser() {
        // 1. Get ID from selected row or ID field
        String idStr = userIdField.getText();
        if (idStr.isEmpty()) {
            showError("Please select a user from the table to update.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            showError("Invalid User ID.");
            return;
        }

        // 2. Get data from fields
        String username = userUsernameField.getText().trim();
        String email = userEmailField.getText().trim();
        String password = new String(userPasswordField.getPassword()); // Get potentially new password
        String address = userAddressField.getText().trim();
        String role = userRoleField.getText().trim();

        // 3. Validate data
        if (username.isEmpty() || email.isEmpty()) {
            showError("Username and Email cannot be empty.");
            return;
        }
         if (!Utilisateur.validerEmail(email)) {
             showError("Invalid email format.");
             return;
        }
        if (role.isEmpty()) {
            role = "client";
        }
        String jsonAddress = "\"" + address.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";

        // 4. Fetch existing user to get created_at and potentially handle password update logic
        Utilisateur existingUser = utilisateurDAO.getUtilisateurById(id);
        if (existingUser == null) {
            showError("User with ID " + id + " not found.");
            return;
        }

        // 5. Create updated Utilisateur object
        // If password field is empty, keep the old password. Otherwise, use the new one.
        String finalPassword = password.isEmpty() ? existingUser.getPassword() : password;
        LocalDate createdAt = existingUser.getCreated_at(); // Keep original creation date

        Utilisateur updatedUser = new Utilisateur(id, username, email, finalPassword, role, jsonAddress, createdAt);


        // 6. Call DAO (mettreAJourUtilisateur needs correction in DAO - table name 'user' vs 'users')
        try {
            // *** IMPORTANT: Fix the table name in UtilisateurDAO.mettreAJourUtilisateur ***
            // It currently uses "UPDATE user SET ...", likely should be "UPDATE users SET ..."
            utilisateurDAO.mettreAJourUtilisateur(updatedUser);
            showSuccess("User ID " + id + " updated successfully.");
            loadUserData();
            clearUserFields();
        } catch (RuntimeException ex) {
            showError("Error updating user: " + ex.getMessage());
             // Check console for SQL errors related to table name 'user'
            ex.printStackTrace();
        }
    }

    private void deleteUser() {
        String idStr = userIdField.getText();
        if (idStr.isEmpty()) {
            showError("Please select a user from the table to delete.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            showError("Invalid User ID.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user ID " + id + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                 // *** IMPORTANT: Fix the table name in UtilisateurDAO.supprimerUtilisateur ***
                 // It currently uses "DELETE FROM user WHERE ...", likely should be "DELETE FROM users WHERE ..."
                utilisateurDAO.supprimerUtilisateur(id);
                showSuccess("User ID " + id + " deleted successfully.");
                loadUserData();
                clearUserFields();
            } catch (RuntimeException ex) {
                showError("Error deleting user: " + ex.getMessage());
                 // Check console for SQL errors related to table name 'user'
                ex.printStackTrace();
            }
        }
    }


    // --- Product Action Methods ---

     private void populateProductFieldsFromTable(int row) {
        TableModel model = productTable.getModel();
        int productId = (int) model.getValueAt(row, 0);

        // Fetch the full product details from DAO using the ID
        Produit selectedProduct = produitDAO.getProductById(productId);

        if (selectedProduct != null) {
            productIdField.setText(String.valueOf(selectedProduct.getId()));
            productNomField.setText(selectedProduct.getNom());
            productDescriptionField.setText(selectedProduct.getDescription());
            productPrixField.setText(String.valueOf(selectedProduct.getPrix()));
            productCategorieIdField.setText(String.valueOf(selectedProduct.getCategorie_id()));
            productMarqueIdField.setText(String.valueOf(selectedProduct.getMarque_id()));
            productImagePathField.setText(selectedProduct.getImage_path() != null ? selectedProduct.getImage_path() : "");
            productImageHoverPathField.setText(selectedProduct.getImagehover_path() != null ? selectedProduct.getImagehover_path() : "");
            productOrderedField.setText(String.valueOf(selectedProduct.getOrdered()));
            productShadeField.setText(selectedProduct.getShade() != null ? selectedProduct.getShade() : "");
            statusBar.setText("Selected Product ID: " + selectedProduct.getId());
        } else {
            showError("Could not retrieve details for selected product ID: " + productId);
            clearProductFields();
        }
    }

    private void clearProductFields() {
        productIdField.setText("");
        productNomField.setText("");
        productDescriptionField.setText("");
        productPrixField.setText("");
        productCategorieIdField.setText("");
        productMarqueIdField.setText("");
        productImagePathField.setText("");
        productImageHoverPathField.setText("");
        productOrderedField.setText("0"); // Default ordered
        productShadeField.setText("");
        productTable.clearSelection();
        statusBar.setText("Product fields cleared.");
    }

    private void addProduct() {
        // 1. Get data
        String nom = productNomField.getText().trim();
        String description = productDescriptionField.getText().trim();
        String prixStr = productPrixField.getText().trim();
        String catIdStr = productCategorieIdField.getText().trim();
        String marqIdStr = productMarqueIdField.getText().trim();
        String imgPath = productImagePathField.getText().trim();
        String imgHoverPath = productImageHoverPathField.getText().trim();
        String orderedStr = productOrderedField.getText().trim();
        String shade = productShadeField.getText().trim();

        // 2. Validate & Convert
        if (nom.isEmpty() || prixStr.isEmpty() || catIdStr.isEmpty() || marqIdStr.isEmpty()) {
            showError("Name, Price, Category ID, and Brand ID are required.");
            return;
        }

        int prix, catId, marqId, ordered;
        try {
            prix = Integer.parseInt(prixStr);
            catId = Integer.parseInt(catIdStr);
            marqId = Integer.parseInt(marqIdStr);
            ordered = orderedStr.isEmpty() ? 0 : Integer.parseInt(orderedStr); // Default 0 if empty
             if (prix < 0 || ordered < 0) throw new NumberFormatException("Cannot be negative");
        } catch (NumberFormatException e) {
            showError("Invalid number format for Price, Category ID, Brand ID, or Ordered count. Ensure they are valid integers (>= 0).");
            return;
        }

        // 3. Create Product object
        Produit newProduct = new Produit(0, nom, description, prix, catId, marqId,
                                         null, // created_at handled by DAO/DB
                                         imgPath.isEmpty() ? null : imgPath,
                                         imgHoverPath.isEmpty() ? null : imgHoverPath,
                                         ordered,
                                         shade.isEmpty() ? null : shade);

        // 4. Call DAO
        try {
            produitDAO.ajouterProduct(newProduct);
            showSuccess("Product '" + nom + "' added successfully.");
            loadProductData();
            clearProductFields();
        } catch (RuntimeException ex) {
            showError("Error adding product: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateProduct() {
        // 1. Get ID
        String idStr = productIdField.getText();
        if (idStr.isEmpty()) {
            showError("Please select a product from the table to update.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            showError("Invalid Product ID.");
            return;
        }

         // 2. Get data
        String nom = productNomField.getText().trim();
        String description = productDescriptionField.getText().trim();
        String prixStr = productPrixField.getText().trim();
        String catIdStr = productCategorieIdField.getText().trim();
        String marqIdStr = productMarqueIdField.getText().trim();
        String imgPath = productImagePathField.getText().trim();
        String imgHoverPath = productImageHoverPathField.getText().trim();
        String orderedStr = productOrderedField.getText().trim();
        String shade = productShadeField.getText().trim();

        // 3. Validate & Convert
        if (nom.isEmpty() || prixStr.isEmpty() || catIdStr.isEmpty() || marqIdStr.isEmpty()) {
            showError("Name, Price, Category ID, and Brand ID are required.");
            return;
        }
        int prix, catId, marqId, ordered;
         try {
            prix = Integer.parseInt(prixStr);
            catId = Integer.parseInt(catIdStr);
            marqId = Integer.parseInt(marqIdStr);
            ordered = orderedStr.isEmpty() ? 0 : Integer.parseInt(orderedStr);
            if (prix < 0 || ordered < 0) throw new NumberFormatException("Cannot be negative");
        } catch (NumberFormatException e) {
            showError("Invalid number format for Price, Category ID, Brand ID, or Ordered count. Ensure they are valid integers (>= 0).");
            return;
        }

        // 4. Fetch existing product to keep created_at
         Produit existingProduct = produitDAO.getProductById(id);
         if (existingProduct == null) {
             showError("Product with ID " + id + " not found.");
             return;
         }
         LocalDate createdAt = existingProduct.getCreated_at(); // Keep original date

        // 5. Create updated Product object
        Produit updatedProduct = new Produit(id, nom, description, prix, catId, marqId,
                                         createdAt,
                                         imgPath.isEmpty() ? null : imgPath,
                                         imgHoverPath.isEmpty() ? null : imgHoverPath,
                                         ordered,
                                         shade.isEmpty() ? null : shade);

        // 6. Call DAO (mettreAJourProduct needs correction in DAO - table name 'product' vs 'produit')
        try {
             // *** IMPORTANT: Fix the table name in ProduitDAO.mettreAJourProduct ***
             // It currently uses "UPDATE product SET ...", likely should be "UPDATE produit SET ..."
            produitDAO.mettreAJourProduct(updatedProduct);
            showSuccess("Product ID " + id + " updated successfully.");
            loadProductData();
            clearProductFields();
        } catch (RuntimeException ex) {
            showError("Error updating product: " + ex.getMessage());
             // Check console for SQL errors related to table name 'product'
            ex.printStackTrace();
        }
    }

    private void deleteProduct() {
        String idStr = productIdField.getText();
        if (idStr.isEmpty()) {
            showError("Please select a product from the table to delete.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            showError("Invalid Product ID.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete product ID " + id + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                 // *** IMPORTANT: Fix the table name in ProduitDAO.supprimerProduct ***
                 // It currently uses "DELETE FROM product WHERE ...", likely should be "DELETE FROM produit WHERE ..."
                produitDAO.supprimerProduct(id);
                showSuccess("Product ID " + id + " deleted successfully.");
                loadProductData();
                clearProductFields();
            } catch (RuntimeException ex) {
                showError("Error deleting product: " + ex.getMessage());
                 // Check console for SQL errors related to table name 'product'
                ex.printStackTrace();
            }
        }
    }


    // --- Utility Methods ---

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        statusBar.setText(message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        statusBar.setText("Error: " + message);
    }

    // --- Main Method ---
    public static void main(String[] args) {
        // Set Look and Feel (optional, makes it look less like old Java)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel.");
        }

        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}