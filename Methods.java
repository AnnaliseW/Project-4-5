import java.util.ArrayList;

public class Methods {


    public ArrayList<Product> productsOnMarket = new ArrayList<>();

    public ArrayList<Product> getProductsOnMarket() {
        return productsOnMarket;
    }



    public void setProductsOnMarket(ArrayList<Product> productsOnMarket) {
        this.productsOnMarket = productsOnMarket;
    }



    // this method searches for a product by what they input

    public ArrayList<Product> searchForProduct(String nameOfProduct, String nameOfStore, String description) {
        ArrayList<Product> searchedProducts = new ArrayList<>();
        Product similarProduct;
        // checking if it matches anything from the product name
        for (int i = 0; i < productsOnMarket.size(); i++) {
            if (productsOnMarket.get(i).getProductName().toLowerCase().contains(nameOfProduct.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the store name
            } else if (productsOnMarket.get(i).getStoreName().toLowerCase().contains(nameOfStore.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the description
            } else if (productsOnMarket.get(i).getDescriptionOfProduct().toLowerCase().contains(description.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);
            }
        }
        return searchedProducts;
    }


    public void purchaseProduct(Product product, int quantityPurchased) {
        for (int i = 0; i < productsOnMarket.size(); i++) {
            if (productsOnMarket.get(i).equals(product)) {
                productsOnMarket.get(i).setQuantityAvailable(productsOnMarket.get(i).getQuantityAvailable() - quantityPurchased);
                productsOnMarket.get(i).setQuantitySold(quantityPurchased);
            }
        }
    }
}
