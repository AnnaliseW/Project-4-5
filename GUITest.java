import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

        setLayout(new GridLayout(3, 2));

        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(signInButton);
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
                    // TODO: Handle sell button action
                    JOptionPane.showMessageDialog(null, "Sell button clicked!", "Sell", JOptionPane.INFORMATION_MESSAGE);
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
