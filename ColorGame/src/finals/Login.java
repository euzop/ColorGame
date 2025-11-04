package finals;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Login extends User {
    public Login(String username, String password, int balance) {
        super(username, password, balance);
    }

    public static Login login(Scanner scanner) {
        System.out.println("\n" + "\u001b[31m" + "Login" + "\u001b[0m");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Read the stored credentials from the file
        try {
            File file = new File("credentials.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] credentials = line.split(",");
                String storedUsername = credentials[0];
                String storedPassword = credentials[1];
                int storedBalance = Integer.parseInt(credentials[2]);

                // Decrypt the stored username and password
                String decryptedUsername = decrypt(storedUsername, 7);
                String decryptedPassword = decrypt(storedPassword, 7);

                // Check if the entered credentials match the stored credentials
                if (username.equals(decryptedUsername) && password.equals(decryptedPassword)) {
                    System.out.println("\n" + "\u001b[32m" + "Logged In Successfully." + "\u001b[0m");
                    fileScanner.close();
                    return new Login(username, password, storedBalance);
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: credentials.txt");
            e.printStackTrace();
        }

        System.out.println("Invalid username or password.");
        return null;
        
        
    }

    
    private static String decrypt(String encryptedText, int shift) {
        StringBuilder decryptedText = new StringBuilder();
        for (char c : encryptedText.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char decryptedChar = (char) (((c - base - shift + 26) % 26) + base);
                decryptedText.append(decryptedChar);
            } else {
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
        
    }
    
    
        public int getBalance() {
        return Balance.getUserBalance(getUsername());
    }
    
}