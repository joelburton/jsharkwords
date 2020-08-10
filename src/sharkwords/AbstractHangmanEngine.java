package sharkwords;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;


/** Abstract engine that all hangman engines must extend. */

@SuppressWarnings("unused")
public abstract class AbstractHangmanEngine {

    /** Current state of game, maintained by all engines. */

    public enum GameState {
        Won, Lost, Active
    }


    /**
     * The list of words in the game vocabulary.
     */

    protected ArrayList<String> vocab;

    /**
     * The secret answer word.
     */

    public String answer;

    /**
     * Set of letters that have been guessed so far.
     */

    public Set<String> guessed;

    /**
     * Number of guesses left until game lost.
     */

    public int nGuessesLeft;

    /**
     * State of game: .Active, .Won, or .Lost
     */

    public GameState gameState = GameState.Active;

    /**
     * Start the game: reading any dictionary files, do any setup.
     */

    public abstract void start() throws IOException;

    /**
     * Choose the secret answer using any method the engine chooses.
     */

    public abstract String chooseAnswer();

    /**
     * Is the guessed-letter in the answer?
     */

    public abstract boolean isGuessCorrect(String guess);

    /**
     * Player makes a guess: determine if game is won/lost.
     */

    public abstract boolean guess(String guess);

    /**
     * Return the active-guessed partially-completed answer
     */

    public abstract String guessedWord();
}