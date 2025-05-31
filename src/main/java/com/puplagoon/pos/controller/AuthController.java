package src.main.java.com.puplagoon.pos.controller;

import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.model.dao.UserDAO;
import src.main.java.com.puplagoon.pos.service.AuthService;
import src.main.java.com.puplagoon.pos.view.LoginView;
import src.main.java.com.puplagoon.pos.view.MainFrame;
import src.main.java.com.puplagoon.pos.view.OrderView;
import src.main.java.com.puplagoon.pos.view.InventoryView;
import src.main.java.com.puplagoon.pos.view.UserManagementView;

import javax.swing.*;

public class AuthController {
    private final LoginView view;
    private final AuthService authService;

    public AuthController(LoginView view) {
        this.view = view;
        this.authService = new AuthService(new UserDAO());
        initController();
    }

    private void initController() {
        view.getLoginButton().addActionListener(e -> authenticate());
    }

    private void authenticate() {
        String username = view.getUsername();
        String password = view.getPassword();

        User user = authService.authenticate(username, password);
        if (user != null) {
            // Close login window
            view.dispose();

            // Build main application frame
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(user);

                // 1) Wire up Order tab
                OrderView orderView = mainFrame.getOrderView();
                new OrderController(orderView, user); // pass user in case we need name/role

                // 2) Wire up Inventory tab
                InventoryView inventoryView = mainFrame.getInventoryView();
                new InventoryController(inventoryView, user);

                // 3) If admin, wire up User Management tab
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    UserManagementView userMgmtView = mainFrame.getUserManagementView();
                    new UserController(userMgmtView);
                }

                mainFrame.setVisible(true);
            });
        } else {
            view.showErrorMessage("Invalid username or password");
        }
    }
}