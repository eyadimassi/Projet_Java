package Projetjava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SignUpDialog extends JDialog {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField addressField;
    private JButton submitButton;
    private JButton cancelButton;
    private JLabel statusLabel;

    private UtilisateurDAO utilisateurDAO;

    public SignUpDialog(Frame owner, UtilisateurDAO dao) {
        super(owner, "Client Sign Up", true);
        this.utilisateurDAO = dao;

        initComponents();
        layoutComponents();
        attachListeners();

        setSize(450, 350);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        addressField = new JTextField(20);
        submitButton = new JButton("Sign Up");
        cancelButton = new JButton("Cancel");
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
    }

    private void layoutComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(confirmPasswordField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(addressField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);

        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void attachListeners() {
        submitButton.addActionListener(e -> performSignUp());
        cancelButton.addActionListener(e -> dispose());
    }

    private void performSignUp() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        char[] confirmPasswordChars = confirmPasswordField.getPassword();
        String address = addressField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || passwordChars.length == 0 || confirmPasswordChars.length == 0) {
            statusLabel.setText("Username, Email, and Passwords are required.");
            return;
        }

        if (!Utilisateur.validerEmail(email)) {
            statusLabel.setText("Invalid email format.");
            return;
        }

        if (!Arrays.equals(passwordChars, confirmPasswordChars)) {
            statusLabel.setText("Passwords do not match.");
            passwordField.setText("");
            confirmPasswordField.setText("");
            return;
        }

        try {
            if (utilisateurDAO.usernameExists(username)) {
                statusLabel.setText("Username '" + username + "' is already taken.");
                return;
            }
            if (utilisateurDAO.emailExists(email)) {
                statusLabel.setText("Email '" + email + "' is already registered.");
                return;
            }
        } catch (RuntimeException ex) {
            statusLabel.setText("Error checking user existence: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        String password = new String(passwordChars);
        Utilisateur newUser = new Utilisateur(
                0,
                username,
                email,
                password,
                "client",
                address,
                null
        );

        try {
            utilisateurDAO.ajouterUtilisateur(newUser);
            JOptionPane.showMessageDialog(this,
                    "Sign up successful for user '" + username + "'!\nPlease log in.",
                    "Sign Up Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (RuntimeException ex) {
            statusLabel.setText("Error during sign up: " + ex.getMessage());
            ex.printStackTrace();
             passwordField.setText("");
             confirmPasswordField.setText("");
        } finally {
            Arrays.fill(passwordChars, ' ');
            Arrays.fill(confirmPasswordChars, ' ');
        }
    }
}