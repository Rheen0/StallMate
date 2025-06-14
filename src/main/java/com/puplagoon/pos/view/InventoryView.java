package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;
import src.main.java.com.puplagoon.pos.view.components.InventoryPanel;
import src.main.java.com.puplagoon.pos.model.dto.User;

import javax.swing.*;
import java.util.List;

public class InventoryView extends JPanel {
    private final InventoryPanel inventoryPanel;
    private final User user;
    private JTextField addQtyField;
    private JButton addStockButton;
    private JTextField updateQtyField;
    private JButton updateStockButton;

    public InventoryView(User user) {
        this.user = user;
        this.inventoryPanel = new InventoryPanel();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Inventory Items"));
        add(new JScrollPane(inventoryPanel));

        if ("admin".equalsIgnoreCase(user.getRole())) {
            JPanel addPanel = new JPanel();
            addQtyField = new JTextField(5);
            addStockButton = new JButton("Add Stock");
            addPanel.add(new JLabel("Add Qty:"));
            addPanel.add(addQtyField);
            addPanel.add(addStockButton);
            add(addPanel);

            JPanel updatePanel = new JPanel();
            updateQtyField = new JTextField(5);
            updateStockButton = new JButton("Update Stock");
            updatePanel.add(new JLabel("Set New Qty:"));
            updatePanel.add(updateQtyField);
            updatePanel.add(updateStockButton);
            add(updatePanel);
        }
    }

    public void refreshInventory(List<Inventory> items) {
        inventoryPanel.setInventoryItems(items);
    }

    public Inventory getSelectedInventoryItem() {
        return inventoryPanel.getSelectedInventoryItem();
    }

    public int getAddQuantity() {
        try {
            return Integer.parseInt(addQtyField.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getUpdateQuantity() {
        try {
            return Integer.parseInt(updateQtyField.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public JButton getAddStockButton() {
        return addStockButton;
    }

    public JButton getUpdateStockButton() {
        return updateStockButton;
    }

    public void showMessage(String msg, boolean isError) {
        JOptionPane.showMessageDialog(this, msg, isError ? "Error" : "Success", 
            isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}