import java.util.List;
import java.util.Map;

/**
 * Micely evil: keep their options open, but try to use their letter.
 */

class NicelyEvilHangmanEngine extends EvilHangmanEngine {
    @Override
    Map<String, List<String>> constructFamilies(String guess) {
        Map<String, List<String>> families = super.constructFamilies(guess);

        // remove families that don't use guessed letter if possible.

        if (families.size() > 1) {
            String key = "";
            for (int i = 0; i < answer.length(); i++)
                key += "-";
            families.remove(key);
        }

        return families;
    }
}
