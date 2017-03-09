import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class EngineButton extends JButton {
    EngineButton(String label,
                 Class<? extends HangmanEngine> engineClass,
                 EngineChooser engineChooser) {
        super(label);
        addActionListener(e -> engineChooser.chooseEngine(engineClass));
    }

}

class EngineChooser extends JFrame {
    EngineChooser() {
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

        setSize(425, 125);
    }

    void chooseEngine(Class<? extends HangmanEngine> engineClass) {
        System.out.printf("%s%n", engineClass);
        setVisible(false);
        new SharkwordsFrame(engineClass);
    }
}

class LetterButton extends JButton {
    LetterButton(String label, SharkwordsFrame frame) {
        super(label);
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(30, 30));
        addActionListener(e -> { setEnabled(false); frame.guess(label); });

    }
}

class LetterButtons extends JPanel {
    LetterButtons(SharkwordsFrame frame) {
        for (String btn : "abcdefghijklmnopqrstuvwxyz".split("")) {
            add(new LetterButton(btn, frame));
        }
    }
}


class ImagePanel extends JPanel{
    private BufferedImage image;

    ImagePanel() {
        setImage(0);
    }

    void setImage(int number) {
        try {
            image = ImageIO.read(new File("guess" + number + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }

}

class SharkwordsFrame extends JFrame {
    private ImagePanel image;
    private JLabel guessedWord;
    private JLabel guessesLeft;
    private LetterButtons letters;
    private HangmanEngine engine;

    SharkwordsFrame(Class<? extends HangmanEngine> engineClass) {
        try {
            engine = engineClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
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

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void guess(String letter) {
        boolean result = engine.guess(letter);
        if (!result) {
            image.setImage(5 - engine.nGuessesLeft);
            guessesLeft.setText("Guesses left: " + engine.nGuessesLeft);
        } else {
            guessedWord.setText(engine.guessedWord());
        }
    }
}

public class Sharkwords {
    public static void main(String[] args) {
        EngineChooser engineChooser = new EngineChooser();
        engineChooser.setVisible(true);
        engineChooser.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
