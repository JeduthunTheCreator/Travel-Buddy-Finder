package com.example.myrestservice.FileStorage;

import org.junit.Test;
public class FileStorageTest {

    @Test
    public void testSaveCredentialsValid() {
        // Create an instance of FileStorage or use a test double
        FileStorage fileStorage = new FileStorage();
        String username = "testuser";
        String password = "testpassword";

        // Call the saveCredentials method with valid credentials
        fileStorage.saveCredentials(username, password);

        // Add assertions to check if the file was updated correctly
        // You can read the file and check its contents or size
        // and verify if it contains the expected data
        // Example:
        // assertTrue(checkFileContainsCredentials(username, hashedPassword));
    }

    @Test
    public void testSaveCredentialsInvalid() {
        // Create an instance of FileStorage or use a test double
        FileStorage fileStorage = new FileStorage();
        String username = "testuser";
        String password = ""; // Invalid password

        // Call the saveCredentials method with invalid credentials
        fileStorage.saveCredentials(username, password);

        // Add assertions to check if the file was not updated
        // Example:
        // assertFalse(checkFileContainsCredentials(username, hashedPassword));
    }
}
