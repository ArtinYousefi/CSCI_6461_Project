import java.io.*;
import java.util.Scanner;

public class test2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] sentences = new String[6];

        try {
            // Read paragraph from file
            BufferedReader reader = new BufferedReader(new FileReader("paragraph.txt"));
            for (int i = 0; i < 6; i++) {
                sentences[i] = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading the paragraph file.");
            return;
        }

        // Print sentences
        System.out.println("Paragraph:");
        for (int i = 0; i < 6; i++) {
            System.out.println((i + 1) + ": " + sentences[i]);
        }

        // Ask user for a word to search
        System.out.println("Enter a word to search:");
        String word = scanner.next();

        // Search for the word
        boolean found = false;
        for (int i = 0; i < 6; i++) {
            String[] words = sentences[i].split("\\s+"); // Split sentence into words
            for (int j = 0; j < words.length; j++) {
                if (words[j].equalsIgnoreCase(word)) {
                    System.out.println("Word found: " + word);
                    System.out.println("Sentence number: " + (i + 1));
                    System.out.println("Word position: " + (j + 1));
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Word not found in the paragraph.");
        }

        scanner.close();
    }
}
