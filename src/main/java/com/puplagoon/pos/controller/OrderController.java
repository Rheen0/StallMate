// Hello World Testing lang

package src.main.java.com.puplagoon.pos.controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import src.main.java.com.puplagoon.pos.model.dto.Order;
import src.main.java.com.puplagoon.pos.model.dto.OrderDetail;
import src.main.java.com.puplagoon.pos.model.dto.Product;
import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.service.OrderService;
import src.main.java.com.puplagoon.pos.view.OrderView;

public class OrderController {
    private final OrderView view;
    private final OrderService orderService;
    private final User currentUser; // in case we want to show “who sold this” or log the user
    private final Order order = new Order(); // Initialize an Order object to manage the current order
    private static final String MANAGEMENT_PIN = "1234";

    public OrderController(OrderView view, User user) {
        this.view = view;
        this.currentUser = user;
        this.orderService = new OrderService(new src.main.java.com.puplagoon.pos.model.dao.OrderDAO(),
                new src.main.java.com.puplagoon.pos.model.dao.ProductDAO());
        initController();
        loadProductsIntoTable();
    }

    private void initController() {
        view.getAddToOrderButton().addActionListener(e -> addItemToOrder());
        view.getCheckoutButton().addActionListener(e -> processCheckout());
        view.getCancelOrderButton().addActionListener(e -> confirmAndCancelOrder());
    }

    private void loadProductsIntoTable() {
        List<Product> allProducts = orderService.findAllProducts();
        view.populateProductTable(allProducts);
    }

    private void addItemToOrder() {
        Product selected = view.getSelectedProduct();
        int qty = view.getSelectedQuantity();
        if (selected != null && qty > 0) {
            OrderDetail detail = new OrderDetail();
            detail.setProduct(selected);
            detail.setQuantity(qty);
            detail.setUnitPrice(selected.getPrice());
            detail.setSubtotal(selected.getPrice() * qty);

            view.addOrderDetail(detail);
            view.updateOrderTotal(detail.getSubtotal());
            // Add the item to the order and increment the counter
            order.addItem(detail);

            // Update the view (if necessary)
            view.addOrderDetail(detail);

            // Log the total items for debugging
            System.out.println("Total items in order: " + order.getTotalItems());
        } else {
            view.showErrorMessage("Please select a valid product and quantity.");
        }

    }

    private void processCheckout() {
        List<OrderDetail> details = view.getOrderDetails();
        if (details == null || details.isEmpty()) {
            view.showErrorMessage("No items in order");
            return;
        }

        // Validate all products exist
        for (OrderDetail detail : details) {
            if (detail.getProduct() == null) {
                view.showErrorMessage("Order contains invalid items (missing product)");
                return;
            }
        }

        Order order = new Order();
        order.setDetails(details);
        order.setTotalAmount(calculateTotal(details));
        // Optionally set who created the order:
        // order.setCreatedBy(currentUser.getUserId());

        if (orderService.processOrder(order)) {
            view.showSuccessMessage("Order processed successfully");
            view.clearOrder();
            // Get the most recent order ID
            int recentOrderId = orderService.getMostRecentOrderId();
            System.out.println("Most recent order ID: " + recentOrderId);

            // Pass the order ID to the receipt printer
            order.setOrderId(recentOrderId); // Set the most recent order ID

            // Print the receipt
            ReceiptPrinter printer = new ReceiptPrinter();
            printer.printReceipt(order, currentUser);
        } else {
            view.showErrorMessage("Failed to process order");
        }

    }

    private double calculateTotal(List<OrderDetail> details) {
        return details.stream()
                .mapToDouble(OrderDetail::getSubtotal)
                .sum();
    }

    private void confirmAndCancelOrder() {
        // First confirm if user really wants to cancel
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to cancel this order?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Create a password field for PIN entry
            JPasswordField passwordField = new JPasswordField();
            passwordField.setEchoChar('*'); // This will show asterisks when typing

            Object[] message = {
                    "Enter management PIN to cancel order:",
                    passwordField
            };

            int option = JOptionPane.showConfirmDialog(
                    view,
                    message,
                    "Authorization Required",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                // Get the password as a String
                String enteredPin = new String(passwordField.getPassword());

                if (enteredPin.equals(MANAGEMENT_PIN)) {
                    // Correct pin - cancel the order
                    cancelOrder();
                    view.showSuccessMessage("Order has been cancelled");
                } else {
                    // Wrong pin or cancelled
                    view.showErrorMessage("Invalid PIN. Order was not cancelled.");
                }
            } else {
                // User clicked cancel or closed the dialog
                view.showErrorMessage("Order cancellation was cancelled.");
            }
        }
    }

    private void cancelOrder() {
        view.clearOrder();
        order.setDetails(null); // Clear the current order
        order.setTotalAmount(0);
        order.setTotalItems(0);
    }
}
