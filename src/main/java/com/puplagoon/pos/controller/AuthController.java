package src.main.java.com.puplagoon.pos.controller;

import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.model.dao.UserDAO;
import src.main.java.com.puplagoon.pos.service.AuthService;
import src.main.java.com.puplagoon.pos.view.LoginView;
import src.main.java.com.puplagoon.pos.view.MainFrame;

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
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(user);
                mainFrame.setVisible(true);
            });
        } else {
            view.showErrorMessage("Invalid username or password");
        }
    }
}