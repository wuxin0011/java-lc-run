package code_generation.utils;


import code_generation.contest.ParseCodeInfo;
import code_generation.proxy.TestData;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing various helper methods for I/O operations, file handling, and testing utilities.
 * This class is designed to assist with tasks such as reading files, writing content, validating methods,
 * and performing comparisons between methods or constructors. It also includes methods for path resolution
 * and content parsing.
 */

public class IoUtil {


    /**
     * Represents the default method name used within the {@code IoUtil} class.
     * This constant is utilized as a fallback or placeholder value when a specific
     * method name is not provided or applicable. The value is set to "null",
     * indicating the absence of a valid method name. This can be useful in scenarios
     * where method resolution logic needs to handle cases where no explicit method
     * name is defined.
     */
    public static final String DEFAULT_METHOD_NAME = "null";


    /**
     * Represents a constant string value "null" used to indicate either a void return type
     * or the presence of arguments in method validation and invocation contexts.
     * This constant is primarily utilized within utility methods to handle scenarios where
     * methods may have no return value or require argument-based comparisons during testing.
     */
    public static final String VOID_OR_ARGS = "null";


    /**
     * Represents the default file name used for reading input data within the utility.
     * This constant defines the standard file name "in.txt" which is expected to be present
     * in the context where file reading operations are performed. It serves as a fallback
     * or default option when no specific file name is provided explicitly.
     */
    public static final String DEFAULT_READ_FILE = "in.txt";


    /**
     * Indicates whether long content support is enabled by default.
     * When set to true, it allows the processing of extended content features;
     * when false, such features are disabled. This flag primarily influences
     * methods that handle file reading or content parsing where extended content
     * may be involved.
     */
    public static final boolean DEFAULT_SUPPORT_LONG_CONTENT = false;


    /**
     * Indicates whether strict equality is enforced during comparisons or validations.
     * When set to true, the comparison will require exact matches, including order and type,
     * whereas a false value allows for more lenient matching criteria.
     */
    public static final boolean IS_STRICT_EQUAL = true;


    /**
     * Represents the default directory roots used as a base path for file operations.
     * These roots are typically utilized to construct or locate project directories,
     * especially in contexts where source code files are organized under specific folder hierarchies.
     * The array defines a sequence of directory names that form the path structure.
     */
    public static final String[] DEFAULT_ROOTS = {"src", "main", "java"};


    /**
     * Represents the root directory of the project.
     * This variable is used to store the absolute path to the root directory, which serves as the base for file operations and resource management.
     * The value should be initialized appropriately before any operations that depend on it to ensure correct behavior.
     * If not set, it defaults to "null", indicating that the root directory has not been configured.
     */
    private static String ROOT_DIR = "null";

    static {
        getProjectRootDir();
    }

    /**
     * Retrieves the project root directory as a string.
     * If the root directory has already been determined and stored in {@code ROOT_DIR},
     * it will be returned directly. Otherwise, the method constructs the root directory
     * path by appending the values from {@code DEFAULT_ROOTS} separated by the system's
     * file separator. If {@code DEFAULT_ROOTS} is null, a runtime exception is thrown.
     *
     * @return the project root directory as a string
     */
    public static String getProjectRootDir() {
        if (!(ROOT_DIR == null || "null".equals(ROOT_DIR))) {
            return ROOT_DIR;
        }
        if (DEFAULT_ROOTS == null) {
            throw new RuntimeException("not null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(File.separator);
        for (String defaultRoot : DEFAULT_ROOTS) {
            sb.append(defaultRoot);
            sb.append(File.separator);
        }
        ROOT_DIR = sb.toString();
        return ROOT_DIR;
    }


    /**
     * Invokes the test utility with default method name and file reading settings.
     * This method serves as a simplified entry point for testing utilities,
     * internally delegating to an overloaded version of {@code testUtil} with
     * predefined default values for method name and file reading options.
     *
     * @param c the Class object representing the type to be tested
     */
    public static void testUtil(Class<?>  c) {
        testUtil(c, DEFAULT_METHOD_NAME, DEFAULT_READ_FILE);
    }


    /**
     * Invokes the test utility with specified parameters for testing a class.
     * This method acts as a bridge to the core testing functionality, allowing
     * customization of the method name, file name, and long content handling.
     * Internally, it delegates to an overloaded version of {@code testUtil} with
     * an additional strict equality parameter.
     *
     * @param c               the Class object representing the type to be tested
     * @param fileName        the name of the file to be used for testing
     * @param openLongContent a boolean flag indicating whether to enable parsing of long content
     */
    public static void testUtil(Class<?> c, String fileName, boolean openLongContent) {
        testUtil(c, DEFAULT_METHOD_NAME, fileName, openLongContent);
    }


    /**
     * Invokes the test utility with a specified class and a flag for handling long content.
     * This method serves as a simplified entry point for testing utilities, internally
     * delegating to an overloaded version of {@code testUtil} with default values for
     * method name and file reading options.
     *
     * @param c               the Class object representing the type to be tested
     * @param openLongContent a boolean flag indicating whether to enable parsing of long content
     */
    public static void testUtil(Class<?> c, boolean openLongContent) {
        testUtil(c, DEFAULT_METHOD_NAME, DEFAULT_READ_FILE, openLongContent);
    }


    /**
     * Invokes the test utility with a specified class, method name, and file name.
     * This method serves as an intermediary for testing utilities, allowing customization
     * of the method name and the file used for testing. It internally delegates to another
     * overloaded version of {@code testUtil} with a default value for long content handling.
     *
     * @param c          the Class object representing the type to be tested
     * @param methodName the name of the method to be invoked during testing;
     *                   if set to a default value, it automatically invokes a method other than "main"
     */
    public static void testUtil(Class<?> c, String methodName) {
        testUtil(c, methodName, DEFAULT_READ_FILE);
    }


    /**
     * Invokes the test utility with specified parameters for testing a class.
     * This method allows customization of the method name, file name, and long content handling.
     * Internally, it delegates to an overloaded version of {@code testUtil} with a default value
     * for strict equality comparison.
     *
     * @param c          the Class object representing the type to be tested
     * @param methodName the name of the method to be invoked during testing;
     *                   if set to a default value, it automatically invokes a method other than "main"
     * @param fileName   the name of the file to be used for testing
     */
    public static  void testUtil(Class<?> c, String methodName, String fileName) {
        testUtil(c, methodName, fileName, DEFAULT_SUPPORT_LONG_CONTENT);
    }

    /**
     * Invokes the test utility with specified parameters for testing a class.
     * This method acts as a bridge to the core testing functionality, allowing
     * customization of the method name, file name, and long content handling.
     * Internally, it delegates to an overloaded version of {@code testUtil} with
     * a default value for strict equality comparison.
     *
     * @param c               the Class object representing the type to be tested
     * @param methodName      the name of the method to be invoked during testing;
     *                        if set to a default value, it automatically invokes a method other than "main"
     * @param fileName        the name of the file to be used for testing
     * @param openLongContent a boolean flag indicating whether to enable parsing of long content
     */
    public static  void testUtil(Class<?> c, String methodName, String fileName, boolean openLongContent) {
        testUtil(c, methodName, fileName, openLongContent, IS_STRICT_EQUAL);
    }


    /**
     * Invokes the test utility with comprehensive parameters for testing a class.
     * This method performs validation of the specified method within the provided class,
     * using input data from a file. It supports handling of long content, strict equality
     * checks, and constructor-based validation.
     *
     * @param src             the Class object representing the type to be tested
     * @param methodName      the name of the method to be invoked during testing;
     *                        if set to a default value, it automatically invokes a method other than "main"
     * @param fileName        the name of the file containing test input data
     * @param openLongContent a boolean flag indicating whether to enable parsing of long content
     * @param isStrict        a boolean flag indicating whether strict equality checks should be applied
     */
    public static void testUtil(Class<?> src, String methodName, String fileName, boolean openLongContent, boolean isStrict) {
        check(src, methodName, fileName);
        boolean find = false;
        try {

            List<String> inputList = readFile(src, fileName, openLongContent);
            if (inputList == null) {
                System.exit(0);
            }
            // 构造类对拍
            if (ParseCodeInfo.ConstructorClass.equals(methodName)) {
                find = true;
                handlerConstructorValid(src, inputList, methodName, isStrict);
            } else {
                Object obj = ReflectUtils.initObjcect(src, null);
                Method method = findMethodName(src, methodName);
                if (method != null) {
                    find = true;
                    startValid(obj, method, inputList, isStrict, true);
                }


            }
            if (!find) {
                System.err.println("check methodName ,not found " + methodName + " method !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Validates the constructor and methods of a given class using provided test data.
     * This method processes input data in groups of three lines: method names, arguments, and expected results.
     * It performs validation by comparing actual outputs with expected outputs for each method call.
     * Errors are collected and reported at the end of execution.
     *
     * @param src        the Class object representing the class to be validated
     * @param inputList  a list of strings containing test data in groups of three lines:
     *                   method names, arguments, and expected results
     * @param methodName the name of the method to be validated (not directly used in the method)
     * @param isStrict   a boolean flag indicating whether strict validation should be applied
     */
    public static void handlerConstructorValid(Class<?> src, List<String> inputList, String methodName, boolean isStrict) {
        String[] names = null, args = null, expect = null;
        // 是否是测试阶段
        boolean isTest = false;

        if (isTest) {
            System.err.println("constructor class is test ... ");
        }

        final String className = src.getSimpleName();

        Constructor constructor = src.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] constructorArgs = null;


        Method[] declaredMethods = src.getDeclaredMethods();


        List<String> result = null;

        int runTimes = 0;

        Map<String, Method> map = new HashMap<>();
        for (Method method : declaredMethods) {
            method.setAccessible(true);
            map.put(method.getName(), method);
        }

        int t = 0;
        int compareTimes = 0;
        TestData testData = new TestData(src);
        int[] testGroup = testData == null || testData.testCaseGroup == null ? new int[]{1, 0x3fffff} : testData.testCaseGroup;
        boolean isTestCase = false;
        List<String> errorTimes = new ArrayList<>();
        for (int k = 0; k < inputList.size(); k++) {
            String s = inputList.get(k);
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            // System.out.println(s);
            t++;
            if (t % 3 == 1) {
                names = ReflectUtils.oneStringArray(s);
                for (int i = 0; i < names.length; i++) {
                    names[i] = StringUtils.ingoreString(names[i]);
                }
            } else if (t % 3 == 2) {
                args = ReflectUtils.parseConstrunctorClassString(s);
            } else if (t % 3 == 0) {
                expect = ReflectUtils.parseConstrunctorClassString(s);
            }


            // 每三行内容为一组 需要填充调用的方法名，填入结果 以及期望结果
            if (t % 3 == 0) {

                Object obj = null;
                int a = 0, b = 0, deep = 0;

                // check
                if (args.length != expect.length || args.length != names.length || expect.length != names.length) {
                    throw new RuntimeException("result not mathch palce check");
                }

                compareTimes++;
                isTestCase = testGroup == null ? true : testGroup[0] <= compareTimes && compareTimes <= testGroup[1];
                if (!isTestCase) {
                    continue;
                }


                for (int index = 0; index < names.length; index++) {

                    String name = names[index];

                    if (StringUtils.isEmpty(name)) {
                        continue;
                    }


                    if (isTest) {
                        System.out.println();
                        System.out.println("name   =   " + name);
                        System.out.println("args   =   " + args[index]);
                        System.out.println("exp    =   " + expect[index]);
                        continue;
                    }

                    if (name.equals(className)) {
                        //  构造函数实例化
                        try {
                            result = new ArrayList<>();
                            ReflectUtils.handlerConstructorMethodInput(args[index], result);
                            obj = handlerConstructorMethod(constructor, result, src);
                        } catch (Exception e) {
                            e.printStackTrace();
                            obj = null;
                        }
                    } else {
                        Objects.requireNonNull(obj, "obj is null");
                        Method method = map.get(name);
                        if (!map.containsKey(name)) {
                            throw new RuntimeException("not find method " + name + ", place check your format !");
                        }
                        result = new ArrayList<>();
                        ReflectUtils.handlerConstructorMethodInput(args[index], result, method);
                        ReflectUtils.handlerConstructorMethodOutput(expect[index], result, method);
                        boolean isOk = startValid(obj, map.get(name), result, isStrict, false);
                        if (!isOk) {
                            String errorInfo = "Run CompareTimes :  " + compareTimes + "\nCall Method      :  " + name + "\nArgs Index       :  " + index + "\nArgs             :  " + args[index];
                            errorTimes.add(errorInfo + "\n");
                        }
                    }
                }

                // clear
                args = names = expect = null;
                result = null;
            }
        }

        if (testData != null && !StringUtils.isEmpty(testData.info)) {
            System.out.println(testData.info);
        }
        if (errorTimes.size() == 0) {
            System.out.println("Accepted!");
        } else {
            for (String errorInfo : errorTimes) {
                System.out.println(errorInfo);
            }
        }
    }

    /**
     * Invokes the specified constructor with the provided input arguments and returns the instantiated object.
     * This method handles both parameterless constructors and constructors requiring parameters.
     * It processes the input list to match the constructor's parameter types, parsing each argument accordingly.
     *
     * @param constructor the constructor to be invoked, which may have zero or more parameters
     * @param inputList   a list of strings representing the input arguments to be parsed and passed to the constructor
     * @param aclass      the class associated with the context in which the constructor is being invoked
     * @return the instantiated object if successful, or null if an exception occurs during instantiation or argument parsing
     */
    public static Object handlerConstructorMethod(Constructor<?> constructor, List<String> inputList, Class<?> aclass) {
        Object obj = null;
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 0) {
            try {
                obj = constructor.newInstance();
                return obj;
            } catch (Exception e) {
                return null;
            }
        }
        Object[] args = null;

        int size = inputList.size();
        String read = null;
        for (int idx = 0; idx < size; ) {
            args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length && idx < size; i++, idx++) {
                // 允许答案和输入参数之间有间隙
                while (idx < size && ((read = inputList.get(idx)) == null || read.isEmpty())) {
                    idx++;
                }
                if (idx == size) {
                    break;
                }
                if (read == null || read.isEmpty()) {
                    throw new RuntimeException("result not match place check your ans !");
                }
                args[i] = ReflectUtils.parseArg(aclass, constructor.getName(), parameterTypes[i], read, i, parameterTypes.length);
                read = null;
            }

            try {
                obj = constructor.newInstance(args);
            } catch (Exception ignore) {
                obj = null;
            }
        }
        return obj;


    }


    /**
     * Validates and tests the execution of a given method against a list of input data.
     * This method performs parameter validation, method invocation, and result comparison.
     * It supports both static and instance methods, and can handle void return types by
     * analyzing modified parameters. Errors and exceptions during execution are tracked,
     * and the method provides feedback on test results.
     *
     * @param obj The object on which the method is invoked. Must not be null.
     *            If the method is static, this parameter is ignored.
     * @param method run method
     * @param inputList  input content
     * @param isStrict  strict mode
     * @param newObj  every test cast will new a object
     * @return true valid ok
     */
    public static boolean startValid(Object obj, Method method, List<String> inputList, boolean isStrict, boolean newObj) {
        Objects.requireNonNull(obj, "obj is null");
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> srcClass = obj.getClass();
        Class<?> origin = ReflectUtils.loadOrigin(obj.getClass());
        Object[] args = null;
        String returnName = method.getReturnType().getSimpleName();
        final String tempRetrunName = returnName;
        int size = inputList.size();
        String read = null;

        TestData testData = newObj ? new TestData(method, origin, srcClass) : null;

        int[] testCaseInfo = testData == null || testData.testCaseGroup == null ? new int[]{1, 0x3f3f3f} : testData.testCaseGroup;


        int typeId = -2; // 如果返回值是空类型 而且需要比较的标志

        boolean isStatic = Modifier.isStatic(method.getModifiers());

        // boolean isConstrunctorClass = !origin.getSimpleName().equals(obj.getClass().getSimpleName());

        List<Integer> errorTimes = new ArrayList<>();
        int exceptionTime = -1;
        int compareTimes = 1;
        boolean isStartTest = false;
        for (int idx = 0; idx < size; ) {

            if (compareTimes > testCaseInfo[1]) {
                break;
            }

            // 是否在测试范围内
            isStartTest = testCaseInfo[0] <= compareTimes && compareTimes <= testCaseInfo[1];

            if (isStartTest) {
                if (newObj && !isStatic) {
                    // 如果不是构造类型对拍，定义普通类型属性会影响下次对拍 因此重新初始化
                    // 就是上次数据影响这次对拍
                    // example: leetcode.everyday.Code_0049_39
                    obj = ReflectUtils.initObjcect(srcClass, null);
                    Objects.requireNonNull(obj, "obj is null");
                }
            }


            // 填充参数信息
            boolean isFill = false; // 参数校验标志信息

            if (parameterTypes.length == 0) {
                while (idx < size && ((read = inputList.get(idx)) == null)) {
                    idx++;
                }
                if (VOID_OR_ARGS.equals(read) || Objects.requireNonNull(read).isEmpty()) {
                    isFill = true;
                    read = null;
                    idx++;
                } else {
                    throw new RuntimeException("NO args fill, should null");
                }
            } else {
                args = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length && idx < size; i++, idx++) {
                    // 允许答案和输入参数之间有间隙
                    while (idx < size && ((read = inputList.get(idx)) == null || read.isEmpty())) {
                        idx++;
                    }
                    if (idx == size) {
                        break;
                    }
                    if (read == null || read.isEmpty()) {
                        throw new RuntimeException("result not match place check your ans !");
                    }
                    isFill = true;
                    if (isStartTest) {
                        args[i] = ReflectUtils.parseArg(origin, method.getName(), parameterTypes[i], read, i, parameterTypes.length);
                    }
                    read = null;
                }

            }


            if (idx >= size) {
                if (isFill) {
                    System.out.println("place check result match");
                    errorTimes.add(compareTimes);
                }
                break;
            }

            // 分析该方法执行参数信息
            Object result = null;

            try {
                if (isStartTest) {
                    if (parameterTypes.length == 0) {
                        result = method.invoke(isStatic ? null : obj);
                    } else {
                        result = method.invoke(isStatic ? null : obj, args);
                    }
                }

                // 允许答案和输入参数之间有间隙
                while (idx < inputList.size() && ((read = inputList.get(idx)) == null || read.isEmpty())) {
                    idx++;
                }
                // 结果不匹配
                if (read == null) {
                    if ("string".equalsIgnoreCase(returnName)) {
                        read = "";
                    } else {
                        throw new RuntimeException("result not match place check your ans !");
                    }
                }

                if (!isStartTest) {
                    returnName = tempRetrunName; // origin return name
                    idx++; // match ok
                    compareTimes++; // 比较次数
                    continue;
                }

                if ("void".equalsIgnoreCase(returnName) && result == null) {

                    // 如果结果为null说明只是调用改方法 并且返回值为 void
                    // 这次就不参与比较了
                    if (VOID_OR_ARGS.equals(read)) {
                        idx++; // match ok
                        compareTimes++; // 比较次数
                        continue;
                    }

                    //  没有返回值时候如何处理呢 ？
                    if (args != null && args.length > 0) {
                        // 先处理成不是基本数据类型 因为基本数据类型是值传递 无法比较
                        if (typeId == -2) {
                            typeId = ReflectUtils.handlerVoidReturnType(parameterTypes);
                        }
                        if (typeId == -1) {
                            throw new RuntimeException("unkonwn compare type");
                        }
                        returnName = parameterTypes[typeId].getSimpleName();
                        result = args[typeId];
                    }
                }

                Object expect = ReflectUtils.parseArg(origin, method.getName(), returnName, read, -1, -1);
                if (expect != null && !TestUtils.valid(result, expect, returnName, isStrict, true)) {
                    // 非构造类才输出错误信息
                    if (newObj) {
                        System.out.println("compare " + compareTimes + " is Error , Run Method Name : " + method.getName() + "\n"); // save error
                    }
                    errorTimes.add(compareTimes);
                }
                args = null;
                read = null;
            } catch (Exception ignore) {
                exceptionTime = compareTimes;
                break;
            }
            returnName = tempRetrunName; // origin return name
            idx++; // match ok
            compareTimes++; // 比较次数
        }


        if (newObj && !StringUtils.isEmpty(testData.info)) {
            System.out.println(testData.info);
        }

        if (errorTimes.isEmpty() && exceptionTime == -1 && newObj) {
            System.out.println("Accepted!");
        } else {
//            for (int error : errorTimes) {
//                System.out.println("compare " + error + " is Error ,place check your program");
//            }
            if (exceptionTime != -1) {
                System.out.println("exception times :" + exceptionTime);
            }
        }
        return errorTimes.isEmpty() && exceptionTime == -1;
    }


    /**
     * Searches for a method with the specified name within the given class.
     * If the provided method name matches certain predefined values (e.g., "main" or DEFAULT_METHOD_NAME),
     * the method attempts to identify an alternative method name dynamically.
     * The method iterates through all declared methods of the class, excluding "main" and lambda methods,
     * and returns the first matching method after setting it accessible.
     *
     * @param src        the class in which the method is to be searched
     * @param methodName the name of the method to find; if it matches "main" or DEFAULT_METHOD_NAME,
     *                   the method will attempt to resolve an alternative name
     * @return the found method if it exists and matches the criteria; null if no such method is found
     */
    public static Method findMethodName(Class<?> src, String methodName) {
        boolean find = false;
        Method[] methods = src.getDeclaredMethods();

        List<String> names = new ArrayList<>();
        for (Method method : methods) {
            names.add(method.getName());
        }

        if (!names.isEmpty() && DEFAULT_METHOD_NAME.equals(methodName) || "main".equals(methodName)) {
            for (String name : names) {
                if (name.equals("main") || name.startsWith("lambda$")) {
                    continue;
                }
                methodName = name;
            }
        }

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            if ("main".equals(method.getName())) {
                continue;
            }
            find = true;
            method.setAccessible(true);
            return method;
        }
        return null;
    }


    /**
     * Reads the contents of a file and returns them as a list of strings.
     * The method constructs the absolute path to the file using the provided class and filename,
     * and processes the file based on the specified flag for long content handling.
     *
     * @param c               the Class object used to determine the origin for building the absolute path
     * @param filename        the name of the file to be read
     * @param openLongContent a boolean flag indicating whether to handle long content in the file
     * @return a List of Strings representing the lines of the file
     */
    public static List<String> readFile(Class<?> c, String filename, boolean openLongContent) {
        return readFile(buildAbsolutePath(ReflectUtils.loadOrigin(c)), filename, openLongContent);
    }

    /**
     * Reads the contents of a file into a list of strings.
     * The file is located using the provided class and filename.
     *
     * @param c        the Class object used to determine the base path for the file
     * @param filename the name of the file to be read
     * @return a List of Strings containing the lines of the file
     */
    public static List<String> readFile(Class<?> c, String filename) {
        return readFile(buildAbsolutePath(ReflectUtils.loadOrigin(c)), filename, false);
    }

    /**
     * Reads the contents of a file from the specified path and file name.
     *
     * @param path     the directory path where the file is located
     * @param fileName the name of the file to be read
     * @return a list of strings representing the lines in the file
     */
    public static List<String> readFile(String path, String fileName) {
        return readFile(path, fileName, false);
    }


    /**
     * Reads the content of a file and returns it as a list of strings.
     * If the file does not exist, an error message is printed and null is returned.
     * If the file exists, its content is read line by line unless the file is identified
     * as containing long content, in which case a specialized parsing method is invoked.
     *
     * @param path            the directory path where the file is located
     * @param fileName        the name of the file to be read
     * @param openLongContent a flag indicating whether the file contains long content
     *                        that requires specialized parsing
     * @return a list of strings representing the lines of the file,
     * or null if the file does not exist or an error occurs during reading
     */
    public static List<String> readFile(String path, String fileName, boolean openLongContent) {
        File file = new File(path + fileName);
        if (!file.exists()) {
            System.out.println(fileName + " not found!");
            return null;
        }

        BufferedReader breder = null;
        BufferedInputStream bis = null;
        List<String> ans = new ArrayList<>();
        try {
            if (openLongContent) {
                return parseShpInfo(file);
            } else {
                breder = new BufferedReader(new FileReader(file));
                String t = null;
                while ((t = breder.readLine()) != null) {
                    ans.add(t);
                }
            }

        } catch (Exception e) {
            System.err.println("parse failed " + e.getMessage());
        } finally {
            close(breder);
            close(bis);
        }
        // System.out.println("read content = >" + ans);
        return ans;
    }


    /**
     * Constructs the absolute path for a given class by appending its package path to the base absolute path.
     * The resulting path ends with the system's file separator.
     *
     * @param c the Class object for which the absolute path is to be constructed
     * @return the constructed absolute path as a String, including the package path and file separator
     */
    public static String buildAbsolutePath(Class<?> c) {
        return buildAbsolutePath() + getPackagePath(c) + File.separator;
    }


    /**
     * Constructs and returns the absolute path by utilizing the project's root directory.
     * This method internally calls buildAbsolutePath with the root directory obtained
     * from getProjectRootDir as its parameter.
     *
     * @return the absolute path derived from the project's root directory
     */
    public static String buildAbsolutePath() {

        return buildAbsolutePath(getProjectRootDir());
    }

    /**
     * Constructs an absolute path by combining the working directory with the provided base directory.
     *
     * @param baseDir the base directory to be appended to the working directory
     * @return the constructed absolute path as a String
     */
    public static String buildAbsolutePath(String baseDir) {
        String wordDir = getWorkDir();
        return wordDir + baseDir;
    }

    /**
     * Returns the package path of the given class as a string with dots replaced by the system's file separator.
     *
     * @param c the Class object whose package path is to be retrieved
     * @return the package path of the class in the format suitable for file system representation,
     * or an empty string if the class does not belong to any package
     */
    public static String getPackagePath(Class<?> c) {
        Package pkg = c.getPackage();
        if (pkg == null) return "";
        String info = pkg.getName();
        info = info.replace("package ", "");
        char[] cs = info.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == '.') {
                cs[i] = File.separator.charAt(0);
            }
        }
        return new String(cs);
    }


    /**
     * Retrieves the current working directory of the application.
     * This method first attempts to obtain the working directory using the system property "user.dir".
     * If the property is not available, is "null", or is an empty string, it falls back to determining
     * the absolute path of the current directory via a newly instantiated File object.
     *
     * @return the absolute path of the current working directory as a String
     */
    public static String getWorkDir() {
        String dir = System.getProperty("user.dir");
        if (StringUtils.isEmpty(dir) || "null".equals(dir)) {
            dir = new File("").getAbsolutePath();
        }
        return dir;
    }


    /**
     * Validates that the provided parameters are not null.
     * This method checks the source class, method name, and file name to ensure
     * they are non-null. If any parameter is null, a NullPointerException is thrown
     * with an appropriate error message.
     *
     * @param src        the source class object to be validated; must not be null
     * @param methodName the name of the method to be validated; must not be null
     * @param fileName   the name of the file to be validated; must not be null
     */
    public static void check(Class<?> src, String methodName, String fileName) {
        Objects.requireNonNull(src, "className not null");
        Objects.requireNonNull(methodName, "methodName not null");
        Objects.requireNonNull(fileName, "fileName  not null");
    }

    /**
     * Closes the given AutoCloseable resources silently, ignoring any exceptions that occur during the close operation.
     * This method is useful for ensuring that multiple resources are closed without throwing exceptions.
     * If a null value is encountered in the array, it is skipped.
     *
     * @param closeables a variable number of AutoCloseable objects to be closed
     */
    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }


    /**
     * Parses a given file to extract information enclosed within '#' symbols.
     * The method reads the content of the file, searches for patterns matching
     * the format #content#, and returns a list of all such matches.
     *
     * @param file the file to be parsed. If the file is null or does not exist,
     *             the method will print an error message and return null.
     * @return a list of strings containing the matched content extracted from
     * the file. If no matches are found, an empty list is returned after
     * printing an error message. Returns null if the file is invalid.
     */
    public static List<String> parseShpInfo(File file) {
        if (file == null || !file.exists()) {
            if (file != null) System.out.println(file.getName() + " is null ,place check exist !");
            return null;
        }
        List<String> ans = new ArrayList<>();
        BufferedInputStream bis = null;
        try {
            byte[] buff = new byte[1024 * 1024];
            StringBuilder sb = new StringBuilder();
            bis = new BufferedInputStream(Files.newInputStream(file.toPath()));
            while ((bis.read(buff)) != -1) {
                sb.append(new String(buff));
            }

            String input = sb.toString();

            Pattern pattern = Pattern.compile("#([^#]+)#", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(input);
            boolean find = false;
            while (matcher.find()) {
                // System.out.println(times + " @@@@=>" + matcher.group(1));
                ans.add(matcher.group(1));
                find = true;
            }
            if (!find) {
                System.out.println("find error place use #content# package your content ");
            }
        } catch (IOException ignore) {

        } finally {
            close(bis);
        }
        return ans;
    }



    private static final String defaultContent = "List<String>";


    /**
     * Searches for a method within a specified class file and returns either the return type
     * or a specific argument type based on the provided parameters.
     *
     * @param c          the Class object representing the class to search within
     * @param name       the name of the method to find; may include special characters like '$'
     * @param returnType the expected return type of the method being searched
     * @param idx        the index of the argument to retrieve; if -1, the return type is processed instead
     * @param argsSize   the expected number of arguments in the method signature for validation
     * @return a String representing either the return type or the type of the argument at the specified index
     */
    public static String findListReturnTypeMethod(Class<?> c, String name, String returnType, int idx, int argsSize) {
        if (c == null) {
            System.out.println("error t is null");
            return defaultContent;
        }
        c = ReflectUtils.loadOrigin(c);
        String path = buildAbsolutePath(c) + c.getSimpleName() + ".java";
        File file = new File(path);
        if (!file.exists()) {
            System.out.println(file.getAbsolutePath() + "Not found !");
            return defaultContent;
        }
        BufferedReader bis = null;
        String s = null;
        // fix LC bi week 147 contest bug
        if (name.contains("$")) {
            String[] split = name.split("\\$");
            name = split[split.length - 1];
        }
        String matchMethodName = name + "(";
        try {
            bis = new BufferedReader(new FileReader(file));
            while ((s = bis.readLine()) != null) {
                if (s.contains(matchMethodName)) {
                    break;
                }
            }
        } catch (Exception ignore) {

        } finally {
            close(bis);
        }

        if (s == null) {
            System.out.println("not find + " + name + " method");
            return defaultContent;
        }
        StringBuilder sb = new StringBuilder();
        Stack<Character> sk = new Stack<>();
        int st = -1;
        if (idx == -1) {
            st = s.indexOf(returnType);
            if (st == -1) {
                throw new RuntimeException("not find " + returnType + " as return type !");
            }
            st += returnType.length();
            sb.append(returnType);
            for (int i = st; i < s.length(); i++) {
                char cr = s.charAt(i);
                if (sk.isEmpty() && cr == ' ') break;
                if (cr == '[') {
                    sk.push(cr);
                } else if (cr == ']') {
                    if (!sk.isEmpty()) {
                        sk.pop();
                    }
                }
                sb.append(cr);
            }
            return sb.toString();
        } else {
            // 根据方法名查找第一个参数
            st = s.indexOf(matchMethodName);
            if (st == -1) throw new RuntimeException("not find methodName");
            st += name.length();
            List<String> argsList = new ArrayList<>();
            for (int i = st; i < s.length(); i++) {
                char chr = s.charAt(i);
                if (chr == ' ') {
                    if (sb != null && !sb.toString().isEmpty()) {
                        argsList.add(sb.toString());
                        sb = null;
                    }
                    continue;
                }
                if (chr == '(') {
                    sb = new StringBuilder();
                    sk.push(chr);
                } else if (chr == ')') {
                    if (sb != null) {
                        argsList.add(sb.toString());
                        sb = null;
                    }
                    if (!sk.isEmpty()) {
                        sk.pop();
                    }
                    if (sk.isEmpty()) break;
                } else if (chr == ',') {
                    sb = new StringBuilder();
                } else {
                    if (sb != null) {
                        sb.append(chr);
                    }
                }
            }
            if (idx >= argsList.size()) throw new RuntimeException("not find " + returnType);
            if (argsSize != argsList.size()) throw new RuntimeException("args size not match place check!");
            return argsList.get(idx);
        }


    }


    /**
     * Constructs an absolute path by appending the provided directory to the absolute path of the given class if necessary.
     * If the provided directory is already an absolute path, it is returned as-is.
     *
     * @param c   the Class object used to determine the base absolute path when needed
     * @param dir the directory path to be checked or appended; must not be null
     * @return the resulting absolute path, either the original directory if it was already absolute,
     * or the combination of the class's absolute path and the provided directory
     */
    public static String wrapperAbsolutePath(Class<?> c, String dir) {
        Objects.requireNonNull(dir, "dir Not allow null");
        return isAbsolutePath(dir) ? dir : IoUtil.buildAbsolutePath(c) + dir;
    }

    /**
     * Checks if the given directory path is an absolute path.
     *
     * @param dir the directory path to check; can be null or empty
     * @return true if the path is absolute, false otherwise
     */
    public static boolean isAbsolutePath(String dir) {
        if (StringUtils.isEmpty(dir)) {
            return false;
        }
        return dir.charAt(0) == File.separator.charAt(0) || dir.charAt(0) == '/' || dir.length() >= 2 && dir.charAt(1) == ':';
    }


    /**
     * Reads the content of a file specified by the given path relative to the provided class.
     * The method first resolves the absolute path using the wrapperAbsolutePath method
     * and then reads the content using the readContent method.
     *
     * @param c    the class used as a reference point to resolve the relative path
     * @param path the relative or absolute path to the file whose content is to be read
     * @return the content of the file as a String
     */
    public static String readContent(Class<?> c, String path) {
        path = wrapperAbsolutePath(c, path);
        return readContent(path);
    }

    /**
     * Reads the content from a file located at the specified path.
     *
     * @param path the path to the file whose content is to be read
     * @return the content of the file as a String
     */
    public static String readContent(String path) {
        return readContent(new File(path));
    }


    /**
     * Reads the entire content of a file into a string.
     * If the file does not exist, an empty string is returned.
     * If an IOException occurs during reading, the exception is logged, and an empty string is returned.
     *
     * @param file the file to read content from
     * @return the content of the file as a string, or an empty string if the file does not exist or an error occurs
     */
    public static String readContent(File file) {
        BufferedInputStream bis = null;
        StringBuilder sb = null;
        try {
            if (!file.exists()) {
                return "";
            }
            bis = new BufferedInputStream(Files.newInputStream(file.toPath()));
            sb = new StringBuilder();
            byte[] buf = new byte[1024 * 1024];
            int len = -1;
            while ((len = bis.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
            return sb.toString();
        } catch (IOException ignore) {
            return "";
        } finally {
            close(bis);
        }

    }


    /**
     * Writes the specified content to a file located at the given URL path.
     * The URL path is first processed to determine its absolute path using the provided class and wrapper method.
     *
     * @param c       the Class object used to resolve the absolute path of the URL
     * @param url     the relative or absolute URL path where the content will be written
     * @param content the content to be written to the file at the specified URL path
     */
    public static void writeContent(Class<?> c, String url, String content) {
        url = wrapperAbsolutePath(c, url);
        writeContent(url, content);
    }


    /**
     * Writes the specified content to a file located at the given URL path.
     *
     * @param url     the path to the file where the content will be written; this is treated as a file path
     * @param content the data to be written into the file
     */
    public static void writeContent(String url, String content) {
        writeContent(new File(url), content);
    }

    /**
     * Writes the specified content to the given file using UTF-8 encoding.
     * If the file does not exist, it will be created.
     * The method ensures that the output stream is properly closed after writing.
     *
     * @param file    the file to which the content will be written
     * @param content the string content to write to the file
     */
    public static void writeContent(File file, String content) {
        BufferedOutputStream bos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            bos.write(content.getBytes(StandardCharsets.UTF_8));
            bos.flush();
        } catch (IOException ingore) {

        } finally {
            IoUtil.close(bos);
        }
    }


    /**
     * Creates a new file associated with the given class and filename.
     * The method resolves the absolute path of the file using the provided class and filename,
     * then delegates the file creation process to an internal method.
     *
     * @param c        the class used to determine the base path for the file
     * @param filename the name of the file to be created
     * @return the newly created File object
     */
    public static File createFile(Class<?> c, String filename) {
        filename = wrapperAbsolutePath(c, filename);
        return createFile(filename);
    }


    /**
     * Creates a new file with the specified file name. If the parent directories
     * of the file do not exist, they are created. If the file already exists,
     * the method returns null.
     *
     * @param fileName the name of the file to be created, including its path
     * @return the File object representing the newly created file, or null if
     * the file already exists or an error occurs during creation
     */
    public static File createFile(String fileName) {

        try {
            File file = new File(fileName);
            String parent = file.getParent();
            if (parent == null) {
                return file;
            }
            File parentFile = new File(parent);
            if (!parentFile.exists()) parentFile.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
                return file;
            }
            //System.out.println(fileName + " is exists create fail");
            return null;
        } catch (Exception ignore) {
            return null;
        }
    }


}
