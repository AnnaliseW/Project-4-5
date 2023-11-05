import java.util.ArrayList;

public class Seller extends Product {
    private double revenue;
    private ArrayList<String> sales;
    // sales could include... products sold, customer name(occurs when bought added), revenue added



    public Seller(String productName, String storeName, String descriptionOfProduct,
                  int quantityAvailable, double price, double revenue,
                  ArrayList<String> sales){
        super(productName, storeName, descriptionOfProduct, quantityAvailable, price);
        this.revenue = revenue;
        this.sales = sales;
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

    public void addProduct(Product addedProduct) {
        getProductsArrayList().add(addedProduct);
    }

    public void removeProduct(Product product) {
        for (int i = 0; i < getProductsArrayList().size(); i++) {
            if (getProductsArrayList().get(i).equals(product)) {
                getProductsArrayList().remove(i);
            }
        }
    }

    public void modifyProduct(Product product, String newProductName, String newStoreName,
                              String newDescriptionOfProduct, int newQuantityAvailable, double newPrice) {
        for (int i =0 ; i < getProductsArrayList().size(); i++) {
            if (getProductsArrayList().get(i).equals(product)) {
                getProductsArrayList().get(i).setProductName(newProductName);
                getProductsArrayList().get(i).setStoreName(newStoreName);
                getProductsArrayList().get(i).setDescriptionOfProduct(newDescriptionOfProduct);
                getProductsArrayList().get(i).setQuantityAvailable(newQuantityAvailable);
                getProductsArrayList().get(i).setPrice(newPrice);
            }
        }
    }

    /// create page to see sales + customer information and revenues from the sale. 
    
    








}

