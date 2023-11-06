public class SoldProduct extends Product{
    private static Customer purchasedBy;

    public SoldProduct(String productName, String storeName, String descriptionOfProduct, int quantityAvailable,
                       double price, Customer purchasedBy){
        super(productName, storeName, descriptionOfProduct, quantityAvailable, price);
        this.purchasedBy = purchasedBy;
    }

    public static Customer getPurchasedBy(){
        return purchasedBy;
    }
}
