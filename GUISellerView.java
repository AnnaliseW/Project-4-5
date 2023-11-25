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


            Product newProductAdded = new Product(productName, storeName, description, quantity, price);

            //TODO: Add products to correct arraylists

            JOptionPane.showMessageDialog(null, newProductAdded.getProductName() + " added to the market!", "Successfully Added", JOptionPane.INFORMATION_MESSAGE);




            boolean existingStoreName = false;
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                String line = "";
                ArrayList<String> allUserData = new ArrayList<>();

                while ((line = bfr.readLine()) != null) {
                    allUserData.add(line);
                }

                for (int i = 0; i < allUserData.size(); i++) {
                    // checking to see if they are sellers
                    String[] checkIfSeller = allUserData.get(i).split(",");
                    // seller identified
                    if (checkIfSeller[3].startsWith("true")) {
                        String[] oneUserDataEachProduct = allUserData.get(i).split(";");

                        if (oneUserDataEachProduct.length == 1) {
                            String[] eachFieldForProduct = oneUserDataEachProduct[0].split(",");
                            // checking if store name is the same and email is the same
                            if (newStoreName.equals(eachFieldForProduct[1]) &&
                                    checkIfSeller[1].equals(userAccount.getEmail())) {
                                existingStoreName = true;
                                break;
                            }
                        } else {
                            for (int j = 0; j < oneUserDataEachProduct.length; j++) {
                                // separating the individual products
                                String[] eachProduct = oneUserDataEachProduct[j].split("@@");
                                for (int k = 0; k < eachProduct.length; k++) {
                                    String[] eachFieldForProduct = eachProduct[k].split(",");
                                    // checking if store name is the same and email is the same
                                    if (eachFieldForProduct[1].equals(newStoreName) &&
                                            oneUserDataEachProduct[1].equals(userAccount.getEmail())) {
                                        existingStoreName = true;
                                        break;
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

            if (existingStoreName) {
                System.out.println("Store name already exists! Please input a different store name\n");
            } else {
                askForStoreName = true;
            }










        }
    }








}
