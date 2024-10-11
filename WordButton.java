import javax.swing.*;
import java.awt.*;

class WordButton extends JButton implements GameObserver {
    private String value;

    public WordButton(String value) {
        super(value);
        this.value = value;
    }

    @Override
    public void update(GameState gameState) {
        // Update button appearance based on game state
        if (gameState.isValueMatch(value)) {
            // Matched value
            setBackground(new Color(155, 227, 159));
            setEnabled(false);
        } else if (gameState.isSelected(value)) {
            // Selected but not matched
            setBackground(new Color(238, 223, 195));
        } else {
            // Not matched value and not selected
            setBackground(new Color(31, 31, 31));
            setForeground(new Color(224, 108, 25));
            setEnabled(true);
        }
    }

    public String getValue() {
        return value;
    }
}
