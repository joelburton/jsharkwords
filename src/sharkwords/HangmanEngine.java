package sharkwords;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;


/** Current state of game, maintained by all engines. */

enum GameState {
    Won, Lost, Active
}


/** Abstract engine that all hangman engines must extend. */

abstract class HangmanEngine {
    /** The list of words in the game vocabulary. */

    ArrayList<String> vocab;

    /** The secret answer word. */

    String answer;

    /** Set of letters that have been guessed so far. */

    Set<String> guessed;

    /** Number of guesses left until game lost. */

    int nGuessesLeft;

    /** State of game: .Active, .Won, or .Lost */

    GameState gameState = GameState.Active;

    /** Start the game: reading any dictionary files, do any setup. */

    abstract void start() throws IOException;

    /** Choose the secret answer using any method the engine chooses. */

    abstract String chooseAnswer();

    /** Is the guessed-letter in the answer? */

    abstract boolean isGuessCorrect(String guess);

    /** Player makes a guess: determine if game is won/lost. */

    abstract boolean guess(String guess);

    /** Return the active-guessed partially-completed answer */

    abstract String guessedWord();


    /**
     * Choose random item from collection.
     * <p>
     * This is very much like Python's random.choice(collection) function.
     *
     * @param collection collection of T to retrieve item from
     * @return random item (type T)
     */

    static <T> T choice(Collection<T> collection) {
        int randomIndex = (int) (Math.random() * collection.size());
        for (T item : collection)
            if (--randomIndex < 0)
                return item;

        // This is thrown if an empty list is provided. It's required because
        // otherwise the compiler rejects this because it can't prove the method
        // has a guaranteed exit point.
        throw new AssertionError("Empty collection supplied.");
    }
}

