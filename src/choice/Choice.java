package choice;

import java.util.Collection;

public class Choice {
    /**
     * Choose random item from collection.
     * <p>
     * This is very much like Python's random.choice(collection) function.
     *
     * @param collection collection of T to retrieve item from
     * @return random item (type T)
     */

    public static <T> T choice(Collection<T> collection) {
        int randomIndex = (int) (Math.random() * collection.size());
        for (T item : collection)
            if (--randomIndex < 0)
                return item;

        // This is thrown if an empty list is provided. It's required because
        // otherwise the compiler rejects this because it can't prove the method
        // has a guaranteed exit point.
        throw new AssertionError("Empty collection supplied.");
    }
}

