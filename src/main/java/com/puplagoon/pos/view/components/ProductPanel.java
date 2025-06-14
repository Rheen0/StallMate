package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.Product;
import src.main.java.com.puplagoon.pos.service.InventoryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class ProductPanel extends JPanel {
    private final JPanel cardsPanel;
    private final InventoryService inventoryService;
    private ProductCard selectedCard;

    public ProductPanel(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setProducts(List<Product> products) {
        cardsPanel.removeAll();
        selectedCard = null; // Reset selection when products are reloaded
        
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                try {
                    int stock = inventoryService.getStockForProduct(product.getProductId());
                    ProductCard card = new ProductCard(product, stock);
                    
                    card.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            // Reset previous selection
                            if (selectedCard != null) {
                                selectedCard.setBackground(selectedCard.getBackgroundColor());
                            }
                            // Set new selection
                            selectedCard = card;
                            card.setBackground(card.getBackgroundColor().darker());
                        }
                    });
                    
                    cardsPanel.add(card);
                } catch (SQLException e) {
                    cardsPanel.add(new ProductCard(product, 0));
                }
            }
        } else {
            JLabel emptyLabel = new JLabel("No products available", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            cardsPanel.add(emptyLabel);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public Product getSelectedProduct() {
        return selectedCard != null ? selectedCard.getProduct() : null;
    }
}