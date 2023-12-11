import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
If IntelliJ or platform says that "cannot run in parallel":
1. Right click on client
2. Select (two windows button) Edit "..."
3. Select "allow Parallel Run"
 */

public class ThreadedMarketPlaceServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        while (true) {
            Socket socket = serverSocket.accept();
            Thread clientHandlerThread = new Thread(new ClientHandler(socket));
            clientHandlerThread.start();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                while (true) {


                    String createOrSignIn = reader.readLine();
                    if (createOrSignIn.equals("processSignIn")) {
                        System.out.println("signing in processing ");
                        //signing in created in client saved to file


                        //create market array list


                        try {
                            //reading products from file into the product array list
                            BufferedReader bfr = new BufferedReader(new FileReader("productArrayList.txt"));
                            String line = "";

                            while ((line = bfr.readLine()) != null) {
                                //splitting by @@ for each product
                                String[] arrayListOfProducts = line.split("@@");
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
                        String emailAndPassword = reader.readLine();
                        String email = emailAndPassword.substring(0, emailAndPassword.indexOf(","));
                        String password = emailAndPassword.substring(emailAndPassword.indexOf(",") + 1);
                        String[] info = new String[4];
                        String userAccountString = "";
                        String logInInfo = "";


                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                            String line = "";
                            ArrayList<String> allUserData = new ArrayList<>();

                            while ((line = bfr.readLine()) != null) {
                                allUserData.add(line);
                            }

                            for (int i = 0; i < allUserData.size(); i++) {
                                String[] oneUserData = allUserData.get(i).split(",");
                                // checking if user and password are the same
                                boolean sellerBoolean = false;
                                if (oneUserData[1].equals(email) && oneUserData[2].equals(password)) {
                                    // format ji,jo1234,1234,false;

                                    // parse boolean for creating the user for sign-in
                                    sellerBoolean = false;
                                    if (oneUserData[3].startsWith("true")) {
                                        sellerBoolean = true;
                                    } else if (oneUserData[3].startsWith("false")) {

                                    }
                                    //STRING FOR USER INFO
                                    userAccountString = oneUserData[0] + "," + oneUserData[1] + "," + oneUserData[2] + "," + sellerBoolean;
                                    userAccount = new User(oneUserData[0], oneUserData[1], oneUserData[2], sellerBoolean);
                                    logInInfo = "";


                                    if (sellerBoolean) {
                                        info[0] = "seller,";
                                        logInInfo += "seller,";
                                    } else {
                                        info[0] = "buyer,";
                                        logInInfo += "buyer,";
                                    }
                                    info[1] = email + ",";
                                    logInInfo += email + ",";

                                    info[2] = password + ",";
                                    logInInfo += password + ",";

                                    info[3] = "true";
                                    logInInfo += "true";


                                    break;

                                } else {

                                    info[0] = "notLogIn";
                                    info[1] = "notLogIn";
                                    info[2] = "notLogIn";
                                    info[3] = "false";
                                    logInInfo = "notLogIn,notLogIn,notLogIn,false";


                                    //SIGN IN SHOULD NOT WORK AND SHOULD LOOP BACK
                                }


                            }

                            bfr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        writer.write(logInInfo);
                        writer.println();
                        writer.flush();


                        // IF SIGN IN WORKS OR NOT ON CLIENT SIDE

                        String signInWorks = reader.readLine();
                        if (signInWorks.equals("notSignIn")) {
                            System.out.println("does not sign in");

                        } else if (signInWorks.equals("signIn")) {
                            System.out.println("sign in works");

                            //SEND SERVER USER INFO

                            writer.write(userAccountString);
                            writer.println();
                            writer.flush();


                            String sellerOrBuyer = reader.readLine();


                            if (sellerOrBuyer.equals("seller")) {

                                // PROCESSING FOR SELLER
                                boolean exitSeller = false;
                                Methods method = new Methods();

                                String userName = reader.readLine();
                                String userEmail = reader.readLine();
                                String userPassword = reader.readLine();

                                userAccount = new User(userName, userEmail, userPassword, true);

                                ArrayList<Product> myProducts = method.generateMyProducts(userAccount);


                                while (!exitSeller) {

                                    String button = reader.readLine();


                                    if (button.equals("viewCustomerCarts")) {
                                        System.out.println("customer cart button clicked");
                                        String output = "";

                                        String allStoreNames = method.findingStoreNamesForSeller(userAccount);

                                        if (allStoreNames.isEmpty()) {
                                            // no stores so nothing for sale
                                            writer.write("noStoresForSale");
                                            writer.println();
                                            writer.flush();
                                        } else {
                                            writer.write("storeShown");
                                            writer.println();
                                            writer.flush();
                                            //method which takes the store names and finds shopping carts


                                            File dataFile = new File("data.txt");
                                            ArrayList<String> allUserData = new ArrayList<>();

                                            try {
                                                BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
                                                String line;
                                                while ((line = bfr.readLine()) != null) {
                                                    allUserData.add(line);
                                                }
                                                bfr.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            for (int i = 0; i < allUserData.size(); i++) {
                                                //splitting to find which ones are shopping carts
                                                String[] customerUsers = allUserData.get(i).split(",");
                                                if (customerUsers[3].startsWith("false")) {
                                                    String[] shoppingCart = allUserData.get(i).split(";");
                                                    if (shoppingCart.length == 1) {
                                                        //no shopping cart for customer
                                                    } else {

                                                        String[] productsInCart = shoppingCart[1].split("@@");
                                                        for (int k = 0; k < productsInCart.length; k++) {
                                                            String[] eachProduct = productsInCart[k].split(",");
                                                            if (allStoreNames.contains(eachProduct[1])) {
                                                                //FORMAT : email name, product name, store name, description, quantity available, price, quantity buying, @@ to separate product in cart

                                                                //pencils,walmart,a pack of 10 pencils,18,5.0,1@@

                                                                output += "User Email: " + customerUsers[1] + ", Product Name: " + eachProduct[0] + ", Store Name: " + eachProduct[1] + ", Description: "
                                                                        + eachProduct[2] + ", Quantity Available: " + eachProduct[3] + ", Price: $" + eachProduct[4] + ", Quantity Buying: " + eachProduct[5] + "@@";

                                                            }
                                                        }
                                                    }


                                                } else {
                                                    // not a customer
                                                }
                                            }
                                            if (output.isEmpty()) {
                                                writer.write("noCustomerCarts");
                                                writer.println();
                                                writer.flush();
                                                // no items in a customers carts from stores/products
                                            } else {
                                                System.out.println(output);
                                                writer.write(output);
                                                writer.println();
                                                writer.flush();
                                            }
                                        }


                                    }
                                    if (button.equals("sellButton")) {
                                        System.out.println("Sell pressed");
                                        String productToSell = reader.readLine();

                                        if (productToSell.equals("PanelClosed")) {

                                        } else if (productToSell.equals("wrong")) {

                                        } else {
                                            String[] productInfoParts = productToSell.split(",");
                                            String productName = productInfoParts[0];
                                            String storeName = productInfoParts[1];
                                            String description = productInfoParts[2];

                                            try {
                                                int quantity = Integer.parseInt(productInfoParts[3]);
                                                double price = Double.parseDouble(productInfoParts[4]);

                                                if (quantity <= 0 || price <= 0) {
                                                    writer.write("InvalidInput");
                                                    writer.println();
                                                    writer.flush();
                                                    return;
                                                }

                                                boolean existingStoreName = false;

                                                try {
                                                    BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                                                    String line = "";
                                                    ArrayList<String> allUserData = new ArrayList<>();

                                                    while ((line = bfr.readLine()) != null) {
                                                        allUserData.add(line);
                                                    }

                                                    for (int i = 0; i < allUserData.size(); i++) {
                                                        String[] checkIfSeller = allUserData.get(i).split(",");
                                                        if (checkIfSeller[3].startsWith("true")) {
                                                            String[] oneUserDataEachProduct = allUserData.get(i).split(";");

                                                            if (oneUserDataEachProduct.length == 1) {

                                                            } else {
                                                                String[] eachProduct = oneUserDataEachProduct[1].split("@@");
                                                                for (int k = 0; k < eachProduct.length; k++) {
                                                                    String[] eachFieldForProduct = eachProduct[k].split(",");
                                                                    if (eachFieldForProduct[1].equals(storeName) &&
                                                                            !checkIfSeller[1].equals(userAccount.getEmail())) {
                                                                        existingStoreName = true;
                                                                        break;
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
                                                    writer.write("existingStoreName");
                                                    writer.println();
                                                    writer.flush();
                                                } else {
                                                    writer.write("noPreviousStoreName");
                                                    writer.println();
                                                    writer.flush();

                                                    Product sellThis = new Product(productName, storeName, description, quantity, price);

                                                    //myProducts arraylist (in server)
                                                    myProducts.add(sellThis);
                                                    //Products arraylist (all products on market)
                                                    Methods.productsOnMarket.add(sellThis);
                                                    System.out.println("TEST OF ARRAY" + Methods.productsOnMarket);
                                                    try {
                                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("productArrayList.txt", true)));

                                                        pw.print(sellThis.getProductName() + "," + sellThis.getStoreName() + "," + sellThis.getDescriptionOfProduct() + ","
                                                                + sellThis.getQuantityAvailable() + "," + sellThis.getPrice() + "@@");

                                                        pw.flush();
                                                        pw.close();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    //Data file (data for each account)
                                                    method.saveDataFileWhenNewProductAddedUserAccount(userAccount, sellThis);
                                                }
                                            } catch (NumberFormatException e) {
                                                writer.write("InvalidInput");
                                                writer.println();
                                                writer.flush();
                                            }
                                        }
                                    } else if (button.equals("editButton")) {
                                        System.out.println("edit button");
                                        System.out.println(myProducts);
                                        if (myProducts.isEmpty()) {
                                            System.out.println("Empty");
                                            writer.println("Empty");
                                            writer.println();
                                            writer.flush();
                                        } else {
                                            for (int i = 0; i < myProducts.size(); i++) {
                                                writer.println(myProducts.get(i).statisticsToStringNoSpace());
                                                System.out.println(myProducts.get(i).statisticsToStringNoSpace());
                                            }
                                            writer.println();
                                            writer.flush();

                                            String closeSelect = reader.readLine();
                                            if (closeSelect.equals("PanelClosed")) {
                                            } else {
                                                System.out.println(closeSelect);
                                                int selection = Integer.parseInt(closeSelect);
                                                System.out.println("selected item #" + selection);
                                                Product oldSelected = myProducts.get(selection);
                                                System.out.println("selected " + oldSelected.statisticsToString());

                                                String newProductName = reader.readLine();

                                                if (newProductName.equals("PanelClosed")) {
                                                } else if (newProductName.equals("wrong")) {

                                                } else {
                                                    String newStoreName = reader.readLine();
                                                    String newDescription = reader.readLine();

                                                    try {
                                                        int newQuantity = Integer.parseInt(reader.readLine());
                                                        double newPrice = Double.parseDouble(reader.readLine());

                                                        Product newEditedProduct = new Product(newProductName, newStoreName, newDescription, newQuantity, newPrice);
                                                        method.modifyShoppingCartProduct(oldSelected, newEditedProduct);


                                                        boolean existingStoreName = false;

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
                                                                    //no products so no problem
                                                                } else {
                                                                    // separating the individual products
                                                                    String[] eachProduct = oneUserDataEachProduct[1].split("@@");
                                                                    for (int k = 0; k < eachProduct.length; k++) {
                                                                        String[] eachFieldForProduct = eachProduct[k].split(",");
                                                                        // checking if store name is the same and email is the same
                                                                        if (eachFieldForProduct[1].equals(newStoreName) &&
                                                                                !checkIfSeller[1].equals(userAccount.getEmail())) {
                                                                            existingStoreName = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        bfr.close();

                                                        if (existingStoreName) {
                                                            writer.write("existingStoreName");
                                                            writer.println();
                                                            writer.flush();
                                                        } else {
                                                            writer.write("noPreviousStoreName");
                                                            writer.println();
                                                            writer.flush();

                                                            Product newProduct = new Product(newProductName, newStoreName, newDescription, newQuantity, newPrice);

                                                            //Products arraylist (all products on market)
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            int indexSelectionMarket = Methods.productsOnMarket.indexOf(oldSelected);

                                                            for (Product product : Methods.productsOnMarket) {
                                                                System.out.println(product.statisticsToStringNoSpace());
                                                            }
                                                            System.out.println();
                                                            System.out.println("Product I want to change in myproducts");
                                                            System.out.println(oldSelected.statisticsToStringNoSpace());
                                                            System.out.println();

                                                            System.out.println("New Product deets");
                                                            System.out.println(newProduct.statisticsToStringNoSpace());
                                                            System.out.println();

                                                            for (Product product : myProducts) {
                                                                if (product.statisticsToStringNoSpace().equals(oldSelected.statisticsToStringNoSpace())) {
                                                                    System.out.println("I found the product I want to change in my products");
                                                                    System.out.println(product.statisticsToStringNoSpace());
                                                                    System.out.println("Here is where i chage it");
                                                                    myProducts.set(myProducts.indexOf(product), newProduct);
                                                                    System.out.println("Changed!");
                                                                    System.out.println("Changed product in my products:");
                                                                    System.out.println(myProducts.get(myProducts.indexOf(newProduct)).statisticsToStringNoSpace());
                                                                    method.saveDataFileWithNewProductList(userAccount, myProducts);
                                                                    break;
                                                                }
                                                            }

                                                            for (Product product : Methods.productsOnMarket) {
                                                                if (product.statisticsToStringNoSpace().equals(oldSelected.statisticsToStringNoSpace())) {
                                                                    System.out.println("I found the product I want to change in the marketplace");
                                                                    System.out.println(product.statisticsToStringNoSpace());
                                                                    System.out.println("Here is where i chage it");
                                                                    Methods.productsOnMarket.set(Methods.productsOnMarket.indexOf(product), newProduct);
                                                                    System.out.println("Changed!");
                                                                    System.out.println("Changed product in marketplace:");
                                                                    System.out.println(Methods.productsOnMarket.get(Methods.productsOnMarket.indexOf(newProduct)).statisticsToStringNoSpace());
                                                                    method.saveProductFile(Methods.productsOnMarket);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    } else if (button.equals("deleteButton")) {
                                        System.out.println("delete button");

                                        if (myProducts.isEmpty()) {
                                            System.out.println("Empty");
                                            writer.println("Empty");
                                            writer.println();
                                            writer.flush();
                                        } else {
                                            for (Product product : myProducts) {
                                                writer.println(product.statisticsToStringNoSpace());
                                            }
                                            writer.println();
                                            writer.flush();
                                        }


                                        String closeSelect = reader.readLine();
                                        if (closeSelect.equals("PanelClosed")) {

                                        } else {
                                            int selection = Integer.parseInt(closeSelect);
                                            System.out.println("selected item #" + selection);
                                            Product itemToDelete = myProducts.get(selection);
                                            System.out.println("selected " + itemToDelete.statisticsToString());


                                            //method to delete item from shopping carts in data.txt
                                            method.deleteShoppingCartProduct(itemToDelete);


                                            myProducts.remove(itemToDelete);
                                            method.saveDataFileWithNewProductList(userAccount, myProducts);
                                            System.out.println("Reached");


                                            for (Product p : Methods.productsOnMarket) {
                                                System.out.println(p.statisticsToStringNoSpace());
                                                if (p.statisticsToStringNoSpace().equals(itemToDelete.statisticsToStringNoSpace())) {
                                                    System.out.println("I found the product I want to remove in the marketplace");
                                                    System.out.println(p.statisticsToStringNoSpace());
                                                    System.out.println("Here is where i delete it");

                                                    //remove from product data file

                                                    method.deleteForProductFile(itemToDelete);


                                                    Methods.productsOnMarket.remove(itemToDelete);
                                                    System.out.println("Deleted!");
                                                    System.out.println("This should be -1:");
                                                    System.out.println(Methods.productsOnMarket.indexOf(itemToDelete));
                                                    break;
                                                }
                                            }


                                            System.out.println("Reached");


                                        }

                                    } else if (button.equals("importButton")) {
                                        System.out.println("import pressed");
                                        String closeSelect = reader.readLine();
                                        if (closeSelect.equals("PanelClosed")) {

                                        } else {
                                            String everything = closeSelect;
                                            System.out.println(everything);

                                            String[] products = everything.split("@@");

                                            for (String product : products) {
                                                String[] parts = product.split(",");
                                                String productName = parts[0];
                                                String storeName = parts[1];
                                                String description = parts[2];
                                                int quantity = Integer.parseInt(parts[3]);
                                                double price = Double.parseDouble(parts[4]);

                                                Product importedProduct = new Product(productName, storeName, description, quantity, price);

                                                //myProducts arraylist (in server)
                                                myProducts.add(importedProduct);
                                                //Products arraylist (all products on market)
                                                Methods.productsOnMarket.add(importedProduct);
                                                method.saveProductFile(Methods.productsOnMarket);
                                                //Data file (data for each account)
                                                method.saveDataFileWhenNewProductAddedUserAccount(userAccount, importedProduct);

                                            }
                                        }
                                    } else if (button.equals("exportButton")) {
                                        System.out.println("export button");

                                        if (myProducts.isEmpty()) {
                                            System.out.println("Empty");
                                            writer.println("Empty");
                                            writer.println();
                                            writer.flush();
                                        } else {
                                            for (Product product : myProducts) {
                                                writer.println(product.statisticsToStringNoSpace());
                                            }
                                            writer.println();
                                            writer.flush();
                                        }

                                        String closeSelect = reader.readLine();
                                        if (closeSelect.equals("PanelClosed")) {

                                        } else {
                                            int selection = Integer.parseInt(closeSelect);
                                            System.out.println("selected item #" + selection);
                                            Product itemToExport = myProducts.get(selection);
                                            System.out.println("selected " + itemToExport.statisticsToString());

                                            String fileName = userAccount.getEmail() + "Exports.txt";
                                            try (FileWriter writerForFile = new FileWriter(fileName)) {
                                                writerForFile.write(itemToExport.statisticsToString());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            myProducts.remove(itemToExport);
                                            method.saveDataFileWithNewProductList(userAccount, myProducts);
                                            System.out.println("Reached");

                                            for (Product p : Methods.productsOnMarket) {
                                                System.out.println(p.statisticsToStringNoSpace());
                                                if (p.statisticsToStringNoSpace().equals(itemToExport.statisticsToStringNoSpace())) {
                                                    System.out.println("I found the product I want to remove in the marketplace");
                                                    System.out.println(p.statisticsToStringNoSpace());
                                                    System.out.println("Here is where i delete it");

                                                    //remove from product data file

                                                    method.deleteForProductFile(itemToExport);

                                                    Methods.productsOnMarket.remove(itemToExport);
                                                    System.out.println("Deleted!");
                                                    System.out.println("This should be -1:");
                                                    System.out.println(Methods.productsOnMarket.indexOf(itemToExport));
                                                    break;
                                                }
                                            }

                                            System.out.println("Reached");

                                        }

                                    } else if (button.equals("salesByStoreButton")) {
                                        //TODO: salesbystore
                                        ArrayList<String> customerHistory = new ArrayList<>();
                                        try {
                                            String line = "";
                                            BufferedReader bfr = new BufferedReader(new FileReader("customerHistory.txt"));
                                            while ((line = bfr.readLine()) != null) {
                                                customerHistory.add(line);
                                            }
                                        } catch (IOException f) {
                                            f.printStackTrace();
                                        }
                                        Methods methods = new Methods();
                                        ArrayList<String> storeNames = methods.findingStoreNamesForSellerList(userAccount);
                                        ArrayList<String> endResult = new ArrayList<>();
                                        String temp;
                                        String info2;
                                        String email2;
                                        String[] eachProduct = null;
                                        String[] eachProductFull = null;
                                        double revenue;
                                        for (int i = 0; i < storeNames.size(); i++) {
                                            revenue = 0;
                                            for (int x = 0; x < customerHistory.size(); x++) {
                                                temp = customerHistory.get(x);
                                                email2 = temp.substring(0, temp.indexOf(":"));
                                                info2 = temp.substring(temp.indexOf(":") + 1);
                                                eachProductFull = info2.split("-");
                                                for (int z = 0; z < eachProductFull.length; z++) {
                                                    eachProduct = eachProductFull[z].split(",");
                                                    if (eachProduct[1].equals(storeNames.get(i))) {
                                                        revenue = Double.parseDouble(eachProduct[4]) * Integer.parseInt(eachProduct[5]);
                                                        endResult.add(storeNames.get(i) + ": Product: " + eachProduct[0] + " Buyer: " + email2 + " Revenue: " + revenue + ", ");
                                                    }
                                                }
                                            }
                                        }

                                        String sendBack = "";
                                        for (int i = 0; i < endResult.size(); i++) {
                                            sendBack += endResult.get(i) + "@";
                                        }
                                        writer.write(sendBack);
                                        writer.println();
                                        writer.flush();
                                    }
                                    if (button.equals("editProfileButton")) {
                                        System.out.println("edit profile enter");
                                        String profileInfo = reader.readLine();

                                        if (profileInfo.equals("null")) {
                                            // exited out

                                        } else {
                                            String[] partsNew = profileInfo.split(",");

                                            String newUserName = partsNew[0];
                                            String newEmail = partsNew[1];
                                            String newPassword = partsNew[2];


                                            method.changeAccount(newUserName, newEmail, newPassword, userAccount);


                                            if (!newUserName.equals(userAccount.getName())) {
                                                userAccount.setName(newUserName);
                                            }
                                            if (!newEmail.equals(userAccount.getEmail())) {
                                                userAccount.setEmail(newEmail);
                                            }
                                            if (!newPassword.equals(userAccount.getPassword())) {
                                                userAccount.setPassword(newPassword);
                                            }
                                            System.out.println("successful edit");

                                        }

                                    } else if (button.equals("deleteAccount")) {
                                        method.removeAccount(userAccount);
                                        for (int i = 0; i < myProducts.size(); i++) {
                                            method.deleteForProductFile(myProducts.get(i));
                                            method.deleteShoppingCartProduct(myProducts.get(i));
                                        }


                                    } else if (button.equals("viewCustomerCartsButton")) {
                                        //TODO: customerCarts

                                    }

                                }

                            } else if (sellerOrBuyer.equals("buyer")) {
                                boolean noExit = false;


                                while (!noExit) {
                                    assert userAccount != null;
                                    Methods method = new Methods();
                                    ArrayList<ShoppingCartProduct> shoppingCart = method.createShoppingCartArray(userAccount);

                                    //PROCESSING FOR BUYER

                                    ///IF SEE BUTTON CLICKED

                                    String buttonClicked = reader.readLine();
                                    if (buttonClicked == null) {
                                        //exit
                                    }
                                    if (buttonClicked.equals("null")) {

                                    }
                                    if (buttonClicked.equals("seeProduct")) {

                                        System.out.println("1: see product button");


                                        //METHOD AFTER SORTING OR NOT SORTING MARKET WHEN PRODUCT CHOSEN

                                        String chooseProduct = reader.readLine();
                                        if (chooseProduct.equals("null")) {


                                        } else {
                                            Methods.productsOnMarket = method.makeProductArrayList();
                                            System.out.println(chooseProduct);
                                            System.out.println("chosen product");


                                            String[] productChangedNames = chooseProduct.split(",");
                                            String findProductName = productChangedNames[0].substring(productChangedNames[0].indexOf(":") + 2);
                                            String findStoreName = productChangedNames[1].substring(productChangedNames[1].indexOf(":") + 2);


                                            //reads the chosen product


                                            Product productBought = null;
                                            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                if (Methods.productsOnMarket.get(i).getProductName().equals(findProductName) &&
                                                        Methods.productsOnMarket.get(i).getStoreName().equals(findStoreName)) {
                                                    productBought = Methods.productsOnMarket.get(i);
                                                }
                                            }


                                            //OPENING NEW PANEL ON CLIENT SIDE WITH SHOPPING CART, BUY, PRODUCT STATISTICS

                                            String buttonChosenStatistics = reader.readLine();
                                            if (buttonChosenStatistics == null) {

                                            }
                                            if (buttonChosenStatistics.equals("null")) {
                                                System.out.println("buttonchosenstatistic null when choosing button");

                                            }

                                            String amountPurchasingString = null;

                                            if (buttonChosenStatistics.equals("buyProduct")) {
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                System.out.println("4: buy product");


                                                //able to purchase correct input amount purchasing

                                                int amountPurchasing = 0;

                                                amountPurchasingString = reader.readLine();
                                                System.out.println("amount purchase string " + amountPurchasingString);

                                                if (!amountPurchasingString.equals("null")) {
                                                    amountPurchasing = Integer.parseInt(amountPurchasingString);
                                                    System.out.println("null");


                                                    System.out.println(amountPurchasing);


                                                    if (productBought.getQuantityAvailable() == 0) {


                                                        for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                            if (Methods.productsOnMarket.get(i).getProductName().equals(findProductName) &&
                                                                    Methods.productsOnMarket.get(i).getStoreName().equals(findStoreName)) {
                                                                productBought = Methods.productsOnMarket.get(i);
                                                            }
                                                        }
                                                        //product is sold out
                                                        writer.write("soldOut");
                                                        writer.println();
                                                        writer.flush();
                                                        //writes back to client
                                                    } else if (productBought.getQuantityAvailable() < amountPurchasing) {
                                                        // if more than quantity available

                                                        //ALL SAVING METHODS: sets shopping cart availability for all users, saves data file of products for all users,
                                                        // and saves shopping cart for specific USER

                                                        method.purchaseProduct(productBought, productBought.getQuantityAvailable());
                                                        productBought.setQuantityAvailable(0);

                                                        method.saveProductFile(Methods.productsOnMarket);
                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                            if (shoppingCart.get(i).getProductName().equals(productBought.getProductName()) &&
                                                                    shoppingCart.get(i).getStoreName().equals(productBought.getStoreName())) {
                                                                shoppingCart.get(i).setQuantityAvailable(0);
                                                                method.saveDataFileCart(shoppingCart.get(i));
                                                            }
                                                        }
                                                        method.saveDataFileWhenPurchased(Methods.productsOnMarket, productBought);
                                                        method.saveProductFile(Methods.productsOnMarket);
                                                        method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                                        method.updateAllShoppingCartsWhenPurchase(productBought);


                                                        //writing back to client for message
                                                        writer.write("purchaseLimit");
                                                        writer.println();
                                                        writer.flush();


                                                        //purchasing product

                                                        // must implement customer receipts ******

                                                    } else {
                                                        System.out.println("amount purchasing " + amountPurchasing);
                                                        //no quantity limit when purchasing
                                                        int currentQuantityAvailable = productBought.getQuantityAvailable();
                                                        method.purchaseProduct(productBought, amountPurchasing);
                                                        productBought.setQuantityAvailable(currentQuantityAvailable - amountPurchasing);

                                                        //all saving methods for setting product available to new for data file, product file, shopping cart
                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                            if (shoppingCart.get(i).getProductName().equals(productBought.getProductName()) &&
                                                                    shoppingCart.get(i).getStoreName().equals(productBought.getStoreName())) {
                                                                shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());
                                                                method.saveDataFileCart(shoppingCart.get(i));
                                                            }
                                                        }

                                                        method.saveDataFileWhenPurchased(Methods.productsOnMarket, productBought);
                                                        method.saveProductFile(Methods.productsOnMarket);
                                                        method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                                        method.updateAllShoppingCartsWhenPurchase(productBought);
                                                        // must implement customer reciepts *****

                                                        //writing back to client item purchase
                                                        writer.write("itemPurchased");
                                                        writer.println();
                                                        writer.flush();
                                                    }
                                                }


                                            }
                                            System.out.println("product quantity test " + productBought.getQuantityAvailable());
                                            if (buttonChosenStatistics.equals("addToCart")) {
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                System.out.println("4: add cart ");
                                                // add to cart button in client pressed


                                                String quantityBuyingString = null;
                                                int quantityBuying = 0;


                                                quantityBuyingString = reader.readLine();


                                                if (!quantityBuyingString.equals("null")) {
                                                    quantityBuying = Integer.parseInt(quantityBuyingString);


                                                    String productName = productBought.getProductName();
                                                    String storeName = productBought.getStoreName();
                                                    String description = productBought.getDescriptionOfProduct();
                                                    int quantityAvailable = productBought.getQuantityAvailable();
                                                    double price = productBought.getPrice();


                                                    if (quantityAvailable == 0) {
                                                        writer.write("soldOut");
                                                        writer.println();
                                                        writer.flush();
                                                    } else if (quantityAvailable < quantityBuying) {
                                                        writer.write("limitedQuantity");
                                                        writer.println();
                                                        writer.flush();
                                                        ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                price, quantityAvailable);
                                                        System.out.println("new quantity available test " + quantityAvailable);
                                                        shoppingCart.add(addedProductToCart);
                                                        method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);

                                                    } else {
                                                        ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                price, quantityBuying);
                                                        shoppingCart.add(addedProductToCart);
                                                        method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);

                                                        writer.write("addedToCart");
                                                        writer.println();
                                                        writer.flush();
                                                    }

                                                }
                                            }


                                        }
                                    }

                                    if (buttonClicked.equals("searchProductsButton")) {
                                        Methods.productsOnMarket = method.makeProductArrayList();

                                        //search products button chosen


                                        String wordSearch = reader.readLine();
                                        System.out.println("1 search product button pressed ");
                                        System.out.println("word searched: " + wordSearch + "end");
                                        if (wordSearch.isEmpty()) {
                                            System.out.println("null/pressed exit");

                                        } else {
                                            ArrayList<Product> searchedProducts = new ArrayList<>();
                                            searchedProducts = method.searchForProduct(wordSearch, Methods.productsOnMarket);
                                            System.out.println("searched found :" + searchedProducts);

                                            if (searchedProducts.isEmpty()) {
                                                writer.write("noProductsFound");
                                                writer.println();
                                                writer.flush();
                                                //telling client nothing is found
                                            } else {
                                                //products found
                                                writer.write("productsFound");
                                                writer.println();
                                                writer.flush();

                                                StringBuilder brokenUp = new StringBuilder();

                                                for (int i = 0; i < searchedProducts.size(); i++) {
                                                    brokenUp.append(searchedProducts.get(i).statisticsToString()).append("@@");
                                                }

                                                writer.write(brokenUp.toString());
                                                writer.println();
                                                writer.flush();


                                                //reading chosen item from client
                                                String itemFromSearchChosen = reader.readLine();
                                                if (itemFromSearchChosen.equals("null")) {
                                                    System.out.println("exit null press product from search");
                                                } else {


                                                    //reads the chosen product


                                                    String[] productChangedNames = itemFromSearchChosen.split(",");
                                                    String findProductName = productChangedNames[0].substring(productChangedNames[0].indexOf(":") + 2);
                                                    String findStoreName = productChangedNames[1].substring(productChangedNames[1].indexOf(":") + 2);
                                                    System.out.println("product name " + findProductName);
                                                    System.out.println("store name: " + findStoreName);


                                                    Product productBoughtNew = null;
                                                    boolean newPage = false;

                                                    for (int k = 0; k < searchedProducts.size(); k++) {
                                                        if (searchedProducts.get(k).getProductName().equals(findProductName) &&
                                                                searchedProducts.get(k).getStoreName().equals(findStoreName)) {
                                                            productBoughtNew = searchedProducts.get(k);
                                                            newPage = true;
                                                            //product insight page
                                                        }
                                                    }
                                                    System.out.println("product found statstic " + productBoughtNew.statisticsToString());
                                                    if (newPage) {
                                                        //PRODUCT INSIGHTS


                                                        System.out.println("entering else ");


                                                        //reads the chosen product

                                                        for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                            if (Methods.productsOnMarket.get(i).getProductName().equals(findProductName) &&
                                                                    Methods.productsOnMarket.get(i).getStoreName().equals(findStoreName)) {
                                                                productBoughtNew = Methods.productsOnMarket.get(i);
                                                            }
                                                        }


                                                        //OPENING NEW PANEL ON CLIENT SIDE WITH SHOPPING CART, BUY, PRODUCT STATISTICS
                                                        String buttonChosenStatistics = null;

                                                        buttonChosenStatistics = reader.readLine();
                                                        if (buttonChosenStatistics.equals("null")) {
                                                            System.out.println("null/exit");


                                                        }
                                                        String amountPurchasingString = null;
                                                        if (buttonChosenStatistics.equals("buyProductSearch")) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            System.out.println("buy product");


                                                            //able to purchase correct input amount purchasing
                                                            int amountPurchasing = 0;


                                                            amountPurchasingString = reader.readLine();
                                                            System.out.println("amountpurchasing " + amountPurchasingString);


                                                            if (!amountPurchasingString.equals("null")) {
                                                                amountPurchasing = Integer.parseInt(amountPurchasingString);
                                                                System.out.println("invalid input");

                                                                System.out.println("amount buying " + amountPurchasing);


                                                                if (productBoughtNew.getQuantityAvailable() == 0) {

                                                                    //product is sold out
                                                                    writer.write("soldOut");
                                                                    writer.println();
                                                                    writer.flush();
                                                                    //writes back to client

                                                                } else if (productBoughtNew.getQuantityAvailable() < amountPurchasing) {
                                                                    // if more than quantity available


                                                                    // and saves shopping cart for specific USER
                                                                    method.purchaseProduct(productBoughtNew, productBoughtNew.getQuantityAvailable());
                                                                    productBoughtNew.setQuantityAvailable(0);

                                                                    method.saveProductFile(Methods.productsOnMarket);
                                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                                        if (shoppingCart.get(i).getProductName().equals(productBoughtNew.getProductName()) &&
                                                                                shoppingCart.get(i).getStoreName().equals(productBoughtNew.getStoreName())) {
                                                                            shoppingCart.get(i).setQuantityAvailable(0);
                                                                            method.saveDataFileCart(shoppingCart.get(i));
                                                                        }
                                                                    }
                                                                    method.saveDataFileWhenPurchased(Methods.productsOnMarket, productBoughtNew);
                                                                    method.saveProductFile(Methods.productsOnMarket);
                                                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);

                                                                    //writing back to client for message
                                                                    writer.write("purchaseLimit");
                                                                    writer.println();
                                                                    writer.flush();


                                                                    //purchasing product

                                                                    // must implement customer receipts ******


                                                                    //purchasing product

                                                                    // must implement customer receipts ******

                                                                } else {

                                                                    int currentQuantityAvailable = productBoughtNew.getQuantityAvailable();
                                                                    method.purchaseProduct(productBoughtNew, amountPurchasing);
                                                                    productBoughtNew.setQuantityAvailable(currentQuantityAvailable - amountPurchasing);
                                                                    System.out.println("product new quantity available after bought: " + productBoughtNew.getQuantityAvailable());


                                                                    //all saving methods for setting product available to new for data file, product file, shopping cart
                                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                                        if (shoppingCart.get(i).getProductName().equals(productBoughtNew.getProductName()) &&
                                                                                shoppingCart.get(i).getStoreName().equals(productBoughtNew.getStoreName())) {
                                                                            shoppingCart.get(i).setQuantityAvailable(productBoughtNew.getQuantityAvailable());
                                                                            method.saveDataFileCart(shoppingCart.get(i));
                                                                        }
                                                                    }

                                                                    method.saveDataFileWhenPurchased(Methods.productsOnMarket, productBoughtNew);
                                                                    method.saveProductFile(Methods.productsOnMarket);
                                                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                                                    // must implement customer reciepts *****

                                                                    //writing back to client item purchase
                                                                    writer.write("itemPurchased");
                                                                    writer.println();
                                                                    writer.flush();
                                                                }


                                                            } else {

                                                            }


                                                        }


                                                        if (buttonChosenStatistics.equals("addToCartSearch")) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            // add to cart button in client pressed
                                                            System.out.println("add to cart button");

                                                            String quantityBuyingString = null;
                                                            int quantityBuying = 0;
                                                            boolean valid = false;

                                                            quantityBuyingString = reader.readLine();


                                                            if (quantityBuyingString.equals("null")) {
                                                                System.out.println("null quantity buying cart exit");


                                                            } else {
                                                                quantityBuying = Integer.parseInt(quantityBuyingString);
                                                                System.out.println("not null");


                                                                String description = productBoughtNew.getDescriptionOfProduct();
                                                                int quantityAvailable = productBoughtNew.getQuantityAvailable();
                                                                double price = productBoughtNew.getPrice();

                                                                if (quantityAvailable == 0) {
                                                                    writer.write("soldOut");
                                                                    writer.println();
                                                                    writer.flush();
                                                                } else if (quantityAvailable < quantityBuying) {
                                                                    writer.write("limitedQuantity");
                                                                    writer.println();
                                                                    writer.flush();
                                                                    ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(findProductName, findStoreName, description, quantityAvailable,
                                                                            price, quantityAvailable);
                                                                    shoppingCart.add(addedProductToCart);
                                                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);

                                                                } else {
                                                                    ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(findProductName, findStoreName, description, quantityAvailable,
                                                                            price, quantityBuying);
                                                                    shoppingCart.add(addedProductToCart);
                                                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);

                                                                    writer.write("addedToCart");
                                                                    writer.println();
                                                                    writer.flush();
                                                                }
                                                            }
                                                        }


                                                    }
                                                }
                                            }
                                        }

                                    }
                                    if (buttonClicked.equals("null")) {
                                        System.out.println("exit clicked no buy or cart");

                                    }
                                    if (buttonClicked.equals("viewShoppingCart")) {
                                        shoppingCart = method.createShoppingCartArray(userAccount);
                                        Methods.productsOnMarket = method.makeProductArrayList();

                                        System.out.println("view shopping cart pressed");
                                        //VIEW SHOPPING CART BUTTON


                                        String cartButtonClicked = reader.readLine();


                                        if (cartButtonClicked.equals("null")) {
                                            //exits out using x button

                                        }
                                        if (cartButtonClicked.equals("emptyCart")) {
                                            System.out.println("empty cart pressed");

                                        }


                                        if (cartButtonClicked.equals("viewCart")) {
                                            System.out.println("view cart view pressed");


                                            //shows cart and which product if want to view statistic

                                            //no processing in server at all


                                        }
                                        if (cartButtonClicked.equals("removeProduct")) {
                                            shoppingCart = method.createShoppingCartArray(userAccount);
                                            Methods.productsOnMarket = method.makeProductArrayList();
                                            System.out.println("remove product pressed");

                                            String seeProduct = reader.readLine();
                                            if (seeProduct.equals("null")) {
                                                System.out.println("null remove product ");

                                                // pressed x
                                            } else {
                                                System.out.println("print" + seeProduct + "end");
                                                String[] product = seeProduct.split(",");
                                                String[] findProductName = product[0].split(":");

                                                String productName = findProductName[2].substring(1);
                                                String storeName = product[1].substring(product[1].indexOf(":") + 2);
                                                int quantityBuying = Integer.parseInt(product[5].substring(product[5].indexOf(":") + 2));
                                                System.out.println("productname " + productName);
                                                System.out.println("storename " + storeName);


                                                ShoppingCartProduct productChosen = null;
                                                for (int i = 0; i < shoppingCart.size(); i++) {
                                                    if (shoppingCart.get(i).getProductName().equals(productName) &&
                                                            shoppingCart.get(i).getStoreName().equals(storeName) &&
                                                            shoppingCart.get(i).getQuantityBuying() == quantityBuying) {
                                                        productChosen = shoppingCart.get(i);
                                                    }
                                                }

                                                assert productChosen != null;


                                                shoppingCart.remove(productChosen);

                                                //save into file remove from shopping cart
                                                method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                                //remove shopping cart from data file


                                                //sending name back for GUI Message
                                            }

                                        }
                                        if (cartButtonClicked.equals("purchaseCart")) {
                                            shoppingCart = method.createShoppingCartArray(userAccount);
                                            Methods.productsOnMarket = method.makeProductArrayList();
                                            System.out.println("purchase cart pressed");

                                            StringBuilder purchasedCorrectly = new StringBuilder(" ");
                                            StringBuilder soldOut = new StringBuilder(" ");
                                            StringBuilder limitedQuantity = new StringBuilder(" ");

                                            for (int i = 0; i < shoppingCart.size(); i++) {

                                                if (shoppingCart.get(i).getQuantityAvailable() == 0) {
                                                    soldOut.append(shoppingCart.get(i).getProductName()).append(" sold out!, ");
                                                    System.out.println("sold out message " + soldOut);

                                                } else if (shoppingCart.get(i).getQuantityBuying() > shoppingCart.get(i).getQuantityAvailable()) {

                                                    limitedQuantity.append(shoppingCart.get(i).getProductName()).append(" ").append(shoppingCart.get(i).getQuantityAvailable()).append(" quantity available to purchase!, ");
                                                    System.out.println("limited quantity message " + limitedQuantity);


                                                    //finding shopping cart in products on market
                                                    Product purchaseInCart = null;
                                                    for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
                                                        if (Methods.productsOnMarket.get(k).getProductName().equals(shoppingCart.get(i).getProductName())
                                                                && Methods.productsOnMarket.get(k).getStoreName().equals(shoppingCart.get(i).getStoreName())) {
                                                            purchaseInCart = Methods.productsOnMarket.get(k);
                                                        }
                                                    }
                                                    method.purchaseProduct(purchaseInCart, shoppingCart.get(i).getQuantityAvailable());
                                                    assert purchaseInCart != null;
                                                    purchaseInCart.setQuantityAvailable(0);

                                                    ShoppingCartProduct updatedQuantity = shoppingCart.get(i);
                                                    updatedQuantity.setQuantityAvailable(0);

                                                    ///INSERT CUSTOMER RECIEPTS HERE


                                                    method.saveDataFileWhenPurchased(Methods.productsOnMarket, purchaseInCart);
                                                    method.saveDataFileCart(updatedQuantity);
                                                    method.saveProductFile(Methods.productsOnMarket);
                                                    //might not be needed as shopping cart will be cleared
                                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                                    //saves to file updating the item that get bought
                                                } else {
                                                    //INSERT CUSTOMER RECEIPTS SOMEWHERE HERE
                                                    purchasedCorrectly.append(shoppingCart.get(i).getProductName()).append(" purchased!, ");
                                                    System.out.println("purchased correctly message " + purchasedCorrectly);

                                                    Product purchaseInCart = null;
                                                    for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
                                                        if (Methods.productsOnMarket.get(k).getProductName().equals(shoppingCart.get(i).getProductName())
                                                                && Methods.productsOnMarket.get(k).getStoreName().equals(shoppingCart.get(i).getStoreName())) {
                                                            purchaseInCart = Methods.productsOnMarket.get(k);
                                                        }
                                                    }
                                                    //debug


                                                    method.purchaseProduct(purchaseInCart, shoppingCart.get(i).getQuantityBuying());
                                                    assert purchaseInCart != null;
                                                    shoppingCart.get(i).setQuantityAvailable(purchaseInCart.getQuantityAvailable());


                                                    System.out.println("product quantity after purchase " + purchaseInCart.getQuantityAvailable());


                                                    method.saveDataFileWhenPurchased(Methods.productsOnMarket, purchaseInCart);
                                                    method.saveDataFileCart(shoppingCart.get(i));
                                                    method.saveProductFile(Methods.productsOnMarket);
                                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);


                                                }


                                            }
                                            System.out.println("ser reach2");
                                            shoppingCart.clear();

                                            method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);

                                            writer.write(soldOut + "@@" + limitedQuantity + "@@" + purchasedCorrectly);
                                            writer.println();
                                            writer.flush();

                                        }
                                        method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                        method.saveProductFile(Methods.productsOnMarket);


                                    }
                                    if (buttonClicked.equals("editProfileButton")) {
                                        System.out.println("edit profile enter");
                                        String profileInfo = reader.readLine();

                                        if (profileInfo.equals("null")) {
                                            // exited out

                                        } else {
                                            String[] partsNew = profileInfo.split(",");

                                            String newUserName = partsNew[0];
                                            String newEmail = partsNew[1];
                                            String newPassword = partsNew[2];


                                            method.changeAccount(newUserName, newEmail, newPassword, userAccount);


                                            if (!newUserName.equals(userAccount.getName())) {
                                                userAccount.setName(newUserName);
                                            }
                                            if (!newEmail.equals(userAccount.getEmail())) {
                                                userAccount.setEmail(newEmail);
                                            }
                                            if (!newPassword.equals(userAccount.getPassword())) {
                                                userAccount.setPassword(newPassword);
                                            }
                                            System.out.println("successful edit");

                                        }

                                    }
                                    if (buttonClicked.equals("deleteAccount")) {
                                        method.removeAccount(userAccount);
                                    }


                                    //PROCESSING SIGN IN
                                }

                                // end
                            }
                        }
                    } else {
                        System.out.println("creating account");
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
