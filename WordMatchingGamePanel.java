import javax.swing.*;
import java.awt.*;

public class WordMatchingGamePanel extends JPanel {
    private GameState gameState = new GameState();
    private JButton newGameButton;
    private int score = 0;
    private JPanel gamePanel;

    public WordMatchingGamePanel() {
        initializeLayout();
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());

        // Set background image
        ImageIcon backgroundImage = new ImageIcon("last_bg.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        gamePanel = new JPanel(new GridLayout(4, 4, 10, 10));
        gamePanel.setBackground(new Color(1, 1, 1));
        for (String value : gameState.getShuffledValues()) {
            WordButton button = new WordButton(value);
            button.setBackground(new Color(31, 31, 31));
            button.setForeground(new Color(224, 108, 25));
            button.addActionListener(e -> gameState.selectValue(button.getValue(), button));
            gameState.addObserver(button);
            gamePanel.add(button);
        }

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());
        newGameButton.setBackground(new Color(255, 129, 36));

        // Create a panel for button layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(newGameButton);
        buttonPanel.setBackground(new Color(1, 1, 1));

        // Center the gamePanel with space around it
        JPanel centeredPanel = new JPanel(new BorderLayout());
        centeredPanel.add(gamePanel, BorderLayout.CENTER);

        // Add components to the panel
        add(backgroundLabel, BorderLayout.CENTER);
        add(centeredPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    protected void startNewGame() {
        // Reset game state
        gameState = new GameState();

        // Remove old components
        removeAll();

        // Set background image
        ImageIcon backgroundImage = new ImageIcon("last_bg.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        // Create word buttons for shuffled values and add them to the game panel
        gamePanel = new JPanel(new GridLayout(4, 4, 10, 10));
        gamePanel.setBackground(new Color(0, 0, 0));
        for (String value : gameState.getShuffledValues()) {
            WordButton button = new WordButton(value);
            button.setBackground(new Color(31, 31, 31));
            button.setForeground(new Color(224, 108, 25));
            button.addActionListener(e -> {
                gameState.selectValue(button.getValue(), button);
                if (areAllButtonsDisabled()) {
                    score++;
                    JOptionPane.showMessageDialog(this, "Congratulations! All buttons are disabled. Score: " + score);
                    startNewGame(); // Start a new game after showing the message
                }
            });
            gameState.addObserver(button);
            gamePanel.add(button);
        }

        // Create "New Game" button
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());
        newGameButton.setBackground(new Color(255, 129, 36));

        // Create a panel for button layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));  // Adjusted layout with spacing
        buttonPanel.add(newGameButton);
        buttonPanel.setBackground(new Color(0, 0, 0));

        JPanel centeredPanel = new JPanel(new BorderLayout());
        centeredPanel.add(gamePanel, BorderLayout.CENTER);

        // Add components to the panel
        add(backgroundLabel, BorderLayout.CENTER);
        add(centeredPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
        setBackground(new Color(0, 0, 0));
        revalidate(); // Refresh the panel
    }

    private boolean areAllButtonsDisabled() {
        for (Component component : gamePanel.getComponents()) {
            if (component instanceof WordButton) {
                WordButton button = (WordButton) component;
                if (button.isEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }

}
