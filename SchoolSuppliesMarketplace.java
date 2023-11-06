import java.io.*;
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


        while (true) {
            if (currentUser == null) {
                System.out.println("Welcome to the School Supplies Marketplace");
                System.out.println("1. Create Account");
                System.out.println("2. Sign In");
                System.out.println("3. Exit");

                int choice = scan.nextInt();
                scan.nextLine();

                switch (choice) {
                    case 1:
                        createAccount(scan);
                        break;
                    case 2:
                        signIn(scan);
                        break;
                    case 3:
                        saveUsersData();
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Welcome, " + currentUser.getName() + "!");
                System.out.println("You are currently signed in as a " + (currentUser.isSeller() ? "Seller" : "Customer"));
                System.out.println("4. Sign Out");
                int choice = scan.nextInt();
                scan.nextLine();
                if (choice == 4) {
                    currentUser = null;
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

        if (users.containsKey(email)) {
            System.out.println("An account with this email already exists. Please sign in.");
            return;
        }

        System.out.println("Are you a Seller or a Customer?");
        System.out.print("Enter 'Seller' or 'Customer': ");
        String userType = scan.nextLine().toLowerCase();

        if (!userType.equals("seller") && !userType.equals("customer")) {
            System.out.println("Invalid user type. Please enter 'Seller' or 'Customer'.");
            return;
        }

        boolean isSeller = userType.equals("seller");

        System.out.print("Create a password: ");
        String password = scan.nextLine();

        User newUser = new User(name, email, password, isSeller);
        users.put(email, newUser);
        currentUser = newUser;
        saveUsersData();
        System.out.println("Account created successfully!");
    }

    private static void signIn(Scanner scanner) {
        System.out.println("Signing in...");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User user = users.get(email);

        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    private static void loadUsersData() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String email = parts[1];
                String password = parts[2];
                boolean isSeller = Boolean.parseBoolean(parts[3]);
                User user = new User(name, email, password, isSeller);
                users.put(email, user);
            }
        } catch (IOException e) {

        }
    }

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

        public User(String name, String email, String password, boolean isSeller) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.isSeller = isSeller;
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