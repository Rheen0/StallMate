package src.main.java.com.puplagoon.pos.service;

import src.main.java.com.puplagoon.pos.model.dao.UserDAO;
import src.main.java.com.puplagoon.pos.model.dto.User;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // public List<User> getAllUsers() {
    //     // Suppose you add a findAll() method in UserDAO, or implement below
    //     try {
    //         return userDAO.findAllUsers();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return List.of();
    //     }
    // }

    public boolean createUser(User user) {
        try {
            return userDAO.insertUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        try {
            return userDAO.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return userDAO.deleteUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
