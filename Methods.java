import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Methods {


    public static ArrayList<Product> productsOnMarket = new ArrayList<>();

    public static ArrayList<Product> getProductsOnMarket() {
        return productsOnMarket;
    }


    public static void setProductsOnMarket(ArrayList<Product> productsOnMarket) {
        Methods.productsOnMarket = productsOnMarket;
    }


    // this method searches for a product by what they input


    public ArrayList<Product> searchForProduct(String searchWord, ArrayList<Product> arrayList) {
        ArrayList<Product> searchedProducts = new ArrayList<>();
        Product similarProduct;
        // checking if it matches anything from the product name
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getProductName().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = arrayList.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the store name
            } else if (arrayList.get(i).getStoreName().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = arrayList.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the description
            } else if (arrayList.get(i).getDescriptionOfProduct().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = arrayList.get(i);
                searchedProducts.add(similarProduct);
            }
        }
        return searchedProducts;
    }


    public String findingStoreNamesForSeller(User user) {

        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String allStoreNames = "";
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }
            String sellerEmail = user.getEmail();

            for (int i = 0; i < allUserData.size(); i++) {
                String[] findUserEmail = allUserData.get(i).split(",");
                if (findUserEmail[1].equals(sellerEmail)) {
                    //finding line of user
                    String[] findProducts = allUserData.get(i).split(";");
                    if (findProducts.length == 1) {
                        // no products selling
                    } else {
                        String[] eachProduct = findProducts[1].split("@@");
                        String findingIfStoreNameAlreadyExists = "";
                        String oneStoreName = null;
                        for (int k = 0; k < eachProduct.length; k++) {
                            //iterating through every product
                            String[] findingStoreName = eachProduct[k].split(",");
                            oneStoreName = "";
                            if (!findingIfStoreNameAlreadyExists.contains(findingStoreName[1])) {
                                oneStoreName = findingStoreName[1];
                                findingIfStoreNameAlreadyExists += findingStoreName[1];
                                //not already in list
                                allStoreNames += oneStoreName + ",";
                            }

                        }

                    }

                } else {
                    // not the user
                }

            }

            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allStoreNames;

    }
    ///must check in market if storenames is empty there not listi
    // ng any products

    public String viewShoppingCarts(String storeNames) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String totalMessage = "";
        int customerCount = 0;

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

                    //more than one item in shopping cart

                    String[] productsInCart = shoppingCart[1].split("@@");
                    if (productsInCart.length > 1) {
                        customerCount++;
                        for (int k = 0; k < productsInCart.length; k++) {
                            String[] eachProduct = productsInCart[k].split(",");
                            if (storeNames.contains(eachProduct[1])) {

                                System.out.printf("\nCustomer " + customerCount + "\nProduct Name: " + eachProduct[0] + "\nStore Name: "
                                        + eachProduct[1] + "\nDescription: " + eachProduct[2] + "\nQuantity Available: "
                                        + eachProduct[3] + "\nPrice : $%.2f" + "\nQuantity Buying in Cart: " + eachProduct[5].replace("@@", "")
                                        + "\n", Double.parseDouble(eachProduct[4]));


                            }
                        }


                    } else {
                        customerCount++;
                        String[] eachProduct = shoppingCart[1].split(",");
                        if (storeNames.contains(eachProduct[1])) {
                            totalMessage += String.format("\nCustomer " + customerCount + "\nProduct Name: " + eachProduct[0] + "\nStore Name: "
                                    + eachProduct[1] + "\nDescription: " + eachProduct[2] + "\nQuantity Available: "
                                    + eachProduct[3] + "\nPrice : $%.2f" + "\nQuantity Buying in Cart: " + eachProduct[5].replace("@@", "")
                                    + "\n", Double.parseDouble(eachProduct[4]));


                        }


                    }

                }


            } else {
                // not a customer
            }
        }
        if (totalMessage.isEmpty()) {
            return "No customers have any items in cart!";
        } else {
            return totalMessage;
        }

    }


    public void purchaseProduct(Product product, int quantityPurchased) {
        for (int i = 0; i < productsOnMarket.size(); i++) {
            if (productsOnMarket.get(i).getStoreName().equals(product.getStoreName()) &&
                    productsOnMarket.get(i).getProductName().equals(product.getProductName())) {
                if (productsOnMarket.get(i).getQuantityAvailable() == 0) {
                    System.out.println("This item is sold out!\n");

                } else {
                    productsOnMarket.get(i).setQuantityAvailable(productsOnMarket.get(i).getQuantityAvailable()
                            - quantityPurchased);
                }
            }
        }
    }


    public void removeAccount(User user) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String currentEmail = user.getPassword();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (int i = 0; i < allUserData.size(); i++) {
                String[] eachUserData = allUserData.get(i).split(",");
                if (eachUserData[1].equals(currentEmail)) {
                    allUserData.remove(i);
                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < allUserData.size(); i++) {
                pw.println(allUserData.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String newPassword, User user) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String currentEmail = user.getEmail();
        String updatedString = "";
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (int i = 0; i < allUserData.size(); i++) {
                String[] eachUserData = allUserData.get(i).split(",");
                if (eachUserData[1].equals(currentEmail)) {
                    String[] productsLine = allUserData.get(i).split(";");
                    updatedString = user.getName() + "," + user.getEmail() + "," + newPassword
                            + "," + user.isSeller();
                    allUserData.set(i, allUserData.get(i).replace(productsLine[0], updatedString));
                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < allUserData.size(); i++) {
                pw.println(allUserData.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //must change it for the server or client

    }

    public void changeUserName(String newUserName, User user) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String currentUserName = user.getEmail();
        String updatedString = "";
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (int i = 0; i < allUserData.size(); i++) {
                String[] eachUserData = allUserData.get(i).split(",");
                if (eachUserData[1].equals(currentUserName)) {
                    String[] productsLine = allUserData.get(i).split(";");
                    updatedString = newUserName + "," + user.getEmail() + "," + user.getPassword()
                            + "," + user.isSeller();
                    allUserData.set(i, allUserData.get(i).replace(productsLine[0], updatedString));

                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < allUserData.size(); i++) {
                pw.println(allUserData.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAccount(String newUserName, String newUserEmail, String newPassword, User user) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String currentEmail = user.getEmail();
        String updatedString = "";


        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (int i = 0; i < allUserData.size(); i++) {
                String[] eachUserData = allUserData.get(i).split(",");
                if (eachUserData[1].equals(currentEmail)) {
                    String[] productsLine = allUserData.get(i).split(";");
                    updatedString = newUserName + "," + newUserEmail + "," + newPassword
                            + "," + user.isSeller();
                    allUserData.set(i, allUserData.get(i).replace(productsLine[0], updatedString));
                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < allUserData.size(); i++) {
                pw.println(allUserData.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    ///method to print and re-update product arrray lists (for sales or shopping cart)

    public void saveArrayListToFile(ArrayList<Product> arrayList, User user) {
        File dataFile = new File("data.txt");
        //array list to reprint the file
        ArrayList<String> updatedContent = new ArrayList<>();
        String firstPart = user.getName() + "," + user.getEmail() + "," + user.getPassword() + "," +
                user.isSeller() + ";";

        ArrayList<String> allProducts = new ArrayList<>();

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            String products;
            for (int i = 0; i < arrayList.size(); i++) {
                products = arrayList.get(i).getProductName() + "," + arrayList.get(i).getStoreName() + "," +
                        arrayList.get(i).getDescriptionOfProduct() + "," +
                        arrayList.get(i).getQuantityAvailable() + "," +
                        arrayList.get(i).getPrice() + "@@";
                allProducts.add(products);
            }

            while ((line = bfr.readLine()) != null) {
                if (!line.contains(user.getEmail())) {
                    updatedContent.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Update the line with new information

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            pw.print(firstPart);
            for (int i = 0; i < allProducts.size(); i++) {
                pw.print(allProducts.get(i));
            }
            pw.println();
            // Append the modified lines (including the updated line)
            for (int i = 0; i < updatedContent.size(); i++) {
                pw.println(updatedContent.get(i));
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveShoppingCartArrayListToFile(ArrayList<ShoppingCartProduct> arrayList, User user) {
        File dataFile = new File("data.txt");
        //array list to reprint the file
        ArrayList<String> updatedContent = new ArrayList<>();
        String firstPart = user.getName() + "," + user.getEmail() + "," + user.getPassword() + "," +
                user.isSeller() + ";";

        ArrayList<String> allProducts = new ArrayList<>();

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            String products;
            for (int i = 0; i < arrayList.size(); i++) {
                products = arrayList.get(i).getProductName() + "," + arrayList.get(i).getStoreName() + "," +
                        arrayList.get(i).getDescriptionOfProduct() + "," +
                        arrayList.get(i).getQuantityAvailable() + "," +
                        arrayList.get(i).getPrice() + "," + arrayList.get(i).getQuantityBuying() + "@@";
                allProducts.add(products);
            }

            while ((line = bfr.readLine()) != null) {
                if (!line.contains(user.getEmail())) {
                    updatedContent.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Update the line with new information


        try {
            System.out.println("saveShoppingCartArrayListToFile test");
            allProducts.forEach(System.out::println);
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            pw.print(firstPart);
            for (int i = 0; i < allProducts.size(); i++) {
                pw.print(allProducts.get(i));
            }
            pw.println();
            // Append the modified lines (including the updated line)
            for (int i = 0; i < updatedContent.size(); i++) {
                pw.println(updatedContent.get(i));
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveDataFileWhenPurchased(ArrayList<Product> arrayList, Product updatedProduct) {
        File dataFile = new File("data.txt");
        System.out.println(updatedProduct.getQuantityAvailable());
        // array list to reprint the file
        ArrayList<String> allUserData = new ArrayList<>();

        String storeName = updatedProduct.getStoreName();
        String productName = updatedProduct.getProductName();
        String quantity = String.valueOf(updatedProduct.getQuantityAvailable());
        System.out.println("quantity test" + quantity);
        String price = String.valueOf(updatedProduct.getPrice());

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (int i = 0; i < allUserData.size(); i++) {
                String[] checkIfSeller = allUserData.get(i).split(",");
                if (checkIfSeller[3].startsWith("true")) {
                    // if seller there is only this amount of index
                    String[] afterSemiColon = allUserData.get(i).split(";");
                    if (allUserData.get(i).contains("@@")) {
                        String[] separtedByProduct = afterSemiColon[1].split("@@");
                        for (int k = 0; k < separtedByProduct.length; k++) {
                            System.out.println("testtest");
                            String[] findStoreName = separtedByProduct[k].split(",");
                            System.out.println("store Name " + storeName + "product name " + productName);
                            System.out.println("store Name " + findStoreName[1] + "product name" + findStoreName[0]);
                            if (findStoreName[1].equals(storeName) && findStoreName[0].equals(productName)) {
                                allUserData.set(i, allUserData.get(i).replace(findStoreName[3], quantity));
                                System.out.println("Updated line: " + allUserData.get(i));
                            }
                        }
                    }
                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update the line with new information

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            System.out.println("testing user data update in data.txt");
            for (int i = 0; i < allUserData.size(); i++) {
                pw.println(allUserData.get(i));
                System.out.println(allUserData.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveDataFileCart(ShoppingCartProduct updatedProduct) {
        File dataFile = new File("data.txt");
        // array list to reprint the file
        ArrayList<String> allUserData = new ArrayList<>();

        String storeName = updatedProduct.getStoreName();
        String productName = updatedProduct.getProductName();
        String description = updatedProduct.getDescriptionOfProduct();
        String quantityAvailable = String.valueOf(updatedProduct.getQuantityAvailable());
        String price = String.valueOf(updatedProduct.getPrice());
        String quantityBuying = String.valueOf(updatedProduct.getQuantityBuying());

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }
            String updated;

            for (int i = 0; i < allUserData.size(); i++) {
                String[] checkIfSeller = allUserData.get(i).split(",");
                if (checkIfSeller[3].startsWith("true")) {
                    // if seller there is only this amount of index
                    String[] afterSemiColon = allUserData.get(i).split(";");
                    if (allUserData.get(i).contains("@@")) {
                        String[] separtedByProduct = afterSemiColon[1].split("@@");
                        for (int k = 0; k < separtedByProduct.length; k++) {
                            String[] findStoreName = separtedByProduct[k].split(",");
                            if (findStoreName[1].equals(storeName) && findStoreName[0].equals(productName) && findStoreName.length == 6) {
                                updated = storeName + "," + productName + ","
                                        + description + "," + quantityAvailable + "," + price + "," + quantityBuying;
                                allUserData.set(i, allUserData.get(i).replace(separtedByProduct[k], updated));
                            }
                        }
                    }
                }
            }

            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update the line with new information

        try {
            System.out.println("saveDataFileCart test");
            allUserData.forEach(System.out::println);
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            for (int i = 0; i < allUserData.size(); i++) {
                pw.println(allUserData.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveProductFile(ArrayList<Product> arrayList) {
        File dataFile = new File("productArrayList.txt");
        //array list to reprint the file
        ArrayList<String> updatedContent = new ArrayList<>();

        ArrayList<String> allProducts = new ArrayList<>();
        String products;
        for (int i = 0; i < arrayList.size(); i++) {
            products = arrayList.get(i).getProductName() + "," + arrayList.get(i).getStoreName() + "," +
                    arrayList.get(i).getDescriptionOfProduct() + "," +
                    arrayList.get(i).getQuantityAvailable() + "," +
                    arrayList.get(i).getPrice() + "@@";
            allProducts.add(products);
        }


        // Update the line with new information

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            for (int i = 0; i < allProducts.size(); i++) {
                pw.print(allProducts.get(i));
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Product> sortByMinPrice(ArrayList<Product> productsArrayList) {
        ArrayList<Product> copyOfProducts = new ArrayList<>(productsArrayList);

        ArrayList<Product> newProductsArrayList = new ArrayList<>();
        while (productsArrayList.size() != newProductsArrayList.size()) {
            double min = Integer.MAX_VALUE;
            for (int i = 0; i < copyOfProducts.size(); i++) {
                if (copyOfProducts.get(i).getPrice() < min) {
                    min = copyOfProducts.get(i).getPrice();
                }
            }

            for (int k = 0; k < copyOfProducts.size(); k++) {
                if (copyOfProducts.get(k).getPrice() == min) {
                    newProductsArrayList.add(copyOfProducts.get(k));
                    copyOfProducts.remove(copyOfProducts.get(k));
                }
            }
        }

        return newProductsArrayList;
    }

    public ArrayList<Product> sortByMaxPrice(ArrayList<Product> productsArrayList) {
        ArrayList<Product> copyOfProducts = new ArrayList<>(productsArrayList);

        ArrayList<Product> newProductsArrayList = new ArrayList<>();
        while (productsArrayList.size() != newProductsArrayList.size()) {
            double max = Integer.MIN_VALUE;
            for (int i = 0; i < copyOfProducts.size(); i++) {
                if (copyOfProducts.get(i).getPrice() > max) {
                    max = copyOfProducts.get(i).getPrice();
                }
            }

            for (int k = 0; k < copyOfProducts.size(); k++) {
                if (copyOfProducts.get(k).getPrice() == max) {
                    newProductsArrayList.add(copyOfProducts.get(k));
                    copyOfProducts.remove(copyOfProducts.get(k));
                }
            }
        }

        return newProductsArrayList;
    }

    public ArrayList<Product> makeProductArrayList() {
        ArrayList<Product> products = null;
        try {
            products = new ArrayList<>();
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
                    products.add(eachProduct);
                }
            }
            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }


    public ArrayList<Product> sortByMinQuantity(ArrayList<Product> productsArrayList) {
        ArrayList<Product> copyOfProducts = new ArrayList<>(productsArrayList);

        ArrayList<Product> newProductsArrayList = new ArrayList<>();
        while (productsArrayList.size() != newProductsArrayList.size()) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < copyOfProducts.size(); i++) {
                if (copyOfProducts.get(i).getQuantityAvailable() < min) {
                    min = copyOfProducts.get(i).getQuantityAvailable();
                }
            }

            for (int k = 0; k < copyOfProducts.size(); k++) {
                if (copyOfProducts.get(k).getQuantityAvailable() == min) {
                    newProductsArrayList.add(copyOfProducts.get(k));
                    copyOfProducts.remove(copyOfProducts.get(k));
                }
            }
        }

        return newProductsArrayList;
    }


    public ArrayList<Product> sortByMaxQuantity(ArrayList<Product> productsArrayList) {
        ArrayList<Product> copyOfProducts = new ArrayList<>(productsArrayList);

        ArrayList<Product> newProductsArrayList = new ArrayList<>();
        while (productsArrayList.size() != newProductsArrayList.size()) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < copyOfProducts.size(); i++) {
                if (copyOfProducts.get(i).getQuantityAvailable() > max) {
                    max = copyOfProducts.get(i).getQuantityAvailable();
                }
            }

            for (int k = 0; k < copyOfProducts.size(); k++) {
                if (copyOfProducts.get(k).getQuantityAvailable() == max) {
                    newProductsArrayList.add(copyOfProducts.get(k));
                    copyOfProducts.remove(copyOfProducts.get(k));
                }
            }
        }

        return newProductsArrayList;
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

    }

    public ArrayList<SoldProduct> makeCustomerHistory(User user) {
        ArrayList<SoldProduct> customerHistory = new ArrayList<>();
        BufferedReader bfr;
        ArrayList<String> historyData = new ArrayList<>();
        try {
            String line;
            bfr = new BufferedReader(new FileReader("customerHistory.txt"));
            while ((line = bfr.readLine()) != null) {
                historyData.add(line);
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int index = -1;
        for (int i = 0; i < historyData.size(); i++) {
            if (user.getEmail().equals(historyData.get(i).substring(0, historyData.get(i).indexOf(':')))) {
                index = i;
                if (index != -1) {
                    historyData.set(index, historyData.get(index).substring(historyData.get(index).indexOf(':') + 1));
                }
            }
        }

        if (index != -1) {
            String[] eachProduct = historyData.get(index).split("-");
            String[] oneProduct;

            for (int i = 0; i < eachProduct.length - 1; i++) {
                oneProduct = eachProduct[i].split(",");
                customerHistory.add(new SoldProduct(oneProduct[0], oneProduct[1], oneProduct[2], Integer.parseInt(oneProduct[3]),
                        Double.parseDouble(oneProduct[4]), Integer.parseInt(oneProduct[5])));
            }
        }
        return customerHistory;
    }

    public void saveCustomerHistory(ArrayList<SoldProduct> customerHistory, User user) {
        //email:prod1,store1,desc1,available,price,purchased-
        ArrayList<String> data = new ArrayList<>();
        BufferedReader bfr = null;
        try {
            String line;
            bfr = new BufferedReader(new FileReader("customerHistory.txt"));
            while ((line = bfr.readLine()) != null) {
                data.add(line);
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            if (user.getEmail().equals(data.get(i).substring(0, data.get(i).indexOf(':')))) {
                index = i;
            }
        }

        String productList = "";
        for (int i = 0; i < customerHistory.size(); i++) {
            productList += customerHistory.get(i).getProductName() + "," + customerHistory.get(i).getStoreName() + "," +
                    customerHistory.get(i).getDescriptionOfProduct() + "," + customerHistory.get(i).getQuantityAvailable() + "," +
                    customerHistory.get(i).getPrice() + "," + customerHistory.get(i).getQuantityPurchased() + "-";
        }
        if (index == -1) {
            data.add(user.getEmail() + ":" + productList);
        } else {
            data.set(index, user.getEmail() + ":" + productList);
        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter("customerHistory.txt");
            for (int i = 0; i < data.size(); i++) {
                pw.println(data.get(i) + ";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.close();
    }

    public ArrayList<Product> generateMyProducts(User account) {
        ArrayList<Product> itemsSoldBySeller = new ArrayList<>();

        try {
            BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
            String line = "";
            ArrayList<String> allUserData = new ArrayList<>();

            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (String userData : allUserData) {
                String[] oneUserData = userData.split(",");
                if (oneUserData.length > 1 && oneUserData[1].equals(account.getEmail())) {
                    String[] separatingForSalesArray = userData.split(";");
                    if (separatingForSalesArray.length > 1) {
                        if (!separatingForSalesArray[1].contains("@@")) {
                            String[] separateParameters = separatingForSalesArray[1].split(",");
                            //productName, storeName, description, quantityAvailable, price
                            if (separateParameters.length >= 5) {
                                String productName = separateParameters[0];
                                String storeName = separateParameters[1];
                                String description = separateParameters[2];
                                int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                double price = Double.parseDouble(separateParameters[4]);
                                Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);
                                itemsSoldBySeller.add(eachProduct);
                            }
                        } else {
                            String[] arrayListSalesProducts = separatingForSalesArray[1].split("@@");
                            for (String productData : arrayListSalesProducts) {
                                String[] separateParameters = productData.split(",");
                                if (separateParameters.length >= 5) {
                                    String productName = separateParameters[0];
                                    String storeName = separateParameters[1];
                                    String description = separateParameters[2];
                                    int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                    double price = Double.parseDouble(separateParameters[4]);
                                    Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);
                                    itemsSoldBySeller.add(eachProduct);
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
        return itemsSoldBySeller;
    }


    public void saveDataFileWhenNewProductAddedUserAccount(User userAccount, Product newProduct) {
        File dataFile = new File("data.txt");
        // format: eraser,purdue,it is a small eraser,5,2.0@@

        String productStatistics = newProduct.getProductName() + "," + newProduct.getStoreName() + "," +
                newProduct.getDescriptionOfProduct() + "," + newProduct.getQuantityAvailable() + "," + newProduct.getPrice()
                + "@@";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));

            ArrayList<String> allLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            reader.close();

            for (int i = 0; i < allLines.size(); i++) {
                String userData = allLines.get(i);
                String[] oneUserData = userData.split(",");
                if (oneUserData.length > 1 && oneUserData[1].equals(userAccount.getEmail())) {
                    allLines.set(i, userData + productStatistics);
                    break;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
            for (String updatedLine : allLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void replaceProductInDataFile(Product oldProduct, Product newProduct) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }
            bfr.close();

            for (int i = 0; i < allUserData.size(); i++) {
                String[] checkIfSeller = allUserData.get(i).split(",");
                if (checkIfSeller.length > 3 && checkIfSeller[3].startsWith("true")) {
                    String[] afterSemiColon = allUserData.get(i).split(";");
                    if (allUserData.get(i).contains("@@")) {
                        String[] separatedByProduct = afterSemiColon[1].split("@@");
                        for (int k = 0; k < separatedByProduct.length; k++) {
                            String[] findProduct = separatedByProduct[k].split(",");
                            if (findProduct.length >= 2 && findProduct[1].equals(oldProduct.getProductName())
                                    && findProduct[0].equals(oldProduct.getStoreName())) {
                                String updated = newProduct.getStoreName() + "," + newProduct.getProductName() + ","
                                        + newProduct.getDescriptionOfProduct() + "," + newProduct.getQuantityAvailable()
                                        + "," + newProduct.getPrice();
                                allUserData.set(i, allUserData.get(i).replace(separatedByProduct[k], updated));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            for (String line : allUserData) {
                pw.println(line);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void removeProductFromDataFile(User userAccount, Product productToRemove) {
        File dataFile = new File("data.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));

            ArrayList<String> allLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            reader.close();

            for (int i = 0; i < allLines.size(); i++) {
                String userData = allLines.get(i);
                String[] oneUserData = userData.split(",");
                if (oneUserData.length > 1 && oneUserData[1].equals(userAccount.getEmail())) {
                    String productStringToRemove = productToRemove.toString();
                    if (userData.contains(productStringToRemove)) {
                        String updatedUserData = userData.replace("@" + productStringToRemove, "");
                        allLines.set(i, updatedUserData);
                        break;
                    }
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
            for (String updatedLine : allLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveDataFileWithNewProductList(User userAccount, ArrayList<Product> newProductList) {
        File file = new File("data.txt");
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String[] userParts = parts[0].split(",");

                if (userParts[1].equals(userAccount.getEmail())) {
                    line = userParts[0] + "," + userParts[1] + "," + userParts[2] + ","
                            + userParts[3] + ";";

                    for (Product product : newProductList) {
                        line += product.getProductName() + ","
                                + product.getStoreName() + ","
                                + product.getDescriptionOfProduct() + ","
                                + product.getQuantityAvailable() + ","
                                + product.getPrice() + "@@";
                    }
                }

                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteForProductFile(Product itemToDelete) {
        File dataFile = new File("productArrayList.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();

        //eraser,purdue,it is a small eraser,5,2.0@@ format


        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (int i = 0; i < allUserData.size(); i++) {
                String[] eachProduct = allUserData.get(i).split("@@");
                for (int k = 0; k < eachProduct.length; k++) {
                    String[] findingItem = eachProduct[k].split(",");
                    if (findingItem[0].equals(itemToDelete.getProductName()) &&
                            findingItem[1].equals(itemToDelete.getStoreName())) {
                    } else {
                        products.add(eachProduct[k] + "@@");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            for (int i = 0; i < products.size(); i++) {
                pw.print(products.get(i));
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
