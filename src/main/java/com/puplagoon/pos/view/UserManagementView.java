package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.view.components.UserPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.util.List;

public class UserManagementView extends JPanel {
    private final UserPanel userPanel;
    private final JButton createUserButton;
    private final JButton updateUserButton;
    private final JButton deleteUserButton;
    // Fields for new/edit user:
    private final JTextField nameField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleComboBox;

    public UserManagementView() {
        this.userPanel = new UserPanel();
        this.createUserButton = new JButton("Create");
        this.updateUserButton = new JButton("Update");
        this.deleteUserButton = new JButton("Delete");
        this.nameField = new JTextField(10);
        this.usernameField = new JTextField(10);
        this.passwordField = new JPasswordField(10);
        this.roleComboBox = new JComboBox<>(new String[] { "admin", "employee" });

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("User Accounts"));
        add(new JScrollPane(userPanel));

        JPanel form = new JPanel();
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel("Role:"));
        form.add(roleComboBox);

        add(form);

        JPanel buttons = new JPanel();
        buttons.add(createUserButton);
        buttons.add(updateUserButton);
        buttons.add(deleteUserButton);
        add(buttons);
    }

    public List<User> getAllUsers() {
        return userPanel.getUsers();
    }

    public void populateUserTable(List<User> users) {
        userPanel.setUsers(users);
    }

    public User getSelectedUser() {
        return userPanel.getSelectedUser();
    }

    public User getUserFormData() {
        User u = new User();
        u.setName(nameField.getText().trim());
        u.setUsername(usernameField.getText().trim());
        u.setPassword(new String(passwordField.getPassword()));
        u.setRole((String) roleComboBox.getSelectedItem());
        return u;
    }

    public JButton getCreateUserButton() {
        return createUserButton;
    }

    public JButton getUpdateUserButton() {
        return updateUserButton;
    }

    public JButton getDeleteUserButton() {
        return deleteUserButton;
    }

    public void showSuccessMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setNameField(String name) {
        nameField.setText(name);
    }

    public void setUsernameField(String username) {
        usernameField.setText(username);
    }

    public void setPasswordField(String password) {
        passwordField.setText(password);
    }

    public void setRoleComboBox(String role) {
        roleComboBox.setSelectedItem(role);
    }

    // Clear all form fields
    public void clearFormFields() {
        setNameField("");
        setUsernameField("");
        setPasswordField("");
        setRoleComboBox("employee"); // default role
    }

    // Add table selection listener
    public void addUserSelectionListener(ListSelectionListener listener) {
        userPanel.getUserTable().getSelectionModel().addListSelectionListener(listener);
    }

    // Get the JTable for additional configuration if needed
    public JTable getUserTable() {
        return userPanel.getUserTable();
    }

    public javax.swing.JTextField getUsernameField() {
        return usernameField;
    }

    // Refresh the user table
    public void refreshUserTable(List<User> users) {
        userPanel.setUsers(users);
        userPanel.getUserTable().repaint();
    }
}
