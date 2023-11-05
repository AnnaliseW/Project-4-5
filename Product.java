public class Product {
    private String productName;
    private String storeName;
    private String descriptionOfProduct;
    private int quantityAvailable;
    private double price;
    private int quantitySold;

    public Product(String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price) {
        this.productName = productName;
        this.storeName = storeName;
        this.descriptionOfProduct = descriptionOfProduct;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
    }

    public Product() {
        this.productName = "";
        this.storeName = "";
        this.descriptionOfProduct = "";
        this.quantityAvailable = 0;
        this.price = 0;
    }

    public String getProductName() {
        return productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getDescriptionOfProduct() {
        return descriptionOfProduct;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setDescriptionOfProduct(String descriptionOfProduct) {
        this.descriptionOfProduct = descriptionOfProduct;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name: %s\nDescription: %s\nQuantity Available: %d\nPrice: %.2f", this.productName
        , this.storeName, this.descriptionOfProduct, this.quantityAvailable, this.price);
    }

     public String listingPagetoString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name: %s\nPrice: %.2f", this.productName, this.storeName, this.price);
    }

}
