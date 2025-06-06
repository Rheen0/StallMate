package src.main.java.com.puplagoon.pos.model.dto;

import javax.swing.ImageIcon;

public class Inventory {
    private int id;
    private int productId;
    private int quantity;
    // private double price;

    private String productCategory;
    private String productSize;
    private String productSugarLevel;
    private double productPrice;
    private ImageIcon productImage;

    public Inventory() {
        // initialize fields if necessary
    }

    public Inventory(int id, int productId, int quantity,
            String productCategory, String productSize, String productSugarLevel,
            double productPrice, ImageIcon productImage) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.productCategory = productCategory;
        this.productSize = productSize;
        this.productSugarLevel = productSugarLevel;
        this.productPrice = productPrice;
        this.productImage = productImage;
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

    // public double getPrice() {
    // return price;
    // }

    // public void setPrice(double price) {
    // this.price = price;
    // }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductSize() {
        return productSize;
    }

    public String getProductSugarLevel() {
        return productSugarLevel;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public ImageIcon getProductImage() {
        return productImage;
    }

    public void setProductCategory(String category) {
        this.productCategory = category;
    }

    public void setProductSize(String size) {
        this.productSize = size;
    }

    public void setProductSugarLevel(String sugarLevel) {
        this.productSugarLevel = sugarLevel;
    }

    public void setProductPrice(double price) {
        this.productPrice = price;
    }

    public void setProductImage(ImageIcon image) {
        this.productImage = image;
    }
}
