package src.main.java.com.puplagoon.pos.view.components;

import src.main.java.com.puplagoon.pos.model.dto.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductCard extends JPanel {
    private final Product product;
    private final JLabel imageLabel;
    private final JLabel nameLabel;
    private final JLabel priceLabel;
    private boolean selected = false;
    private static final Color SELECTED_COLOR = new Color(200, 230, 255);

    public ProductCard(Product product) {
        this.product = product;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setPreferredSize(new Dimension(150, 180));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateImage();

        // Name (Category + Size)
        nameLabel = new JLabel(
                "<html><center>" + product.getCategory() + "<br>" + product.getSize() + "</center></html>");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Price
        priceLabel = new JLabel(String.format("₱%.2f", product.getPrice()));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 100, 0));

        // Layout
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);

        add(imageLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);

        // Add hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!isSelected());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    setBackground(new Color(240, 240, 240));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    setBackground(null);
                }
            }
        });
    }

    private void updateImage() {
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                java.net.URL imgURL = getClass().getResource("/assets/" + product.getImage());
                if (imgURL != null) {
                    ImageIcon originalIcon = new ImageIcon(imgURL);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    setFallbackIcon();
                }
            } catch (Exception e) {
                setFallbackIcon();
            }
        } else {
            setFallbackIcon();
        }
    }

    private void setFallbackIcon() {
        imageLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        imageLabel.setText("");
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        setBackground(selected ? SELECTED_COLOR : null);
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    public Product getProduct() {
        return product;
    }
}