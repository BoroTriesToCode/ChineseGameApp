import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Game3 implements GamePanel {

    private Map<String, WordDecorator> wordDecoratorMap;
    private List<String> characters;
    private List<String> images;
    private JButton[] buttons;
    private JButton resetButton;
    private JButton backButton;
    private int firstClickedIndex = -1;
    private JPanel panel;

    public Game3() {
        wordDecoratorMap = new HashMap<>();
        wordDecoratorMap.put("\u732B", new ImageDecorator("img/cat.jpg"));
        wordDecoratorMap.put("\u9AD8", new ImageDecorator("img/dog.jpg"));
        wordDecoratorMap.put("\u82B1", new ImageDecorator("img/flower.jpg"));
        wordDecoratorMap.put("\u6811", new ImageDecorator("img/tree.jpg"));

        characters = new ArrayList<>(wordDecoratorMap.keySet());
        images = new ArrayList<>(wordDecoratorMap.keySet());

        buttons = new JButton[characters.size() * 2];
        resetButton = new JButton("New game");
        backButton = new JButton("Back to Main");

        panel = new JPanel(new GridLayout(2, characters.size()));
        panel.setBackground(Color.BLACK);

        Collections.shuffle(characters);
        Collections.shuffle(images);

        for (int i = 0; i < characters.size(); i++) {
            buttons[i] = new JButton("?");
            int finalI = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(finalI);
                }
            });
            panel.add(buttons[i]);

            buttons[i + characters.size()] = new JButton("?");
            int finalI2 = i;
            buttons[i + characters.size()].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(finalI2 + characters.size());
                }
            });
            panel.add(buttons[i + characters.size()]);
        }

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        backButton.addActionListener(e -> {
            MainFrame.getInstance().changeContentPane(new GameButtonPanel());
        });

        for (JButton button : buttons) {
            button.setBackground(new Color(1,1,1));
            button.setForeground(new Color(224, 108, 25));
        }

        resetButton.setBackground(new Color(224, 108, 25));
        resetButton.setForeground(Color.BLACK);

        backButton.setBackground(new Color(236, 91, 9));
        backButton.setForeground(Color.BLACK);
    }

    private void handleButtonClick(int index) {
        try {
            if (index < characters.size()) {
                buttons[index].setText(characters.get(index));
            } else {
                wordDecoratorMap.get(images.get(index - characters.size())).decorate(buttons[index]);
                buttons[index].setText(images.get(index - characters.size()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (firstClickedIndex == -1) {
            firstClickedIndex = index;
        } else {
            if (buttons[firstClickedIndex].getText().equals(buttons[index].getText())) {
                JOptionPane.showMessageDialog(null, "Match found!");
                buttons[firstClickedIndex].setEnabled(false);
                buttons[firstClickedIndex].setBackground(new Color(169, 169, 169));
                buttons[index].setEnabled(false);
                buttons[index].setBackground(new Color(169, 169, 169));
            } else {
                JOptionPane.showMessageDialog(null, "Not a match. Try again!");
                buttons[firstClickedIndex].setText("?");
                buttons[firstClickedIndex].setIcon(null);
                buttons[index].setText("?");
                buttons[index].setIcon(null);
                buttons[firstClickedIndex].setEnabled(true);
                buttons[index].setEnabled(true);
            }

            firstClickedIndex = -1;
        }
    }

    private void resetGame() {
        firstClickedIndex = -1;
        Collections.shuffle(characters);
        Collections.shuffle(images);

        for (JButton button : buttons) {
            button.setBackground(Color.BLACK);
        }

        for (int i = 0; i < characters.size() * 2; i++) {
            buttons[i].setText("?");
            buttons[i].setIcon(null);
            buttons[i].setEnabled(true);
        }
    }

    @Override
    public JPanel createPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        backButton.addActionListener(e -> {
            MainFrame.getInstance().changeContentPane(new GameButtonPanel());
        });

        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.setBackground(Color.BLACK);
        buttonPanel.setBackground(Color.BLACK);

        return mainPanel;
    }
}