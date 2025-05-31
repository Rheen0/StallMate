package src.main.java.com.puplagoon.pos;

import src.main.java.com.puplagoon.pos.controller.AuthController;
import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.view.LoginView;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Show login screen
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new AuthController(loginView);
            loginView.setVisible(true);
        });
    }
}