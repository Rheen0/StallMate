package src.main.java.com.puplagoon.pos.view;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Image;
import java.net.URL;

public class LoginView extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginView() {
        super("StallMate");
        this.usernameField = new JTextField(15);
        this.passwordField = new JPasswordField(15);
        this.loginButton = new JButton("Login");
        initializeUI();
    }

    private void initializeUI() {
        setSize(350, 250); // Slightly increased height to accommodate logo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add small logo at the top (row 0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createLogoLabel(), gbc);

        // Reset gridwidth for other components
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;

        // Username row (now row 1)
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password row (row 2)
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button spans 2 columns (row 3)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        add(panel);
    }

    private JLabel createLogoLabel() {
        try {
            // Load the image (adjust path as needed)
            URL imageUrl = getClass().getClassLoader().getResource("resources/logo.png");
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                // Scale to small size (adjust width as needed)
                Image scaledImage = originalIcon.getImage().getScaledInstance(80, -1, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
        // Fallback: Empty label if image fails to load
        return new JLabel();
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
}