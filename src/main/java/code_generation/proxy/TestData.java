package code_generation.proxy;

import code_generation.annotation.Description;
import code_generation.annotation.TestCaseGroup;
import code_generation.utils.ReflectUtils;

import java.lang.reflect.Method;

/**
 * A container class for test-related data including problem descriptions and test case groups.
 * This class collects and processes testing metadata from method and class annotations.
 * @author wuxin0011
 * @since 1.0
 */
public class TestData {

    /**
     * The problem description information
     */
    public String info;

    /**
     * Array of test case group identifiers
     */
    public int[] testCaseGroup;

    /**
     * The method being tested
     */
    Method method;

    /**
     * The source class where the test originates
     */
    Class<?> src;

    /**
     * The original target class being tested
     */
    Class<?> origin;

    /**
     * Constructs a TestData instance using only a source class.
     * Both origin and src will be set to the provided class.
     *
     * @param src The source class for test data
     */
    public TestData(Class<?> src) {
        this(null, src, src);
    }

    /**
     * Constructs a TestData instance with source and origin classes.
     * The method will be set to null.
     *
     * @param src The source class for test data
     * @param origin The original target class being tested
     */
    public TestData(Class<?> src, Class<?> origin) {
        this(null, src, origin);
    }

    /**
     * Constructs a complete TestData instance with method, origin and source classes.
     *
     * @param method The method being tested (can be null)
     * @param origin The original target class being tested
     * @param src The source class where the test originates
     */
    public TestData(Method method, Class<?> origin, Class<?> src) {
        this.method = method;
        this.origin = origin;
        this.src = src;
        this.process();
    }

    /**
     * Processes the test data by extracting description and test case information
     * from available annotations.
     */
    public void process() {
        this.info = this.getDescInfo();
        this.testCaseGroup = this.getTestCaseInfo();
    }

    /**
     * Gets the description information from available annotations.
     * Checks method, origin class, and source class annotations in order.
     *
     * @return The description text, or empty string if no annotation found
     */
    public String getDescInfo() {
        Description declaredAnnotation = method != null ? method.getDeclaredAnnotation(Description.class) : null;
        if (declaredAnnotation != null) {
            return ReflectUtils.getDescriptionInfo(declaredAnnotation);
        }
        declaredAnnotation = origin != null ? origin.getDeclaredAnnotation(Description.class) : null;
        if (declaredAnnotation != null) {
            return ReflectUtils.getDescriptionInfo(declaredAnnotation);
        }
        declaredAnnotation = src != null ? src.getDeclaredAnnotation(Description.class) : null;
        if (declaredAnnotation != null) {
            return ReflectUtils.getDescriptionInfo(declaredAnnotation);
        }
        return "";
    }

    /**
     * Gets the test case group information from available annotations.
     * Checks method, origin class, and source class annotations in order.
     * Returns a default range [1, 0x3fffff] if no annotation is found.
     *
     * @return Array containing test case group identifiers
     */
    public int[] getTestCaseInfo() {
        TestCaseGroup declaredAnnotation = method != null ? method.getDeclaredAnnotation(TestCaseGroup.class) : null;
        if (declaredAnnotation != null) {
            return ReflectUtils.getTestCaseInfo(declaredAnnotation);
        }
        declaredAnnotation = origin != null ? origin.getDeclaredAnnotation(TestCaseGroup.class) : null;
        if (declaredAnnotation != null) {
            return ReflectUtils.getTestCaseInfo(declaredAnnotation);
        }
        declaredAnnotation = src != null ? src.getDeclaredAnnotation(TestCaseGroup.class) : null;
        if (declaredAnnotation != null) {
            return ReflectUtils.getTestCaseInfo(declaredAnnotation);
        }
        return new int[]{1, 0x3fffff};
    }
}