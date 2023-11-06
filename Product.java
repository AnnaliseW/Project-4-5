import java.util.ArrayList;

public class Product {
    public static ArrayList<Product> productsArrayList;
    public static String productName;
    public static String storeName;
    public static String descriptionOfProduct;
    public static int quantityAvailable;
    public static double price;
    public static int quantitySold;


    public Product(String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price) {
        Product.productName = productName;
        Product.storeName = storeName;
        Product.descriptionOfProduct = descriptionOfProduct;
        Product.quantityAvailable = quantityAvailable;
        Product.price = price;
    }

    public static ArrayList<Product> getProductsArrayList() {
        return productsArrayList;
    }

    public static void setQuantitySold(int quantitySold) {
        Product.quantitySold = quantitySold;
    }

    public static int getQuantitySold() {
        return quantitySold;
    }

    public static void setProductsArrayList(ArrayList<Product> productsArrayList) {
        Product.productsArrayList = productsArrayList;
    }



    public static String getProductName() {
        return productName;
    }

    public static String getStoreName() {
        return storeName;
    }

    public static String getDescriptionOfProduct() {
        return descriptionOfProduct;
    }

    public static int getQuantityAvailable() {
        return quantityAvailable;
    }

    public static double getPrice() {
        return price;
    }

    public static void setProductName(String productName) {
        Product.productName = productName;
    }

    public static void setStoreName(String storeName) {
        Product.storeName = storeName;
    }

    public static void setDescriptionOfProduct(String descriptionOfProduct) {
        Product.descriptionOfProduct = descriptionOfProduct;
    }

    public static void setQuantityAvailable(int quantityAvailable) {
        Product.quantityAvailable = quantityAvailable;
    }

    public static void setPrice(double price) {
        Product.price = price;
    }

    public static String statisticsToString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name: %s\nDescription: %s\nQuantity Available: %d\nPrice: %.2f", productName
                , storeName, descriptionOfProduct, quantityAvailable, price);
    }

    public static String listingPagetoString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name: %s\nPrice: %.2f", productName, storeName, price);
    }

}
