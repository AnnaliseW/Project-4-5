import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GUISellerView {

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

    public void sellProduct() {
        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("Product Name:"));
        JTextField productNameField = new JTextField();
        panel.add(productNameField);

        panel.add(new JLabel("Store Name:"));
        JTextField storeNameField = new JTextField();
        panel.add(storeNameField);

        panel.add(new JLabel("Description:"));
        JTextField descriptionField = new JTextField();
        panel.add(descriptionField);

        panel.add(new JLabel("Quantity Selling:"));
        JTextField quantityField = new JTextField();
        panel.add(quantityField);

        panel.add(new JLabel("Price:"));
        JTextField priceField = new JTextField();
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Product Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String productName = productNameField.getText();
            String storeName = storeNameField.getText();
            String description = descriptionField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            Product newProductAdded = new Product(productName, storeName, description, quantity, price);

            //TODO: Add products to correct arraylists

            JOptionPane.showMessageDialog(null, newProductAdded.getProductName() + " added to the market!", "Successfully Added", JOptionPane.INFORMATION_MESSAGE);

        }
    }


    public void editProducts() {
        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("New Product Name:"));
        JTextField productNameField = new JTextField();
        panel.add(productNameField);

        panel.add(new JLabel("New Store Name:"));
        JTextField storeNameField = new JTextField();
        panel.add(storeNameField);

        panel.add(new JLabel("New Description:"));
        JTextField descriptionField = new JTextField();
        panel.add(descriptionField);

        panel.add(new JLabel("New Quantity Selling:"));
        JTextField quantityField = new JTextField();
        panel.add(quantityField);

        panel.add(new JLabel("New Price:"));
        JTextField priceField = new JTextField();
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Product Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newProductName = productNameField.getText();
            String newStoreName = storeNameField.getText();
            String newDescription = descriptionField.getText();
            int newQuantity = Integer.parseInt(quantityField.getText());
            double newPrice = Double.parseDouble(priceField.getText());


            Product newProductAdded = new Product(newProductName, newStoreName, newDescription, newQuantity, newPrice);

            //TODO: Add products to correct arraylists

            JOptionPane.showMessageDialog(null, newProductAdded.getProductName() + " added to the market!", "Successfully Added", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    
}
