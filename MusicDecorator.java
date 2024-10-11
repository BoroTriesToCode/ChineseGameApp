import javax.sound.sampled.*;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;

public class MusicDecorator implements GameDecorator {

    private JPanel decoratedPanel;
    private static Clip clip;

    public void addBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            System.out.println("Music stopped");
        }
    }

    public void restartBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            stopMusic();
        } else {
            clip.setMicrosecondPosition(0); // Reset to the beginning
            clip.start();
            System.out.println("Music restarted");
        }
    }

    public void stopMusic() {
        stopBackgroundMusic();
    }

    @Override
    public JPanel decoratePanel(JPanel panel, String filePath) {
        addBackgroundMusic(filePath);
        System.out.println("Decorating with background music");
        return panel;
    }
}
