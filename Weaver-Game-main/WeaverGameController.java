import java.awt.event.*;
import java.util.List;

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
            return "Invalid word! Not in dictionary.";
        }
        
        // 后续成功处理逻辑（反馈生成、路径检查等）
        String feedback = model.getFeedback(processedWord);
        if (model.isTargetWord(processedWord)) {
            return "Success! Target reached: " + feedback;
        }
        return "Valid transition. Feedback: " + feedback;
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