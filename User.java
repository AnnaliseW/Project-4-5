import java.util.ArrayList;

public class User {
    private String name;
    private String password;
    private String username;

    public Seller seller;

    public Customer customer;

      private ArrayList<Product> itemsSoldBySeller = new ArrayList<>();

    private ArrayList<Product> shoppingCart = new ArrayList<>();



  public User(String name, String email, String password, boolean seller,
                ArrayList<Product> itemsSoldBySeller, ArrayList<Product> shoppingCart) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.seller = seller;
        this.itemsSoldBySeller = itemsSoldBySeller;
        this.shoppingCart = shoppingCart;
    }
    // Constructor without itemsSoldBySeller (used for loading users from file)
    public User(String name, String email, String password, boolean seller) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.seller = seller;
        // itemsSoldBySeller is not initialized here; it will be populated when needed
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSeller() {
        return seller;
    }

    public ArrayList<Product> getItemsSoldBySeller() {
        return itemsSoldBySeller;
    }

    public void addItemsSoldBySeller(Product product) {
        itemsSoldBySeller.add(product);
    }


    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }


}
