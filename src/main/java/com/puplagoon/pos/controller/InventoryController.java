package src.main.java.com.puplagoon.pos.controller;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;
import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.service.InventoryService;
import src.main.java.com.puplagoon.pos.view.InventoryView;
import src.main.java.com.puplagoon.pos.model.dao.InventoryDAO;

import java.sql.SQLException;
import java.util.List;

public class InventoryController {
    private final InventoryView view;
    private final InventoryService inventoryService;
    private final User currentUser;

    public InventoryController(InventoryView view, User user) {
        this.view = view;
        this.currentUser = user;
        this.inventoryService = new InventoryService(new InventoryDAO());
        initialize();
    }

    private void initialize() {
        loadInventory();
        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            view.getAddStockButton().addActionListener(e -> addStock());
            view.getUpdateStockButton().addActionListener(e -> updateStock());
        }
    }

    private void loadInventory() {
        List<Inventory> items = inventoryService.getAllInventoryItems();
        view.refreshInventory(items);
    }

    private void addStock() {
        Inventory item = view.getSelectedInventoryItem();
        int addQty = view.getAddQuantity();

        if (item == null) {
            view.showMessage("Please select an item first", true);
            return;
        }

        if (addQty <= 0) {
            view.showMessage("Please enter a valid quantity", true);
            return;
        }

        try {
            if (inventoryService.updateStock(item.getProductId(), item.getQuantity() + addQty, currentUser)) {
                view.showMessage("Stock added successfully", false);
                loadInventory();
            }
        } catch (SQLException e) {
            view.showMessage("Database error: " + e.getMessage(), true);
        }
    }

    private void updateStock() {
        Inventory item = view.getSelectedInventoryItem();
        int newQty = view.getUpdateQuantity();

        if (item == null) {
            view.showMessage("Please select an item first", true);
            return;
        }

        if (newQty < 0) {
            view.showMessage("Please enter a valid quantity", true);
            return;
        }

        try {
            if (inventoryService.updateStock(item.getId(), newQty, currentUser)) {
                view.showMessage("Stock updated successfully", false);
                loadInventory();
            } else {
                view.showMessage("Failed to update stock", true);
            }
        } catch (SQLException e) {
            view.showMessage("Database error: " + e.getMessage(), true);
        }
    }
}