
public class Product {

    public String productName;
    public String storeName;
    public String descriptionOfProduct;
    public int quantityAvailable;
    public double price;
    public int quantitySold;


    public Product(String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price) {
        this.productName = productName;
        this.storeName = storeName;
        this.descriptionOfProduct = descriptionOfProduct;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescriptionOfProduct() {
        return descriptionOfProduct;
    }

    public void setDescriptionOfProduct(String descriptionOfProduct) {
        this.descriptionOfProduct = descriptionOfProduct;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String statisticsToString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name: %s\nDescription: %s\nQuantity Available: %d\nPrice: %.2f", productName
                , storeName, descriptionOfProduct, quantityAvailable, price);
    }

    public String listingPagetoString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name: %s\nPrice: %.2f", getProductName(), getStoreName(), getPrice());
    }

}
