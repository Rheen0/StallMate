package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryPanel extends JPanel {
    private final JTable inventoryTable;
    private final InventoryTableModel tableModel;

    public InventoryPanel() {
        this.tableModel = new InventoryTableModel();
        this.inventoryTable = new JTable(tableModel);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        inventoryTable.setRowHeight(30); // Reduced row height since we don't need space for images
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
    }

    public void setInventoryItems(List<Inventory> items) {
        tableModel.setInventoryItems(items);
    }

    public Inventory getSelectedInventoryItem() {
        int row = inventoryTable.getSelectedRow();
        if (row < 0)
            return null;
        return tableModel.getInventoryAt(row);
    }

    private static class InventoryTableModel extends AbstractTableModel {
        private List<Inventory> items = new ArrayList<>();
        private final String[] columnNames = {
                "ID", "Category", "Size", "Price", "Quantity"
        };

        public void setInventoryItems(List<Inventory> items) {
            this.items = items != null ? items : new ArrayList<>();
            fireTableDataChanged();
        }

        public Inventory getInventoryAt(int row) {
            return items.get(row);
        }

        @Override
        public int getRowCount() {
            return items.size();
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
            Inventory inv = items.get(row);
            return switch (col) {
                case 0 -> inv.getId();
                case 1 -> inv.getProductCategory();
                case 2 -> inv.getProductSize();
                case 3 -> inv.getProductPrice();
                case 4 -> inv.getQuantity();
                default -> null;
            };
        }

    }
}
