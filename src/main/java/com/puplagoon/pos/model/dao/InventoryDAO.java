package src.main.java.com.puplagoon.pos.model.dao;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    private final Connection connection;

    public InventoryDAO() {
        this.connection = DBConnection.getConnection();
    }

    public List<Inventory> findAll() throws SQLException {
        List<Inventory> inventory = new ArrayList<>();
        String query = "SELECT * FROM Inventory";
        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                inventory.add(mapResultSetToInventory(rs));
            }
        }
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
        item.setId(rs.getInt("inventory_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity_in_stock"));
        return item;
    }
}
