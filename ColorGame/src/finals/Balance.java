package finals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

class Balance {
    private static final String CREDENTIALS_FILE = "credentials.txt";

    // Caesar cipher encryption method
    private static String encrypt(String text, int shift) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char encryptedChar = (char) (((c - base + shift) % 26) + base);
                encryptedText.append(encryptedChar);
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

public static int getUserBalance(String username) {
    String encryptedUsername = encrypt(username, 7);
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] credentials = line.split(",");
            String storedEncryptedUsername = credentials[0];
            if (storedEncryptedUsername.equals(encryptedUsername)) {
                int storedEncryptedBalance = Integer.parseInt(credentials[2]);
                int balance = storedEncryptedBalance; // Decrypt the balance by adding 7
                return balance >= 0 ? balance : 0; // Ensure balance is non-negative
            }
        }
    } catch (IOException | NumberFormatException e) {
        System.out.println("Failed to retrieve the user's balance.");
        e.printStackTrace();
    }
    return -1; // User not found or failed to retrieve balance
}

public static boolean updateUserBalance(String username, int balance) {
    String encryptedUsername = encrypt(username, 7);
    File inputFile = new File(CREDENTIALS_FILE);
    File tempFile = new File("temp_credentials.txt");
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile))) {
        String line;
        boolean userUpdated = false;
        while ((line = bufferedReader.readLine()) != null) {
            String[] credentials = line.split(",");
            String storedEncryptedUsername = credentials[0];
            if (storedEncryptedUsername.equals(encryptedUsername)) {
                credentials[2] = Integer.toString(balance + 7); // Encrypt and update the balance
                userUpdated = true;
            }
            bufferedWriter.write(String.join(",", credentials));
            bufferedWriter.newLine();
        }

        if (!userUpdated) {
            String[] newCredentials = {encryptedUsername, "password_placeholder", Integer.toString(balance + 7)};
            bufferedWriter.write(String.join(",", newCredentials));
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
        bufferedWriter.close();
        bufferedReader.close();

        // Replace the original file with the new file
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Failed to update the user's balance.");
                return false;
            }
        } else {
            System.out.println("Failed to update the user's balance.");
            return false;
        }

        return true;
    } catch (IOException e) {
        System.out.println("Failed to update the user's balance.");
        e.printStackTrace();
    }
    return false;
}
}