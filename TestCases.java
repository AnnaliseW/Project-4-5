import org.junit.Before;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCases {

    @Test
    public void testGetProductsOnMarket() {
        // Create a sample product and add it to the market
        Product originalProduct = new Product("OriginalName", "OriginalStore", "OriginalDescription", 10, 19.99);
        Methods.getProductsOnMarket().add(originalProduct);

        // Modify the product on the market using purchaseProduct method
        Methods method = new Methods();
        int quantityPurchased = 5;
        method.purchaseProduct(originalProduct, quantityPurchased);

        // Check if the modification was successful
        assertEquals("OriginalName", originalProduct.getProductName());
        assertEquals("OriginalStore", originalProduct.getStoreName());
        assertEquals("OriginalDescription", originalProduct.getDescriptionOfProduct());
        assertEquals(10 - quantityPurchased, originalProduct.getQuantityAvailable());
        assertEquals(19.99, originalProduct.getPrice(), 0.001);
    }

    @Test
    public void testSearchForProduct() {
        // Create a list of products for testing
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Pencil", "Writing Shop", "Colorful BIC mechanical pencil", 50, 2.99));
        products.add(new Product("Biology Textbook", "Book Resale Store", "Paper copy of BIOL 541 textbook", 2, 189.99));
        products.add(new Product("Calculator", "Electronics Store", "Simple, one-line calculator", 10, 25.99));

        Methods.setProductsOnMarket(products);

        // Search for products based on keywords
        Methods method = new Methods();
        ArrayList<Product> result1 = method.searchForProduct("Pencil");
        ArrayList<Product> result2 = method.searchForProduct("Book");
        ArrayList<Product> result3 = method.searchForProduct("Simple");

        // Check if the search results are as expected
        assertEquals(1, result1.size());
        assertEquals("Pencil", result1.get(0).getProductName());

        assertEquals(1, result2.size());
        assertEquals("Biology Textbook", result2.get(0).getProductName());

        assertEquals(1, result3.size());
        assertEquals("Calculator", result3.get(0).getProductName());
    }

    @Test
    public void testPurchaseProduct() {
        // Create a sample product and add it to the market
        Product product = new Product("SampleProduct", "SampleStore", "SampleDescription", 10, 19.99);
        Methods.getProductsOnMarket().add(product);

        // Purchase a quantity of the product
        int quantityPurchased = 5;
        Methods method = new Methods();
        method.purchaseProduct(product, quantityPurchased);

        // Check if the purchase was successful
        assertEquals(5, product.getQuantityAvailable()); // 10 - 5 = 5
        assertEquals(5, product.getQuantitySold());
    }

    @Test
    public void testPurchaseProductSoldOut() {
        // Create a sample product with 0 quantity and add it to the market
        Product soldOutProduct = new Product("SoldOutProduct", "Store", "Description", 0, 9.99);
        Methods.getProductsOnMarket().add(soldOutProduct);

        // Try to purchase the sold-out product
        int quantityPurchased = 3;
        Methods method = new Methods();
        method.purchaseProduct(soldOutProduct, quantityPurchased);
    }

    @Test
    public void testSaveArrayListToFile() {
        // Create a sample product and user
        Product product1 = new Product("Product1", "Store1", "Description1", 10, 19.99);
        Product product2 = new Product("Product2", "Store2", "Description2", 20, 29.99);
        ArrayList<Product> arrayList = new ArrayList<>();
        arrayList.add(product1);
        arrayList.add(product2);

        User user = new User("Aviana Franco", "franco28@purdue.edu", "1234", false);

        // Create an instance of the Methods class
        Methods methods = new Methods();

        // Create a temporary file for testing
        File dataFile = new File("test_data.txt");

        // Call the method to be tested
        methods.saveArrayListToFile(arrayList, user);

        // Check if the file is created and contains the expected content
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            // Check the first line in the file
            String firstLine = reader.readLine();
            assertEquals("Aviana Franco,franco28@purdue.edu,1234,false;", firstLine);

            // Check the content of products
            String line;
            ArrayList<String> fileContent = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }

            assertEquals(2, fileContent.size()); // Assuming two products in the test case

            // Check the content of each product
            assertEquals("Product1,Store1,Description1,10,19.99", fileContent.get(0));
            assertEquals("Product2,Store2,Description2,20,29.99", fileContent.get(1));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Delete the temporary file created during the test
            if (dataFile.exists() && !dataFile.delete()) {
                System.err.println("Failed to delete the file after the test.");
            }
        }
    }

    @Test
    public void testSaveProductArrayList() {
        // Create a sample product ArrayList
        Product product1 = new Product("Product1", "Store1", "Description1", 10, 19.99);
        Product product2 = new Product("Product2", "Store2", "Description2", 20, 29.99);
        ArrayList<Product> arrayList = new ArrayList<>();
        arrayList.add(product1);
        arrayList.add(product2);

        // Call the method
        Methods method = new Methods();
        method.saveProductArrayList(arrayList);

        // Check if the file is created and contains the expected content
        File dataFile = new File("productArrayList.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            // Check the content of the file
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }

            // Check the content of each product
            String expectedContent = "Product1,Store1,Description1,10,19.99@@Product2,Store2,Description2,20,29.99@@";
            assertEquals(expectedContent, fileContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Exception thrown while reading the file", e);
        } finally {
            // Delete the file created during the test
            if (dataFile.exists() && !dataFile.delete()) {
                System.err.println("Failed to delete the file after the test.");
            }
        }
    }
}
