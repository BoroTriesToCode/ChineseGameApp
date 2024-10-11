import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class ImageDecorator implements WordDecorator {
    private String imagePath;

    public ImageDecorator(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void decorate(JButton button) throws IOException {
        try {
            Image img = ImageIO.read(getClass().getResource(imagePath));
            int buttonHeight = 80;
            int buttonWidth = 80;
            Image resizedImage = img.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(resizedImage));
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setForeground(Color.ORANGE);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
