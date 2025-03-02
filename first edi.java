import java.io.*;
import java.util.*;

public class WordLadder {
    // 控制标志（可根据需要修改这些值）
    private static final boolean VALIDATE_WORD_FLAG = true;
    private static final boolean DISPLAY_PATH_FLAG = true;
    private static final boolean RANDOM_WORDS_FLAG = true;

    public static void main(String[] args) {
        try {
            // 读取词典
            Map<Integer, Set<String>> dictionary = readDictionary("dictionary.txt");
            
            // 获取起始和目标词
            String[] words = getStartAndEndWords(dictionary);
            String start = words[0], end = words[1];
            
            // 验证词对有效性
            if (!isValidWordPair(start, end, dictionary)) {
                System.out.println("Invalid word pair");
                return;
            }

            // 构建邻接表
            Map<String, List<String>> adjacency = buildAdjacency(dictionary.get(start.length()));

            // 根据标志选择运行模式
            if (DISPLAY_PATH_FLAG) {
                List<String> path = findPathBFS(start, end, adjacency);
                System.out.println("Solution Path: " + path);
            } else {
                playInteractive(start, end, adjacency);
            }
            
        } catch (IOException e) {
            System.err.println("Error reading dictionary: " + e.getMessage());
        }
    }

    // 读取词典文件
    private static Map<Integer, Set<String>> readDictionary(String filename) throws IOException {
        Map<Integer, Set<String>> dict = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String word;
            while ((word = br.readLine()) != null) {
                word = word.trim().toLowerCase();
                dict.computeIfAbsent(word.length(), k -> new HashSet<>()).add(word);
            }
        }
        return dict;
    }

    // 获取起始和目标词
    private static String[] getStartAndEndWords(Map<Integer, Set<String>> dict) {
        if (RANDOM_WORDS_FLAG) {
            Random rand = new Random();
            List<Integer> lengths = new ArrayList<>(dict.keySet());
            int length = lengths.get(rand.nextInt(lengths.size()));
            List<String> words = new ArrayList<>(dict.get(length));
            Collections.shuffle(words);
            return new String[]{words.get(0), words.get(1)};
        } else {
            return new String[]{"cold", "warm"}; // 默认固定词对
        }
    }

    // 构建邻接关系
    private static Map<String, List<String>> buildAdjacency(Set<String> words) {
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> patternMap = new HashMap<>();

        for (String word : words) {
            char[] arr = word.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                char temp = arr[i];
                arr[i] = '*';
                String pattern = new String(arr);
                patternMap.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
                arr[i] = temp;
            }
        }

        for (String word : words) {
            adj.put(word, new ArrayList<>());
            char[] arr = word.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                char temp = arr[i];
                arr[i] = '*';
                String pattern = new String(arr);
                for (String neighbor : patternMap.get(pattern)) {
                    if (!neighbor.equals(word)) {
                        adj.get(word).add(neighbor);
                    }
                }
                arr[i] = temp;
            }
        }
        return adj;
    }

    // BFS查找最短路径
    private static List<String> findPathBFS(String start, String end, Map<String, List<String>> adj) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(Arrays.asList(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String current = path.get(path.size()-1);
            
            if (current.equals(end)) {
                return path;
            }

            for (String neighbor : adj.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return Collections.emptyList();
    }

    // 交互式游戏模式
    private static void playInteractive(String start, String end, Map<String, List<String>> adj) {
        Scanner scanner = new Scanner(System.in);
        String current = start;
        int steps = 0;
        
        System.out.println("Starting word: " + start);
        System.out.println("Target word: " + end);

        while (!current.equals(end)) {
            System.out.print("Current word [" + current + "] Enter next word: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (!isValidTransition(current, input)) {
                if (VALIDATE_WORD_FLAG) {
                    System.out.println("Invalid transition! Only change one letter");
                }
                continue;
            }

            if (!adj.containsKey(input) || !adj.get(current).contains(input)) {
                if (VALIDATE_WORD_FLAG) {
                    System.out.println("Invalid word: " + input);
                }
                continue;
            }

            current = input;
            steps++;
        }

        System.out.println("Congratulations! Solved in " + steps + " steps");
        scanner.close();
    }

    // 验证词对有效性
    private static boolean isValidWordPair(String start, String end, Map<Integer, Set<String>> dict) {
        return start.length() == end.length() &&
               dict.get(start.length()).contains(start) &&
               dict.get(end.length()).contains(end);
    }

    // 验证单字母变化
    private static boolean isValidTransition(String a, String b) {
        if (a.length() != b.length()) return false;
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) diff++;
        }
        return diff == 1;
    }
}
