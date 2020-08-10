package sharkwords.frontends;

import sharkwords.*;
import sharkwords.AbstractHangmanEngine.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;


/** Starting panel: lets a player pick a engine. */

class EngineChooserFrame extends JFrame {

    /** Button for each engine, so player can pick one. */

    static class EngineButton extends JButton {
        EngineButton(String label,
                     Class<? extends AbstractHangmanEngine> engineClass,
                     EngineChooserFrame engineChooserFrame) {
            super(label);
            addActionListener(e -> engineChooserFrame.chooseEngine(engineClass));
        }
    }

    EngineChooserFrame() {
        JPanel engines = new JPanel();

        engines.add(new EngineButton(
                "Normal", NormalHangmanEngine.class, this));
        engines.add(new EngineButton(
                "Evil", EvilHangmanEngine.class, this));
        engines.add(new EngineButton(
                "Nice", NiceHangmanEngine.class, this));
        engines.add(new EngineButton(
                "Nicely-Evil", NicelyEvilHangmanEngine.class, this));

        setLayout(new GridLayout(2, 1));
        add(new JLabel(
                "Welcome to Sharkwords! Select your game style:",
                JLabel.CENTER));
        add(engines);

        setSize(500, 125);
    }

    void chooseEngine(Class<? extends AbstractHangmanEngine> engineClass) {
        System.out.printf("%s%n", engineClass);
        new SharkwordsGameFrame(engineClass);
    }
}

/** All letter buttons. */

class LetterButtons extends JPanel {

    /** Single letter button in game. */

    static class LetterButton extends JButton {
        LetterButton(String label, SharkwordsGameFrame frame) {
            super(label);
            setMargin(new Insets(0, 0, 0, 0));
            setPreferredSize(new Dimension(30, 30));
            addActionListener(e -> frame.guess(label));
        }
    }

    /* Keyboard listener so user can type guesses. */

    class SharkKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            frame.guess(String.valueOf(e.getKeyChar()));
        }
    }

    final SharkwordsGameFrame frame;
    final HashMap<String, LetterButton> keyMap = new HashMap<>();

    LetterButtons(SharkwordsGameFrame frame) {
        for (String btn : "abcdefghijklmnopqrstuvwxyz".split("")) {
            var button = new LetterButton(btn, frame);
            add(button);
            keyMap.put(btn, button);
        }

        this.frame = frame;
        addKeyListener(new SharkKeyListener());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }
}


/** Image for current number of guesses. */

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


/** Panel for a game: handles all in-game UI. */

class SharkwordsGameFrame extends JFrame {
    private ImagePanel image;
    private JLabel guessedWord;
    private JLabel guessesLeft;
    private AbstractHangmanEngine engine;
    private LetterButtons letters;

    SharkwordsGameFrame(Class<? extends AbstractHangmanEngine> engineClass) {
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

        JPanel guessesArea = new JPanel();
        guessedWord = new JLabel(formatGuessWord(), SwingConstants.CENTER);
        guessesLeft = new JLabel(
                "Guesses left: " + engine.nGuessesLeft,
                SwingConstants.CENTER);
        guessesArea.add(guessedWord);
        guessesArea.add(guessesLeft);
        guessesArea.setLayout(new GridLayout(2, 1));

        letters = new LetterButtons(this);

        setResizable(false);
        setLayout(new GridLayout(3, 1));
        setSize(300, 500);
        add(image);
        add(guessesArea);
        add(letters);
        setTitle("Sharkwords!");
        setVisible(true);
    }

    /** Display guessed word has spaces between each letter. */

    String formatGuessWord() {
        return String.join(" ", engine.guessedWord().split(""));
    }

    void guess(String letter) {
        if (letters.keyMap.containsKey(letter))
            letters.keyMap.get(letter).setEnabled(false);

        boolean result = engine.guess(letter);
        if (!result) {
            image.setImage(NormalHangmanEngine.maxGuesses - engine.nGuessesLeft);
            guessesLeft.setText("Guesses left: " + engine.nGuessesLeft);
        } else {
            guessedWord.setText(formatGuessWord());
        }

        if (engine.gameState == GameState.Lost) {
            guessedWord.setText(engine.answer);
            letters.setVisible(false);
        }

        if (engine.gameState == GameState.Won) {
            guessedWord.setText("Correct: " + engine.answer);
            letters.setVisible(false);
        }
    }
}


/** Start: make and show the engine chooser. */

public class Sharkwords {
    public static void main(String[] args) {
        EngineChooserFrame engineChooserFrame = new EngineChooserFrame();
        engineChooserFrame.setVisible(true);
        engineChooserFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
