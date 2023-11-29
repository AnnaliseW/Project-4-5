import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class GUITestClient extends JFrame{
    private JTextField emailField;
    private JPasswordField passwordField;

    public GUICustomerView guiCustomerView;
    public GUISellerView guiSellerView;


    public static void run() {
        JOptionPane.showMessageDialog(null, "Welcome to the marketplace", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        new GUITestClient().setVisible(true);
    }

    public static void main(String[] args) {
        run();
    }

    public GUITestClient() {
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


        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //SEND MESSAGE "SIGN IN PRESSED" TO SERVER


                //recievss message of succesfull sign in and whteher buyer or seller from server

                //open market

                String[] info = new String[4];
                info[0] = "false";
                info[1] = "seller";
                info[2] = emailField.getText();;
                info[3] = new String(passwordField.getPassword());


                //send info to server



                boolean signInSuccessful = signInResult[0].equals("true");

                if (signInSuccessful && signInResult[1].equals("seller")) {
                    GUITest.SellerMarketplaceHomePage sellerMarketplaceHomePage = new GUITest.SellerMarketplaceHomePage();
                    sellerMarketplaceHomePage.setVisible(true);
                    this.setVisible(false);                } else if (signInSuccessful && signInResult[1].equals("buyer")) {
                    openBuyerMarketPlaceHomePage();
                }
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showCreateAccountDialog();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

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

        public User userAccount;

        public SellerMarketplaceHomePage() {
            guiSellerView = new GUISellerView();

            String[] signInResult = signIn();
            //creating user
            userAccount = guiSellerView.returnUserAccount(signInResult[2], signInResult[3]);
            //creates product array list from file

            guiSellerView.createProductArray();

            ArrayList<Product> myProducts = guiSellerView.generateMyProducts(userAccount);

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


            sellButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //TODO: calls server thing

                }
            });



        }
    }

}
