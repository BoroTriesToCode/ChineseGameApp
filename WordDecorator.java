import javax.swing.JButton;
import java.io.IOException;

interface WordDecorator {
    void decorate(JButton button) throws IOException;
}
