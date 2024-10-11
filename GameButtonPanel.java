import javax.swing.*;
import java.awt.*;

public class GameButtonPanel extends JPanel {

    public GameButtonPanel() {
        setLayout(new GridBagLayout());

        // Set background image
        setOpaque(false);

        // Create buttons
        JButton game1Button = new JButton("Match pairs game");
        JButton game2Button = new JButton("Put the words in the right order");
        JButton game3Button = new JButton("Memory game");

        game1Button.setBackground(new Color(31, 30, 30));
        game1Button.setForeground(new Color(252, 102, 9));
        game2Button.setBackground(new Color(31, 30, 30));
        game2Button.setForeground(new Color(252, 102, 9));
        game3Button.setBackground(new Color(31, 30, 30));
        game3Button.setForeground(new Color(252, 102, 9));

        game1Button.setMargin(new Insets(15,15,15,15));
        game2Button.setMargin(new Insets(15,15,15,15));
        game3Button.setMargin(new Insets(15,15,15,15));


        // Set preferred size for all buttons
        Dimension buttonSize = new Dimension(250, 50);
        game1Button.setPreferredSize(buttonSize);
        game2Button.setPreferredSize(buttonSize);
        game3Button.setPreferredSize(buttonSize);

        // Add buttons to the panel with GridBagLayout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(game1Button, gbc);

        gbc.gridy = 1;
        add(game2Button, gbc);

        gbc.gridy = 2;
        add(game3Button, gbc);

        // Add action listeners to buttons
        game1Button.addActionListener(new ButtonClickListener("Match pairs game"));
        game2Button.addActionListener(new ButtonClickListener("Put the words in the right order"));
        game3Button.addActionListener(new ButtonClickListener("Memory game"));
    }
}
