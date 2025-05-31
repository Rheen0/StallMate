package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.model.dto.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final User currentUser;

    // Sub-views
    private final OrderView orderView;
    private final InventoryView inventoryView;
    private final UserManagementView userManagementView; // only if admin

    public MainFrame(User user) {
        super("StallMate | Dashboard");
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

        // Top panel: Welcome message
        JLabel welcomeLabel = new JLabel(
                "Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: JTabbedPane (or you could do a JSplitPane + JList for a “sidebar” feel)
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Orders", orderView);
        tabbedPane.addTab("Inventory", inventoryView);

        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            tabbedPane.addTab("Users", userManagementView);
        }

        // Lay out top + center
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
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
