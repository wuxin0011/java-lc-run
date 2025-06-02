package code_generation.utils;

import java.util.Random;

/**
 * @author: wuxin0011
 * @Description:
 */
/**
 * A utility class for generating random arrays and strings with various configurations.
 * Provides methods to create random integer arrays, character arrays, strings,
 * and string arrays with customizable length bounds and value ranges.
 * @author wuxin0011
 * @since 1.0
 */
public class RandomArrayUtils {

    // Constants for common array size limits
    /** 10^1 (10) */
    public static final int int_10_1 = (int) 1e1;
    /** 10^2 (100) */
    public static final int int_10_2 = (int) 1e2;
    /** 10^3 (1,000) */
    public static final int int_10_3 = (int) 1e3;
    /** 10^4 (10,000) */
    public static final int int_10_4 = (int) 1e4;
    /** 10^5 (100,000) */
    public static final int int_10_5 = (int) 1e5;
    /** 10^6 (1,000,000) */
    public static final int int_10_6 = (int) 1e6;
    /** 10^7 (10,000,000) */
    public static final int int_10_7 = (int) 1e7;
    /** 10^8 (100,000,000) */
    public static final int int_10_8 = (int) 1e8;
    /** 10^9 (1,000,000,000) */
    public static final int int_10_9 = (int) 1e9;

    private static final Random random = new Random();

    /**
     * Generates a random integer between minValue and maxValue (inclusive).
     *
     * @param minValue the minimum possible value
     * @param maxValue the maximum possible value
     * @return a random integer between minValue and maxValue
     */
    public static int randomValue(int minValue, int maxValue) {
        if (minValue == maxValue) {
            return minValue;
        }
        if (minValue > maxValue) {
            int t = minValue;
            minValue = maxValue;
            maxValue = t;
        }
        return random.nextInt(maxValue - minValue + 1) + minValue;
    }

    /**
     * Generates a random integer array with length up to maxArrayLength.
     *
     * @param maxArrayLength maximum length of the array
     * @param minValue minimum value for array elements
     * @param maxValue maximum value for array elements
     * @return randomly generated integer array
     */
    public static int[] randomIntArray(int maxArrayLength, int minValue, int maxValue) {
        return randomIntArray(0, maxArrayLength, minValue, maxValue);
    }

    /**
     * Generates a random integer array with configurable length and value ranges.
     *
     * @param minArrayLength minimum length of the array (default 0)
     * @param maxArrayLength maximum length of the array
     * @param minValue minimum value for array elements
     * @param maxValue maximum value for array elements
     * @return randomly generated integer array
     */
    public static int[] randomIntArray(int minArrayLength, int maxArrayLength, int minValue, int maxValue) {
        int N = randomValue(minArrayLength, maxArrayLength);
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = randomValue(minValue, maxValue);
        }
        return arr;
    }

    /**
     * Validates array bounds with default value bounds (Integer.MIN_VALUE to Integer.MAX_VALUE).
     *
     * @param maxArrayLength maximum array length to validate
     * @param min minimum value to validate
     * @param max maximum value to validate
     * @throws RuntimeException if any bounds are invalid
     */
    public static void checkBound(int maxArrayLength, int min, int max) {
        checkBound(0, maxArrayLength, min, Integer.MIN_VALUE, max, Integer.MAX_VALUE);
    }

    /**
     * Validates array bounds with configurable length bounds.
     *
     * @param minArrayLength minimum array length to validate
     * @param maxArrayLength maximum array length to validate
     * @param min minimum value to validate
     * @param max maximum value to validate
     * @throws RuntimeException if any bounds are invalid
     */
    public static void checkBound(int minArrayLength, int maxArrayLength, int min, int max) {
        checkBound(minArrayLength, maxArrayLength, min, Integer.MIN_VALUE, max, Integer.MAX_VALUE);
    }

    /**
     * Validates array bounds with fully configurable parameters.
     *
     * @param minArrayLength minimum array length
     * @param maxArrayLength maximum array length
     * @param min minimum value
     * @param minBound absolute minimum allowed value
     * @param max maximum value
     * @param maxBound absolute maximum allowed value
     * @throws RuntimeException if any bounds are invalid
     */
    public static void checkBound(int minArrayLength, int maxArrayLength, int min, int minBound, int max, int maxBound) {
        if (minArrayLength < 0) {
            throw new RuntimeException("len must > 0");
        }
        if (max < min || minArrayLength > maxArrayLength) {
            throw new RuntimeException("max > min");
        }
        if (min < minBound) {
            throw new RuntimeException("minBound = " + minBound + ", but = " + min);
        }
        if (max > maxBound) {
            throw new RuntimeException("maxBound = " + maxBound + ", but = " + max);
        }
    }

    /**
     * Generates a random char array with length up to maxArrayLength (a-z characters).
     *
     * @param maxArrayLength maximum length of the array
     * @return randomly generated char array
     */
    public static char[] randomCharArray(int maxArrayLength) {
        return randomCharArray(0, maxArrayLength, 'a', 'z');
    }

    /**
     * Generates a random char array with configurable length (a-z characters).
     *
     * @param minArrayLength minimum length of the array
     * @param maxArrayLength maximum length of the array
     * @return randomly generated char array
     */
    public static char[] randomCharArray(int minArrayLength, int maxArrayLength) {
        return randomCharArray(minArrayLength, maxArrayLength, 'a', 'z');
    }

    /**
     * Generates a random char array with length up to maxArrayLength and custom character range.
     *
     * @param maxArrayLength maximum length of the array
     * @param minValue minimum character value
     * @param maxValue maximum character value
     * @return randomly generated char array
     */
    public static char[] randomCharArray(int maxArrayLength, int minValue, int maxValue) {
        return randomCharArray(0, maxArrayLength, minValue, maxValue);
    }

    /**
     * Generates a random char array with fully configurable parameters.
     *
     * @param minArrayLength minimum length of the array
     * @param maxArrayLength maximum length of the array
     * @param minValue minimum character value
     * @param maxValue maximum character value
     * @return randomly generated char array
     */
    public static char[] randomCharArray(int minArrayLength, int maxArrayLength, int minValue, int maxValue) {
        checkBound(minArrayLength, maxArrayLength, minValue, 0, maxValue, 127);
        int N = randomValue(minArrayLength, maxArrayLength);
        char[] chars = new char[N];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (randomValue(minValue, maxValue));
        }
        return chars;
    }

    /**
     * Generates a random String with length up to maxArrayLength (a-z characters).
     *
     * @param maxArrayLength maximum length of the string
     * @return randomly generated string
     */
    public static String randomString(int maxArrayLength) {
        return randomString(0, maxArrayLength, 'a', 'z');
    }

    /**
     * Generates a random String with length up to maxArrayLength and custom character range.
     *
     * @param maxArrayLength maximum length of the string
     * @param minValue minimum character value
     * @param maxValue maximum character value
     * @return randomly generated string
     */
    public static String randomString(int maxArrayLength, int minValue, int maxValue) {
        return randomString(0, maxArrayLength, minValue, maxValue);
    }

    /**
     * Generates a random String with fully configurable parameters.
     *
     * @param minArrayLength minimum length of the string
     * @param maxArrayLength maximum length of the string
     * @param minValue minimum character value
     * @param maxValue maximum character value
     * @return randomly generated string
     */
    public static String randomString(int minArrayLength, int maxArrayLength, int minValue, int maxValue) {
        return new String(randomCharArray(minArrayLength, maxArrayLength, minValue, maxValue));
    }

    /**
     * Generates an array of random Strings with configurable lengths (a-z characters).
     *
     * @param maxArrayLength maximum number of strings in array
     * @param minStringLength minimum length of each string
     * @param maxStringLength maximum length of each string
     * @return array of randomly generated strings
     */
    public static String[] randomStringArray(int maxArrayLength, int minStringLength, int maxStringLength) {
        return randomStringArray(0, maxArrayLength, minStringLength, maxStringLength, 'a', 'z');
    }

    /**
     * Generates an array of random Strings with fully configurable parameters.
     *
     * @param minArrayLength minimum number of strings in array
     * @param maxArrayLength maximum number of strings in array
     * @param minStringLength minimum length of each string
     * @param maxStringLength maximum length of each string
     * @param minValue minimum character value
     * @param maxValue maximum character value
     * @return array of randomly generated strings
     */
    public static String[] randomStringArray(int minArrayLength, int maxArrayLength, int minStringLength, int maxStringLength, int minValue, int maxValue) {
        int N = randomValue(minArrayLength, maxArrayLength);
        String[] ss = new String[N];
        for (int i = 0; i < ss.length; i++) {
            ss[i] = randomString(minStringLength, maxStringLength, minValue, maxValue);
        }
        return ss;
    }
}