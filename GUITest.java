import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class GUITest extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public GUITest() {
        setTitle("Sign In");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);

        JButton signInButton = createSignInButton();
        JButton createAccountButton = new JButton("Create Account");

        setLayout(new GridLayout(3, 2));

        add(emailLabel);
        add(emailField);

        add(passwordLabel);
        add(passwordField);

        add(signInButton);
        add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateAccountDialog();
            }
        });

    }

    private void showCreateAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        //Only allows one to be selected
        //More info here: https://codehs.com/tutorial/david/java-swing-buttons-layout
        JRadioButton sellerRadioButton = new JRadioButton("Seller");
        JRadioButton buyerRadioButton = new JRadioButton("Buyer");

        ButtonGroup group = new ButtonGroup();
        group.add(sellerRadioButton);
        group.add(buyerRadioButton);

        panel.add(sellerRadioButton);
        panel.add(buyerRadioButton);

        int result = JOptionPane.showConfirmDialog(null, panel, "Create Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            boolean isSeller = sellerRadioButton.isSelected();

            if (!name.isEmpty() && !email.isEmpty() && password.length() > 0) {
                saveNewAccount(name, email, password, isSeller);
                JOptionPane.showMessageDialog(null, "Account created successfully!", "Account Created",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input. Please fill in all fields.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveNewAccount(String name, String email, String password, boolean isSeller) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
            writer.write(String.format("%s,%s,%s,%b;", name, email, password, isSeller));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JButton createSignInButton() {
        JButton signInButton = new JButton("Sign In");

        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] signInResult = signIn();

                if (signInResult[0].equals("true") && signInResult[1].equals("seller")) {
                    openSellerMarketplaceHomePage();
                } else if (signInResult[0].equals("true") && signInResult[1].equals("buyer")) {
                    openBuyerMarketPlaceHomePage();
                }
            }
        });

        return signInButton;
    }

    private void openSellerMarketplaceHomePage() {
        SellerMarketplaceHomePage sellerMarketplaceHomePage = new SellerMarketplaceHomePage();
        sellerMarketplaceHomePage.setVisible(true);
        this.setVisible(false);
    }

    private void openBuyerMarketPlaceHomePage() {
        BuyerMarketPlaceHomePage buyerMarketPlaceHomePage = new BuyerMarketPlaceHomePage();
        buyerMarketPlaceHomePage.setVisible(true);
        this.setVisible(false);
    }

    class BuyerMarketPlaceHomePage extends JFrame {
        private JButton seeProductsButton;
        private JButton searchProductsButton;
        private JButton viewShoppingCartButton;
        private JButton editProfileButton;
        private JButton viewPurchaseHistoryButton;
        private JButton exitButton;

        public BuyerMarketPlaceHomePage() {
            setTitle("Marketplace Home Page");
            setSize(600, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            seeProductsButton = new JButton("See Products");
            searchProductsButton = new JButton("Search Products");
            viewShoppingCartButton = new JButton("View Shopping Cart");
            editProfileButton = new JButton("Edit Profile");
            viewPurchaseHistoryButton = new JButton("View Purchase History");
            exitButton = new JButton("Exit");


            setLayout(new GridLayout(2, 3));

            add(seeProductsButton);
            add(searchProductsButton);
            add(viewShoppingCartButton);
            add(editProfileButton);
            add(viewPurchaseHistoryButton);
            add(exitButton);

            //TODO: Implement logic for buttons

            seeProductsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // TODO: Handle sell button action
                    JOptionPane.showMessageDialog(null, "See products button clicked!", "See Products", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }

    }

    class SellerMarketplaceHomePage extends JFrame {
        private JButton sellButton;
        private JButton editButton;
        private JButton deleteButton;
        private JButton importButton;
        private JButton exportButton;
        private JButton salesByStoreButton;
        private JButton editProfileButton;
        private JButton viewCustomerCartsButton;
        private JButton exitButton;

        public SellerMarketplaceHomePage() {
            setTitle("Marketplace Home Page");
            setSize(600, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            sellButton = new JButton("Sell");
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            importButton = new JButton("Import");
            exportButton = new JButton("Export");
            salesByStoreButton = new JButton("Sales By Store");
            editProfileButton = new JButton("Edit Profile");
            viewCustomerCartsButton = new JButton("View Customer Carts");
            exitButton = new JButton("Exit");

            setLayout(new GridLayout(3, 3));

            add(sellButton);
            add(editButton);
            add(deleteButton);
            add(importButton);
            add(exportButton);
            add(salesByStoreButton);
            add(editProfileButton);
            add(viewCustomerCartsButton);
            add(exitButton);

            //TODO: Implement logic for buttons

            sellButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
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
            });

            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });


        }
    }

    private String[] signIn() {
        boolean signInComplete = false;
        String[] info = new String[2];
        info[0] = "false";
        info[1] = "seller";


        String email = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

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
                // checking if user and password are the same
                if (oneUserData[1].equals(email) && oneUserData[2].equals(password)) {
                    // format ji,jo1234,1234,false;

                    // parse boolean for creating the user for sign-in
                    boolean sellerBoolean = false;
                    if (oneUserData[3].startsWith("true")) {
                        sellerBoolean = true;
                    } else if (oneUserData[3].startsWith("false")) {
                        sellerBoolean = false;
                    }
                    userAccount = new User(oneUserData[0], oneUserData[1], oneUserData[2], sellerBoolean);
                    info[0] = "true";

                    if (sellerBoolean) {
                        info[1] = "seller";
                    } else {
                        info[1] = "buyer";
                    }

                    signInComplete = true;
                    break;
                }
            }

            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!signInComplete) {
            JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Invalid Sign-In",
                    JOptionPane.ERROR_MESSAGE);
            // Clear fields for re-entry
            emailField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Signed in!", "Signed in", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("signed in");
            this.setVisible(false);
        }

        return info;
    }

    public static void run() {
        JOptionPane.showMessageDialog(null, "Welcome to the marketplace", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        new GUITest().setVisible(true);
    }

    public static void main(String[] args) {
        run();
    }
}
