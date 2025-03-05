import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class WeaverGameController {
    private WeaverGameModel model;

    public WeaverGameController(WeaverGameModel model) {
        this.model = model;
    }

    // Handle user input and update model
    public String processInput(String word) {
        if (word == null || word.trim().isEmpty()) {
            return "Empty input!";
        }

        String processedWord = word.toUpperCase().trim();
        if (processedWord.length() != 4) {
            return "Invalid word! Must be 4 letters.";
        }

        if (!model.isValidWord(processedWord)) {
            return model.getErrorFlag() ? "Invalid word! Not in dictionary." : "";
        }

        return generateFeedback(processedWord);
    }

    // Generate feedback after processing the word
    private String generateFeedback(String word) {
        String feedback = model.getFeedback(word);
        if (model.isTargetWord(word)) {
            return "Success! Target reached: " + feedback;
        }
        return "Valid transition. Feedback: " + feedback;
    }

    public void startNewGame(List<String> validWords) {
        String startWord = getRandomWord(validWords);
        String targetWord = getRandomWord(validWords);
        model.setStartWord(startWord);
        model.setTargetWord(targetWord);
    }

    private String getRandomWord(List<String> validWords) {
        Random rand = new Random();
        return validWords.get(rand.nextInt(validWords.size()));
    }

    public WeaverGameModel getModel() {
        return this.model;
    }

    // Optionally, show path from start to target
    public String showPath() {
        if (model.getShowPathFlag()) {
            List<String> path = model.findPath();
            if (path.isEmpty()) {
                return "No path found!";
            }
            return "Path: " + String.join(" -> ", path);
        }
        return "Path not enabled.";
    }
}

