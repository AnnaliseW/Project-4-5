
    //individual salesByStore object (specific to storename)
    public void saveSalesByStoreFile(SalesByStore salesByStore) {
        File dataFile = new File("salesByStore.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            writer.write(salesByStore.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //multiple salesByStore objects
    public void saveSalesByStoreFileArray(ArrayList<SalesByStore> salesByStoreArray) {
        File dataFile = new File("salesByStore.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            for (SalesByStore salesByStore : salesByStoreArray) {
                writer.write(salesByStore.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SalesByStore> loadSalesByStoreFile(User account) {
        File dataFile = new File("salesByStore.txt");
        ArrayList<SalesByStore> salesByStoresList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            SalesByStore currentSales = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Sales For ")) {
                    String storeName = line.substring(11);
                    currentSales = new SalesByStore(storeName.trim());
                    salesByStoresList.add(currentSales);
                } else if (line.startsWith("ItemBought")) {
                    String[] values = line.split(", ");
                    String buyerName = values[0].split("='")[1].replace("'", "");
                    String buyerEmail = values[1].split("='")[1].replace("'", "");
                    String sellerEmail = values[2].split("='")[1].replace("'", "");
                    String storeName = values[3].split("='")[1].replace("'", "");
                    String productName = values[4].split("='")[1].replace("'", "");
                    double productPrice = Double.parseDouble((values[5]).split("='")[1].replace("'", ""));
                    int quantityBought = Integer.parseInt((values[6]).split("='")[1].replace("'", ""));
                    ItemBought itemBought = new ItemBought(buyerName, buyerEmail, sellerEmail, storeName, productName, productPrice, quantityBought);
                    if (currentSales != null) {
                        currentSales.addItemBought(itemBought);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<SalesByStore> salesByStorePersonal = new ArrayList<>();

        for (SalesByStore s : salesByStoresList) {
            String thisSellerEmail = s.getSalesList().get(0).getSellerEmail();
            if (thisSellerEmail.equals(account.getEmail())) {
                salesByStorePersonal.add(s);
            }
        }
        return salesByStorePersonal;
    }
