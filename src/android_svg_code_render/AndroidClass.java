package android_svg_code_render;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract base class for simulated Android classes
 * <p/>
 * This class tracks the name of the created instance and other
 * classes which are using the instance as dependency.
 *
 * @author Almos Rajnai
 */
public abstract class AndroidClass {

    private static final HashMap<Class, Integer> sNameCache = new HashMap<>();
    protected String mInstanceName;
    protected AndroidClass mParent;
    private HashSet<AndroidClass> mUsedBy = new HashSet<>();

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

    public void init() {
        init(null);
    }

    public void init(String parameters, Object... objects) {
        setInstanceName(generateInstanceName(getClass()));

        OutputBuilder.addImport(getClass());

        String simpleClassName = getClass().getSimpleName();
        OutputBuilder.appendInit(this, "%s = new %s(%s);",
                getInstanceName(null),
                simpleClassName,
                parameters != null ? String.format(parameters, objects) : "");
        OutputBuilder.append(this, getResetMethod());
    }

    public String getResetMethod() {
        return String.format("%s.reset();", getInstanceName(null));
    }

    protected void checkInheritance() {
        //This function is called when a property is set from the outside.
        //If the instance is "inherited", that means it was not initialized and represents the same
        //implementation class instance which was used in the constructor as parameter.
        //But since this instance will be changed now we need a real new instance, so we create it now.
        if (mParent != null) {
            //This instance is referring to the parent class, must be initialized from that
            init();
            OutputBuilder.append(this, "%s.set(%s);", getInstanceName(null), mParent.getInstanceName(this));
            mParent = null;
        }
    }

    protected void setParent(AndroidClass parent) {
        //Inherited instance is not initialized in the output unless it is used by through other functions
        mParent = parent;

        //Use the same name as the source instance unless something changes the fields from the outside
        mInstanceName = parent.mInstanceName;
    }
}
