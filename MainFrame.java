import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    // Singleton instance
    private static MainFrame instance;

    // Add button for decorators
    JButton musicDecoratorButton;
    JPanel centerPanel;
    private boolean musicAdded = false;
    private MainFrame() {
        // Frame properties
        setTitle("Chinese Games");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 635);
        setLocationRelativeTo(null);
        setResizable(false);
        JMenuBar menuBar=new JMenuBar();

        musicDecoratorButton = new JButton("Music ON/OFF");
        musicDecoratorButton.setBackground(new Color(255, 98, 0));
        musicDecoratorButton.setForeground(Color.BLACK);

        musicDecoratorButton.addActionListener(e -> toggleMusicDecorator());
        menuBar.add(musicDecoratorButton);
        setJMenuBar(menuBar);

        // Load background image
        ImageIcon backgroundImage = loadImageIcon("img/last_bg.jpg");

        if (backgroundImage != null) {
            // Create background label
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

            // Create button panel
            GameButtonPanel buttonPanel = new GameButtonPanel();
            buttonPanel.setBorder(new EmptyBorder(100, 0, 0, 0));

            // Create center panel with FlowLayout
            centerPanel = createCenterPanel(buttonPanel);

            // Set up layered pane
            JLayeredPane layeredPane = createLayeredPane(backgroundLabel, centerPanel);

            // Set content pane
            setContentPane(layeredPane);
        } else {
            System.err.println("Error loading background image.");
            // Fallback to a plain content pane with ButtonPanel
            setContentPane(new GameButtonPanel());
        }

        // Show the frame
        setVisible(true);
    }

    // Load image icon utility
    private ImageIcon loadImageIcon(String imagePath) {
        try {
            return new ImageIcon(getClass().getResource(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Singleton instance retrieval
    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    // Change content pane method
    public void changeContentPane(JPanel panel) {
        // Remove old components
        getContentPane().removeAll();

        // Load background image
        ImageIcon backgroundImage = loadImageIcon("img/last_bg.jpg");

        if (backgroundImage != null) {
            // Create background label
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

            // Create center panel with BorderLayout
            centerPanel = createCenterPanelWithBorderLayout(panel);

            // Set up layered pane
            JLayeredPane layeredPane = createLayeredPane(backgroundLabel, centerPanel);

            // Set content pane
            setContentPane(layeredPane);
        } else {
            System.err.println("Error loading background image.");
            setContentPane(panel);
        }

        revalidate();
        repaint();
    }

    private JPanel createCenterPanel(JPanel buttonPanel) {
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title=new JLabel("Let's learn Chinese!");
        title.setFont(new Font("CALIBRI", Font.BOLD, 50));
        title.setForeground(new Color(255, 90, 18));
        centerPanel.setOpaque(false);
        centerPanel.add(title);
        centerPanel.add(buttonPanel);
        centerPanel.setBounds(200, 100, 500, 400);
        return centerPanel;
    }

    private JLayeredPane createLayeredPane(JLabel backgroundLabel, JPanel centerPanel) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(900, 600));
        backgroundLabel.setBounds(0, 0, backgroundLabel.getIcon().getIconWidth(), backgroundLabel.getIcon().getIconHeight());
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        // Add the button panel with a higher layer
        layeredPane.add(centerPanel, Integer.valueOf(1));
        return layeredPane;
    }

    private JPanel createCenterPanelWithBorderLayout(JPanel panel) {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.setBounds(200, 100, 500, 400);
        return centerPanel;
    }

    private void toggleMusicDecorator() {
        MusicDecorator musicDecorator = new MusicDecorator();
        if (centerPanel != null && !musicAdded) {
            centerPanel = musicDecorator.decoratePanel(centerPanel, "C:/Users/Boro/Downloads/chinese_bgmusic.wav");
            musicAdded = true;
            updateLayeredPane();
        } else {
            if (musicAdded) {
                musicDecorator.restartBackgroundMusic();
            }
        }
    }

    // Update the layered pane with the current center panel
    private void updateLayeredPane() {
        getContentPane().removeAll();

        // Load background image
        ImageIcon backgroundImage = loadImageIcon("img/last_bg.jpg");

        if (backgroundImage != null) {
            // Create background label
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

            // Set up layered pane
            JLayeredPane layeredPane = createLayeredPane(backgroundLabel, centerPanel);

            // Set content pane
            setContentPane(layeredPane);
        } else {
            System.err.println("Error loading background image.");
            setContentPane(centerPanel);
        }

        revalidate();
        repaint();
    }
}
