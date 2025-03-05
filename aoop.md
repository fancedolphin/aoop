aoop

cli version

weaver game in java

1. The starting word and target word will be fixed at four letters.
   2. Players will have unlimited attempts to reach the target word.
   3. Each attempt must produce a valid intermediate word.
   4. If the player successfully reaches the target word, they win.
   5. Feedback is provided for each word entered

For the CLI version, a confirmatory message indicating the player has won is required.

The behaviour of the program shall be controlled by three flags: · One flag should, if set to true, cause an error message to be displayed if the word is not a valid intermediate word. If set to false, no error message should be displayed. · Another flag should, if set to true, display the path (not necessarily the best path) from the start word to the end word for testing purposes. · A third flag should, if set to true, cause the starting and target words to be randomly selected from a dictionary (dictionary.txt). If set to false, these words will be fixed

CLI versions of the program should allow players to input their intermediate words

The Model should load a list of valid words from a fixed location (from one provided file, dictionary.txt). This list will serve as the potential valid intermediate words for the player

The CLI should indicate the start word, the end word, and any intermediate words

must be constructed according to the principles of MVC

Model. This should have an interface designed to be convenient for the Controller, View and JUnit class to use with no superfluous public methods, no references to two classes and contain no GUI code. It may consist of several classes but there must be a class called Model or similar that provides the interface and this class should extend Observable. File reading should also be done in the Model. A high mark will be earned for a Model that implements all the required functionality and respects all these constraints. A pass mark will be earned for a Model that implements only some of the required functionality or fails to respect these constraints.



gui version

1. The starting word and target word will be fixed at four letters.
2. Players will have unlimited attempts to reach the target word. 
3. Each attempt must produce a valid intermediate word.
4. If the player successfully reaches the target word, they win.
5. Feedback is provided for each word entered (see figure 3):

·     Letters in the correct position are highlighted in green.

·     Letters not in the target word are shown in grey.



For the GUI version, a confirmatory message or a message box should be displayed to indicate whether the player has won (transformed the starting word into the target word), even though the game status is clear from the tile colouring on the last filled row.

The behaviour of the program shall be controlled by three flags:

·      One flag should, if set to true, cause an error message to be displayed if the word is not a valid intermediate word. If set to false, no error message should be displayed.

·      Another flag should, if set to true, display the path (not necessarily the best path) from the start word to the end word for testing purposes.

A third flag should, if set to true, cause the starting and target words to be randomly selected from a dictionary (dictionary.txt). If set to false, these words will be fixed, as shown in figure 2.

GUI versions of the program should allow players to input their intermediate words

The GUI version should have a button to request a reset of the game (return to the original state), which will be enabled only after the first valid input has been made. The GUI version should also have a button to request a new game, regardless of whether input has been made. This is not required for the CLI version.

  The GUI version must be constructed  according to the principles of MVC, as restated below. Code that belongs in  the View but is placed in the Model will usually not be counted towards the  marks for the View. Similar rules will apply for other misplaced code.  

The code must be documented with assert statements, unit testing, class diagrams, docstrings and comments as described below.

The flags mentioned in FR3 should be in the Model. It is necessary for them to be changeable at runtime. (e.g., using three buttons or other controls

| Model. This should have an interface designed to be  convenient for the Controller, View and JUnit class to use with no  superfluous public methods, no references to two classes and contain no GUI  code. It may consist of several classes but there must be a class called  Model or similar that provides the interface and this class should extend  Observable. File reading should also be done in the Model. A high mark will  be earned for a Model that implements all the required functionality and  respects all these constraints. A pass mark will be earned for a Model that  implements only some of the required functionality or fails to respect these  constraints. |
| ------------------------------------------------------------ |
| Controller. This should forward only  valid requests to the Model, querying the Model if necessary to find out if  the request is valid, and must also enable / disable buttons as described  above in the functional requirements. It must have no GUI code, though it may  send messages to the View. A high mark will be given to a controller that  respects all these constraints and a pass mark will be given to a controller  that respects only some of them |
| View of GUI version using the Swing framework. It  should implement Observer and therefore have an update method that is called  when the Model changes. This will be marked according to how many of the  functional requirements have been met. A high mark will be given to a view  that implements all the requirements and a pass mark will be given to a view  that implements only some of them. |

Specification of Model with asserts. This should include invariants for the class as well as pre and post conditions for each public method in the model. This will be marked according to how many of the relevant conditions are included and whether the ones that are included are correct. Partial credit will be available for describing them in English. A high mark will be given to a specification that includes all the relevant constraints. A pass mark will be given to a specification that includes only a few of them.

import java.io.*;

import java.util.*;



public class WeaverGame {

​    public static void main(String[] args) {

​        // 从文件中读取字典

​        List<String> validWords = loadDictionary("dictionary.txt");



​        // 随机选择起始和目标单词

​        Random rand = new Random();

​        String startWord = validWords.get(rand.nextInt(validWords.size()));

​        String targetWord = validWords.get(rand.nextInt(validWords.size()));



​        // 创建模型、控制器和视图

​        WeaverGameModel model = new WeaverGameModel(startWord, targetWord);

​        WeaverGameController controller = new WeaverGameController(model);

​        WeaverGameView view = new WeaverGameView(controller);

​    }



​    // 从文件加载字典

​    private static List<String> loadDictionary(String filename) {

​        List<String> words = new ArrayList<>();

​        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

​            String line;

​            while ((line = reader.readLine()) != null) {

​                line = line.trim().toUpperCase();  // 转为大写

​                if (line.length() == 4) {  // 只加载四个字母的单词

​                    words.add(line);

​                }

​            }

​        } catch (IOException e) {

​            e.printStackTrace();

​        }

​        return words;

​    }

}

import java.awt.event.*;

import java.util.List;



public class WeaverGameController {

​    private WeaverGameModel model;



​    public WeaverGameController(WeaverGameModel model) {

​        this.model = model;

​    }



​    // Handle user input and update model

​    public String processInput(String word) {

​        if (!model.isValidWord(word)) {

​            if (model.getErrorFlag()) {

​                return "Invalid word!";

​            }

​        } else {

​            String feedback = model.getFeedback(word);

​            if (model.isTargetWord(word)) {

​                return "Congratulations! You've reached the target word!";

​            } else {

​                return "Feedback: " + feedback;

​            }

​        }

​        return "Try again!";

​    }

​    public void startNewGame() {

​    List<String> dictionary = model.getValidWords();

​    model.resetGame(dictionary);

}

​    public WeaverGameModel getModel() {

​        return this.model;

​    }

​    // Optionally, show path from start to target

​    public String showPath() {

​        if (model.getShowPathFlag()) {

​            return "Path is displayed!";

​        }

​        return "Path not enabled.";

​    }

}

import java.io.*;

import java.util.*;



public class WeaverGameModel extends Observable {

​    private String startWord;

​    private String targetWord;

​    private List<String> validWords;

​    private boolean errorFlag;

​    private boolean showPathFlag;

​    private boolean randomWordsFlag;



​    // Constructor

​    public WeaverGameModel(String startWord, String targetWord) {

​        this.startWord = startWord;

​        this.targetWord = targetWord;

​        this.validWords = loadDictionary("dictionary.txt"); // 从文件加载字典

​        this.errorFlag = false;

​        this.showPathFlag = false;

​        this.randomWordsFlag = false;

​    }



​    // 从文件中加载字典

​    private List<String> loadDictionary(String filename) {

​        List<String> words = new ArrayList<>();

​        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

​            String line;

​            while ((line = reader.readLine()) != null) {

​                line = line.trim().toUpperCase();  // 转为大写，确保一致性

​                if (line.length() == 4) {  // 只加载四个字母的单词

​                    words.add(line);

​                }

​            }

​        } catch (IOException e) {

​            e.printStackTrace();

​        }

​        return words;

​    }



​    // Getter methods for start and target words

​    public String getStartWord() {

​        return startWord;

​    }



​    public String getTargetWord() {

​        return targetWord;

​    }



​    // Validate if a word is a valid intermediate word

​    public boolean isValidWord(String word) {

​        return validWords.contains(word);

​    }



​    // Check if the current word is the target word

​    public boolean isTargetWord(String word) {

​        return word.equals(targetWord);

​    }

​    

​    

​    private boolean isValidTransition(String from, String to) {

​        int diffCount = 0;

​        for (int i = 0; i < from.length(); i++) {

​            if (from.charAt(i) != to.charAt(i)) {

​                diffCount++;

​                if (diffCount > 1) return false;

​            }

​        }

​        return diffCount == 1;

​    }

​    public void resetGame(List<String> validWords) {

​        Random rand = new Random();

​        do {

​            this.startWord = validWords.get(rand.nextInt(validWords.size()));

​            this.targetWord = validWords.get(rand.nextInt(validWords.size()));

​        } while (startWord.equals(targetWord)); // 确保起始词不等于目标词

​        



​    }

​        // Get feedback on the current word (green or gray)

​    public String getFeedback(String word) {

​        StringBuilder feedback = new StringBuilder();

​        for (int i = 0; i < word.length(); i++) {

​            if (word.charAt(i) == targetWord.charAt(i)) {

​                feedback.append("G");  // Green

​            } else if (targetWord.indexOf(word.charAt(i)) != -1) {

​                feedback.append("Y");  // Yellow

​            } else {

​                feedback.append("X");  // Gray

​            }

​        }

​        return feedback.toString();

​    }



​    // Setters for flags (for runtime changes)

​    public void setErrorFlag(boolean errorFlag) {

​        this.errorFlag = errorFlag;

​    }



​    public void setShowPathFlag(boolean showPathFlag) {

​        this.showPathFlag = showPathFlag;

​    }



​    public void setRandomWordsFlag(boolean randomWordsFlag) {

​        this.randomWordsFlag = randomWordsFlag;

​    }



​    public boolean getErrorFlag() {

​        return errorFlag;

​    }



​    public boolean getShowPathFlag() {

​        return showPathFlag;

​    }



​    public boolean getRandomWordsFlag() {

​        return randomWordsFlag;

​    }



​    public List<String> getValidWords() {

​        // 假设 validWords 是从文件或数据库加载的有效词汇列表

​        return validWords;  // 返回有效的词汇列表

​    }

}

import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;



public class WeaverGameView extends JFrame {

​    private WeaverGameController controller;

​    private JTextField inputField;

​    private JButton submitButton;

​    private JTextArea feedbackArea;

​    private JButton resetButton;

​    private JButton newGameButton;

​    private JLabel startWordLabel;

​    private JLabel targetWordLabel;

​    private JTextArea enteredWordsArea;

​    private List<String> enteredWords = new ArrayList<>();

​    private boolean isValidInputMade = false;



​    // Panels for start word, target word, and entered words

​    private JPanel enteredWordsPanel;

​    private JPanel startWordPanel;

​    private JPanel targetWordPanel;



​    public WeaverGameView(WeaverGameController controller) {

​        this.controller = controller;

​        controller.getModel().addObserver((o, arg) -> {

​            updateWordsDisplay(controller.getModel().getStartWord(),

​                    controller.getModel().getTargetWord());

​        });

​        initialize();  // Initialize the UI components

​    }





​    private void initialize() {

​        setTitle("Word Ladder Game");  // Set window title

​        setSize(400, 400);  // Set window size

​        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close the app when the window is closed

​        setLayout(new BorderLayout());  // Use BorderLayout for the main window layout



​        JPanel panel = new JPanel();

​        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Stack components vertically



​        // Initialize enteredWordsPanel, startWordPanel, and targetWordPanel

​        enteredWordsPanel = new JPanel();

​        enteredWordsPanel.setLayout(new BoxLayout(enteredWordsPanel, BoxLayout.Y_AXIS));

​        enteredWordsPanel.setOpaque(false);  // Make the entered words panel non-opaque



​        startWordPanel = createWordPanel(controller.getModel().getStartWord(), "start");

​        targetWordPanel = createWordPanel(controller.getModel().getTargetWord(), "target");



​        // Add start word panel, entered words panel, and target word panel to the main panel

​        panel.add(startWordPanel);

​        panel.add(enteredWordsPanel);

​        panel.add(targetWordPanel);



​        JLabel inputLabel = new JLabel("Enter Word: ");

​        inputField = new JTextField(10);  // Input field for the user to type a word

​        submitButton = new JButton("Submit");  // Submit button to process the input word

​        panel.add(inputLabel);

​        panel.add(inputField);

​        panel.add(submitButton);



​        feedbackArea = new JTextArea(3, 30);  // Area to show feedback after each submission

​        feedbackArea.setEditable(false);  // Make feedback area read-only

​        JScrollPane feedbackScrollPane = new JScrollPane(feedbackArea);  // Add scrolling functionality

​        panel.add(feedbackScrollPane);



​        enteredWordsArea = new JTextArea(5, 30);  // Area to show entered words

​        enteredWordsArea.setEditable(false);  // Make entered words area read-only

​        JScrollPane enteredWordsScrollPane = new JScrollPane(enteredWordsArea);

​        panel.add(enteredWordsScrollPane);



​        resetButton = new JButton("Reset");  // Button to reset the game

​        newGameButton = new JButton("New Game");  // Button to start a new game

​        panel.add(resetButton);

​        panel.add(newGameButton);



​        add(panel, BorderLayout.CENTER);  // Add the main panel to the center of the window



​        // Action listener for the submit button

​        submitButton.addActionListener(new ActionListener() {

​            @Override

​            public void actionPerformed(ActionEvent e) {

​                String word = inputField.getText().toUpperCase().trim();  // Get the input word

​                controller.processInput(word);  // Process the input and get feedback

​                String feedback = controller.getModel().getFeedback(word);  // Get feedback for this word

​                feedbackArea.setText(feedback);  // Display feedback in the feedback area



​                if (!word.isEmpty()) {

​                    enteredWords.add(word);  // Store the entered word

​                    enteredWordsArea.setText(String.join("\n", enteredWords));  // Display entered words

​                    updateEnteredWordsPanel(word, feedback);  // Update entered words with feedback

​                }



​                inputField.setText("");  // Clear input field for the next word

​                if (controller.getModel().isValidWord(word)) {

​                    isValidInputMade = true;

​                    enableResetButton(true);  // Enable the reset button

​                }

​            }

​        });



​        // Action listener for the reset button

​        resetButton.addActionListener(new ActionListener() {

​            @Override

​            public void actionPerformed(ActionEvent e) {

​                // Clear the entered words and reset the cells

​                enteredWords.clear();

​        enteredWordsArea.setText("");  // Clear the entered words text area

​        enteredWordsPanel.removeAll();  // Remove all entered words cells

​        enteredWordsPanel.revalidate();  // Revalidate to update the layout

​        enteredWordsPanel.repaint();  // Repaint to refresh the display



​        // Reset feedback and input field

​        feedbackArea.setText("");  // Clear the feedback area

​        inputField.setText("");  // Clear the input field



​        // Reset the start and target words panels to their initial state

​        updateWordsDisplay(controller.getModel().getStartWord(), controller.getModel().getTargetWord());



​        resetButton.setEnabled(false);

​            }

​        });



​        // Action listener for the new game button

​        newGameButton.addActionListener(new ActionListener() {

​            @Override

​            public void actionPerformed(ActionEvent e) {

​                controller.startNewGame();  // Start a new game

​                updateWordsDisplay(controller.getModel().getStartWord(),

​                                    controller.getModel().getTargetWord());  // Update start/target words

​        

​                // Clear the entered words but keep the start/target word panels intact

​                enteredWords.clear();

​                enteredWordsArea.setText("");  // Clear the entered words text area

​                enteredWordsPanel.removeAll();  // Clear the entered words panel

​                enteredWordsPanel.revalidate();  // Revalidate to update the layout

​                enteredWordsPanel.repaint();  // Repaint to refresh the display

​        

​                feedbackArea.setText("New game started!");  // Set feedback message

​                inputField.setText("");  // Clear the input field

​            }

​        });



​        setVisible(true);  // Make the window visible

​    }



​    /**

​     \* Enables or disables the reset button.

​     \* @param enabled true to enable the button, false to disable it

​     */

​    public void enableResetButton(boolean enabled) {

​        resetButton.setEnabled(enabled);  // Enable or disable the reset button

​    }



​    /**

​     \* Updates the start and target word panels to display the current start and target words.

​     \* @param startWord the start word to display

​     \* @param targetWord the target word to display

​     */

​    private void updateWordsDisplay(String startWord, String targetWord) {

​        startWordPanel.removeAll();

​        targetWordPanel.removeAll();

​        

​        // Add the letters of the start word as cells

​        Arrays.stream(startWord.split(""))

​              .forEach(c -> startWordPanel.add(new Cell(c, "X")));  // 'X' is the initial status

​    

​        // Add the letters of the target word as cells

​        Arrays.stream(targetWord.split(""))

​              .forEach(c -> targetWordPanel.add(new Cell(c, "X")));  // 'X' is the initial status

​    

​        // Revalidate and repaint the panels to update the UI

​        startWordPanel.revalidate();

​        targetWordPanel.revalidate();

​    }

​    



​    /**

​     \* Creates a panel displaying each letter of the word with its status.

​     \* @param word the word to display

​     \* @param type the type of word ("start" or "target")

​     \* @return the panel displaying the word

​     */

   

​     private JPanel createWordPanel(String word, String type) {

​        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));

​        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.X_AXIS)); 

​        wordPanel.setOpaque(false);

​        

​        for (int i = 0; i < word.length(); i++) {

​            String letter = String.valueOf(word.charAt(i));

​            String status = type.equals("start") ? "X" : "X"; // 初始状态设为灰色

​            Cell cell = new Cell(letter, status);

​            wordPanel.add(cell);

​        }

​        return wordPanel;

​    }



​    /**

​     \* Updates the entered words panel by adding a new word with feedback.

​     \* @param word the word to display

​     \* @param feedback the feedback for the word (e.g., "G", "Y", "X")

​     */

​    private void updateEnteredWordsPanel(String word, String feedback) {

​        JPanel wordPanel = new JPanel();

​        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.X_AXIS));  // Use BoxLayout for horizontal arrangement

​        wordPanel.setOpaque(false);  //



​        // Create a cell for each letter of the word with corresponding feedback

​        for (int i = 0; i < word.length(); i++) {

​            String letter = String.valueOf(word.charAt(i));

​            String feedbackStatus = String.valueOf(feedback.charAt(i));  // Get the feedback for the letter

​            Cell cell = new Cell(letter, "X");  // Create a new cell for the letter

​            cell.setStatus(feedbackStatus);  // Set the feedback status

​            wordPanel.add(cell);  // Add the cell to the panel

​        }



​        enteredWordsPanel.add(wordPanel);  // Add the word panel to the entered words panel

​        enteredWordsPanel.revalidate();  // Revalidate to update the layout

​        enteredWordsPanel.repaint();  // Repaint to refresh the display

​    }



​    /**

​     \* Cell class to display each letter of the word with its feedback status.

​     */

​    private class Cell extends JPanel {

​        private String value;

​        private String status;



​        public Cell(String value, String status) {

​            this.value = value;

​            this.status = status;

​            setPreferredSize(new Dimension(40, 40));  // Set the size of the cell

​            setOpaque(true);  // Enable opacity for the cell

​            setBackground(getColorForStatus(status));  // Set the background color based on status

​            setLayout(new BorderLayout());

​            add(new JLabel(value, SwingConstants.CENTER));  // Add the letter to the center of the cell

​        }



​        @Override

​        protected void paintComponent(Graphics g) {

​            super.paintComponent(g);

​            g.setColor(getBackground());

​            g.fillRect(0, 0, getWidth(), getHeight());  // Fill the cell with the background color

​        }



​        /**

​         \* Returns the color for a given status.

​         \* @param status the feedback status ("G", "Y", or "X")

​         \* @return the color corresponding to the status

​         */

​        private Color getColorForStatus(String status) {

​            switch (status) {

​                case "G":  // Green for correct

​                    return Color.GREEN;

​                case "Y":  // Yellow for present but incorrect

​                    return Color.YELLOW;

​                case "X":  // Gray for absent

​                    return Color.GRAY;

​                default:

​                    return Color.WHITE;  // Default to white if no status is specified

​            }

​        }



​        public void setStatus(String status) {

​            this.status = status;

​            setBackground(getColorForStatus(status));  // Update the cell color based on new status

​            repaint();  // Repaint to update the display

​        }



​        public void setValue(String value) {

​            this.value = value;

​            repaint();  // Repaint to update the letter in the cell

​        }

​    }



​    

}