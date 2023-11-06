import java.util.ArrayList;

public class User {
    private String name;
    private String password;
    private String username;

    public Seller seller;

    public Customer customer;

    public ArrayList<Product> itemsBought;



    public User(String name, String password, String username) {
        this.name = name;
        this.password = password;
        this.username = username;

    }

    public User(String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price) {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Seller getSeller() {
        return seller;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
