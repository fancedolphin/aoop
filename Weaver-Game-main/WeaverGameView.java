import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

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
    private JPanel virtualKeyboardPanel;  // Panel for virtual keyboard

    public WeaverGameView(WeaverGameController controller) {
        this.controller = controller;
        controller.getModel().addObserver((o, arg) -> {
            updateWordsDisplay(controller.getModel().getStartWord(),
                    controller.getModel().getTargetWord());
        });
        initialize();  // Initialize the UI components
    }

    private void initialize() {
        setTitle("Word Ladder Game");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Initialize enteredWordsPanel, startWordPanel, and targetWordPanel
        enteredWordsPanel = new JPanel();
        enteredWordsPanel.setLayout(new BoxLayout(enteredWordsPanel, BoxLayout.Y_AXIS));
        enteredWordsPanel.setOpaque(false);

        startWordPanel = createWordPanel(controller.getModel().getStartWord(), "start");
        targetWordPanel = createWordPanel(controller.getModel().getTargetWord(), "target");

        // Add start word panel, entered words panel, and target word panel to the main panel
        panel.add(startWordPanel);
        panel.add(enteredWordsPanel);
        panel.add(targetWordPanel);

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

        enteredWordsArea = new JTextArea(5, 30);  // Area to show entered words
        enteredWordsArea.setEditable(false);  // Make entered words area read-only
        JScrollPane enteredWordsScrollPane = new JScrollPane(enteredWordsArea);
        panel.add(enteredWordsScrollPane);

        resetButton = new JButton("Reset");
        newGameButton = new JButton("New Game");
        panel.add(resetButton);
        panel.add(newGameButton);

        // Virtual Keyboard Panel
        virtualKeyboardPanel = createVirtualKeyboardPanel();  // Create the virtual keyboard
        panel.add(virtualKeyboardPanel);

        add(panel, BorderLayout.CENTER);
inputField.setDocument(new PlainDocument() {
    @Override
    public void insertString(int offs, String str, AttributeSet a) 
        throws BadLocationException {
        if ((getLength() + str.length()) <= 4) {
            super.insertString(offs, str.toUpperCase(), a);
        }
    }
});

        submitButton.addActionListener(e -> {
            String word = inputField.getText().toUpperCase().trim();
            String response = controller.processInput(word);  // 获取控制器返回的完整响应
            
            // 更新反馈显示逻辑
            feedbackArea.setText(response);
            
            // 仅在有效输入时记录历史
            if (controller.getModel().isValidWord(word)) {
                enteredWords.add(word);
                enteredWordsArea.setText(String.join("\n", enteredWords));
                updateEnteredWordsPanel(word, controller.getModel().getFeedback(word));
                isValidInputMade = true;
                enableResetButton(true);
            }
            
            inputField.setText("");
        });
        // Action listener for the reset button
        resetButton.addActionListener(e -> {
            enteredWords.clear();
            enteredWordsArea.setText("");
            enteredWordsPanel.removeAll();
            enteredWordsPanel.revalidate();
            enteredWordsPanel.repaint();
            feedbackArea.setText("");
            inputField.setText("");
            updateWordsDisplay(controller.getModel().getStartWord(), controller.getModel().getTargetWord());
            resetButton.setEnabled(false);
        });

        // Action listener for the new game button
        newGameButton.addActionListener(e -> {
            controller.startNewGame(controller.getModel().getValidWords());
            updateWordsDisplay(controller.getModel().getStartWord(),
                                controller.getModel().getTargetWord());
            enteredWords.clear();
            enteredWordsArea.setText("");
            enteredWordsPanel.removeAll();
            enteredWordsPanel.revalidate();
            enteredWordsPanel.repaint();
            feedbackArea.setText("New game started!");
            inputField.setText("");
        });
        

        setVisible(true);
    }

    public void enableResetButton(boolean enabled) {
        resetButton.setEnabled(enabled);
    }

    private void updateWordsDisplay(String startWord, String targetWord) {
        startWordPanel.removeAll();
        targetWordPanel.removeAll();

        Arrays.stream(startWord.split(""))
              .forEach(c -> startWordPanel.add(new Cell(c, "X")));

        Arrays.stream(targetWord.split(""))
              .forEach(c -> targetWordPanel.add(new Cell(c, "X")));

        startWordPanel.revalidate();
        targetWordPanel.revalidate();
    }

    private JPanel createWordPanel(String word, String type) {
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.X_AXIS));
        wordPanel.setOpaque(false);

        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            String status = type.equals("start") ? "X" : "X";
            Cell cell = new Cell(letter, status);
            wordPanel.add(cell);
        }
        return wordPanel;
    }

    private JPanel createVirtualKeyboardPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        addKeyRow(mainPanel, "QWERTYUIOP");
        addKeyRow(mainPanel, "ASDFGHJKL");
        addKeyRow(mainPanel, "ZXCVBNM");
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.add(createControlButton("ENTER"));
        controlPanel.add(createControlButton("DELETE"));
        
        mainPanel.add(controlPanel);
        return mainPanel;
    }
    
    private void addKeyRow(JPanel parent, String keys) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        for (char key : keys.toCharArray()) {
            rowPanel.add(createLetterButton(String.valueOf(key)));
        }
        parent.add(rowPanel);
    }
    
    private JButton createLetterButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(createKeyActionListener(text));
        return btn;
    }
    
    private JButton createControlButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(100, 30));
        btn.addActionListener(createKeyActionListener(text));
        return btn;
    }


  

    private ActionListener createKeyActionListener(String key) {
        return e -> {
            if (key.equals("ENTER")) {
                String word = inputField.getText().toUpperCase().trim();
                controller.processInput(word);
                String feedback = controller.getModel().getFeedback(word);
                feedbackArea.setText(feedback);
                if (!word.isEmpty()) {
                    enteredWords.add(word);
                    enteredWordsArea.setText(String.join("\n", enteredWords));
                    updateEnteredWordsPanel(word, feedback);
                }
                inputField.setText("");
            } else if (key.equals("DELETE")) {
                String text = inputField.getText();
                if (!text.isEmpty()) {
                    inputField.setText(text.substring(0, text.length() - 1));  // Remove last character
                }
            } else {
                String currentText = inputField.getText();
                inputField.setText(currentText + key);  // Append the letter to the input field
            }
        };
    }

    private void updateEnteredWordsPanel(String word, String feedback) {
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.X_AXIS));
        wordPanel.setOpaque(false);

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

    private class Cell extends JPanel {
        private String value;
        private String status;

        public Cell(String value, String status) {
            this.value = value;
            this.status = status;
            setPreferredSize(new Dimension(40, 40));
            setOpaque(true);
            setBackground(getColorForStatus(status));
            setLayout(new BorderLayout());
            add(new JLabel(value, SwingConstants.CENTER));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        private Color getColorForStatus(String status) {
            switch (status) {
                case "G":
                    return Color.GREEN;
                case "Y":
                    return Color.YELLOW;
                case "X":
                    return Color.GRAY;
                default:
                    return Color.WHITE;
            }
        }

        public void setStatus(String status) {
            this.status = status;
            setBackground(getColorForStatus(status));
            repaint();
        }

        public void setValue(String value) {
            this.value = value;
            repaint();
        }
    }
}






