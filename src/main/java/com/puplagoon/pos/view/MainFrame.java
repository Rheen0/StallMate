package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.controller.*;
import src.main.java.com.puplagoon.pos.model.dao.*;
import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.service.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import src.main.java.com.puplagoon.pos.view.components.ProductPanel;

public class MainFrame extends JFrame {
    private final User currentUser;

    public MainFrame(User user) {
        super("StallMate");
        this.currentUser = user;

        // Initialize services
        InventoryService inventoryService = new InventoryService(new InventoryDAO());
        OrderService orderService = new OrderService(new OrderDAO(), new ProductDAO());
        UserService userService = new UserService(new UserDAO());

        // Initialize views with their dependencies
        ProductPanel productPanel = new ProductPanel(inventoryService);
        OrderView orderView = new OrderView(productPanel);
        InventoryView inventoryView = new InventoryView(currentUser);
        UserManagementView userManagementView = "admin".equals(user.getRole()) ? new UserManagementView() : null;

        // Initialize controllers
        new OrderController(orderView, currentUser, inventoryService);
        new InventoryController(inventoryView, currentUser);
        if (userManagementView != null) {
            new UserController(userManagementView, userService);
        }

        // Set up UI
        initializeUI(orderView, inventoryView, userManagementView);
    }

    private void initializeUI(OrderView orderView, InventoryView inventoryView,
            UserManagementView userManagementView) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Create top panel with logo and welcome message
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add logo
        topPanel.add(createLogoLabel(50), BorderLayout.WEST);

        // Welcome message
        JLabel welcomeLabel = new JLabel(
                "Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Logout button on the right
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Orders", orderView);
        tabbedPane.addTab("Inventory", inventoryView);

        // Add user management tab if admin
        if (userManagementView != null) {
            tabbedPane.addTab("Users", userManagementView);
        }

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void logout() {
        this.dispose(); // Close the current window
        // Show login window again
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new AuthController(loginView);
            loginView.setVisible(true);
        });
    }

    private JLabel createLogoLabel(int height) {
        try {
            URL imageUrl = getClass().getClassLoader().getResource("resources/logo.png");
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                int width = (int) (originalIcon.getIconWidth() *
                        ((double) height / originalIcon.getIconHeight()));
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        width, height, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
        return new JLabel();
    }
}