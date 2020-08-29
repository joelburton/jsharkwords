package sharkwords.frontends;

import sharkwords.GameState;
import sharkwords.engines.AbstractEngine;
import sharkwords.engines.Registration;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

class InvalidEngineError extends RuntimeException {
}

/**
 * Hangman game.
 */

public class ConsoleSharkwords {
    private static final Map<String, Class<? extends AbstractEngine>> engineList =
            Registration.getEngines();
    private final AbstractEngine engine;

    private ConsoleSharkwords(AbstractEngine engine) {
        this.engine = engine;
    }

    /**
     * Show game status
     */

    private void showStatus() {
        System.out.printf("Number guesses left: %s%n", engine.nGuessesLeft);
        System.out.printf("Guessed: %s%n", String.join("", engine.guessed));
        System.out.printf("%n%s%n", engine.guessedWord());
    }

    /**
     * Main game round control
     */

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

    /**
     * Prompt for engine.
     */

    private static AbstractEngine selectEngineInteractively() {
        AbstractEngine engine = null;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Choose: ");
            for (String name : engineList.keySet()) {
                System.out.print(name + " ");
            }
            System.out.print(": ");
            String name = scanner.nextLine();
            try {
                return selectEngine(name);
            } catch (InvalidEngineError err) {
                System.out.println("Invalid engine: " + name);
            }
        }
    }

    /**
     * Engine selector
     */

    private static AbstractEngine selectEngine(String name) {
        for (String key : engineList.keySet()) {
            try {
                if (name.equals(key))
                    return engineList.get(key).getConstructor().newInstance();
            } catch (ReflectiveOperationException err) {
                throw new InvalidEngineError();
            }
        }
        throw new InvalidEngineError();
    }

    /**
     * Command-line interface
     */

    public static void main(String[] args) throws IOException {
        AbstractEngine engine;
        try {
            engine = args.length > 0
                    ? selectEngine(args[0])
                    : selectEngineInteractively();
        } catch (InvalidEngineError err) {
            throw new RuntimeException("No such engine");
        }

        engine.start();
        new ConsoleSharkwords(engine).playRound();
    }
}
