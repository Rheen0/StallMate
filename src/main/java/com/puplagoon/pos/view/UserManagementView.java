package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.view.components.UserPanel;

import javax.swing.*;
import java.util.List;

public class UserManagementView extends JPanel {
    private final UserPanel userPanel;
    private final JButton createUserButton;
    private final JButton updateUserButton;
    private final JButton deleteUserButton;
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

    public void refreshUsers(List<User> users) {
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

    public void showMessage(String msg, boolean isError) {
        JOptionPane.showMessageDialog(this, msg, isError ? "Error" : "Success",
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}