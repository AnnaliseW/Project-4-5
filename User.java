
public class User {
    private String name;
    private String email;
    private String password;
    private boolean seller;

    public User(String name, String email, String password, boolean seller) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.seller = seller;
    }
    // Constructor without itemsSoldBySeller (used for loading users from file)


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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSeller(boolean seller) {
        this.seller = seller;
    }
}
