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
Enter text here.

### ThreadedMarketPlaceServer
Enter text here.

### Methods
Enter text here.

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

