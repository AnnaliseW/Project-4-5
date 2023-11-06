import java.util.ArrayList;
import java.util.Scanner;

//MOST METHODS AND OBJECT CALLS HAVE NOT BEEN CREATED

public class Market extends Product {
    public static boolean started = true;
    public static boolean marketPlace = true;

    public Market(String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price) {
        super(productName, storeName, descriptionOfProduct, quantityAvailable, price);
    }


    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        User user = null;
        while (started) {

            System.out.println("[1] Log in\n[2] Create a new account");
            int response = s.nextInt();
            s.nextLine();
            if (response == 1) {
                System.out.println("Enter username:");
                String userInput = s.nextLine();
                System.out.println("Enter password");
                String passwordInput = s.nextLine();
                started = false;
            } else if (response == 2) {
                System.out.println("Create an account!\n");
                System.out.println("Enter your username");
                String username = s.nextLine();
                System.out.println("Enter your first and last name");
                String name = s.nextLine();
                System.out.println("Enter your password");
                String password = s.nextLine();
                user = new User(name, password, username);
                started = false;

            } else {
                System.out.println("Error! Please input 1 or 2");
            }

        }
        System.out.println("Would you like to be a seller or buyer? [1] seller, [2] buyer");
        /// figure out how to isolate accounts and add on for accounts (buying + selling items attached to acc)
        int sellerOrBuyer = s.nextInt();
        s.nextLine();

        if (sellerOrBuyer == 1) {
            // Seller seller = new Seller(); ?
            // seller view
            //have to implement many methods

            System.out.println("[1] Add product to sell\n[2] Edit product\n[3] Delete Product\n[4] View Sales For Store");
            int sellerChoice = s.nextInt();
            s.nextLine();
            if (sellerChoice == 1) {
                //String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price
                System.out.println("Enter name of Product");
                String productName = s.nextLine();

                System.out.println("Enter Store Name");
                String storeName = s.nextLine();

                System.out.println("Enter description for Product");
                String description = s.nextLine();

                System.out.println("Enter quantity selling");
                int quantity = s.nextInt();
                s.nextLine();

                System.out.println("Enter price of product");
                double price = s.nextDouble();
                s.nextLine();

                Product newProductAdded = new Product(productName, storeName, description, quantity, price);
                Seller.addProduct(newProductAdded);
                Seller.getItemsSellingBySeller().add(newProductAdded);


            } else if (sellerChoice == 2) {
                // array list of items that are selling of specific user

                for (int i =1; i <= Seller.getItemsSellingBySeller().size(); i++) {
                    System.out.println(i + " " + Seller.getItemsSellingBySeller().get(i).listingPagetoString());
                }
                System.out.println("Choose product index you want to edit");
                int indexOfChange = s.nextInt();
                s.nextLine();

                System.out.println("What is the new name?");
                String newName = s.nextLine();
                System.out.println("What is the new store name?");
                String newStoreName = s.nextLine();
                System.out.println("What is the new description");
                String newDescription = s.nextLine();
                System.out.println("What is the new quantity?");
                int newQuantity = s.nextInt();
                s.nextLine();
                System.out.println("What is the new price?");
                double newPrice = s.nextDouble();
                s.nextLine();
                Seller.modifyProduct(Seller.getItemsSellingBySeller().get(indexOfChange), newName, newStoreName, newDescription, newQuantity, newPrice);

                //still need to code for array list of selling... occurs when added
                /// display items being sold from user and take it
                // implementing and reading from list
            } else if (sellerChoice == 3) {

                for (int i =1; i <= Seller.getItemsSellingBySeller().size(); i++) {
                    System.out.println(i + " " + Seller.getItemsSellingBySeller().get(i).listingPagetoString());
                }
                System.out.println("Choose product index you want to remove");
                int indexOfChange = s.nextInt();
                s.nextLine();
                Seller.removeProduct(Seller.getItemsSellingBySeller().get(indexOfChange));

            } else if (sellerChoice == 4) {
                /// dependent on figuring out isolating accounts for user and seller...
                // statistics when buying
            }


        }



        //account creation and login

        else if (sellerOrBuyer == 2) {
            // buyer view
            while (marketPlace) {
                System.out.println("Welcome to the Marketplace!");
                System.out.println("Available products");
                System.out.println("-------------------");

                if (productsArrayList == null) {
                    System.out.println("No products currently being sold!");
                } else {

                    for (int i = 1; i <= productsArrayList.size(); i++) {
                        System.out.println(i + " " + productsArrayList.get(i).listingPagetoString());
                        System.out.println("-------------------");
                    }


                    System.out.println("Type in number for chosen product insights");
                    int productNumber = s.nextInt();
                    s.nextLine();

                    productsArrayList.get(productNumber - 1).statisticsToString();
                    System.out.println("Would you like to purchase this item? [1] yes, [2] no");
                    int purchaseResponse = s.nextInt();
                    s.nextLine();
                    if (purchaseResponse == 1) {
                        System.out.println("How many items would you like to purchase");
                        int amountPurchasing = s.nextInt();
                        s.nextLine();
                        Customer.purchaseProduct(productsArrayList.get(productNumber - 1), amountPurchasing);

                        // later have to change when implementing shopping cart...


                        //String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price


                    } else if (purchaseResponse == 2) {
                        System.out.println("Feel free to keep looking! Press [1] to exit");
                        int leave = s.nextInt();
                        s.nextLine();
                        if (leave == 1) {
                            return;
                        }
                    }


                }
            }

        }
    }


}
