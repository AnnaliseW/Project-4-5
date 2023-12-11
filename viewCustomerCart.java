// SERVER

 if (button.equals("viewCustomerCarts")) {
                                System.out.println("customer cart button clicked");
                                String output = "";

                                String allStoreNames = method.findingStoreNamesForSeller(userAccount);

                                if (allStoreNames.isEmpty()) {
                                    // no stores so nothing for sale
                                    writer.write("noStoresForSale");
                                    writer.println();
                                    writer.flush();
                                } else {
                                    writer.write("storeShown");
                                    writer.println();
                                    writer.flush();
                                    //method which takes the store names and finds shopping carts



                                    File dataFile = new File("data.txt");
                                    ArrayList<String> allUserData = new ArrayList<>();

                                    try {
                                        BufferedReader bfr = new BufferedReader(new FileReader(dataFile));
                                        String line;
                                        while ((line = bfr.readLine()) != null) {
                                            allUserData.add(line);
                                        }
                                        bfr.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    for (int i = 0; i < allUserData.size(); i++) {
                                        //splitting to find which ones are shopping carts
                                        String[] customerUsers = allUserData.get(i).split(",");
                                        if (customerUsers[3].startsWith("false")) {
                                            String[] shoppingCart = allUserData.get(i).split(";");
                                            if (shoppingCart.length == 1) {
                                                //no shopping cart for customer
                                            } else {

                                                String[] productsInCart = shoppingCart[1].split("@@");
                                                    for (int k = 0; k < productsInCart.length; k++) {
                                                        String[] eachProduct = productsInCart[k].split(",");
                                                        if (allStoreNames.contains(eachProduct[1])) {
                                                            //FORMAT : email name, product name, store name, description, quantity available, price, quantity buying, @@ to separate product in cart

                                                            //pencils,walmart,a pack of 10 pencils,18,5.0,1@@

                                                            output += "User Email: " + customerUsers[1] + ", Product Name: " + eachProduct[0] + ", Store Name: " + eachProduct[1] + ", Description: "
                                                                    + eachProduct[2] + ", Quantity Available: " + eachProduct[3] + ", Price: $" + eachProduct[4] + ", Quantity Buying: " + eachProduct[5] + "@@";

                                                        }
                                                    }
                                            }


                                        } else {
                                            // not a customer
                                        }
                                    }
                                    if (output.isEmpty()) {
                                        writer.write("noCustomerCarts");
                                        writer.println();
                                        writer.flush();
                                        // no items in a customers carts from stores/products
                                    } else {
                                        System.out.println(output);
                                        writer.write(output);
                                        writer.println();
                                        writer.flush();
                                    }
                                }


   // CLIENT SIDE 

   viewCustomerCartsButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println("customer cart clicked");
                                    writer.write("viewCustomerCarts");
                                    writer.println();
                                    writer.flush();

                                    String storeForSale = "";
                                    try {
                                        storeForSale = reader.readLine();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    if (storeForSale.equals("noStoresForSale")) {
                                        JOptionPane.showMessageDialog(null, "No products being sold or stores", "Customer Shopping Carts", JOptionPane.INFORMATION_MESSAGE);
                                        // exits cause nothing to see
                                    } else if (storeForSale.equals("storeShown")) {
                                        // SHOWS STORES


                                        String allInformation = "";
                                        try {
                                            allInformation = reader.readLine();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }

                                        if (allInformation.equals("noCustomerCarts")) {
                                            JOptionPane.showMessageDialog(null, "No customers have products/store in their carts!", "Customer Shopping Carts", JOptionPane.INFORMATION_MESSAGE);
                                        } else {
                                            String[] eachProductInfo = allInformation.split("@@");
//                                            JOptionPane.show(null, "Customer Carts With Your Stores", "View Customer Carts",
//                                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, eachProductInfo, eachProductInfo[0]);

                                            JOptionPane.showInputDialog(null, "Select a product:", "Product Selection",
                                                    JOptionPane.QUESTION_MESSAGE, null, eachProductInfo, eachProductInfo[0]);
                                        }

                                    }


                                }
                            });




                            }

