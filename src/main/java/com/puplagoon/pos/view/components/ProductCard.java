package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductCard extends JPanel {
    private final Product product;
    private final int currentStock;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font STOCK_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Color SELECTED_COLOR = new Color(200, 230, 255); // Example selected color

    public ProductCard(Product product, int currentStock) {
        this.product = product;
        this.currentStock = currentStock;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setBackground(getBackgroundColor());
        setPreferredSize(new Dimension(160, 190));
        initializeComponents();
    }

    public Color getBackgroundColor() {
        return currentStock > 0 ? Color.WHITE : new Color(255, 220, 220);
    }

    public Color getSelectedColor() {
        return SELECTED_COLOR;
    }

    private void initializeComponents() {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadProductImage(imageLabel);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(product.getCategory(), SwingConstants.CENTER);
        nameLabel.setFont(TITLE_FONT);

        JLabel priceLabel = new JLabel(String.format("₱%.2f", product.getPrice()), SwingConstants.CENTER);
        priceLabel.setFont(PRICE_FONT);

        JLabel stockLabel = new JLabel(getStockText(), SwingConstants.CENTER);
        stockLabel.setFont(STOCK_FONT);
        if (currentStock <= 0) {
            stockLabel.setForeground(Color.RED);
        }

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);

        add(imageLabel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private String getStockText() {
        return currentStock > 0 ? "Available: " + currentStock : "OUT OF STOCK";
    }

    private void loadProductImage(JLabel imageLabel) {
        try {
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                java.net.URL imgURL = getClass().getResource("/assets/" + product.getImage());
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                    imageLabel.setText("No Image");
                }
            } else {
                imageLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
                imageLabel.setText("No Image");
            }
        } catch (Exception e) {
            imageLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
            imageLabel.setText("Image Error");
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getCurrentStock() {
        return currentStock;
    }
}