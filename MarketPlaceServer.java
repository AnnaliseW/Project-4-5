import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MarketPlaceServer {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket socket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());


        //create market array list

        while (true) {

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
            GUICustomerView guiCustomerView = new GUICustomerView();

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

            //SEND SERVER USER INFO

            writer.write(userAccountString);
            writer.println();
            writer.flush();


            String sellerOrBuyer = reader.readLine();


            if (sellerOrBuyer.equals("seller")) {
                // PROCESSING FOR SELLER


            } else if (sellerOrBuyer.equals("buyer")) {
                boolean noExit = false;


                while (!noExit) {
                    assert userAccount != null;
                    ArrayList<ShoppingCartProduct> shoppingCart = guiCustomerView.createShoppingCartArray(userAccount);

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
                            if (buttonChosenStatistics.equals("null")) {

                            }

                            String amountPurchasingString = null;

                            if (buttonChosenStatistics.equals("buyProduct")) {
                                System.out.println("4: buy product");
                                Methods method = new Methods();


                                //able to purchase correct input amount purchasing
                                boolean loop = false;

                                int amountPurchasing = 0;


                                amountPurchasingString = reader.readLine();

                                System.out.println("amount purchase string " + amountPurchasingString);

                                if (!amountPurchasingString.equals("null")) {
                                    amountPurchasing = Integer.parseInt(amountPurchasingString);
                                    System.out.println("null");
                                }


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

                                    //writing back to client for message
                                    writer.write("purchaseLimit");
                                    writer.println();
                                    writer.flush();


                                    //purchasing product

                                    // must implement customer receipts ******

                                } else {
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
                                    method.saveDataFileWhenPurchased(Methods.productsOnMarket, productBought);
                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                    // must implement customer reciepts *****

                                    //writing back to client item purchase
                                    writer.write("itemPurchased");
                                    writer.println();
                                    writer.flush();
                                }


                            }
                            System.out.println("product quantity test " + productBought.getQuantityAvailable());
                            if (buttonChosenStatistics.equals("addToCart")) {
                                System.out.println("4: add cart ");
                                // add to cart button in client pressed


                                String quantityBuyingString = null;
                                int quantityBuying = 0;

                                quantityBuyingString = reader.readLine();
                                if (!quantityBuyingString.equals("null")) {
                                    quantityBuying = Integer.parseInt(quantityBuyingString);
                                    System.out.println("null");
                                }


                                String productName = productBought.getProductName();
                                String storeName = productBought.getStoreName();
                                String description = productBought.getDescriptionOfProduct();
                                int quantityAvailable = productBought.getQuantityAvailable();
                                double price = productBought.getPrice();

                                Methods method = new Methods();

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

                    if (buttonClicked.equals("searchedProductsButton")) {

                        Methods method = new Methods();
                        //search products button chosen


                        String wordSearch = reader.readLine();
                        if (wordSearch.isEmpty()) {

                        } else {
                            ArrayList<Product> searchedProducts = new ArrayList<>();
                            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                searchedProducts = method.searchForProduct(wordSearch);
                            }

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
                                String[] split = itemFromSearchChosen.split("@@");
                                String productName = split[0].substring(split[0].indexOf(":") + 2);
                                String storeName = split[1].substring(split[1].indexOf(":") + 2);


                                Product productBoughtNew = null;
                                boolean newPage = false;

                                for (int k = 0; k < searchedProducts.size(); k++) {
                                    if (searchedProducts.get(k).getProductName().equals(productName) &&
                                            searchedProducts.get(k).getStoreName().equals(storeName)) {
                                        productBoughtNew = searchedProducts.get(k);
                                        newPage = true;
                                        //product insight page
                                    }
                                }
                                if (newPage) {
                                    //PRODUCT INSIGHTS


                                    String chooseProduct = reader.readLine();
                                    //reads the chosen product

                                    for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                        if (Methods.productsOnMarket.get(i).statisticsToString().equals(chooseProduct)) {
                                            productBoughtNew = Methods.productsOnMarket.get(i);
                                        }
                                    }


                                    //OPENING NEW PANEL ON CLIENT SIDE WITH SHOPPING CART, BUY, PRODUCT STATISTICS
                                    String buttonChosenStatistics = reader.readLine();
                                    if (buttonChosenStatistics.equals("buyProduct")) {


                                        //able to purchase correct input amount purchasing
                                        int amountPurchasing = Integer.parseInt(reader.readLine());


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


                                            method.purchaseProduct(productBoughtNew, amountPurchasing);

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
                                            method.saveDataFileWhenPurchased(Methods.productsOnMarket, productBoughtNew);
                                            method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                            // must implement customer reciepts *****

                                            //writing back to client item purchase
                                            writer.write("itemPurchased");
                                            writer.println();
                                            writer.flush();

                                        }

                                    } else if (buttonChosenStatistics.equals("addToCart")) {
                                        // add to cart button in client pressed

                                        String quantityBuyingString = reader.readLine();
                                        int quantityBuying = Integer.parseInt(quantityBuyingString);


                                        assert productBoughtNew != null;
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
                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                    price, quantityAvailable);
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


                    }
                    if (buttonClicked.equals("null")) {

                    }
                    if (buttonClicked.equals("viewShoppingCart")) {
                        System.out.println("view shopping cart pressed");
                        //VIEW SHOPPING CART BUTTON
                        Methods method = new Methods();


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
                            System.out.println("purchase cart pressed");

                            StringBuilder purchasedCorrectly = new StringBuilder(" ");
                            StringBuilder soldOut = new StringBuilder(" ");
                            StringBuilder limitedQuantity = new StringBuilder(" ");

                            for (int i = 0; i < shoppingCart.size(); i++) {

                                if (shoppingCart.get(i).getQuantityAvailable() == 0) {
                                    soldOut.append(shoppingCart.get(i).getProductName()).append(" sold out!, ");

                                } else if (shoppingCart.get(i).getQuantityBuying() > shoppingCart.get(i).getQuantityAvailable()) {
                                    limitedQuantity.append(shoppingCart.get(i).getProductName()).append(" ").append(shoppingCart.get(i).getQuantityAvailable()).append(" quantity available to purchase!, ");


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
                                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                                    //saves to file updating the item that get bought
                                } else {
                                    //INSERT CUSTOMER RECEIPTS SOMEWHERE HERE
                                    purchasedCorrectly.append(shoppingCart.get(i).getProductName()).append(" purchased!, ");


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
                                    purchaseInCart.setQuantityAvailable(shoppingCart.get(i).getQuantityAvailable() - shoppingCart.get(i).getQuantityBuying());

                                    ShoppingCartProduct updatedQuantity = shoppingCart.get(i);
                                    updatedQuantity.setQuantityAvailable(shoppingCart.get(i).getQuantityAvailable() - shoppingCart.get(i).getQuantityBuying());


                                    System.out.println(purchaseInCart.getQuantityAvailable());
                                    System.out.println(updatedQuantity.getQuantityAvailable());


                                    assert purchaseInCart != null;
                                    method.saveDataFileWhenPurchased(Methods.productsOnMarket, purchaseInCart);
                                    System.out.println("shoppingCart.get(i): " + shoppingCart.get(i).getQuantityAvailable());
                                    method.saveDataFileCart(updatedQuantity);
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


                    //PROCESSING SIGN IN
                }

                // end
            }
        }


    }
}
