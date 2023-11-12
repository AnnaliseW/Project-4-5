import java.io.*;
import java.util.ArrayList;

//TODO: In market, need to prompt to enter file path

public class Files {

    public String infoFileName;
    public boolean seller;
    public boolean buyer;

    //NEED TO CREATE A FILES OBJECT FIRST
    public Files(String infoFileName, boolean seller, boolean buyer) {
        this.infoFileName = infoFileName;
        this.seller = seller;
        this.buyer = buyer;
    }

    public String getInfoFileName() {
        return this.infoFileName;
    }

    public boolean exportPurchaseHistory() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(infoFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter("purchaseHistory.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                Boolean isSeller = Boolean.parseBoolean(fields[3]);

                if (seller == isSeller) {
                    return false;
                } else {
                    String productsInfo = line.substring(line.indexOf(";") + 1);
                    String[] products = productsInfo.split("@@");
                    for (int i = 0; i < products.length; i++) {
                        int index = i + 1;
                        writer.write("Purchase " + index + ": " + products[i]);
                        writer.newLine();
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    //Takes in file with each line being a product in this format:
    //pencil,purdue,it is pencil,53,2.44
    //String productName, String storeName, String descriptionOfProduct, int quantityAvailable, double price
    public ArrayList<Product> importProducts(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Product> imports = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException f) {
            System.out.println("No File Found");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (String productString : lines) {
            String[] parts = productString.split(", ");
            String productName = parts[0];
            String storeName = parts[1];
            String descriptionOfProduct = parts[2];
            int quantityAvailable = Integer.parseInt(parts[3]);
            double price = Double.parseDouble(parts[4]);
            Product product = new Product(productName, storeName, descriptionOfProduct, quantityAvailable, price);
            imports.add(product);
        }
        return imports;
    }

    //TODO: will need to remove products from the seller's inventory after this is called
    public boolean exportProducts(ArrayList<Product> exportedProducts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ExportedProducts.txt"))) {
            for (Product product : exportedProducts) {
                writer.write(product.statisticsToString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
