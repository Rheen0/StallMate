package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.model.dto.User;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainFrame extends JFrame {
    private final User currentUser;

    // Sub-views
    private final OrderView orderView;
    private final InventoryView inventoryView;
    private final UserManagementView userManagementView; // only if admin

    public MainFrame(User user) {
        super("StallMate");
        this.currentUser = user;

        this.orderView = new OrderView();
        this.inventoryView = new InventoryView();
        this.userManagementView = new UserManagementView();

        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null); // center on screen

        // Create top panel with logo and welcome message
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add small logo on the left
        JLabel logoLabel = createLogoLabel(50); // 50px height
        topPanel.add(logoLabel, BorderLayout.WEST);

        // Welcome message in center
        JLabel welcomeLabel = new JLabel(
                "Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Left: JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Orders", orderView);
        tabbedPane.addTab("Inventory", inventoryView);

        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            tabbedPane.addTab("Users", userManagementView);
        }

        // Lay out top + center
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JLabel createLogoLabel(int height) {
        try {
            URL imageUrl = getClass().getClassLoader().getResource("resources/logo.png");
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                // Scale proportionally to desired height
                int width = (int) (originalIcon.getIconWidth() *
                        ((double) height / originalIcon.getIconHeight()));
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        width, height, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
        return new JLabel(); // Return empty label if image fails to load
    }

    // Expose getters so controllers can hook into each sub-view
    public OrderView getOrderView() {
        return orderView;
    }

    public InventoryView getInventoryView() {
        return inventoryView;
    }

    public UserManagementView getUserManagementView() {
        return userManagementView;
    }
}
