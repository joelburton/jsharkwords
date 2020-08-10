package sharkwords;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static choice.Choice.choice;

/** Hangman game engine for normal game. */

public class NormalHangmanEngine extends AbstractHangmanEngine {
    final private Logger logger = Logger.getLogger("NormalHangmanEngine");

    public static final int maxGuesses = 5;

    /** Start engine: read list of vocab words, choose answer, setup guesses. */

    public void start() throws IOException {
        final int MIN_WORD_LENGTH = 4;
        final int MAX_WORD_LENGTH = 10;
        final String DICT_PATH = "/10000-medium.txt";

        String lenRange = MIN_WORD_LENGTH + "," + MAX_WORD_LENGTH;
        logger.info("word-lens=" + lenRange);

        nGuessesLeft = maxGuesses;

        Pattern legal = Pattern.compile("^[a-z]{" + lenRange + "}$");

        var path = getClass().getResource(DICT_PATH).getPath();

        try (Stream<String> words = Files.lines(Paths.get(path))) {
            var vocabList = words
                    .filter(line -> legal.matcher(line).matches())
                    .collect(Collectors.toUnmodifiableList());
            vocab = new ArrayList<>(vocabList);
        }

        logger.info("vocab=" + vocab.size());

        answer = chooseAnswer();
        guessed = new TreeSet<>();
    }

    /**
     * Choose secret answer as simple random choice of vocab.
     * <p>
     * In versions of hangman like evil hangman, this should be overridden to
     * update the list of candidate words.
     *
     * @return answer
     */

    public String chooseAnswer() {
        return choice(vocab);
    }

    /**
     * Check if guess is correct: is letter in answer?
     * <p>
     * This is a trivial check in friendly hangman, but having this as a
     * separate method makes it easier to subclass this for different variants
     * that might change the answer-word in response to the guess (like the evil
     * variant does).
     *
     * @param guess Guessed letter
     * @return true if guess is correct else false
     */

    public boolean isGuessCorrect(String guess) {
        return answer.contains(guess);
    }

    /**
     * Guess a letter and updates gameState if applicable.
     *
     * @param guess letter to guess
     * @return was this a correct guess?
     */

    public boolean guess(String guess) {
        boolean correct = isGuessCorrect(guess);

        guessed.add(guess);

        if (!correct)
            nGuessesLeft -= 1;

        if (guessed.containsAll(Arrays.asList(answer.split(""))))
            gameState = GameState.Won;

        else if (nGuessesLeft == 0)
            gameState = GameState.Lost;

        return correct;
    }

    /** Return displayable guessed word (eg "a__le") */

    public String guessedWord() {
        return Arrays.stream(answer.split(""))
                .map(letter -> guessed.contains(letter) ? letter : "_")
                .collect(Collectors.joining());
    }
}
