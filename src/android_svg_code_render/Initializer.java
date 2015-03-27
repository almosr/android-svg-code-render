package android_svg_code_render;

import java.util.HashMap;

/**
 * Initializer for simulated Android classes
 *
 * @author Almos Rajnai
 */
public class Initializer {
    private static HashMap<Class, Integer> mNameCache = new HashMap<>();

    public static void init(AndroidClass instance) {
        init(instance, null);
    }

    public static void init(AndroidClass instance, String parameters, Object... objects) {
        instance.setInstanceName(generateInstanceName(instance.getClass()));

        OutputBuilder.addImport(instance.getClass());

        String simpleClassName = instance.getClass().getSimpleName();
        OutputBuilder.append(instance, "%s %s = new %s(%s);",
                simpleClassName,
                instance.getInstanceName(null),
                simpleClassName,
                parameters != null ? String.format(parameters, objects) : "");
    }

    public static String generateInstanceName(Class clazz) {
        Integer count = mNameCache.get(clazz);
        if (count == null) {
            count = 0;
        }

        String name = String.format("%s_%d", clazz.getSimpleName(), count).toLowerCase();
        count++;
        mNameCache.put(clazz, count);
        return name;
    }
}
