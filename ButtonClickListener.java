import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonClickListener implements ActionListener {

    private String gameName;

    public ButtonClickListener(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame mainFrame = MainFrame.getInstance();

        // Create an instance of the selected game panel
        GamePanel gamePanel = null;
        switch (gameName) {
            case "Match pairs game":
                gamePanel = new Game1();
                break;
            case "Put the words in the right order":
                gamePanel = new Game2();
                break;
            case "Memory game":
                gamePanel = new Game3();
                break;
        }

        if (gamePanel != null) {
            mainFrame.changeContentPane(gamePanel.createPanel());
        }
    }
}
