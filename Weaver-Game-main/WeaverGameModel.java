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
        this.errorFlag = true;
        this.showPathFlag = false;
        this.randomWordsFlag = false;
    }

    // 从文件中加载字典
    public static List<String> loadDictionary(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toUpperCase();
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
    public void setStartWord(String startWord) {
        this.startWord = startWord;
        setChanged();
        notifyObservers();  // Notify the observers (view) that the start word has been updated
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
        setChanged();
        notifyObservers();  // Notify the observers (view) that the target word has been updated
    }
    // Validate if a word is a valid intermediate word
    public boolean isValidWord(String word) {
        return word != null 
        && word.length() == 4 
        && validWords.contains(word.toUpperCase().trim());
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
        if (!randomWordsFlag) {//可删除
            return;//可删除
        }//可删除
        Random rand = new Random();
        do {
            this.startWord = validWords.get(rand.nextInt(validWords.size()));
            this.targetWord = validWords.get(rand.nextInt(validWords.size()));
        } while (startWord.equals(targetWord) || !isValidWord(startWord) || !isValidWord(targetWord)); // 确保起始词不等于目标词且都在字典中
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

    public List<String> findPath() {
        if (startWord.equals(targetWord)) {
            return Collections.singletonList(startWord);  // No path needed if words are the same
        }

        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        visited.add(startWord);
        queue.add(Arrays.asList(startWord));

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String lastWord = path.get(path.size() - 1);

            for (String neighbor : validWords) {
                if (!visited.contains(neighbor) && isValidTransition(lastWord, neighbor)) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    if (neighbor.equals(targetWord)) {
                        return newPath;
                    }
                    queue.add(newPath);
                    visited.add(neighbor);
                }
            }
        }
        return Collections.emptyList();  // No path found
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
        return validWords;  // 返回有效的词汇列表
    }
}