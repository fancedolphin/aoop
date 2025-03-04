import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeaverGameView extends JFrame {
    private WeaverGameController controller;
    private JTextField inputField;
    private JButton submitButton;
    private JTextArea feedbackArea;
    private JButton resetButton;
    private JButton newGameButton;
    private JLabel startWordLabel;
    private JLabel targetWordLabel;
    private JTextArea enteredWordsArea;
    private List<String> enteredWords = new ArrayList<>();
    private boolean isValidInputMade = false;

    // Panels for start word, target word, and entered words
    private JPanel enteredWordsPanel;
    private JPanel startWordPanel;
    private JPanel targetWordPanel;

    public WeaverGameView(WeaverGameController controller) {
        this.controller = controller;
        controller.getModel().addObserver((o, arg) -> {
            updateWordsDisplay(controller.getModel().getStartWord(),
                    controller.getModel().getTargetWord());
        });
        initialize();  // Initialize the UI components
    }


    private void initialize() {
        setTitle("Word Ladder Game");  // Set window title
        setSize(400, 400);  // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close the app when the window is closed
        setLayout(new BorderLayout());  // Use BorderLayout for the main window layout

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Stack components vertically

        // Initialize enteredWordsPanel, startWordPanel, and targetWordPanel
        enteredWordsPanel = new JPanel();
        enteredWordsPanel.setLayout(new BoxLayout(enteredWordsPanel, BoxLayout.Y_AXIS));
        enteredWordsPanel.setOpaque(false);  // Make the entered words panel non-opaque

        startWordPanel = createWordPanel(controller.getModel().getStartWord(), "start");
        targetWordPanel = createWordPanel(controller.getModel().getTargetWord(), "target");

        // Add start word panel, entered words panel, and target word panel to the main panel
        panel.add(startWordPanel);
        panel.add(enteredWordsPanel);
        panel.add(targetWordPanel);

        JLabel inputLabel = new JLabel("Enter Word: ");
        inputField = new JTextField(10);  // Input field for the user to type a word
        submitButton = new JButton("Submit");  // Submit button to process the input word
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(submitButton);

        feedbackArea = new JTextArea(3, 30);  // Area to show feedback after each submission
        feedbackArea.setEditable(false);  // Make feedback area read-only
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackArea);  // Add scrolling functionality
        panel.add(feedbackScrollPane);

        enteredWordsArea = new JTextArea(5, 30);  // Area to show entered words
        enteredWordsArea.setEditable(false);  // Make entered words area read-only
        JScrollPane enteredWordsScrollPane = new JScrollPane(enteredWordsArea);
        panel.add(enteredWordsScrollPane);

        resetButton = new JButton("Reset");  // Button to reset the game
        newGameButton = new JButton("New Game");  // Button to start a new game
        panel.add(resetButton);
        panel.add(newGameButton);

        add(panel, BorderLayout.CENTER);  // Add the main panel to the center of the window

        // Action listener for the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = inputField.getText().toUpperCase().trim();  // Get the input word
                controller.processInput(word);  // Process the input and get feedback
                String feedback = controller.getModel().getFeedback(word);  // Get feedback for this word
                feedbackArea.setText(feedback);  // Display feedback in the feedback area

                if (!word.isEmpty()) {
                    enteredWords.add(word);  // Store the entered word
                    enteredWordsArea.setText(String.join("\n", enteredWords));  // Display entered words
                    updateEnteredWordsPanel(word, feedback);  // Update entered words with feedback
                }

                inputField.setText("");  // Clear input field for the next word
                if (controller.getModel().isValidWord(word)) {
                    isValidInputMade = true;
                    enableResetButton(true);  // Enable the reset button
                }
            }
        });

        // Action listener for the reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the entered words and reset the cells
                enteredWords.clear();
        enteredWordsArea.setText("");  // Clear the entered words text area
        enteredWordsPanel.removeAll();  // Remove all entered words cells
        enteredWordsPanel.revalidate();  // Revalidate to update the layout
        enteredWordsPanel.repaint();  // Repaint to refresh the display

        // Reset feedback and input field
        feedbackArea.setText("");  // Clear the feedback area
        inputField.setText("");  // Clear the input field

        // Reset the start and target words panels to their initial state
        updateWordsDisplay(controller.getModel().getStartWord(), controller.getModel().getTargetWord());

        resetButton.setEnabled(false);
            }
        });

        // Action listener for the new game button
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startNewGame();  // Start a new game
                updateWordsDisplay(controller.getModel().getStartWord(),
                                    controller.getModel().getTargetWord());  // Update start/target words
        
                // Clear the entered words but keep the start/target word panels intact
                enteredWords.clear();
                enteredWordsArea.setText("");  // Clear the entered words text area
                enteredWordsPanel.removeAll();  // Clear the entered words panel
                enteredWordsPanel.revalidate();  // Revalidate to update the layout
                enteredWordsPanel.repaint();  // Repaint to refresh the display
        
                feedbackArea.setText("New game started!");  // Set feedback message
                inputField.setText("");  // Clear the input field
            }
        });

        setVisible(true);  // Make the window visible
    }

    /**
     * Enables or disables the reset button.
     * @param enabled true to enable the button, false to disable it
     */
    public void enableResetButton(boolean enabled) {
        resetButton.setEnabled(enabled);  // Enable or disable the reset button
    }

    /**
     * Updates the start and target word panels to display the current start and target words.
     * @param startWord the start word to display
     * @param targetWord the target word to display
     */
    private void updateWordsDisplay(String startWord, String targetWord) {
        startWordPanel.removeAll();
        targetWordPanel.removeAll();
        
        // Add the letters of the start word as cells
        Arrays.stream(startWord.split(""))
              .forEach(c -> startWordPanel.add(new Cell(c, "X")));  // 'X' is the initial status
    
        // Add the letters of the target word as cells
        Arrays.stream(targetWord.split(""))
              .forEach(c -> targetWordPanel.add(new Cell(c, "X")));  // 'X' is the initial status
    
        // Revalidate and repaint the panels to update the UI
        startWordPanel.revalidate();
        targetWordPanel.revalidate();
    }
    

    /**
     * Creates a panel displaying each letter of the word with its status.
     * @param word the word to display
     * @param type the type of word ("start" or "target")
     * @return the panel displaying the word
     */
   
     private JPanel createWordPanel(String word, String type) {
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.X_AXIS)); 
        wordPanel.setOpaque(false);
        
        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            String status = type.equals("start") ? "X" : "X"; // 初始状态设为灰色
            Cell cell = new Cell(letter, status);
            wordPanel.add(cell);
        }
        return wordPanel;
    }

    /**
     * Updates the entered words panel by adding a new word with feedback.
     * @param word the word to display
     * @param feedback the feedback for the word (e.g., "G", "Y", "X")
     */
    private void updateEnteredWordsPanel(String word, String feedback) {
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.X_AXIS));  // Use BoxLayout for horizontal arrangement
        wordPanel.setOpaque(false);  //

        // Create a cell for each letter of the word with corresponding feedback
        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            String feedbackStatus = String.valueOf(feedback.charAt(i));  // Get the feedback for the letter
            Cell cell = new Cell(letter, "X");  // Create a new cell for the letter
            cell.setStatus(feedbackStatus);  // Set the feedback status
            wordPanel.add(cell);  // Add the cell to the panel
        }

        enteredWordsPanel.add(wordPanel);  // Add the word panel to the entered words panel
        enteredWordsPanel.revalidate();  // Revalidate to update the layout
        enteredWordsPanel.repaint();  // Repaint to refresh the display
    }

    /**
     * Cell class to display each letter of the word with its feedback status.
     */
    private class Cell extends JPanel {
        private String value;
        private String status;

        public Cell(String value, String status) {
            this.value = value;
            this.status = status;
            setPreferredSize(new Dimension(40, 40));  // Set the size of the cell
            setOpaque(true);  // Enable opacity for the cell
            setBackground(getColorForStatus(status));  // Set the background color based on status
            setLayout(new BorderLayout());
            add(new JLabel(value, SwingConstants.CENTER));  // Add the letter to the center of the cell
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());  // Fill the cell with the background color
        }

        /**
         * Returns the color for a given status.
         * @param status the feedback status ("G", "Y", or "X")
         * @return the color corresponding to the status
         */
        private Color getColorForStatus(String status) {
            switch (status) {
                case "G":  // Green for correct
                    return Color.GREEN;
                case "Y":  // Yellow for present but incorrect
                    return Color.YELLOW;
                case "X":  // Gray for absent
                    return Color.GRAY;
                default:
                    return Color.WHITE;  // Default to white if no status is specified
            }
        }

        public void setStatus(String status) {
            this.status = status;
            setBackground(getColorForStatus(status));  // Update the cell color based on new status
            repaint();  // Repaint to update the display
        }

        public void setValue(String value) {
            this.value = value;
            repaint();  // Repaint to update the letter in the cell
        }
    }

    
}






