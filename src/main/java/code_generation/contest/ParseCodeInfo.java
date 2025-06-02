package code_generation.contest;

import code_generation.utils.StringUtils;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * A container class that holds parsed code information including class name,
 * method signatures, and constructor detection status. Used in code template processing.
 * @author: wuxin0011
 * @since 1.0
 */
public class ParseCodeInfo {

    /**
     * Special identifier used to mark constructor methods.
     */
    public final static String ConstructorClass = "__ConstructorClass__";

    /**
     * The original unprocessed code string.
     */
    private String origin;

    /**
     * The detected class name from parsed code.
     */
    private String className;

    /**
     * The detected method name from parsed code.
     * For constructor classes, this will be set to {@link #ConstructorClass}.
     */
    private String methodName;

    /**
     * The extracted method signatures (excluding class declaration).
     * Example for code:
     * <pre>
     * class Solution {
     *   public void f1(){}
     *   public void f2(){}
     * }
     * </pre>
     * Would store:
     * <pre>"public void f1(){} public void f2(){}"</pre>
     */
    private String method;

    /**
     * Flag indicating whether the parsed class is a constructor class.
     * Determined by checking against {@link #noConstructorNames}.
     */
    private boolean isConstructor;

    /**
     * Array of class names that should be treated as non-constructor classes.
     */
    private String[] noConstructorNames;

    /**
     * Constructs a new ParseCodeInfo with specified non-constructor class names.
     *
     * @param noConstructorNames varargs of class names that should not be
     *                           treated as constructor classes
     */
    public ParseCodeInfo(String... noConstructorNames) {
        this.noConstructorNames = noConstructorNames;
    }

    /**
     * Gets the original unprocessed code string.
     *
     * @return the original code string
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the original code string.
     *
     * @param origin the original code string to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Gets the detected class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the class name and automatically determines constructor status
     * by checking against the non-constructor names list.
     *
     * @param className the class name to set
     */
    public void setClassName(String className) {
        this.className = className;
        if (this.noConstructorNames != null && this.noConstructorNames.length > 0) {
            this.isConstructor = true; // Assume constructor by default
            for (String name : noConstructorNames) {
                if (!StringUtils.isEmpty(name) && name.equals(className)) {
                    this.isConstructor = false; // Found in non-constructor list
                    break;
                }
            }
        } else {
            this.isConstructor = false;
        }
    }

    /**
     * Gets the method name. For constructor classes, returns {@link #ConstructorClass}.
     *
     * @return the method name or constructor marker
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets the method name. Automatically converts to {@link #ConstructorClass}
     * if this is a constructor class.
     *
     * @param methodName the method name to set
     */
    public void setMethodName(String methodName) {
        this.methodName = isConstructor ? ConstructorClass : methodName;
    }

    /**
     * Gets the extracted method signatures.
     *
     * @return the method signatures string
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the method signatures string.
     *
     * @param method the method signatures to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Checks if this is a constructor class.
     *
     * @return true if constructor class, false otherwise
     */
    public boolean isConstructor() {
        return isConstructor;
    }

    /**
     * Manually sets the constructor status.
     *
     * @param constructor true to mark as constructor class, false otherwise
     */
    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    /**
     * Gets the array of non-constructor class names.
     *
     * @return array of non-constructor class names
     */
    public String[] getNoConstructorName() {
        return noConstructorNames;
    }

    /**
     * Sets the non-constructor class names.
     *
     * @param noConstructorNames varargs of non-constructor class names
     */
    public void setNoConstructorName(String... noConstructorNames) {
        this.noConstructorNames = noConstructorNames;
    }

    /**
     * Returns a string representation of the parsed code information,
     * excluding the original code for readability.
     *
     * @return formatted string containing class information
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", ParseCodeInfo.class.getSimpleName() + "[", "]")
                .add("className='" + className + "'")
                .add("methodName='" + methodName + "'")
                .add("method='" + method + "'")
                .add("isConstructor=" + isConstructor)
                .add("noConstructorNames='" + Arrays.toString(noConstructorNames) + "'")
                .toString();
    }
}
