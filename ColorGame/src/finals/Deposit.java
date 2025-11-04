package finals;
import java.util.Scanner;

public class Deposit {
    public static void depositMoney(Scanner scanner, String username, int[] balance) {
        System.out.println("Deposit Money");
        System.out.println("Current Balance: " + balance[0]);

        System.out.print("Enter Amount: ");
        int amount;
        try {
            amount = Integer.parseInt(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive value.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
            return;
        }

        // Update the balance
        int newBalance = balance[0] + amount;

        // Update the user's balance
        balance[0] = newBalance;

        System.out.println("Deposit successful. New balance: " + newBalance);
    }
}