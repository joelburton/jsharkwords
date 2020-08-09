package sharkwords;

import java.util.List;
import java.util.Map;

/**
 * Nicely evil: keep their options open, but try to use their letter.
 */

class NicelyEvilHangmanEngine extends sharkwords.EvilHangmanEngine {
    @Override
    Map<String, List<String>> constructFamilies(String guess) {
        Map<String, List<String>> families = super.constructFamilies(guess);

        // remove families that don't use guessed letter if possible.

        if (families.size() > 1) {
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < answer.length(); i++)
                key.append("-");
            families.remove(key.toString());
        }

        return families;
    }
}
