public class ItemBought {
    private String buyerName;
    private String buyerEmail;
    private String sellerEmail;
    private String storeName;
    private String productName;
    private double productPrice;
    private int quantityBought;

    public ItemBought(String buyerName, String buyerEmail, String sellerEmail, String storeName, String productName, double productPrice, int quantityBought) {
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.sellerEmail = sellerEmail;
        this.storeName = storeName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantityBought = quantityBought;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getQuantityBought() {
        return quantityBought;
    }

    public String toString() {
        return "ItemBought{" +
                "buyerName='" + buyerName + '\'' +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", storeName='" + storeName + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", quantityBought=" + quantityBought +
                '}';
    }

}
