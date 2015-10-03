package android_svg_code_render;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract base class for simulated Android classes
 *
 * This class tracks the name of the created instance and other
 * classes which are using the instance as dependency.
 *
 * @author Almos Rajnai
 */
public abstract class AndroidClass {

    private static final HashMap<Class, Integer> sNameCache = new HashMap<>();

    private HashSet<AndroidClass> mUsedBy = new HashSet<>();
    protected String mInstanceName;

    public String getInstanceName(AndroidClass referringClass) {
        if (referringClass != null) {
            mUsedBy.add(referringClass);
        }

        return mInstanceName;
    }

    public void setInstanceName(String instanceName) {
        mInstanceName = instanceName;
    }

    public boolean isUsed() {
        return mUsedBy.size() != 0;
    }

    public boolean removeUnusedDependencies() {
        HashSet<AndroidClass> validDependencies = new HashSet<>();
        boolean wasChange = false;
         for (AndroidClass dependency : mUsedBy) {
            if (dependency.isUsed()) {
                validDependencies.add(dependency);
            } else {
                wasChange = true;
            }
        }

        mUsedBy = validDependencies;

        return wasChange;
    }

    public void init(AndroidClass[] dependencies) {
        init(dependencies, null);
    }

    public void init(AndroidClass[] dependencies, String parameters, Object... objects) {
        setInstanceName(generateInstanceName(getClass()));

        OutputBuilder.addImport(getClass());

        String simpleClassName = getClass().getSimpleName();
        OutputBuilder.appendInit(this, this, dependencies, "%s = new %s(%s);",
                getInstanceName(null),
                simpleClassName,
                parameters != null ? String.format(parameters, objects) : "");
        OutputBuilder.append(this, this, dependencies, getResetMethod());
    }

    public static String generateInstanceName(Class clazz) {
        return generateInstanceName(clazz, false);
    }

    public static String generateInstanceName(Class clazz, boolean allCaps) {
        Integer count = sNameCache.get(clazz);
        if (count == null) {
            count = 0;
        }

        String name = String.format("%s_%d", clazz.getSimpleName(), count);
        if (allCaps) {
            name = name.toUpperCase();
        } else {
            name = name.toLowerCase();
        }

        count++;
        sNameCache.put(clazz, count);
        return name;
    }

    public String getResetMethod() {
        return String.format("%s.reset();", getInstanceName(null));
    }
}
