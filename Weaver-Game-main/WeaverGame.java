import java.io.*;
import java.util.*;

public class WeaverGame {
    public static void main(String[] args) {
        // 从文件中读取字典
        List<String> validWords = loadDictionary("dictionary.txt");

        // 随机选择起始和目标单词
        Random rand = new Random();
        String startWord = validWords.get(rand.nextInt(validWords.size()));
        String targetWord = validWords.get(rand.nextInt(validWords.size()));

        // 创建模型、控制器和视图
        WeaverGameModel model = new WeaverGameModel(startWord, targetWord);
        WeaverGameController controller = new WeaverGameController(model);
        WeaverGameView view = new WeaverGameView(controller);
    }

    // 从文件加载字典
    private static List<String> loadDictionary(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toUpperCase();  // 转为大写
                if (line.length() == 4) {  // 只加载四个字母的单词
                    words.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}

