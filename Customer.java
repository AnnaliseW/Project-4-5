import java.util.ArrayList;
public class Customer extends Product {

    public ArrayList<Product> searchForProduct(String nameOfProduct, String nameOfStore, String description) {
        ArrayList<Product> searchedProducts = new ArrayList<>();
        Product similarProduct = new Product();
        for (int i = 0; i < getProductsArrayList().size(); i++) {
            if (getProductsArrayList().get(i).getProductName().toLowerCase().contains(nameOfProduct.toLowerCase())) {
                similarProduct = getProductsArrayList().get(i);
                searchedProducts.add(similarProduct);
            } else if (getProductsArrayList().get(i).getStoreName().toLowerCase().contains(nameOfStore.toLowerCase())) {
                similarProduct = getProductsArrayList().get(i);
                searchedProducts.add(similarProduct);
            } else if (getProductsArrayList().get(i).getDescriptionOfProduct().toLowerCase().contains(description.toLowerCase())) {
                similarProduct = getProductsArrayList().get(i);
                searchedProducts.add(similarProduct);
            }
        }
        return searchedProducts;
    }

    public void purchaseProduct(Product product, int quantityPurchased) {
        for (int i = 0; i < getProductsArrayList().size(); i++) {
            if (getProductsArrayList().get(i).equals(product)) {
                getProductsArrayList().get(i).setQuantityAvailable(getQuantityAvailable() - quantityPurchased);

            }
        }
    }



}
