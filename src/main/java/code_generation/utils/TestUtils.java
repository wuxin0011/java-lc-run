package code_generation.utils;

import code_generation.bean.ListNode;
import code_generation.bean.TreeNode;
import code_generation.function.Expect;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class providing methods for testing and validation of various data types and structures.
 * This class offers a range of static methods to compare expected and actual values, validate data structures,
 * and handle conversion between primitive and wrapper types. It also includes utilities for printing
 * differences and validating conditions in test scenarios.
 */
@SuppressWarnings("all")
public class TestUtils {



    /**
     * Tests the equality of two boolean values represented as objects.
     * This method compares the actual value with the expected value and prints a success message if they match.
     * If the values do not match, it prints a failure message along with the actual and expected values.
     *
     * @param real the actual value to be tested
     * @param exp the expected value for comparison
     */
    public static void testBoolean(Object real, Object exp) {
        testBoolean(real, exp, System.currentTimeMillis() + " 测试通过！");
    }

    /**
     * Tests the equality of two boolean values represented as objects and prints the result of the comparison.
     * This method compares the actual value with the expected value. If they match, it prints the provided success message.
     * If the values do not match, it prints a failure message along with the actual and expected values, including a timestamp.
     *
     * @param real the actual value to be tested
     * @param exp the expected value for comparison
     * @param successMsg the message to be printed if the actual value matches the expected value
     */
    public static void testBoolean(Object real, Object exp, String successMsg) {
        Expect<Object, String> expect = (v1, v2, msg) -> {
            if (v1 != null && v1.equals(v2) || v1 == v2) {
                System.out.println(msg);
            } else {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 测试失败！\n" + "real = " + v1 + ", exp = " + v2 + "\n====================");
            }
        };
        expect.expect(real, exp, successMsg);
    }

    /**
     * Tests the equality of two arrays and prints the result of the comparison.
     * This method compares the elements of the two provided arrays to determine if they are equal in terms of length
     * and corresponding element values. If both arrays are null, they are considered equal. If one array is null and
     * the other is not, or if their lengths differ, the test fails. For non-null arrays, each element is compared
     * individually, with special handling for null elements. If all elements match, the test passes; otherwise, it fails.
     * The provided message is used to provide context for the test result.
     *
     * @param arr1    the first array to be tested, representing the actual result or state
     * @param arr2    the second array to be tested, representing the expected result or state
     * @param message the message associated with the test, providing context or details about the comparison
     */
    public static <T> void testArray(T[] arr1, T[] arr2, String message) {
        Expect<T[], String> expect = (v1, v2, msg) -> {
            if (v1 == v2) {
                System.out.println("测试通过");
            } else if (v1 != null && v2 != null && v2.length != v1.length) {
                System.out.println("test error");
            } else if (v1 != null && v2 != null) {
                int len = v2.length;
                boolean flag = true;
                for (int i = 0; i < len; i++) {

                    // 判断为 null 情况
                    if (v1[i] == null && v2[i] != null || v1[i] != null && v2[i] == null) {
                        flag = false;
                        break;
                    }
                    if (v1[i] != null && !v1[i].equals(v2[i])) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    System.out.println("测试失败！");
                } else {
                    System.out.println("测试ok！");
                }
            } else {
                System.out.println("test error");
            }
        };
        expect.expect(arr1, arr2, message);
    }


    /**
     * Compares two lists for equality, with an option for strict or non-strict comparison.
     * In strict mode, the method checks for equality of elements at the same indices in both lists.
     * In non-strict mode, the method compares the lists as sets, disregarding element order.
     * If the lists are identical, the method returns true; otherwise, it logs the differences
     * and returns false.
     *
     * @param b the list to be tested, representing the actual result or state
     * @param expect the list representing the expected result or state
     * @param isStrict a boolean flag indicating whether the comparison should be strict (true)
     *                 or non-strict (false)
     * @return true if the lists are considered equal based on the specified comparison mode,
     *         false otherwise
     */
    public static <T> boolean deepEqual(List<T> b, List<T> expect, boolean isStrict) {
        if (expect == b) {
            return true;
        }
        if (expect == null || b == null || expect.size() != b.size()) {
//            System.out.println("result.size() != expect.size()");
            printDiffInfo(String.valueOf(expect), String.valueOf(b));
            return false;
        }
        int n = expect.size();
        if (isStrict) {
            int idx = -1;
            for (int i = 0; i < n; i++) {
                T t1 = expect.get(i);
                T t2 = b.get(i);
                if (t1 == null || !valid(t1, t2, t1.getClass().getSimpleName(), isStrict)) {
                    idx = i;
                    break;
                }
            }
            if (idx == -1) {
                // System.out.println("ok");
                return true;
            } else {
                // System.out.println("error");
                // System.out.println("index = " + idx + ",Expect: " + expect.get(idx) + ",Result: " + CustomColor.error(b.get(idx)));
                printDiffInfo(String.valueOf(expect), String.valueOf(b));
                return false;
            }
        } else {
            Set<T> aset = new HashSet<>();
            Set<T> bset = new HashSet<>();
            for (int i = 0; i < n; i++) {
                T t1 = expect.get(i);
                T t2 = b.get(i);
                aset.add(t1);
                bset.add(t2);
            }
            return valid(aset, bset);
        }

    }

    /**
     * Compares two arrays for equality, with an option for strict or non-strict comparison.
     * In strict mode, the method checks for equality of elements at the same indices in both arrays.
     * In non-strict mode, the method compares the arrays as sets, disregarding element order.
     * If the arrays are identical based on the specified comparison mode, the method returns true;
     * otherwise, it returns false.
     *
     * @param a the first array to be compared, representing the actual result or state
     * @param b the second array to be compared, representing the expected result or state
     * @param isStrict a boolean flag indicating whether the comparison should be strict (true)
     *                 or non-strict (false)
     * @return true if the arrays are considered equal based on the specified comparison mode,
     *         false otherwise
     */
    public static <T> boolean deepEqual(T[] a, T[] b, boolean isStrict) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null || a.length != b.length) {
            //System.out.println(" result = " + Arrays.toString(a) + ",length = " + a.length);
            //System.out.println(" expect = " + Arrays.toString(b) + ",length = " + b.length);
            return false;
        }
        if (isStrict) {
            int n = a.length, x = -1;
            for (int i = 0; i < n; i++) {
                if (!valid(a[i], b[i], b[i].getClass().getSimpleName(), isStrict)) {
                    x = i;
                    break;
                }
            }
            if (x != -1) {
                // System.out.println("error:( idx = " + x + " expect result = " + b[x] + ",but result =  " + CustomColor.error(a[x]));
                return false;
            }
            return true;
        } else {
            Set<T> aset = new HashSet<>();
            Set<T> bset = new HashSet<>();
            for (int i = 0; i < a.length; i++) {
                aset.add(a[i]);
                bset.add(b[i]);
            }
            // valid
            return valid(aset, bset);
        }

    }


    /**
     * Compares two two-dimensional arrays for equality, with an option for strict or non-strict comparison.
     * In strict mode, the method checks for equality of elements at the same indices in both arrays.
     * In non-strict mode, the method compares the arrays as sets, disregarding element order.
     * If the arrays are identical based on the specified comparison mode, the method returns true;
     * otherwise, it returns false.
     *
     * @param <T> the type of elements in the arrays
     * @param a the first two-dimensional array to be compared, representing the actual result or state
     * @param b the second two-dimensional array to be compared, representing the expected result or state
     * @param isStrict a boolean flag indicating whether the comparison should be strict (true)
     *                 or non-strict (false)
     * @return true if the arrays are considered equal based on the specified comparison mode,
     *         false otherwise
     */
    public static <T> boolean deepEqual(T[][] a, T[][] b, boolean isStrict) {
        if (a == b) {
            // System.out.println("ok");
            return true;
        }
        if (a == null || b == null || a.length != b.length || a[0].length != b[0].length) {
            // System.out.println("error length not equal!" + "a.length = " + a.length + ",expect[0].length=" + a[0].length + "b.length = " + b.length + ",b[0].length=" + b[0].length);
            return false;
        }
        int m = a.length, n = a[0].length, x = -1, y = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (!valid(a[i][j], b[i][j], b[i][j].getClass().getSimpleName(), isStrict)) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        if (x == -1) {
            return true;
        } else {
            // System.out.println("error : (" + x + "," + y + " ) expect result = " + b[x][y] + ",but result =  " + CustomColor.error(a[x][y]));
            return false;
        }

    }


    /**
     * Compares two ListNode objects for deep equality.
     * This method checks if both ListNode objects are identical by comparing their values iteratively.
     * If both nodes are null, they are considered equal. If one is null and the other is not, they are not equal.
     * The method iterates through both lists, comparing the values of each node. If any mismatch is found,
     * the method returns false. Additionally, if the lengths of the two lists differ, the method logs a message
     * indicating the length mismatch and returns false.
     *
     * @param result the first ListNode to compare, representing the actual result or state
     * @param expect the second ListNode to compare, representing the expected result or state
     * @return true if both ListNode objects are deeply equal, false otherwise
     */
    public static boolean deepEqual(ListNode result, ListNode expect) {
        if (result == expect) return true;

        if (result == null || expect == null) return false;

        while (result != null && expect != null) {
            if (result.val != expect.val) {
                // System.out.println("result = " + result.val + ",expect = " + expect.val);
                return false;
            }
            result = result.next;
            expect = expect.next;
        }

        if (result != expect) {
            System.out.println("ListNode length not equal");
            return false;
        }
        return true;
    }


    /**
     * Compares two TreeNode objects for deep equality.
     * This method checks if both TreeNode objects are identical by comparing their values and structure iteratively.
     * If both nodes are null, they are considered equal. If one is null and the other is not, they are not equal.
     * The method uses a breadth-first traversal approach to compare each node's value and its children.
     * If any mismatch is found in the values or structure, the method returns false.
     *
     * @param result the first TreeNode to compare, representing the actual result or state
     * @param expect the second TreeNode to compare, representing the expected result or state
     * @return true if both TreeNode objects are deeply equal, false otherwise
     */
    public static boolean deepEqual(TreeNode result, TreeNode expect) {
        if (result == expect) return true;
        if (result == null || expect == null) return false;

        Deque<TreeNode> rq = new ArrayDeque<>();
        Deque<TreeNode> eq = new ArrayDeque<>();
        rq.add(result);
        eq.add(expect);

        while (!rq.isEmpty() && !eq.isEmpty()) {
            int s1 = rq.size();
            int s2 = eq.size();
            if (s1 != s2) {
                return false;
            }
            int size = s1;
            while (size > 0) {
                size--;
                TreeNode rNode = rq.poll();
                TreeNode eNode = eq.poll();
                if (rNode == null || eNode == null) {
                    return false;
                }
                if (rNode.val != eNode.val) {
                    return false;
                }

                if (rNode.left != null) {
                    rq.add(rNode.left);
                }
                if (rNode.right != null) {
                    rq.add(rNode.right);
                }

                if (eNode.left != null) {
                    eq.add(eNode.left);
                }
                if (eNode.right != null) {
                    eq.add(eNode.right);
                }

            }
        }
        return rq.size() == eq.size();
    }

    /**
     * Compares two 3-dimensional arrays deeply for equality based on the specified strictness mode.
     *
     * This method evaluates whether two 3D arrays are equal in terms of dimensions and their corresponding elements.
     * If the arrays are identical or meet the criteria defined by the strictness parameter, they are considered equal.
     *
     * @param a the first 3-dimensional array to compare
     * @param b the second 3-dimensional array to compare
     * @param isStrict a boolean flag indicating whether the comparison should be strict;
     *                  if true, stricter rules are applied during element validation
     * @return true if the arrays are deeply equal according to the specified strictness mode, false otherwise
     */
    public static <T> boolean deepEqual(T[][][] a, T[][][] b, boolean isStrict) {
        if (a == b) {
            // System.out.println("ok");
            return true;
        }
        if (a == null || b == null || a.length != b.length || a[0].length != b[0].length || a[0][0].length != b[0][0].length) {
            System.out.println("error length not equal");
            return false;
        }
        int m = a.length, n = a[0].length, z = a[0][0].length;
        boolean f = true;
        int x = -1, y = -1, o = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < z; k++) {
                    if (a[i][j][k] == b[i][j][k]) {
                        continue;
                    }
                    if (!valid(a[i][j][k], b[i][j][k], b[i][j][k].getClass().getSimpleName(), isStrict)) {
                        x = i;
                        y = j;
                        o = k;
                        break;
                    }
                }
            }
        }
        if (x == -1) {
            // System.out.println("ok");
            return true;
        } else {
            // System.out.println("error" + "index = (" + x + "," + y + "," + o + "),expect result = " + b[x][y][o] + ",but result = " + CustomColor.error(a[x][y][0]));
            return false;
        }


    }

    /**
     * Validates whether the provided result matches the expected value based on the specified return type and strictness mode.
     * This method delegates the validation logic to an overloaded version of the `valid` method, passing an additional
     * parameter to control whether detailed information should be printed during the validation process.
     *
     * @param result the actual result to be validated
     * @param expect the expected value against which the result is compared
     * @param returnType a string indicating the type of the values being compared, used to determine the validation strategy
     * @param isStrict a boolean flag indicating whether the comparison should be strict (true) or non-strict (false)
     * @return true if the result matches the expected value based on the specified criteria, false otherwise
     */
    public static boolean valid(Object result, Object expect, String returnType, boolean isStrict) {
        return valid(result, expect, returnType, isStrict, false);
    }

    /**
     * Validates whether the provided result matches the expected value based on the specified return type and comparison rules.
     *
     * @param result      the actual result to be validated, which can be of any type including arrays, collections, or custom objects
     * @param expect      the expected value against which the result is compared, which can be of any type including arrays, collections, or custom objects
     * @param returnType  a String representing the type of the expected and result values, used to determine the appropriate comparison logic
     * @param isStrict    a boolean flag indicating whether the comparison should be strict (e.g., considering order for collections)
     * @param isPrintInfo a boolean flag indicating whether to print detailed information about mismatches when validation fails
     * @return true if the result matches the expected value according to the specified rules, false otherwise
     */
    public static boolean valid(Object result, Object expect, String returnType, boolean isStrict, boolean isPrintInfo) {
        if (result == expect) {
            return true;
        }
        if (result == null) {
            printDiffInfo(String.valueOf(expect), "null",isPrintInfo);
            return false;
        }
        if (returnType == null) {
            System.out.println(CustomColor.error("not support return type is null"));
            return false;
        }

        boolean ok = false;

        try {
            switch (returnType) {
                case "int[]": {
                    Integer[] e = covert((int[]) expect);
                    Integer[] r = covert((int[]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.toString(e), Arrays.toString(r), isPrintInfo);
                    }
                    return ok;
                }
                case "int[][]": {
                    Integer[][] e = covert((int[][]) expect);
                    Integer[][] r = covert((int[][]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString(e), Arrays.deepToString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "int[][][]": {
                    Integer[][][] e = covert((int[][][]) expect);
                    Integer[][][] r = covert((int[][][]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString(e), Arrays.deepToString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "Long[]":
                case "long[]": {
                    Long[] e = covert((long[]) expect);
                    Long[] r = covert((long[]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.toString(e), Arrays.toString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "Long[][]":
                case "long[][]": {
                    Long[][] e = covert((long[][]) expect);
                    Long[][] r = covert((long[][]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString(e), Arrays.deepToString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "Double":
                case "double": {
                    double r = ReflectUtils.parseDouble(String.valueOf(result));
                    double e = ReflectUtils.parseDouble(String.valueOf(expect));
                    ok = r == e;
                    if (!ok) {
                        System.out.println("Expect:" + e);
                        System.out.println("Result:" + CustomColor.error(r));
                    }
                    return ok;
                }
                case "double[]": {
                    Double[] e = covert((double[]) expect);
                    Double[] r = covert((double[]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.toString(e), Arrays.toString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "double[][]": {
                    Double[][] e = covert((double[][]) expect);
                    Double[][] r = covert((double[][]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString(e), Arrays.deepToString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "float[]": {
                    Float[] e = covert((float[]) expect);
                    Float[] r = covert((float[]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.toString(e), Arrays.toString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "String[]": {
                    String[] r = (String[]) result;
                    String[] e = (String[]) expect;
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.toString(e), Arrays.toString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "String[][]":
                    ok = deepEqual((String[][]) result, (String[][]) expect, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString((String[][]) expect), Arrays.deepToString((String[][]) result),isPrintInfo);
                    }
                    return ok;
                case "String[][][]":
                    ok = deepEqual((String[][][]) result, (String[][][]) expect, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString((String[][][]) expect), Arrays.deepToString((String[][][]) result),isPrintInfo);
                    }
                    return ok;
                case "char[]": {
                    Character[] e = covert((char[]) expect);
                    Character[] r = covert((char[]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.toString(e), Arrays.toString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "char[][]": {
                    Character[][] e = covert((char[][]) expect);
                    Character[][] r = covert((char[][]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString(e), Arrays.deepToString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "char[][][]": {
                    Character[][][] e = covert((char[][][]) expect);
                    Character[][][] r = covert((char[][][]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString(e), Arrays.deepToString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "boolean[]": {
                    Boolean[] e = covert((boolean[]) expect);
                    Boolean[] r = covert((boolean[]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.toString(e), Arrays.toString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "boolean[][]": {
                    Boolean[][] e = covert((boolean[][]) expect);
                    Boolean[][] r = covert((boolean[][]) result);
                    ok = deepEqual(r, e, isStrict);
                    if (!ok) {
                        printDiffInfo(Arrays.deepToString(e), Arrays.deepToString(r),isPrintInfo);
                    }
                    return ok;
                }
                case "TreeNode": {
                    TreeNode e = (TreeNode) expect;
                    TreeNode r = (TreeNode) result;
                    ok = deepEqual(r, e);
                    return ok;
                }
                case "ListNode": {
                    ListNode e = (ListNode) expect;
                    ListNode r = (ListNode) result;
                    ok = deepEqual(r, e);
                    if (!ok) {
                        printDiffInfo(ListNode.print(e), ListNode.print(r),isPrintInfo);
                    }
                    return ok;
                }

                case "List":
                case "ArrayList":
                    return deepEqual((ArrayList<Object>) result, (ArrayList<Object>) expect, isStrict);
                default:
                    boolean t = expect != null && expect.equals(result);
                    boolean isArray = expect != null && expect.getClass().getSimpleName().contains("[]");
                    if (isArray) {
                        t = Arrays.deepEquals((Object[]) result, (Object[]) expect);
                        if (!t) {
                            // System.out.println("expect result = " + Arrays.deepToString((Object[]) expect) + ",but result = " + CustomColor.error(Arrays.deepToString((Object[]) result)));
                            printDiffInfo(Arrays.deepToString((Object[]) expect), Arrays.deepToString((Object[]) result),isPrintInfo);
                        }
                    } else {
                        if (!t) {
                            // System.out.println("expect result = " + expect + ",but result = " + CustomColor.error(result));
                            printDiffInfo(String.valueOf(expect), String.valueOf(result),isPrintInfo);
                        }
                    }
                    return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Converts an array of primitive boolean values to an array of Boolean objects.
     * Each element in the input array is wrapped into its corresponding Boolean object.
     *
     * @param a the array of primitive boolean values to be converted
     * @return an array of Boolean objects containing the same values as the input array
     */
    public static Boolean[] covert(boolean[] a) {
        Boolean[] t = new Boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = a[i];
        }
        return t;
    }

    /**
     * Converts a 2D array of primitive boolean values into a 2D array of Boolean objects.
     * Each element in the input array is wrapped as a Boolean object and placed in the corresponding position in the output array.
     *
     * @param a the input 2D array of primitive boolean values to be converted
     * @return a 2D array of Boolean objects containing the same values as the input array
     */
    public static Boolean[][] covert(boolean[][] a) {
        Boolean[][] t = new Boolean[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                t[i][j] = a[i][j];
            }
        }
        return t;
    }


    /**
     * Converts an array of primitive int values to an array of Integer objects.
     *
     * @param a the input array of primitive int values to be converted
     * @return an array of Integer objects containing the same values as the input array
     */
    public static Integer[] covert(int[] a) {
        Integer[] t = new Integer[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = a[i];
        }
        return t;
    }

    /**
     * Converts an array of primitive long values to an array of Long objects.
     *
     * @param a the input array of primitive long values to be converted
     * @return an array of Long objects corresponding to the input array elements
     */
    public static Long[] covert(long[] a) {
        Long[] t = new Long[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = a[i];
        }
        return t;
    }

    /**
     * Converts a 2D array of primitive long values to a 2D array of Long objects.
     *
     * @param a the input 2D array of primitive long values to be converted
     * @return a 2D array of Long objects containing the same values as the input array
     */
    public static Long[][] covert(long[][] a) {
        Long[][] t = new Long[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for(int j = 0;j<a[0].length;j++){
                t[i][j] = a[i][j];
            }
        }
        return t;
    }


    /**
     * Converts an array of primitive float values to an array of Float objects.
     *
     * @param a the input array of primitive float values to be converted
     * @return an array of Float objects corresponding to the input array elements
     */
    public static Float[] covert(float[] a) {
        Float[] t = new Float[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = a[i];
        }
        return t;
    }

    /**
     * Converts an array of primitive double values to an array of Double objects.
     *
     * @param a the input array of primitive double values to be converted
     * @return an array of Double objects corresponding to the input array elements
     */
    public static Double[] covert(double[] a) {
        Double[] t = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = a[i];
        }
        return t;
    }
    /**
     * Converts a 2D array of primitive double values to a 2D array of Double objects.
     * Each element in the input array is wrapped into its corresponding Double object
     * and stored in the output array.
     *
     * @param a a 2D array of primitive double values to be converted
     * @return a 2D array of Double objects containing the same values as the input array
     */
    public static Double[][] covert(double[][] a) {
        Double[][] t = new Double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for(int j = 0;j<a[0].length;j++) {
                t[i][j] = a[i][j];
            }
        }
        return t;
    }
    /**
     * Converts a primitive char array to an array of Character objects.
     * Each element in the input array is wrapped into its corresponding Character object.
     *
     * @param a the input array of primitive char values to be converted
     * @return an array of Character objects containing the same elements as the input array
     */
    public static Character[] covert(char[] a) {
        Character[] t = new Character[a.length];
        for (int i = 0; i < a.length; i++) {
            t[i] = a[i];
        }
        return t;
    }

    /**
     * Converts a 2D array of primitive integers into a 2D array of Integer objects.
     * Each element in the input array is wrapped as an Integer object in the output array.
     *
     * @param a the input 2D array of primitive integers to be converted
     * @return a 2D array of Integer objects containing the same elements as the input array
     */
    public static Integer[][] covert(int[][] a) {
        int m = a.length, n = a[0].length;
        Integer[][] t = new Integer[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                t[i][j] = a[i][j];
            }
        }
        return t;
    }

    /**
     * Converts a 2D array of primitive char to a 2D array of Character objects.
     * Each element in the input array is wrapped as a Character object in the output array.
     *
     * @param a the input 2D array of primitive char values to be converted
     * @return a 2D array of Character objects containing the same elements as the input array
     */
    public static Character[][] covert(char[][] a) {
        int m = a.length, n = a[0].length;
        Character[][] t = new Character[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                t[i][j] = a[i][j];
            }
        }
        return t;
    }

    /**
     * Converts a 3-dimensional array of primitive integers to a 3-dimensional array of Integer objects.
     *
     * @param a the input 3-dimensional array of primitive integers to be converted
     * @return a new 3-dimensional array of Integer objects containing the same values as the input array
     */
    public static Integer[][][] covert(int[][][] a) {
        int m = a.length, n = a[0].length, z = a[0][0].length;
        Integer[][][] t = new Integer[m][n][z];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < z; k++) {
                    t[i][j][k] = a[i][j][k];
                }
            }
        }
        return t;
    }


    /**
     * Converts a 3-dimensional array of primitive char to a 3-dimensional array of Character objects.
     * Each element in the input array is wrapped into its corresponding Character object.
     *
     * @param a the 3-dimensional array of primitive char to be converted
     * @return a 3-dimensional array of Character objects containing the same elements as the input array
     */
    public static Character[][][] covert(char[][][] a) {
        int m = a.length, n = a[0].length, z = a[0][0].length;
        Character[][][] t = new Character[m][n][z];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < z; k++) {
                    t[i][j][k] = a[i][j][k];
                }
            }
        }
        return t;
    }


    /**
     * Converts an array of Objects to an array of a specified type.
     * This method creates a new array of the specified class type and attempts to cast each element
     * of the input array to the specified type. If casting fails, the exception is silently ignored.
     *
     * @param a the input array of Objects to be converted
     * @param c the Class object representing the type of the new array to be created
     * @return an array of the specified type containing the elements of the input array
     */
    public static <T> T[] covert(Object[] a, Class<T> c) {
        T[] t = (T[]) Array.newInstance(c, a.length);
        try {
            for (int i = 0; i < a.length; i++) {
                t[i] = (T) a[i];
            }
        } catch (Exception e) {
            // ignore

        }
        return t;
    }


    /**
     * Checks if two sets are valid according to the defined criteria.
     * Two sets are considered valid if they are either the same instance,
     * or if they have the same size and all elements of the first set are
     * contained in the second set.
     *
     * @param aset the first set to compare
     * @param bset the second set to compare
     * @return true if the sets are valid according to the criteria, false otherwise
     */
    public static <T> boolean valid(Set<T> aset, Set<T> bset) {
        if (aset == bset) {
            return true;
        }
        if (aset.size() != bset.size()) {
            return false;
        }
        int cnt = 0;
        for (T a : aset) {
            if (bset.contains(a)) {
                cnt++;
            } else {
                return false;
            }
        }
        return cnt == aset.size();
    }

    /**
     * Prints the difference information between two strings.
     * This method is a convenience overload that calls the more detailed
     * printDiffInfo method with a default value for the verbose parameter.
     *
     * @param e the first string to compare, typically representing the expected value
     * @param r the second string to compare, typically representing the actual value
     */
    public static void printDiffInfo(String e, String r) {
        printDiffInfo(e, r, true);
    }

    /**
     * Prints the difference information between the expected and actual result strings.
     * This method compares two strings character by character, identifying differences
     * while ignoring certain characters. If differences are found, they are highlighted
     * using custom formatting. The output is printed only if the `isPrintInfo` flag is true.
     *
     * @param e the expected result string to compare against
     * @param r the actual result string to be compared
     * @param isPrintInfo a boolean flag that determines whether the comparison info should be printed
     */
    public static void printDiffInfo(String e, String r, boolean isPrintInfo) {
        if (!isPrintInfo) return;
        StringBuilder rb = new StringBuilder();
        StringBuilder eb = new StringBuilder();
        int m = r.length(), n = e.length();
        if (("[]".equals(r) && !"[]".equals(e)) || ("{}".equals(r) && !"{}".equals(e)) || ("null".equals(r) && !"null".equals(e))) {
            rb.append(CustomColor.error(r));
            eb.append(e);
        } else {
            for (int i = 0, j = 0; i < m || j < n; ++i, ++j) {

                for (; i < m && isIgnore(r.charAt(i)); i++) {
                    if (StringUtils.isIgnore(r.charAt(i))) continue;
                    rb.append(r.charAt(i));
                }
                for (; j < n && isIgnore(e.charAt(j)); j++) {
                    if (StringUtils.isIgnore(e.charAt(j))) continue;
                    eb.append(e.charAt(j));
                }

                if (i >= m && j >= n) {
                    break;
                }

                StringBuilder temp1 = new StringBuilder();
                StringBuilder temp2 = new StringBuilder();

                while (i < m && !isIgnore(r.charAt(i))) {
                    temp1.append(r.charAt(i));
                    i++;
                }

                while (j < n && !isIgnore(e.charAt(j))) {
                    temp2.append(e.charAt(j));
                    j++;
                }


                String result = temp1.toString();
                String expectResult = temp2.toString();
                boolean isEquals = (StringUtils.isEmpty(result) && StringUtils.isEmpty(expectResult)) || (!StringUtils.isEmpty(expectResult) && expectResult.equals(result));

                boolean deleteDot = false;
                boolean isDot = false;
                if (!isEquals) {
                    if (result.length() == 0) {
                        result = "\"\"";
                    } else {
                        isDot = result.charAt(result.length() - 1) == ',';
                        if (isDot) {
                            result = result.substring(0, result.length() - 1);
                        }
                    }


                }

                rb.append(isEquals ? result : CustomColor.error(result));

                if (isDot) {
                    rb.append(",");
                }

                eb.append(expectResult);

                if (i < m) {
                    rb.append(r.charAt(i));
                    rb.append("");
                }

                if (j < n) {
                    eb.append(e.charAt(j));
                    eb.append("");
                }
            }
        }

        System.out.println("Expect: " + eb.toString());
        System.out.println("Result: " + rb.toString());
    }

    /**
     * Checks if the given character should be ignored based on specific criteria.
     *
     * @param c the character to evaluate
     * @return true if the character is considered ignorable, false otherwise
     */
    public static boolean isIgnore(char c) {
        return StringUtils.isIgnore(c) || c == '[' || c == ']' || c == '{' || c == '}';
    }


}
