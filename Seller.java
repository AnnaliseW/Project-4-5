
import java.util.ArrayList;

public class Seller extends User {
    private static double revenue;
    private static ArrayList<String> sales;
    public static ArrayList<Product> itemsSellingBySeller;

    public static ArrayList<Product> itemsSold;
    // ?? similar to sales maybe delete
    // sales could include... products sold, customer name(occurs when bought added), revenue added



    public Seller(String productName, String storeName, String descriptionOfProduct,
                  int quantityAvailable, double price, double revenue,
                  ArrayList<String> sales){
        super(productName, storeName, descriptionOfProduct, quantityAvailable, price);
        this.revenue = revenue;
        this.sales = sales;
    }

    public static ArrayList<Product> getItemsSellingBySeller() {
        return itemsSellingBySeller;
    }

    public static void setItemsSellingBySeller(ArrayList<Product> itemsSellingBySeller) {
        Seller.itemsSellingBySeller = itemsSellingBySeller;
    }

    public static ArrayList<Product> getItemsSold() {
        return itemsSold;
    }

    public static void setItemsSold(ArrayList<Product> itemsSold) {
        Seller.itemsSold = itemsSold;
    }

    public double getRevenue() {
        return revenue;
    }

    public ArrayList<String> getSales() {
        return sales;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setSales(ArrayList<String> sales) {
        this.sales = sales;
    }

    public static void addProduct(Product addedProduct) {
        Product.getProductsArrayList().add(addedProduct);
    }

    public static void removeProduct(Product product) {
        for (int i = 0; i < Product.getProductsArrayList().size(); i++) {
            if (Product.getProductsArrayList().get(i).equals(product)) {
                Product.getProductsArrayList().remove(i);
            }
        }
    }

    public static void modifyProduct(Product product, String newProductName, String newStoreName,
                              String newDescriptionOfProduct, int newQuantityAvailable, double newPrice) {
        for (int i = 0; i < Product.getProductsArrayList().size(); i++) {
            if (Product.getProductsArrayList().get(i).equals(product)) {
                Product.getProductsArrayList().get(i).setProductName(newProductName);
                Product.getProductsArrayList().get(i).setStoreName(newStoreName);
                Product.getProductsArrayList().get(i).setDescriptionOfProduct(newDescriptionOfProduct);
                Product.getProductsArrayList().get(i).setQuantityAvailable(newQuantityAvailable);
                Product.getProductsArrayList().get(i).setPrice(newPrice);
            }
        }
    }

    public static double calculateRevenue(int quantitySold, double price) {
        double revenue = quantitySold * price;
        return revenue;
    }

    /// create page to see sales + customer information and revenues from the sale.










}
