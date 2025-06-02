package code_generation.utils;

import code_generation.annotation.Description;
import code_generation.annotation.TestCaseGroup;
import code_generation.bean.ListNode;
import code_generation.bean.TreeNode;
import code_generation.enums.Type;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Utility class providing various reflection-based operations and helper methods for handling
 * class information, method details, type parsing, and input/output transformations.
 * This class is designed to assist in scenarios requiring dynamic interaction with classes,
 * methods, and their metadata.
 *
 * The utility includes methods for extracting class and method descriptions, parsing arguments,
 * converting strings into typed arrays or lists, and handling special cases such as constructor
 * inputs and outputs. Additionally, it provides methods for validating empty strings or arrays,
 * error handling, and processing test case annotations.
 *
 * The methods are generic where applicable, allowing them to operate on a wide range of types.
 * Private helper methods support the public API by performing low-level operations such as
 * creating specialized data structures or parsing complex inputs.
 *
 * This class is not intended to be instantiated directly and serves as a static utility.
 */
public class ReflectUtils {


    /**
     * A constant representing an empty string.
     * This immutable value is used throughout the class to denote the absence of a string value
     * or to initialize variables that will later be assigned actual string content.
     * It serves as a standardized representation of an empty string, ensuring consistency
     * and avoiding unnecessary object creation.
     */
    private static final String EMPTY_STR = "";



    /**
     * Retrieves the description information of a class based on the provided class object.
     *
     * @param c the class object for which the description information is to be retrieved
     * @return a string containing the formatted description information of the class,
     *         or an empty string if the input class object is null or no description annotation is found
     */
    public static String getClassInfo(Class<?> c) {
        if (c == null) {
            return EMPTY_STR;
        }
        return getDescriptionInfo(c.getDeclaredAnnotation(Description.class));
    }

    /**
     * Retrieves the description information of a method based on the provided class and method name.
     *
     * @param c the class object that contains the method for which the description information is to be retrieved
     * @param methodName the name of the method for which the description information is to be retrieved
     * @return a string containing the formatted description information of the method,
     *         or an empty string if the method does not exist or no description annotation is found
     */
    public static String getMethodInfo(Class<?> c, String methodName) {
        try {
            return getMethodInfo(c.getDeclaredMethod(methodName, c));
        } catch (NoSuchMethodException e) {
            return EMPTY_STR;
        }
    }


    /**
     * Retrieves the description information of a method based on the provided method object.
     *
     * @param method the method object for which the description information is to be retrieved
     * @return a string containing the formatted description information of the method,
     *         or an empty string if the input method object is null or no description annotation is found
     */
    public static String getMethodInfo(Method method) {
        if (method == null) {
            return EMPTY_STR;
        }
        return getDescriptionInfo(method.getDeclaredAnnotation(Description.class));
    }

    /**
     * Retrieves the formatted description information based on the provided Description annotation.
     *
     * @param description the Description annotation object containing details about the problem
     * @return a formatted string containing the problem's information such as description, tags, difficulty, URL, types, and reference links;
     *         returns an empty string if the input Description object is null or lacks relevant data
     */
    public static String getDescriptionInfo(Description description) {
        if (description == null) {
            return EMPTY_STR;
        }
        // tag
        String tag = description.tag() != null ? description.tag().getTag() : EMPTY_STR;
        tag = isEmpty(description.customTag()) ? tag : description.customTag();
        // diff
        String difficulty = description.diff() != null ? description.diff().getDesc() : EMPTY_STR;
        // url
        String url = description.url();
        // desc
        String desc = description.value();
        // types
        String types = getTypes(description);
        // views
        String views = getViews(description);
        return CustomColor.success("======================================题目信息=======================" +
                (!isEmpty(desc) ? "\n简介: " + desc : "") +
                (!isEmpty(tag) ? "\n标签: " + tag : "") +
                (!isEmpty(types) ? "\n类型: " + types : "") +
                (!isEmpty(difficulty) ? "\n难度: " + difficulty : "") +
                (!isEmpty(url) ? "\n题目地址: " + url : "") +
                (!isEmpty(views) ? "\n参考链接: " + views : "") +
                "\n======================================输出结果=========================");
    }


    /**
     * Retrieves and concatenates the types associated with a given Description annotation.
     * This includes both predefined types and custom types, filtering out any empty or null values.
     *
     * @param description the Description annotation object containing type information
     * @return a string representing the concatenated types from the Description annotation,
     *         or an empty string if no valid types are found or the input is null
     */
    public static String getTypes(Description description) {
        Type[] types = description.types();
        String[] customTypes = description.customType();
        StringBuilder typeBuilder = new StringBuilder();
        if (types != null) {
            for (Type type : types) {
                if (!isEmpty(type.getType())) {
                    typeBuilder.append(type);
                }
            }
        }
        if (customTypes != null) {
            for (String type : customTypes) {
                if (!isEmpty(type)) {
                    typeBuilder.append(type);
                }
            }
        }
        return typeBuilder.toString();
    }


    /**
     * Retrieves and formats the reference links associated with a given Description annotation.
     * Filters out any empty or null values from the views array and constructs a formatted string.
     *
     * @param description the Description annotation object containing the reference links
     * @return a formatted string representing the concatenated reference links with their respective indices,
     *         or an empty string if the input is null, the views array is empty, or no valid links are found
     */
    private static String getViews(Description description) {
        if (description == null) {
            return EMPTY_STR;
        }
        StringBuilder builder = new StringBuilder();
        String[] views = description.views();
        if (isEmpty(views)) {
            return EMPTY_STR;
        }
        int len = views.length;
        for (int i = 0; i < len; i++) {
            if (!isEmpty(views[i])) {
                builder.append((int) (i + 1));
                builder.append(" 、");
                builder.append(views[i]);
                if (i != len - 1) {
                    builder.append(" , ");
                }
            }

        }

        return "null".contentEquals(builder) || EMPTY_STR.contentEquals(builder) ? EMPTY_STR : builder.toString();
    }


    /**
     * Checks if the provided string is empty or null.
     *
     * @param s the string to check for emptiness or nullity
     * @return true if the string is null or has a length of zero, false otherwise
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * Checks if the provided string array is empty or null.
     *
     * @param s the string array to check for emptiness or nullity
     * @return true if the string array is null or has a length of zero, false otherwise
     */
    public static boolean isEmpty(String[] s) {
        return s == null || s.length == 0;
    }


    /**
     * Parses an argument based on the provided parameters and returns the parsed object.
     *
     * @param origin the class object from which the method is invoked
     * @param methodName the name of the method for which the argument is being parsed
     * @param src the class object representing the type of the argument to be parsed
     * @param input the string representation of the input to be parsed
     * @param idx the index of the argument in the method's parameter list
     * @param argsSize the total number of arguments in the method's parameter list
     * @return the parsed object corresponding to the input string and argument type
     */
    public static Object parseArg(Class<?> origin, String methodName, Class<?> src, String input, int idx, int argsSize) {
        return parseArg(origin, methodName, src.getSimpleName(), input, idx, argsSize);
    }

    /**
     * Parses the given input string into an object of the specified type.
     * The method supports a wide range of types, including primitive types, arrays, and custom objects.
     * If the input is null, empty, or cannot be parsed into the specified type, the method returns null.
     *
     * @param src       the class associated with the method invocation context
     * @param methodName the name of the method being invoked
     * @param type      the target type to which the input should be parsed (e.g., "int", "String[]", "TreeNode")
     * @param input     the input string to be parsed
     * @param idx       the index of the argument in the method signature
     * @param argsSize  the total number of arguments in the method signature
     * @return the parsed object corresponding to the specified type, or null if parsing fails
     */
    public static Object parseArg(Class<?> src, String methodName, String type, String input, int idx, int argsSize) {
        if (input == null || "".equals(input) || input.length() == 0) {
            System.out.println("read content is null");
            return null;
        }
        if ("void".equals(type)) {
            System.out.println("void type not support place check type !");
            return null;
        }
        input = toString(input);
        try {
            switch (type) {
                case "int":
                case "Integer":
                    return Integer.parseInt(input);
                case "long":
                case "Long":
                    return Long.parseLong(input);
                case "boolean":
                case "Boolean":
                    return input.contains("t");
                case "boolean[]":
                case "Boolean[]":
                    return oneBooleanArray(input);
                case "boolean[][]":
                case "Boolean[][]":
                    return doubleBooleanArray(input);
                case "double":
                    return parseDouble(input);
                case "double[]":
                    return oneDoubleArray(input);
                case "double[][]":
                    return doubleDoubleArray(input);
                case "long[]":
                case "Long[]":
                    return oneLongArray(input);
                case "long[][]":
                case "Long[][]":
                    return doubleLongArray(input);
                case "float":
                    return Float.parseFloat(input);
                case "int[]":
                case "Integer[]":
                    return oneIntArray(input);
                case "int[][]":
                case "Integer[][]":
                    return doubleIntArray(input);
                case "int[][][]":
                case "Integer[][][]":
                    return threeIntArray(input);
                case "char":
                case "Character":
                    return input.toCharArray()[0];
                case "char[]":
                case "Character[]":
                    return oneCharArray(input);
                case "char[][]":
                case "Character[][]":
                    return doubleCharArray(input);
                case "char[][][]":
                case "Character[][][]":
                    return threeCharArray(input);
                case "string":
                case "String":
                    return toString(input);
                case "string[]":
                case "String[]":
                    return oneStringArray(input);
                case "string[][]":
                case "String[][]":
                    return doubleStringArray(input);
                case "string[][][]":
                case "String[][][]":
                    return threeStringArray(input);
                case "TreeNode":
                    return TreeNode.widthBuildTreeNode(oneStringArray(input));
                case "TreeNode[]":
                    return createListTreeNodeArray(input);
                case "ListNode":
                    return ListNode.createListNode(oneIntArray(input));
                case "ListNode[]":
                    return createListNodeArray(input);
                case "List":
                case "ArrayList":
                    return toList(src, methodName, type, input, idx, argsSize);
                default:
                    System.out.println(type + " not implement ,place implement!");
                    return null;
            }
        } catch (NumberFormatException e) {
            // e.printStackTrace();
            errorInfo(type);
            return null;
        }

    }

    /**
     * Creates an array of TreeNode objects from the given input string.
     * The input string is parsed into a list of lists of strings, and each inner list is used to construct a TreeNode.
     * If an exception occurs during the creation of a TreeNode, the corresponding array element is set to null.
     *
     * @param input the input string to be parsed and converted into an array of TreeNode objects
     * @return an array of TreeNode objects constructed from the input string,
     *         where each element corresponds to a TreeNode created from an inner list of strings,
     *         or null if an exception occurred during creation
     */
    private static TreeNode[] createListTreeNodeArray(String input) {
        List<List<String>> lists = parseDoubleString(input);
        TreeNode[] nodes = new TreeNode[lists.size()];
        for (int i = 0; i < lists.size(); i++) {
            try {
                int size = lists.get(i).size();
                String[] ss = new String[size];
                for (int k = 0; k < size; k++) {
                    ss[k] = lists.get(i).get(i);
                }
                nodes[i] = TreeNode.widthBuildTreeNode(ss);
            } catch (Exception e) {
                nodes[i] = null;
            }
        }
        return nodes;
    }


    /**
     * Creates an array of ListNode objects from a string representation of nested integer lists.
     * Each inner list in the input is converted into a ListNode chain, and the resulting chains
     * are stored in an array. If any error occurs during conversion, the corresponding array
     * entry is set to null.
     *
     * @param input a string representation of a list containing nested integer lists, where each
     *              inner list represents the values for a ListNode chain
     * @return an array of ListNode objects, where each element corresponds to a ListNode chain
     *         created from an inner list in the input; null values are placed in the array if
     *         an error occurs during conversion
     */
    public static ListNode[] createListNodeArray(String input) {
        List<List<Integer>> ls = parseListDoubleInteger(input);
        int n = ls.size();
        ListNode[] nodes = new ListNode[n];
        for (int i = 0; i < n; i++) {
            try {
                if (ls.get(i) == null) continue;
                int[] array = ls.get(i).stream().mapToInt(Integer::intValue).toArray();
                nodes[i] = ListNode.createListNode(array);
            } catch (Exception e) {
                nodes[i] = null;
            }
        }
        return nodes;
    }

    /**
     * Converts a string input into a two-dimensional array of longs.
     * The method parses the input string to generate a list of lists containing Long values,
     * which is then transformed into a 2D array of primitive longs.
     *
     * @param input the string input to be parsed into a 2D array of longs
     * @return a two-dimensional array of primitive longs derived from the parsed input
     */
    private static long[][] doubleLongArray(String input) {
        List<List<Long>> longList = parseDoubleLongList(input);
        long[][] longs = new long[longList.size()][];
        for (int i = 0; i < longList.size(); i++) {
            longs[i] = new long[longList.get(i).size()];
            for (int j = 0; j < longList.get(i).size(); j++) {
                longs[i][j] = longList.get(i).get(j);
            }
        }
        return longs;
    }

    /**
     * Parses a string representation into a list of lists containing Long values.
     * The input string is expected to be structured in a way that can be processed by the parseDoubleString method.
     * Each inner list contains numeric strings that are converted to Long values.
     *
     * @param input the string to be parsed into a list of lists of Long values
     * @return a list of lists containing Long values parsed from the input string
     */
    private static List<List<Long>> parseDoubleLongList(String input) {
        List<List<String>> list = parseDoubleString(input);
        List<List<Long>> longs = new ArrayList<>();
        for (List<String> one : list) {
            List<Long> temp = new ArrayList<>();
            for (String s : one) {
                temp.add(Long.parseLong(s));
            }
            longs.add(temp);
        }
        return longs;
    }

    /**
     * Converts a string representation of a nested list of doubles into a 2D array of doubles.
     * The input string is parsed into a list of lists of doubles, which is then transformed into a 2D array.
     *
     * @param input the string representation of a nested list of doubles to be converted
     * @return a 2D array of doubles parsed from the input string
     */
    private static double[][] doubleDoubleArray(String input) {
        List<List<Double>> doubleDoubleList = parseDoubleDoubleList(input);
        double[][] doubles = new double[doubleDoubleList.size()][];
        for(int i = 0;i<doubleDoubleList.size();i++) {
            doubles[i] = new double[doubleDoubleList.get(i).size()];
            for (int j = 0; j < doubleDoubleList.get(i).size(); j++) {
                doubles[i][j] = doubleDoubleList.get(i).get(j);
            }
        }
        return doubles;
    }


    /**
     * Converts a string input into a single long array by parsing the input into a list of Long values.
     * Each Long value from the list is then transferred to a primitive long array.
     *
     * @param input the string representation of a list of long values to be parsed and converted
     * @return an Object which is actually a primitive long array containing the parsed long values
     */
    private static Object oneLongArray(String input) {
        List<Long> ls = parseListLong(input);
        long[] res = new long[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            res[i] = ls.get(i);
        }
        return res;
    }

    /**
     * Parses a given input string into a list of Long values.
     * The method first retrieves a list of strings by calling parseListString,
     * then attempts to convert each string in the list to a Long.
     * Strings that cannot be parsed into a Long are ignored.
     *
     * @param input the input string to be parsed into a list of Long values
     * @return a list of Long values successfully parsed from the input string
     */
    private static List<Long> parseListLong(String input) {
        List<String> strings = parseListString(input);
        List<Long> res = new ArrayList<>();
        for (String s : strings) {
            try {
                res.add(Long.parseLong(s));
            } catch (Exception e) {
            }
        }
        return res;
    }


    /**
     * Converts the given input string into a list of the specified type based on the method's parameters.
     * This method determines the appropriate list type by analyzing the return type of the specified method
     * in the provided class. It supports various generic list types and performs parsing accordingly.
     *
     * @param t          the Class object representing the class containing the method to analyze
     * @param methodName the name of the method whose return type is used to determine the list type
     * @param type       the type of the parameter being processed (used for method resolution)
     * @param input      the input string to be parsed into a list of the determined type
     * @param idx        the index of the parameter being processed (used for method resolution)
     * @param argsSize   the total number of arguments in the method signature (used for method resolution)
     * @return an Object representing the parsed list of the determined type, or a default string list
     *         if the type is not explicitly supported
     */
    public static Object toList(Class<?> t, String methodName, String type, String input, int idx, int argsSize) {
        String listType = IoUtil.findListReturnTypeMethod(t, methodName, type, idx, argsSize);
        String originType = listType;
        if (listType.contains("ArrayList")) {
            listType = listType.replace("ArrayList", "List");
        }
        switch (listType) {
            case "List<TreeNode>":
                return parseListTreeNode(input); // 4.2日 新增
            case "List<String>":
                return parseListString(input);
            case "List<List<String>>":
                return parseDoubleString(input);
            case "List<List<List<String>>>":
                return parseThreeString(input);
            case "List<Integer>":
                return parseListInteger(input);
            case "List<Long>":
                return parseListLong(input);
            case "List<Boolean>":
                return parseListBoolean(input);
            case "List<List<Boolean>>":
                return parseDoubleBoolean(input);
            case "List<Double>":
                return parseDoubleList(input);
            case "List<List<Double>>":
                return parseDoubleDoubleList(input);
            case "List<List<Integer>>":
                return parseListDoubleInteger(input);
            case "List<List<List<Integer>>>":
                return parseListThreeInteger(input);
            case "List<Character>":
                return parseListChar(input);
            case "List<List<Character>>":
                return parseListDoubleChar(input);
            case "List<List<List<Character>>>":
                return parseThreeCharArray(input);
            default:
                System.err.println("NOT implement " + originType + ",place implement this ,default convert string list");
                return parseListString(input);
        }
    }

    /**
     * Parses a string input into a list of TreeNode objects.
     *
     * @param input the string input to be parsed into TreeNode objects;
     *        it should be formatted in a way that can be processed by the doubleStringArray method
     * @return a list of TreeNode objects constructed from the parsed input
     */
    private static List<TreeNode> parseListTreeNode(String input) {
        List<TreeNode> treeNodes = new ArrayList<>();
        String[][] strings = doubleStringArray(input);
        for (int i = 0; i < strings.length; i++) {
            TreeNode treeNode = TreeNode.widthBuildTreeNode(strings[i]);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }


    /**
     * Converts the given input string to a processed string by filtering out specific characters.
     *
     * @param input the input string to be processed; can be null or empty
     * @return the processed string after filtering out ignored characters, or an empty string if input is null or empty
     */
    public static String toString(String input) {
        if (input == null || input.length() == 0) {
            // throw new NullPointerException("input content is null");
            return "";
        }
        char[] charArray = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        char st = input.charAt(0);
        for (char c : charArray) {
            if (StringUtils.isIgnore(c, st)) continue;
            sb.append(c);
        }
        return sb.toString();
    }


    /**
     * Converts a string representation of a list of booleans into a boolean array.
     * The input string is parsed into a list of Boolean objects, which is then
     * converted into a primitive boolean array.
     *
     * @param input the string representation of a list of booleans to be parsed
     * @return a boolean array containing the parsed boolean values
     */
    public static boolean[] oneBooleanArray(String input) {
        List<Boolean> ls = parseListBoolean(input);
        boolean[] ans = new boolean[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            ans[i] = ls.get(i);
        }
        return ans;
    }

    /**
     * Converts a string representation of a double boolean array into a 2D boolean array.
     * The input string is parsed into a nested list structure, which is then transformed
     * into a 2D boolean array where each inner array corresponds to a list of boolean values.
     *
     * @param input the string representation of the double boolean array to be converted
     * @return a 2D boolean array derived from the parsed input string
     */
    public static boolean[][] doubleBooleanArray(String input) {
        List<List<Boolean>> ls = parseDoubleBoolean(input);
        boolean[][] ans = new boolean[ls.size()][];
        for (int i = 0; i < ls.size(); i++) {
            ans[i] = new boolean[ls.get(i).size()];
            for (int j = 0; j < ls.get(i).size(); j++) {
                ans[i][j] = ls.get(i).get(j);
            }
        }
        return ans;
    }

    /**
     * Parses a given input string into a list of boolean lists by interpreting each string element.
     * The method relies on the presence of the character 't' in the string to determine if the value is true;
     * otherwise, it is considered false.
     *
     * @param input the input string that will be parsed into nested lists of strings and then converted to booleans
     * @return a list of lists containing boolean values derived from the parsed string input
     */
    private static List<List<Boolean>> parseDoubleBoolean(String input) {
        List<List<String>> doubles = parseDoubleString(input);
        List<List<Boolean>> ans = new ArrayList<>();
        int m = doubles.size(), n = doubles.get(0).size();
        for (int i = 0; i < m; i++) {
            ArrayList<Boolean> temp = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                String s = doubles.get(i).get(j);
                if (s.contains("t")) {
                    temp.add(true);
                } else {
                    temp.add(false);
                }
            }
            ans.add(temp);
        }
        return ans;
    }

    /**
     * Parses a string input into a list of Boolean values.
     * The method first converts the input string into a list of strings using parseListString,
     * then iterates through each string to determine its Boolean value.
     * A string is considered true if it contains the character 't', otherwise it is considered false.
     *
     * @param input the string to be parsed into a list of Boolean values
     * @return a list of Boolean values derived from the input string
     */
    private static List<Boolean> parseListBoolean(String input) {
        List<String> strings = parseListString(input);
        List<Boolean> ans = new ArrayList<>();
        for (String s : strings) {
            if (s.contains("t")) ans.add(true);
            else ans.add(false);
        }
        return ans;
    }


    /**
     * Converts a string representation of a list of doubles into an array of double values.
     * The input string is parsed to extract individual double values which are then stored in the returned array.
     *
     * @param input the string containing the representation of a list of doubles
     * @return an array of double values parsed from the input string
     */
    public static double[] oneDoubleArray(String input) {
        List<Double> ls = parseDoubleList(input);
        double[] ans = new double[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            ans[i] = ls.get(i);
        }
        return ans;
    }


    /**
     * Parses a string representation of a list into a list of Double values.
     * Each element in the input string is converted to a Double if possible.
     * Non-convertible elements are ignored.
     *
     * @param input the string representation of a list to be parsed
     * @return a list of Double values extracted from the input string
     */
    public static List<Double> parseDoubleList(String input) {
        List<String> list = parseListString(input);
        ArrayList<Double> doubles = new ArrayList<>();
        for (String s : list) {
            try {
                doubles.add(parseDouble(s));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return doubles;
    }

    /**
     * Parses a string input into a list of lists containing Double values.
     * The input is expected to be structured in a way that can be processed by the parseDoubleString method,
     * which converts it into a list of lists of strings. Each string is then converted to a Double using
     * the parseDouble method.
     *
     * @param input the string input to be parsed into a list of lists of Double values
     * @return a list of lists of Double values parsed from the input string
     */
    private static List<List<Double>> parseDoubleDoubleList(String input) {
        List<List<String>> list = parseDoubleString(input);
        List<List<Double>> doubles = new ArrayList<>();
        for (List<String> one : list) {
            List<Double> temp = new ArrayList<>();
            for (String s : one) {
                temp.add(parseDouble(s));
            }
            doubles.add(temp);
        }
        return doubles;
    }
    /**
     * Converts a string representation of a list into an array of integers.
     * The input string can be in the format of a JSON-like array (e.g., "[1, 2, 3]")
     * or a JSON-like object (e.g., "{1, 2, 3}"). Empty or invalid inputs return an empty array.
     *
     * @param input the string representation of a list to be parsed into integers
     * @return an array of integers parsed from the input string, or an empty array if input is invalid or empty
     */
    public static int[] oneIntArray(String input) {
        if ("[]".equals(input) || "{}".equals(input)) {
            return new int[]{};
        }
        List<Integer> ls = parseListInteger(input);
        if (ls.isEmpty()) {
            return new int[]{};
        }
        int[] ans = new int[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            ans[i] = ls.get(i);
        }
        return ans;
    }

    /**
     * Converts a string representation of a nested integer list into a two-dimensional integer array.
     * The input string should follow specific formats to represent an empty or non-empty nested list.
     * If the input represents an empty structure, an empty two-dimensional array is returned.
     *
     * @param input the string representation of a nested integer list.
     *              Supported formats for empty structures include "[]", "[[]]", "{}", and "{{}}".
     *              Non-empty structures must follow a valid parseable format expected by parseListDoubleInteger.
     * @return a two-dimensional integer array corresponding to the parsed nested integer list.
     *         Returns an empty two-dimensional array if the input represents an empty structure or parsing fails.
     */
    public static int[][] doubleIntArray(String input) {
        if ("[]".equals(input) || "[[]]".equals(input) || "{}".equals(input) || "{{}}".equals(input)) {
            return new int[][]{};
        }
        List<List<Integer>> ls = parseListDoubleInteger(input);
        if (ls == null || ls.size() == 0) {
            return new int[][]{};
        }
        int row = ls.size();
        int[][] ans = new int[row][];
        for (int i = 0; i < row; i++) {
            ans[i] = new int[ls.get(i).size()];
            for (int j = 0; j < ls.get(i).size(); j++) {
                ans[i][j] = ls.get(i).get(j);
            }
        }
        return ans;
    }

    /**
     * Converts a string representation of a three-dimensional integer array into an actual 3D integer array.
     * The input string is parsed into a nested list structure, which is then converted into a 3D array.
     *
     * @param input the string representation of the three-dimensional integer array to be converted
     * @return a three-dimensional integer array parsed from the input string
     */
    public static int[][][] threeIntArray(String input) {
        List<List<List<Integer>>> ls = parseListThreeInteger(input);
        int[][][] result = new int[ls.size()][][];
        for (int i = 0; i < ls.size(); i++) {
            List<List<Integer>> d = ls.get(i);
            result[i] = new int[d.size()][];
            for (int j = 0; j < d.size(); j++) {
                List<Integer> t = d.get(j);
                result[i][j] = new int[t.size()];
                for (int k = 0; k < t.size(); k++) {
                    result[i][j][k] = t.get(k);
                }
            }
        }
        return result;
    }


    /**
     * Converts a given string into a character array by parsing it into a list of characters first.
     *
     * @param input the string to be converted into a character array
     * @return a character array containing all characters from the input string
     */
    public static char[] oneCharArray(String input) {
        List<Character> ls = parseListChar(input);
        char[] cs = new char[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            cs[i] = ls.get(i);
        }
        return cs;
    }

    /**
     * Converts a given string into a two-dimensional character array.
     * The input string is parsed into a list of character lists, which is then
     * transformed into a 2D character array where each row corresponds to a list
     * of characters.
     *
     * @param input the string to be parsed and converted into a 2D character array
     * @return a two-dimensional character array derived from the parsed input string
     */
    public static char[][] doubleCharArray(String input) {
        List<List<Character>> ls = parseListDoubleChar(input);
        int row = ls.size();
        char[][] cs = new char[row][];
        for (int i = 0; i < row; i++) {
            List<Character> cc = ls.get(i);
            cs[i] = new char[cc.size()];
            for (int j = 0; j < cc.size(); j++) {
                cs[i][j] = cc.get(j);
            }
        }
        return cs;
    }


    /**
     * Converts a given string representation into a three-dimensional character array.
     * The input string is parsed to construct a nested structure of characters organized
     * in a 3D array format. The method relies on a helper method, parseThreeCharArray,
     * to generate the intermediate nested list structure before converting it into the
     * final 3D array.
     *
     * @param input the string to be parsed and converted into a 3D character array;
     *              the format of the string should be compatible with the expected
     *              structure returned by parseThreeCharArray
     * @return a three-dimensional character array derived from the parsed structure
     *         of the input string
     */
    public static char[][][] threeCharArray(String input) {
        List<List<List<Character>>> lists = parseThreeCharArray(input);
        int row = lists.size();
        char[][][] ans = new char[row][][];
        for (int i = 0; i < row; i++) {
            ans[i] = new char[lists.get(i).size()][];
            for (int j = 0; j < lists.get(i).size(); j++) {
                ans[i][j] = new char[lists.get(i).get(j).size()];
                for (int z = 0; z < lists.get(i).get(j).size(); z++) {
                    ans[i][j][z] = lists.get(i).get(j).get(z);
                }
            }
        }
        return ans;
    }


    /**
     * Parses a string representation of a list into a list of integers.
     * The input string is expected to be formatted as a list, such as "[1, 2, 3]" or "{4, 5, 6}".
     * Non-integer values within the list are ignored.
     *
     * @param input the string representation of a list to be parsed
     * @return a list of integers extracted from the input string
     */
    public static List<Integer> parseListInteger(String input) {
        List<String> strings = parseListString(input);
        ArrayList<Integer> ans = new ArrayList<>();
        if ("[]".equals(input) || "{}".equals(input)) {
            return ans;
        }
        for (String s : strings) {
            try {
                ans.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return ans;
    }

    /**
     * Parses a string representation of a nested list structure into a list of lists containing integers.
     * The input string is expected to represent a nested list where each inner list contains integer values.
     * Handles various edge cases such as empty or malformed input gracefully.
     *
     * @param input the string representation of a nested list structure to be parsed
     * @return a list of lists of integers parsed from the input string, or an empty list if the input is empty or invalid
     */
    public static List<List<Integer>> parseListDoubleInteger(String input) {

        List<List<Integer>> ls = new ArrayList<>();
        if ("[]".equals(input) || "[[]]".equals(input) || "{}".equals(input) || "{{}}".equals(input)) {
            return new ArrayList<>();
        }
        List<List<String>> lists = parseDoubleString(input);
        for (List<String> row : lists) {
            if (row == null) {
                continue;
            }
            List<Integer> temp = new ArrayList<>();
            for (String s : row) {
                if (s == null) continue;
                try {
                    temp.add(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
            ls.add(temp);
        }
        return ls;
    }

    /**
     * Parses a string input into a three-dimensional list of integers.
     * The method processes the input by first parsing it into a three-dimensional list of strings,
     * then converting each string to an integer where possible. Strings that cannot be parsed into
     * integers are ignored.
     *
     * @param input the input string to be parsed into a three-dimensional list of integers
     * @return a three-dimensional list of integers derived from the input string
     */
    public static List<List<List<Integer>>> parseListThreeInteger(String input) {
        List<List<List<Integer>>> ans = new ArrayList<>();
        List<List<List<String>>> lists = parseThreeString(input);
        for (List<List<String>> list : lists) {
            List<List<Integer>> d = new ArrayList<>();
            if (list == null) continue;
            for (List<String> strings : list) {
                if (strings == null) continue;
                List<Integer> t = new ArrayList<>();
                for (String string : strings) {
                    if (string == null) continue;
                    try {
                        t.add(Integer.parseInt(string));
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }
                d.add(t);
            }
            ans.add(d);
        }
        return ans;
    }


    /**
     * Parses a given string into a list of characters.
     * The method first parses the input string into a list of strings using an auxiliary method,
     * then extracts the first character from each string and adds it to the resulting list.
     *
     * @param input the input string to be parsed into a list of characters
     * @return a list of characters extracted from the input string
     */
    public static List<Character> parseListChar(String input) {
        List<Character> ls = new ArrayList<>();
        List<String> strings = parseListString(input);
        for (String s : strings) {
            ls.add(s.charAt(0));
        }
        return ls;
    }

    /**
     * Parses a given string input into a list of lists of characters.
     * The method processes the input by first parsing it into a list of lists of strings,
     * then converts each string into its corresponding character.
     *
     * @param input the input string to be parsed
     * @return a list of lists of characters derived from the input string
     */
    public static List<List<Character>> parseListDoubleChar(String input) {
        List<List<String>> lists = parseDoubleString(input);
        List<List<Character>> ls = new ArrayList<>();
        List<Character> temp = null;
        for (List<String> row : lists) {
            temp = new ArrayList<>();
            for (String s : row) {
                temp.add(s.charAt(0));
            }
            ls.add(temp);
        }
        return ls;
    }

    /**
     * Parses a given string input into a three-dimensional list of characters.
     * The method processes the input by first converting it into a three-dimensional list of strings
     * using an external method `parseThreeString`. Each non-null string is then reduced to its first character,
     * which is added to the resulting structure.
     *
     * @param input the input string to be parsed into a three-dimensional list of characters
     * @return a three-dimensional list of characters derived from the input string,
     *         where each character corresponds to the first character of the strings in the intermediate structure
     */
    public static List<List<List<Character>>> parseThreeCharArray(String input) {
        List<List<List<Character>>> ans = new ArrayList<>();
        List<List<List<String>>> lists = parseThreeString(input);
        for (List<List<String>> list : lists) {
            List<List<Character>> d = new ArrayList<>();
            if (list == null) continue;
            for (List<String> strings : list) {
                if (strings == null) continue;
                List<Character> t = new ArrayList<>();
                for (String string : strings) {
                    if (string == null) continue;
                    t.add(string.charAt(0));
                }
                d.add(t);
            }
            ans.add(d);
        }
        return ans;
    }


    /**
     * Converts a given input string into an array of strings by parsing it into a list and then transforming it.
     *
     * @param input the input string that needs to be parsed and converted into an array of strings
     * @return an array of strings derived from the parsed input string
     */
    public static String[] oneStringArray(String input) {
        List<String> ls = parseListString(input);
        String[] ans = new String[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            ans[i] = ls.get(i);
        }
        return ans;
    }

    /**
     * Converts a given input string into a two-dimensional String array by parsing the input
     * into nested lists and then transforming it into an array structure.
     *
     * @param input the input string that needs to be parsed and converted into a 2D String array
     * @return a two-dimensional String array derived from the parsed input
     */
    public static String[][] doubleStringArray(String input) {
        List<List<String>> lists = parseDoubleString(input);
        int row = lists.size();
        String[][] ans = new String[row][];
        for (int i = 0; i < row; i++) {
            ans[i] = new String[lists.get(i).size()];
            for (int j = 0; j < lists.get(i).size(); j++) {
                ans[i][j] = lists.get(i).get(j);
            }
        }
        return ans;
    }

    /**
     * Converts a given input string into a three-dimensional String array
     * by parsing it into nested lists and transforming those lists into arrays.
     *
     * @param input the input string to be parsed and converted into a 3D String array
     * @return a three-dimensional String array derived from the parsed input
     */
    public static String[][][] threeStringArray(String input) {
        List<List<List<String>>> lists = parseThreeString(input);
        int row = lists.size();
        String[][][] ans = new String[row][][];
        for (int i = 0; i < row; i++) {
            ans[i] = new String[lists.get(i).size()][];
            for (int j = 0; j < lists.get(i).size(); j++) {
                ans[i][j] = new String[lists.get(i).get(j).size()];
                for (int z = 0; z < lists.get(i).get(j).size(); z++) {
                    ans[i][j][z] = lists.get(i).get(j).get(z);
                }
            }
        }
        return ans;
    }


    /**
     * Parses a given input string based on specific flag characters and returns a list of substrings.
     * The method identifies substrings enclosed by start and end flags, optionally separated by an interrupt flag.
     *
     * @param input the input string to be parsed; may contain special flag characters or plain text
     * @return a list of strings extracted from the input based on the parsing logic;
     *         if no flags are present, the entire input is returned as a single-element list
     */
    public static List<String> parseListString(String input) {
        List<String> ls = new ArrayList<>();
        char[] flag = getFlag(input);
        char startFlag = flag[0];
        char endFlag = flag[1];
        char interruptFlag = flag[2];
        if (!input.contains(String.valueOf(startFlag)) && !input.contains(String.valueOf(endFlag))) {
            ls.add(input);
            return ls;
        }
        String nullStr = new String(new char[]{startFlag, endFlag});
        if (nullStr.equals(input)) return ls;
        StringBuilder sb = null;
        char[] cs = input.toCharArray();
        for (char c : cs) {
            if (c == startFlag) {
                sb = new StringBuilder();
                continue;
            }
            if (sb == null) break;
            if (c == endFlag) {
                ls.add(sb.toString());
                break;
            } else if (c == interruptFlag) {
                ls.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        return ls;
    }


    /**
     * Parses a given input string based on specific flag characters and returns a nested list structure.
     * The method identifies segments of the string enclosed by start and end flags,
     * and further splits these segments by an interrupt flag.
     *
     * @param input the input string to be parsed; must contain valid flag characters for processing
     * @return a list of lists of strings, where each inner list represents a segment enclosed
     *         by the start and end flags, and the strings within the inner list are substrings
     *         separated by the interrupt flag
     */
    public static List<List<String>> parseDoubleString(String input) {
        StringBuilder sb = null;
        char[] flag = getFlag(input);
        char startFlag = flag[0];
        char endFlag = flag[1];
        char interruptFlag = flag[2];
        List<List<String>> ls = new ArrayList<>();
        List<String> temp = null;
        Stack<Character> sk = new Stack<>();
        char[] cs = input.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            if (c == startFlag) {
                sk.push(c);
                if (sk.size() == 2) temp = new ArrayList<>();
            } else if (c == endFlag) {
                if (!sk.isEmpty()) {
                    sk.pop();
                }
                if (sk != null && !sk.isEmpty() && temp != null) {
                    if (sb != null) {
                        temp.add(sb.toString());
                    }
                    ls.add(temp);
                }
                sb = null;
                temp = null;
            } else if (c == interruptFlag) {
                if (temp != null && sb != null) {
                    temp.add(sb.toString());
                    sb = new StringBuilder();
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(c);
            }
        }

        return ls;
    }


    /**
     * Parses a given input string into a nested list structure based on specific delimiters.
     * The method identifies three levels of nesting using start, end, and interrupt flags
     * derived from the input string. It processes the input character by character,
     * grouping substrings into lists at each level of nesting.
     *
     * @param input the input string to be parsed; must not be null or empty
     * @return a nested list of strings, where each level of nesting corresponds to the
     *         hierarchical structure defined by the delimiters in the input string
     */
    public static List<List<List<String>>> parseThreeString(String input) {
        List<List<List<String>>> ans = new ArrayList<>();
        char[] charArray = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean f1 = false, f2 = false, f3 = false;
        List<List<String>> d = new ArrayList<>();
        List<String> t = new ArrayList<>();
        Stack<Character> sk = new Stack<>();
        char[] flag = getFlag(input);
        char startFlag = flag[0];
        char endFlag = flag[1];
        char interruptFlag = flag[2];
        char stChar = input.charAt(0);
        for (char c : charArray) {
            if (StringUtils.isIgnore(c, stChar)) continue;
            if (c == startFlag) {
                sk.push(c);
                if (sk.size() == 2) {
                    d = new ArrayList<>();
                } else if (sk.size() == 3) {
                    t = new ArrayList<>();
                }
            } else if (c == endFlag) {
                if (!sk.isEmpty()) {
                    sk.pop();
                }
                if (sk.size() == 1) {
                    ans.add(d);
                } else if (sk.size() == 2) {
                    if (sb != null) {
                        t.add(sb.toString());
                        sb = null;
                    }
                    d.add(t);
                }
            } else if (c == interruptFlag) {
                if (sb != null) {
                    t.add(sb.toString());
                }
                sb = null;
            } else {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(c);
            }
        }
        return ans;
    }


    /**
     * Provides error information and example input formats based on the specified argument type.
     * This method is used to guide users on how to provide valid input for various data types.
     *
     * @param argType the type of argument for which error information is requested.
     *                Supported types include "boolean", "long", "double", "float", "int", "int[]",
     *                "ListNode", "int[][]", "char", "char[]", "char[][]", "List", "string[]",
     *                "string[][]", and "TreeNode". If an unsupported type is provided,
     *                a default message for unknown types is displayed.
     */
    public static void errorInfo(String argType) {
        switch (argType) {
            case "boolean":
                System.out.println("place input a valid boolean content , example true ");
                break;
            case "long":
                System.out.println("place input a valid long number , example 100000000 ");
                break;
            case "double":
                System.out.println("place input a valid double number , example 100.0");
                break;
            case "float":
                System.out.println("place input a valid float number , example 1.1");
                break;
            case "int":
                System.out.println("place input a valid number , example 100,-1 , 0");
                break;
            case "int[]":
            case "ListNode":
                System.out.println("place input this format int[] ,example [1,5,4,2,9,9,9]");
                break;
            case "int[][]":
                System.out.println("place input this format int[][] ,example [[1,2,-1],[4,-1,6],[7,8,9]]");
                break;
            case "char":
                System.out.println("lace input this format char example a ");
                break;
            case "char[]":
                System.out.println("place input this format char[] ,example [\"8\",\"3\",\".\",\".\",\"7\",\".\",\".\",\".\",\".\"]");
                break;
            case "char[][]":
                System.out.println("place input this format char[][] ,example [[\"8\",\"3\",\".\",\".\",\"7\",\".\",\".\",\".\",\".\"],[\".\",\".\",\".\",\".\",\"8\",\".\",\".\",\"7\",\"9\"]]\n");
                break;
            case "List":
            case "string[]":
                System.out.println("place input this format List<String> ,example [\"abbb\",\"ba\",\"aa\"] if content is List<Integer> [1,5,4,2,9,9,9]");
                break;
            case "string[][]":
                System.out.println("place input this format string[][],example [[\"abbb\",\"ba\",\"aa\"],[\"ee\",\"dd\",\"cc\"]");
                break;
            case "TreeNode":
                System.out.println("place input this format TreeNode,example [3,9,20,null,null,15,7]");
                break;
            default:
                System.out.println("unknown support type");
                break;
        }
    }


    /**
     * Determines a flag array based on the first non-space character of the input string.
     * The method identifies specific starting characters ('{' or '[') and assigns
     * corresponding values to the returned flag array. If no valid starting character is found,
     * the flag array reflects an invalid format by modifying its last element.
     *
     * @param input the input string to analyze for determining the flag; must not be null
     * @return a char array of length 4 where:
     *         - The first three elements represent the identified flag characters
     *         - The fourth element indicates validity ('Y' for valid, 'N' for invalid)
     */
    public static char[] getFlag(String input) {
        char st = '\0';
        for (int i = 0; i < input.length(); i++) {
            if ((st = input.charAt(i)) != ' ' && st != '\0') {
                break;
            }
        }
        char[] flag = new char[4];
        flag[3] = 'Y';
        if (st == '{') {
            flag[0] = '{';
            flag[1] = '}';
            flag[2] = ',';
        } else if (st == '[') {
            flag[0] = '[';
            flag[1] = ']';
            flag[2] = ',';
        } else {
            flag[3] = 'N';
            //  throw new RuntimeException("NO this parse format, place implement ,start flag is " + st);
        }
        return flag;
    }


    /**
     * Retrieves the package information based on the provided class file path.
     * The method processes the input path to determine the package structure,
     * ensuring it is valid and belongs to the expected project directory.
     *
     * @param classFile the path to the class file or directory for which package information is required
     * @return a string representing the package structure in dot notation (e.g., com.example.package)
     * @throws NullPointerException if the provided classFile parameter is null
     * @throws RuntimeException if the directory is invalid, outside the project's working directory,
     *                          or does not belong to the expected project root directory
     */
    public static String getPackageInfo(String classFile) {
        if (classFile == null) {
            throw new NullPointerException();
        }
        classFile = classFile.replace("\\\\\\", "\\").replace("\\\\", "\\");
        File file = new File(classFile);
        String dir = "";
        if (file.exists()) {
            if (file.isFile()) {
                int i = classFile.lastIndexOf(File.separator);
                if (i != -1) {
                    dir = classFile.substring(0, i);
                }
            } else { // is dir
                dir = classFile;
            }

        } else {
            if (classFile.endsWith(".java")) {
                int i = classFile.lastIndexOf(File.separator);
                if (i != -1) {
                    dir = classFile.substring(0, i);
                }
            } else {
                dir = classFile;
            }
        }
        if (StringUtils.isEmpty(dir) || !dir.contains(IoUtil.getWorkDir())) {
            throw new RuntimeException("place check project running on " + IoUtil.getWorkDir());
        }
        int id = dir.indexOf(IoUtil.getProjectRootDir());
        if (id == -1) {
            throw new RuntimeException("place check " + dir + " is local work dir ");
        }
        String packageDir = dir.substring(id + IoUtil.getProjectRootDir().length());
        char[] charArray = packageDir.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c != '\\' && c != '/') {
                continue;
            }
            if (i == charArray.length - 1) {
                charArray[i] = ' ';
                break;
            } else {
                c = '.';
            }
            charArray[i] = c;
        }
        return new String(charArray);
    }


    /**
     * Parses a constructor class string into an array of strings based on specific delimiters and flags.
     *
     * The method identifies the start, end, and intermediate delimiters either from the input string's flags
     * or defaults to predefined values. It then processes the string to extract substrings separated by the
     * intermediate delimiter, while accounting for nested structures defined by the start and end delimiters.
     *
     * @param s the input string to be parsed, which may contain nested structures and delimiters
     * @return an array of strings representing the parsed components of the input string,
     *         with ignored or processed content as per the utility method `StringUtils.ingoreString`
     */
    // constructor class parse
    public static String[] parseConstrunctorClassString(String s) {
        int st = 0, ed = s.length() - 1;
        char start = 0;
        char end = 0;
        char inter = 0;
        char[] flag = ReflectUtils.getFlag(s);
        if (flag[3] == 'Y') {
            start = flag[0];
            end = flag[1];
            inter = flag[2];
            while (st < s.length() && s.charAt(st) != start) {
                st++;
            }
            while (ed >= 0 && s.charAt(ed) != end) {
                ed--;
            }
        } else {
            start = '[';
            end = ']';
            inter = ',';
            ed = s.length();
            st = -1;
        }
        int deep = 0;
        List<String> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = st + 1; i < ed; i++) {
            char c = s.charAt(i);
            if (c == start) {
                deep++;
                if (sb == null) sb = new StringBuilder();
                sb.append(c);
            } else if (c == end) {
                deep--;
                if (sb != null) {
                    sb.append(c);
                }
                if (deep == 0) {
                    ans.add(sb.toString());
                    sb = null;
                }
            } else if (c == inter) {
                if (sb != null) {
                    if (deep == 0) {
                        ans.add(sb.toString());
                        sb = null;
                    } else {
                        sb.append(c);
                    }
                } else {
                    sb = new StringBuilder();
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(c);

            }


        }
        if (sb != null) {
            ans.add(sb.toString());
        }

        String[] strings = new String[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            strings[i] = StringUtils.ingoreString(ans.get(i));
        }

        return strings;
    }



    /**
     * Processes the input argument for a constructor method and updates the result list.
     * This method parses the input string to extract class names relevant to the constructor,
     * then adds these parsed strings to the provided result list.
     *
     * @param arg    the input string containing constructor-related class information to be parsed
     * @param result the list to which the parsed class names will be added
     * @param method the method object associated with the constructor being processed
     */
    public static void handlerConstructorMethodInput(String arg, List<String> result, Method method) {
        String[] ss = parseConstrunctorClassString(arg);
        for (String s : ss) {
            result.add(s);
        }
    }

    /**
     * Handles the constructor method input by delegating the processing to an overloaded method.
     * This method acts as a wrapper that simplifies the invocation by allowing the caller
     * to omit the optional parameter.
     *
     * @param arg    the input argument to be processed, typically representing a key or identifier
     * @param result a list of strings where the processed results will be stored or appended
     */
    public static void handlerConstructorMethodInput(String arg, List<String> result) {
        handlerConstructorMethodInput(arg, result, null);
    }



    /**
     * Processes the output of a constructor method and appends the result to a list.
     * This method evaluates the return type of the provided method and determines
     * how to format the expression string before adding it to the result list.
     *
     * @param exp the expression string representing the constructor output
     * @param result the list where the processed output will be stored
     * @param method the method object whose return type is used to determine processing logic
     */
    public static void handlerConstructorMethodOutput(String exp, List<String> result, Method method) {
        Class<?> returnType = method.getReturnType();
        String returnName = returnType.getSimpleName();
        if (returnName.contains("[]")
                || returnName.contains("List")
                || returnName.contains("ArrayList")
                || returnName.contains("TreeNode")
                || returnName.contains("LinkedList")
                || returnName.contains("ListNode")
                || returnName.contains("Queue")
        ) {
            result.add(exp);
        } else {
            String s = parseConstrunctorClassString(exp)[0];
            result.add(s);
        }
    }

    /**
     * Loads the origin class of the given source class.
     * If the source class is a nested or inner class, the method attempts to return the top-level enclosing class.
     * If the source class is already a top-level class, it returns the source class itself.
     * If the source class is null, the method returns null.
     *
     * @param src the source class for which the origin class is to be determined
     * @return the origin class of the source class, or the source class itself if it is not nested,
     *         or null if the input source class is null
     */
    public static  Class<?> loadOrigin(Class<?> src) {
        if (src == null) {
            return null;
        }
        String name = src.getName();
        if (name.contains("$")) {
            String[] ss = name.split("\\$");
            try {
                return Class.forName(ss[0]);
            } catch (ClassNotFoundException e) {
                return src;
            }
        } else {
            return src;
        }
    }

    /**
     * Parses the given string representation of a double value into its primitive double equivalent.
     * This method internally uses Double.parseDouble to convert the string and then passes the result
     * to an overloaded parseDouble method.
     *
     * @param d the string to be parsed as a double value; must not be null and should contain a valid
     *          numeric representation
     * @return the primitive double value corresponding to the input string
     */
    public static double parseDouble(String d) {
        return parseDouble(Double.parseDouble(d));
    }

    /**
     * Parses a double value and rounds it to five decimal places.
     *
     * @param d the double value to be parsed and rounded
     * @return the double value rounded to five decimal places
     */
    // https://leetcode.cn/problems/minimum-cost-to-hire-k-workers
    // 结果保留五位小数
    public static double parseDouble(double d) {
        return Double.parseDouble(String.format("%.5f", d));
    }

    /**
     * Initializes a new instance of the specified class using the provided arguments.
     * Attempts to create an instance using the default constructor first. If that fails,
     * it tries to use the declared constructor with the given arguments.
     *
     * @param src the Class object representing the class to instantiate
     * @param args an array of objects to pass as arguments to the constructor
     * @return a new instance of the specified class, or null if instantiation fails
     */
    public static Object initObjcect(Class<?> src,Object[] args) {
        Object obj = null;

        try {
            obj = src.newInstance();
        } catch (IllegalAccessException e) {
            try {
                Constructor<?> constructor = src.getDeclaredConstructor(null);
                if (constructor != null) {
                    constructor.setAccessible(true);
                    obj = constructor.newInstance(args);
                }
            } catch (Exception ex) {

            }
        } catch (Exception e) {

        }

        return obj;
    }


    /**
     * Determines whether the provided class represents a base type.
     * Base types include primitive types and their corresponding wrapper classes.
     * If the input class is null, it is considered a base type by default.
     *
     * @param c the Class object to evaluate; can be null
     * @return true if the class represents a base type, false otherwise
     */
    public static boolean isBaseType(Class<?> c) {
        if (c == null) {
            return true;
        }
        String name = c.getSimpleName();

        // char byte short int long double float
        if ("char".equals(name) || "Character".equals(name)) {
            return true;
        }
        if ("byte".equalsIgnoreCase(name)) {
            return true;
        }
        if ("Integer".equals(name) || "int".equals(name)) {
            return true;
        }
        if ("boolean".equalsIgnoreCase(name)) {
            return true;
        }
        if ("long".equalsIgnoreCase(name)) {
            return true;
        }
        if ("double".equalsIgnoreCase(name)) {
            return true;
        }
        if ("float".equalsIgnoreCase(name)) {
            return true;
        }
        return false;
    }

    /**
     * Determines the index of the first non-base type in the provided array of parameter types.
     * If the array is null or all elements are base types, returns -1.
     *
     * @param parameterTypes an array of Class objects representing the parameter types to evaluate
     * @return the index of the first non-base type in the array, or -1 if the array is null or contains only base types
     */
    public static int handlerVoidReturnType(Class<?>[] parameterTypes) {
        if (parameterTypes == null) {
            return -1;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (isBaseType(parameterTypes[i])) {
                continue;
            }
            return i;
        }
        return -1;
    }




    /**
     * Retrieves the test case information associated with the given class.
     *
     * @param aClass the Class object for which the test case information is to be retrieved
     * @return an array of integers containing the test case information, or null if no annotation is present
     */
    public static int[] getTestCaseInfo(Class<?> aClass) {
        return getTestCaseInfo(aClass.getDeclaredAnnotation(TestCaseGroup.class));
    }

    /**
     * Retrieves the test case information associated with the given method.
     * The method looks for a declared annotation of type TestCaseGroup on the provided method
     * and uses it to determine the test case details.
     *
     * @param method the method for which test case information is to be retrieved
     * @return an array of integers representing the test case information derived from the TestCaseGroup annotation
     */
    public static int[] getTestCaseInfo(Method method) {
        return getTestCaseInfo(method.getDeclaredAnnotation(TestCaseGroup.class));
    }


    /**
     * Retrieves the test case information based on the provided annotation.
     *
     * @param annotation the TestCaseGroup annotation containing the start and end values for test cases
     * @return an array of two integers where the first element is the starting test case index
     *         and the second element is the ending test case index; defaults to [1, Integer.MAX_VALUE]
     *         if the annotation is null or its use flag is false
     */
    public static int[] getTestCaseInfo(TestCaseGroup annotation) {
        if (annotation == null || !annotation.use()) {
            return new int[]{1, Integer.MAX_VALUE};
        }
        int start = Math.max(1, Math.min(annotation.start(), annotation.end()));
        int end = Math.max(1, Math.max(annotation.start(), annotation.end()));
        return new int[]{start, end};
    }

}
