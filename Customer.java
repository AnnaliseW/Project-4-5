import java.util.ArrayList;
public class Customer extends User {

    public Customer(String name, String password, String username) {
        super(name, password, username);
    }

    public static ArrayList<Product> searchForProduct(String nameOfProduct, String nameOfStore, String description) {
        ArrayList<Product> searchedProducts = new ArrayList<>();
        Product similarProduct;
        for (int i = 0; i < Product.getProductsArrayList().size(); i++) {
            if (Product.getProductsArrayList().get(i).getProductName().toLowerCase().contains(nameOfProduct.toLowerCase())) {
                similarProduct = Product.getProductsArrayList().get(i);
                searchedProducts.add(similarProduct);
            } else if (Product.getProductsArrayList().get(i).getStoreName().toLowerCase().contains(nameOfStore.toLowerCase())) {
                similarProduct = Product.getProductsArrayList().get(i);
                searchedProducts.add(similarProduct);
            } else if (Product.getProductsArrayList().get(i).getDescriptionOfProduct().toLowerCase().contains(description.toLowerCase())) {
                similarProduct = Product.getProductsArrayList().get(i);
                searchedProducts.add(similarProduct);
            }
        }
        return searchedProducts;
    }
    // implement more so it contains a toString for customer viewing
    // being able to click on the product back in main market

    public static void purchaseProduct(Product product, int quantityPurchased) {
        for (int i = 0; i < Product.getProductsArrayList().size(); i++) {
            if (Product.getProductsArrayList().get(i).equals(product)) {
                Product.getProductsArrayList().get(i).setQuantityAvailable(Product.getQuantityAvailable() - quantityPurchased);
                Product.getProductsArrayList().get(i).setQuantitySold(quantityPurchased);
                Seller.getItemsSold().add(new SoldProduct(product.getProductName(), product.getStoreName(), product.getDescriptionOfProduct()
                , product.getQuantityAvailable(), product.getPrice(), new Customer(name, password, username)));
                // add to sales in seller
            }
        }
    }



}
