package code_generation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to define test case groups for evaluation.
 * This annotation can be applied to methods or types to specify
 * a range of test cases to execute and whether the group should be active.
 *
 * <p>Typical use cases include:
 * <ul>
 *   <li>Selectively running specific test case ranges</li>
 *   <li>Temporarily disabling test groups without removing them</li>
 *   <li>Organizing test cases into logical groups for execution</li>
 * </ul>
 * @author: wuxin0011
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestCaseGroup {

    /**
     * Specifies the starting index of the test case group (inclusive).
     * Test cases will be executed from this index onwards.
     *
     * @return the starting index of the test case group, default is 1
     */
    int start() default 1;

    /**
     * Specifies the ending index of the test case group (inclusive).
     * Test cases will be executed up to this index.
     * <p>
     * The default value (0x3f3f3f = 4144959 in decimal) is chosen as
     * a sufficiently large number to cover most practical test case ranges.
     *
     * @return the ending index of the test case group, default is 0x3f3f3f
     */
    int end() default 0x3f3f3f;

    /**
     * Determines whether this test case group should be active.
     * When set to false, the test group will be skipped during execution.
     *
     * @return true if the test case group should be executed, false otherwise
     * }
     */
    boolean use() default true;
}