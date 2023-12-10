//INSIDE PRODUCT CLASS
public String statisticsToStringNoSpace() {
        return String.format("Product Statistics:Product Name: %s Store Name: %s Description: %s Quantity Available: %d Price: %.2f", productName
                , storeName, descriptionOfProduct, quantityAvailable, price);
}


//INSIDE METHODS
public void saveDataFileWithNewProductList(User userAccount, ArrayList<Product> newProductList) {
        File file = new File("data.txt");
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String[] userParts = parts[0].split(",");

                if (userParts[1].equals(userAccount.getEmail())) {
                    line = userParts[0] + "," + userParts[1] + "," + userParts[2] + ","
                            + userParts[3] + ";";

                    for (Product product : newProductList) {
                        line += product.getProductName() + ","
                                + product.getStoreName() + ","
                                + product.getDescriptionOfProduct() + ","
                                + product.getQuantityAvailable() + ","
                                + product.getPrice() + "@@";
                    }
                }

                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//Inside Methods
public void deleteForProductFile(ArrayList<Product> arrayList, Product itemToDelete) {
        File dataFile = new File("productArrayList.txt");

        ArrayList<String> allProducts = new ArrayList<>();

        for (Product product : arrayList) {
            if (!product.equals(itemToDelete)) {
                String productString = product.getProductName() + "," +
                        product.getStoreName() + "," +
                        product.getDescriptionOfProduct() + "," +
                        product.getQuantityAvailable() + "," +
                        product.getPrice() + "@@";
                allProducts.add(productString);
            }
        }

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)))) {
            for (String productString : allProducts) {
                pw.print(productString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
