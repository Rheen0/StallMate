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

    public List<Inventory> findAll() throws SQLException {
        List<Inventory> inventory = new ArrayList<>();
        String query = """
                SELECT
                    i.inventory_id,
                    i.product_id,
                    i.quantity_in_stock,
                    p.category,
                    p.size,
                    p.price,
                    p.image
                FROM Inventory i
                JOIN Product p ON i.product_id = p.product_id
                """;

        System.out.println("Executing query: " + query); // Debug log

        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("Found inventory item with product ID: " + rs.getInt("product_id")); // Debug
                Inventory item = mapResultSetToInventory(rs);
                inventory.add(item);
            }
        }

        System.out.println("Total inventory items loaded: " + inventory.size());
        return inventory;
    }

    public boolean updateStock(int productId, int quantity) throws SQLException {
        String query = "UPDATE Inventory SET quantity_in_stock = ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Pinalitan ko yung DTO class to match inventory
    private Inventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        Inventory item = new Inventory();

        // Set inventory fields
        item.setId(rs.getInt("inventory_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity_in_stock"));

        // Set product fields
        item.setProductCategory(rs.getString("category"));
        item.setProductSize(rs.getString("size"));
        item.setProductPrice(rs.getDouble("price"));

        // Handle image
        String imagePath = rs.getString("image");
        if (imagePath != null && !imagePath.isBlank()) {
            try {
                java.net.URL imgURL = getClass().getResource("/assets/" + imagePath);
                if (imgURL != null) {
                    item.setProductImage(new ImageIcon(imgURL));
                } else {
                    System.err.println("Image not found: " + imagePath);
                    item.setProductImage(null);
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + imagePath);
                e.printStackTrace();
                item.setProductImage(null);
            }
        } else {
            item.setProductImage(null);
        }

        return item;
    }

    // Syncing inventory with products
    public void syncInventoryWithProducts() throws SQLException {
        String sql = """
                    INSERT INTO Inventory (product_id, quantity_in_stock)
                    SELECT p.product_id, 0
                    FROM Product p
                    LEFT JOIN Inventory i ON p.product_id = i.product_id
                    WHERE i.product_id IS NULL
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

}
