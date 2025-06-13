package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.Product;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ProductPanel extends JPanel {
    private final JPanel cardPanel;
    private ProductCard selectedCard = null;

    public ProductPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create panel for cards with wrapping flow layout
        cardPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 10, 10));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cardPanel.setBackground(Color.WHITE);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setProducts(List<Product> products) {
        cardPanel.removeAll();
        selectedCard = null;

        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                ProductCard card = new ProductCard(product);
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (selectedCard != null) {
                            selectedCard.setSelected(false);
                        }
                        selectedCard = card;
                        selectedCard.setSelected(true);
                    }
                });
                cardPanel.add(card);
            }
        } else {
            JLabel emptyLabel = new JLabel("No products available", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            cardPanel.add(emptyLabel);
        }

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public Product getSelectedProduct() {
        return selectedCard != null ? selectedCard.getProduct() : null;
    }
}

// Helper class for wrapping layout
class WrapLayout extends FlowLayout {
    public WrapLayout() {
        super();
    }

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;
            if (targetWidth == 0) {
                targetWidth = Integer.MAX_VALUE;
            }

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);

            Dimension dim = new Dimension(0, 0);
            int rowWidth = 0;
            int rowHeight = 0;

            for (Component comp : target.getComponents()) {
                if (comp.isVisible()) {
                    Dimension d = comp.getPreferredSize();
                    if (rowWidth + d.width > maxWidth) {
                        dim.width = Math.max(dim.width, rowWidth);
                        dim.height += rowHeight + vgap;
                        rowWidth = 0;
                        rowHeight = 0;
                    }
                    if (rowWidth != 0) {
                        rowWidth += hgap;
                    }
                    rowWidth += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                }
            }
            dim.width = Math.max(dim.width, rowWidth);
            dim.height += rowHeight;

            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom + vgap * 2;
            return dim;
        }
    }
}