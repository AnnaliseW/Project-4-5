import java.util.ArrayList;

public class Statistics {

    public static ArrayList<Product> productsPurchased;
    User user;

    public Statistics(ArrayList<Product> productsPurchased, User user) {
        this.productsPurchased = productsPurchased;
        this.user = user;
    }

    /*
    Sellers can view a dashboard that lists statistics for each of their stores.
    Data will include a list of customers with the number of items that they have purchased and a list of products with the number of sales.
    Sellers can choose to sort the dashboard.
    */
    public String sellerViewStats() {
        String message = "";
        ArrayList<String> Customers1 = new ArrayList<String>();
        ArrayList<Integer> ItemsPurchased1 = new ArrayList<Integer>();

        ArrayList<Product> Products2 = new ArrayList<Product>();
        ArrayList<Integer> Sales2 = new ArrayList<Integer>();

        if (user.isSeller()) {

            //load in their stores
            //loop through stores
            //loop through customers
            //loop through array and increase count for individual customer purchases
            //store info in ArrayList 1

            //loop through stores
            //loop through products
            //loop through array and increase count num sales
            //store info in ArrayList 2

            message += "Customer List:\n";
            for (int i = 0; i < ItemsPurchased1.size(); i++) {
                message += Customers1.get(i) + "purchased " + ItemsPurchased1.get(i) + " items." + "\n";
            }
            message += "\n";
            message += "Products List:\n";
            for (int i = 0; i < Products2.size(); i++) {
                message += Products2.get(i) + "has " + Products2.get(i) + " sales." + "\n";
            }

            //finding a way to either call sort method
            //  or sort inside this and have a parameter for whether they want it sorted or not


        } else {
            message = "Error. You are not a seller. Please log in with a seller account";
        }

        return message;
    }

    public String sellerViewStatsSorted() {
        String message = "";
        if (user.isSeller()) {

        } else {
            message = "Error. You are not a seller. Please log in with a seller account";
        }
        return message;
    }


    /*
    Customers can view a dashboard with store and seller information.
    Data will include a list of stores by number of products sold and a list of stores by the products purchased by that particular customer.
    Customers can choose to sort the dashboard.
     */
    public String customerViewStats() {
        String message = "";
        ArrayList<String> Stores1 = new ArrayList<String>();
        ArrayList<Integer> productsSold1 = new ArrayList<Integer>();

        ArrayList<String> Stores2 = new ArrayList<String>();

        if (!user.isSeller()) {

            //load in stores
            //loop through them and get number of products sold
            //put those into Stores1 and productsSold1

            //loop through purchased itmes
            //if new store, add that store to Store2
            //go back through purchased items and for each store, get count of items purchased and put that in array
            String purchased[][] = new String[Stores2.size()][];

            //loop through stores (i)
            //  int countItems = 0
            //  loop through all items
            //      if from that store, countItems++;
                purchased[i] = new String[countItems];
            //  go back through all items
            //      if from store, add it to purchased[i]

            message += "Store List of products sold:\n";
            for (int i = 0; i < Stores1.size(); i++) {
                message += Stores1.get(i) + "sold " + productsSold1.get(i) + " items." + "\n";
            }
            message += "\n";

            message += "Products List of products purchased:\n";
            for (int i = 0; i < Stores2.size(); i++) {

                message += "Products purchased from " + Stores2.get(i) + ":";
                for (int j = 0; j < purchased[i][j].length(); j++) {
                    message += purchased[i][j] + ",";

                }
                message = message.substring(0, message.length() - 1);
                message += "\n";

            }

            //finding a way to either call sort method
            //  or sort inside this and have a parameter for whether they want it sorted or not

        } else {
            message = "Error. You are not a customer. Please log in with a customer account";
        }

        return message;
    }

    public String customerViewStatsSorted() {
        String message = "";
        if (!user.isSeller()) {

        } else {
            message = "Error. You are not a customer. Please log in with a customer account";
        }
        return message;
    }

}
