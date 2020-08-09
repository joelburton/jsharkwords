package sharkwords;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Nice engine: try to winnow candidates as quickly as possible.
 */

class NiceHangmanEngine extends sharkwords.EvilHangmanEngine {
    /**
     * Check if guess is correct, but first update candidate words.
     * <p>
     * Like evil hangman's version, but uses the shortest family list --- so it
     * always zooms reduces the guesses as much as possible.
     *
     * @param guess Guessed letter
     * @return is this letter correct?
     */

    @Override
    boolean isGuessCorrect(String guess) {
        var families = constructFamilies(guess).values();

        // find the family with the shortest list of words; in the example
        // above, that would be [dog, pup, cur]
        candidateAnswers = Collections.min(
                families,
                Comparator.comparingInt(List::size));

        // choose a provisional answer from this; this is only needed in case
        // the hangman program in general wants to print the answer out for
        // users, etc
        answer = choice(candidateAnswers);
        logger.fine("answer=" + answer);

        return answer.contains(guess);
    }
}
