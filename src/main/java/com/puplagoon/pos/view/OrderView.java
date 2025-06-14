package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.model.dto.OrderDetail;
import src.main.java.com.puplagoon.pos.model.dto.Product;
import src.main.java.com.puplagoon.pos.view.components.OrderPanel;
import src.main.java.com.puplagoon.pos.view.components.ProductPanel;

import javax.swing.*;
import java.util.List;

public class OrderView extends JPanel {
    private final ProductPanel productPanel;
    private final OrderPanel orderPanel;
    private final JTextField quantityField;
    private final JButton addToOrderButton;
    private final JButton checkoutButton;
    private final JButton cancelOrderButton;
    private final JLabel totalLabel;

    public OrderView(ProductPanel productPanel) {
        this.productPanel = productPanel;
        this.orderPanel = new OrderPanel();
        this.quantityField = new JTextField(5);
        this.addToOrderButton = new JButton("Add to Order");
        this.checkoutButton = new JButton("Checkout");
        this.cancelOrderButton = new JButton("Cancel Order");
        this.totalLabel = new JLabel("Total: ₱0.00");
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Select Product to Add:"));
        add(new JScrollPane(productPanel));

        JPanel qtyPanel = new JPanel();
        qtyPanel.add(new JLabel("Quantity:"));
        qtyPanel.add(quantityField);
        qtyPanel.add(addToOrderButton);
        add(qtyPanel);

        add(new JLabel("Current Order:"));
        add(new JScrollPane(orderPanel));

        JPanel bottom = new JPanel();
        bottom.add(totalLabel);
        bottom.add(checkoutButton);
        bottom.add(cancelOrderButton);
        add(bottom);
    }

    public void refreshProducts(List<Product> products) {
        productPanel.setProducts(products);
    }

    public Product getSelectedProduct() {
        return productPanel.getSelectedProduct();
    }

    public int getSelectedQuantity() {
        try {
            return Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void addOrderDetail(OrderDetail detail) {
        if (detail.getProduct() == null) {
            showMessage("Cannot add item: Product is null", true);
            return;
        }
        orderPanel.addOrderItem(detail);
        updateOrderTotal(detail.getSubtotal());
    }

    public List<OrderDetail> getOrderDetails() {
        return orderPanel.getAllOrderDetails();
    }

    public void updateOrderTotal(double subtotalToAdd) {
        String current = totalLabel.getText().replace("Total: ₱", "").trim();
        double oldTotal = 0.0;
        try {
            oldTotal = Double.parseDouble(current);
        } catch (NumberFormatException ign) {
        }
        totalLabel.setText(String.format("Total: ₱%.2f", oldTotal + subtotalToAdd));
    }

    public void clearOrder() {
        orderPanel.clearOrder();
        totalLabel.setText("Total: ₱0.00");
        quantityField.setText("");
    }

    public JButton getAddToOrderButton() {
        return addToOrderButton;
    }

    public JButton getCheckoutButton() {
        return checkoutButton;
    }

    public JButton getCancelOrderButton() {
        return cancelOrderButton;
    }

    public void showMessage(String message, boolean isError) {
        JOptionPane.showMessageDialog(this, message, isError ? "Error" : "Success",
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    public void showProcessingMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Processing", JOptionPane.INFORMATION_MESSAGE);
    }
}