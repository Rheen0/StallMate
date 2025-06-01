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

    public OrderView() {
        this.productPanel = new ProductPanel();
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

        // Top half: Product list
        add(new JLabel("Select Product to Add:"));
        add(new JScrollPane(productPanel));

        // Quantity + Add
        JPanel qtyPanel = new JPanel();
        qtyPanel.add(new JLabel("Quantity:"));
        qtyPanel.add(quantityField);
        qtyPanel.add(addToOrderButton);
        add(qtyPanel);

        // Middle: Current Order (OrderPanel)
        add(new JLabel("Current Order:"));
        add(new JScrollPane(orderPanel));

        // Bottom: Total, Checkout, Cancel
        JPanel bottom = new JPanel();
        bottom.add(totalLabel);
        bottom.add(checkoutButton);
        bottom.add(cancelOrderButton);
        add(bottom);
    }

    // Controller hooks
    public JButton getAddToOrderButton() {
        return addToOrderButton;
    }

    public JButton getCheckoutButton() {
        return checkoutButton;
    }

    public JButton getCancelOrderButton() {
        return cancelOrderButton;
    }

    // Table & field accessors
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

    public void populateProductTable(List<Product> products) {
        productPanel.setProducts(products);
    }

    // OrderTable operations (OrderPanel)
    public void addOrderDetail(OrderDetail detail) {
        orderPanel.addOrderItem(
                detail.getProduct().getImage(),
                detail.getProduct().getCategory() + " " + detail.getProduct().getSize(),
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getSubtotal());
    }

    public List<OrderDetail> getOrderDetails() {
        // You’ll need to modify OrderPanel to store and return a List<OrderDetail>.
        return orderPanel.getAllOrderDetails();
    }

    public void updateOrderTotal(double subtotalToAdd) {
        // parse the current total from label, then add subtotalToAdd
        String current = totalLabel.getText().replace("Total: ₱", "").trim();
        double oldTotal = 0.0;
        try {
            oldTotal = Double.parseDouble(current);
        } catch (NumberFormatException ign) {
        }
        double newTotal = oldTotal + subtotalToAdd;
        totalLabel.setText(String.format("Total: ₱%.2f", newTotal));
    }

    public void clearOrder() {
        orderPanel.clearOrder();
        totalLabel.setText("Total: ₱0.00");
        quantityField.setText("");
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
