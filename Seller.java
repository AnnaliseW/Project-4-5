

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Seller extends User {
    private static double revenue;
    private static ArrayList<String> sales;
    public static ArrayList<Product> itemsSellingBySeller;

    public static ArrayList<SoldProduct> itemsSold;
    // ?? similar to sales maybe delete
    // sales could include... products sold, customer name(occurs when bought added), revenue added

    public static ArrayList<Customer> customersWhoBought; //Lists the customers who bought something from the seller


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

    public static ArrayList<SoldProduct> getItemsSold() {
        return itemsSold;
    }

    public static void setItemsSold(ArrayList<SoldProduct> itemsSold) {
        Seller.itemsSold = itemsSold;
    }

    public double getRevenue() {
        return revenue;
    }

    public ArrayList<String> getSales() {
        return sales;
    }

    public static ArrayList<Customer> getCustomersWhoBought(){
        return customersWhoBought;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setSales(ArrayList<String> sales) {
        this.sales = sales;
    }

    public static void addProduct(Product addedProduct) {
        itemsSellingBySeller.add(addedProduct);
        Product.getProductsArrayList().add(addedProduct);
    }

    public static void removeProduct(Product product) {

        for (int i = 0; i < itemsSellingBySeller.size(); i++) {
            if (itemsSellingBySeller.get(i).equals(product)) {
                itemsSellingBySeller.remove(i);
            }
        }


        for (int i = 0; i < Product.getProductsArrayList().size(); i++) {
            if (Product.getProductsArrayList().get(i).equals(product)) {
                Product.getProductsArrayList().remove(i);
            }
        }
    }

    public static void modifyProduct(Product product, String newProductName, String newStoreName,
                                     String newDescriptionOfProduct, int newQuantityAvailable, double newPrice) {
        for (int i = 0; i < itemsSellingBySeller.size(); i++) {
            if (itemsSellingBySeller.get(i).equals(product)) {
                itemsSellingBySeller.get(i).setProductName(newProductName);
                itemsSellingBySeller.get(i).setStoreName(newStoreName);
                itemsSellingBySeller.get(i).setDescriptionOfProduct(newDescriptionOfProduct);
                itemsSellingBySeller.get(i).setQuantityAvailable(newQuantityAvailable);
                itemsSellingBySeller.get(i).setPrice(newPrice);
            }
        }

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

    public static String salesCustRev(){
        String s = "";
        ArrayList<String> storeNames = new ArrayList<String>();
        ArrayList<String> purchasedBy = new ArrayList<String>();
        ArrayList<String> productNames = new ArrayList<String>();
        boolean hasStoreName;
        boolean hasProductName;
        for(int i = 0; i < itemsSold.size(); i++){ //for loop to add to an array list one of each store
            hasStoreName = false;
            for(int ii = 0; ii < storeNames.size(); i++){
                if(storeNames.get(ii).equals(itemsSold.get(i).getStoreName())){
                    hasStoreName = true;
                }
            }
            if(hasStoreName == false){
                storeNames.add(itemsSold.get(i).getStoreName());
            }
        }

        for(int i = 0; i < itemsSold.size(); i++){ //for loop to add to an array list one of each product
            hasProductName = false;
            for(int ii = 0; ii < productNames.size(); ii++){
                if(productNames.get(ii).equals(itemsSold.get(i).getProductName())){
                    hasProductName = false;
                }
            }
            if(hasProductName == false){
                productNames.add(itemsSold.get(i).getProductName());
            }
        }

        for(int i = 0; i < itemsSold.size(); i++){ // for loop to add the store names followed by who purchased it and the revenues of each item
            s += storeNames.get(i) + ": \nPurchased By: ";
            for(int ii = 0; ii < itemsSold.size(); ii++){//for loop to add who purchased it
                if(storeNames.get(i).equals(itemsSold.get(ii).getStoreName())){
                    s += itemsSold.get(ii).getPurchasedBy() + ", ";
                }
            }
            s += "\nRevenues: ";
            int productQuantity = 0;
            for(int ii = 0; ii < itemsSold.size(); ii++) { //for loop to add revenues of each item
                if (productNames.get(i).equals(itemsSold.get(ii).getProductName())) {
                    productQuantity++;
                }
                s += productNames.get(i) + " - " + calculateRevenue(productQuantity, itemsSold.get(ii).getPrice()) + ", ";
            }
        }
        return s;
    }

    /// create page to see sales + customer information and revenues from the sale.

}
