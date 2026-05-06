import java.util.Scanner;

public class Main {

    private static final String USERNAME = "user123";
    private static final String PASSWORD = "pass456";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==============================");
        System.out.println("       Welcome to QuickChat   ");
        System.out.println("==============================");

        if (!login(scanner)) {
            System.out.println("Too many failed attempts. Exiting.");
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    sendMessagesFlow(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    private static boolean login(Scanner scanner) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Username: ");
            String enteredUser = scanner.nextLine().trim();
            System.out.print("Password: ");
            String enteredPass = scanner.nextLine().trim();

            if (enteredUser.equals(USERNAME) && enteredPass.equals(PASSWORD)) {
                System.out.println("Login successful.");
                return true;
            } else {
                attempts++;
                System.out.println("Incorrect username or password. Attempts remaining: " + (3 - attempts));
            }
        }
        return false;
    }

    private static void sendMessagesFlow(Scanner scanner) {
        System.out.print("\nHow many messages do you want to send? ");
        int numMessages;

        try {
            numMessages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
            return;
        }

        if (numMessages <= 0) {
            System.out.println("Number of messages must be greater than 0.");
            return;
        }

        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

            // Recipient
            String recipient;
            while (true) {
                System.out.print("Recipient cell number (e.g. +27718693002): ");
                recipient = scanner.nextLine().trim();

                Message tempCheck = new Message(i, recipient, "placeholder check");
                String cellCheck = tempCheck.checkRecipientCell();

                if (cellCheck.equals("Cell phone number successfully captured.")) {
                    break;
                } else {
                    System.out.println(cellCheck);
                }
            }

            // Message text
            String messageText;
            while (true) {
                System.out.print("Message (max 250 characters): ");
                messageText = scanner.nextLine().trim();

                if (messageText.length() > 250) {
                    System.out.println("Please enter a message of less than 250 characters.");
                } else {
                    System.out.println("Message ready.");
                    break;
                }
            }

            // Create message
            Message message = new Message(i, recipient, messageText);

            if (!message.checkMessageID()) {
                System.out.println("Error: Message ID generation failed.");
                continue;
            }

            System.out.println("\nMessage Hash: " + message.getMessageHash());

            // Send / disregard / store
            String result = message.sendMessage();
            System.out.println(result);

            // Display full details only if sent
            if (result.equals("Message successfully sent.")) {
                System.out.println("\n--- Message Details ---");
                System.out.println("Message ID   : " + message.getMessageID());
                System.out.println("Message Hash : " + message.getMessageHash());
                System.out.println("Recipient    : " + message.getRecipient());
                System.out.println("Message      : " + message.getMessageText());
            }
        }

        System.out.println("\n==============================");
        System.out.println("Total messages sent: " + Message.getTotalMessagesSent());
        System.out.println("==============================");

        if (!Message.getSentMessages().isEmpty()) {
            System.out.println("\n--- All Sent Messages ---");
            Message dummy = Message.getSentMessages().get(0);
            System.out.println(dummy.printMessages());
        }
    }
}
