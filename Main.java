import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the model (this includes CLI interaction)
        WeaverGameModel model = new WeaverGameModel();

        // Read dictionary file and load valid words
        model.readDictionary("dictionary.txt", 4); // fixed to 4-letter words for simplicity

        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            // Start the game logic
            System.out.println("Weaver is a game where you try to find a way to get from the starting word to the ending word.");
            System.out.println("You can change only one letter at a time, and each word along the way must be a valid word.");
            System.out.println("Enjoy!");

            int wordSize = 4; // Fixed to 4 for simplicity
            String startWord = model.getWordInput("Enter the starting word: ", wordSize);
            String targetWord = model.getWordInput("Enter the target word: ", wordSize);

            int moveCounter = 0;

            while (!model.isEndWord(startWord, targetWord)) {
                moveCounter++;
                String userGuess = model.getWordInput("Enter your guess: ", wordSize);

                // Validate that the guess is one letter apart
                if (model.isOneLetterApart(startWord, userGuess)) {
                    startWord = userGuess; // Update the start word to the new guess
                } else {
                    model.displayErrorMessage("Your guess is not one letter apart.");
                }
            }

            // Display end message
            model.displayEndMessage(startWord, targetWord, moveCounter);

            // Ask if user wants to play again or change the word size
            model.displayGameChoices();
            int userChoice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            if (userChoice == 1) {
                // Continue playing with the same settings (game loop will restart)
                continue;
            }  else if (userChoice == 2) {
                System.out.println("\nThanks for playing!");
                playAgain = false;  // Exit the game
            } else {
                System.out.println("Invalid choice! Exiting...");
                playAgain = false;
            }
        }

        scanner.close(); // Close the scanner when done
    }
}


