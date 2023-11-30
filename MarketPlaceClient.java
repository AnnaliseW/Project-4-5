import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MarketPlaceClient extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public GUICustomerView guiCustomerView;
    public GUISellerView guiSellerView;

    public User userAccount;

    public boolean openSeller = false;
    public boolean logIn = false;


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


            signInButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {


                    //SEND MESSAGE "SIGN IN PRESSED" TO SERVER

                    String email = emailField.getText();
                    char[] passwordChar = passwordField.getPassword();
                    String password = new String(passwordChar);
                    String emailAndPassword = email + "," + password;
                    writer.write(emailAndPassword);
                    writer.println();
                    writer.flush();


                    //recievss message of succesfull sign in and whteher buyer or seller from server

                    //open market

                    String[] infoArray;


                    String info;
                    try {
                        info = reader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    infoArray = info.split(",");
                    boolean seller = false;


                    if (infoArray[3].equals("true")) {
                        logIn = true;
                        // sign in worked


                        try {
                            String userInformation = reader.readLine();
                            System.out.println(userInformation);

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
                        GUICustomerView guiCustomerView = new GUICustomerView();


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
                            JButton viewCustomerCartsButton;
                            JButton exitButton;



                            guiCustomerView.createProductArray();
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
                            viewCustomerCartsButton = new JButton("View Customer Carts");
                            exitButton = new JButton("Exit");

                            sellerView.setLayout(new GridLayout(3, 3));

                            sellerView.add(sellButton);
                            sellerView.add(editButton);
                            sellerView.add(deleteButton);
                            sellerView.add(importButton);
                            sellerView.add(exportButton);
                            sellerView.add(salesByStoreButton);
                            sellerView.add(editProfileButton);
                            sellerView.add(viewCustomerCartsButton);
                            sellerView.add(exitButton);

                            sellerView.setVisible(true);

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
                            guiCustomerView.createProductArray();
                            ArrayList<ShoppingCartProduct> shoppingCart = guiCustomerView.createShoppingCartArray(userAccount);


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
                            JButton exitButton;


                            seeProductsButton = new JButton("See Products");
                            searchProductsButton = new JButton("Search Products");
                            viewShoppingCartButton = new JButton("View Shopping Cart");
                            editProfileButton = new JButton("Edit Profile");
                            viewPurchaseHistoryButton = new JButton("View Purchase History");
                            exitButton = new JButton("Exit");


                            CustomerView.setLayout (new GridLayout(2, 3));

                            CustomerView.add(seeProductsButton);
                            CustomerView.add(searchProductsButton);
                            CustomerView.add(viewShoppingCartButton);
                            CustomerView.add(editProfileButton);
                            CustomerView.add(viewPurchaseHistoryButton);
                            CustomerView.add(exitButton);

                            CustomerView.setVisible(true);

                            seeProductsButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    // TODO: Handle sell button action





                                   // customerGui.SeeProducts(userAccount, shoppingCart);


                                    //add implementation of seeing products


                                }
                            });




                        }

                    }
                }
            });


            //send info to server


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }



}


