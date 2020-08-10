package sharkwords.engines;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/** Nicely evil: keep their options open, but try to use their letter. */

public class NicelyEvilEngine extends EvilEngine {
    final private Logger logger = Logger.getLogger("NicelyEvilHangmanEngine");

    @Override
    Map<String, List<String>> constructFamilies(String guess) {
        Map<String, List<String>> families = super.constructFamilies(guess);

        // remove families that don't use guessed letter if possible.
        if (families.size() > 1) {
            String guessNotUsedKey = "-".repeat(answer.length());
            logger.info("Removing " + guessNotUsedKey);
            families.remove(guessNotUsedKey);
        }

        return families;
    }
}
