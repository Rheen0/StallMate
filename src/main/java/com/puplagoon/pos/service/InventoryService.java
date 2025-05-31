package src.main.java.com.puplagoon.pos.service;

import src.main.java.com.puplagoon.pos.model.dao.InventoryDAO;
import src.main.java.com.puplagoon.pos.model.dto.Inventory;

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

    public boolean updateStock(int productId, int newQty) {
        try {
            return inventoryDAO.updateStock(productId, newQty);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
