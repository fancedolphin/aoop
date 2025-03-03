import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    
    // Panels for start word, target word, and entered words
    private JPanel enteredWordsPanel;
    private JPanel startWordPanel;
    private JPanel targetWordPanel;

    public WeaverGameView(WeaverGameController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        setTitle("Word Ladder Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Stack components vertically

        // Initialize enteredWordsPanel, startWordPanel, and targetWordPanel
        enteredWordsPanel = new JPanel();
        enteredWordsPanel.setLayout(new BoxLayout(enteredWordsPanel, BoxLayout.Y_AXIS));
        enteredWordsPanel.setOpaque(false);  // Make the entered words panel non-opaque

        startWordPanel = createWordPanel(controller.getModel().getStartWord(), "start");
        targetWordPanel = createWordPanel(controller.getModel().getTargetWord(), "target");

        panel.add(startWordPanel);  // Add start word panel
        panel.add(enteredWordsPanel);  // Add entered words panel (now between start and target words)
        panel.add(targetWordPanel);  // Add target word panel

        JLabel inputLabel = new JLabel("Enter Word: ");
        inputField = new JTextField(10);
        submitButton = new JButton("Submit");
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(submitButton);

        feedbackArea = new JTextArea(3, 30);
        feedbackArea.setEditable(false);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackArea);
        panel.add(feedbackScrollPane);

        enteredWordsArea = new JTextArea(5, 30);
        enteredWordsArea.setEditable(false);
        JScrollPane enteredWordsScrollPane = new JScrollPane(enteredWordsArea);
        panel.add(enteredWordsScrollPane);

        resetButton = new JButton("Reset");
        newGameButton = new JButton("New Game");
        panel.add(resetButton);
        panel.add(newGameButton);

        add(panel, BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = inputField.getText().toUpperCase().trim();
                controller.processInput(word);  // Process the input and get feedback
                String feedback = controller.getModel().getFeedback(word);  // Get feedback for this word
                feedbackArea.setText(feedback);

                if (!word.isEmpty()) {
                    enteredWords.add(word);  // Store the entered word
                    enteredWordsArea.setText(String.join("\n", enteredWords));  // Display entered words
                    updateEnteredWordsPanel(word, feedback);  // Update entered words with feedback
                }

                inputField.setText("");  // Clear input field
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enteredWords.clear();
                enteredWordsArea.setText("");  // Clear entered words
                feedbackArea.setText("");  // Clear feedback
                inputField.setText("");  // Clear input field
            }
        });

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                feedbackArea.setText("New game started!");
                enteredWords.clear();
                enteredWordsArea.setText("");  // Clear entered words
                inputField.setText("");  // Clear input field
            }
        });

        setVisible(true);
    }

    public void enableResetButton(boolean enabled) {
        resetButton.setEnabled(enabled);  // Enable or disable Reset button
    }
    public void updateWordsDisplay(String startWord, String targetWord) {
        startWordPanel.removeAll();
        targetWordPanel.removeAll();
        
        startWordPanel.add(new JLabel("Start Word: " + startWord));
        targetWordPanel.add(new JLabel("Target Word: " + targetWord));
        
        startWordPanel.revalidate();
        targetWordPanel.revalidate();
        startWordPanel.repaint();
        targetWordPanel.repaint();
    }

    // Helper function to create a word panel with Cell components
    private JPanel createWordPanel(String word, String type) {
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        wordPanel.setOpaque(false);
        wordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        wordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);  // Left-align for stability

        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            String feedbackStatus = "X";  // Default to "X" (absent)
            Cell cell = new Cell(letter, feedbackStatus);  // Create a new Cell for each letter
            wordPanel.add(cell);
        }

        return wordPanel;
    }

    private void updateEnteredWordsPanel(String word, String feedback) {
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        wordPanel.setOpaque(false);
        wordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        wordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            String feedbackStatus = String.valueOf(feedback.charAt(i));
            Cell cell = new Cell(letter, "X");
            cell.setStatus(feedbackStatus);
            wordPanel.add(cell);
        }

        enteredWordsPanel.add(wordPanel);
        enteredWordsPanel.revalidate();
        enteredWordsPanel.repaint();
    }

    // Define the Cell class here
    private class Cell extends JPanel {
        private String value;
        private String status;

        public Cell(String value, String status) {
            this.value = value;
            this.status = status;
            setPreferredSize(new Dimension(40, 40));
            setOpaque(true);  // Enable opacity for this component
            setBackground(getColorForStatus(status));
            setLayout(new BorderLayout());
            add(new JLabel(value, SwingConstants.CENTER));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);  // Clear the previous painting
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());  // Fill the background with the current color
        }

        private Color getColorForStatus(String status) {
            switch (status) {
                case "G":  // Green for correct
                    return Color.GREEN;
                case "Y":  // Yellow for present but incorrect
                    return Color.YELLOW;
                case "X":  // Gray for absent
                    return Color.GRAY;
                default:
                    return Color.WHITE;
            }
        }

        public void setStatus(String status) {
            this.status = status;
            setBackground(getColorForStatus(status));
            repaint();  // Force a repaint to update the color
        }

        public void setValue(String value) {
            this.value = value;
            repaint();  // Repaint to update the label
        }
    }

    public void resetGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetGame'");
    }
}




