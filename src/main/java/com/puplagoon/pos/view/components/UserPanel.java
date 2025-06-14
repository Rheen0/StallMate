package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserPanel extends JPanel {
    private final JTable userTable;
    private final UserTableModel tableModel;

    public UserPanel() {
        this.tableModel = new UserTableModel();
        this.userTable = new JTable(tableModel);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(userTable), BorderLayout.CENTER);
    }

    public JTable getUserTable() {
        return userTable;
    }

    public void setUsers(List<User> users) {
        tableModel.setUsers(users);
    }

    public User getSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row < 0)
            return null;
        return tableModel.getUserAt(row);
    }

    public List<User> getUsers() {
        return new ArrayList<>(tableModel.users);
    }

    private static class UserTableModel extends AbstractTableModel {
        private List<User> users = new ArrayList<>();
        private final String[] columnNames = { "ID", "Name", "Username", "Role" };

        public void setUsers(List<User> users) {
            this.users = users != null ? users : new ArrayList<>();
            fireTableDataChanged();
        }

        public User getUserAt(int row) {
            return users.get(row);
        }

        @Override
        public int getRowCount() {
            return users.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            User u = users.get(row);
            return switch (col) {
                case 0 -> u.getUserId();
                case 1 -> u.getName();
                case 2 -> u.getUsername();
                case 3 -> u.getRole();
                default -> null;
            };
        }
    }
}
