package src.main.java.com.puplagoon.pos.model.dao;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class InventoryDAO {
    private final Connection connection;

    public InventoryDAO() {
        this.connection = DBConnection.getConnection();
    }

    // Get current stock for a specific product
    public int getStockForProduct(int productId) throws SQLException {
        String query = "SELECT quantity_in_stock FROM inventory WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("quantity_in_stock") : 0;
            }
        }
    }

    // Get all inventory items with product details (for card display)
    public List<Inventory> findAll() throws SQLException {
        List<Inventory> items = new ArrayList<>();
        String query = """
                SELECT i.inventory_id, i.product_id, i.quantity_in_stock,
                       p.category, p.price, p.image
                FROM inventory i
                JOIN product p ON i.product_id = p.product_id
                """;

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                items.add(mapResultSetToInventory(rs));
            }
        }
        return items;
    }

    // Update stock quantity (admin only)
    public boolean updateStock(int productId, int newQuantity) throws SQLException {
        String query = "UPDATE inventory SET quantity_in_stock = ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Helper method to map database row to Inventory object
    private Inventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        Inventory item = new Inventory();
        item.setId(rs.getInt("inventory_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity_in_stock"));

        // Product details for the card UI
        item.setProductCategory(rs.getString("category"));
        item.setProductPrice(rs.getDouble("price"));

        // Load product image
        String imagePath = rs.getString("image");
        if (imagePath != null && !imagePath.isBlank()) {
            java.net.URL imgURL = getClass().getResource("/assets/" + imagePath);
            if (imgURL != null) {
                item.setProductImage(new ImageIcon(imgURL));
            }
        }

        return item;
    }

    // Optional: Auto-create inventory entries for new products
    public void syncInventory() throws SQLException {
        String query = """
                INSERT INTO inventory (product_id, quantity_in_stock)
                SELECT p.product_id, 0
                FROM product p
                LEFT JOIN inventory i ON p.product_id = i.product_id
                WHERE i.product_id IS NULL
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }
    }
}