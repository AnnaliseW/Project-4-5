import java.io.*;
import java.util.ArrayList;


public class Methods {


    public ArrayList<Product> productsOnMarket = new ArrayList<>();

    public ArrayList<Product> getProductsOnMarket() {
        return productsOnMarket;
    }


    public void setProductsOnMarket(ArrayList<Product> productsOnMarket) {
        this.productsOnMarket = productsOnMarket;
    }


    // this method searches for a product by what they input

    public ArrayList<Product> searchForProduct(String nameOfProduct, String nameOfStore, String description) {
        ArrayList<Product> searchedProducts = new ArrayList<>();
        Product similarProduct;
        // checking if it matches anything from the product name
        for (int i = 0; i < productsOnMarket.size(); i++) {
            if (productsOnMarket.get(i).getProductName().toLowerCase().contains(nameOfProduct.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the store name
            } else if (productsOnMarket.get(i).getStoreName().toLowerCase().contains(nameOfStore.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);

                // checking if it matches anything from the description
            } else if (productsOnMarket.get(i).getDescriptionOfProduct().toLowerCase().contains(description.toLowerCase())) {
                similarProduct = productsOnMarket.get(i);
                searchedProducts.add(similarProduct);
            }
        }
        return searchedProducts;
    }


    public void purchaseProduct(Product product, int quantityPurchased) {
        for (int i = 0; i < productsOnMarket.size(); i++) {
            if (productsOnMarket.get(i).equals(product)) {
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


}
