import java.io.*;
import java.util.*;
import java.util.Observer;

public class WeaverGameModel {
    private List<String> dictionary;

    // Reads the dictionary from a file
    public void readDictionary(String fileName, int wordSize) {
        dictionary = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = br.readLine()) != null) {
                if (word.length() == wordSize) {
                    dictionary.add(word);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading dictionary file.");
            System.exit(1);
        }
    }

    // Checks if the word exists in the dictionary
    public boolean isValidWord(String word) {
        return dictionary.contains(word);
    }

    // Checks if the word is one letter apart
    public boolean isOneLetterApart(String prevWord, String userGuess) {
        int diff = 0;
        for (int i = 0; i < prevWord.length(); i++) {
            if (prevWord.charAt(i) != userGuess.charAt(i)) {
                diff++;
            }
        }
        return diff == 1;
    }

    // Checks if the current word is the end word
    public boolean isEndWord(String currentWord, String targetWord) {
        return currentWord.equals(targetWord);
    }

    // Method to check if the user input is of correct length
    public String getWordInput(String prompt, int expectedLength) {
        Scanner scanner = new Scanner(System.in);
        String word = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.print(prompt);
            word = scanner.nextLine();

            // Check if word length matches the expected length
            if (word.length() == expectedLength) {
                isValid = true;
            } else {
                System.out.println("Invalid input! Please enter a " + expectedLength + "-letter word.");
            }
        }

        return word;
    }

    // Displays a message for invalid word input
    public void displayInvalidWordMessage(String word) {
        System.out.println("The word " + word + " is not valid.");
    }

    // Method to display the end message when the game is won
    public void displayEndMessage(String startWord, String endWord, int moves) {
        System.out.println("Congratulations! You changed '" + startWord + "' into '" + endWord + "' in " + moves + " moves.");
    }

    // Method to display error message
    public void displayErrorMessage(String message) {
        System.out.println(message);
    }

    // Method to display options for replay
    public void displayGameChoices() {
        System.out.println("\nEnter: \t1 to play again,");
        
        System.out.println("\t2 to exit the program.");
    }
}
