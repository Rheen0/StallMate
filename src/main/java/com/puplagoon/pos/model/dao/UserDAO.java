package src.main.java.com.puplagoon.pos.model.dao;

import src.main.java.com.puplagoon.pos.model.dto.User;
import java.sql.*;

public class UserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = DBConnection.getConnection();
    }

    // public User findAllUsers() throws SQLException {
    // String query = "SELECT * FROM Users";
    // try (Statement stmt = connection.createStatement();
    // ResultSet rs = stmt.executeQuery(query)) {
    // if (rs.next()) {
    // return mapResultSetToUser(rs);
    // }
    // }
    // return null; // or throw an exception if no users found
    // }

    // These are AI Generated
    public boolean insertUser(User user) throws SQLException {
        String query = "INSERT INTO Users (name, user_name, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateUser(User user) throws SQLException {
        String query = "UPDATE Users SET name = ?, user_name = ?, password = ?, role = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getUserId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM Users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    public User findByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE user_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setUsername(rs.getString("user_name"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
