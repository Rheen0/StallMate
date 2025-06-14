package src.main.java.com.puplagoon.pos.controller;

import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.service.UserService;
import src.main.java.com.puplagoon.pos.view.UserManagementView;

import java.sql.SQLException;
import java.util.List;

public class UserController {
    private final UserManagementView view;
    private final UserService userService;

    public UserController(UserManagementView view, UserService userService) {
        this.view = view;
        this.userService = userService;
        initController();
        loadAllUsers();
    }

    private void initController() {
        view.getCreateUserButton().addActionListener(e -> createUser());
        view.getUpdateUserButton().addActionListener(e -> updateUser());
        view.getDeleteUserButton().addActionListener(e -> deleteUser());
    }

    private void loadAllUsers() {
        List<User> users = userService.getAllUsers();
        view.refreshUsers(users);
    }

    private void createUser() {
        User newUser = view.getUserFormData(); // e.g. name, username, password, role
        if (userService.createUser(newUser)) {
            view.showMessage("User created", true);
            loadAllUsers();
        } else {
            view.showMessage("Failed to create user", true);
        }
    }

    private void updateUser() {
        User selected = view.getSelectedUser();
        if (selected == null) {
            view.showMessage("Select a user first", true);
            return;
        }
        User updated = view.getUserFormData(); // new data from form fields
        updated.setUserId(selected.getUserId());
        if (userService.updateUser(updated)) {
            view.showMessage("User updated", true);
            loadAllUsers();
        } else {
            view.showMessage("Failed to update user", true);
        }
    }

    private void deleteUser() {
        User selected = view.getSelectedUser();
        if (selected == null) {
            view.showMessage("Select a user first", true);
            return;
        }
        if (userService.deleteUser(selected.getUserId())) {
            view.showMessage("User deleted", true);
            loadAllUsers();
        } else {
            view.showMessage("Failed to delete user", true);
        }
    }
}
