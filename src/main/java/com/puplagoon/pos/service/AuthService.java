package src.main.java.com.puplagoon.pos.service;

import src.main.java.com.puplagoon.pos.model.dao.UserDAO;
import src.main.java.com.puplagoon.pos.model.dto.User;

import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
