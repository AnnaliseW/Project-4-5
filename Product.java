/**
 * Product class for a specific instance of a product on market including their statistics used in other classes
 * <p>
 * Purdue University -- CS18000 -- Fall 2023 -- Project 5 -- Product
 *
 * @author Annalise Wang, Joseph Hsin, Aviana Franco, Taylor Kamel Purdue CS
 * @version Dec 11, 2023
 */
public class Product {

    public String productName;
    public String storeName;
    public String descriptionOfProduct;
    public int quantityAvailable;
    public double price;
    public int quantitySold;


    public Product(String productName, String storeName, String
            descriptionOfProduct, int quantityAvailable, double price) {
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
        return String.format("Product Name: " +
                        "%s, Store Name: %s, Description: %s, Quantity Available:" +
                        " %d, Price: %.2f", productName
                , storeName, descriptionOfProduct, quantityAvailable, price);
    }

    public String listingPagetoString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name:" +
                " %s\nPrice: %.2f", getProductName(), getStoreName(), getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product objectO = (Product) o;
            return objectO.getProductName().equals(this.productName) &&
                    objectO.getStoreName().equals(this.storeName)
                    && objectO.getDescriptionOfProduct().equals(this.descriptionOfProduct)
                    && objectO.getQuantityAvailable() == this.quantityAvailable
                    && objectO.getPrice() == (this.price);

        } else {
            return false;
        }
    }

    public String statisticsToStringNoSpace() {
        return String.format("Product Statistics:Product Name: %s Store Name: %s " +
                        "Description: %s Quantity Available: %d Price: %.2f", productName
                , storeName, descriptionOfProduct, quantityAvailable, price);
    }


}
