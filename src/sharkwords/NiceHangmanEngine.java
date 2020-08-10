package sharkwords;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Nice engine: try to winnow candidates as quickly as possible. */

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

        // find family with shortest word list; ex = [dog, pup, cur]
        candidateAnswers = Collections.min(
                families,
                Comparator.comparingInt(List::size));

        // choose provisional answer from this; only needed in case hangman
        // wants to print the answer out for users, etc
        answer = choice(candidateAnswers);
        logger.fine("answer=" + answer);

        return answer.contains(guess);
    }
}
