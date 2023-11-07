import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SchoolSuppliesMarketplace {

    private static final String USERS_FILE = "users.txt";
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        loadUsersData();

        Scanner scan = new Scanner(System.in);

        // Main program loop
        while (true) {
            if (currentUser == null) {
                //Displays options for users who are not signed in
                System.out.println("Welcome to the School Supplies Marketplace");
                System.out.println("1. Create Account");
                System.out.println("2. Sign In");
                System.out.println("3. Exit");

                // Reads the user's choice
                int choice = scan.nextInt();
                scan.nextLine();

                //Processes the user's choice
                switch (choice) {
                    case 1:
                        createAccount(scan);
                        break;
                    case 2:
                        signIn(scan);
                        break;
                    case 3:
                        saveUsersData(); // Saves user data to the file
                        System.out.println("Goodbye!");
                        System.exit(0); // Exits the program
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                // Displays options for signed-in users
                System.out.println("Welcome, " + currentUser.getName() + "!");
                System.out.println("You are currently signed in as a " + (currentUser.isSeller() ? "Seller" : "Customer"));
                System.out.println("4. Sign Out");

                // Reads the user's choice
                int choice = scan.nextInt();
                scan.nextLine();

                if (choice == 4) {
                    currentUser = null; // Sign the user out
                    System.out.println("You have been signed out.");
                }
            }
        }
    }

    private static void createAccount(Scanner scan) {
        System.out.println("Creating a new account...");
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
        System.out.print("Enter your email: ");
        String email = scan.nextLine();

        // Checks if an account with the provided email already exists
        if (users.containsKey(email)) {
            System.out.println("An account with this email already exists. Please sign in.");
            return;
        }

        System.out.println("Are you a Seller or a Customer?");
        System.out.print("Enter 'Seller' or 'Customer': ");
        String userType = scan.nextLine().toLowerCase();

        // Checks and sets the user type
        if (!userType.equals("seller") && !userType.equals("customer")) {
            System.out.println("Invalid user type. Please enter 'Seller' or 'Customer'.");
            return;
        }

        boolean isSeller = userType.equals("seller");

        System.out.print("Create a password: ");
        String password = scan.nextLine();

        // Creates an ArrayList to store user-specific data
        ArrayList<String> itemsList = new ArrayList<>();

        // Creates a new User object with the provided information
        User newUser = new User(name, email, password, isSeller, itemsList);
        users.put(email, newUser); // Adds the new user to the users map with email as the key
        currentUser = newUser; // Sets the currentUser to the newly created user
        saveUsersData(); // Saves user data to the file
        System.out.println("Account created successfully!");
    }

    // Signs in a user based on their provided email and password
    private static void signIn(Scanner scanner) {
        System.out.println("Signing in...");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Retrieve the User object associated with the provided email
        User user = users.get(email);

        // Checks if the user exists and if the provided password macthes
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user; // Sets the current user to the signed-in user
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    // Loads user data from the specified file into the 'users' map
    private static void loadUsersData() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String email = parts[1];
                String password = parts[2];
                boolean isSeller = Boolean.parseBoolean(parts[3]);

                // Creates a User object with the loaded data and adds it to the 'users' map
                User user = new User(name, email, password, isSeller);
                users.put(email, user);
            }
        } catch (IOException e) {

        }
    }

    //Saves user data from the 'users' map into the specified file
    private static void saveUsersData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                pw.println(user.getName() + "," + user.getEmail() + "," + user.getPassword() + "," + user.isSeller());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class User {
        private String name;
        private String email;
        private String password;
        private boolean isSeller;
        private ArrayList<String> itemsList;

        public User(String name, String email, String password, boolean isSeller, ArrayList<String> itemsList) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.isSeller = isSeller;
            this.itemsList = itemsList;
        }
        // Constructor without itemsList (used for loading users from file)
        public User(String name, String email, String password, boolean isSeller) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.isSeller = isSeller;
            // itemsList is not initialized here; it will be populated when needed
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public boolean isSeller() {
            return isSeller;
        }
    }
}