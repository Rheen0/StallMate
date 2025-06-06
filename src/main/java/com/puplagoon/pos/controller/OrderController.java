package src.main.java.com.puplagoon.pos.controller;

import java.util.List;
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
        view.getCancelOrderButton().addActionListener(e -> cancelOrder());
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
        if (details.isEmpty()) {
            view.showErrorMessage("No items in order");
            return;
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

    private void cancelOrder() {
        view.clearOrder();
    }
}
