package sharkwords;

import java.util.*;
import java.util.stream.Collectors;

/** Evil hangman: tries to make the game as difficult as possible. */

class EvilHangmanEngine extends sharkwords.NormalHangmanEngine {
    List<String> candidateAnswers;

    /**
     * Choose candidate list of words and a provisional "answer".
     * <p>
     * In evil hangman, the game keeps a list of candidate answers that match
     * the information provided so far to the player. This selects a secret word
     * and finds all candidate answers of the same length.
     *
     * @return provisional answer
     */

    @Override
    String chooseAnswer() {
        String answer = super.chooseAnswer();

        candidateAnswers = vocab.stream()
                .filter(w -> w.length() == answer.length())
                .collect(Collectors.toList());

        return answer;
    }

    /**
     * Make "families" of matching pattern:words-that-use-pattern
     * <p>
     * for example, if "a" was chosen:
     * <p>
     * { "a--": [ace, app],
     * "-a-": [cat],
     * "---": [dog, pup, cur]}
     */

    Map<String, List<String>> constructFamilies(String guess) {
        Map<String, List<String>> families = new HashMap<>();

        for (String word : candidateAnswers) {
            String key = Arrays.stream(word.split(""))
                    .map(c -> Objects.equals(c, guess) ? c : "-")
                    .collect(Collectors.joining());
            logger.fine("word=" + word + " key=" + key);
            families.getOrDefault(key, new ArrayList<>()).add(word);
        }
        return families;
    }

    /**
     * Check if guess is correct, but first update candidate words.
     * <p>
     * This is the heart of evil hangman. It first re-examines the candidate
     * word list in light of the new guess, preferring to refine the candidate
     * list to the largest possible list of options. It updates the candidate
     * list, then finds a random "candidate answer" in it. (Strictly speaking,
     * evil hangman doesn't really need a candidate answer, just the list of
     * other possibilities, but we pick a random answer from list to match the
     * API of hangman games that need an "answer".
     *
     * @param guess Guessed letter
     * @return is this letter correct?
     */

    @Override
    boolean isGuessCorrect(String guess) {
        Collection<List<String>> families = constructFamilies(guess).values();

        // find the family with the longest list of words; in the example above,
        // that would be [dog, pup, cur]
        candidateAnswers = Collections.max(
                families,
                Comparator.comparingInt(List::size));

        // choose a provisional answer from this; this is only needed in case
        // the hangman program in general wants to print the answer out for
        // users, etc
        answer = choice(candidateAnswers);
        logger.fine("answer=" + answer);

        return super.isGuessCorrect(guess);
    }
}
