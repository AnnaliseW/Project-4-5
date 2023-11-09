import java.io.*;

public class Files {

    public String infoFileName;
    public boolean seller;
    public boolean buyer;

    public Files(String infoFileName, boolean seller, boolean buyer) {
        this.infoFileName = infoFileName;
        this.seller = seller;
        this.buyer = buyer;
    }

    public String getInfoFileName() {
        return this.infoFileName;
    }

    //files from main program will be loaded at beggining, updated at end, so dont need to read it again

    public boolean exportPurchaseHistory() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(infoFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter("exportHistory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String breakLine = line;
                breakLine = breakLine.substring(breakLine.indexOf(",") + 1, breakLine.indexOf(";"));
                breakLine = breakLine.substring(breakLine.indexOf(",") + 1, breakLine.length());
                breakLine = breakLine.substring(breakLine.indexOf(",") + 1, breakLine.length());
                Boolean isSeller = Boolean.parseBoolean(breakLine);

                if (seller == isSeller) { //seller
                    return false;
                } else { //buyer
                    String productsInfo = line;
                    productsInfo = productsInfo.substring(productsInfo.indexOf("cart"), productsInfo.length() - 1);
                    String[] products = productsInfo.split(";");

                    for (int i = 0; i < products.length - 1; i++) {
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

    public boolean importProducts() throws IOException {
        //file wwill have all product details included, with one row per product

    }

    public boolean exportProducts() throws IOException {
        //file wwill have all product details included, with one row per product

    }


}
