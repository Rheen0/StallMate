package src.main.java.com.puplagoon.pos.view.components;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import src.main.java.com.puplagoon.pos.model.dto.OrderDetail;
import src.main.java.com.puplagoon.pos.model.dto.Product;

public class OrderPanel extends JPanel {
    private final JTable orderTable;
    private final DefaultTableModel tableModel;

    // We keep a separate list of OrderDetail, so we can return it to the controller
    // quickly
    private final List<OrderDetail> orderDetails = new ArrayList<>();

    public OrderPanel() {
        this.tableModel = new DefaultTableModel(
                new Object[] { "Product", "Quantity", "Price", "Subtotal" }, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }

        };

        this.orderTable = new JTable(tableModel);
        orderTable.setRowHeight(40);
        initializeUI();

    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        orderTable.setIntercellSpacing(new Dimension(10, 5));
        orderTable.setShowGrid(false);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);
    }

    public void updateOrderItem(String image, String description, int quantity, double unitPrice, double subtotal) {
        // Logic to find and update the corresponding row in the order table
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            // Check if the description in the current row matches the given description
            String existingDescription = (String) tableModel.getValueAt(i, 0); // Column 1 is the "Product" column
            if (existingDescription.equals(description)) {
                // Update the row with the new values
                // tableModel.setValueAt(image, i, 0); // Update the "Image" column
                tableModel.setValueAt(description, i, 1); // Update the "Product" column
                tableModel.setValueAt(quantity, i, 2); // Update the "Quantity" column
                tableModel.setValueAt(String.format("₱%.2f", unitPrice), i, 3); // Update the "Price" column
                tableModel.setValueAt(String.format("₱%.2f", subtotal), i, 4); // Update the "Subtotal" column
                return; // Exit the method after updating the row
            }
        }
    }

    public void addOrderItem(Product product, String productName, int qty, double price, double subtotal) {
        // Load image
        ImageIcon icon = null;
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            java.net.URL imgURL = getClass().getResource("/assets/" + product.getImage());
            if (imgURL != null) {
                icon = new ImageIcon(
                        new ImageIcon(imgURL).getImage()
                                .getScaledInstance(48, 48, Image.SCALE_SMOOTH));
            }
        }

        tableModel.addRow(new Object[] {
                productName,
                qty,
                String.format("₱%.2f", price),
                String.format("₱%.2f", subtotal)
        });

        // Also store in our internal list
        OrderDetail detail = new OrderDetail();
        detail.setProduct(product); // Set the product here
        detail.setQuantity(qty);
        detail.setUnitPrice(price);
        detail.setSubtotal(subtotal);
        orderDetails.add(detail);
    }

    public List<OrderDetail> getAllOrderDetails() {
        return new ArrayList<>(orderDetails);
    }

    public void clearOrder() {
        tableModel.setRowCount(0);
        orderDetails.clear();
    }
}
