public class SoldProduct extends Product{
    private static int quantityPurchased;

    public SoldProduct(String productName, String storeName, String descriptionOfProduct, int quantityAvailable,
                       double price, int quantityPurchased){
        super(productName, storeName, descriptionOfProduct, quantityAvailable, price);
        this.quantityPurchased = quantityPurchased;
    }

    public SoldProduct(Product product, int quantityPurchased){
        super(product.getProductName(), product.getStoreName(), product.getDescriptionOfProduct(),
                product.getQuantityAvailable(), product.getPrice());
        this.quantityPurchased = quantityPurchased;
    }

    public static int getQuantityPurchased(){
        return quantityPurchased;
    }
}
