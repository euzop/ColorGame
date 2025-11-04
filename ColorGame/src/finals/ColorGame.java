package finals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ColorGame {

    private static final String CREDENTIALS_FILE_PATH = "credentials.txt";

    private static void updateBalanceFile(int balance) {
        try {
            File file = new File(CREDENTIALS_FILE_PATH);

            // Check if the file exists
            if (!file.exists()) {
                FileWriter writer = new FileWriter(file);
                writer.write(String.valueOf(balance));
                writer.close();
            } else {
                // Read the current contents of the file
                Scanner fileScanner = new Scanner(file);
                String fileContent = fileScanner.nextLine();
                fileScanner.close();

                // Extract the username and password from the file content
                String[] parts = fileContent.split(",");
                String encryptedUsername = parts[0];
                String encryptedPassword = parts[1];

                // Write the updated balance along with the username and password
                FileWriter writer = new FileWriter(file);
                writer.write(encryptedUsername + "," + encryptedPassword + "," + balance);
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving the balance.");
        }
    }

    public static void start(int balance) {
        Scanner scanner = new Scanner(System.in);
        // Define the available colors and their respective multipliers
        String[] colors = {"Yellow", "White", "Pink", "Blue", "Red", "Green"};
        int[] multipliers = {2, 2, 2, 2, 2, 2};
        String reset = "\u001B[0m";
        String blue = reset + "\u001B[44m";
        String yellow = reset + "\u001B[43m";
        String white = reset + "\u001B[47m";
        String pink = reset + "\u001B[45m";
        String cyan = reset + "\u001B[46m";
        String red = reset + "\u001B[41m";
        String green = reset + "\u001B[42m";

        // Print the colored squares
        System.out.println("\n" + blue + "                             ");
        System.out.println(blue + "  " + yellow + "       " + blue + "  " + white + "       " + blue + "  " + pink + "       " + blue + "  ");
        System.out.println(blue + "  " + yellow + "       " + blue + "  " + white + "       " + blue + "  " + pink + "       " + blue + "  ");
        System.out.println(blue + "  " + yellow + "       " + blue + "  " + white + "       " + blue + "  " + pink + "       " + blue + "  ");
        System.out.println(blue + "                             ");
        System.out.println(blue + "  " + cyan + "       " + blue + "  " + red + "       " + blue + "  " + green + "       " + blue + "  ");
        System.out.println(blue + "  " + cyan + "       " + blue + "  " + red + "       " + blue + "  " + green + "       " + blue + "  ");
        System.out.println(blue + "  " + cyan + "       " + blue + "  " + red + "       " + blue + "  " + green + "       " + blue + "  ");
        System.out.println(blue + "                             " + reset);
        System.out.println("Colors:");
        System.out.println("\u001b[33m" + "Yellow " + reset + "\u001b[37m" + "White " + reset + "\u001b[35m" + "Pink " + reset);
        System.out.println("\u001b[36m" + "Blue " + reset + "\u001b[31m " + "Red " + reset + "\u001b[32m" + "Green " + reset);
        System.out.println("Amount of money you have: " + balance);

        
        // Get the number of colors the player wants to bet on
        int numColors = 0;
        do {
            System.out.print("No. of Colors you would like to bet (1-6): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                scanner.nextLine(); // Clear the input buffer
                continue;
            }
            numColors = scanner.nextInt();
            if (numColors < 1 || numColors > 6) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
            }
        } while (numColors < 1 || numColors > 6);

        // Create arrays to store the player's bets and corresponding amounts
        String[] playerBets = new String[numColors];
        int[] betAmounts = new int[numColors];

        // Get the player's bets and amounts
        int totalBetAmount = 0;  // New variable to store total bet amount
        for (int i = 0; i < numColors; i++) {
            System.out.print("Pick your Color " + (i + 1) + ": ");
            String betColor = scanner.next();

            // Discard the bet if it's not a valid color
            if (!isValidColor(betColor, colors)) {
                System.out.println("Invalid color. Please choose a color from the available options.");
                i--; // Decrement i to repeat the current iteration and get a valid bet
                continue;
            }

            playerBets[i] = betColor;

            // Get the bet amount and validate it against the player's money
            int betAmount = 0;
            boolean isValidBetAmount;
            do {
                System.out.print("Amount of Bet: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid bet amount.");
                    scanner.nextLine();
                    isValidBetAmount = false;
                } else {
                    betAmount = scanner.nextInt();
                    if (betAmount > balance) {
                        System.out.println("You don't have enough money to place this bet.");
                        isValidBetAmount = false;
                    } else {
                        isValidBetAmount = true;
                        betAmounts[i] = betAmount;
                    }
                }
            } while (!isValidBetAmount);

            totalBetAmount += betAmount;  // Update total bet amount
        }

        // Randomly select the winning colors
        Random random = new Random();
        String[] winningColors = new String[3];
        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(colors.length);
            winningColors[i] = colors[index];
        }
        

        // Display the winning colors
        // Display the winning colors
        System.out.println("\nWinning Colors:");
        for (String color : winningColors) {
            String colorCode = getColorCode(color);
            System.out.println(colorCode + color);
        }
        
        // Calculate the total winnings and update the player's money
        int totalWinnings = 0;
        System.out.println("");
        for (int i = 0; i < numColors; i++) {
            boolean isWinningBet = false;

            for (String winningColor : winningColors) {
                if (playerBets[i].equalsIgnoreCase(winningColor)) {
                    isWinningBet = true;
                    break;
                }
            }

            if (isWinningBet) {
                int winnings = betAmounts[i] * multipliers[i];
                totalWinnings += winnings;
                System.out.println("\n" + "\u001b[32m" + "Congratulations! You won " + "\u001b[0m" + winnings + "\u001b[32m" + "! " + "\u001b[0m");
            } else {
                System.out.println("\u001b[31m" + "Sorry, you lost your bet." + "\u001b[0m");
            }
        }

        int newBalance = balance + totalWinnings - totalBetAmount;
        System.out.println("\n" + "\u001b[36m" + "Total Winnings: " + "\u001b[0m" + totalWinnings);
        System.out.println("\u001b[35m" + "New Balance: " + "\u001b[0m" + newBalance);

        // Update the balance in the file
        updateBalanceFile(newBalance);

        // Close the scanner
        //scanner.close();
    }

    private static boolean isValidColor(String color, String[] colors) {
        for (String validColor : colors) {
            if (validColor.equalsIgnoreCase(color)) {
                return true;
            }
        }
        return false;
    }
    
    private static String getColorCode(String color) {
    String reset = "\u001B[0m";
    String blue = "\u001B[36m";
    String yellow = "\u001B[33m";
    String white = "\u001B[37m";
    String pink = "\u001B[35m";
    String red = "\u001B[31m";
    String green = "\u001B[32m";

    switch (color.toLowerCase()) {
        case "yellow":
            return yellow;
        case "white":
            return white;
        case "pink":
            return pink;
        case "blue":
            return blue;
        case "red":
            return red;
        case "green":
            return green;
        default:
            return reset;
    }
}
}


