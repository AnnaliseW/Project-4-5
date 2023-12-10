# CS 180 Project 5 README

## Compiling
To see if a class in our project compiles properly, open the terminal on Vocareum. Use the ‘javac’ command followed by the class you want to test. Hit enter, and if there are no errors, the code properly compiles.

(If you are on IntelliJ, make sure to configure the client class to allow parallel running)

## Running
Usually on Vocareum, to test if everything runs properly, you would either press the ‘Run’ button or use the ‘java’ command followed by the class you want to run. But, since GUI applications will not run on Vocareum, you must use an IDE to run this project. To run the program successfully on the IDE, you must run MarketPlaceServer.java first then run MarketPlaceClient.java after. This will allow you to properly test the classes.

## Submissions
Joseph Hsin - Submitted Vocareum Workspace

Aviana Franco - Submitted Report on Brightspace

Aviana Franco - Submitted Presentation on Brightspace

## Classes

### MarketPlaceClient
(Enter text here)

### ThreadedMarketPlaceServer
(Enter text here)

### Methods
(Enter text here)

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





