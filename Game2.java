import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Game2 implements GamePanel {

    private JPanel panel;

    private ArrayList<String> sentenceWords;
    private JLabel sentenceLabel;

    private List<mixedOrderSentence> randomSentences;
    private int currentSentenceIndex;
    private JLabel twoSentences;
    private JPanel buttonPanel;
    private int score;
    private JLabel scoreLabel;

    private Stack<Memento> undoStack;  // Stack to hold mementos for undo functionality

    public Game2() {
        sentenceWords = new ArrayList<>();
        undoStack = new Stack<>();

        panel = new JPanel(new BorderLayout());

        panel.setBackground(new Color(0, 0, 0));
        JLabel label = new JLabel("", SwingConstants.CENTER);
        JButton undoButton = new JButton("Undo");
        undoButton.setBackground(new Color(0, 0, 0));
        undoButton.setForeground(new Color(236, 91, 9));

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(0, 0, 0));
        resetButton.setForeground(new Color(236, 91, 9));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBackground(new Color(236, 91, 9));
        newGameButton.setForeground(new Color(16, 16, 16));

        JButton backButton = new JButton("Back to Main");
        backButton.setBackground(new Color(236, 91, 9));
        backButton.setForeground(new Color(16, 16, 16));

        //Scrore label
        score = 0;
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setForeground(new Color(236, 91, 9));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create buttons with random order words
        randomSentences = createRandomSentences();
        currentSentenceIndex = 0;
        String[] currentSentence = randomSentences.get(currentSentenceIndex).splitCharacterSentence;
        buttonPanel = createWordButtonPanel(currentSentence);

        // Create the sentence label
        sentenceLabel = new JLabel("Your sentence: ", SwingConstants.CENTER);

        // Create the twoSentences label
        twoSentences = new JLabel("<html>" + randomSentences.get(currentSentenceIndex).englishSentence + "<br>" + randomSentences.get(currentSentenceIndex).pinyinSentence + "</html>", SwingConstants.CENTER);
        twoSentences.setFont(new Font("Arial", Font.BOLD, 13));

        // Add components to the main panel
        panel.add(label, BorderLayout.NORTH);

        // Panel for the 4 buttons
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(238, 192, 160));
        JPanel innerContentPane = new JPanel(new GridLayout(4, 1));
        innerContentPane.add(buttonPanel);
        innerContentPane.add(twoSentences);
        innerContentPane.add(sentenceLabel);
        innerContentPane.add(scoreLabel);
        contentPanel.add(innerContentPane, BorderLayout.CENTER);

        // Add the content panel to the main panel
        panel.add(contentPanel, BorderLayout.CENTER);

        // Create a panel for control buttons with GridLayout
        JPanel controlPanel = new JPanel(new GridLayout(2, 2));
        controlPanel.add(undoButton);
        controlPanel.add(resetButton);
        controlPanel.add(newGameButton);
        controlPanel.add(backButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        // Back button action listener
        backButton.addActionListener(e -> {
            MainFrame.getInstance().changeContentPane(new GameButtonPanel());
        });

        // Undo button action listener
        undoButton.addActionListener(e -> {
            if (!sentenceWords.isEmpty()) {
                // Create a memento before undoing
                Memento memento = new Memento(new ArrayList<>(sentenceWords));

                // Undo the last word
                String removedWord = sentenceWords.remove(sentenceWords.size() - 1);
                sentenceLabel.setText(buildSentence());

                // Restore the state with the memento
                restoreState(memento, buttonPanel);
            }
        });

        // Reset button action listener
        resetButton.addActionListener(e -> {
            resetGame(buttonPanel);
        });

        // New game button action listener
        newGameButton.addActionListener(e -> {
            currentSentenceIndex = (currentSentenceIndex + 1) % randomSentences.size();
            String[] newSentence = randomSentences.get(currentSentenceIndex).splitCharacterSentence;

            // Update twoSentences label with the new sentence values
            twoSentences.setText("<html>" + randomSentences.get(currentSentenceIndex).englishSentence + "<br>" + randomSentences.get(currentSentenceIndex).pinyinSentence + "</html>");

            resetGameWithNewSentence(newSentence, buttonPanel);
            shuffleWordButtons(buttonPanel); // Shuffle word buttons for the new game
        });

        // Shuffle word buttons to display in a random order
        shuffleWordButtons(buttonPanel);
    }

    private void updateSentence(String word) {
        // Save the current state before updating
        saveMemento();

        sentenceWords.add(word);
        updateSentenceLabel();
    }

    private void updateSentenceLabel() {
        sentenceLabel.setText(buildSentence());
    }

    private void saveMemento() {
        // Save the current state (memento) to the stack
        Memento memento = new Memento(new ArrayList<>(sentenceWords));
        undoStack.push(memento);
    }

    private void restoreState(Memento memento, JPanel buttonPanel) {
        // Restore the state from the memento
        sentenceWords = new ArrayList<>(memento.getState());

        if (!sentenceWords.isEmpty()) {
            // Get the last word added to the sentence
            String lastWord = sentenceWords.get(sentenceWords.size() - 1);

            // Remove the last word from the displayed sentence
            sentenceWords.remove(sentenceWords.size() - 1);
            sentenceLabel.setText(buildSentence());

            // Add the last removed word to the button panel
            addWordButton(lastWord, buttonPanel);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }





    private String buildSentence() {
        StringBuilder sentenceBuilder = new StringBuilder("Your sentence: ");
        for (String wordInSentence : sentenceWords) {
            sentenceBuilder.append(wordInSentence).append(" ");
        }
        return sentenceBuilder.toString().trim();
    }

    class mixedOrderSentence {
        String englishSentence, pinyinSentence, characterSentence;
        String[] splitCharacterSentence = new String[]{};

        public mixedOrderSentence(String englishSentence, String pinyinSentence, String characterSentence, String[] splitCharacterSentence) {
            this.englishSentence = englishSentence;
            this.pinyinSentence = pinyinSentence;
            this.characterSentence = characterSentence;
            this.splitCharacterSentence = splitCharacterSentence;
        }
    }

    private List<mixedOrderSentence> createRandomSentences() {
        List<mixedOrderSentence> sentences = new ArrayList<>();
        mixedOrderSentence sentence1 = new mixedOrderSentence("The weather is very good today", "Jīntiān tiānqì hěn hǎo", "今天天气很好", new String[]{"今天", "天气", "很", "好"});
        mixedOrderSentence sentence2 = new mixedOrderSentence("He likes to watch movies", "Tā xǐhuān kàn diànyǐng", "他喜欢看电影", new String[]{"他", "喜欢", "看", "电影"});
        mixedOrderSentence sentence3 = new mixedOrderSentence("There are three people in my family", "Wǒ de jiā yǒu sān kǒu rén", "我的家有三口人", new String[]{"我", "的", "家", "有", "三", "口", "人"});
        mixedOrderSentence sentence4 = new mixedOrderSentence("Let's go to the park together tomorrow!", "Wǒmen míngtiān yīqǐ qù gōngyuán ba!", "我们明天一起去公园吧!", new String[]{"我们", "明天", "一起", "去", "公园", "吧!"});
        mixedOrderSentence sentence5 = new mixedOrderSentence("Last night I watched TV at home", "Zuótiān wǎnshàng wǒ zài jiā kàn diànshì", "昨天晚上我在家看电视", new String[]{"昨天", "晚上", "我", "在家", "看", "电视"});
        mixedOrderSentence sentence6 = new mixedOrderSentence("Her birthday is on the 22d of April", "Tā de shēngrì shì sì yuè èrshí'èr hào", "她的生日是四月二十二号", new String[]{"她", "的", "生日", "是", "四", "月", "二十二", "号"});
        mixedOrderSentence sentence7 = new mixedOrderSentence("What would you like to eat?", "Nǐ xiǎng chī diǎnr shénme?", "你想吃点什么？", new String[]{"你", "想", "吃点", "什么", "？"});
        mixedOrderSentence sentence8 = new mixedOrderSentence("I think spring in China is beautiful", "Wǒ juéde Zhōngguó de chūn tiān hěn piàoliang", "我觉得中国的春天很漂亮", new String[]{"我", "觉得", "中国", "的", "春天", "很", "漂亮"});
        mixedOrderSentence sentence9 = new mixedOrderSentence("I don't have an answer to this question", "Zhè gè wèntí wǒ méiyǒu dǎsuan", "这个问题我没有答案", new String[]{"这", "个", "问题", "我", "没有", "答案"});
        mixedOrderSentence sentence10 = new mixedOrderSentence("He has two very good friends", "Tā yǒu liǎngge tèbié hǎo de péngyǒu", "他有两个特别好的朋友", new String[]{"他", "有", "两", "个", "特别", "好", "的", "朋友"});
        mixedOrderSentence sentence11 = new mixedOrderSentence("The coffee at this cafe shop is delicious", "Zhè jiā kāfēiguǎn de kāfēi hěn hǎochī", "这家咖啡馆的咖啡很好吃", new String[]{"这", "家", "咖啡馆", "的", "咖啡", "很", "好吃"});
        mixedOrderSentence sentence12 = new mixedOrderSentence("Is this clothing nice-looking?", "Zhè jiàn yīfu hǎo kàn ma?", "这件衣服好看吗？", new String[]{"这", "件", "衣服", "好看", "吗", "？"});

        sentences.add(sentence1);
        sentences.add(sentence2);
        sentences.add(sentence3);
        sentences.add(sentence4);
        sentences.add(sentence5);
        sentences.add(sentence6);
        sentences.add(sentence7);
        sentences.add(sentence8);
        sentences.add(sentence9);
        sentences.add(sentence10);
        sentences.add(sentence11);
        sentences.add(sentence12);

        // Shuffle the sentences and return
        Collections.shuffle(sentences);
        return sentences;
    }

    private JPanel createWordButtonPanel(String[] wordList) {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        List<String> wordListShuffled = new ArrayList<>(List.of(wordList));
        Collections.shuffle(wordListShuffled); // Shuffle the word list

        for (String word : wordListShuffled) {
            addWordButton(word, buttonPanel);
        }

        return buttonPanel;
    }

    private void shuffleWordButtons(JPanel buttonPanel) {
        Component[] buttons = buttonPanel.getComponents();
        List<Component> buttonList = new ArrayList<>(List.of(buttons));
        buttonPanel.removeAll();

        Collections.shuffle(buttonList);

        for (Component button : buttonList) {
            buttonPanel.add(button);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void addWordButton(String word, JPanel buttonPanel) {
        JButton wordButton = new JButton(word);
        wordButton.setBackground(new Color(38, 38, 38));
        wordButton.setForeground(new Color(255, 105, 8));
        wordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the sentence when a button is clicked
                updateSentence(word);

                // Remove the clicked button
                buttonPanel.remove(wordButton);
                buttonPanel.revalidate();
                buttonPanel.repaint();

                // Check if all word buttons are gone and show pop-up messages accordingly
                if (buttonPanel.getComponentCount() == 0) {
                    checkSentence();
                }
            }
        });

        buttonPanel.add(wordButton);
    }

    private void resetGameWithNewSentence(String[] newSentence, JPanel buttonPanel) {
        sentenceWords.clear();
        sentenceLabel.setText(buildSentence());

        // Remove all buttons from the panel
        buttonPanel.removeAll();
        buttonPanel.revalidate();
        buttonPanel.repaint();

        // Create buttons with the new sentence
        for (String word : newSentence) {
            addWordButton(word, buttonPanel);
        }

        // Shuffle word buttons in the new panel
        shuffleWordButtons(buttonPanel);
    }

    private void resetGame(JPanel buttonPanel) {
        sentenceWords.clear();
        sentenceLabel.setText(buildSentence());

        // Remove all buttons from the panel
        buttonPanel.removeAll();
        buttonPanel.revalidate();
        buttonPanel.repaint();

        // Create buttons with the initial sentence
        String[] initialSentence = randomSentences.get(currentSentenceIndex).splitCharacterSentence;
        for (String word : initialSentence) {
            addWordButton(word, buttonPanel);
        }

        // Shuffle word buttons in the new panel
        shuffleWordButtons(buttonPanel);
    }

    private void checkSentence() {
        String constructedSentence = buildSentence().substring("Your sentence: ".length()).replaceAll("\\s", "");
        String correctSentence = randomSentences.get(currentSentenceIndex).characterSentence;
        System.out.println("Your answer:" + constructedSentence + "    correct answer:" + correctSentence);
        if (constructedSentence.equals(correctSentence)) {
            JOptionPane.showMessageDialog(panel, "Congratulations! You've completed the sentence.", "Success", JOptionPane.INFORMATION_MESSAGE);
            score++; // Increase the score when the sentence is correct
            scoreLabel.setText("Score: " + score);
            currentSentenceIndex = (currentSentenceIndex + 1) % randomSentences.size();
            String[] newSentence = randomSentences.get(currentSentenceIndex).splitCharacterSentence;

            // Update twoSentences label with the new sentence values
            twoSentences.setText("<html>" + randomSentences.get(currentSentenceIndex).englishSentence + "<br>" + randomSentences.get(currentSentenceIndex).pinyinSentence + "</html>");

            resetGameWithNewSentence(newSentence, buttonPanel);
            shuffleWordButtons(buttonPanel);

        } else {
            JOptionPane.showMessageDialog(panel, "Try again. Your sentence is not in the right order yet.", "Try Again", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public JPanel createPanel() {
        panel.setBackground(new Color(80, 80, 80));
        return panel;
    }

    // Inside the Game2 class
    private class Memento {
        private final List<String> state;

        public Memento(List<String> state) {
            this.state = new ArrayList<>(state);
        }

        public List<String> getState() {
            return state;
        }
    }

}
