public static ArrayList<Product> generateMyProducts(User account) {
        ArrayList<Product> itemsSoldBySeller = new ArrayList<>();

        try {
            BufferedReader bfr = new BufferedReader(new FileReader("data.txt"));
            String line = "";
            ArrayList<String> allUserData = new ArrayList<>();

            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }

            for (String userData : allUserData) {
                String[] oneUserData = userData.split(",");
                if (oneUserData.length > 1 && oneUserData[1].equals(account.getEmail())) {
                    String[] separatingForSalesArray = userData.split(";");
                    if (separatingForSalesArray.length > 1) {
                        if (!separatingForSalesArray[1].contains("@@")) {
                            String[] separateParameters = separatingForSalesArray[1].split(",");
                            //productName, storeName, description, quantityAvailable, price
                            if (separateParameters.length >= 5) {
                                String productName = separateParameters[0];
                                String storeName = separateParameters[1];
                                String description = separateParameters[2];
                                int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                double price = Double.parseDouble(separateParameters[4]);
                                Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);
                                itemsSoldBySeller.add(eachProduct);
                            }
                        } else {
                            String[] arrayListSalesProducts = separatingForSalesArray[1].split("@@");
                            for (String productData : arrayListSalesProducts) {
                                String[] separateParameters = productData.split(",");
                                if (separateParameters.length >= 5) {
                                    String productName = separateParameters[0];
                                    String storeName = separateParameters[1];
                                    String description = separateParameters[2];
                                    int quantityAvailable = Integer.parseInt(separateParameters[3]);
                                    double price = Double.parseDouble(separateParameters[4]);
                                    Product eachProduct = new Product(productName, storeName, description, quantityAvailable, price);
                                    itemsSoldBySeller.add(eachProduct);
                                }
                            }
                        }
                    }
                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemsSoldBySeller;
    }


public void saveDataFileWhenNewProductAddedUserAccount(User userAccount, Product newProduct) {
        File dataFile = new File("data.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));

            ArrayList<String> allLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            reader.close();

            for (int i = 0; i < allLines.size(); i++) {
                String userData = allLines.get(i);
                String[] oneUserData = userData.split(",");
                if (oneUserData.length > 1 && oneUserData[1].equals(userAccount.getEmail())) {
                    allLines.set(i, userData + "@" + newProduct.toString());
                    break;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
            for (String updatedLine : allLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

public void replaceProductInDataFile(Product oldProduct, Product newProduct) {
        File dataFile = new File("data.txt");
        ArrayList<String> allUserData = new ArrayList<>();

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = bfr.readLine()) != null) {
                allUserData.add(line);
            }
            bfr.close();

            for (int i = 0; i < allUserData.size(); i++) {
                String[] checkIfSeller = allUserData.get(i).split(",");
                if (checkIfSeller.length > 3 && checkIfSeller[3].startsWith("true")) {
                    String[] afterSemiColon = allUserData.get(i).split(";");
                    if (allUserData.get(i).contains("@@")) {
                        String[] separatedByProduct = afterSemiColon[1].split("@@");
                        for (int k = 0; k < separatedByProduct.length; k++) {
                            String[] findProduct = separatedByProduct[k].split(",");
                            if (findProduct.length >= 2 && findProduct[1].equals(oldProduct.getProductName())
                                    && findProduct[0].equals(oldProduct.getStoreName())) {
                                String updated = newProduct.getStoreName() + "," + newProduct.getProductName() + ","
                                        + newProduct.getDescriptionOfProduct() + "," + newProduct.getQuantityAvailable()
                                        + "," + newProduct.getPrice();
                                allUserData.set(i, allUserData.get(i).replace(separatedByProduct[k], updated));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, false)));
            for (String line : allUserData) {
                pw.println(line);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


public void removeProductFromDataFile(User userAccount, Product productToRemove) {
        File dataFile = new File("data.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));

            ArrayList<String> allLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            reader.close();

            for (int i = 0; i < allLines.size(); i++) {
                String userData = allLines.get(i);
                String[] oneUserData = userData.split(",");
                if (oneUserData.length > 1 && oneUserData[1].equals(userAccount.getEmail())) {
                    String productStringToRemove = productToRemove.toString();
                    if (userData.contains(productStringToRemove)) {
                        String updatedUserData = userData.replace("@" + productStringToRemove, "");
                        allLines.set(i, updatedUserData);
                        break;
                    }
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
            for (String updatedLine : allLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
