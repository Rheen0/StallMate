package src.main.java.com.puplagoon.pos.model.dto;

public class Product {
    private int productId;
    private String image;
    private String category;
    private String size;
    private double price;

    // Getters and setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // public String getSugarLevel() {
    //     return sugarLevel;
    // }

    // public void setSugarLevel(String sugarLevel) {
    //     this.sugarLevel = sugarLevel;
    // }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
