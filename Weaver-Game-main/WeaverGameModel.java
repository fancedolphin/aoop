import java.io.*;
import java.util.*;

public class WeaverGameModel extends Observable {
    private String startWord;
    private String targetWord;
    private List<String> validWords;
    private boolean errorFlag;
    private boolean showPathFlag;
    private boolean randomWordsFlag;

    // Constructor
    public WeaverGameModel(String startWord, String targetWord) {
        this.startWord = startWord;
        this.targetWord = targetWord;
        this.validWords = loadDictionary("dictionary.txt"); // 从文件加载字典
        this.errorFlag = false;
        this.showPathFlag = false;
        this.randomWordsFlag = false;
    }

    // 从文件中加载字典
    private List<String> loadDictionary(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toUpperCase();  // 转为大写，确保一致性
                if (line.length() == 4) {  // 只加载四个字母的单词
                    words.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    // Getter methods for start and target words
    public String getStartWord() {
        return startWord;
    }

    public String getTargetWord() {
        return targetWord;
    }

    // Validate if a word is a valid intermediate word
    public boolean isValidWord(String word) {
        return validWords.contains(word);
    }

    // Check if the current word is the target word
    public boolean isTargetWord(String word) {
        return word.equals(targetWord);
    }
    
    
    private boolean isValidTransition(String from, String to) {
        int diffCount = 0;
        for (int i = 0; i < from.length(); i++) {
            if (from.charAt(i) != to.charAt(i)) {
                diffCount++;
                if (diffCount > 1) return false;
            }
        }
        return diffCount == 1;
    }
    public void resetGame(List<String> validWords) {
        Random rand = new Random();
        do {
            this.startWord = validWords.get(rand.nextInt(validWords.size()));
            this.targetWord = validWords.get(rand.nextInt(validWords.size()));
        } while (startWord.equals(targetWord)); // 确保起始词不等于目标词
        

    }
        // Get feedback on the current word (green or gray)
    public String getFeedback(String word) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == targetWord.charAt(i)) {
                feedback.append("G");  // Green
            } else if (targetWord.indexOf(word.charAt(i)) != -1) {
                feedback.append("Y");  // Yellow
            } else {
                feedback.append("X");  // Gray
            }
        }
        return feedback.toString();
    }

    // Setters for flags (for runtime changes)
    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    public void setShowPathFlag(boolean showPathFlag) {
        this.showPathFlag = showPathFlag;
    }

    public void setRandomWordsFlag(boolean randomWordsFlag) {
        this.randomWordsFlag = randomWordsFlag;
    }

    public boolean getErrorFlag() {
        return errorFlag;
    }

    public boolean getShowPathFlag() {
        return showPathFlag;
    }

    public boolean getRandomWordsFlag() {
        return randomWordsFlag;
    }

    public List<String> getValidWords() {
        // 假设 validWords 是从文件或数据库加载的有效词汇列表
        return validWords;  // 返回有效的词汇列表
    }
}