import javax.swing.*;
import java.awt.*;

public class Game1 implements GamePanel {
    JPanel panel;
    @Override
    public JPanel createPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 129, 36));
        JButton backButton = new JButton("Back to Main");
        backButton.setBackground(new Color(236, 91, 9));
        backButton.setForeground(Color.BLACK);

        // Create WordMatchingGamePanel
        CustomWordMatchingGamePanel gamePanel = new CustomWordMatchingGamePanel();
        gamePanel.setSize(400, 400);

        panel.add(gamePanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            // Change content panel to Main
            MainFrame.getInstance().changeContentPane(new GameButtonPanel());
        });

        return panel;
    }
}
