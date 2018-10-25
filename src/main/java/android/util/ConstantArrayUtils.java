package android.util;

import java.util.HashMap;

/**
 * Utility functions for constant arrays
 *
 * @author Almos Rajnai
 */
public class ConstantArrayUtils {

    /**
     * Try to find an existing instance of the a constant array in the specified list and return it, or fall back
     * to the provided default new instance if it was not available yet.
     *
     * @param key          Unique key for the instance generated from the constant array data
     * @param instanceList List of previous instances
     * @param newInstance  A new instance as a default value if previous instance could not be found in the list, this
     *                     instance will be added to the list and returned.
     * @return previous instance with the same key or the provided new instance if could not be found.
     */
    public static <T> T findInstance(String key, HashMap<String, T> instanceList, T newInstance) {
        T result = instanceList.get(key);
        if (result == null) {
            result = newInstance;
            instanceList.put(key, result);
        }

        return result;
    }
}
