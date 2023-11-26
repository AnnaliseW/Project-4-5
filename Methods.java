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


    public ArrayList<Product> searchForProduct(String searchWord) {
        ArrayList<Product> searchedProducts = new ArrayList<>();
        Product similarProduct;
        // checking if it matches anything from the product name
        for (int i = 0; i < productsOnMarket.size(); i++) {
            if (productsOnMarket.get(i).getProductName().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the store name
            } else if (productsOnMarket.get(i).getStoreName().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the description
            } else if (productsOnMarket.get(i).getDescriptionOfProduct().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
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
            if (productsOnMarket.get(i).equals(product)) {
                if (productsOnMarket.get(i).getQuantityAvailable() == 0) {
                    System.out.println("This item is sold out!\n");

                } else {
                    productsOnMarket.get(i).setQuantityAvailable(productsOnMarket.get(i).getQuantityAvailable()
                            - quantityPurchased);
                    productsOnMarket.get(i).setQuantitySold(quantityPurchased);
                }
            }
        }
        saveDataFileWhenPurchased(Methods.productsOnMarket, product);
    }

    public void sellProduct(Product product) {
        productsOnMarket.add(product);
        saveDataFileWhenNewProductAdded(productsOnMarket, product);
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
        String updatedString = null;
        ArrayList<String> printedOut = new ArrayList<>();
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
                    updatedString += productsLine[1];
                } else {
                    printedOut.add(allUserData.get(i));

                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printedOut.add(updatedString);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < printedOut.size(); i++) {
                pw.println(printedOut.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeUserName(String newUserName, User user) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String currentUserName = user.getEmail();
        String updatedString = null;
        ArrayList<String> printedOut = new ArrayList<>();
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
                    updatedString += productsLine[1];
                } else {
                    printedOut.add(allUserData.get(i));

                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printedOut.add(updatedString);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < printedOut.size(); i++) {
                pw.println(printedOut.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeUserEmail(String newUserEmail, User user) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String currentEmail = user.getEmail();
        String updatedString = null;
        ArrayList<String> printedOut = new ArrayList<>();
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
                    updatedString = user.getName() + "," + newUserEmail + "," + user.getPassword()
                            + "," + user.isSeller();
                    updatedString += productsLine[1];
                } else {
                    printedOut.add(allUserData.get(i));

                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printedOut.add(updatedString);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < printedOut.size(); i++) {
                pw.println(printedOut.get(i));
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
        // array list to reprint the file
        ArrayList<String> updatedContent = new ArrayList<>();
        ArrayList<String> allUserData = new ArrayList<>();
        String products;
        ArrayList<String> allProducts = new ArrayList<>();
        String updatedLine;


        String storeName = updatedProduct.getStoreName();
        String productName = updatedProduct.getProductName();
        String description = updatedProduct.getDescriptionOfProduct();
        String quantity = String.valueOf(updatedProduct.getQuantityAvailable());
        String price = String.valueOf(updatedProduct.getPrice());

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
                            if (findStoreName.length >= 2 && findStoreName[1].equals(storeName) && findStoreName[0].equals(productName)) {
                                updated = storeName + "," + productName + ","
                                        + description + "," + quantity + "," + price;
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

    public void saveDataFileWhenNewProductAdded(ArrayList<Product> arrayList, Product newProduct) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();
        String products;

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

        String storeName = newProduct.getStoreName();
        String productName = newProduct.getProductName();
        String description = newProduct.getDescriptionOfProduct();
        String quantity = String.valueOf(newProduct.getQuantityAvailable());
        String price = String.valueOf(newProduct.getPrice());

        String newProductData = storeName + "," + productName + ","
                + description + "," + quantity + "," + price;

        allUserData.add(newProductData);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            for (String userData : allUserData) {
                pw.println(userData);
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
        ArrayList<String> updatedContent = new ArrayList<>();
        ArrayList<String> allUserData = new ArrayList<>();
        String products;
        ArrayList<String> allProducts = new ArrayList<>();
        String updatedLine;


        String storeName = updatedProduct.getStoreName();
        String productName = updatedProduct.getProductName();
        String description = updatedProduct.getDescriptionOfProduct();
        String quantity = String.valueOf(updatedProduct.getQuantityAvailable());
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
                            if (findStoreName.length >= 2 && findStoreName[1].equals(storeName) && findStoreName[0].equals(productName)) {
                                updated = storeName + "," + productName + ","
                                        + description + "," + quantity + "," + price + "," + quantityBuying;
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



    //NEW VERSION
    //JUST STORES ALL THE ITEMBOUGHT OBJECTS IN LARGE TEXT FILE, NO ORDER
    //CAN GO THROUGH AND FIND THE ONES THAT ARE UNDER THE SELLER EMAIL

    public void addItemBoughtToFile(ItemBought itemBought) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("salesByStore.txt", true))) {
            bw.write(itemBought.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ItemBought> getItemsBySellerEmail(String sellerEmail) {
        ArrayList<ItemBought> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("salesByStore.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(", ");
                String buyerName = values[0].split("=")[1];
                String buyerEmail = values[1].split("=")[1];
                String currentSellerEmail = values[2].split("=")[1];
                String storeName = values[3].split("=")[1];
                String productName = values[4].split("=")[1];
                double productPrice = Double.parseDouble(values[5].split("=")[1]);
                int quantityBought = Integer.parseInt(values[6].split("=")[1]);

                if (currentSellerEmail.equals(sellerEmail)) {
                    ItemBought itemBought = new ItemBought(buyerName, buyerEmail, currentSellerEmail, storeName, productName, productPrice, quantityBought);
                    result.add(itemBought);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
