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
    private final List<OrderDetail> detailList;

    public OrderPanel() {
        this.detailList = new ArrayList<>();
        this.tableModel = new DefaultTableModel(
                new Object[] { "Product", "Quantity", "Price", "Subtotal" }, 0);
        this.orderTable = new JTable(tableModel);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(orderTable), BorderLayout.CENTER);
    }

    public void addOrderItem(String productName, int qty, double price, double subtotal) {
        tableModel.addRow(new Object[] { productName, qty, price, subtotal });

        // Also store in our internal list
        OrderDetail detail = new OrderDetail();
        detail.setProduct(null); // we only store the name & price here; alternatively, pass in Product
        detail.setQuantity(qty);
        detail.setUnitPrice(price);
        detail.setSubtotal(subtotal);
        detailList.add(detail);
    }

    public List<OrderDetail> getAllOrderDetails() {
        return new ArrayList<>(detailList);
    }

    public void clearOrder() {
        tableModel.setRowCount(0);
        detailList.clear();
    }
}
