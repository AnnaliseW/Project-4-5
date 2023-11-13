import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class Market {

    //FOR LOGOUT ** must ensure array lists are being printed out
    // CHECK FOR INPUT ERRORS
    // check for open buffers and print writers?
    // MUST IMPLEMENT BEING ABLE TO EXIT WHENEVER
    //MUST CHECK: products are NOT BEING SAVED CURRENTLY when user logs out



    public static void main(String[] args) {


        //used for separating for products
        String[] arrayListOfProducts;

        try {
            //readding products from file into the product array list
            BufferedReader bfr = new BufferedReader(new FileReader("productArrayList.txt"));
            String line = "";

            while ((line = bfr.readLine()) != null) {
                //splitting by @@ for each product
                arrayListOfProducts = line.split("@@");
                for (int i = 0; i < arrayListOfProducts.length; i++) {
                    String[] separateParameters = arrayListOfProducts[i].split(",");
                    String productName = separateParameters[0];
                    String storeName = separateParameters[1];
                    String description = separateParameters[2];
                    int quantityAvailable = Integer.parseInt(separateParameters[3]);
                    double price = Double.parseDouble(separateParameters[4]);
                    //recreate product that is currently selling
                    Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);
                    Methods.productsOnMarket.add(eachProduct);
                }
            }
            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        User userAccount = null;

        File dataFile = new File("data.txt");
        Scanner s = new Scanner(System.in);

        // boolean for mainPage (log in screen)
        boolean mainPage = true;
        //checking if acc exists
        boolean alreadyAnAccount = false;

        //checking if user exists when SIGNING IN
        boolean signInComplete = false;

        boolean seller = false;
        while (mainPage) {
            //inputting welcome log in
            System.out.println("Welcome to the School Supplies Marketplace");
            System.out.println("1. Create Account");
            System.out.println("2. Sign In");
            System.out.println("3. Exit");
            int selectionToSignIn = 0;
            try {
                selectionToSignIn = s.nextInt();
                s.nextLine();
            } catch (InputMismatchException e) {
                s.nextLine();
            }

            ///creating account 1 option
            ArrayList<Product> itemsSoldBySeller = null;
            if (selectionToSignIn == 1) {
                System.out.println("Creating a new account...\n");
                System.out.print("Enter your name: ");
                String name = s.nextLine();
                //NOTE: email and password WILL be CASE SENSITIVE

                System.out.print("Enter your email: ");
                String email = s.nextLine();

                System.out.println("Enter your password: ");
                String password = s.nextLine();
                //looping for 1 or 2 input
                boolean correctInputSeller = false;
                //determining if buyer or seller
                while (!correctInputSeller) {
                    System.out.println("Would you like to be a [1] seller or [2] buyer?");
                    int sellerOrBuyer = 0;
                    try {
                        sellerOrBuyer = s.nextInt();
                        s.nextLine();
                    } catch (InputMismatchException e) {
                        s.nextLine();
                    }

                    if (sellerOrBuyer == 1) {
                        seller = true;
                        correctInputSeller = true;
                    } else if (sellerOrBuyer == 2) {
                        seller = false;
                        correctInputSeller = true;

                    } else {
                        System.out.println("Please input 1 or 2!");
                    }
                }


                // if fileEmpty is true no users so don't need to check


                // checking if no users currently created
                boolean fileIsEmpty = false;
                try (BufferedReader bfr = new BufferedReader(new FileReader(dataFile))) {
                    if (bfr.readLine() == null) {
                        fileIsEmpty = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!fileIsEmpty) {
                    try {
                        BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
                        String line = "";
                        ArrayList<String> allUserData = new ArrayList<>();


                        while ((line = bfr.readLine()) != null) {
                            allUserData.add(line);
                        }

                        alreadyAnAccount = false;
                        for (int i = 0; i < allUserData.size(); i++) {
                            String[] oneUserData = allUserData.get(i).split(",");
                            if (oneUserData[1].equals(email)) {
                                alreadyAnAccount = true;
                                break;
                            }
                        }

                        bfr.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                if (alreadyAnAccount) {
                    System.out.println("There is already an account with this email! Please log in!");
                }
                if (!alreadyAnAccount) {
                    //make user
                    userAccount = new User(name, email, password, seller);

                    System.out.println("Welcome " + userAccount.getName() + "!");
                    System.out.println("Please sign in!");
                    //print data to file
                    try {
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("data.txt", true)));
                        pw.println(userAccount.getName() + "," + userAccount.getEmail() + "," +
                                userAccount.getPassword() + "," + userAccount.isSeller() + ";");
                        pw.flush();
                        pw.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else if (selectionToSignIn == 2) {
                System.out.println("Please enter your email!");
                String email = s.nextLine();
                System.out.println("Please enter your password!");
                String password = s.nextLine();

                //testing if user exists
                try {
                    BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                    String line = "";
                    ArrayList<String> allUserData = new ArrayList<>();


                    while ((line = bfr.readLine()) != null) {
                        allUserData.add(line);
                    }

                    for (int i = 0; i < allUserData.size(); i++) {
                        String[] oneUserData = allUserData.get(i).split(",");
                        //checking if user and password is same
                        if (oneUserData[1].equals(email) && oneUserData[2].equals(password)) {
                            //format ji,jo1234,1234,false;

                            //parse boolean for creating the user for sign in
                            boolean sellerBoolean = false;
                            if (oneUserData[3].startsWith("true")) {
                                sellerBoolean = true;
                            } else if (oneUserData[3].startsWith("false")) {
                                sellerBoolean = false;
                            }
                            userAccount = new User(oneUserData[0], oneUserData[1], oneUserData[2], sellerBoolean);
                            signInComplete = true;
                            break;
                        } else {
                            signInComplete = false;
                        }
                    }

                    bfr.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (signInComplete) {

                    //in the market as email and password was found the same
                    boolean sellerView = false;


                    //// entire market

                    try {
                        BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                        String line = "";
                        ArrayList<String> allUserData = new ArrayList<>();


                        while ((line = bfr.readLine()) != null) {
                            allUserData.add(line);
                        }

                        for (int i = 0; i < allUserData.size(); i++) {
                            String[] oneUserData = allUserData.get(i).split(",");
                            //checking if user is seller
                            if (oneUserData[1].equals(email) && oneUserData[3].startsWith("true")) {
                                //is seller
                                sellerView = true;
                                break;
                            } else {
                                // is customer
                                sellerView = false;
                            }
                        }
                        bfr.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (sellerView) {

                        //START OF ADDED CODE
                        //Reads salesbystore
                        //Creates an arraylist of SalesByStore objects that are under the logged in email

                        ArrayList<SalesByStore> salesByStoresList = new ArrayList<>();

                        try (BufferedReader bfr = new BufferedReader(new FileReader("salesbystore.txt"))) {
                            String line;

                            while ((line = bfr.readLine()) != null) {
                                String[] parts = line.substring(line.indexOf('[') + 1, line.length() - 1).split(", ");
                                String buyerName = parts[0].split("=")[1];
                                String buyerEmail = parts[1].split("=")[1];
                                String storeName = parts[2].split("=")[1];
                                String productName = parts[3].split("=")[1];
                                String sellerEmail = parts[4].split("=")[1];
                                double productPrice = Double.parseDouble(parts[5].split("=")[1]);
                                int quantityBought = Integer.parseInt(parts[6].split("=")[1]);

                                if (sellerEmail.equals(userAccount.getEmail())) {
                                    boolean storeFound = false;
                                    for (SalesByStore salesByStore : salesByStoresList) {
                                        if (salesByStore.getStoreName().equals(storeName)) {
                                            salesByStore.addItemBought(new ItemBought(buyerName, buyerEmail, sellerEmail, storeName, productName, productPrice, quantityBought));
                                            storeFound = true;
                                            break;
                                        }
                                    }
                                    if (!storeFound) {
                                        SalesByStore newSalesByStore = new SalesByStore(storeName);
                                        newSalesByStore.addItemBought(new ItemBought(buyerName, buyerEmail, sellerEmail, storeName, productName, productPrice, quantityBought));
                                        salesByStoresList.add(newSalesByStore);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //END OF ADDED CODE


                        itemsSoldBySeller = new ArrayList<>();

                        //seller view for seller

                        // choosing options 1 -4

                        //boolean to check loop if not choosing 1-4
                        boolean sellerChooseCorrectInput = false;
                        //array list of PRODUCTS BEING SOLD BY SELLER


                        ///reproducing the array list of sales

                        //boolean to check if user previously has products stored in sales
                        boolean emptySalesArrayList = false;
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                            String line = "";
                            ArrayList<String> allUserData = new ArrayList<>();


                            while ((line = bfr.readLine()) != null) {
                                allUserData.add(line);
                            }

                            itemsSoldBySeller.clear();

                            for (int i = 0; i < allUserData.size(); i++) {
                                String[] oneUserData = allUserData.get(i).split(",");
                                //checking for email
                                if (oneUserData[1].equals(email)) {
                                    //splitting sales array
                                    String[] separatingForSalesArray = allUserData.get(i).split(";");
                                    if (separatingForSalesArray.length == 1) {
                                        emptySalesArrayList = true;
                                        break;

                                        // if we need to populate the array list with previous sales data
                                    } else {

                                        if (!separatingForSalesArray[1].contains("@@")) {
                                            //only one product in array

                                            String[] separateParameters = separatingForSalesArray[1].split(",");
                                            //order of text file: productName, storeName, description, quantityAvailable, price
                                            String productName = separateParameters[0];
                                            String storeName = separateParameters[1];
                                            String description = separateParameters[2];
                                            int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                            double price = Double.parseDouble(separateParameters[4]);
                                            //recreate product that is currently selling
                                            Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);

                                            //adding to the items that seller currently has from data file
                                            itemsSoldBySeller.add(eachProduct);


                                        } else {
                                            //splitting by PRODUCT
                                            String[] arrayListSalesProducts = separatingForSalesArray[1].split("@@");
                                            // separating the string text to get parameters for each product
                                            for (int j = 0; j < arrayListSalesProducts.length; j++) {
                                                String[] separateParameters = arrayListSalesProducts[j].split(",");
                                                //order of text file: productName, storeName, description, quantityAvailable, price
                                                String productName = separateParameters[0];
                                                String storeName = separateParameters[1];
                                                String description = separateParameters[2];
                                                int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                                double price = Double.parseDouble(separateParameters[4]);
                                                //recreate product that is currently selling
                                                Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);

                                                //adding to the items that seller currently has from data file
                                                itemsSoldBySeller.add(eachProduct);
                                            }
                                        }
                                    }
                                }
                            }


                            bfr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        while (!sellerChooseCorrectInput) {


                            System.out.println("[1] Add product to sell\n[2] Edit product\n[3] Delete Product\n[4] Choose to import or export products file\n[5] View sales by store\n[6] Exit");
                            int sellerChoice = 0;
                            try {
                                sellerChoice = s.nextInt();
                                s.nextLine();
                                //check for not integer
                            } catch (InputMismatchException e) {
                                s.nextLine();
                            }


                            //if choosing to add product
                            if (sellerChoice == 1) {
                                //boolean for loop for question 2
                                boolean askForStoreName = false;


                                System.out.println("Enter name of Product");
                                String productName = s.nextLine();
                                String storeName = null;
                                while (!askForStoreName) {
                                    System.out.println("Enter Store Name");
                                    storeName = s.nextLine();
                                    //checking to see if store name already exists
                                    //reads data file for every store name with user

                                    boolean existingStoreName = false;

                                    try {
                                        BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                                        String line = "";
                                        ArrayList<String> allUserData = new ArrayList<>();

                                        while ((line = bfr.readLine()) != null) {
                                            allUserData.add(line);
                                        }

                                        for (int i = 0; i < allUserData.size(); i++) {
                                            // checking to see if they are sellers
                                            String[] checkIfSeller = allUserData.get(i).split(",");
                                            // seller identified
                                            if (checkIfSeller[3].startsWith("true")) {
                                                String[] oneUserDataEachProduct = allUserData.get(i).split(";");

                                                if (oneUserDataEachProduct.length == 1) {
                                                    String[] eachFieldForProduct = oneUserDataEachProduct[0].split(",");
                                                    // checking if store name is the same and email is the same
                                                    if (storeName.equals(eachFieldForProduct[1]) && checkIfSeller[1].equals(userAccount.getEmail())) {
                                                        existingStoreName = true;
                                                        break;
                                                    }
                                                } else {
                                                    for (int j = 0; j < oneUserDataEachProduct.length; j++) {
                                                        // separating the individual products
                                                        String[] eachProduct = oneUserDataEachProduct[j].split("@@");
                                                        for (int k = 0; k < eachProduct.length; k++) {
                                                            String[] eachFieldForProduct = eachProduct[k].split(",");
                                                            // checking if store name is the same and email is the same
                                                            if (eachFieldForProduct[1].equals(storeName) && oneUserDataEachProduct[1].equals(userAccount.getEmail())) {
                                                                existingStoreName = true;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        bfr.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if (existingStoreName) {
                                        System.out.println("Store name already exists! Please input a different store name\n");
                                    } else {
                                        askForStoreName = true;
                                    }
                                }
                                System.out.println("Enter description for Product");
                                String description = s.nextLine();
                                
                                
                                int quantity = 0;
                                boolean invalidQuantity = false;
                                while(!invalidQuantity) {
                                    try {
                                        System.out.println("Enter quantity selling");
                                        quantity = s.nextInt();
                                        s.nextLine();
                                        invalidQuantity = true;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid Error");
                                        s.nextLine();
                                    }
                                }

                                double price = 0;
                                boolean invalidPrice = false;
                                while (!invalidPrice) {
                                    System.out.println("Enter price of product");
                                    try {
                                        price = s.nextDouble();
                                        s.nextLine();
                                        invalidPrice = true;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid Error");
                                        s.nextLine();
                                    }
                                }

                               
                                Product newProductAdded = new Product(productName, storeName, description, quantity, price);
                                itemsSoldBySeller.add(newProductAdded);

                                //pull product on market array

                                //adds to product market array list
                                Methods.productsOnMarket.add(newProductAdded);


                                //exit out of while loop
                                System.out.println(newProductAdded.getProductName() + " added to the market!\n");
                            } else if (sellerChoice == 2) {
                                boolean askForStoreName = false;


                                if (itemsSoldBySeller.isEmpty()) {
                                    System.out.println("There are no products for you to edit!\n\n");

                                } else {

                                    //prints out every product they are selling from array sale list
                                    for (int i = 0; i < itemsSoldBySeller.size(); i++) {
                                        System.out.println(("[" + (i + 1) + "]") + " " + itemsSoldBySeller.get(i).statisticsToString() + "\n");
                                    }
                                    int indexOfChange = 0;
                                    ///check if valid input
                                    boolean invalidInput = false;

                                    do {
                                        System.out.println("Choose product index you want to edit\n[-1] to exit");
                                        try {
                                            indexOfChange = s.nextInt();
                                            s.nextLine();
                                            if (indexOfChange == -1) {
                                                invalidInput = true;
                                                Methods method = new Methods();
                                                method.saveArrayListToFile(itemsSoldBySeller, userAccount);
                                                method.saveProductArrayList(Methods.productsOnMarket);
                                            }
                                            //adding check if index is valid
                                            //adding -1 to exit
                                            else if (indexOfChange > itemsSoldBySeller.size() || indexOfChange < 0) {
                                                System.out.println("Invalid input, try again\n");
                                            } else {
                                                invalidInput = true;
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please input integer\n");
                                            s.nextLine();
                                        }


                                    } while (!invalidInput);

                                    if (indexOfChange == -1) {
                                        Methods method = new Methods();
                                        method.saveArrayListToFile(itemsSoldBySeller, userAccount);
                                        method.saveProductArrayList(Methods.productsOnMarket);
                                    } else {
                                       String userEmail = null;
                                        //finding email associated with product
                                        try {
                                            BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                                            String line = "";
                                            ArrayList<String> allUserData = new ArrayList<>();


                                            while ((line = bfr.readLine()) != null) {
                                                allUserData.add(line);
                                            }
                                            Product modifyingSeller = itemsSoldBySeller.get(indexOfChange - 1);
                                            String dataTextProductFormat = modifyingSeller.getProductName() + "," + modifyingSeller.getStoreName() + "," 
                                                    + modifyingSeller.getDescriptionOfProduct() + "," + modifyingSeller.getQuantityAvailable() + "," + modifyingSeller.getPrice();
                                            for (int i = 0; i < allUserData.size(); i++) {
                                                //check if the product is in line 
                                                if (allUserData.get(i).contains(dataTextProductFormat)) {
                                                    String[] userEmailFind = allUserData.get(i).split(",");
                                                    userEmail = userEmailFind[1];
                                                }
                                            }
                                            bfr.close();
                                            
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        
                                        

                                        System.out.println("What is the new name?");
                                        String newName = s.nextLine();

                                        String newStoreName = null;
                                        while (!askForStoreName) {
                                            System.out.println("What is the new store Name");
                                            newStoreName = s.nextLine();
                                            //checking to see if store name already exists
                                            //reads data file for every store name with user

                                            boolean existingStoreName = false;

                                            try {
                                                BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                                                String line = "";
                                                ArrayList<String> allUserData = new ArrayList<>();

                                                while ((line = bfr.readLine()) != null) {
                                                    allUserData.add(line);
                                                }

                                                for (int i = 0; i < allUserData.size(); i++) {
                                                    // checking to see if they are sellers
                                                    String[] checkIfSeller = allUserData.get(i).split(",");
                                                    // seller identified
                                                    if (checkIfSeller[3].startsWith("true")) {
                                                        String[] oneUserDataEachProduct = allUserData.get(i).split(";");

                                                        if (oneUserDataEachProduct.length == 1) {
                                                            String[] eachFieldForProduct = oneUserDataEachProduct[0].split(",");
                                                            // checking if store name is the same and email is the same
                                                            if (newStoreName.equals(eachFieldForProduct[1]) && checkIfSeller[1].equals(userAccount.getEmail())) {
                                                                existingStoreName = true;
                                                                break;
                                                            }
                                                        } else {
                                                            for (int j = 0; j < oneUserDataEachProduct.length; j++) {
                                                                // separating the individual products
                                                                String[] eachProduct = oneUserDataEachProduct[j].split("@@");
                                                                for (int k = 0; k < eachProduct.length; k++) {
                                                                    String[] eachFieldForProduct = eachProduct[k].split(",");
                                                                    // checking if store name is the same and email is the same
                                                                    if (eachFieldForProduct[1].equals(newStoreName) && oneUserDataEachProduct[1].equals(userAccount.getEmail())) {
                                                                        existingStoreName = true;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                bfr.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            if (existingStoreName) {
                                                System.out.println("Store name already exists! Please input a different store name\n");
                                            } else {
                                                askForStoreName = true;
                                            }
                                        }
                                        //end of check if store name exists for new updated store name


                                        System.out.println("What is the new description");
                                        String newDescription = s.nextLine();



                                        int newQuantity = 0;
                                        boolean invalidQuantity = false;
                                        while(!invalidQuantity) {
                                            try {
                                                System.out.println("What is the new quantity");
                                                newQuantity = s.nextInt();
                                                s.nextLine();
                                                invalidQuantity = true;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Invalid Error");
                                                s.nextLine();
                                            }
                                        }
                                        
                                        double newPrice = 0;
                                        ///if valid
                                        boolean invalidPrice = false;
                                        while (!invalidPrice) {
                                            System.out.println("What is the new price?");
                                            try {
                                                newPrice = s.nextDouble();
                                                s.nextLine();
                                                invalidPrice = true;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Invalid Error");
                                                s.nextLine();
                                            }
                                        }

                                        //takes variables in and modifies the product in seller list

                                        Product modifyingSeller = itemsSoldBySeller.get(indexOfChange - 1);
                                        modifyingSeller.setProductName(newName);
                                        modifyingSeller.setStoreName(newStoreName);
                                        modifyingSeller.setDescriptionOfProduct(newDescription);
                                        modifyingSeller.setQuantityAvailable(newQuantity);
                                        modifyingSeller.setPrice(newPrice);

                                        System.out.println(modifyingSeller.getProductName() + " updated in the market!\n");

                                        //modifies product in product market array list
                                        Methods method = new Methods();
                                        for (int i = 0; i < Methods.getProductsOnMarket().size(); i++) {
                                            if (Methods.getProductsOnMarket().get(i).equals(modifyingSeller)) {
                                                Methods.getProductsOnMarket().get(i).setProductName(newName);
                                                Methods.getProductsOnMarket().get(i).setStoreName(newStoreName);
                                                Methods.getProductsOnMarket().get(i).setDescriptionOfProduct(newDescription);
                                                Methods.getProductsOnMarket().get(i).setQuantityAvailable(newQuantity);
                                                Methods.getProductsOnMarket().get(i).setPrice(newPrice);
                                            }
                                        }
                                    }

                                    //exit out of while loop

                                }
                                //to delete an item seller is listing\
                            } else if (sellerChoice == 3) {

                                //lists all products selling
                                for (int i = 0; i < itemsSoldBySeller.size(); i++) {
                                    System.out.println(("[" + (i + 1) + "]") + " " + itemsSoldBySeller.get(i).listingPagetoString() + "\n");
                                }

                                boolean invalidInput = false;
                                int indexOfDeletion = 0;

                                do {
                                    System.out.println("Choose product index you want to remove\n[-1] to exit");

                                    try {
                                        indexOfDeletion = s.nextInt();
                                        s.nextLine();
                                        //if want to exit
                                        if (indexOfDeletion == -1) {
                                            Methods method = new Methods();
                                            method.saveArrayListToFile(itemsSoldBySeller, userAccount);
                                            method.saveProductArrayList(Methods.productsOnMarket);
                                            invalidInput = true;
                                        }
                                        //adding check if index is valid
                                        //adding -1 to exit
                                        else if (indexOfDeletion > itemsSoldBySeller.size() || indexOfDeletion < 0) {
                                            System.out.println("Invalid input, try again\n");
                                        } else {
                                            invalidInput = true;
                                        }
                                        // if not integer
                                    } catch (InputMismatchException e) {
                                        System.out.println("Please input integer\n");
                                        s.nextLine();
                                    }


                                } while (!invalidInput);

                                if (indexOfDeletion == -1) {
                                    Methods method = new Methods();
                                    method.saveArrayListToFile(itemsSoldBySeller, userAccount);
                                    method.saveProductArrayList(Methods.productsOnMarket);
                                    //logs out
                                } else {
                                    //deletes product
                                    Methods method = new Methods();

                                    System.out.println(itemsSoldBySeller.get(indexOfDeletion - 1).getProductName() + " removed from market!\n");
                                    //removing item fromm product market array list
                                    for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                        if (Methods.productsOnMarket.get(i) == itemsSoldBySeller.get(indexOfDeletion - 1)) {
                                            Methods.productsOnMarket.remove(i);
                                            method.saveArrayListToFile(itemsSoldBySeller, userAccount);
                                            method.saveProductArrayList(Methods.productsOnMarket);

                                        }
                                    }
                                    //removes from sales list
                                    itemsSoldBySeller.remove(indexOfDeletion - 1);



                                    //exit out of while loop
                                }


                            } else if (sellerChoice == 5) {
                                if (salesByStoresList.isEmpty()) {
                                    System.out.println("No sales for any stores!");
                                } else {
                                    ArrayList<String> customerInfoAndSales = null;
                                    String oneStoreData;
                                    double totalRevenue = 0;
                                    ///array list that will be output at the end... will output by store name
                                    //Sellers can view a list of their sales by store, including customer information and revenues from the sale.
                                    for (int i = 0; i < salesByStoresList.size(); i++) {
                                        String storingIntoArray = null;
                                        //has to check which stores they own
                                        for (int k = 0; k < salesByStoresList.get(i).getSalesList().size(); k++) {
                                            //users data + item purchase
                                            oneStoreData = "Buyer Name: " + salesByStoresList.get(i).getSalesList().get(k).getBuyerName();
                                            oneStoreData += "\nBuyer Email: " + salesByStoresList.get(i).getSalesList().get(k).getBuyerEmail();
                                            oneStoreData += "\nItems Purchased: " + salesByStoresList.get(i).getSalesList().get(k).getQuantityBought();
                                            //revenue of one person
                                            double revenue = salesByStoresList.get(i).getSalesList().get(k).getQuantityBought() *
                                                    salesByStoresList.get(i).getSalesList().get(k).getProductPrice();
                                            //gets total revenue for one store
                                            totalRevenue = totalRevenue + revenue;
                                            oneStoreData += String.format("\nTotal Revenue from Buyer: $%.2f", revenue);
                                            //stores one customers data into a string
                                            //string of all customers data associated with one specific store
                                            storingIntoArray = oneStoreData + "\n---------------------";
                                        }
                                        storingIntoArray += String.format("\n%s Total Revenue: $%.2f", salesByStoresList.get(i).getStoreName(), totalRevenue);
                                        customerInfoAndSales.add(storingIntoArray);
                                    }
                                    for (int i = 0; i < customerInfoAndSales.size(); i++) {
                                        System.out.println(customerInfoAndSales.get(i));
                                    }
                                    boolean invalidInput = false;
                                    while (!invalidInput) {
                                        int toExit = 0;
                                        try {
                                            System.out.println("[1] to exit");
                                            toExit = s.nextInt();
                                            s.nextLine();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid Entry");
                                        }
                                        if (toExit == 1) {
                                            //returns

                                            invalidInput = true;
                                        }
                                    }
                                }
                            } else if (sellerChoice == 6) {
                                System.out.println("Back to main page!\n");
                                sellerChooseCorrectInput = true;
                                /// IMPLEMENT LOOPING BACK TO MAIN
                                //CALL THE METHOD TO SAVE TO FILE ** EVERYTIME EXITING
                                Methods method = new Methods();
                                method.saveArrayListToFile(itemsSoldBySeller, userAccount);
                                method.saveProductArrayList(Methods.productsOnMarket);
                                break;


                            } else if (sellerChoice == 4) {

                                //checks file
                                //make sure to check file not found exception

                                Files filesObject = new Files("data.txt", true, false);
                                int indexOfChoice;
                                while (true) {

                                    System.out.println("Choose what you want to do:\n[1] Import Products\n[2] Export products\n[3] Quit");
                                    indexOfChoice = 0;
                                    try {
                                        indexOfChoice = s.nextInt();
                                        s.nextLine();
                                    } catch (InputMismatchException e) {
                                        s.nextLine();
                                    }

                                    ArrayList<Product> exportedProducts = null;
                                    if (indexOfChoice == 1) {
                                        System.out.println("What is the name of the import file?");
                                        ArrayList<Product> importedProducts = null;
                                        try {
                                            String fileName = s.nextLine();
                                            importedProducts = new ArrayList<Product>();

                                            importedProducts = filesObject.importProducts(fileName);

                                        } catch (FileNotFoundException f) {
                                            System.out.println("No File Found\n");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if (importedProducts.isEmpty()) {
                                            System.out.println("No imports in file\n");
                                        } else {
                                            System.out.println("Imported Successfully");
                                            for (Product product : importedProducts) {
                                                itemsSoldBySeller.add(product);
                                                Methods.productsOnMarket.add(product);
                                            }
                                        }
                                    } else if (indexOfChoice == 2) {
                                        exportedProducts = new ArrayList<Product>();
                                        int indexOfDeletion = 0;
                                        while (true) {
                                            for (int i = 0; i < itemsSoldBySeller.size(); i++) {
                                                System.out.println("[" + (i + 1) + "] " + itemsSoldBySeller.get(i).listingPagetoString() + "\n");
                                            }
                                            System.out.println("Choose product index you want to remove\nEnter [-1] to stop");
                                            try {
                                                indexOfDeletion = s.nextInt();
                                                s.nextLine();
                                            } catch (InputMismatchException e) {
                                                s.nextLine();
                                            }
                                            if (indexOfDeletion == -1) {
                                                break;
                                            } else if (indexOfDeletion > 0 && indexOfDeletion <= itemsSoldBySeller.size()) {
                                                Methods method = new Methods();
                                                exportedProducts.add(itemsSoldBySeller.get(indexOfDeletion - 1));
                                                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                    if (Methods.productsOnMarket.get(i) == itemsSoldBySeller.get(indexOfDeletion - 1)) {
                                                        Methods.productsOnMarket.remove(i);
                                                        break;
                                                    }
                                                }
                                                //remove item from sales list
                                                itemsSoldBySeller.remove(indexOfDeletion - 1);
                                                try {
                                                    if (filesObject.exportProducts(exportedProducts)) {
                                                        System.out.println("Exported Successfully.\n");
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                System.out.println("Invalid index. Please enter a valid index.\n");
                                            }

                                        }
                                        if (indexOfDeletion == -1) {
                                            break;
                                        } else if (exportedProducts.isEmpty()) {
                                            System.out.println("No products to export.");
                                        } else {
                                            try {
                                                if (filesObject.exportProducts(exportedProducts)) {
                                                    System.out.println("Exported Successfully.");
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else if (indexOfChoice == 3) {
                                        break;
                                    } else {
                                        System.out.println("Invalid Choice\n");
                                    }
                                }
                            } else {
                                System.out.println("Please enter choices: 1-6");
                            }

                        }

                    } else {
                        //customer view

                        boolean exitMarketPlace = false;
                        //boolean to check if user previously has products stored shopping cart
                        boolean emptyShoppingCart = false;

                        //shopping cart array list
                        ArrayList<Product> shoppingCart = new ArrayList<>();
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                            String line = "";
                            ArrayList<String> allUserData = new ArrayList<>();


                            while ((line = bfr.readLine()) != null) {
                                allUserData.add(line);
                            }

                            for (int i = 0; i < allUserData.size(); i++) {
                                String[] oneUserData = allUserData.get(i).split(",");
                                //checking for email
                                if (oneUserData[1].equals(email)) {
                                    //splitting sales array
                                    String[] separatingForSalesArray = allUserData.get(i).split(";");

                                    if (separatingForSalesArray.length == 1) {
                                        emptyShoppingCart = true;


                                        // if we need to populate the array list with previous sales data
                                    } else {
                                        //splitting by PRODUCT

                                        if (!separatingForSalesArray[1].contains("@@")) {
                                            //there is only one product item
                                            String[] separateParameters = separatingForSalesArray[1].split(",");
                                            //order of text file: productName, storeName, description, quantityAvailable, price
                                            String productName = separateParameters[0];
                                            String storeName = separateParameters[1];
                                            String description = separateParameters[2];
                                            int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                            double price = Double.parseDouble(separateParameters[4]);
                                            //recreate product that is currently selling
                                            Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);

                                            //adding to the items to shopping cart previously from file
                                            shoppingCart.add(eachProduct);

                                        } else {

                                            String[] arrayListCartProducts = separatingForSalesArray[1].split("@@");

                                            // separating the string text to get parameters for each product
                                            for (int j = 0; j < arrayListCartProducts.length; j++) {
                                                String[] separateParameters = arrayListCartProducts[j].split(",");
                                                //order of text file: productName, storeName, description, quantityAvailable, price
                                                String productName = separateParameters[0];
                                                String storeName = separateParameters[1];
                                                String description = separateParameters[2];
                                                int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                                double price = Double.parseDouble(separateParameters[4]);
                                                //recreate product that is currently selling
                                                Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);

                                                //adding to the items to shopping cart previously from file
                                                shoppingCart.add(eachProduct);
                                            }
                                        }
                                    }
                                }
                            }


                            bfr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        while (!exitMarketPlace) {


                            System.out.println("Welcome to the Marketplace!");
                            System.out.println("Available products");
                            System.out.println("-------------------\n");

                            Methods method = new Methods();
                            boolean pressOne = false;

                            if (Methods.productsOnMarket.isEmpty()) {
                                System.out.println("No products are currently being sold on the market!");
                                while (!pressOne) {
                                    System.out.println("Enter [1] to go back to main page!");
                                    int exitToMain = s.nextInt();
                                    s.nextLine();
                                    if (exitToMain == 1) {
                                        // add saving
                                        method.saveArrayListToFile(shoppingCart, userAccount);
                                        method.saveProductArrayList(Methods.productsOnMarket);
                                        break;
                                    }
                                }
                                break;
                            } else {
                                //prints out every product insight for listing page
                                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                    System.out.println(("[" + (i + 1) + "]") + " " + Methods.productsOnMarket.get(i).listingPagetoString());
                                    System.out.println("-------------------\n");
                                }
                                boolean invalidInput = false;

                                int productNumber = 0;
                                do {
                                    System.out.println("Type in number for chosen product insights\n-------------------\n[0] Search for products" +
                                            "\n[-1] Check shopping cart\n[-2] to exit");
                                    try {
                                        ///WILL STILL HAVE TO "sort the marketplace on price or quantity available."
                                        productNumber = s.nextInt();
                                        s.nextLine();
                                        if (productNumber == -2) {
                                            method.saveArrayListToFile(shoppingCart, userAccount);
                                            method.saveProductArrayList(Methods.productsOnMarket);
                                            invalidInput = true;
                                        } else if (productNumber > Methods.productsOnMarket.size() || productNumber < 0) {
                                            System.out.println("Invalid input, try again\n");
                                        } else {
                                            invalidInput = true;
                                        }
                                        // if not integer
                                    } catch (InputMismatchException e) {
                                        System.out.println("Please input integer\n");
                                        s.nextLine();
                                    }
                                } while (!invalidInput);


                                if (productNumber == 0) {
                                    System.out.println("What word would you like to input to search?");
                                    String wordSearch = s.nextLine();
                                    ArrayList<Product> searchedProducts = new ArrayList<>();
                                    // checking for similar search in every product in array
                                    for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                        searchedProducts = method.searchForProduct(wordSearch);
                                    }

                                    if (searchedProducts.isEmpty()) {
                                        System.out.println("Sorry! No products matched your search!\n-------------------\n");
                                    } else {
                                        //searching for product and listing out their products

                                        //WILL HAVE TO ADD CODE TO BUY ITEM
                                        for (int i = 0; i < searchedProducts.size(); i++) {
                                            System.out.println((i + 1) + " " + searchedProducts.get(i).listingPagetoString());
                                            System.out.println("-------------------\n");
                                        }

                                        //MUST MUST MUST FIX THISSS
                                        System.out.println("View item statistic!\nType in index number of product:\nEnter [-1] to exit");

                                        int itemFromSearchChosen = s.nextInt();
                                        s.nextLine();

                                        if (itemFromSearchChosen == -1) {
                                            method.saveArrayListToFile(shoppingCart, userAccount);
                                            method.saveProductArrayList(Methods.productsOnMarket);
                                            //leaving to go back to main page
                                            System.out.println("Back to Market Place Listing Page!\n-------------------\n");


                                        } else {

                                            System.out.println(searchedProducts.get(itemFromSearchChosen - 1).statisticsToString() + "\n");
                                            //asking if they would like to purchase the item asked for statistics
                                            //added by taylor
                                            int purchaseResponse;
                                            int chooseRemoval;
                                            int removeFromCart;
                                            do {
                                                System.out.println("Would you like to purchase this item?\n[1] yes\n[2] no\n[3] If you would like to add the item" +
                                                        " to your shopping cart!\n[4] exit");
                                                purchaseResponse = s.nextInt();
                                                s.nextLine();
                                                //added by taylor
                                                int amountPurchasing = 0;
                                                if (purchaseResponse == 1) {
                                                    System.out.println("How many items would you like to purchase");
                                                    amountPurchasing = s.nextInt();
                                                    s.nextLine();

                                                    //implemented by Taylor for checking amt of products

                                                    if (Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable() < amountPurchasing) {
                                                        System.out.println("Could only purchase " + Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable()
                                                                + " products");
                                                        method.purchaseProduct(Methods.productsOnMarket.get(productNumber - 1),
                                                                Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable());
                                                    } else {
                                                        method.purchaseProduct(Methods.productsOnMarket.get(productNumber - 1), amountPurchasing);
                                                        System.out.println(Methods.productsOnMarket.get(productNumber - 1).getProductName() + " purchased! Thank you!");
                                                    }
                                                    // calls method to purchase
                                                    // in method... sets quantity sold and sets quantity available
                                                    //still has to add other statistics for following sales and receipts for customer
                                                    method.purchaseProduct(searchedProducts.get(itemFromSearchChosen - 1), amountPurchasing);
                                                    System.out.println(searchedProducts.get(itemFromSearchChosen - 1).getProductName() + " purchased! Thank you!");
                                                    ///IMPLEMENT: take into consideration if quantity available turns to 0 or will no longer be available
                                                    //EX: 5 quantity left.. buyer wants to

                                                    // later have to change when implementing shopping cart...

                                                    //START OF ADDED CODE
                                                    String buyerName = userAccount.getName();
                                                    String buyerEmail = userAccount.getEmail();
                                                    String storeName = searchedProducts.get(itemFromSearchChosen - 1).getStoreName();
                                                    String productName = searchedProducts.get(itemFromSearchChosen - 1).getProductName();
                                                    double productPrice = searchedProducts.get(itemFromSearchChosen - 1).getPrice();
                                                    int quantityBought = amountPurchasing;

                                                    Product product = searchedProducts.get(itemFromSearchChosen - 1);

                                                    String line;
                                                    String[] userData;

                                                    String sellerEmail = "";
                                                    try (BufferedReader bfr = new BufferedReader(new FileReader("data.txt"))) {
                                                        while ((line = bfr.readLine()) != null) {
                                                            userData = line.split(";");
                                                            String[] storeInfo = userData[0].split(",");

                                                            // Assuming the storeName is the second element in the storeInfo array
                                                            if (storeInfo.length > 1 && storeInfo[1].equals(storeName)) {
                                                                sellerEmail = storeInfo[0]; // Return the username
                                                            }
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace(); // Handle the exception according to your needs
                                                    }
                                                    //implemented by taylor
                                                    ItemBought item = new ItemBought(buyerName, buyerEmail, sellerEmail, storeName, productName, productPrice, quantityBought);
                                                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("salesbystore.txt"))) {
                                                        writer.write(item.toString());
                                                        writer.newLine();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    //END OF ADDED CODE


                                                    //String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price
                                                } else if (purchaseResponse == 2) {
                                                    boolean noExit = false;
                                                    //possible change: this is if do not want to purchase product SHOULD take you back to main page (test it)
                                                    while (!noExit) {
                                                        System.out.println("Feel free to keep looking! Press [1] to exit back to Market Place Listing Page");
                                                        int leave = s.nextInt();
                                                        s.nextLine();
                                                        if (leave == 1) {
                                                            //IMPLEMENTS EXIT
                                                            method.saveArrayListToFile(shoppingCart, userAccount);
                                                            method.saveProductArrayList(Methods.productsOnMarket);
                                                            noExit = true;
                                                        } else {
                                                            //adding seached item to shopping cart

                                                            shoppingCart.add(searchedProducts.get(itemFromSearchChosen - 1));
                                                            System.out.println(searchedProducts.get(itemFromSearchChosen - 1).getProductName() + " added to your shopping cart!");
                                                        }
                                                    }
                                                } else if (purchaseResponse == 3) {
                                                    //adding seached item to shopping cart

                                                    shoppingCart.add(searchedProducts.get(itemFromSearchChosen - 1));
                                                    System.out.println(searchedProducts.get(itemFromSearchChosen - 1).getProductName() + " added to your shopping cart!");
                                                    if (Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable() < amountPurchasing) {
                                                        System.out.println("Could only purchase " + Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable()
                                                                + " products");
                                                        method.purchaseProduct(Methods.productsOnMarket.get(productNumber - 1),
                                                                Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable());
                                                    } else {
                                                        method.purchaseProduct(Methods.productsOnMarket.get(productNumber - 1), amountPurchasing);
                                                        System.out.println(Methods.productsOnMarket.get(productNumber - 1).getProductName() + " purchased! Thank you!");
                                                    }
                                                } else if (purchaseResponse == 4) {
                                                    //exits back to main page
                                                    System.out.println("Back to main page!");
                                                    //IMPLEMENT SAVING METHOD

                                                    //save shopping cart
                                                    method.saveArrayListToFile(shoppingCart, userAccount);

                                                    //save product array list
                                                    method.saveProductArrayList(Methods.productsOnMarket);

                                                }
                                            } while (!invalidInput);

                                        }
                                    }
                                } else if (productNumber == -2) {
                                    System.out.println("Back to main page!");
                                    exitMarketPlace = true;
                                    //IMPLEMENT SAVING METHOD

                                    //save shopping cart
                                    method.saveArrayListToFile(shoppingCart, userAccount);

                                    //save product array list
                                    method.saveProductArrayList(Methods.productsOnMarket);

                                } else if (productNumber == -1) {

                                    System.out.println("Shopping cart");
                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                        System.out.println("[" + (i + 1) + "] " + shoppingCart.get(i).getProductName() + ", " + shoppingCart.get(i).getStoreName()
                                                + ", " + shoppingCart.get(i).getDescriptionOfProduct() + ", " + shoppingCart.get(i).getPrice());
                                    }
                                    int removeFromCart;
                                    int chooseRemoval;
                                    do {
                                        System.out.println("\nWould you like to remove a product? Yes[1] Exit[0]");
                                        removeFromCart = s.nextInt();
                                        s.nextLine();
                                        if (removeFromCart == 1) {
                                            do {
                                                System.out.println("Which product would you want to remove? [0] to cancel");
                                                chooseRemoval = s.nextInt();
                                                s.nextLine();
                                                if (chooseRemoval > shoppingCart.size() + 1 || chooseRemoval < 1) {
                                                    System.out.println("Invalid input, try again");
                                                } else if (chooseRemoval == 0) {
                                                    System.out.println("Exiting shopping cart");
                                                } else {
                                                    System.out.println("Removed the product");
                                                    shoppingCart.remove(chooseRemoval - 1);
                                                }
                                            } while (chooseRemoval != 0);
                                        } else if (removeFromCart == 0) {
                                        } else {
                                            System.out.println("Incorrect Input");
                                        }
                                    } while (!(removeFromCart == 1 || removeFromCart == 0));

                                } else {

                                    System.out.println(Methods.productsOnMarket.get(productNumber - 1).statisticsToString());
                                    //asking if they would like to purchase the item asked for statistics
                                    System.out.println("\nWould you like to purchase this item?\n[1] yes\n[2] no\n[3] If you would like to add the item" +
                                            " to your shopping cart!\n[4] exit");
                                    int purchaseResponse = s.nextInt();
                                    s.nextLine();

                                    if (purchaseResponse == 1) {
                                        System.out.println("How many items would you like to purchase");
                                        int amountPurchasing = s.nextInt();
                                        s.nextLine();
                                        //implemented by Taylor

                                        if (Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable() < amountPurchasing) {
                                            System.out.println("Could only purchase " + Methods.productsOnMarket.get(productNumber - 1).getQuantityAvailable()
                                                    + " products");
                                            method.purchaseProduct(Methods.productsOnMarket.get(productNumber - 1), productNumber - 1);
                                        } else {
                                            method.purchaseProduct(Methods.productsOnMarket.get(productNumber - 1), amountPurchasing);
                                        }

                                        // calls method to purchase
                                        // in method... sets quantity sold and sets quantity available
                                        //still has to add other statistics for following sales and receipts for customer

                                        ///IMPLEMENT: take into consideration if quantity available turns to 0 or will no longer be available
                                        //EX: 5 quantity left.. buyer wants to

                                        // later have to change when implementing shopping cart...


                                        //String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price
                                    } else if (purchaseResponse == 2) {
                                        boolean noExit = false;
                                        //possible change: this is if do not want to purchase product SHOULD take you back to main page (test it)
                                        while (!noExit) {
                                            System.out.println("Feel free to keep looking! Press [1] to exit back to Market Place Listing Page");
                                            int leave = s.nextInt();
                                            s.nextLine();
                                            if (leave == 1) {
                                                //IMPLEMENTS EXIT
                                                method.saveArrayListToFile(shoppingCart, userAccount);
                                                method.saveProductArrayList(Methods.productsOnMarket);
                                                noExit = true;
                                            }
                                        }

                                    } else if (purchaseResponse == 3) {
                                        //adds the product from market into the shopping cart of customer
                                        shoppingCart.add(Methods.productsOnMarket.get(productNumber - 1));
                                        System.out.println(Methods.productsOnMarket.get(productNumber - 1).getProductName() + " added to your shopping cart!");
                                        break;
                                        //needs to save/update this when logout


                                    } else if (purchaseResponse == 4) {
                                        System.out.println("Back to main page!");
                                        //IMPLEMENT SAVING METHOD

                                        //save shopping cart
                                        method.saveArrayListToFile(shoppingCart, userAccount);

                                        //save product array list
                                        method.saveProductArrayList(Methods.productsOnMarket);
                                    }


                                }
                            }

                            ///will still have to check if inputting value that is not available (index 11 but only 8 indexes items)


                        }


                    }
                } else {
                    System.out.println("No email or password with the following was found! Please log in again");

                }
            } else if (selectionToSignIn == 3) {
                System.out.println("Thank you for visiting School Supplies Marketplace!");
                Methods method = new Methods();
                method.saveArrayListToFile(itemsSoldBySeller, userAccount);
                method.saveProductArrayList(Methods.productsOnMarket);

                mainPage = false;
            } else {
                System.out.println("Please input options 1-3\n");
            }


        }


    }
}


