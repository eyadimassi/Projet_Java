package Projetjava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    private UtilisateurDAO utilisateurDAO;
    private Connection connection; // Keep connection reference if needed by other frames

    public LoginFrame() {
        super("Login - Cosmetics Website");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout(10, 10)); // Add gaps

        // --- Initialize Database Connection and DAO ---
        try {
            // Get connection using your existing method
            connection = CosmeticsWebsite.getConnection();
            utilisateurDAO = new UtilisateurDAO(connection);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection error: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if DB connection fails
        }

        // --- Create UI Components ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Component padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Row
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        // Password Row
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        // Login Button Row
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        loginButton = new JButton("Login");
        formPanel.add(loginButton, gbc);

        // Status Label
        statusLabel = new JLabel(" ", SwingConstants.CENTER); // Start with empty text
        statusLabel.setForeground(Color.RED);

        // --- Add Components to Frame ---
        add(formPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // --- Attach Listeners ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Allow login on pressing Enter in password field
        passwordField.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and Password cannot be empty.");
            return;
        }

        try {
            Utilisateur user = utilisateurDAO.getUtilisateurByUsernameAndPassword(username, password);

            if (user != null) {
                // Login successful
                statusLabel.setText("Login successful!"); // Optional brief success message
                dispose(); // Close the login window

                // Open the appropriate interface based on role
                String role = user.getRole();
                if (role != null && role.equalsIgnoreCase("admin")) {
                    // Launch Admin Dashboard
                    SwingUtilities.invokeLater(() -> {
                        // Pass the connection if AdminDashboard needs it directly,
                        // otherwise it can get its own via CosmeticsWebsite.getConnection()
                        AdminDashboard adminDashboard = new AdminDashboard();
                        adminDashboard.setVisible(true);
                    });
                } else { // Default to client interface if role is "client" or null/other
                    // Launch Client Interface
                    SwingUtilities.invokeLater(() -> {
                        ClientInterface clientInterface = new ClientInterface(user, connection); // Pass user and connection
                        clientInterface.setVisible(true);
                    });
                }

            } else {
                // Login failed
                statusLabel.setText("Invalid username or password.");
                passwordField.setText(""); // Clear password field
            }
        } catch (RuntimeException ex) {
            statusLabel.setText("Error during login. See console.");
            showError("Login Error: " + ex.getMessage()); // Show detailed error in popup
            ex.printStackTrace();
        }
    }

     private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Main method to start the application
    public static void main(String[] args) {
         // Set Look and Feel (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel.");
        }

        // Run the Login GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}