package src.main.java.com.puplagoon.pos.view;

import src.main.java.com.puplagoon.pos.model.dto.Inventory;
import src.main.java.com.puplagoon.pos.view.components.InventoryPanel;

import javax.swing.*;
import java.util.List;

public class InventoryView extends JPanel {
    private final InventoryPanel inventoryPanel;
    private final JTextField addQtyField;
    private final JButton addStockButton;
    private final JTextField updateQtyField;
    private final JButton updateStockButton;

    public InventoryView() {
        this.inventoryPanel = new InventoryPanel();
        this.addQtyField = new JTextField(5);
        this.addStockButton = new JButton("Add Stock");
        this.updateQtyField = new JTextField(5);
        this.updateStockButton = new JButton("Update Stock");
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Inventory Items"));
        add(new JScrollPane(inventoryPanel));

        JPanel addPanel = new JPanel();
        addPanel.add(new JLabel("Add Qty:"));
        addPanel.add(addQtyField);
        addPanel.add(addStockButton);

        JPanel updatePanel = new JPanel();
        updatePanel.add(new JLabel("Set New Qty:"));
        updatePanel.add(updateQtyField);
        updatePanel.add(updateStockButton);

        // add(addPanel);
        add(updatePanel);
    }

    public void populateInventoryTable(List<Inventory> items) {
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

    public void showSuccessMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
