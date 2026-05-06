import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    private static int totalMessagesSent = 0;
    private static List<Message> sentMessages = new ArrayList<>();

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // --- ID Generation ---

    private String generateMessageID() {
        Random rand = new Random();
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    // --- Required Methods ---

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient == null) {
            return "Invalid: recipient is null.";
        }
        if (!recipient.startsWith("+")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        if (recipient.length() > 10) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        return "Cell phone number successfully captured.";
    }

    public String createMessageHash() {
        if (messageID == null || messageText == null || messageText.isBlank()) {
            return "";
        }
        String idPrefix = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String hash = (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return hash;
    }

    public String sendMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Message Options ---");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Choose an option: ");

        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message successfully sent.";
            case "2":
                return "Press 0 to delete the message.";
            case "3":
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option. Message was not sent.";
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("-----------------------------\n");
            sb.append("Message ID   : ").append(m.messageID).append("\n");
            sb.append("Message Hash : ").append(m.messageHash).append("\n");
            sb.append("Recipient    : ").append(m.recipient).append("\n");
            sb.append("Message      : ").append(m.messageText).append("\n");
        }
        sb.append("-----------------------------");
        return sb.toString();
    }

    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        String json = toJson();
        System.out.println("\n[Simulated JSON write to file]");
        System.out.println(json);

        try (FileWriter writer = new FileWriter("stored_messages.json", true)) {
            writer.write(json + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // --- Helpers ---

    public String toJson() {
        return "{\n" +
               "  \"messageID\": \"" + messageID + "\",\n" +
               "  \"messageNumber\": " + messageNumber + ",\n" +
               "  \"recipient\": \"" + recipient + "\",\n" +
               "  \"message\": \"" + messageText.replace("\"", "\\\"") + "\",\n" +
               "  \"messageHash\": \"" + messageHash + "\"\n" +
               "}";
    }

    // --- Getters ---

    public String getMessageID()   { return messageID; }
    public int getMessageNumber()  { return messageNumber; }
    public String getRecipient()   { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }

    public static int getTotalMessagesSent()    { return totalMessagesSent; }
    public static List<Message> getSentMessages() { return sentMessages; }

    // Reset static state (used by unit tests)
    public static void resetState() {
        totalMessagesSent = 0;
        sentMessages.clear();
    }
}
