package src.main.java.com.puplagoon.pos.service;

import src.main.java.com.puplagoon.pos.model.dao.InventoryDAO;
import src.main.java.com.puplagoon.pos.model.dto.Inventory;
import src.main.java.com.puplagoon.pos.model.dto.User;

import java.sql.SQLException;
import java.util.List;

public class InventoryService {
    private final InventoryDAO inventoryDAO;

    public InventoryService(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public List<Inventory> getAllInventoryItems() {
        try {
            return inventoryDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public int getStockForProduct(int productId) throws SQLException {
        return inventoryDAO.getStockForProduct(productId);
    }

    public boolean updateStock(int inventoryId, int newQty, User user) throws SQLException {
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            throw new SecurityException("Unauthorized access attempt");
        }
        return inventoryDAO.updateStock(inventoryId, newQty);
    }

    public boolean reduceStock(int productId, int quantity, User user) throws SQLException {
        if (!"admin".equalsIgnoreCase(user.getRole()) && !"employee".equalsIgnoreCase(user.getRole())) {
            throw new SecurityException("Unauthorized access attempt");
        }

        // Get current stock first
        int currentStock = inventoryDAO.getStockForProduct(productId);
        if (currentStock < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        return inventoryDAO.updateStock(productId, currentStock - quantity);
    }
}
