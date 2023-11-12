import java.util.ArrayList;

public class SalesByStore {
    private String storeName;
    private ArrayList<ItemBought> salesList;

    public SalesByStore(String storeName) {
        this.storeName = storeName;
        this.salesList = new ArrayList<>();
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void addItemBought(ItemBought itemBought) {
        salesList.add(itemBought);
    }

    public ArrayList<ItemBought> getSalesList() {
        return salesList;
    }

    public String toString() {
        return "SalesByStore{" +
                "storeName='" + storeName + '\'' +
                ", salesList=" + salesList +
                '}';
    }

}
