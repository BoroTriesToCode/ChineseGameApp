import javax.swing.JPanel;

public interface GameDecorator {
    JPanel decoratePanel(JPanel panel,String filePath);
}
