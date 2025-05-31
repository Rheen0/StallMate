package src.main.java.com.puplagoon.pos.controller;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;
import src.main.java.com.puplagoon.pos.model.dto.User;
import src.main.java.com.puplagoon.pos.service.InventoryService;
import src.main.java.com.puplagoon.pos.view.InventoryView;

import java.util.List;

public class InventoryController {
    private final InventoryView view;
    private final InventoryService inventoryService;
    private final User currentUser; // optional, in case we log who restocked

    public InventoryController(InventoryView view, User user) {
        this.view = view;
        this.currentUser = user;
        this.inventoryService = new InventoryService(new src.main.java.com.puplagoon.pos.model.dao.InventoryDAO());
        initController();
        loadInventoryItems();
    }

    private void initController() {
        view.getAddStockButton().addActionListener(e -> addStock());
        view.getUpdateStockButton().addActionListener(e -> updateStock());
    }

    private void loadInventoryItems() {
        List<Inventory> items = inventoryService.getAllInventoryItems();
        view.populateInventoryTable(items);
    }


    // Pinalitan ko yung DTO class
    private void addStock() {
        Inventory item = view.getSelectedInventoryItem();
        int addQty = view.getAddQuantity();
        if (item != null && addQty > 0) {
            int newQty = item.getQuantity() + addQty;
            if (inventoryService.updateStock(item.getId(), newQty)) {
                view.showSuccessMessage("Stock added successfully");
                loadInventoryItems();
            } else {
                view.showErrorMessage("Failed to add stock");
            }
        }
    }

    private void updateStock() {
        Inventory item = view.getSelectedInventoryItem();
        int newQty = view.getUpdateQuantity();
        if (item != null && newQty >= 0) {
            if (inventoryService.updateStock(item.getId(), newQty)) {
                view.showSuccessMessage("Stock updated successfully");
                loadInventoryItems();
            } else {
                view.showErrorMessage("Failed to update stock");
            }
        }
    }
}
