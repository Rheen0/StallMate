package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;

import javax.swing.*;

import java.awt.*;

import java.util.List;

public class InventoryPanel extends JPanel {
    private final JPanel cardsPanel;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setInventoryItems(List<Inventory> items) {
        cardsPanel.removeAll();
        if (items != null && !items.isEmpty()) {
            for (Inventory item : items) {
                InventoryCard card = new InventoryCard(item);
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        cardsPanel.getComponent(0).setBackground(Color.WHITE);
                        card.setBackground(card.getStockColor().darker());
                    }
                });
                cardsPanel.add(card);
            }
        } else {
            JLabel emptyLabel = new JLabel("No inventory items found", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            cardsPanel.add(emptyLabel);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public Color getCardColor(Inventory item) {
        int quantity = item.getQuantity();
        if (quantity <= 0)
            return new Color(255, 220, 220); // red-ish
        if (quantity <= 5)
            return new Color(255, 255, 200); // yellow-ish
        return new Color(220, 255, 220); // green-ish
    }

    public Inventory getSelectedInventoryItem() {
        for (Component comp : cardsPanel.getComponents()) {
            if (comp instanceof InventoryCard && comp.getBackground().equals(comp.getBackground().darker())) {
                return ((InventoryCard) comp).getInventory();
            }
        }
        return null;
    }
}