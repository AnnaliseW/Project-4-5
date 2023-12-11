# CS 180 Project 5 README

NOTE: THE USER CANNOT INPUT ANY SPECIAL CHARACTERS SUCH AS ",", ";", OR "@@" AT ANY POINT!

## Compiling
To see if a class in our project compiles properly, open the terminal on Vocareum. Use the ‘javac’ command followed by the class you want to test. Hit enter, and if there are no errors, the code properly compiles.

(If you are on IntelliJ, make sure to configure the client class to allow parallel running)

## Running
Usually on Vocareum, to test if everything runs properly, you would either press the ‘Run’ button or use the ‘java’ command followed by the class you want to run. But, since GUI applications will not run on Vocareum, you must use an IDE to run this project. To run the program successfully on the IDE, you must run MarketPlaceServer.java first then run MarketPlaceClient.java after. This will allow you to properly test the classes.

## Submissions
Joseph Hsin - Submitted Vocareum Workspace

Aviana Franco - Submitted Report on Brightspace

Annalise Wang - Submitted Presentation on Brightspace

## Classes

### MarketPlaceClient
MarketPlaceClient represents the client-side application for the School Supplies Marketplace. The client establishes a connection to the server using Java’s Socket class. It starts by running a user authentication. Some methods used in this are *sendCredentialsToServer* and *handleAuthenticationResponse*. The *sendCredentialsToServer* method takes the entered email and password and sends them to the server for authentication. The *handleAuthenticationResponse* method reads the server’s response and determines if the authentication was successful. The program then sets up the GUI with buttons for actions like viewing products, shopping cart, purchase history, and editing profiles using the *initializeGUI* method.

The client implements handling for product searching. Some of the methods used for this are *sendSearchQueryToServer* and *processSearchResults*. The *sendSearchQueryToServer* method sends the buyer’s product search query to the server for processing. The *processSearchResults* method reads the server’s response containing information about the search results. The client also implements handling for the product purchasing, which uses the *sendPurchaseRequest* and *processPurchaseResponse* methods. The *sendPurchaseRequest* method sends a request to the server to purchase a specific quantity of a product. The *processPurchaseResponse* method reads the server’s response to the purchase request.

The client implements shopping cart interactions too. Some methods being used for this are *sendAddToCartRequest* and *processAddToCartResponse*. The *sendAddToCartRequest* method sends a request to the server to add a product to the bueyr’s shopping cart. The *processAddToCartResponse* reads the server’s response if the product was successfully added to the shopping cart. To view the shopping cart, the client uses methods like *sendViewCartRequest* and *processViewCartResponse*. The *sendViewCartRequest* method requests the server for the current contents of the buyer’s shopping cart. The *processViewCartResponse* method reads the server’s response containing information about the contents of the shopping cart.

The client has a feature to view purchase history. Some methods being used for this are *sendViewHistoryRequest* and *processViewHistoryResponse*. The *sendViewHistoryRequest* method asks the server for the buyer’s purchase history. The *processViewHistoryResponse* method reads the server’s response containing the purchase history.

For the seller side, methods like *sendSellRequests* and *processSellResponse* allow sellers to add new products to the marketplace. The seller will	send product details such as name, store name, description, quantity, and price, to the server. Methods like *sendEditProductRequest* and *processEditProductResponse* enables sellers to edit information about their existing products. The updated product setails will be sent ot the server, which will cuase the user interface to update.

Additional features sellers have are listed below:

1. Removing products from the marketplace.
2. Importing and exporting product information using text files.
3. Being able to view sales statistics for their store.
4. Being able to delete their account.
5. Being able to view the current contents of their buyers' shopping carts.

Finally, the client implements user profile editing. Some methods used for this are *sendEditProfileRequest* and *processEditProfileResponse*. The *sendEditProfileRequest* method sends a request to the server to edit the user’s profile information. The *processEditProfileResponse* method reads the server’s response to the outcome of the profile edit.

### ThreadedMarketPlaceServer
ThreadMarketPlaceServer represents a threaded server for the application. The server listens for incoming client connections and spawns a new thread to handle each client. The ClientHandler class processes client requests, distinguishing between sellers and buyers. Sellers can add, edit, delete, and export products, while buyers can see, buy, and import products. *ThreadedMarketPlaceServer* utilizes multithreading to handle multiple clients concurrently. 

When the buyer clicks the "editProfileButton," the server reads the new profile information from the client. When the buyer clicks the “buyProductSearch” button, the server reads the amount of the product to be purchased. It checks if the amount is valid and proceeds with the purchase. When the buyer clicks the "addToCart" button, the server reads the quantity to be added to the cart from the client. It checks if the quantity is valid and proceeds with adding the product to the cart. The server handles the search request from the buyer by reading the searched word from the client. After the buyer selects a product from the search results, the server opens a new panel on the client side to display insights about the product.

The server also handles various shopping cart operations based on the client's button clicks. Finally, the program includes a try-catch block to handle IOException exceptions, printing the stack trace if an exception occurs during input/output operations.


### Methods
Methods contains various methods for interacting with different aspects of the project. Methods like *saveArrayListToFile* and *saveShoppingCartArrayListToFile* are responsible for updating information in the data file. Sorting methods like *sortByMinPrice* and *sortByMaxPrice* sort product lists based on different criteria listed. The *makeProductArrayList* method reads product information from a file and creates an ArrayList of ‘Product’ objects. Methods like *createShoppingCartArray* and *savaDataFileCart* are used for creating and updating shopping cart arrays, which can track buyer purchases and items in buyers’ shopping carts. The method *generateMyProducts* creates a list of products sold by a seller. The methods *saveDataFileWhenNewProductAddedUserAccount, replaceProductInDataFile, removeProductFromDataFile,* and *savaDataFileWithNewProductList* are used to add, replace, remove, and save products to the user’s account in the “data.txt” file. Methods like *makeCustomerHistory* and *saveCustomerHistory* are used to create and save customer purchase history to the “customerHistory.txt” file. Finally, the *deleteForProductFile* method is used to delete a product from the “productArrayList.txt” file.

### Product
Product has fields to store information about a product in the marketplace, such as its name, the store selling it, a description, the quantity available, the price, and the quantity sold. It also includes methods to access and modify these fields.

**Instance Variables:**
- productName: The name of the product.
- storeName: The name of the store selling the product.
- descriptionOfProduct: A description of the product.
- quantityAvailable: The quantity of the product available in the store.
- price: The price of the product.
- quantitySold: The quantity of the product that has been sold.

**Constructor:**
- Product: Creates an object with specified values for the parameters.

**Methods:**
- getProductName: Returns the name of this product.
- setProductName: Sets the name of this product.
- getStoreName: Returns the store name of this product.
- setStoreName: Set the store name of this product.
- getDescriptionOfProduct: Returns the description of this product.
- setDescriptionOfProduct: Sets the description of this product.
- getQuantityAvailable: Returns the quantity available of this product.
- setQuantityAvailable: Sets the quantity available of this product.
- getPrice: Returns the price of this product.
- setPrice: Sets the price of this product.
- getQuantitySold: Returns the quantity sold of this product.
- setQuantitySold: Sets the quantity sold of this product.
- statisticsToString: Formats and returns a string with statistics about the product, including its name, store, description, quantity available, and price.
- listingPagetoString: Formats and returns a string with a representation for a product listing, including the product name, store name, and price.
- equals: Compares two Product objects to see if they are equal. Two Product objects are considered equal if their name, store name, description, quantity available, and price are all equal.

This class makes comparing different products easier for users. In the marketplace, this class causes products to be listed, sold, and tracked by their statistics.

### ShoppingCartProduct
ShoppingCartProduct represents a product that can be added to the shopping cart. This class summarizes the attributes of a product and includes methods for interacting with and obtaining information about the product. It can also compare products to see if they are the same.

**Instance Variables:**
- productName: The name of the product.
- storeName: The name of the store selling the product.
- descriptionOfProduct: A description of the product.
- quantityAvailable: The quantity of the product available in the store.
- price: The price of the product.
- quantitySold: The quantity of the product the buyer wants to purchase.

**Constructor:**
- ShoppingCartProduct: Takes the parameters for all the attributes and sets the object with the provided values.

**Methods:**
- getProductName: Returns the name of this product.
- setProductName: Sets the name of this product.
- getStoreName: Returns the store name of this product.
- setStoreName: Set the store name of this product.
- getDescriptionOfProduct: Returns the description of this product.
- setDescriptionOfProduct: Sets the description of this product.
- getQuantityAvailable: Returns the quantity available of this product.
- setQuantityAvailable: Sets the quantity available of this product.
- getPrice: Returns the price of this product.
- setPrice: Sets the price of this product.
- getQuantityBuying: Returns the quantity of the product the buyer wants to purchase.
- setQuantityBuying: Sets the quantity of the product the buyer wants to purchase.
- shoppingCartStatisticsToString: Formats and returns a string with statistics about the product, including its name, store, description, quantity available, price, and the quantity the user is buying.
- listingPagetoString: Formats and returns a string with a representation for a product listing, including the product name, store name, and price.
- equals: Compares two ShoppingCartProduct objects to see if they are equal. Two ShoppingCartProduct objects are considered equal if their product name, store name, description, quantity available, price, and quantity buying are all equal.

### SoldProduct
SoldProduct extends Product. This class is used to represent a product that has been sold. It retrieves information about products that have been purchased. This is used to allow sellers to see how much of a product they have sold. It also allows buyers to view their receipts.

**Instance Variables:**
- quantityPurchased: Represents the quantity of the product that has been purchased.

**Constructors:**
- There are two ‘SoldProduct’ constructors. One takes the individual variables, and the other takes the ‘Product’ object along with the quantity purchased.

**Methods:**
- getQuantityPurchased: An accessor method that returns the quantity of the product that has been purchased.
- customerReceiptStatistics: Returns a String that contains information about the product, store, and purchase. This is used to generate the buyer’s receipts and statistics related to a purchase.

### User
User has fields that represent a user in the marketplace system. Each user has private attributes including a name, email, password, and if they are a seller or buyer. All of this information will be stored, so the user can create an account or sign into the marketplace.

**Instance Variables:**
- name: A private String instance variable to store the user’s name.
- email: A private String instance variable to store the user’s email.
- password: A private String instance variable to store the user’s password.
- seller: A private boolean instance variable that indicates whether the user is a seller (true) or a buyer (false).

**Constructor:**
- User: Creates an object with specified values for the parameters.

**Methods:**
- getName: Returns the name of this user.
- setName: Set the name of this user.
- getEmail: Returns the email of this user.
- setEmail: Sets the email of this user.
- getPassword: Returns the password of this user.
- setPassword: Sets the password of this user.
- isSeller: Returns a boolean indicating whether the user is a seller or a buyer.
- setSeller: Sets the boolean indicating whether the user is a seller or a buyer.

### customerHistory.txt
(Enter text here)

### data.txt
Used to store the user’s data when signing in. When signing in, it will be used to store the user’s name, email, password, and if the user is a seller or a buyer, all followed by a semicolon (;). The line will end there if the user is a buyer or a seller with no stores yet. If the user is a seller with a store, following the semicolon will be the seller’s product name, store name, product description, quantity of the product, and the price of the product, followed by two @ symbols (@@). If the user is a seller with multiple stores, after the two @ symbols, the format described about the seller with a store will be repeated.

It would be stored using these formats:

*Buyer:*

Joseph,jhsin1234,1234,false;


*Seller with no stores:*

Annalise,wang1234,1234,true;


*Seller with stores:*

Aviana,franco28,1234,true;Eraser,Target,It is a small eraser,10,2.00@@Pencils,Walmart,A pack of 10 pencils,20,5.00@@

### productArrayList.txt
Used to store the ArrayLists of the products being sold by sellers. These will be listed in the same format as a ‘Seller with stores’ after the semicolon (;).

*Example:*

Eraser,Target,It is a small eraser,10,2.00@@Pencils,Walmart,A pack of 10 pencils,20,5.00@@






