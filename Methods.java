package Proj4;

import java.io.*;
import java.util.ArrayList;


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
            }
            else if (productsOnMarket.get(i).getStoreName().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the description
            }
            else if (productsOnMarket.get(i).getDescriptionOfProduct().toLowerCase().contains(searchWord.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);
            }
        }
        return searchedProducts;
    }


    public void purchaseProduct(Product product, int quantityPurchased) {
        for (int i = 0; i < productsOnMarket.size(); i++) {
            if (productsOnMarket.get(i).equals(product)) {
                if(productsOnMarket.get(i).getQuantityAvailable() == 0){
                    System.out.println("This item is sold out!");
                }
                productsOnMarket.get(i).setQuantityAvailable(productsOnMarket.get(i).getQuantityAvailable() - quantityPurchased);
                productsOnMarket.get(i).setQuantitySold(quantityPurchased);
            }
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
                        arrayList.get(i).getDescriptionOfProduct() + "," + arrayList.get(i).getQuantityAvailable() + "," +
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


    public void saveProductArrayList(ArrayList<Product> arrayList) {
        File dataFile = new File("productArrayList.txt");
        //array list to reprint the file

        ArrayList<String> allProducts = new ArrayList<>();
        String products;


        for (int i = 0; i < arrayList.size(); i++) {
            products = arrayList.get(i).getProductName() + "," + arrayList.get(i).getStoreName() + "," +
                    arrayList.get(i).getDescriptionOfProduct() + "," + arrayList.get(i).getQuantityAvailable() + "," +
                    arrayList.get(i).getPrice() + "@@";
            allProducts.add(products);
        }


        // Update the line with new information

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            for (int i = 0; i < allProducts.size(); i++) {
                pw.print(allProducts.get(i));
            }

            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
