package src.main.java.com.puplagoon.pos.model.dto;

import java.util.List;

public class Order {
    private int orderId;
    private List<OrderDetail> details;
    private double totalAmount;
    private int totalItems; // Counter for total items

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    // Method to add an item and increment the counter
    public void addItem(OrderDetail detail) {
        details.add(detail);
        totalItems += detail.getQuantity(); // Increment by the quantity of the item
    }
    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
