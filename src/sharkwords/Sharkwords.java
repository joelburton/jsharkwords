package sharkwords;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * Button for each engine, so player can pick one.
 */

class EngineButton extends JButton {
    EngineButton(String label,
                 Class<? extends sharkwords.HangmanEngine> engineClass,
                 EngineChooserFrame engineChooserFrame) {
        super(label);
        addActionListener(e -> engineChooserFrame.chooseEngine(engineClass));
    }

}


/**
 * Starting panel: lets a player pick a engine.
 */

class EngineChooserFrame extends JFrame {
    EngineChooserFrame() {
        JPanel engines = new JPanel();

        engines.add(new EngineButton(
                "Normal", sharkwords.NormalHangmanEngine.class, this));
        engines.add(new EngineButton(
                "Evil", sharkwords.EvilHangmanEngine.class, this));
        engines.add(new EngineButton(
                "Nice", sharkwords.NiceHangmanEngine.class, this));
        engines.add(new EngineButton(
                "Nicely-Evil", sharkwords.NicelyEvilHangmanEngine.class, this));

        setLayout(new GridLayout(2, 1));
        add(new JLabel(
                "Welcome to Sharkwords! Select your game style:",
                JLabel.CENTER));
        add(engines);

        setSize(425, 125);
    }

    void chooseEngine(Class<? extends sharkwords.HangmanEngine> engineClass) {
        System.out.printf("%s%n", engineClass);
        new SharkwordsGameFrame(engineClass);
    }
}


/**
 * Single letter button in game.
 */

class LetterButton extends JButton {
    LetterButton(String label, SharkwordsGameFrame frame) {
        super(label);
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(30, 30));
        addActionListener(e -> {
            setEnabled(false);
            frame.guess(label);
        });

    }
}


/**
 * All letter buttons.
 */

class LetterButtons extends JPanel {
    LetterButtons(SharkwordsGameFrame frame) {
        for (String btn : "abcdefghijklmnopqrstuvwxyz".split("")) {
            add(new LetterButton(btn, frame));
        }
    }
}


/**
 * Image for current number of guesses.
 */

class ImagePanel extends JPanel {
    private BufferedImage image;

    ImagePanel() {
        setImage(0);
    }

    void setImage(int number) {
        String path = "/guess" + number + ".png";
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}


/**
 * Panel for a game: handles all in-game UI.
 */

class SharkwordsGameFrame extends JFrame {
    private ImagePanel image;
    private JLabel guessedWord;
    private JLabel guessesLeft;
    private sharkwords.HangmanEngine engine;
    private LetterButtons letters;

    SharkwordsGameFrame(Class<? extends sharkwords.HangmanEngine> engineClass) {
        try {
            engine = engineClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return;
        }

        try {
            engine.start();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        image = new ImagePanel();
        guessedWord = new JLabel(engine.guessedWord(), SwingConstants.CENTER);
        guessesLeft = new JLabel("Guesses left", SwingConstants.CENTER);
        letters = new LetterButtons(this);

        setLayout(new GridLayout(4, 1));
        setSize(300, 500);

        add(image);
        add(guessedWord);
        add(guessesLeft);
        add(letters);

        setVisible(true);
    }

    void guess(String letter) {
        boolean result = engine.guess(letter);
        if (!result) {
            image.setImage(HangmanEngine.MAX_GUESSES - engine.nGuessesLeft);
            guessesLeft.setText("Guesses left: " + engine.nGuessesLeft);
        } else {
            guessedWord.setText(engine.guessedWord());
        }

        if (engine.nGuessesLeft == 0) {
            guessedWord.setText(engine.answer);
            letters.setVisible(false);
        }
    }
}


/**
 * Start: make and show the engine chooser.
 */

public class Sharkwords {
    public static void main(String[] args) {
        EngineChooserFrame engineChooserFrame = new EngineChooserFrame();
        engineChooserFrame.setVisible(true);
        engineChooserFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
