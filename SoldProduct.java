/**
 * SoldProduct class for a specific instance of a product that was sold on market including their statistics used in other classes
 * <p>
 * Purdue University -- CS18000 -- Fall 2023 -- Project 5 -- SoldProduct
 *
 * @author Annalise Wang, Joseph Hsin, Aviana Franco, Taylor Kamel Purdue CS
 * @version Dec 11, 2023
 */
public class SoldProduct extends Product {
    public int quantityPurchased;

    public SoldProduct(String productName, String storeName, String descriptionOfProduct, int quantityAvailable,
                       double price, int quantityPurchased) {
        super(productName, storeName, descriptionOfProduct, quantityAvailable, price);
        this.quantityPurchased = quantityPurchased;
    }


    public SoldProduct(Product product, int quantityPurchased) {
        super(product.getProductName(), product.getStoreName(), product.getDescriptionOfProduct(),
                product.getQuantityAvailable(), product.getPrice());
        this.quantityPurchased = quantityPurchased;
    }

    public int getQuantityPurchased() {
        return this.quantityPurchased;
    }

    public String customerRecieptStatistics() {
        return String.format("Product Name: " +
                        "%s, Store Name: %s, Description: %s, Quantity Available:" +
                        " %d, Price: %.2f, Quantity Purchased: %d", productName
                , storeName, descriptionOfProduct, quantityAvailable, price, quantityPurchased);

    }
}
