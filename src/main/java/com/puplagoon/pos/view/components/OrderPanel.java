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
                new Object[] { "Product", "Size", "Quantity", "Unit Price", "Subtotal" }, 0) {
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

    public void updateOrderItem(String productName, String size, int quantity, double unitPrice, double subtotal) {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(productName) && model.getValueAt(i, 1).equals(size)) { // Match product name and size
                model.setValueAt(quantity, i, 2); // Update quantity
                model.setValueAt(unitPrice, i, 3); // Update unit price
                model.setValueAt(subtotal, i, 4); // Update subtotal
                return; // Exit after updating
            }
        }
    }

    public void addOrderItem(Product product, int quantity, double unitPrice, double subtotal) {
        tableModel.addRow(new Object[] { product.getCategory(), product.getSize(), quantity, unitPrice, subtotal });
    
        // Also store in our internal list
        OrderDetail detail = new OrderDetail();
        detail.setProduct(product); // Set the Product object here
        detail.setQuantity(quantity);
        detail.setUnitPrice(unitPrice);
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
