package src.main.java.com.puplagoon.pos.controller;

import javax.swing.JOptionPane;
import java.util.List;
import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.service.UserService;
import src.main.java.com.puplagoon.pos.view.UserManagementView;

public class UserController {
    private final UserManagementView view;
    private final UserService userService;

    public UserController(UserManagementView view) {
        this.view = view;
        this.userService = new UserService(new src.main.java.com.puplagoon.pos.model.dao.UserDAO());
        initController();
        loadAllUsers();
    }

    private void initController() {
        view.getCreateUserButton().addActionListener(e -> createUser());
        view.getUpdateUserButton().addActionListener(e -> updateUser());
        view.getDeleteUserButton().addActionListener(e -> deleteUser());
    }

    private void loadAllUsers() {
        try {
            // We'll need to add a findAllUsers() method to UserDAO
            List<User> users = userService.getAllUsers();
            view.populateUserTable(users);
        } catch (Exception e) {
            view.showErrorMessage("Failed to load users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createUser() {
        User newUser = view.getUserFormData();

        // Validate form first
        if (!validateUserForm(newUser)) {
            return;
        }

        // Check if username already exists
        if (userService.isUsernameTaken(newUser.getUsername())) {
            int option = JOptionPane.showConfirmDialog(
                    view,
                    "Username '" + newUser.getUsername()
                            + "' already exists.\nDo you want to choose a different username?",
                    "Username Exists",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // Let user try again
                view.setUsernameField("");
                view.getUsernameField().requestFocus();
                return;
            } else {
                // User chose to continue anyway (maybe not the best practice)
                proceedWithUserCreation(newUser);
            }
        } else {
            proceedWithUserCreation(newUser);
        }
    }

    private void proceedWithUserCreation(User user) {
        if (userService.createUser(user)) {
            view.showSuccessMessage("User created successfully");
            clearForm();
            loadAllUsers();
        } else {
            view.showErrorMessage("Failed to create user");
        }
    }

    private void updateUser() {
        User selectedUser = view.getSelectedUser();
        if (selectedUser == null) {
            view.showErrorMessage("Please select a user first");
            return;
        }

        User updatedUser = view.getUserFormData();
        updatedUser.setUserId(selectedUser.getUserId());

        if (validateUserForm(updatedUser)) {
            if (userService.updateUser(updatedUser)) {
                view.showSuccessMessage("User updated successfully");
                clearForm();
                loadAllUsers();
            } else {
                view.showErrorMessage("Failed to update user");
            }
        }
    }

    private void deleteUser() {
        User selectedUser = view.getSelectedUser();
        if (selectedUser == null) {
            view.showErrorMessage("Please select a user first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to delete user: " + selectedUser.getName() + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (userService.deleteUser(selectedUser.getUserId())) {
                view.showSuccessMessage("User deleted successfully");
                clearForm();
                loadAllUsers();
            } else {
                view.showErrorMessage("Failed to delete user");
            }
        }
    }

    private boolean validateUserForm(User user) {
        if (user.getName().isEmpty()) {
            view.showErrorMessage("Name is required");
            return false;
        }
        if (user.getUsername().isEmpty()) {
            view.showErrorMessage("Username is required");
            return false;
        }
        if (user.getPassword().isEmpty()) {
            view.showErrorMessage("Password is required");
            return false;
        }
        if (user.getRole() == null) {
            view.showErrorMessage("Role is required");
            return false;
        }

        // Basic username validation (adjust as needed)
        if (user.getUsername().length() < 4) {
            view.showErrorMessage("Username must be at least 4 characters");
            return false;
        }

        return true;
    }

    private void clearForm() {
        // You'll need to add these methods to UserManagementView
        view.setNameField("");
        view.setUsernameField("");
        view.setPasswordField("");
        view.setRoleComboBox("employee"); // default role
    }
}