package finals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MenuBar {

    private static Scanner scanner = new Scanner(System.in);
    private static Login loggedIn = null;
    private static ColorGame colorGame = new ColorGame(); // Create an instance of the ColorGame class

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n" + "\u001b[31m" + "Welcome" + "\u001b[32m" + " to" + "\u001b[36m" + " Color" + "\u001b[33m" + " Game" + "\u001b[0m");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Input Key: ");
            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    loggedIn = Login.login(scanner);
                    if (loggedIn != null) {
                        showLoggedInMenu();
                    }
                    break;
                case 2:
                    SignUp.signUp(scanner);
                    break;
                case 3:
                    if (loggedIn != null) {
                        System.out.println("Logging out...");
                    }
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showLoggedInMenu() {
        while (true) {
            System.out.println("\n" + "\u001b[33m" + "Options" + "\u001b[0m");
            System.out.println("1. Start Game");
            System.out.println("2. Check Balance");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Log Out");
            System.out.print("Input Key: ");

            if (scanner.hasNextInt()) { // Check if input is available and of type integer
                int choicelog = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                switch (choicelog) {
                    case 1 -> startGame();
                    case 2 -> checkBalance();
                    case 3 -> {
                        System.out.print("\nEnter the deposit amount: ");
                        int depositAmount;
                        try {
                            depositAmount = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            continue;
                        }
                        depositMoney(depositAmount);
                    }
                    case 4 -> withdrawMoney();
                    case 5 -> {
                        System.out.println("Logging out...");
                        loggedIn = null;
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                String invalidInput = scanner.nextLine(); // Read the invalid input
                System.out.println("Invalid input: " + invalidInput);
            }
        }
    }

    private static void startGame() {
        if (loggedIn != null) {
            colorGame.start(loggedIn.getBalance()); // Call the start method of the ColorGame instance
        } else {
            System.out.println("You need to log in first.");
        }
    }

    private static void checkBalance() {
        if (loggedIn != null) {
            System.out.println("\n" + "\u001b[35m" + "Current balance: " + "\u001b[0m" + loggedIn.getBalance());
        } else {
            System.out.println("You need to log in first.");
        }
    }

    private static void depositMoney(int depositAmount) {
        if (loggedIn != null) {
            int currentBalance = loggedIn.getBalance();
            int updatedBalance = currentBalance + depositAmount;

            // Update the account balance
            loggedIn.setBalance(updatedBalance);
            System.out.println("\u001b[32m" + "You have deposited " + "\u001b[0m" + depositAmount + "\u001b[32m" + " to your current balance!" + "\u001b[0m");
            System.out.println("\u001b[36m" + "Updated Balance: " + "\u001b[0m" + updatedBalance);

            // Save the updated balance to credentials.txt
            saveBalanceToFile(updatedBalance);
        } else {
            System.out.println("You need to log in first.");
        }
    }

    private static void saveBalanceToFile(int updatedBalance) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt"))) {
            String encryptedUsername = encrypt(loggedIn.getUsername(), 7); // Use shift value of 7
            String encryptedPassword = encrypt(loggedIn.getPassword(), 7); // Use shift value of 7
            writer.write(encryptedUsername + "," + encryptedPassword + "," + updatedBalance);
            loggedIn.setBalance(updatedBalance); // Update the balance in the loggedIn object as well
        } catch (IOException e) {
            System.out.println("An error occurred while saving the balance to file.");
            e.printStackTrace();
        }
    }

    private static String encrypt(String text, int shift) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                char encryptedChar = (char) (((c - 'a' + shift) % 26) + 'a');
                encryptedText.append(encryptedChar);
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    private static void withdrawMoney() {
        if (loggedIn != null) {
            int currentBalance = loggedIn.getBalance();
            System.out.print("\nEnter the withdrawal amount: ");
            int withdrawalAmount;
            try {
                withdrawalAmount = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                return;
            }
            if (withdrawalAmount > currentBalance) {
                System.out.println("\u001b[31m" + "Insufficient funds. Please enter a valid withdrawal amount." + "\u001b[0m");
            } else {
                int updatedBalance = currentBalance - withdrawalAmount;
                loggedIn.setBalance(updatedBalance);

                System.out.println("\u001b[32m" + "You have withdrawn " + "\u001b[0m" + withdrawalAmount + "\u001b[32m" + " from your current balance!" + "\u001b[0m");
                System.out.println("\u001b[36m" + "Updated Balance: " + "\u001b[0m" + updatedBalance);
                saveBalanceToFile(updatedBalance);
            }
        } else {
            System.out.println("You need to log in first.");
        }
    }
}
