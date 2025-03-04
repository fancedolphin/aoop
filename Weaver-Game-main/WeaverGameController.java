import java.awt.event.*;
import java.util.List;

public class WeaverGameController {
    private WeaverGameModel model;

    public WeaverGameController(WeaverGameModel model) {
        this.model = model;
    }

    // Handle user input and update model
    public String processInput(String word) {
        if (!model.isValidWord(word)) {
            if (model.getErrorFlag()) {
                return "Invalid word!";
            }
        } else {
            String feedback = model.getFeedback(word);
            if (model.isTargetWord(word)) {
                return "Congratulations! You've reached the target word!";
            } else {
                return "Feedback: " + feedback;
            }
        }
        return "Try again!";
    }
    public void startNewGame() {
    List<String> dictionary = model.getValidWords();
    model.resetGame(dictionary);
}
    public WeaverGameModel getModel() {
        return this.model;
    }
    // Optionally, show path from start to target
    public String showPath() {
        if (model.getShowPathFlag()) {
            return "Path is displayed!";
        }
        return "Path not enabled.";
    }
}