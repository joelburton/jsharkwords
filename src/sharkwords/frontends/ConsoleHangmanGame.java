package sharkwords.frontends;

import sharkwords.EvilHangmanEngine;
import sharkwords.AbstractHangmanEngine;
import sharkwords.AbstractHangmanEngine.GameState;
import sharkwords.NiceHangmanEngine;
import sharkwords.NicelyEvilHangmanEngine;
import sharkwords.NormalHangmanEngine;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Hangman game. */

public class ConsoleHangmanGame {
    private final AbstractHangmanEngine engine;

    private ConsoleHangmanGame(AbstractHangmanEngine engine) {
        this.engine = engine;
    }

    /** Show game status */

    private void showStatus() {
        System.out.printf("Number guesses left: %s%n", engine.nGuessesLeft);
        System.out.printf("Guessed: %s%n", String.join("", engine.guessed));
        System.out.printf("%n%s%n", engine.guessedWord());
    }

    /** Main game round control */

    private void playRound() {
        Scanner terminalInput = new Scanner(System.in);
        Pattern lettersOnly = Pattern.compile("[a-z]");

        while (engine.gameState == GameState.Active) {
            showStatus();
            System.out.print("Guess: ");
            String s = terminalInput.nextLine();

            if (!lettersOnly.matcher(s).matches()) {
                System.out.println("duuuude... only lowercase letter!");
                continue;
            }

            if (engine.guessed.contains(s)) {
                System.out.println("You already guessed this.");
                continue;
            }

            if (engine.guess(s))
                System.out.println("Correct!");
            else
                System.out.println("Wrong!");
        }

        System.out.println((engine.gameState == GameState.Won)
                ? "You win!"
                : "You suck -- it was " + engine.answer + " !");
    }

    /** Command-line interface */

    public static void main(String[] args) throws IOException {

        AbstractHangmanEngine engine = switch (args[0]) {
            case "evil" -> new EvilHangmanEngine();
            case "nice" -> new NiceHangmanEngine();
            case "nice-evil" -> new NicelyEvilHangmanEngine();
            case "normal" -> new NormalHangmanEngine();
            default -> throw new RuntimeException("Illegal Engine");
        };

        engine.start();
        new ConsoleHangmanGame(engine).playRound();
    }
}
