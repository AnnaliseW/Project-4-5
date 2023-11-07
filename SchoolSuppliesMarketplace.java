import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SchoolSuppliesMarketplace {

    private static final String USERS_FILE = "users.txt";
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser = null;
    public static ArrayList<Product> productsOnMarket;

    public static ArrayList<Product> getProductsOnMarket() {
        return productsOnMarket;
    }

    public void addProductsOnMarket(Product product) {
        productsOnMarket.add(product);
    }

    public static void setProductsOnMarket(ArrayList<Product> productsOnMarket) {
        SchoolSuppliesMarketplace.productsOnMarket = productsOnMarket;
    }

    public static void main(String[] args) {
        boolean exit = false;

        loadUsersData();

        Scanner scan = new Scanner(System.in);

        // Main program loop
        while (true) {
            if (currentUser == null) {
                //Displays options for users who are not signed in
                System.out.println("Welcome to the School Supplies Marketplace");
                System.out.println("1. Create Account");
                System.out.println("2. Sign In");
                System.out.println("3. Exit");

                // Reads the user's choice
                int choice = scan.nextInt();
                scan.nextLine();

                //Processes the user's choice
                switch (choice) {
                    case 1:
                        createAccount(scan);
                        break;
                    case 2:
                        signIn(scan);
                        break;
                    case 3:
                        saveUsersData(); // Saves user data to the file
                        System.out.println("Goodbye!");
                        System.exit(0); // Exits the program
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                // Displays options for signed-in users
                System.out.println("Welcome, " + currentUser.getName() + "!");
                System.out.println("You are currently signed in as a " + (currentUser.isSeller() ? "Seller" : "Customer"));
                System.out.println("4. Sign Out");

                // Reads the user's choice
                int choice = scan.nextInt();
                scan.nextLine();

                if (choice == 4) {
                    currentUser = null; // Sign the user out
                    System.out.println("You have been signed out.");
                }
            }
        }
    }


    private static void createAccount(Scanner scan) {
        System.out.println("Creating a new account...");
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
        System.out.print("Enter your email: ");
        String email = scan.nextLine();

        // Checks if an account with the provided email already exists
        if (users.containsKey(email)) {
            System.out.println("An account with this email already exists. Please sign in.");
            return;
        }

        System.out.println("Are you a Seller or a Customer?");
        System.out.print("Enter 'Seller' or 'Customer': ");
        String userType = scan.nextLine().toLowerCase();

        // Checks and sets the user type
        if (!userType.equals("seller") && !userType.equals("customer")) {
            System.out.println("Invalid user type. Please enter 'Seller' or 'Customer'.");
            return;
        }

        boolean seller = userType.equals("seller");

        System.out.print("Create a password: ");
        String password = scan.nextLine();

        // Creates an ArrayList to store user-specific data
        ArrayList<Product> itemsSoldBySeller = new ArrayList<>();

        ArrayList<Product> shoppingCart = new ArrayList<>();

        // Creates a new User object with the provided information
        User newUser = new User(name, email, password, seller, itemsSoldBySeller, shoppingCart);
        users.put(email, newUser); // Adds the new user to the users map with email as the key
        currentUser = newUser; // Sets the currentUser to the newly created user
        saveUsersData(); // Saves user data to the file
        System.out.println("Account created successfully!");
    }

    // Signs in a user based on their provided email and password
    private static void signIn(Scanner scan) {
        //boolean statement for loop of marketplace
        boolean marketPlace = true;
        ArrayList<Product> productsOnMarket = new ArrayList<>();

        System.out.println("Signing in...");
        System.out.print("Enter your email: ");
        String email = scan.nextLine();
        System.out.print("Enter your password: ");
        String password = scan.nextLine();

        // Retrieve the User object associated with the provided email
        User user = users.get(email);

        // Checks if the user exists and if the provided password matches
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user; // Sets the current user to the signed-in user
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
            // needs to be fixed: currently causing null pointer
        if (user.isSeller()) {
            while (marketPlace == true) {
                // IMPORTANT: currently not storing products everytime logging in and out
                ///MUST STILL IMPLEMENT
                // exit method for every point of page which will go back to the main page(loops)
                System.out.println("[1] Add product to sell\n[2] Edit product\n[3] Delete Product\n[4] View Sales For Store");
                int sellerChoice = scan.nextInt();
                scan.nextLine();


                //if choosing to add product
                if (sellerChoice == 1) {
                    System.out.println("Enter name of Product");
                    String productName = scan.nextLine();

                    System.out.println("Enter Store Name");
                    String storeName = scan.nextLine();

                    System.out.println("Enter description for Product");
                    String description = scan.nextLine();

                    System.out.println("Enter quantity selling");
                    int quantity = scan.nextInt();
                    scan.nextLine();

                    System.out.println("Enter price of product");
                    double price = scan.nextDouble();
                    scan.nextLine();

                    //adds product to selling array list individual to user

                    Product newProductAdded = new Product(productName, storeName, description, quantity, price);
                    user.addItemsSoldBySeller(newProductAdded);
                    productsOnMarket.add(newProductAdded);


                    // if seller wants to modify the list

                } else if (sellerChoice == 2) {

                    //displays products statistics
                    for (int i = 0; i < user.getItemsSoldBySeller().size(); i++) {
                        System.out.println(i + 1 + " " + user.getItemsSoldBySeller().get(i).listingPagetoString());
                    }
                    System.out.println("Choose product index you want to edit");
                    int indexOfChange = scan.nextInt();
                    scan.nextLine();

                    System.out.println("What is the new name?");
                    String newName = scan.nextLine();
                    System.out.println("What is the new store name?");
                    String newStoreName = scan.nextLine();
                    System.out.println("What is the new description");
                    String newDescription = scan.nextLine();
                    System.out.println("What is the new quantity?");
                    int newQuantity = scan.nextInt();
                    scan.nextLine();
                    System.out.println("What is the new price?");
                    double newPrice = scan.nextDouble();
                    scan.nextLine();

                    //takes variables in and modifies the product in seller list

                    Product modifyingSeller = user.getItemsSoldBySeller().get(indexOfChange - 1);
                    modifyingSeller.setProductName(newName);
                    modifyingSeller.setStoreName(newStoreName);
                    modifyingSeller.setDescriptionOfProduct(newDescription);
                    modifyingSeller.setQuantityAvailable(newQuantity);
                    modifyingSeller.setPrice(newPrice);

                    // modifies the product on the product array list listed on market

                    for (int i = 0; i < productsOnMarket.size(); i++) {
                        if (productsOnMarket.get(i) == modifyingSeller) {
                            productsOnMarket.get(i).setProductName(newName);
                            productsOnMarket.get(i).setStoreName(newStoreName);
                            productsOnMarket.get(i).setDescriptionOfProduct(newDescription);
                            productsOnMarket.get(i).setQuantityAvailable(newQuantity);
                            productsOnMarket.get(i).setPrice(newPrice);
                        }
                    }


                    //if user wants to delete product
                } else if (sellerChoice == 3) {
                    //displays products statistics
                    for (int i = 0; i < user.getItemsSoldBySeller().size(); i++) {
                        System.out.println(i + 1 + " " + user.getItemsSoldBySeller().get(i).listingPagetoString());
                    }
                    System.out.println("Choose product index you want to remove");
                    int indexOfDeletion = scan.nextInt();
                    scan.nextLine();

                    //removes the product from the products market array list
                    for (int i = 0; i < productsOnMarket.size(); i++) {
                        if (productsOnMarket.get(i) == user.getItemsSoldBySeller().get(indexOfDeletion - 1)) {
                            productsOnMarket.remove(i);
                        }
                    }
                    //removes the product from the seller array list
                    user.getItemsSoldBySeller().remove(indexOfDeletion - 1);


                } else if (sellerChoice == 4) {

                    // add code of SALES + revenue calculations

                } // still need to add if it's not 1,2,3,4

            }

            // output for if user is customer view
        } else {
            ///MUST STILL IMPLEMENT
            // exit method for every point of page which will go back to the main page(loops)

            while (marketPlace == true) {

                System.out.println("Welcome to the Marketplace!");
                System.out.println("Available products");
                System.out.println("-------------------");

                if (productsOnMarket == null) {
                    System.out.println("No products are currently being sold on the market!");
                    marketPlace = false;
                } else {
                    //printing out the products insights
                    for (int i = 0; i < productsOnMarket.size(); i++) {
                        System.out.println(i + 1 + " " + productsOnMarket.get(i).listingPagetoString());
                        System.out.println("-------------------");
                    }


                    System.out.println("Type in number for chosen product insights\nEnter 0 to search for products");
                    int productNumber = scan.nextInt();
                    scan.nextLine();

                    // accessing search
                    if (productNumber == 0) {
                        // checking for similar search in every product in array
                        for (int i = 0; i < productsOnMarket.size(); i++) {
                            Methods method = new Methods();
                            String productName = productsOnMarket.get(i).getProductName();
                            String storeName = productsOnMarket.get(i).getStoreName();
                            String description = productsOnMarket.get(i).getDescriptionOfProduct();
                            method.searchForProduct(productName, storeName, description);
                        }
                        return;
                    } else { /// still need to take into acc if number inputted does not exist

                        //print out statistic for product chosen
                        productsOnMarket.get(productNumber - 1).statisticsToString();

                        System.out.println("Would you like to purchase this item? [1] yes, [2] no");
                        int purchaseResponse = scan.nextInt();
                        scan.nextLine();

                        if (purchaseResponse == 1) {
                            System.out.println("How many items would you like to purchase");
                            int amountPurchasing = scan.nextInt();
                            scan.nextLine();
                            Methods method = new Methods();
                            // calls method to purchase
                            // in method... sets quantity sold and sets quantity available
                            //still has to add other statistics for following sales and receipts for customer
                            method.purchaseProduct(productsOnMarket.get(productNumber - 1), amountPurchasing);


                            //TODO: need to modify productsOnMarket

                            // later have to change when implementing shopping cart...

                            //String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price
                        return;
                        } else if (purchaseResponse == 2) {
                            System.out.println("Feel free to keep looking! Press [1] to exit");
                            int leave = scan.nextInt();
                            scan.nextLine();
                            if (leave == 1) {
                                return;
                            }
                            // need to exit out of method
                        } // still need to take into acc if not 1 or 2


                    }

                }


            }
        }

    }

    // Loads user data from the specified file into the 'users' map
    private static void loadUsersData() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String email = parts[1];
                String password = parts[2];
                boolean seller = Boolean.parseBoolean(parts[3]);

                // Creates a User object with the loaded data and adds it to the 'users' map
                User user = new User(name, email, password, seller);
                users.put(email, user);
            }
        } catch (IOException e) {

        }
    }
// needs to load array lists into text files




    //Saves user data from the 'users' map into the specified file
    private static void saveUsersData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                pw.println(user.getName() + "," + user.getEmail() + "," + user.getPassword() + "," + user.isSeller());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
