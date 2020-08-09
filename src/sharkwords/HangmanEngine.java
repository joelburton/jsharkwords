package sharkwords;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


/** Current state of game, maintained by all engines. */

enum GameState {
    Won, Lost, Active
}


/** Abstract engine that all hangman engines must extend. */

abstract class HangmanEngine {
    static String DICT_PATH = "/usr/share/dict/words";
    static final int MAX_GUESSES = 5;
    static int MIN_WORD_LENGTH = 4;
    static int MAX_WORD_LENGTH = 10;

    Logger logger = Logger.getLogger("com.joelburton.HangmanEngine");

    ArrayList<String> vocab;
    String answer;
    Set<String> guessed;
    int nGuessesLeft = MAX_GUESSES;
    GameState gameState = GameState.Active;

    abstract void start() throws IOException;

    abstract String chooseAnswer();

    abstract boolean isGuessCorrect(String guess);

    abstract boolean guess(String guess);

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
        // otherwise the compiler rejects this because it can't prove the
        // method has a guaranteed exit point.

        throw new AssertionError();
    }
}

