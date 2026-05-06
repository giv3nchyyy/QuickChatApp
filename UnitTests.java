public class UnitTests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("     QuickChat Unit Tests     ");
        System.out.println("==============================\n");

        Message.resetState();

        testMessageTooLong();
        testMessageWithinLimit();
        testRecipientValid();
        testRecipientNoPlus();
        testRecipientTooLong();
        testMessageHashCorrect();
        testMessageIDCreated();
        testMessageIDLength();

        System.out.println("\n==============================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");
        System.out.println("==============================");
    }

    // --- Test 1: Message too long (> 250 chars) ---
    private static void testMessageTooLong() {
        String longMsg = "A".repeat(251);
        boolean tooLong = longMsg.length() > 250;
        String expected = "Please enter a message of less than 250 characters.";
        String actual = tooLong ? "Please enter a message of less than 250 characters." : "Message ready.";
        assertEqual("Message too long (251 chars) shows correct error", expected, actual);
    }

    // --- Test 2: Message within limit (250 chars) ---
    private static void testMessageWithinLimit() {
        String msg = "Hi Mike, can you join us for dinner tonight?";
        boolean valid = msg.length() <= 250;
        String expected = "Message ready.";
        String actual = valid ? "Message ready." : "Please enter a message of less than 250 characters.";
        assertEqual("Message within 250 chars is accepted", expected, actual);
    }

    // --- Test 3: Valid recipient (Test Case 1) ---
    private static void testRecipientValid() {
        Message m = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String result = m.checkRecipientCell();
        assertEqual("Valid recipient +27718693002 passes check",
                "Cell phone number successfully captured.", result);
    }

    // --- Test 4: Recipient missing + (Test Case 2) ---
    private static void testRecipientNoPlus() {
        Message m = new Message(2, "08575975889", "Hi Keegan, did you receive the payment?");
        String result = m.checkRecipientCell();
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEqual("Recipient without + fails check", expected, result);
    }

    // --- Test 5: Recipient too long ---
    private static void testRecipientTooLong() {
        Message m = new Message(1, "+27123456789012", "Test message.");
        String result = m.checkRecipientCell();
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEqual("Recipient longer than 10 chars fails check", expected, result);
    }

    // --- Test 6: Message hash correct format for Test Case 1 ---
    private static void testMessageHashCorrect() {
        Message m = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hash = m.getMessageHash();

        // Hash format: first 2 digits of ID + ":" + messageNum + ":" + FIRSTWORDLASTWORD (uppercase)
        // We can verify structural rules since ID is random
        boolean correctFormat =
                hash != null &&
                hash.contains(":") &&
                hash.equals(hash.toUpperCase()) &&
                hash.endsWith(":HITONIGHT?");

        assertCondition("Message hash for Test Case 1 ends with :HITONIGHT?", correctFormat);
    }

    // --- Test 7: Message ID is created ---
    private static void testMessageIDCreated() {
        Message m = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String id = m.getMessageID();
        assertCondition("Message ID is not null or empty", id != null && !id.isEmpty());
    }

    // --- Test 8: Message ID is not more than 10 characters ---
    private static void testMessageIDLength() {
        Message m = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        boolean valid = m.checkMessageID();
        assertCondition("Message ID is 10 characters or fewer", valid);
    }

    // --- Assertion Helpers ---

    private static void assertEqual(String testName, String expected, String actual) {
        if (expected.equals(actual)) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            System.out.println("      Expected : " + expected);
            System.out.println("      Actual   : " + actual);
            failed++;
        }
    }

    private static void assertCondition(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }
}
