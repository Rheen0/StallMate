package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.OrderDetail;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderPanel extends JPanel {
    private final JTable orderTable;
    private final DefaultTableModel tableModel;

    // We keep a separate list of OrderDetail, so we can return it to the controller
    // quickly
    private final List<OrderDetail> orderDetails = new ArrayList<>();

    public OrderPanel() {
        this.tableModel = new DefaultTableModel(
                new Object[] { "Image", "Product", "Quantity", "Price", "Subtotal" }, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0)
                    return ImageIcon.class;
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        this.orderTable = new JTable(tableModel);
        orderTable.setRowHeight(50);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        orderTable.setIntercellSpacing(new Dimension(10, 5));
        orderTable.setShowGrid(false);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);
    }

    public void addOrderItem(String imageFilename, String productName, int qty, double price, double subtotal) {
        // Load image
        ImageIcon icon = null;
        if (imageFilename != null && !imageFilename.isEmpty()) {
            java.net.URL imgURL = getClass().getResource("/assets/" + imageFilename);
            if (imgURL != null) {
                icon = new ImageIcon(
                        new ImageIcon(imgURL).getImage()
                                .getScaledInstance(48, 48, Image.SCALE_SMOOTH));
            }
        }

        tableModel.addRow(new Object[] {
                icon,
                productName,
                qty,
                String.format("₱%.2f", price),
                String.format("₱%.2f", subtotal)
        });

        // Also store in our internal list
        OrderDetail detail = new OrderDetail();
        detail.setProduct(null); // we only store the name & price here; alternatively, pass in Product
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
