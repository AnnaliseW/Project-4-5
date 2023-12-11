# Tests

### Test 1: User creating an account

Steps:

1. User launches application.
2. User selects the “Create Account” button.
3. User selects the name textbox.
4. User enters their name via keyboard.
5. User selects the email textbox.
6. User enters their email via keyboard.
7. User selects the password textbox.
8. User enters their password via keyboard.
9. Users selects the “Ok” button.

Expected result: Application verifies that the inputted information is not already taken, then sends them to the “Profile Selection” page.

Test Status: Passed.

### Test 2: User choosing profile type

Steps:

1. User is on the “Profile Selection” page of creating an account.
2. User selects either the “Seller” or “Buyer” button.
3. User is prompted with the “Account created! Please sign in” page.
4. User selects the “Ok” button.

Expected result: Application sets that user to the corresponded profile selection, then sends them back to the “Sign In” page.

Test Status: Passed.

### Test 3: User logging in

Steps:

1. User has launched the application and is on the “Sign In” page.
2. User selects the email textbox.
3. User enters their email via keyboard.
4. User selects the password textbox.
5. User enters their password via keyboard.
6. User selects the “Sign In” button.

Expected result: Application verifies that the user’s email and password are stored and matched, then loads their homescreen automatically.

Test Status: Passed.

### Test 4: Logging In to Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.

Expected Result: MarketPlace is loaded with the buttons for a seller.

Test Result: Passed.

### Test 5: Selling a product in the Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.
4. User clicks sell button on the MarketPlace.
5. User enters valid information for product info.
6. User clicks ok.

Expected Result: Confirmation message is shown, productArraylist.txt and data.txt are updated with the product.

Test Result: Passed.

### Test 6: Editing a product in the Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.
4. User clicks edit button on the MarketPlace.
5. User selects a product from the list of products.
6. User gets confirmation of product they picked.
7. User inputs the new information.
8. User clicks ok.

Expected Result: Confirmation message is shown, chosen product in productArraylist.txt and data.txt are updated with the new information.

Test Result: Passed.

### Test 7: Deleting a product in the Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.
4. User clicks delete button on the MarketPlace.
5. User selects product to delete.
6. User gets a confirmation of product they deleted.
7. User clicks ok.

Expected Result: Confirmation message is shown, chosen product to delete is removed from productArraylist.txt and data.txt.

Test Result: Passed.

### Test 8: Importing products in the Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.
4. User clicks import button on the MarketPlace.
5. User types in name of the file.
6. User gets a confirmation that their products have been imported.

Expected Result: Confirmation message is shown, products in the txt file are added to productArraylist.txt and data.txt.

Test Result: Passed.

### Test 9: Exporting a product in the Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.
4. User clicks export button on the MarketPlace.
5. User selects product to export.
6. User gets a confirmation of product they exported.
7. User clicks ok.

Expected Result: Confirmation message is shown, chosen product to export is removed from productArraylist.txt and data.txt., chosen product to remove is inside a txt file named “UserEmailExports.txt”.

Test Result: Passed.

### Test 10: Editing a profile in the Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.
4. User clicks edit profile button on the MarketPlace.
5. User enters new information.
6. User gets confirmation of updated information.

Expected Result: Confirmation message is shown, user data in data.txt are updated with the new information.

Test Result: Passed.

### Test 11: Deleting a profile in the Seller Side

Steps:

1. User launches application.
2. User clicks selects “OK” on welcome message.
3. User enters default seller login information.
4. User clicks delete profile button on the MarketPlace.
5. User gets confirmation of deleted information.
6. User is prompted to close the application.

Expected Result: Confirmation message is shown, user data in data.txt is deleted.

Test Result: Passed.

### Test 12: Seeing if selling a product will occur for concurrent seller and buyer

Steps:

1. User 1 logs in as seller.
2. User 2 logs in as buyer.
3. User 2 clicks see products, then cancel.
4. User 1 clicks sell and adds a product.
5. User 2 clicks see products again.

Expected Result: User 2 sees the product added to the list of products displayed.

Test Result: Passed.

### Test 13: Seeing if editing a product will occur for concurrent seller and buyer

Steps:

1. User 1 logs in as seller.
2. User 2 logs in as buyer.
3. User 2 clicks see products, then cancel.
4. User 1 clicks edit and edits a product.
5. User 2 clicks see products again.

Expected Result: User 2 sees the product edited correctly.

Test Result: Passed.

### Test 14: Seeing if deleting a product will occur for concurrent seller and buyer

Steps:

1. User 1 logs in as seller.
2. User 2 logs in as buyer.
3. User 2 clicks see products, then cancel.
4. User 1 clicks delete and deletes a product.
5. User 2 clicks see products again.

Expected Result: User 2 cannot see the deleted product.

Test Result: Passed.

### Test 15: Seeing if importing products will occur for concurrent seller and buyer

Steps:

1. User 1 logs in as seller.
2. User 2 logs in as buyer.
3. User 2 clicks see products, then cancel.
4. User 1 clicks import products and imports multiple products.
5. User 2 clicks see products again.

Expected Result: User 2 sees the products added to the list of products displayed.

Test Result: Passed.

### Test 16: Seeing if exporting a product will occur for concurrent seller and buyer

Steps:

1. User 1 logs in as seller.
2. User 2 logs in as buyer.
3. User 2 clicks see products, then cancel.
4. User 1 clicks export and exports a product.
5. User 2 clicks see products again.

Expected Result: User 2 cannot see the exported product.

Test Result: Passed.

### Test 17: Seeing if invalid error for selling a product will be handled correctly

Steps:

1. User logs in as seller.
2. User clicks sell product.
3. User enters “hello” in Quantity Selling.
4. User clicks ok.

Expected Result: Error message will be displayed, and sell button can be pressed again.

Test Result: Passed.

### Test 18: Seeing if invalid error for selling a product will be handled correctly

Steps:

1. User logs in as seller.
2. User clicks sell product.
3. User enters “hello” in Quantity Selling.
4. User clicks ok.

Expected Result: Error message will be displayed, and sell button can be pressed again.

Test Result: Passed.
