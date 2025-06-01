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
    private final JLabel imagePreview;

    public ProductPanel() {
        this.tableModel = new ProductTableModel();
        this.productTable = new JTable(tableModel);
        this.imagePreview = new JLabel();
        this.imagePreview.setHorizontalAlignment(JLabel.CENTER);
        this.imagePreview.setPreferredSize(new Dimension(400, 50));
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Main product table with scroll
        JScrollPane tableScroll = new JScrollPane(productTable);

        // Right panel for image preview
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(imagePreview, BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add selection listener for image preview
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateImagePreview();
            }
        });

        // Combine components
        add(tableScroll, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void updateImagePreview() {
        Product product = getSelectedProduct();
        if (product == null || product.getImage() == null) {
            imagePreview.setIcon(null);
            imagePreview.setText("No Image");
            return;
        }

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/" + product.getImage()));
            Image scaledImage = icon.getImage().getScaledInstance(
                    imagePreview.getWidth(),
                    imagePreview.getHeight(),
                    Image.SCALE_SMOOTH);
            imagePreview.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imagePreview.setIcon(null);
            imagePreview.setText("Image Missing");
        }
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
                case 4 -> String.format("₱%.2f", p.getPrice());
                default -> null;
            };
        }
    }
}