import java.util.ArrayList;

public class Methods {



    public static ArrayList<Product> productsOnMarket;

    public static ArrayList<Product> getProductsOnMarket() {
        return productsOnMarket;
    }

    public static void addProductsOnMarket(Product product) {
        productsOnMarket.add(product);
    }

    public static void setProductsOnMarket(ArrayList<Product> productsOnMarket) {
        Methods.productsOnMarket = productsOnMarket;
    }

    

    // this method searches for a product by what they input

    public ArrayList<Product> searchForProduct(String nameOfProduct, String nameOfStore, String description) {
        ArrayList<Product> searchedProducts = new ArrayList<>();
        Product similarProduct;
        // checking if it matches anything from the product name
        for (int i = 0; i < SchoolSuppliesMarketplace.getProductsOnMarket().size(); i++) {
            if (SchoolSuppliesMarketplace.getProductsOnMarket().get(i).getProductName().toLowerCase().contains(nameOfProduct.toLowerCase())) {
                similarProduct = SchoolSuppliesMarketplace.getProductsOnMarket().get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the store name
            } else if (SchoolSuppliesMarketplace.getProductsOnMarket().get(i).getStoreName().toLowerCase().contains(nameOfStore.toLowerCase())) {
                similarProduct = SchoolSuppliesMarketplace.getProductsOnMarket().get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the description
            } else if (SchoolSuppliesMarketplace.getProductsOnMarket().get(i).getDescriptionOfProduct().toLowerCase().contains(description.toLowerCase())) {
                similarProduct = SchoolSuppliesMarketplace.getProductsOnMarket().get(i);
                searchedProducts.add(similarProduct);
            }
        }
        return searchedProducts;
    }


    public void purchaseProduct(Product product, int quantityPurchased) {
        for (int i = 0; i < SchoolSuppliesMarketplace.getProductsOnMarket().size(); i++) {
            if (SchoolSuppliesMarketplace.getProductsOnMarket().get(i).equals(product)) {
                SchoolSuppliesMarketplace.getProductsOnMarket().get(i).setQuantityAvailable(Product.getQuantityAvailable() - quantityPurchased);
                SchoolSuppliesMarketplace.getProductsOnMarket().get(i).setQuantitySold(quantityPurchased);

            }
        }
    }
}
