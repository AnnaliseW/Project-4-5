import javax.crypto.CipherInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MarketPlaceClient extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    public User userAccount;

    public boolean openSeller = false;
    public boolean logIn = false;

    public Product productBought;

    public Product productBoughtNew;

    public boolean productInsight;

    public boolean signInWorks = false;
    boolean alreadyAnAccount = false;

    boolean customerOrSeller = false;

    ArrayList<ShoppingCartProduct> shoppingCart;

    //IMPORTANT: for threading, must reinstantiate the product array list for every instance in order to have new
    // and updated array list from changes made if user buys


    public static void run() throws IOException {
        new MarketPlaceClient().setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        run();
    }

    public MarketPlaceClient() throws IOException {


        JOptionPane.showMessageDialog(null, "Welcome to the marketplace", "Welcome", JOptionPane.INFORMATION_MESSAGE);


        try {
            Socket socket = new Socket("localhost", 1234);


            // CLIENT IS CONNECTED

            final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter writer = new PrintWriter(socket.getOutputStream());


            setTitle("Sign In");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JLabel emailLabel = new JLabel("Email:");
            JLabel passwordLabel = new JLabel("Password:");

            emailField = new JTextField(15);
            passwordField = new JPasswordField(15);


            JButton signInButton = new JButton("Sign In");

            JButton createAccountButton = new JButton("Create Account");

            setLayout(new GridLayout(3, 2));

            add(emailLabel);
            add(emailField);

            add(passwordLabel);
            add(passwordField);

            add(signInButton);
            add(createAccountButton);

            setVisible(true);


            createAccountButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {


                    JPanel panelAccount = new JPanel(new GridLayout(4, 1));
                    panelAccount.add(new JLabel("name"));
                    JTextField name = new JTextField();
                    panelAccount.add(name);

                    panelAccount.add(new JLabel("email"));
                    JTextField email = new JTextField();
                    panelAccount.add(email);

                    panelAccount.add(new JLabel("password"));
                    JPasswordField password = new JPasswordField();
                    panelAccount.add(password);


                    panelAccount.setVisible(true);

                    System.out.println("visible");

                    int option = JOptionPane.showOptionDialog(null, panelAccount, "Create Account",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                    if (option == JOptionPane.OK_OPTION) {


                        String[] choices = {"Seller", "Buyer"};

                        int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Profile Selection",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

                        if (choice == 0) {
                            customerOrSeller = true;
                        } else if (choice == 1) {
                            customerOrSeller = false;
                        }


                        String nameAnswer = name.getText();
                        String emailAnswer = email.getText();
                        char[] passwordChar = password.getPassword();
                        String passwordAnswer = new String(passwordChar);


                        boolean fileIsEmpty = false;
                        try (BufferedReader bfr = new BufferedReader(new FileReader("data.txt"))) {
                            if (bfr.readLine() == null) {
                                fileIsEmpty = true;
                            }
                        } catch (IOException m) {
                            m.printStackTrace();
                        }

                        if (!fileIsEmpty) {
                            try {
                                BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
                                String line = "";
                                ArrayList<String> allUserData = new ArrayList<>();


                                while ((line = bfr.readLine()) != null) {
                                    allUserData.add(line);
                                }

                                alreadyAnAccount = false;
                                for (int i = 0; i < allUserData.size(); i++) {
                                    String[] oneUserData = allUserData.get(i).split(",");
                                    if (oneUserData[1].equals(emailAnswer)) {
                                        alreadyAnAccount = true;
                                        break;
                                    }
                                }

                                bfr.close();

                            } catch (IOException m) {
                                m.printStackTrace();
                            }

                        }


                        if (alreadyAnAccount) {
                            JOptionPane.showMessageDialog(null, "There is already an account with this email! Please log in!", "Create Account", JOptionPane.INFORMATION_MESSAGE);

                            writer.write("createAcc");
                            writer.println();
                            writer.flush();
                            //write to server not log in


                            panelAccount.setVisible(false);
                        } else {
                            userAccount = new User(nameAnswer, emailAnswer, passwordAnswer, customerOrSeller);

                            JOptionPane.showMessageDialog(null, "Account created! Please sign in", "Create Account", JOptionPane.INFORMATION_MESSAGE);


                            //print data to file
                            try {
                                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("data.txt", true)));
                                pw.println(userAccount.getName() + "," + userAccount.getEmail() + "," +
                                        userAccount.getPassword() + "," + userAccount.isSeller() + ";");
                                pw.flush();
                                pw.close();

                            } catch (IOException m) {
                                m.printStackTrace();

                            }
                            writer.write("createAcc");
                            writer.println();
                            writer.flush();
                            panelAccount.setVisible(false);
                        }


                    }
                }

            });


            signInButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    writer.write("processSignIn");
                    writer.println();
                    writer.flush();


                    //SEND MESSAGE "SIGN IN PRESSED" TO SERVER

                    String email = emailField.getText();
                    char[] passwordChar = passwordField.getPassword();
                    String password = new String(passwordChar);
                    String emailAndPassword = email + "," + password;
                    writer.write(emailAndPassword);
                    writer.println();
                    writer.flush();


                    //recieves message of succesfull sign in and whteher buyer or seller from server

                    //open market

                    String[] infoArray;


                    String info = null;
                    try {
                        info = reader.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    infoArray = info.split(",");
                    boolean seller = false;

                    if (!infoArray[3].equals("true")) {


                        //does not sign in

                        JOptionPane.showMessageDialog(null, "Incorrect Sign In!", "Sign In", JOptionPane.INFORMATION_MESSAGE);

                        writer.write("notSignIn");
                        writer.println();
                        writer.flush();


                    } else {
                        signInWorks = true;
                        setVisible(false);

                        logIn = true;
                        // sign in worked

                        writer.write("signIn");
                        writer.println();
                        writer.flush();


                        try {
                            String userInformation = reader.readLine();


                            String[] user = userInformation.split(",");
                            String nameUser = user[0];
                            String emailUser = user[1];
                            String passwordUser = user[2];
                            seller = Boolean.parseBoolean(user[3]);
                            //MAKES USERACCOUNT
                            userAccount = new User(nameUser, emailUser, passwordUser, seller);
                        } catch (IOException g) {
                            g.printStackTrace();
                        }


                        //USER ACCOUNT must be taken from server and put into USER

                        if (seller) {
                            openSeller = true;
                            //opens seller side

                            writer.write("seller");
                            writer.println();
                            writer.flush();


                            //open seller market
                            JFrame sellerView = new JFrame();

                            JButton sellButton;
                            JButton editButton;
                            JButton deleteButton;
                            JButton importButton;
                            JButton exportButton;
                            JButton salesByStoreButton;
                            JButton editProfileButton;
                            JButton deleteAccountButton;
                            JButton viewCustomerCartsButton;

                            String userName = userAccount.getName();
                            String userEmail = userAccount.getEmail();
                            String userPassword = userAccount.getPassword();

                            writer.write(userName);
                            writer.write(userEmail);
                            writer.write(userPassword);

                            sellerView.setTitle("Marketplace Home Page");
                            sellerView.setSize(600, 300);
                            sellerView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            sellerView.setLocationRelativeTo(null);

                            sellButton = new JButton("Sell");
                            editButton = new JButton("Edit");
                            deleteButton = new JButton("Delete");
                            importButton = new JButton("Import");
                            exportButton = new JButton("Export");
                            salesByStoreButton = new JButton("Sales By Store");
                            editProfileButton = new JButton("Edit Profile");
                            deleteAccountButton = new JButton("Delete Account");
                            viewCustomerCartsButton = new JButton("View Customer Carts");


                            sellerView.setLayout(new GridLayout(3, 3));

                            sellerView.add(sellButton);
                            sellerView.add(editButton);
                            sellerView.add(deleteButton);
                            sellerView.add(importButton);
                            sellerView.add(exportButton);
                            sellerView.add(salesByStoreButton);
                            sellerView.add(editProfileButton);
                            sellerView.add(deleteAccountButton);
                            sellerView.add(viewCustomerCartsButton);

                            sellerView.setVisible(true);

                            // implement buttons here
                            sellButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("sellButton");
                                    writer.println();
                                    writer.flush();

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

                                        String productInfo = productName;
                                        productInfo += "," + storeName;
                                        productInfo += "," + description;
                                        productInfo += "," + quantity;
                                        productInfo += "," + price;

                                        writer.write(productInfo);
                                        writer.println();
                                        writer.flush();

                                        String sucess = "";
                                        try {
                                            sucess = reader.readLine();
                                        } catch (IOException g) {
                                            g.printStackTrace();
                                        }

                                        if (sucess.equals("Success")) {
                                            JOptionPane.showMessageDialog(null, "Your product has been added to the market!", "Successfully Added", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }

                                }
                            });

                            editButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    boolean cont = false;
                                    writer.write("editButton");
                                    writer.println();
                                    writer.flush();
                                    ArrayList<Product> productList = new ArrayList<>();
                                    try {
                                        String line = reader.readLine();
                                        if (line.equals("Empty")) {
                                            JOptionPane.showMessageDialog(null, "No items to edit", "Empty List", JOptionPane.INFORMATION_MESSAGE);
                                        } else {
                                            cont = true;
                                            do {
                                                String[] parts = line.split("\n");
                                                String productName = parts[1].split(": ")[1];
                                                String storeName = parts[2].split(": ")[1];
                                                String description = parts[3].split(": ")[1];
                                                int quantityAvailable = Integer.parseInt(parts[4].split(": ")[1]);
                                                double price = Double.parseDouble(parts[5].split(": ")[1]);
                                                productList.add(new Product(productName, storeName, description, quantityAvailable, price));
                                                line = reader.readLine();
                                            } while (line != null);
                                        }
                                    } catch (IOException i) {
                                        i.printStackTrace();
                                    }

                                    if (cont) {

                                        String[] products = new String[productList.size()];
                                        for (int i = 0; i < productList.size(); i++) {
                                            products[i] = productList.get(i).statisticsToString();
                                        }
                                        String itemFromSearchChosen = (String) JOptionPane.showInputDialog(null, "Select item to edit",
                                                "Edit", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

                                        Product productToEdit = productList.get(0);

                                        int selection = 0;

                                        for (int k = 0; k < productList.size(); k++) {
                                            if (productList.get(k).statisticsToString().equals(itemFromSearchChosen)) {
                                                productToEdit = productList.get(k);
                                                selection = k;
                                                break;
                                            }
                                        }

                                        writer.println(selection);
                                        writer.println();
                                        writer.flush();

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

                                            writer.println(newProductName);
                                            writer.println();
                                            writer.flush();
                                            writer.println(newStoreName);
                                            writer.println();
                                            writer.flush();
                                            writer.println(newDescription);
                                            writer.println();
                                            writer.flush();
                                            writer.println(newQuantity);
                                            writer.println();
                                            writer.flush();
                                            writer.println(newPrice);
                                            writer.println();
                                            writer.flush();

                                        }

                                        String sucess = "";
                                        try {
                                            sucess = reader.readLine();
                                        } catch (IOException g) {
                                            g.printStackTrace();
                                        }

                                        if (sucess.equals("Success")) {
                                            JOptionPane.showMessageDialog(null, "Your product has been edited!", "Successfully Edited", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }
                                }
                            });

                            deleteButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    boolean cont = false;
                                    writer.write("deleteButton");
                                    writer.println();
                                    writer.flush();

                                    ArrayList<Product> productList = new ArrayList<>();
                                    try {
                                        String line = reader.readLine();
                                        if (line.equals("Empty")) {
                                            JOptionPane.showMessageDialog(null, "No items to edit", "Empty List", JOptionPane.INFORMATION_MESSAGE);
                                        } else {
                                            cont = true;
                                            do {
                                                String[] parts = line.split("\n");
                                                String productName = parts[1].split(": ")[1];
                                                String storeName = parts[2].split(": ")[1];
                                                String description = parts[3].split(": ")[1];
                                                int quantityAvailable = Integer.parseInt(parts[4].split(": ")[1]);
                                                double price = Double.parseDouble(parts[5].split(": ")[1]);
                                                productList.add(new Product(productName, storeName, description, quantityAvailable, price));
                                                line = reader.readLine();
                                            } while (line != null);
                                        }
                                    } catch (IOException i) {
                                        i.printStackTrace();
                                    }

                                    if (cont) {
                                        String[] products = new String[productList.size()];
                                        for (int i = 0; i < productList.size(); i++) {
                                            products[i] = productList.get(i).statisticsToString();
                                        }
                                        String itemFromSearchChosen = (String) JOptionPane.showInputDialog(null, "Select item to delete",
                                                "Delete", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

                                        Product productToDelete = productList.get(0);

                                        int selection = 0;

                                        for (int k = 0; k < productList.size(); k++) {
                                            if (productList.get(k).statisticsToString().equals(itemFromSearchChosen)) {
                                                productToDelete = productList.get(k);
                                                selection = k;
                                                break;
                                            }
                                        }

                                        writer.println(selection);
                                        writer.println();
                                        writer.flush();

                                        String sucess = "";
                                        try {
                                            sucess = reader.readLine();
                                        } catch (IOException g) {
                                            g.printStackTrace();
                                        }
                                        if (sucess.equals("Success")) {
                                            JOptionPane.showMessageDialog(null, "Your product has been edited!", "Successfully Edited", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }

                                }
                            });
                            importButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("importButton");
                                    writer.println();
                                    writer.flush();

                                    String fileName = JOptionPane.showInputDialog(null, "Enter the filename:");

                                    if (fileName != null && !fileName.isEmpty()) {
                                        ArrayList<String> lines = new ArrayList<>();

                                        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                                            String line;
                                            while ((line = br.readLine()) != null) {
                                                lines.add(line);
                                            }
                                        } catch (IOException ioException) {
                                            ioException.printStackTrace();
                                        }
                                        for (String item : lines) {
                                            writer.println(item);
                                            writer.println();
                                            writer.flush();
                                        }
                                    }
                                    String sucess = "";
                                    try {
                                        sucess = reader.readLine();
                                    } catch (IOException g) {
                                        g.printStackTrace();
                                    }
                                    if (sucess.equals("Success")) {
                                        JOptionPane.showMessageDialog(null, "Your products have been imported!", "Successfully imported", JOptionPane.INFORMATION_MESSAGE);
                                    }

                                }
                            });
                            exportButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("exportButton");
                                    writer.println();
                                    writer.flush();

                                    boolean cont = false;

                                    ArrayList<Product> productList = new ArrayList<>();
                                    try {
                                        String line = reader.readLine();
                                        if (line.equals("Empty")) {
                                            JOptionPane.showMessageDialog(null, "No items to edit", "Empty List", JOptionPane.INFORMATION_MESSAGE);
                                        } else {
                                            cont = true;
                                            do {
                                                String[] parts = line.split("\n");
                                                String productName = parts[1].split(": ")[1];
                                                String storeName = parts[2].split(": ")[1];
                                                String description = parts[3].split(": ")[1];
                                                int quantityAvailable = Integer.parseInt(parts[4].split(": ")[1]);
                                                double price = Double.parseDouble(parts[5].split(": ")[1]);
                                                productList.add(new Product(productName, storeName, description, quantityAvailable, price));
                                                line = reader.readLine();
                                            } while (line != null);
                                        }
                                    } catch (IOException i) {
                                        i.printStackTrace();
                                    }

                                    if (cont) {
                                        String[] products = new String[productList.size()];
                                        for (int i = 0; i < productList.size(); i++) {
                                            products[i] = productList.get(i).statisticsToString();
                                        }
                                        String itemFromSearchChosen = (String) JOptionPane.showInputDialog(null, "Select item to export",
                                                "Export", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

                                        Product productToExport = productList.get(0);

                                        int selection = 0;

                                        for (int k = 0; k < productList.size(); k++) {
                                            if (productList.get(k).statisticsToString().equals(itemFromSearchChosen)) {
                                                productToExport = productList.get(k);
                                                selection = k;
                                                break;
                                            }
                                        }

                                        writer.println(selection);
                                        writer.println();
                                        writer.flush();

                                        String sucess = "";
                                        try {
                                            sucess = reader.readLine();
                                        } catch (IOException g) {
                                            g.printStackTrace();
                                        }
                                        if (sucess.equals("Success")) {
                                            JOptionPane.showMessageDialog(null, "Your product has been exported!", "Successfully Exported", JOptionPane.INFORMATION_MESSAGE);
                                        }

                                    }

                                }
                            });
                            salesByStoreButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("salesByStoreButton");
                                    writer.println();
                                    writer.flush();


                                }
                            });

                            editProfileButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("editProfileButton");
                                    writer.println();
                                    writer.flush();

                                    int message = JOptionPane.showOptionDialog(null, "Leave fields empty if you would like to keep anything", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                                    if (message == -1) {
                                        writer.write("null");
                                        writer.println();
                                        writer.flush();

                                    } else {

                                        JPanel panel = new JPanel(new GridLayout(5, 2));

                                        panel.add(new JLabel("New Name:"));
                                        JTextField newNameField = new JTextField();
                                        panel.add(newNameField);

                                        panel.add(new JLabel("New Email:"));
                                        JTextField newEmailField = new JTextField();
                                        panel.add(newEmailField);

                                        panel.add(new JLabel("New Password:"));
                                        JTextField newPasswordField = new JTextField();
                                        panel.add(newPasswordField);

                                        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Product Details",
                                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                                            writer.write("null");
                                            writer.println();
                                            writer.flush();
                                        } else if (result == JOptionPane.OK_OPTION) {
                                            String newUserName = newNameField.getText();
                                            String newUserEmail = newEmailField.getText();
                                            String newUserPassword = newPasswordField.getText();

                                            if (newUserName.isEmpty()) {
                                                newUserName = userAccount.getName();
                                            } else {
                                                userAccount.setName(newUserName);
                                            }

                                            if (newUserEmail.isEmpty()) {
                                                newUserEmail = userAccount.getEmail();
                                            } else {
                                                userAccount.setEmail(newUserEmail);
                                            }

                                            if (newUserPassword.isEmpty()) {
                                                newUserPassword = userAccount.getPassword();
                                            } else {
                                                userAccount.setPassword(newUserPassword);
                                            }

                                            String combined = newUserName + "," + newUserEmail + "," + newUserPassword;
                                            writer.write(combined);
                                            writer.println();
                                            writer.flush();

                                            System.out.println(combined);

                                            JOptionPane.showMessageDialog(null, "Your product has been edited!", "Successfully Edited", JOptionPane.INFORMATION_MESSAGE);


                                        }
                                    }
                                }
                            });

                            deleteAccountButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("deleteAccount");
                                    writer.println();
                                    writer.flush();

                                    String sucess = "";
                                    try {
                                        sucess = reader.readLine();
                                    } catch (IOException g) {
                                        g.printStackTrace();
                                    }
                                    if (sucess.equals("Success")) {
                                        JOptionPane.showMessageDialog(null, "All data will be removed. Please close the program now", "Your account has been deleted", JOptionPane.INFORMATION_MESSAGE);
                                    }

                                }
                            });

                            viewCustomerCartsButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("viewCustomerCartsButton");
                                    writer.println();
                                    writer.flush();

                                }
                            });

                            // implement buttons here


                        } else {

                            //opens buyer market side
                            openSeller = false;


                            writer.write("buyer");
                            writer.println();
                            writer.flush();


                            JFrame CustomerView = new JFrame();


                            // NOTE : sign in message occurs twice using this method


                            //creating user
                            //creates product array list from file

                            Methods method = new Methods();
                            Methods.productsOnMarket = method.makeProductArrayList();

                            shoppingCart = method.createShoppingCartArray(userAccount);

                            //making customer history
                            ArrayList<SoldProduct> customerHistory = method.makeCustomerHistory(userAccount);


                            //creates shopping cart array list from file
                            CustomerView.setTitle("Marketplace Home Page");
                            CustomerView.setSize(600, 300);
                            CustomerView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            CustomerView.setLocationRelativeTo(null);

                            JButton seeProductsButton;
                            JButton searchProductsButton;
                            JButton viewShoppingCartButton;
                            JButton editProfileButton;
                            JButton viewPurchaseHistoryButton;
                            JButton exportFiles;


                            seeProductsButton = new JButton("See Products");
                            searchProductsButton = new JButton("Search Products");
                            viewShoppingCartButton = new JButton("View Shopping Cart");
                            editProfileButton = new JButton("Edit Profile");
                            viewPurchaseHistoryButton = new JButton("View Purchase History");
                            exportFiles = new JButton("Export Files");


                            CustomerView.setLayout(new GridLayout(2, 3));

                            CustomerView.add(seeProductsButton);
                            CustomerView.add(searchProductsButton);
                            CustomerView.add(viewShoppingCartButton);
                            CustomerView.add(editProfileButton);
                            CustomerView.add(viewPurchaseHistoryButton);
                            CustomerView.add(exportFiles);

                            CustomerView.setVisible(true);

                            exportFiles.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    //exporting customer reciepts into a file

                                }
                            });


                            seeProductsButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    Methods.productsOnMarket = method.makeProductArrayList();
                                    //instantiate new updated array list
                                    productInsight = false;
                                    // TODO: Handle sell button action
                                    writer.write("seeProduct");
                                    writer.println();
                                    writer.flush();
                                    System.out.println("1 see products button ");
                                    //indicator to process see products
                                    if (Methods.productsOnMarket.isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "No products in the market!", "See Products", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        int sortResponse = JOptionPane.showConfirmDialog(null, "Would you like to sort the market?",
                                                "Sort Market", JOptionPane.YES_NO_OPTION);
                                        if (sortResponse == -1) {
                                            System.out.println("no sort response exit ");
                                            writer.write("null");
                                            writer.println();
                                            writer.flush();
                                            return;
                                            //exiting
                                        }
                                        if (sortResponse == 0) {
                                            //chose yes
                                            System.out.println("choose to sort");


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


                                            //one button
                                            sortByMinQuantity.addActionListener(f -> {
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                frame.setVisible(false);
                                                ArrayList<Product> copy = new ArrayList<>();
                                                copy = method.sortByMinQuantity(Methods.productsOnMarket);
                                                Methods.productsOnMarket.clear();
                                                Methods.productsOnMarket.addAll(copy);

                                                String[] products = new String[Methods.productsOnMarket.size()];
                                                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                    products[i] = Methods.productsOnMarket.get(i).statisticsToString();
                                                }
                                                System.out.println(Arrays.toString(products));
                                                String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                                                        "Market", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                                                if (chooseProduct == null) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();

                                                } else {
                                                    String[] findingNames = chooseProduct.split(",");
                                                    String productName = findingNames[0].substring(findingNames[0].indexOf(":") + 2);
                                                    String storeName = findingNames[1].substring(findingNames[1].indexOf(":") + 2);

                                                    //sending which product they chose
                                                    Product productBuying = null;
                                                    for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                        if (Methods.productsOnMarket.get(i).getProductName().equals(productName) &&
                                                                Methods.productsOnMarket.get(i).getStoreName().equals(storeName)) {
                                                            productBuying = Methods.productsOnMarket.get(i);
                                                        }
                                                    }

                                                    writer.write(chooseProduct);
                                                    writer.println();
                                                    writer.flush();
                                                    System.out.println(chooseProduct);


                                                    productBought = productBuying;

                                                }
                                                frame.setVisible(false);
                                                System.out.println("2 sort");


                                                System.out.println("3 panel pop up ");
                                                //DEALING WITH BUYING, SHOPPING CART, INSIGHTS
                                                JButton buyProduct;
                                                JButton addToCart;
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


                                                JPanel panels = new JPanel();
                                                productChosen.setLayout(new BorderLayout());

                                                productChosen.add(productStatistics, BorderLayout.CENTER);
                                                productChosen.addWindowListener(new WindowAdapter() {
                                                    @Override
                                                    public void windowClosing(WindowEvent e) {
                                                        // Check if the window closing event is due to the dispose on close
                                                        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                            writer.write("null");
                                                            writer.println();
                                                            writer.flush();
                                                            productChosen.dispose();
                                                        }
                                                        System.out.println("exit and print null exit product window");
                                                    }
                                                });


                                                //BUYING PRODUCT BUTTON
                                                buyProduct.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        Methods.productsOnMarket = method.makeProductArrayList();
                                                        // implementation of buyProduct button
                                                        writer.write("buyProduct");
                                                        writer.println();
                                                        writer.flush();

                                                        //sending that clicked this button in SERVER


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
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();


                                                                System.out.println("null redo to do cart/buy again ");
                                                                productChosen.setVisible(false);
                                                            } else {
                                                                amountPurchasing = Integer.parseInt(answer);


                                                            }


                                                        } catch (NumberFormatException f) {
                                                            JOptionPane.showMessageDialog(null, "Invalid input! Please try again",
                                                                    "Buy Product", JOptionPane.ERROR_MESSAGE);
                                                            purchaseMessage = false;
                                                            System.out.println("invalid catch");

                                                            writer.write("null");
                                                            writer.println();
                                                            writer.flush();
                                                            productChosen.setVisible(false);
                                                        }


                                                        if (purchaseMessage) {


                                                            writer.write("" + amountPurchasing);
                                                            writer.println();
                                                            writer.flush();

                                                            Methods method = new Methods();


                                                            //TESTING


                                                            //TESTING


                                                            try {

                                                                String productAvailability = reader.readLine();


                                                                if (productAvailability.equals("soldOut")) {
                                                                    //SOLD OUT READ FROM SERVER
                                                                    JOptionPane.showMessageDialog(null, "Error! Product is sold out!",
                                                                            "Buy Product", JOptionPane.ERROR_MESSAGE);

                                                                } else if (productAvailability.equals("purchaseLimit")) {

                                                                    int purchaseHistoryQuantityAvailable = productBought.getQuantityAvailable();
                                                                    // purchasing wanted > amount available
                                                                    JOptionPane.showMessageDialog(null, "Could only purchase " + productBought.getQuantityAvailable() + " products!\n "
                                                                                    + productBought.getProductName() + " purchased!",
                                                                            "Buy Product", JOptionPane.INFORMATION_MESSAGE);

                                                                    method.purchaseProduct(productBought, productBought.getQuantityAvailable());
                                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                                        if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                            shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                        }
                                                                    }


                                                                    ///customer history
                                                                    //String productName, String storeName, String descriptionOfProduct, int quantityAvailable,
                                                                    //                       double price, int quantityPurchased

                                                                    SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                            productBought.getQuantityAvailable(), productBought.getPrice(), purchaseHistoryQuantityAvailable);
                                                                    customerHistory.add(addedProductHistory);
                                                                    method.saveCustomerHistory(customerHistory, userAccount);

                                                                } else if (productAvailability.equals("itemPurchased")) {
                                                                    //item correctly purchased in the server
                                                                    JOptionPane.showMessageDialog(null, productBought.getProductName() + " purchased!", "Buy Products",
                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                    method.purchaseProduct(productBought, amountPurchasing);
                                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                                        if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                            shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                        }
                                                                    }


                                                                    SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                            productBought.getQuantityAvailable(), productBought.getPrice(), amountPurchasing);
                                                                    customerHistory.add(addedProductHistory);
                                                                    method.saveCustomerHistory(customerHistory, userAccount);

                                                                }
                                                            } catch (IOException ex) {
                                                                ex.printStackTrace();
                                                            }


                                                        } else {
                                                            return;
                                                        }
                                                        productChosen.setVisible(false);
                                                        System.out.println("4: buy product");


                                                    }

                                                });

                                                addToCart.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        Methods.productsOnMarket = method.makeProductArrayList();
                                                        //sending to server button clicked
                                                        writer.write("addToCart");
                                                        writer.println();
                                                        writer.flush();

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
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();


                                                                System.out.println("null redo to do cart/buy again ");
                                                                productChosen.setVisible(false);

                                                            } else {
                                                                quantityBuying = Integer.parseInt(answer);

                                                            }


                                                        } catch (NumberFormatException f) {
                                                            JOptionPane.showMessageDialog(null, "Invalid Input! Please try again",
                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                            shoppingCartMessage = false;
                                                            System.out.println("invalid catch");

                                                            writer.write("null");
                                                            writer.println();
                                                            writer.flush();
                                                            productChosen.setVisible(false);

                                                        }

                                                        //sending to server quantityBuying


                                                        if (shoppingCartMessage) {

                                                            writer.write("" + quantityBuying);
                                                            writer.println();
                                                            writer.flush();


                                                            String productName = productBought.getProductName();
                                                            String storeName = productBought.getStoreName();
                                                            String description = productBought.getDescriptionOfProduct();
                                                            int quantityAvailable = productBought.getQuantityAvailable();
                                                            double price = productBought.getPrice();


                                                            try {
                                                                String successCart = reader.readLine();
                                                                if (successCart == null) {
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();
                                                                    System.out.println("error fix");
                                                                    productChosen.setVisible(false);
                                                                    return;

                                                                } else {
                                                                    if (successCart.equals("addedToCart")) {
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " added to your shopping cart!",
                                                                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                price, quantityBuying);
                                                                        shoppingCart.add(addedProductToCart);

                                                                    } else if (successCart.equals("soldOut")) {
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " sold out! Can't add to cart",
                                                                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                    } else if (successCart.equals("limitedQuantity")) {
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " only " + productBought.getQuantityAvailable() + " quantity available to add to cart!",
                                                                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                price, quantityAvailable);
                                                                        shoppingCart.add(addedProductToCart);
                                                                    }
                                                                    //TESTING IF QUANTITY CAN NOT BE ADDED
                                                                }
                                                            } catch (IOException ex) {
                                                                ex.printStackTrace();
                                                            }


                                                        } else {
                                                            return;
                                                        }
                                                        productChosen.setVisible(false);
                                                        System.out.println("4: add cart");
                                                    }

                                                });
                                                // CHECK: shouldn't need to send anything to server? Just exit out of panel


                                                panels.add(buyProduct);
                                                panels.add(addToCart);


                                                productChosen.add(panels, BorderLayout.CENTER);
                                                productChosen.setVisible(true);


                                            });

                                            sortByMaxQuantity.addActionListener(f -> {
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                ArrayList<Product> copy = new ArrayList<>();
                                                copy = method.sortByMaxQuantity(Methods.productsOnMarket);
                                                Methods.productsOnMarket.clear();
                                                Methods.productsOnMarket.addAll(copy);
                                                frame.setVisible(false);


                                                String[] products = new String[Methods.productsOnMarket.size()];
                                                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                    products[i] = Methods.productsOnMarket.get(i).statisticsToString();
                                                }

                                                String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                                                        "Market", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                                                if (chooseProduct == null) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();
                                                } else {
                                                    String[] findingNames = chooseProduct.split(",");
                                                    String productName = findingNames[0].substring(findingNames[0].indexOf(":") + 2);
                                                    String storeName = findingNames[1].substring(findingNames[1].indexOf(":") + 2);

                                                    //sending which product they chose
                                                    Product productBuying = null;
                                                    for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                        if (Methods.productsOnMarket.get(i).getProductName().equals(productName) &&
                                                                Methods.productsOnMarket.get(i).getStoreName().equals(storeName)) {
                                                            productBuying = Methods.productsOnMarket.get(i);
                                                        }
                                                    }


                                                    writer.write(chooseProduct);
                                                    writer.println();
                                                    writer.flush();


                                                    productBought = productBuying;
                                                    frame.setVisible(false);


                                                    System.out.println("3 panel pop up ");
                                                    //DEALING WITH BUYING, SHOPPING CART, INSIGHTS
                                                    JButton buyProduct;
                                                    JButton addToCart;
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


                                                    JPanel panels = new JPanel();
                                                    productChosen.setLayout(new BorderLayout());

                                                    productChosen.add(productStatistics, BorderLayout.CENTER);
                                                    productChosen.addWindowListener(new WindowAdapter() {
                                                        @Override
                                                        public void windowClosing(WindowEvent e) {
                                                            // Check if the window closing event is due to the dispose on close
                                                            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.dispose();
                                                            }
                                                            System.out.println("exit and print null exit product window");
                                                        }
                                                    });


                                                    //BUYING PRODUCT BUTTON
                                                    buyProduct.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            // implementation of buyProduct button
                                                            writer.write("buyProduct");
                                                            writer.println();
                                                            writer.flush();

                                                            //sending that clicked this button in SERVER


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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();

                                                                    System.out.println("null redo to do cart/buy again ");
                                                                    productChosen.setVisible(false);
                                                                } else {
                                                                    amountPurchasing = Integer.parseInt(answer);

                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid input! Please try again",
                                                                        "Buy Product", JOptionPane.ERROR_MESSAGE);
                                                                purchaseMessage = false;
                                                                System.out.println("invalid catch");

                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);
                                                            }


                                                            if (purchaseMessage) {


                                                                writer.write("" + amountPurchasing);
                                                                writer.println();
                                                                writer.flush();

                                                                Methods method = new Methods();


                                                                //TESTING


                                                                //TESTING


                                                                try {

                                                                    String productAvailability = reader.readLine();


                                                                    if (productAvailability.equals("soldOut")) {
                                                                        //SOLD OUT READ FROM SERVER
                                                                        JOptionPane.showMessageDialog(null, "Error! Product is sold out!",
                                                                                "Buy Product", JOptionPane.ERROR_MESSAGE);

                                                                    } else if (productAvailability.equals("purchaseLimit")) {
                                                                        int purchaseHistoryQuantityAvailable = productBought.getQuantityAvailable();

                                                                        // purchasing wanted > amount available
                                                                        JOptionPane.showMessageDialog(null, "Could only purchase " + productBought.getQuantityAvailable() + " products!\n "
                                                                                        + productBought.getProductName() + " purchased!",
                                                                                "Buy Product", JOptionPane.INFORMATION_MESSAGE);

                                                                        method.purchaseProduct(productBought, productBought.getQuantityAvailable());
                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), purchaseHistoryQuantityAvailable);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);

                                                                    } else if (productAvailability.equals("itemPurchased")) {
                                                                        //item correctly purchased in the server
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " purchased!", "Buy Products",
                                                                                JOptionPane.INFORMATION_MESSAGE);

                                                                        method.purchaseProduct(productBought, amountPurchasing);
                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), amountPurchasing);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);


                                                                    }
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("4: buy product");


                                                        }

                                                    });

                                                    addToCart.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            //sending to server button clicked
                                                            writer.write("addToCart");
                                                            writer.println();
                                                            writer.flush();

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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();

                                                                    System.out.println("null redo to do cart/buy again ");
                                                                    productChosen.setVisible(false);

                                                                } else {
                                                                    quantityBuying = Integer.parseInt(answer);

                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid Input! Please try again",
                                                                        "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                shoppingCartMessage = false;
                                                                System.out.println("invalid catch");


                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);

                                                            }

                                                            //sending to server quantityBuying


                                                            if (shoppingCartMessage) {

                                                                writer.write("" + quantityBuying);
                                                                writer.println();
                                                                writer.flush();


                                                                String productName = productBought.getProductName();
                                                                String storeName = productBought.getStoreName();
                                                                String description = productBought.getDescriptionOfProduct();
                                                                int quantityAvailable = productBought.getQuantityAvailable();
                                                                double price = productBought.getPrice();


                                                                try {
                                                                    String successCart = reader.readLine();
                                                                    if (successCart == null) {
                                                                        writer.write("null");
                                                                        writer.println();
                                                                        writer.flush();
                                                                        System.out.println("error fix");
                                                                        productChosen.setVisible(false);
                                                                        return;

                                                                    } else {
                                                                        if (successCart.equals("addedToCart")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " added to your shopping cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityBuying);
                                                                            shoppingCart.add(addedProductToCart);

                                                                        } else if (successCart.equals("soldOut")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " sold out! Can't add to cart",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        } else if (successCart.equals("limitedQuantity")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " only " + productBought.getQuantityAvailable() + " quantity available to add to cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityAvailable);
                                                                            shoppingCart.add(addedProductToCart);
                                                                        }
                                                                        //TESTING IF QUANTITY CAN NOT BE ADDED
                                                                    }
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("4: add cart");
                                                        }

                                                    });
                                                    // CHECK: shouldn't need to send anything to server? Just exit out of panel


                                                    panels.add(buyProduct);
                                                    panels.add(addToCart);


                                                    productChosen.add(panels, BorderLayout.CENTER);
                                                    productChosen.setVisible(true);

                                                }
                                                System.out.println("2 sort");
                                            });

                                            sortByMinPrice.addActionListener(f -> {
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                ArrayList<Product> copy = new ArrayList<>();
                                                copy = method.sortByMinPrice(Methods.productsOnMarket);
                                                Methods.productsOnMarket.clear();
                                                Methods.productsOnMarket.addAll(copy);
                                                frame.setVisible(false);


                                                String[] products = new String[Methods.productsOnMarket.size()];
                                                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                    products[i] = Methods.productsOnMarket.get(i).statisticsToString();
                                                }

                                                String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                                                        "Market", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                                                if (chooseProduct == null) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();
                                                } else {
                                                    String[] findingNames = chooseProduct.split(",");
                                                    String productName = findingNames[0].substring(findingNames[0].indexOf(":") + 2);
                                                    String storeName = findingNames[1].substring(findingNames[1].indexOf(":") + 2);

                                                    //sending which product they chose
                                                    Product productBuying = null;
                                                    for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                        if (Methods.productsOnMarket.get(i).getProductName().equals(productName) &&
                                                                Methods.productsOnMarket.get(i).getStoreName().equals(storeName)) {
                                                            productBuying = Methods.productsOnMarket.get(i);
                                                        }
                                                    }

                                                    writer.write(chooseProduct);
                                                    writer.println();
                                                    writer.flush();


                                                    productBought = productBuying;
                                                    frame.setVisible(false);


                                                    System.out.println("3 panel pop up ");
                                                    //DEALING WITH BUYING, SHOPPING CART, INSIGHTS
                                                    JButton buyProduct;
                                                    JButton addToCart;
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


                                                    JPanel panels = new JPanel();
                                                    productChosen.setLayout(new BorderLayout());

                                                    productChosen.add(productStatistics, BorderLayout.CENTER);
                                                    productChosen.addWindowListener(new WindowAdapter() {
                                                        @Override
                                                        public void windowClosing(WindowEvent e) {
                                                            // Check if the window closing event is due to the dispose on close
                                                            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.dispose();
                                                            }
                                                            System.out.println("exit and print null product window ");
                                                        }
                                                    });


                                                    //BUYING PRODUCT BUTTON
                                                    buyProduct.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            // implementation of buyProduct button
                                                            writer.write("buyProduct");
                                                            writer.println();
                                                            writer.flush();
                                                            //sending that clicked this button in SERVER


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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();

                                                                    System.out.println("null redo to do cart/buy again ");
                                                                    productChosen.setVisible(false);
                                                                } else {
                                                                    amountPurchasing = Integer.parseInt(answer);

                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid input! Please try again",
                                                                        "Buy Product", JOptionPane.ERROR_MESSAGE);
                                                                purchaseMessage = false;
                                                                System.out.println("invalid catch");

                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);
                                                            }


                                                            if (purchaseMessage) {


                                                                writer.write("" + amountPurchasing);
                                                                writer.println();
                                                                writer.flush();
                                                                Methods method = new Methods();


                                                                //TESTING


                                                                //TESTING


                                                                try {

                                                                    String productAvailability = reader.readLine();


                                                                    if (productAvailability.equals("soldOut")) {
                                                                        //SOLD OUT READ FROM SERVER
                                                                        JOptionPane.showMessageDialog(null, "Error! Product is sold out!",
                                                                                "Buy Product", JOptionPane.ERROR_MESSAGE);

                                                                    } else if (productAvailability.equals("purchaseLimit")) {

                                                                        int purchaseHistoryQuantityAvailable = productBought.getQuantityAvailable();
                                                                        // purchasing wanted > amount available
                                                                        JOptionPane.showMessageDialog(null, "Could only purchase " + productBought.getQuantityAvailable() + " products!\n "
                                                                                        + productBought.getProductName() + " purchased!",
                                                                                "Buy Product", JOptionPane.INFORMATION_MESSAGE);

                                                                        method.purchaseProduct(productBought, productBought.getQuantityAvailable());


                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), purchaseHistoryQuantityAvailable);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);

                                                                    } else if (productAvailability.equals("itemPurchased")) {
                                                                        //item correctly purchased in the server
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " purchased!", "Buy Products",
                                                                                JOptionPane.INFORMATION_MESSAGE);

                                                                        method.purchaseProduct(productBought, amountPurchasing);

                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), amountPurchasing);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);

                                                                    }
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("4: buy product");


                                                        }

                                                    });

                                                    addToCart.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            //sending to server button clicked
                                                            writer.write("addToCart");
                                                            writer.println();
                                                            writer.flush();

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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();

                                                                    System.out.println("null redo to do cart/buy again ");
                                                                    productChosen.setVisible(false);

                                                                } else {
                                                                    quantityBuying = Integer.parseInt(answer);

                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid Input! Please try again",
                                                                        "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                shoppingCartMessage = false;
                                                                System.out.println("invalid catch");
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);


                                                            }

                                                            //sending to server quantityBuying


                                                            if (shoppingCartMessage) {

                                                                writer.write("" + quantityBuying);
                                                                writer.println();
                                                                writer.flush();


                                                                String productName = productBought.getProductName();
                                                                String storeName = productBought.getStoreName();
                                                                String description = productBought.getDescriptionOfProduct();
                                                                int quantityAvailable = productBought.getQuantityAvailable();
                                                                double price = productBought.getPrice();


                                                                try {
                                                                    String successCart = reader.readLine();
                                                                    if (successCart == null) {
                                                                        writer.write("null");
                                                                        writer.println();
                                                                        writer.flush();
                                                                        System.out.println("error fix");
                                                                        productChosen.setVisible(false);
                                                                        return;

                                                                    } else {
                                                                        if (successCart.equals("addedToCart")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " added to your shopping cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityBuying);
                                                                            shoppingCart.add(addedProductToCart);

                                                                        } else if (successCart.equals("soldOut")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " sold out! Can't add to cart",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        } else if (successCart.equals("limitedQuantity")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " only " + productBought.getQuantityAvailable() + " quantity available to add to cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityAvailable);
                                                                            shoppingCart.add(addedProductToCart);
                                                                        }
                                                                        //TESTING IF QUANTITY CAN NOT BE ADDED
                                                                    }
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("4: add cart");
                                                        }

                                                    });
                                                    // CHECK: shouldn't need to send anything to server? Just exit out of panel


                                                    panels.add(buyProduct);
                                                    panels.add(addToCart);


                                                    productChosen.add(panels, BorderLayout.CENTER);
                                                    productChosen.setVisible(true);


                                                }
                                                System.out.println("2 sort");

                                            });

                                            sortByMaxPrice.addActionListener(f -> {
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                ArrayList<Product> copy = new ArrayList<>();
                                                copy = method.sortByMaxPrice(Methods.productsOnMarket);
                                                Methods.productsOnMarket.clear();
                                                Methods.productsOnMarket.addAll(copy);
                                                frame.setVisible(false);


                                                String[] products = new String[Methods.productsOnMarket.size()];
                                                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                    products[i] = Methods.productsOnMarket.get(i).statisticsToString();
                                                }

                                                String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                                                        "Market", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);

                                                System.out.println("show Up" + chooseProduct);
                                                if (chooseProduct == null) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();
                                                } else {

                                                    String[] findingNames = chooseProduct.split(",");
                                                    String productName = findingNames[0].substring(findingNames[0].indexOf(":") + 2);
                                                    String storeName = findingNames[1].substring(findingNames[1].indexOf(":") + 2);

                                                    //sending which product they chose
                                                    Product productBuying = null;
                                                    for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                        if (Methods.productsOnMarket.get(i).getProductName().equals(productName) &&
                                                                Methods.productsOnMarket.get(i).getStoreName().equals(storeName)) {
                                                            productBuying = Methods.productsOnMarket.get(i);
                                                        }
                                                    }

                                                    writer.write(chooseProduct);
                                                    writer.println();
                                                    writer.flush();


                                                    productBought = productBuying;
                                                    frame.setVisible(false);


                                                    System.out.println("3 panel pop up ");
                                                    //DEALING WITH BUYING, SHOPPING CART, INSIGHTS
                                                    JButton buyProduct;
                                                    JButton addToCart;
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


                                                    JPanel panels = new JPanel();
                                                    productChosen.setLayout(new BorderLayout());

                                                    productChosen.add(productStatistics, BorderLayout.CENTER);
                                                    productChosen.addWindowListener(new WindowAdapter() {
                                                        @Override
                                                        public void windowClosing(WindowEvent e) {
                                                            // Check if the window closing event is due to the dispose on close
                                                            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.dispose();
                                                            }
                                                            System.out.println("exit and print null product window");
                                                        }
                                                    });


                                                    //BUYING PRODUCT BUTTON
                                                    buyProduct.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            // implementation of buyProduct button
                                                            writer.write("buyProduct");
                                                            writer.println();
                                                            writer.flush();


                                                            //sending that clicked this button in SERVER


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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();

                                                                    System.out.println("null redo to do cart/buy again ");
                                                                    productChosen.setVisible(false);
                                                                } else {
                                                                    amountPurchasing = Integer.parseInt(answer);

                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid input! Please try again",
                                                                        "Buy Product", JOptionPane.ERROR_MESSAGE);
                                                                purchaseMessage = false;
                                                                System.out.println("invalid catch");

                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);

                                                            }


                                                            if (purchaseMessage) {


                                                                writer.write("" + amountPurchasing);
                                                                writer.println();
                                                                writer.flush();

                                                                Methods method = new Methods();


                                                                //TESTING


                                                                //TESTING


                                                                try {

                                                                    String productAvailability = reader.readLine();


                                                                    if (productAvailability.equals("soldOut")) {
                                                                        //SOLD OUT READ FROM SERVER
                                                                        JOptionPane.showMessageDialog(null, "Error! Product is sold out!",
                                                                                "Buy Product", JOptionPane.ERROR_MESSAGE);

                                                                    } else if (productAvailability.equals("purchaseLimit")) {
                                                                        int purchaseHistoryQuantityAvailable = productBought.getQuantityAvailable();
                                                                        // purchasing wanted > amount available
                                                                        JOptionPane.showMessageDialog(null, "Could only purchase " + productBought.getQuantityAvailable() + " products!\n "
                                                                                        + productBought.getProductName() + " purchased!",
                                                                                "Buy Product", JOptionPane.INFORMATION_MESSAGE);

                                                                        method.purchaseProduct(productBought, productBought.getQuantityAvailable());
                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), purchaseHistoryQuantityAvailable);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);

                                                                    } else if (productAvailability.equals("itemPurchased")) {
                                                                        //item correctly purchased in the server
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " purchased!", "Buy Products",
                                                                                JOptionPane.INFORMATION_MESSAGE);

                                                                        method.purchaseProduct(productBought, amountPurchasing);
                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), amountPurchasing);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);

                                                                    }
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("4: buy product");


                                                        }

                                                    });

                                                    addToCart.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            //sending to server button clicked
                                                            writer.write("addToCart");
                                                            writer.println();
                                                            writer.flush();

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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();

                                                                    System.out.println("null redo to do cart/buy again ");
                                                                    productChosen.setVisible(false);

                                                                } else {
                                                                    quantityBuying = Integer.parseInt(answer);

                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid Input! Please try again",
                                                                        "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                shoppingCartMessage = false;
                                                                System.out.println("invalid catch");

                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);


                                                            }

                                                            //sending to server quantityBuying


                                                            if (shoppingCartMessage) {

                                                                writer.write("" + quantityBuying);
                                                                writer.println();
                                                                writer.flush();


                                                                String productName = productBought.getProductName();
                                                                String storeName = productBought.getStoreName();
                                                                String description = productBought.getDescriptionOfProduct();
                                                                int quantityAvailable = productBought.getQuantityAvailable();
                                                                double price = productBought.getPrice();


                                                                try {
                                                                    String successCart = reader.readLine();
                                                                    if (successCart == null) {
                                                                        writer.write("null");
                                                                        writer.println();
                                                                        writer.flush();
                                                                        System.out.println("error fix");
                                                                        productChosen.setVisible(false);
                                                                        return;

                                                                    } else {
                                                                        if (successCart.equals("addedToCart")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " added to your shopping cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityBuying);
                                                                            shoppingCart.add(addedProductToCart);

                                                                        } else if (successCart.equals("soldOut")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " sold out! Can't add to cart",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        } else if (successCart.equals("limitedQuantity")) {
                                                                            JOptionPane.showMessageDialog(null, productBought.getProductName() + " only " + productBought.getQuantityAvailable() + " quantity available to add to cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityAvailable);
                                                                            shoppingCart.add(addedProductToCart);
                                                                        }
                                                                        //TESTING IF QUANTITY CAN NOT BE ADDED
                                                                    }
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("4: add cart");
                                                        }

                                                    });
                                                    // CHECK: shouldn't need to send anything to server? Just exit out of panel


                                                    panels.add(buyProduct);
                                                    panels.add(addToCart);


                                                    productChosen.add(panels, BorderLayout.CENTER);
                                                    productChosen.setVisible(true);


                                                }
                                                System.out.println("2 sort");
                                            });


                                            //if exiting... buttonClicked will be null
                                        }
                                        if (sortResponse == 1) {
                                            Methods.productsOnMarket = method.makeProductArrayList();
                                            boolean newPage = false;

                                            String[] products = new String[Methods.productsOnMarket.size()];
                                            for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                products[i] = Methods.productsOnMarket.get(i).statisticsToString();
                                            }

                                            String chooseProduct = (String) JOptionPane.showInputDialog(null, "Products on market!",
                                                    "Market", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                                            if (chooseProduct == null) {
                                                writer.write("null");
                                                writer.println();
                                                writer.flush();
                                            } else {
                                                String[] findingNames = chooseProduct.split(",");
                                                String productName = findingNames[0].substring(findingNames[0].indexOf(":") + 2);
                                                String storeName = findingNames[1].substring(findingNames[1].indexOf(":") + 2);

                                                //sending which product they chose
                                                Product productBuying = null;
                                                for (int i = 0; i < Methods.productsOnMarket.size(); i++) {
                                                    if (Methods.productsOnMarket.get(i).getProductName().equals(productName) &&
                                                            Methods.productsOnMarket.get(i).getStoreName().equals(storeName)) {
                                                        productBuying = Methods.productsOnMarket.get(i);
                                                    }
                                                }

                                                writer.write(chooseProduct);
                                                writer.println();
                                                writer.flush();


                                                productBought = productBuying;

                                                System.out.println("2 market no sort ");


                                                //start of 5 time repeat use buyer/cart window for chooosing product
                                                // variable productBuying is the product they chose

                                                System.out.println("3 panel pop up ");
                                                //DEALING WITH BUYING, SHOPPING CART, INSIGHTS
                                                JButton buyProduct;
                                                JButton addToCart;
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


                                                JPanel panels = new JPanel();
                                                productChosen.setLayout(new BorderLayout());

                                                productChosen.add(productStatistics, BorderLayout.CENTER);
                                                productChosen.addWindowListener(new WindowAdapter() {
                                                    @Override
                                                    public void windowClosing(WindowEvent e) {
                                                        // Check if the window closing event is due to the dispose on close
                                                        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                            writer.write("null");
                                                            writer.println();
                                                            writer.flush();
                                                            productChosen.dispose();
                                                        }
                                                        System.out.println("exit and print null product window");
                                                    }
                                                });


                                                //BUYING PRODUCT BUTTON
                                                buyProduct.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        Methods.productsOnMarket = method.makeProductArrayList();
                                                        // implementation of buyProduct button
                                                        writer.write("buyProduct");
                                                        writer.println();
                                                        writer.flush();

                                                        //sending that clicked this button in SERVER


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
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();


                                                                System.out.println("null redo to do cart/buy again ");
                                                                productChosen.setVisible(false);
                                                            } else {
                                                                amountPurchasing = Integer.parseInt(answer);

                                                            }


                                                        } catch (NumberFormatException f) {
                                                            JOptionPane.showMessageDialog(null, "Invalid input! Please try again",
                                                                    "Buy Product", JOptionPane.ERROR_MESSAGE);
                                                            purchaseMessage = false;
                                                            System.out.println("invalid catch");

                                                            writer.write("null");
                                                            writer.println();
                                                            writer.flush();
                                                            productChosen.setVisible(false);

                                                        }


                                                        if (purchaseMessage) {


                                                            writer.write("" + amountPurchasing);
                                                            writer.println();
                                                            writer.flush();

                                                            Methods method = new Methods();


                                                            //TESTING


                                                            //TESTING


                                                            try {

                                                                String productAvailability = reader.readLine();


                                                                if (productAvailability.equals("soldOut")) {
                                                                    //SOLD OUT READ FROM SERVER
                                                                    JOptionPane.showMessageDialog(null, "Error! Product is sold out!",
                                                                            "Buy Product", JOptionPane.ERROR_MESSAGE);

                                                                } else if (productAvailability.equals("purchaseLimit")) {
                                                                    int purchaseHistoryQuantityAvailable = productBought.getQuantityAvailable();
                                                                    // purchasing wanted > amount available
                                                                    JOptionPane.showMessageDialog(null, "Could only purchase " + productBought.getQuantityAvailable() + " products!\n "
                                                                                    + productBought.getProductName() + " purchased!",
                                                                            "Buy Product", JOptionPane.INFORMATION_MESSAGE);

                                                                    method.purchaseProduct(productBought, productBought.getQuantityAvailable());
                                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                                        if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                            shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                        }
                                                                    }


                                                                    SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                            productBought.getQuantityAvailable(), productBought.getPrice(), purchaseHistoryQuantityAvailable);
                                                                    customerHistory.add(addedProductHistory);
                                                                    method.saveCustomerHistory(customerHistory, userAccount);

                                                                } else if (productAvailability.equals("itemPurchased")) {
                                                                    //item correctly purchased in the server
                                                                    JOptionPane.showMessageDialog(null, productBought.getProductName() + " purchased!", "Buy Products",
                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                    method.purchaseProduct(productBought, amountPurchasing);
                                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                                        if (shoppingCart.get(i).getStoreName().equals(productBought.getStoreName()) &&
                                                                                shoppingCart.get(i).getProductName().equals(productBought.getProductName())) {
                                                                            shoppingCart.get(i).setQuantityAvailable(productBought.getQuantityAvailable());

                                                                        }
                                                                    }


                                                                    SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                            productBought.getQuantityAvailable(), productBought.getPrice(), amountPurchasing);
                                                                    customerHistory.add(addedProductHistory);
                                                                    method.saveCustomerHistory(customerHistory, userAccount);

                                                                }
                                                            } catch (IOException ex) {
                                                                ex.printStackTrace();
                                                            }


                                                        } else {
                                                            return;
                                                        }
                                                        productChosen.setVisible(false);
                                                        System.out.println("4: buy product");


                                                    }

                                                });

                                                addToCart.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        Methods.productsOnMarket = method.makeProductArrayList();
                                                        //sending to server button clicked
                                                        writer.write("addToCart");
                                                        writer.println();
                                                        writer.flush();

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
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();

                                                                System.out.println("null redo to do cart/buy again ");
                                                                productChosen.setVisible(false);

                                                            } else {
                                                                quantityBuying = Integer.parseInt(answer);

                                                            }


                                                        } catch (NumberFormatException f) {
                                                            JOptionPane.showMessageDialog(null, "Invalid Input! Please try again",
                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                            shoppingCartMessage = false;
                                                            System.out.println("invalid catch");

                                                            writer.write("null");
                                                            writer.println();
                                                            writer.flush();
                                                            productChosen.setVisible(false);

                                                        }

                                                        //sending to server quantityBuying


                                                        if (shoppingCartMessage) {

                                                            writer.write("" + quantityBuying);
                                                            writer.println();
                                                            writer.flush();


                                                            String productName = productBought.getProductName();
                                                            String storeName = productBought.getStoreName();
                                                            String description = productBought.getDescriptionOfProduct();
                                                            int quantityAvailable = productBought.getQuantityAvailable();
                                                            double price = productBought.getPrice();


                                                            try {
                                                                String successCart = reader.readLine();
                                                                if (successCart == null) {
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();
                                                                    System.out.println("error fix");
                                                                    productChosen.setVisible(false);
                                                                    return;

                                                                } else {
                                                                    if (successCart.equals("addedToCart")) {
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " added to your shopping cart!",
                                                                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                price, quantityBuying);
                                                                        shoppingCart.add(addedProductToCart);

                                                                    } else if (successCart.equals("soldOut")) {
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " sold out! Can't add to cart",
                                                                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                    } else if (successCart.equals("limitedQuantity")) {
                                                                        JOptionPane.showMessageDialog(null, productBought.getProductName() + " only " + productBought.getQuantityAvailable() + " quantity available to add to cart!",
                                                                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                price, quantityAvailable);
                                                                        shoppingCart.add(addedProductToCart);
                                                                    }
                                                                    //TESTING IF QUANTITY CAN NOT BE ADDED
                                                                }
                                                            } catch (IOException ex) {
                                                                ex.printStackTrace();
                                                            }


                                                        } else {
                                                            return;
                                                        }
                                                        productChosen.setVisible(false);
                                                        System.out.println("4: add cart");
                                                    }

                                                });
                                                // CHECK: shouldn't need to send anything to server? Just exit out of panel


                                                panels.add(buyProduct);
                                                panels.add(addToCart);


                                                productChosen.add(panels, BorderLayout.CENTER);
                                                productChosen.setVisible(true);
                                            }
                                            //CHECK


                                            //end of 5 time repeat use buyer/cart window for chooosing product


                                        }


                                    }


                                    // customerGui.SeeProducts(userAccount, shoppingCart);


                                    //add implementation of seeing products


                                }

                            });

                            //OTHER BUTTON

                            searchProductsButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    Methods.productsOnMarket = method.makeProductArrayList();
                                    writer.write("searchProductsButton");
                                    writer.println();
                                    writer.flush();
                                    System.out.println("1 search product button pressed ");
                                    // TODO: Handle sell button action

                                    Product productBought = null;
                                    boolean newPage = false;
                                    Methods method = new Methods();
                                    String wordSearch = null;

                                    wordSearch = JOptionPane.showInputDialog(null, "What word would you like to input to search?",
                                            "Search Product", JOptionPane.QUESTION_MESSAGE);

                                    if (wordSearch == null) {
                                        writer.write("");
                                        writer.println();
                                        writer.flush();
                                        // Exit the method
                                    } else {


                                        writer.write(wordSearch);
                                        writer.println();
                                        writer.flush();


                                        try {
                                            String foundProducts = reader.readLine();
                                            System.out.println(foundProducts);

                                            if (foundProducts.equals("noProductsFound")) {
                                                JOptionPane.showMessageDialog(null, "Sorry! No products matched your search!",
                                                        "Search Product", JOptionPane.INFORMATION_MESSAGE);
                                            } else if (foundProducts.equals("productsFound")) {
                                                String productsBrokenUp = reader.readLine();
                                                System.out.println(productsBrokenUp);

                                                String[] products = productsBrokenUp.split("@@");

                                                String itemFromSearchChosen = (String) JOptionPane.showInputDialog(null, "Products that matched your search!",
                                                        "See Products", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                                                if (itemFromSearchChosen == null) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();
                                                } else {
                                                    System.out.println("item from chosen" + itemFromSearchChosen);

                                                    //sending back item chosen to server
                                                    writer.write(itemFromSearchChosen);
                                                    writer.println();
                                                    writer.flush();


                                                    String[] productChangedNames = itemFromSearchChosen.split(",");
                                                    String findProductName = productChangedNames[0].substring(productChangedNames[0].indexOf(":") + 2);
                                                    String findStoreName = productChangedNames[1].substring(productChangedNames[1].indexOf(":") + 2);
                                                    System.out.println("product name " + findProductName);
                                                    System.out.println("store name: " + findStoreName);


                                                    for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
                                                        if (Methods.productsOnMarket.get(k).getProductName().equals(findProductName) &&
                                                                Methods.productsOnMarket.get(k).getStoreName().equals(findStoreName)) {
                                                            productBoughtNew = Methods.productsOnMarket.get(k);
                                                            //product insight page
                                                        }
                                                    }


                                                    //DEALING WITH BUYING, SHOPPING CART, INSIGHTS
                                                    JButton buyProduct;
                                                    JButton addToCart;

                                                    String productDescription = "<html>"
                                                            + "Product Name: " + productBoughtNew.getProductName() + "<br>"
                                                            + "Store: " + productBoughtNew.getStoreName() + "<br>"
                                                            + "Description: " + productBoughtNew.getDescriptionOfProduct() + "<br>"
                                                            + "Available Quantity: " + productBoughtNew.getQuantityAvailable() + "<br>"
                                                            + "Price: " + productBoughtNew.getPrice() + "<br>"
                                                            + "</html>";
                                                    JLabel productStatistics = new JLabel(productDescription, JLabel.CENTER);
                                                    productStatistics.setSize(600, 300);
                                                    productStatistics.setHorizontalAlignment(JLabel.CENTER);
                                                    JFrame productChosen = new JFrame();


                                                    productChosen.setTitle(productBoughtNew.getProductName());
                                                    productChosen.setSize(600, 300);
                                                    productChosen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                    productChosen.setLocationRelativeTo(null);

                                                    buyProduct = new JButton("Buy Product");
                                                    addToCart = new JButton("Add Product to Cart");


                                                    JPanel panels = new JPanel();
                                                    productChosen.setLayout(new BorderLayout());

                                                    productChosen.add(productStatistics, BorderLayout.CENTER);

                                                    productChosen.addWindowListener(new WindowAdapter() {
                                                        @Override
                                                        public void windowClosing(WindowEvent e) {
                                                            // Check if the window closing event is due to the dispose on close
                                                            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.dispose();
                                                            }
                                                            System.out.println("exit and print null closed entire window to view product");
                                                        }
                                                    });


                                                    //BUYING PRODUCT BUTTON
                                                    buyProduct.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            // implementation of buyProduct button
                                                            writer.write("buyProductSearch");
                                                            writer.println();
                                                            writer.flush();
                                                            //sending that clicked this button in SERVER


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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();
                                                                    productChosen.setVisible(false);

                                                                    System.out.println("answer is null");
                                                                } else {
                                                                    amountPurchasing = Integer.parseInt(answer);
                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid input! Please try again",
                                                                        "Buy Product", JOptionPane.ERROR_MESSAGE);
                                                                purchaseMessage = false;
                                                                System.out.println("invalid catch");

                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);

                                                            }

                                                            if (purchaseMessage) {
                                                                System.out.println("sending amount purchasing" + amountPurchasing);
                                                                writer.write("" + amountPurchasing);
                                                                writer.println();
                                                                writer.flush();

                                                                Methods method = new Methods();


                                                                try {
                                                                    String productAvailability = reader.readLine();
                                                                    if (productAvailability.equals("soldOut")) {
                                                                        //SOLD OUT READ FROM SERVER
                                                                        JOptionPane.showMessageDialog(null, "Error! Product is sold out!",
                                                                                "Buy Product", JOptionPane.ERROR_MESSAGE);
                                                                    } else if (productAvailability.equals("purchaseLimit")) {
                                                                        int purchaseHistoryQuantityAvailable = productBought.getQuantityAvailable();
                                                                        // purchasing wanted > amount available
                                                                        JOptionPane.showMessageDialog(null, "Could only purchase " + productBoughtNew.getQuantityAvailable() + " products!\n "
                                                                                        + productBoughtNew.getProductName() + " purchased!",
                                                                                "Buy Product", JOptionPane.INFORMATION_MESSAGE);
                                                                        method.purchaseProduct(productBoughtNew, productBoughtNew.getQuantityAvailable());
                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBoughtNew.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBoughtNew.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBoughtNew.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), purchaseHistoryQuantityAvailable);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);

                                                                    } else if (productAvailability.equals("itemPurchased")) {
                                                                        //item correctly purchased in the server
                                                                        JOptionPane.showMessageDialog(null, productBoughtNew.getProductName() + " purchased!", "Buy Products",
                                                                                JOptionPane.INFORMATION_MESSAGE);
                                                                        method.purchaseProduct(productBoughtNew, amountPurchasing);
                                                                        for (int i = 0; i < shoppingCart.size(); i++) {
                                                                            if (shoppingCart.get(i).getStoreName().equals(productBoughtNew.getStoreName()) &&
                                                                                    shoppingCart.get(i).getProductName().equals(productBoughtNew.getProductName())) {
                                                                                shoppingCart.get(i).setQuantityAvailable(productBoughtNew.getQuantityAvailable());

                                                                            }
                                                                        }


                                                                        SoldProduct addedProductHistory = new SoldProduct(productBought.getProductName(), productBought.getStoreName(), productBought.getDescriptionOfProduct(),
                                                                                productBought.getQuantityAvailable(), productBought.getPrice(), amountPurchasing);
                                                                        customerHistory.add(addedProductHistory);
                                                                        method.saveCustomerHistory(customerHistory, userAccount);

                                                                    }
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("buy product");


                                                        }
                                                    });

                                                    addToCart.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            Methods.productsOnMarket = method.makeProductArrayList();
                                                            //sending to server button clicked
                                                            writer.write("addToCartSearch");
                                                            writer.println();
                                                            writer.flush();

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
                                                                    writer.write("null");
                                                                    writer.println();
                                                                    writer.flush();
                                                                    System.out.println("null redo to do cart/buy again");
                                                                    productChosen.setVisible(false);
                                                                } else {
                                                                    quantityBuying = Integer.parseInt(answer);

                                                                }


                                                            } catch (NumberFormatException f) {
                                                                JOptionPane.showMessageDialog(null, "Invalid Input! Please try again",
                                                                        "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                shoppingCartMessage = false;
                                                                System.out.println("invalid catch");
                                                                writer.write("null");
                                                                writer.println();
                                                                writer.flush();
                                                                productChosen.setVisible(false);


                                                            }

                                                            //sending to server quantityBuying


                                                            if (shoppingCartMessage) {

                                                                writer.write("" + quantityBuying);
                                                                writer.println();
                                                                writer.flush();


                                                                String productName = productBoughtNew.getProductName();
                                                                String storeName = productBoughtNew.getStoreName();
                                                                String description = productBoughtNew.getDescriptionOfProduct();
                                                                int quantityAvailable = productBoughtNew.getQuantityAvailable();
                                                                double price = productBoughtNew.getPrice();


                                                                try {
                                                                    String successCart = reader.readLine();
                                                                    if (successCart == null) {
                                                                        writer.write("null");
                                                                        writer.println();
                                                                        writer.flush();
                                                                        System.out.println("error null");
                                                                        productChosen.setVisible(false);
                                                                        return;
                                                                    } else {
                                                                        if (successCart.equals("addedToCart")) {
                                                                            JOptionPane.showMessageDialog(null, productBoughtNew.getProductName() + " added to your shopping cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityBuying);
                                                                            shoppingCart.add(addedProductToCart);

                                                                        } else if (successCart.equals("soldOut")) {
                                                                            JOptionPane.showMessageDialog(null, productBoughtNew.getProductName() + " sold out! Can't add to cart",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                        } else if (successCart.equals("limitedQuantity")) {
                                                                            JOptionPane.showMessageDialog(null, productBoughtNew.getProductName() + " only " + productBoughtNew.getQuantityAvailable() +
                                                                                            " quantity available to add to cart!",
                                                                                    "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                                                                            ShoppingCartProduct addedProductToCart = new ShoppingCartProduct(productName, storeName, description, quantityAvailable,
                                                                                    price, quantityAvailable);
                                                                            shoppingCart.add(addedProductToCart);
                                                                        }
                                                                    }
                                                                    //TESTING IF QUANTITY CAN NOT BE ADDED
                                                                } catch (IOException ex) {
                                                                    ex.printStackTrace();
                                                                }


                                                            } else {
                                                                return;
                                                            }
                                                            productChosen.setVisible(false);
                                                            System.out.println("add cart");
                                                        }

                                                    });

                                                    // CHECK: shouldn't need to send anything to server? Just exit out of panel


                                                    panels.add(buyProduct);
                                                    panels.add(addToCart);


                                                    productChosen.add(panels, BorderLayout.CENTER);
                                                    productChosen.setVisible(true);


                                                }
                                            }


                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }

                                    }

                                }


                            });

                            viewShoppingCartButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    shoppingCart = method.createShoppingCartArray(userAccount);
                                    Methods.productsOnMarket = method.makeProductArrayList();
                                    // must use shopping cart method in GUITEST
                                    //*** THIS STILL MUST IMPLEMENT CUSTOMER RECEIPTS WHEN PURCHASING


                                    //write to server view cart pressed
                                    writer.write("viewShoppingCart");
                                    writer.println();
                                    writer.flush();

                                    Methods method = new Methods();

                                    if (shoppingCart.isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "Nothing in your shopping cart!",
                                                "Search Product", JOptionPane.INFORMATION_MESSAGE);
                                        writer.write("emptyCart");
                                        writer.println();
                                        writer.flush();
                                        System.out.println("empty cart print null");
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

                                        System.out.println("view cart");

                                        frame.addWindowListener(new WindowAdapter() {
                                            @Override
                                            public void windowClosing(WindowEvent e) {
                                                // Check if the window closing event is due to the dispose on close
                                                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();
                                                    frame.dispose();
                                                }
                                                System.out.println("exit and print null entire product window ");
                                            }
                                        });


                                        viewCart.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                shoppingCart = method.createShoppingCartArray(userAccount);
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                writer.write("viewCart");
                                                writer.println();
                                                writer.flush();


                                                String[] products = new String[shoppingCart.size()];


                                                for (int i = 0; i < shoppingCart.size(); i++) {
                                                    products[i] = shoppingCart.get(i).shoppingCartStatisticsToString();
                                                    System.out.println("test for shopping cart" + products[i]);

                                                }


                                                String seeProduct = (String) JOptionPane.showInputDialog(null, "Shopping Cart!",
                                                        "View Cart", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                                                if (seeProduct == null) {
                                                    // previously had a return??
                                                } else {
                                                    //CHECK
                                                    String[] findingNames = seeProduct.split(",");
                                                    String[] findProduct = findingNames[0].split(":");
                                                    String productName = findProduct[2].substring(1);
                                                    String storeName = findingNames[1].substring(findingNames[1].indexOf(":") + 2);

                                                    ShoppingCartProduct productChosen = null;
                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                        if (shoppingCart.get(i).getStoreName().equals(storeName) &&
                                                                shoppingCart.get(i).getProductName().equals(productName)) {
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
                                                frame.setVisible(false);
                                            }
                                        });


                                        removeProduct.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                shoppingCart = method.createShoppingCartArray(userAccount);
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                writer.write("removeProduct");
                                                writer.println();
                                                writer.flush();


                                                String[] products = new String[shoppingCart.size()];

                                                for (int i = 0; i < shoppingCart.size(); i++) {
                                                    products[i] = shoppingCart.get(i).shoppingCartStatisticsToString();
                                                    System.out.println("repeat test remove product \n" + products[i]);
                                                }

                                                // shows cart and choose which product to remove
                                                String seeProduct = (String) JOptionPane.showInputDialog(null, "Remove Product From Cart!",
                                                        "Shopping Cart", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
                                                if (seeProduct == null) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();
                                                    System.out.println("remove product null exit");

                                                } else {
                                                    System.out.println("see Product " + seeProduct + "end ");

                                                    System.out.println("remove product");

                                                    writer.write(seeProduct);
                                                    writer.println();
                                                    writer.flush();
                                                    //writing to server the product

                                                    ShoppingCartProduct productChosen = null;
                                                    String[] product = seeProduct.split(",");
                                                    String[] findProductName = product[0].split(":");

                                                    String productName = findProductName[2].substring(1);
                                                    String storeName = product[1].substring(product[1].indexOf(":") + 2);
                                                    int quantityBuying = Integer.parseInt(product[5].substring(product[5].indexOf(":") + 2));


                                                    for (int i = 0; i < shoppingCart.size(); i++) {
                                                        if (shoppingCart.get(i).getProductName().equals(productName) &&
                                                                shoppingCart.get(i).getStoreName().equals(storeName) &&
                                                                shoppingCart.get(i).getQuantityBuying() == quantityBuying) {
                                                            productChosen = shoppingCart.get(i);
                                                        }
                                                    }


                                                    assert productChosen != null;
                                                    JOptionPane.showMessageDialog(null, productChosen.getProductName() + " removed from cart!",
                                                            "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);


                                                    shoppingCart.remove(productChosen);

                                                    //save into file remove from shopping cart


                                                    if (shoppingCart.isEmpty()) {
                                                        frame.dispose();
                                                    }

                                                    frame.setVisible(false);
                                                }
                                            }
                                        });


                                        purchaseCart.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                shoppingCart = method.createShoppingCartArray(userAccount);
                                                Methods.productsOnMarket = method.makeProductArrayList();
                                                System.out.println("purchase cart pressed");
                                                writer.write("purchaseCart");
                                                writer.println();
                                                writer.flush();

                                                for (int i = 0; i < shoppingCart.size(); i++) {
                                                    if (shoppingCart.get(i).getQuantityBuying() > shoppingCart.get(i).getQuantityAvailable()) {
                                                        for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
                                                            //changing quantity in cart in client view
                                                            if (Methods.productsOnMarket.get(k).getProductName().equals(shoppingCart.get(i).getProductName())
                                                                    && Methods.productsOnMarket.get(k).getStoreName().equals(shoppingCart.get(i).getStoreName())) {
                                                                shoppingCart.get(i).setQuantityAvailable(Methods.productsOnMarket.get(k).getQuantityAvailable());

                                                                int purchaseHistoryQuantityAvailable = Methods.productsOnMarket.get(k).getQuantityAvailable();


                                                                Methods.productsOnMarket.get(k).setQuantityAvailable(0);
                                                                shoppingCart.get(i).setQuantityAvailable(0);
                                                                System.out.println("testing quantity" + Methods.productsOnMarket.get(k).getQuantityAvailable());
                                                                ///INSERT CUSTOMER RECIEPTS HERE


                                                                SoldProduct addedProductHistory = new SoldProduct(Methods.productsOnMarket.get(k).getProductName(), Methods.productsOnMarket.get(k).getStoreName(),
                                                                        Methods.productsOnMarket.get(k).getDescriptionOfProduct(), Methods.productsOnMarket.get(k).getQuantityAvailable(), Methods.productsOnMarket.get(k).getPrice(),
                                                                        purchaseHistoryQuantityAvailable);
                                                                customerHistory.add(addedProductHistory);
                                                                method.saveCustomerHistory(customerHistory, userAccount);


                                                            }
                                                        }
                                                    } else if (shoppingCart.get(i).getQuantityAvailable() == 0) {
                                                        //nothing
                                                    } else {


                                                        for (int k = 0; k < Methods.productsOnMarket.size(); k++) {
                                                            if (Methods.productsOnMarket.get(k).getProductName().equals(shoppingCart.get(i).getProductName())
                                                                    && Methods.productsOnMarket.get(k).getStoreName().equals(shoppingCart.get(i).getStoreName())) {
                                                                Methods.productsOnMarket.get(k).setQuantityAvailable(Methods.productsOnMarket.get(k).getQuantityAvailable() - shoppingCart.get(i).getQuantityBuying());
                                                                shoppingCart.get(i).setQuantityAvailable(Methods.productsOnMarket.get(k).getQuantityAvailable());
                                                                System.out.println("testing quantity on client: " + Methods.productsOnMarket.get(k).getQuantityAvailable());

                                                                SoldProduct addedProductHistory = new SoldProduct(Methods.productsOnMarket.get(k).getProductName(), Methods.productsOnMarket.get(k).getStoreName(),
                                                                        Methods.productsOnMarket.get(k).getDescriptionOfProduct(), Methods.productsOnMarket.get(k).getQuantityAvailable(), Methods.productsOnMarket.get(k).getPrice(),
                                                                        shoppingCart.get(i).getQuantityBuying());
                                                                customerHistory.add(addedProductHistory);
                                                                method.saveCustomerHistory(customerHistory, userAccount);
                                                            }

                                                        }

                                                    }
                                                }
                                                shoppingCart.clear();


                                                //after everything is purchased

                                                try {
                                                    String purchased = reader.readLine();
                                                    System.out.println("purchased full mesages test " + purchased);
                                                    if (purchased.equals("null")) {
                                                        //purchased not pressed
                                                        System.out.println("null error fix ");

                                                    } else {
                                                        String[] messages = purchased.split("@@");
                                                        if (messages[0].equals(" ")) {
                                                            System.out.println("purchased is nothing purchased");


                                                        } else {
                                                            JOptionPane.showMessageDialog(null, messages[0].substring(0, messages[0].length() - 2));
                                                        }
                                                        if (messages[1].equals(" ")) {

                                                        } else {
                                                            JOptionPane.showMessageDialog(null, messages[1].substring(0, messages[1].length() - 2));
                                                        }
                                                        if (messages[2].equals(" ")) {

                                                        } else {
                                                            JOptionPane.showMessageDialog(null, messages[2].substring(0, messages[2].length() - 2));
                                                            System.out.println("message purchased cart: " + messages[2]);
                                                        }
                                                    }


                                                } catch (IOException ex) {
                                                    ex.printStackTrace();
                                                }

                                                frame.dispose();

                                                frame.setVisible(false);

                                            }

                                        });

                                        frame.addWindowListener(new WindowAdapter() {
                                            @Override
                                            public void windowClosing(WindowEvent e) {
                                                // Check if the window closing event is due to the dispose on close
                                                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                    writer.write("null");
                                                    writer.println();
                                                    writer.flush();
                                                    frame.dispose();
                                                }
                                                System.out.println("exit and print null exit cart entire window");
                                            }
                                        });


                                    }

                                }
                            });

                            viewPurchaseHistoryButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    Methods methods = new Methods();
                                    ArrayList<SoldProduct> history = methods.makeCustomerHistory(userAccount);
                                    String[] historyString = new String[history.size()];
                                    for (int i = 0; i < history.size(); i++) {
                                        historyString[i] = history.get(i).customerRecieptStatistics();
                                    }
                                    if (history.isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "No History!", "Customer History",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        System.out.println("empty history print null");
                                    } else {
                                        JOptionPane.showInputDialog(null, "Purchase History",
                                                "Customer History", JOptionPane.QUESTION_MESSAGE, null, historyString, historyString[0]);
                                        System.out.println("customer background shown");
                                    }
                                }
                            });


                            editProfileButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    writer.write("editProfileButton");
                                    writer.println();
                                    writer.flush();

                                    int message = JOptionPane.showOptionDialog(null, "Leave fields empty if you would like to keep anything", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                                    if (message == -1) {
                                        writer.write("null");
                                        writer.println();
                                        writer.flush();

                                    } else {

                                        JPanel panel = new JPanel(new GridLayout(5, 2));

                                        panel.add(new JLabel("New Name:"));
                                        JTextField newNameField = new JTextField();
                                        panel.add(newNameField);

                                        panel.add(new JLabel("New Email:"));
                                        JTextField newEmailField = new JTextField();
                                        panel.add(newEmailField);

                                        panel.add(new JLabel("New Password:"));
                                        JTextField newPasswordField = new JTextField();
                                        panel.add(newPasswordField);

                                        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Product Details",
                                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                                            writer.write("null");
                                            writer.println();
                                            writer.flush();
                                        } else if (result == JOptionPane.OK_OPTION) {
                                            String newUserName = newNameField.getText();
                                            String newUserEmail = newEmailField.getText();
                                            String newUserPassword = newPasswordField.getText();

                                            if (newUserName.isEmpty()) {
                                                newUserName = userAccount.getName();
                                            } else {
                                                userAccount.setName(newUserName);
                                            }

                                            if (newUserEmail.isEmpty()) {
                                                newUserEmail = userAccount.getEmail();
                                            } else {
                                                userAccount.setEmail(newUserEmail);
                                            }

                                            if (newUserPassword.isEmpty()) {
                                                newUserPassword = userAccount.getPassword();
                                            } else {
                                                userAccount.setPassword(newUserPassword);
                                            }

                                            String combined = newUserName + "," + newUserEmail + "," + newUserPassword;
                                            writer.write(combined);
                                            writer.println();
                                            writer.flush();

                                            System.out.println(combined);

                                            JOptionPane.showMessageDialog(null, "Your product has been edited!", "Successfully Edited", JOptionPane.INFORMATION_MESSAGE);


                                        }
                                    }
                                }
                            });


                        }
                    }
                }


            });


            //send info to server


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}


