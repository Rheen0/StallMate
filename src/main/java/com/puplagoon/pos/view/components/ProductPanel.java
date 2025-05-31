package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.Product;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductPanel extends JPanel {
    private final JTable productTable;
    private final ProductTableModel tableModel;

    public ProductPanel() {
        this.tableModel = new ProductTableModel();
        this.productTable = new JTable(tableModel);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(productTable), BorderLayout.CENTER);
    }

    public void setProducts(List<Product> products) {
        tableModel.setProducts(products);
    }

    public Product getSelectedProduct() {
        int row = productTable.getSelectedRow();
        if (row < 0)
            return null;
        return tableModel.getProductAt(row);
    }

    private static class ProductTableModel extends AbstractTableModel {
        private List<Product> products = new ArrayList<>();
        private final String[] columnNames = { "ID", "Category", "Size", "Sugar Level", "Price" };

        public void setProducts(List<Product> products) {
            this.products = products != null ? products : new ArrayList<>();
            fireTableDataChanged();
        }

        public Product getProductAt(int row) {
            return products.get(row);
        }

        @Override
        public int getRowCount() {
            return products.size();
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
            Product p = products.get(row);
            return switch (col) {
                case 0 -> p.getProductId();
                case 1 -> p.getCategory();
                case 2 -> p.getSize();
                case 3 -> p.getSugarLevel();
                case 4 -> p.getPrice();
                default -> null;
            };
        }
    }
}
