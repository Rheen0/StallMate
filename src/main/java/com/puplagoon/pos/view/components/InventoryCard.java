package src.main.java.com.puplagoon.pos.view.components;

import javax.swing.*;
import java.awt.*;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;

public class InventoryCard extends JPanel {
    private final Inventory inventory;
    private static final Color IN_STOCK_COLOR = new Color(220, 255, 220);
    private static final Color LOW_STOCK_COLOR = new Color(255, 255, 200);
    private static final Color OUT_OF_STOCK_COLOR = new Color(255, 220, 220);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public InventoryCard(Inventory inventory) {
        this.inventory = inventory;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setBackground(getStockColor());
        setPreferredSize(new Dimension(180, 220));
        initializeComponents();
    }

    public Color getStockColor() {
        int quantity = inventory.getQuantity();
        if (quantity <= 0)
            return OUT_OF_STOCK_COLOR;
        if (quantity <= 5)
            return LOW_STOCK_COLOR;
        return IN_STOCK_COLOR;
    }

    private void initializeComponents() {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadProductImage(imageLabel);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(inventory.getProductCategory(), SwingConstants.CENTER);
        nameLabel.setFont(TITLE_FONT);

        JLabel priceLabel = new JLabel(String.format("₱%.2f", inventory.getProductPrice()), SwingConstants.CENTER);
        priceLabel.setFont(PRICE_FONT);

        JLabel stockLabel = new JLabel(getStockText(), SwingConstants.CENTER);
        stockLabel.setFont(LABEL_FONT);
        if (inventory.getQuantity() <= 0) {
            stockLabel.setForeground(Color.RED);
        }

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);

        add(imageLabel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private String getStockText() {
        int quantity = inventory.getQuantity();
        if (quantity <= 0)
            return "OUT OF STOCK";
        if (quantity <= 5)
            return "LOW STOCK: " + quantity;
        return "In Stock: " + quantity;
    }

    private void loadProductImage(JLabel imageLabel) {
        try {
            if (inventory.getProductImage() != null) {
                ImageIcon icon = new ImageIcon(inventory.getProductImage().getImage()
                        .getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                imageLabel.setIcon(icon);
            } else {
                imageLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                imageLabel.setText("No Image");
            }
        } catch (Exception e) {
            imageLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
            imageLabel.setText("Image Error");
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}