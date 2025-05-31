package src.main.java.com.puplagoon.pos.controller;

import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.service.UserService;
import src.main.java.com.puplagoon.pos.view.UserManagementView;

import java.util.List;

public class UserController {
    private final UserManagementView view;
    private final UserService userService;

    public UserController(UserManagementView view) {
        this.view = view;
        this.userService = new UserService(new src.main.java.com.puplagoon.pos.model.dao.UserDAO());
        // initController();
        // loadAllUsers();
    }

    // private void initController() {
    //     view.getCreateUserButton().addActionListener(e -> createUser());
    //     view.getUpdateUserButton().addActionListener(e -> updateUser());
    //     view.getDeleteUserButton().addActionListener(e -> deleteUser());
    // }

    // private void loadAllUsers() {
    // List<User> all = userService.getAllUsers();
    // view.populateUserTable(all);
    // }

    // private void createUser() {
    // User newUser = view.getUserFormData(); // e.g. name, username, password, role
    // if (userService.createUser(newUser)) {
    // view.showSuccessMessage("User created");
    // loadAllUsers();
    // } else {
    // view.showErrorMessage("Failed to create user");
    // }
    // }

    // private void updateUser() {
    // User selected = view.getSelectedUser();
    // if (selected == null) {
    // view.showErrorMessage("Select a user first");
    // return;
    // }
    // User updated = view.getUserFormData(); // new data from form fields
    // updated.setUserId(selected.getUserId());
    // if (userService.updateUser(updated)) {
    // view.showSuccessMessage("User updated");
    // loadAllUsers();
    // } else {
    // view.showErrorMessage("Failed to update user");
    // }
    // }

    // private void deleteUser() {
    // User selected = view.getSelectedUser();
    // if (selected == null) {
    // view.showErrorMessage("Select a user first");
    // return;
    // }
    // if (userService.deleteUser(selected.getUserId())) {
    // view.showSuccessMessage("User deleted");
    // loadAllUsers();
    // } else {
    // view.showErrorMessage("Failed to delete user");
    // }
    // }
}
