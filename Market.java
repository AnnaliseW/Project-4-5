import java.util.Scanner;

//MOST METHODS AND OBJECT CALLS HAVE NOT BE CREATED

public class Market {

    public static void main(String[] args) {

        //account creation and login
        if (account.isSeller()) {

        } else {

        }

        System.out.println("Welcome to the Marketplace!");
        System.out.println("Available products");
        System.out.println("-------------------");

        for (int i = 0; i < productsArrayList.length(); i++) {
            int j = i+1;
            System.out.println(j + productsArrayList.get(i).listingPagetoString());
            System.out.println("-------------------");

        }


        boolean exit = false;
        while (!exit) {

            System.out.println("Select the product number");

            Scanner scan = new Scanner(System.in);
            int selection = scan.nextInt();
            Product selectedProduct =  productsArrayList.get(selection - 1);

            System.out.println(selectedProduct.toString());

            System.out.println("Do you want to purchase this? 0 for yes, 1 for no");
            int purchaseMaybe = scan.nextInt();


        }

    }


}
