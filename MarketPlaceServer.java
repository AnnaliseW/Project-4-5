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


                            //PROCESSING SIGN IN
                        }

                        // end
                    }
                }
            } else {
                System.out.println("creating account");
            }

        }


    }
}
