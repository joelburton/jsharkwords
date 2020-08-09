package sharkwords;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Hangman game engine for normal game. */

class NormalHangmanEngine extends sharkwords.HangmanEngine {

    void start() throws IOException {
        String lenRange = MIN_WORD_LENGTH + "," + MAX_WORD_LENGTH;
        logger.info("wordlen=" + lenRange);

        Pattern legal = Pattern.compile("^[a-z]{" + lenRange + "}$");

        try (Stream<String> words = Files.lines(Paths.get(DICT_PATH))) {
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
     * Choose secret answer.
     * <p>
     * In versions of hangman like evil hangman, this should be overridden to
     * update the list of candidate words.
     *
     * @return answer
     */

    String chooseAnswer() {
        return choice(vocab);
    }

    /**
     * Check if guess is correct.
     * <p>
     * This is a trivial check in friendly hangman, but having this as a
     * separate method makes it easier to subclass this for different variants
     * that might change the answer-word in response to the guess (like the evil
     * variant does).
     *
     * @param guess Guessed letter
     * @return true if guess is correct else false
     */

    boolean isGuessCorrect(String guess) {
        return answer.contains(guess);
    }

    /**
     * Guess a letter and updates gameState if applicable.
     *
     * @param guess letter to guess
     * @return was this a correct guess?
     */

    boolean guess(String guess) {
        boolean correct = isGuessCorrect(guess);

        guessed.add(guess);

        if (!correct)
            nGuessesLeft -= 1;

        if (guessed.containsAll(Arrays.asList(answer.split(""))))
            gameState = sharkwords.GameState.Won;

        else if (nGuessesLeft == 0)
            gameState = sharkwords.GameState.Lost;

        return correct;
    }

    /** Return displayable guessed word (eg "a__le") */

    String guessedWord() {
        return Arrays.stream(answer.split(""))
                .map(letter -> guessed.contains(letter) ? letter : "_")
                .collect(Collectors.joining());
    }
}
