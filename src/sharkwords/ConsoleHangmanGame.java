package sharkwords;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Hangman game. */

public class ConsoleHangmanGame {
    private final sharkwords.HangmanEngine engine;

    private ConsoleHangmanGame(sharkwords.HangmanEngine engine) {
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

        while (engine.gameState == sharkwords.GameState.Active) {
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

        System.out.println((engine.gameState == sharkwords.GameState.Won)
                ? "You win!"
                : "You suck -- it was " + engine.answer + " !");
    }

    /** Command-line interface */

    public static void main(String[] args) throws IOException {

        HangmanEngine engine;

        switch (args[0]) {
            case "evil":
                engine = new sharkwords.EvilHangmanEngine();
                break;
            case "nice":
                engine = new sharkwords.NiceHangmanEngine();
                break;
            case "nice-evil":
                engine = new sharkwords.NicelyEvilHangmanEngine();
                break;
            case "normal":
                engine = new NormalHangmanEngine();
                break;
            default:
                throw new RuntimeException("Illegal Engine");
        }

        engine.start();
        new ConsoleHangmanGame(engine).playRound();
    }
}
