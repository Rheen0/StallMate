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

    public void addOrderItem(OrderDetail detail) {
        ImageIcon icon = null;
        if (detail.getProduct().getImage() != null) {
            java.net.URL imgURL = getClass().getResource("/assets/" + detail.getProduct().getImage());
            if (imgURL != null) {
                icon = new ImageIcon(new ImageIcon(imgURL).getImage()
                        .getScaledInstance(48, 48, Image.SCALE_SMOOTH));
            }
        }
        tableModel.addRow(new Object[] {
                icon,
                detail.getProduct().getCategory(),
                detail.getQuantity(),
                String.format("₱%.2f", detail.getUnitPrice()),
                String.format("₱%.2f", detail.getSubtotal())
        });

        // Also store in our internal list
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
