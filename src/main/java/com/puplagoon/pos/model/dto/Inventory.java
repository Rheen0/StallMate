package src.main.java.com.puplagoon.pos.model.dto;

public class Inventory {
    private int id;
    private int productId;
    private int quantity;
    private double price;

    public Inventory() {
        // initialize fields if necessary
    }

    public Inventory(int id, int productId, int quantity, double price) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    // public String getProductName() {
    // return productName;
    // }

    // public void setProductName(String productName) {
    // this.productName = productName;
    // }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
