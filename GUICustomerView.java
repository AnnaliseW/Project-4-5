import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.StringJoiner;

import static javax.swing.JOptionPane.*;

public class GUICustomerView {
public ArrayList<SoldProduct> customerHistory = new ArrayList<>();
    public ArrayList<SoldProduct> getCustomerHistory(){
        return customerHistory;
    }
    
    public User returnUserAccount(String email, String password) {
        User userAccount = null;
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
                    break;
                } else {

                }
            }

            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userAccount;
    }


    public void createProductArray() {
        try {
            String[] arrayListOfProducts;
            //reading products from file into the product array list
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

        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }


    public ArrayList<ShoppingCartProduct> createShoppingCartArray(User userAccount) {
        String email = userAccount.getEmail();
        ArrayList<ShoppingCartProduct> shoppingCart = new ArrayList<>();
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
                            int quantityBuying = Integer.parseInt(separateParameters[5]);
                            //recreate product that is currently selling
                            ShoppingCartProduct eachProduct = new ShoppingCartProduct(productName,
                                    storeName, description, quantityAvailable, price, quantityBuying);

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
                                int quantityBuying = Integer.parseInt(separateParameters[5]);
                                //recreate product that is currently selling
                                ShoppingCartProduct eachProduct = new ShoppingCartProduct(productName,
                                        storeName, description, quantityAvailable, price, quantityBuying);

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

        return shoppingCart;
        //NEED TO FIGURE OUT WHY ITS NOT BEING POPULATED EVERYTIME

    }


    public void SeeProducts(User userAccount, ArrayList<ShoppingCartProduct> shoppingCart) {
        if (Methods.productsOnMarket.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products in the market!", "See Products", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int sortResponse = JOptionPane.showConfirmDialog(null, "Would you like to sort the market?",
                    "Sort Market", JOptionPane.YES_NO_OPTION);
            if (sortResponse == -1) {
                return;
            }
            if (sortResponse == NO_OPTION) {
                // no sort method
                boolean newPage = false;

                String[] products = new String[Methods.productsOnMarket.size()];
                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                    products[i] = Methods.productsOnMarket.get(i).statisticsToString();
                }

                String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                        "Market", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

                Product productBought = null;
                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                    if (Methods.productsOnMarket.get(i).statisticsToString().equals(chooseProduct)) {
                        productBought = Methods.productsOnMarket.get(i);
                    }
                }
                handleProductSelection(chooseProduct, productBought, newPage, userAccount, shoppingCart);


            } else if (sortResponse == YES_OPTION) {
                sortProducts(userAccount, shoppingCart);
            }
        }

    }

    // method to handle product selection
    private void handleProductSelection(String chooseProduct, Product productBought, boolean newPage, User userAccount, ArrayList<ShoppingCartProduct> shoppingCart) {
        for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
            if (Methods.productsOnMarket.get(k).statisticsToString().equals(chooseProduct)) {
                productBought = Methods.productsOnMarket.get(k);
                newPage = true;
                break;
            }
        }
        if (newPage) {
            productInsights(productBought, userAccount, shoppingCart);
        }
    }

    public void sortProducts(User userAccount, ArrayList<ShoppingCartProduct> shoppingCart) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JButton sortByMinPrice = new JButton("sort low -> high price");
        JButton sortByMaxPrice = new JButton("sort high -> low price");
        JButton sortByMinQuantity = new JButton("sort low -> high quantity");
        JButton sortByMaxQuantity = new JButton("sort high -> low quantity");


        panel.add(sortByMinQuantity);
        panel.add(sortByMaxQuantity);
        panel.add(sortByMinPrice);
        panel.add(sortByMaxPrice);

        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        Methods method = new Methods();

        boolean newPage = false;
        sortByMinQuantity.addActionListener(e -> {
            ArrayList<Product> copy = new ArrayList<>();
            copy = method.sortByMinQuantity(Methods.productsOnMarket);
            Methods.productsOnMarket.clear();
            Methods.productsOnMarket.addAll(copy);

            String[] products = new String[Methods.productsOnMarket.size()];
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                products[i] = Methods.productsOnMarket.get(i).statisticsToString();
            }

            // Move the product selection logic here
            String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                    "sort low -> high quantity", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

            Product productBought = null;
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                if (Methods.productsOnMarket.get(i).statisticsToString().equals(chooseProduct)) {
                    productBought = Methods.productsOnMarket.get(i);
                }
            }
            handleProductSelection(chooseProduct, productBought, newPage, userAccount, shoppingCart);
        });


        sortByMaxQuantity.addActionListener(e -> {
            ArrayList<Product> copy = new ArrayList<>();
            copy = method.sortByMaxQuantity(Methods.productsOnMarket);
            Methods.productsOnMarket.clear();
            Methods.productsOnMarket.addAll(copy);

            String[] products = new String[Methods.productsOnMarket.size()];
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                products[i] = Methods.productsOnMarket.get(i).statisticsToString();
            }

            String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                    "sort high -> low quantity", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
            Product productBought = null;
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                if (Methods.productsOnMarket.get(i).statisticsToString().equals(chooseProduct)) {
                    productBought = Methods.productsOnMarket.get(i);
                }
            }
            handleProductSelection(chooseProduct, productBought, newPage, userAccount, shoppingCart);
        });

        sortByMinPrice.addActionListener(e -> {
            ArrayList<Product> copy = new ArrayList<>();
            copy = method.sortByMinPrice(Methods.productsOnMarket);
            Methods.productsOnMarket.clear();
            Methods.productsOnMarket.addAll(copy);

            String[] products = new String[Methods.productsOnMarket.size()];
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                products[i] = Methods.productsOnMarket.get(i).statisticsToString();
            }

            String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                    "sort low -> high price", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

            Product productBought = null;
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                if (Methods.productsOnMarket.get(i).statisticsToString().equals(chooseProduct)) {
                    productBought = Methods.productsOnMarket.get(i);
                }
            }
            handleProductSelection(chooseProduct, productBought, newPage, userAccount, shoppingCart);
        });

        sortByMaxPrice.addActionListener(e -> {
            ArrayList<Product> copy = new ArrayList<>();
            copy = method.sortByMaxPrice(Methods.productsOnMarket);
            Methods.productsOnMarket.clear();
            Methods.productsOnMarket.addAll(copy);

            String[] products = new String[Methods.productsOnMarket.size()];
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                products[i] = Methods.productsOnMarket.get(i).statisticsToString();
            }

            String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                    "sort high -> low price", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

            Product productBought = null;
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                if (Methods.productsOnMarket.get(i).statisticsToString().equals(chooseProduct)) {
                    productBought = Methods.productsOnMarket.get(i);
                }
            }
            handleProductSelection(chooseProduct, productBought, newPage, userAccount, shoppingCart);
        });


    }


    public void productInsights(Product productBought, User userAccount, ArrayList<ShoppingCartProduct> shoppingCart) {
        JButton buyProduct;
        JButton addToCart;
        JButton exitButton;
        String productDescription = "<html>"
                + "Product Name: " + productBought.getProductName() + "<br>"
                + "Store: " + productBought.getStoreName() + "<br>"
                + "Description: " + productBought.getDescriptionOfProduct() + "<br>"
                + "Available Quantity: " + productBought.getQuantityAvailable() + "<br>"
                + "Price: " + productBought.getPrice() + "<br>"
                + "</html>";
        JLabel productStatistics = new JLabel(productDescription, JLabel.CENTER);
        productStatistics.setSize(600, 300);
        productStatistics.setHorizontalAlignment(JLabel.CENTER);
        JFrame productChosen = new JFrame();


        productChosen.setTitle(productBought.getProductName());
        productChosen.setSize(600, 300);
        productChosen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productChosen.setLocationRelativeTo(null);

        buyProduct = new JButton("Buy Product");
        addToCart = new JButton("Add Product to Cart");
        exitButton = new JButton("Exit");

        JPanel panels = new JPanel();
        productChosen.setLayout(new BorderLayout());


        productChosen.add(productStatistics, BorderLayout.CENTER);


        //TODO: Implement logic for buttons

        buyProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // implementation of buyProduct button


                //debugging to test if cancel or exit
                //boolean to see if shopping cart message and method will occur (if null or invalid it will not work)
                boolean purchaseMessage = true;
                int amountPurchasing = 0;
                String answer = "";
                try {
                    answer = JOptionPane.showInputDialog(null, "How many items would you like to purchase?",
                            "Buy Product", JOptionPane.QUESTION_MESSAGE);
                    if (answer == null) {
                        purchaseMessage = false;
                    } else {
                        amountPurchasing = Integer.parseInt(answer);
                    }


                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Please try again",
                            "Buy Product", JOptionPane.ERROR_MESSAGE);
                    purchaseMessage = false;
                }


                if (purchaseMessage) {
                    Methods method = new Methods();

                    if (productBought.getQuantityAvailable() == 0) {
                        JOptionPane.showMessageDialog(null, "Error! Product is sold out!",
                                "Buy Product", JOptionPane.ERROR_MESSAGE);
                    } else if (productBought.getQuantityAvailable() < amountPurchasing) {
                        // if more than quantity available
                        JOptionPane.showMessageDialog(null, "Could only purchase " + productBought.getQuantityAvailable() + " products!\n "
                                        + productBought.getProductName() + " purchased!",
                                "Buy Product", JOptionPane.INFORMATION_MESSAGE);

                        method.purchaseProduct(productBought, productBought.getQuantityAvailable());
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


                        //purchasing product

                        // must implement customer receipts ******

                    } else {
                        method.purchaseProduct(productBought, amountPurchasing);


                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " purchased!", "Buy Products",
                                JOptionPane.INFORMATION_MESSAGE);

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

                    }
                } else {
                    return;
                }


            }
        });

        addToCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                boolean shoppingCartMessage = true;
                Methods method = new Methods();
                // implementation of adding to shopping cart button
                int quantityBuying = 0;
                String answer = "";
                try {
                    answer = JOptionPane.showInputDialog(null, "How many items would you like to add to " +
                                    "your shopping cart?",
                            "Shopping Cart", JOptionPane.QUESTION_MESSAGE);
                    if (answer == null) {
                        shoppingCartMessage = false;

                    } else {
                        quantityBuying = Integer.parseInt(answer);
                    }


                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(null, "Invalid Input! Please try again",
                            "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                    shoppingCartMessage = false;

                }
                if (shoppingCartMessage) {


                    String productName = productBought.getProductName();
                    String storeName = productBought.getStoreName();
                    String description = productBought.getDescriptionOfProduct();
                    int quantityAvailable = productBought.getQuantityAvailable();
                    double price = productBought.getPrice();

                    ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                            price, quantityBuying);
                    shoppingCart.add(addedProductToCart);
                    JOptionPane.showMessageDialog(null, productBought.getProductName() + " added to your shopping cart!",
                            "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    return;
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Methods method = new Methods();
                method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                method.saveProductFile(Methods.productsOnMarket);
                productChosen.dispose();
            }
        });

        panels.add(buyProduct);
        panels.add(addToCart);
        panels.add(exitButton);


        productChosen.add(panels, BorderLayout.CENTER);
        productChosen.setVisible(true);

    }

    public void searchProducts(User userAccount, ArrayList<ShoppingCartProduct> shoppingCart) {
        Product productBought = null;
        boolean newPage = false;
        Methods method = new Methods();
        String wordSearch = null;
        try {
            wordSearch = JOptionPane.showInputDialog(null, "What word would you like to input to search?",
                    "Search Product", JOptionPane.QUESTION_MESSAGE);

            if (wordSearch == null) {
                return; // Exit the method
            }


            ArrayList<Product> searchedProducts = new ArrayList<>();
            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                searchedProducts = method.searchForProduct(wordSearch);
            }
            if (searchedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Sorry! No products matched your search!",
                        "Search Product", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String[] products = new String[searchedProducts.size()];
                for (int i = 0; i < searchedProducts.size(); i++) {
                    products[i] = searchedProducts.get(i).statisticsToString();
                }
                String itemFromSearchChosen = (String) JOptionPane.showInputDialog(null, "Products that matched your search!",
                        "See Products", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

                for (int k = 0; k < searchedProducts.size(); k++) {
                    if (searchedProducts.get(k).statisticsToString().equals(itemFromSearchChosen)) {
                        productBought = searchedProducts.get(k);
                        newPage = true;
                        break;
                        //product insight page
                    }
                }
                if (newPage) {
                    productInsights(productBought, userAccount, shoppingCart);

                }
            }
        } catch (NullPointerException f) {

        }

    }

    public void viewShoppingCart(User userAccount, ArrayList<ShoppingCartProduct> shoppingCart) {
        // must use shopping cart method in GUITEST
        //*** THIS STILL MUST IMPLEMENT CUSTOMER RECEIPTS WHEN PURCHASING
        if (shoppingCart.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nothing in your shopping cart!",
                    "Search Product", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JFrame frame = new JFrame();
            JPanel panel = new JPanel(new GridLayout(1, 3));
            JButton viewCart = new JButton("View shopping cart");
            JButton removeProduct = new JButton("Remove a product from cart");
            JButton purchaseCart = new JButton("Purchase items from cart");

            panel.add(viewCart);
            panel.add(removeProduct);
            panel.add(purchaseCart);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

            Methods method = new Methods();

            //viewing shopping cart button
            viewCart.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {


                    String[] products = new String[shoppingCart.size()];

                    for (int i = 0; i < shoppingCart.size(); i++) {
                        products[i] = shoppingCart.get(i).shoppingCartStatisticsToString();
                    }


                    String seeProduct = (String) JOptionPane.showInputDialog(null, "Shopping Cart!",
                            "View Cart", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                    if (seeProduct == null) {
                        return;
                    } else {
                        ShoppingCartProduct productChosen = null;
                        for (int i = 0; i < shoppingCart.size(); i++) {
                            if (shoppingCart.get(i).shoppingCartStatisticsToString().equals(seeProduct)) {
                                productChosen = shoppingCart.get(i);
                            }
                        }
                        //shows cart and which product if want to view statistic

                        String productDescription = "<html>"
                                + "Product Name: " + productChosen.getProductName() + "<br>"
                                + "Store: " + productChosen.getStoreName() + "<br>"
                                + "Description: " + productChosen.getDescriptionOfProduct() + "<br>"
                                + "Available Quantity: " + productChosen.getQuantityAvailable() + "<br>"
                                + "Price: " + productChosen.getPrice() + "<br>"
                                + "Quantity Buying: " + productChosen.getQuantityBuying() + "<br>"
                                + "</html>";
                        JLabel productStatistics = new JLabel(productDescription, JLabel.CENTER);
                        productStatistics.setSize(600, 300);
                        productStatistics.setHorizontalAlignment(JLabel.CENTER);
                        JFrame frame = new JFrame();
                        frame.setTitle(productChosen.getProductName());
                        frame.setSize(600, 300);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.add(productStatistics);
                        frame.setVisible(true);
                    }
                }
            });


            removeProduct.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    String[] products = new String[shoppingCart.size()];

                    for (int i = 0; i < shoppingCart.size(); i++) {
                        products[i] = shoppingCart.get(i).shoppingCartStatisticsToString();
                    }

                    // shows cart and choose which product to remove
                    String seeProduct = (String) JOptionPane.showInputDialog(null, "Remove Product From Cart!",
                            "Shopping Cart", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                    if (seeProduct == null) {
                        return;
                    }

                    ShoppingCartProduct productChosen = null;
                    for (int i = 0; i < shoppingCart.size(); i++) {
                        if (shoppingCart.get(i).shoppingCartStatisticsToString().equals(seeProduct)) {
                            productChosen = shoppingCart.get(i);
                        }
                    }


                    shoppingCart.remove(productChosen);
                    //save into file remove from shopping cart
                    method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                    //remove shopping cart from data file


                    JOptionPane.showMessageDialog(null, productChosen.getProductName() + " removed from cart!",
                            "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                    if (shoppingCart.isEmpty()) {
                        frame.dispose();
                    }
                }
            });

            purchaseCart.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    for (int i = 0; i < shoppingCart.size(); i++) {


                        if (shoppingCart.get(i).getQuantityAvailable() == 0) {
                            //item sold out
                            JOptionPane.showMessageDialog(null, shoppingCart.get(i).getProductName() + "is sold out! Not Purchased",
                                    "Purchase Cart", JOptionPane.ERROR_MESSAGE);


                        } else if (shoppingCart.get(i).getQuantityBuying() > shoppingCart.get(i).getQuantityAvailable()) {

                            JOptionPane.showMessageDialog(null, "Only " + shoppingCart.get(i).getQuantityAvailable() + " quantity " +
                                    "available to purhcase!", "Purchase Cart", JOptionPane.INFORMATION_MESSAGE);


                            //finding shopping cart in products on market
                            Product purchaseInCart = null;
                            for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
                                if (Methods.productsOnMarket.get(k).getProductName().equals(shoppingCart.get(i).getProductName())
                                        && Methods.productsOnMarket.get(k).getStoreName().equals(shoppingCart.get(i).getStoreName())) {
                                    purchaseInCart = Methods.productsOnMarket.get(k);
                                }
                            }
                            method.purchaseProduct(purchaseInCart, shoppingCart.get(i).getQuantityAvailable());

                            shoppingCart.get(i).setQuantityAvailable(0);
                            ///INSERT CUSTOMER RECIEPTS HERE

                            method.saveDataFileWhenPurchased(Methods.productsOnMarket, purchaseInCart);
                            method.saveDataFileCart(shoppingCart.get(i));
                            method.saveProductFile(Methods.productsOnMarket);
                            method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
                            //saves to file updating the item that get bought
                        } else {
                            //INSERT CUSTOMER RECEIPTS SOMEWHERE HERE


                            Product purchaseInCart = null;
                            for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
                                if (Methods.productsOnMarket.get(k).getProductName().equals(shoppingCart.get(i).getProductName())
                                        && Methods.productsOnMarket.get(k).getStoreName().equals(shoppingCart.get(i).getStoreName())) {
                                    purchaseInCart = Methods.productsOnMarket.get(k);
                                }
                            }
                            //debug

                            method.purchaseProduct(purchaseInCart, shoppingCart.get(i).getQuantityBuying());
                            shoppingCart.get(i).setQuantityAvailable(shoppingCart.get(i).getQuantityAvailable() - shoppingCart.get(i).getQuantityBuying());

                            assert purchaseInCart != null;
                            method.saveDataFileWhenPurchased(Methods.productsOnMarket, purchaseInCart);
                            method.saveDataFileCart(shoppingCart.get(i));
                            method.saveProductFile(Methods.productsOnMarket);
                            method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);

                            JOptionPane.showMessageDialog(null, purchaseInCart.getProductName() + " purchased!", "Purchase Cart", JOptionPane.INFORMATION_MESSAGE);
                        }
                        shoppingCart.clear();
                    }
                    //restarts shopping cart after purchasing all items
                    frame.dispose();
                }
            });
            method.saveShoppingCartArrayListToFile(shoppingCart, userAccount);
            method.saveProductFile(Methods.productsOnMarket);

        }

    }

     public void customerHistory(User user, ArrayList<SoldProduct> purchaseHistory) {
        Methods method = new Methods();
        method.saveCustomerHistory(purchaseHistory, user);
        if (purchaseHistory.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No History!",
                    "Purchase History", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            String[] products = new String[purchaseHistory.size()];
            for(int i = 0; i < purchaseHistory.size(); i++){
                products[i] = purchaseHistory.get(i).statisticsToString();
            }

            String product = (String) JOptionPane.showInputDialog(null, "Purchase History",
                    "Purchase History", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

            if(product == null){
                return;
            }
            else {
                SoldProduct productChosen = null;
                for(int i = 0; i < purchaseHistory.size(); i++){
                    if(purchaseHistory.get(i).statisticsToString().equals(product)){
                        productChosen = purchaseHistory.get(i);
                    }
                }

                String productDescription = "<html>"
                        + "Product Name: " + productChosen.getProductName() + "<br>"
                        + "Store: " + productChosen.getStoreName() + "<br>"
                        + "Description: " + productChosen.getDescriptionOfProduct() + "<br>"
                        + "Available Quantity: " + productChosen.getQuantityAvailable() + "<br>"
                        + "Price: " + productChosen.getPrice() + "<br>"
                        + "Quantity Buying: " + productChosen.getQuantityPurchased() + "<br>"
                        + "</html>";
                JLabel productStatistics = new JLabel(productDescription, JLabel.CENTER);
                productStatistics.setSize(600, 300);
                productStatistics.setHorizontalAlignment(JLabel.CENTER);
                JFrame frame = new JFrame();
                frame.setTitle(productChosen.getProductName());
                frame.setSize(600, 300);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.add(productStatistics);
                frame.setVisible(true);
            }
        }
    }


}
