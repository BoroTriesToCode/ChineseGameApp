import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

class GameState {
    private List<GameObserver> observers = new ArrayList<>();
    private HashMap<String, String> pinyinOfCharacters = new HashMap<>();
    private List<String> shuffledValues = new ArrayList<>();
    private Set<String> matchedValues = new HashSet<>();
    private String selectedValue = null;
    private WordButton selectedButton = null;

    public GameState() {
        initializeCharacterMap();
        selectAndShufflePairs();
    }

    private void initializeCharacterMap() {
        // Initialize the map
        pinyinOfCharacters.put("高", "dog");
        pinyinOfCharacters.put("好", "good");
        pinyinOfCharacters.put("高兴", "happy");
        pinyinOfCharacters.put("出租车", "taxi");
        pinyinOfCharacters.put("咖啡", "coffee");
        pinyinOfCharacters.put("电影", "movie");
        pinyinOfCharacters.put("小猫", "kitten");
        pinyinOfCharacters.put("舒服", "comfortable");
        pinyinOfCharacters.put("电梯", "elevator");
        pinyinOfCharacters.put("同事", "colleague");
        pinyinOfCharacters.put("朋友", "friend");
        pinyinOfCharacters.put("牛奶", "milk");
        pinyinOfCharacters.put("生日", "birthday");
        pinyinOfCharacters.put("熊猫", "panda");
        pinyinOfCharacters.put("音乐会", "concert");
        pinyinOfCharacters.put("生病", "sick");
        pinyinOfCharacters.put("晚上", "evening");
        pinyinOfCharacters.put("作业", "homework");
        pinyinOfCharacters.put("服务员", "waiter");
        pinyinOfCharacters.put("水果", "fruit");
        pinyinOfCharacters.put("可乐", "cola");
        pinyinOfCharacters.put("运动", "sport");
        pinyinOfCharacters.put("比赛", "competition");
        pinyinOfCharacters.put("打篮球", "basketball");
        pinyinOfCharacters.put("踢足球", "football");
    }

    private void selectAndShufflePairs() {
        // Select 4 random key-value pairs
        List<Map.Entry<String, String>> pairList = new ArrayList<>(pinyinOfCharacters.entrySet());
        Collections.shuffle(pairList);
        List<Map.Entry<String, String>> selectedPairs = pairList.subList(0, Math.min(8, pairList.size()));

        // Shuffle and store the values in a list
        for (Map.Entry<String, String> pair : selectedPairs) {
            shuffledValues.add(pair.getKey());
            shuffledValues.add(pair.getValue());
        }
        Collections.shuffle(shuffledValues);
    }

    public boolean isSelected(String value) {
        return selectedValue != null && selectedValue.equals(value);
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public List<String> getShuffledValues() {
        return shuffledValues;
    }

    public void selectValue(String value, WordButton button) {
        if (selectedValue == null) {
            // First selection
            selectedValue = value;
            selectedButton = button;
            button.setBackground(new Color(238, 223, 195));
        } else {
            // Second selection
            if (selectedValue.equals(value) && selectedButton != button) {
                // Same button clicked twice
                selectedButton = null;
                selectedValue = null;
            } else {
                // Check if the pair is a match
                if (isPairMatch(selectedValue, value)) {
                    // Correct pair
                    matchedValues.add(selectedValue);
                    matchedValues.add(value);
                    selectedButton.setBackground(new Color(155, 227, 159));
                    button.setBackground(new Color(155, 227, 159));
                    selectedButton.setEnabled(false);
                    button.setEnabled(false);
                } else {
                    // Incorrect pair
                    final WordButton finalSelectedButton = selectedButton;
                    final WordButton finalButton = button;
                    SwingUtilities.invokeLater(() -> {
                        if (finalSelectedButton != null) {
                            finalSelectedButton.setBackground(new Color(224, 123, 123));
                            finalSelectedButton.setForeground(Color.BLACK);
                        }
                        if (finalButton != null) {
                            finalButton.setBackground(new Color(224, 123, 123));
                            finalButton.setForeground(Color.BLACK);
                        }
                    });

                    WordButton firstButton = selectedButton;  // Capture the reference
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(() -> {
                                if (firstButton != null) {
                                    firstButton.setBackground(null);
                                    firstButton.setEnabled(true);
                                }
                                button.setBackground(null);
                                button.setEnabled(true);
                                notifyObservers();
                            });
                        }
                    }, 1000); // 1 second delay
                }
                selectedValue = null;
                selectedButton = null;
            }
        }
        notifyObservers();
    }

    public boolean isValueMatch(String value) {
        return matchedValues.contains(value);
    }

    private void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this);
        }
    }

    private boolean isPairMatch(String value1, String value2) {
        String pinyin1 = pinyinOfCharacters.get(value1);
        String pinyin2 = pinyinOfCharacters.get(value2);
        return pinyin1 != null && pinyin1.equals(value2) || pinyin2 != null && pinyin2.equals(value1);
    }
}
