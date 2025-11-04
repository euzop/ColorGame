package finals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

class SignUp extends User {
    private static final String CREDENTIALS_FILE = "credentials.txt";

    public SignUp(String username, String password) {
        super(username, password, 0);
    }

    public static SignUp signUp(Scanner scanner) {
        System.out.println("\n" + "\u001b[32m" + "Sign Up" + "\u001b[0m");
        String username;
        while (true) {
            System.out.print("Username: ");
            username = scanner.nextLine();

            if (isUsernameTaken(username)) {
                System.out.println("Username already exists. Please choose a different username.");
            } else {
                break;
            }
        }

        String encryptedUsername = encrypt(username, 7);

        String password;
        while (true) {
            System.out.print("Password: ");
            password = scanner.nextLine();

            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("\n" + "\u001b[31m" + "Invalid Password" + "\u001b[0m");
                displayPasswordRequirements(password);
            }
        }
        String encryptedPassword = encrypt(password, 7);

        // Write encrypted username, password, and initial balance to a text file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true))) {
            // Append the encrypted username, password, and initial balance to the file
            bufferedWriter.write(encryptedUsername + "," + encryptedPassword + ",0");
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n" + "\u001b[32m" + "Account Created Successfully." + "\u001b[0m");
        return new SignUp(username, password);
    }

    private static boolean isUsernameTaken(String username) {
        String encryptedUsername = encrypt(username, 7);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] credentials = line.split(",");
                String storedEncryptedUsername = credentials[0];
                if (storedEncryptedUsername.equals(encryptedUsername)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

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

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasSpecialChar = false;
        boolean hasNumber = false;
        boolean hasUppercase = false;

        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
        }

        return hasSpecialChar && hasNumber && hasUppercase;
    }

    private static void displayPasswordRequirements(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        if (password.length() < 8) {
            System.out.println("Minimum of 8 characters is required.");
        }
        if (!hasUppercase) {
            System.out.println("At least 1 uppercase letter is required.");
        }
        if (!hasLowercase) {
            System.out.println("At least 1 lowercase letter is required.");
        }
        if (!hasNumber) {
            System.out.println("At least 1 number is required.");
        }
        if (!hasSpecialChar) {
            System.out.println("At least 1 special character is required.");
        }

        System.out.println();
    }
}