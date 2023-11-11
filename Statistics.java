import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Statistics {

    public static ArrayList<Product> productsPurchased;
    User user;

    public Statistics(ArrayList<Product> productsPurchased, User user) {
        this.productsPurchased = productsPurchased;
        this.user = user;
    }

    // NO SORTING YET
    public String sellerViewStats() {

        String result = "";

        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            ArrayList<Product> userProducts = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("@@");

                String[] userInfo = parts[0].split(",");
                String email = userInfo[3].trim();

                if (email.equals(user.getEmail())) {
                    userProducts = new ArrayList<>();
                    for (int i = 4; i < parts.length; i++) {
                        String[] productInfo = parts[i].split(",");
                        String productName = productInfo[0].trim();
                        String storeName = productInfo[1].trim();
                        String descriptionOfProduct = productInfo[2].trim();
                        int quantityAvailable = Integer.parseInt(productInfo[3].trim());
                        double price = Double.parseDouble(productInfo[4].trim());

                        Product product = new Product(productName, storeName, descriptionOfProduct, quantityAvailable, price);
                        userProducts.add(product);
                    }
                    break;
                }
            }

            if (userProducts != null) {
                result += "User's Products:\n";
                for (Product product : userProducts) {
                    result += product + "\n";
                }

                result += "\nSales Statistics:\n";
                calculateAndAppendSalesStatistics(userProducts, br, result);
            } else {
                result += "User not found.";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void calculateAndAppendSalesStatistics(ArrayList<Product> userProducts, BufferedReader br, String result) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("@@");

            for (int i = 4; i < parts.length; i++) {
                String[] productInfo = parts[i].split(",");
                String productName = productInfo[0].trim();

                if (userProducts.stream().noneMatch(p -> p.getProductName().equals(productName))) {
                    int salesCount = calculateSalesCount(productName, parts);
                    result += "Product name: ";
                    result += productName;
                    result += ", ";
                    result += salesCount;
                    result += " sales\n";
                }
            }
        }
    }

    private static int calculateSalesCount(String productName, String[] parts) {
        int salesCount = 0;
        for (int i = 1; i < parts.length; i++) {
            String[] productInfo = parts[i].split(",");
            String currentProductName = productInfo[0].trim();
            if (currentProductName.equals(productName)) {
                salesCount++;
            }
        }
        return salesCount;
    }

    // NO SORTING YET
    public String customerViewStats() {
        ArrayList<Product> userProducts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("@@");

                String[] userInfo = parts[0].split(",");
                String email = userInfo[3].trim();

                if (email.equals(user.getEmail())) {
                    for (int i = 4; i < parts.length; i++) {
                        String[] productInfo = parts[i].split(",");
                        String productName = productInfo[0].trim();
                        String storeName = productInfo[1].trim();
                        String descriptionOfProduct = productInfo[2].trim();
                        int quantityAvailable = Integer.parseInt(productInfo[3].trim());
                        double price = Double.parseDouble(productInfo[4].trim());

                        Product product = new Product(productName, storeName, descriptionOfProduct, quantityAvailable, price);
                        userProducts.add(product);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userProducts.toString();
    }

    public static ArrayList<String> getStoreNamesForUserProducts(ArrayList<Product> userProducts) {
        ArrayList<String> storeNames = new ArrayList<>();
        for (Product product : userProducts) {
            if (!storeNames.contains(product.getStoreName())) {
                storeNames.add(product.getStoreName());
            }
        }
        return storeNames;
    }

    public static ArrayList<ArrayList<String>> getStoresAndProductCount() {
        ArrayList<ArrayList<String>> storesAndProducts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("@@");

                for (int i = 4; i < parts.length; i++) {
                    String[] productInfo = parts[i].split(",");
                    String storeName = productInfo[1].trim();
                    boolean isSeller = Boolean.parseBoolean(parts[3].trim());

                    if (isSeller && !containsStore(storesAndProducts, storeName)) {
                        ArrayList<String> storeAndCount = new ArrayList<>();
                        storeAndCount.add(storeName);
                        storeAndCount.add(String.valueOf(getProductCount(parts, storeName)));
                        storesAndProducts.add(storeAndCount);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return storesAndProducts;
    }

    private static boolean containsStore(ArrayList<ArrayList<String>> storesAndProducts, String storeName) {
        for (ArrayList<String> storeAndCount : storesAndProducts) {
            if (storeAndCount.get(0).equals(storeName)) {
                return true;
            }
        }
        return false;
    }

    private static int getProductCount(String[] parts, String storeName) {
        int count = 0;
        for (int i = 4; i < parts.length; i++) {
            String[] productInfo = parts[i].split(",");
            if (productInfo[1].trim().equals(storeName)) {
                count++;
            }
        }
        return count;
    }
}
