public class ShoppingCartProduct {

    public String productName;
    public String storeName;
    public String descriptionOfProduct;
    public int quantityAvailable;
    public double price;
    public int quantityBuying;


    public ShoppingCartProduct(String productName, String storeName, String
            descriptionOfProduct, int quantityAvailable, double price, int quantityBuying) {
        this.productName = productName;
        this.storeName = storeName;
        this.descriptionOfProduct = descriptionOfProduct;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
        this.quantityBuying = quantityBuying;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescriptionOfProduct() {
        return descriptionOfProduct;
    }

    public void setDescriptionOfProduct(String descriptionOfProduct) {
        this.descriptionOfProduct = descriptionOfProduct;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String shoppingCartStatisticsToString() {
        return String.format("Product Statistics:\nProduct Name: " +
                        "%s\nStore Name: %s\nDescription: %s\nQuantity Available:" +
                        " %d\nPrice: %.2f\nQuantity Buying: %d", productName
                , storeName, descriptionOfProduct, quantityAvailable, price, quantityBuying);
    }

    public String listingPagetoString() {
        return String.format("Product Statistics:\nProduct Name: %s\nStore Name:" +
                " %s\nPrice: %.2f", getProductName(), getStoreName(), getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ShoppingCartProduct) {
            ShoppingCartProduct objectO = (ShoppingCartProduct) o;
            return objectO.getProductName().equals(this.productName) &&
                    objectO.getStoreName().equals(this.storeName)
                    && objectO.getDescriptionOfProduct().equals(this.descriptionOfProduct)
                    && objectO.getQuantityAvailable() == this.quantityAvailable
                    && objectO.getPrice() == (this.price)
                    && objectO.getQuantityBuying() == (this.quantityBuying);

        } else {
            return false;
        }
    }

    public int getQuantityBuying() {
        return quantityBuying;
    }

    public void setQuantityBuying(int quantityBuying) {
        this.quantityBuying = quantityBuying;
    }
}