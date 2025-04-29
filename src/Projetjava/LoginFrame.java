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
    private Connection connection; 

    public LoginFrame() {
        super("Login - Cosmetics Website");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10)); 

        try {
            connection = CosmeticsWebsite.getConnection();
            utilisateurDAO = new UtilisateurDAO(connection);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection error: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); 
        }

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);


        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);


        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        loginButton = new JButton("Login");
        formPanel.add(loginButton, gbc);


        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);

      
        add(formPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

      
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        
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
                statusLabel.setText("Login successful!");
                dispose();

               
                String role = user.getRole();
                if (role != null && role.equalsIgnoreCase("admin")) {
                   
                    SwingUtilities.invokeLater(() -> {
                        AdminDashboard adminDashboard = new AdminDashboard();
                        adminDashboard.setVisible(true);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        ClientInterface clientInterface = new ClientInterface(user, connection);
                        clientInterface.setVisible(true);
                    });
                }

            } else {
                statusLabel.setText("Invalid username or password.");
                passwordField.setText(""); 
            }
        } catch (RuntimeException ex) {
            statusLabel.setText("Error during login. See console.");
            showError("Login Error: " + ex.getMessage()); 
            ex.printStackTrace();
        }
    }

     private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel.");
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}