package src.main.java.com.puplagoon.pos.controller;

import src.main.java.com.puplagoon.pos.model.dao.OrderDAO;
import src.main.java.com.puplagoon.pos.model.dao.ProductDAO;
import src.main.java.com.puplagoon.pos.model.dto.*;
import src.main.java.com.puplagoon.pos.service.*;

import src.main.java.com.puplagoon.pos.view.OrderView;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OrderController {
    private final OrderView view;
    private final OrderService orderService;
    private final User currentUser;
    private final InventoryService inventoryService;

    public OrderController(OrderView view, User user, InventoryService inventoryService) {
        this.view = view;
        this.currentUser = user;
        this.inventoryService = inventoryService;
        this.orderService = new OrderService(new OrderDAO(), new ProductDAO());
        initialize();
    }

    private void initialize() {
        loadProducts();
        view.getAddToOrderButton().addActionListener(e -> addItemToOrder());
        view.getCheckoutButton().addActionListener(e -> processCheckout());
        view.getCancelOrderButton().addActionListener(e -> confirmCancelOrder());
    }

    private void loadProducts() {
        List<Product> products = orderService.findAllProducts();
        view.refreshProducts(products);
    }

    private void addItemToOrder() {
        Product selected = view.getSelectedProduct();
        int qty = view.getSelectedQuantity();

        if (selected == null) {
            view.showMessage("Please select a product first", true);
            return;
        }

        try {
            int currentStock = inventoryService.getStockForProduct(selected.getProductId());

            if (currentStock <= 0) {
                view.showMessage("Cannot add: " + selected.getCategory() + " is OUT OF STOCK", true);
                return;
            }

            if (qty <= 0) {
                view.showMessage("Please enter a valid quantity", true);
                return;
            }

            if (qty > currentStock) {
                view.showMessage("Only " + currentStock + " items available in stock", true);
                return;
            }

            OrderDetail detail = new OrderDetail();
            detail.setProduct(selected);
            detail.setQuantity(qty);
            detail.setUnitPrice(selected.getPrice());
            detail.setSubtotal(selected.getPrice() * qty);

            view.addOrderDetail(detail);
        } catch (SQLException e) {
            view.showMessage("Error checking inventory: " + e.getMessage(), true);
        }
    }

    private void processCheckout() {
        List<OrderDetail> details = view.getOrderDetails();
        if (details.isEmpty()) {
            view.showMessage("No items in order", true);
            return;
        }

        // Validate stock first
        try {
            for (OrderDetail detail : details) {
                int currentStock = inventoryService.getStockForProduct(detail.getProduct().getProductId());
                if (currentStock < detail.getQuantity()) {
                    view.showMessage("Not enough stock for " + detail.getProduct().getCategory(), true);
                    return;
                }
            }
        } catch (SQLException e) {
            view.showMessage("Error checking inventory", true);
            return;
        }

        Order order = new Order();
        order.setDetails(details);

        view.showProcessingMessage("Processing your order...");

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return orderService.processOrder(order);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        view.showMessage("Order processed successfully!", false);
                        view.clearOrder();
                        loadProducts(); // Refresh product availability
                    } else {
                        view.showMessage("Failed to process order", true);
                    }
                } catch (Exception e) {
                    view.showMessage("Error processing order: " + e.getMessage(), true);
                }
            }
        }.execute();
    }

    private double calculateTotal(List<OrderDetail> details) {
        return details.stream().mapToDouble(OrderDetail::getSubtotal).sum();
    }

    private void confirmCancelOrder() {
        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to cancel this order?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION)
            return;

        JPanel pinPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel pinLabel = new JLabel("Enter 4-digit Manager PIN:");
        JPasswordField pinField = new JPasswordField(4);
        pinPanel.add(pinLabel);
        pinPanel.add(pinField);

        int pinResponse = JOptionPane.showConfirmDialog(view, pinPanel,
                "Manager Authorization", JOptionPane.OK_CANCEL_OPTION);

        if (pinResponse == JOptionPane.OK_OPTION &&
                new String(pinField.getPassword()).equals("1234")) {
            view.clearOrder();
            view.showMessage("Order cancelled successfully.", false);
        } else {
            view.showMessage("Incorrect PIN. Order not cancelled.", true);
        }
    }
}