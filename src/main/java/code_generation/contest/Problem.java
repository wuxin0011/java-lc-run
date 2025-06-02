package code_generation.contest;

import code_generation.crwal.leetcode.LCContest;
import code_generation.utils.IoUtil;
import code_generation.utils.ReflectUtils;
import code_generation.utils.StringUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;
/**
 * Utility class for creating and managing coding problem templates and directories.
 * Provides methods for generating Java class files with associated test case files,
 * and organizing them in directory structures.
 * @author: wuxin0011
 * @since 1.0
 */
public class Problem {

    private static final String ROOT_DIR = IoUtil.getProjectRootDir();

    /**
     * Creates a problem template with the given class file path, test case, and code template.
     *
     * @param classFilePath path to the Java class file to create
     * @param testCase      test case content to include
     * @param code          code template to use
     */
    public static void createTemplate(String classFilePath, String testCase, String code) {
        createTemplate(new File(classFilePath), null, testCase, code);
    }

    /**
     * Creates a problem template with the given class file path.
     *
     * @param classFilePath path to the Java class file to create
     */
    public static void createTemplate(String classFilePath) {
        createTemplate(new File(classFilePath));
    }

    /**
     * Creates a problem template with the given File object.
     *
     * @param classFilePath File object representing the Java class file to create
     */
    public static void createTemplate(File classFilePath) {
        createTemplate(classFilePath, null);
    }

    /**
     * Handles text file creation and path processing.
     *
     * @param txtFile      path to the text file (can be null)
     * @param name         base name to use if txtFile is null
     * @param javaFilePath associated Java file
     * @return processed text file path
     */
    public static String handlerTxt(String txtFile, String name, File javaFilePath) {
        return handlerTxt(txtFile, name, javaFilePath, "");
    }

    /**
     * Handles text file creation with test case content.
     *
     * @param txtFile      path to the text file (can be null)
     * @param name         base name to use if txtFile is null
     * @param javaFilePath associated Java file
     * @param testCase     test case content to write
     * @return processed text file path
     */
    public static String handlerTxt(String txtFile, String name, File javaFilePath, String testCase) {
        if (txtFile == null) {
            txtFile = name;
        }
        if (txtFile.endsWith(".txt")) {
            txtFile = txtFile.replace(".txt", "");
        }
        String temp = IoUtil.isAbsolutePath(txtFile)
                ? (txtFile + ".txt")
                : javaFilePath.getParent() + File.separator + txtFile + ".txt";

        File file = IoUtil.createFile(temp);
        if (file == null) {
            return txtFile;
        }

        if (testCase != null && testCase.length() > 0) {
            IoUtil.writeContent(file, testCase);
        }

        if (javaFilePath != null && file.getAbsolutePath().contains(javaFilePath.getParent())) {
            txtFile = "." + file.getAbsolutePath().replace(javaFilePath.getParent(), "").replace("\\", "\\\\");
            txtFile = txtFile.replace(".\\\\", "");
        }

        if (txtFile.endsWith(".txt")) {
            txtFile = txtFile.replace(".txt", "");
        }
        return txtFile;
    }

    /**
     * Creates a problem template with the given File object and text file path.
     *
     * @param classFilePath File object representing the Java class file
     * @param txtFile       path to the associated text file
     */
    public static void createTemplate(File classFilePath, String txtFile) {
        createTemplate(classFilePath, txtFile, "", "");
    }

    /**
     * Creates a complete problem template with all components.
     *
     * @param classFilePath  File object representing the Java class file
     * @param txtFile        path to the associated text file
     * @param testCase       test case content
     * @param classTemplate  code template content
     */
    public static void createTemplate(File classFilePath, String txtFile, String testCase, String classTemplate) {
        try {
            String name = classFilePath.getName().replace(".java", "");
            txtFile = handlerTxt(txtFile, name, classFilePath, testCase);

            if (StringUtils.isEmpty(classTemplate)) {
                String packageInfo = ReflectUtils.getPackageInfo(classFilePath.getAbsolutePath());
                classTemplate = String.format(pattern, packageInfo, name, name, txtFile);
            }

            IoUtil.writeContent(classFilePath, classTemplate);
            System.out.println(classFilePath + "  create success !\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Default class template pattern
    final static String pattern = "package %s;\n" +
            "\n" +
            "import code_generation.utils.IoUtil;\n" +
            "import code_generation.annotation.TestCaseGroup;\n" +
            "import java.util.*;\n" +
            "/**\n" +
            " * @author: \n" +
            " * @Description:\n" +
            " * @url\n" +
            " * @title\n" +
            " */\n" +
            "//@TestCaseGroup(start = 1,end = 0x3fff,use = true)\n" +
            "public class %s {\n" +
            "    public static void main(String[] args) {\n" +
            "        IoUtil.testUtil(%s.class,IoUtil.DEFAULT_METHOD_NAME,\"%s.txt\");\n" +
            "    }\n" +
            "}\n";

    /**
     * Creates a directory name from an ID (0-25).
     *
     * @param id      the directory ID (0-25)
     * @param isUpper whether to use uppercase letters
     * @return single-character directory name
     * @throws RuntimeException if ID is out of range
     */
    public static String createDir(int id, boolean isUpper) {
        if (id < 0 || id >= 26) {
            throw new RuntimeException("Please input a number between 0-25");
        }
        char c = isUpper ? (char) (id + 'A') : (char) (id + 'a');
        return String.valueOf(c);
    }

    /**
     * Creates a directory path by combining a base directory and an ID-based subdirectory.
     *
     * @param id      the directory ID (0-25)
     * @param isUpper whether to use uppercase letters
     * @param dir     the base directory path
     * @return combined directory path
     */
    public static String createDir(int id, boolean isUpper, String dir) {
        return dir + File.separatorChar + createDir(id, isUpper);
    }

    /**
     * Creates multiple problem directories and templates.
     *
     * @param problems number of problems to create
     * @param dir      base directory path
     * @throws RuntimeException if directory exists but is not a directory
     */
    public static void createProblems(int problems, String dir) {
        File file = new File(dir);
        if (file.exists() && !file.isDirectory()) {
            throw new RuntimeException("File exists and is not a directory");
        }
        if (!file.exists()) file.mkdirs();

        for (int i = 0; i < problems; i++) {
            try {
                create(i, file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a problem from ProblemInfo object.
     *
     * @param info ProblemInfo containing all creation parameters
     */
    public static void create(ProblemInfo info) {
        File classFile = IoUtil.createFile(info.getJavaFile());
        createTemplate(classFile, info.getTxtFile(), info.getTestCase(), ClassTemplate.getTemplate(info.getClassTemplate()));
    }

    /**
     * Creates a problem with Java and text files relative to a class location.
     *
     * @param javaFilePath path to Java file
     * @param txtFilePath  path to text file
     * @param c            class used for relative path resolution
     */
    public static void create(String javaFilePath, String txtFilePath, Class<?> c) {
        javaFilePath = IoUtil.wrapperAbsolutePath(c, javaFilePath);
        txtFilePath = IoUtil.wrapperAbsolutePath(c, txtFilePath);
        create(javaFilePath, txtFilePath);
    }

    /**
     * Creates a problem with absolute Java and text file paths.
     *
     * @param javaFilePath absolute path to Java file
     * @param txtFilePath  absolute path to text file
     */
    public static void create(String javaFilePath, String txtFilePath) {
        File classFile = IoUtil.createFile(javaFilePath);
        if (classFile != null) {
            createTemplate(classFile, txtFilePath);
        }
    }

    /**
     * Gets a File object for a problem class file based on ID and path.
     *
     * @param id   problem ID (0-25)
     * @param path base directory path
     * @return File object for the Java file
     */
    public static File getClassFile(int id, String path) {
        File dirFile = new File(createDir(id, false, path));
        if (!dirFile.exists()) dirFile.mkdir();
        String prefix = createDir(id, true, dirFile.getAbsolutePath());
        return IoUtil.createFile(prefix + ".java");
    }

    /**
     * Creates a basic problem with ID and path.
     *
     * @param id   problem ID
     * @param path base directory path
     */
    public static void create(int id, String path) {
        create(id, path, "", "");
    }

    /**
     * Creates a problem with test case and code content.
     *
     * @param id       problem ID
     * @param path     base directory path
     * @param testCase test case content
     * @param code     code template content
     */
    public static void create(int id, String path, String testCase, String code) {
        File classFile = getClassFile(id, path);
        if (classFile != null) {
            createTemplate(classFile.getAbsolutePath(), testCase, code);
        }
    }

    /**
     * Creates a problem with a ClassTemplate object.
     *
     * @param id            problem ID
     * @param path          base directory path
     * @param testCase      test case content
     * @param classTemplate ClassTemplate object for code generation
     */
    public static void create(int id, String path, String testCase, ClassTemplate classTemplate) {
        File classFile = getClassFile(id, path);
        String packageInfo = ReflectUtils.getPackageInfo(classFile.getAbsolutePath());
        classTemplate.buildPackageInfo(packageInfo);
        String code = ClassTemplate.getTemplate(classTemplate);
        createTemplate(classFile.getAbsolutePath(), testCase, code);
    }

    /**
     * Creates a custom contest with current date in the directory structure.
     *
     * @param problems   number of problems
     * @param dirPrefix  directory prefix
     * @param dirName    base directory name
     * @param c          class for path resolution
     */
    public static void customContest(int problems, String dirPrefix, String dirName, Class<?> c) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();
        customContest(problems, year, month, day, dirPrefix, dirName, c);
    }

    /**
     * Interactive method to create a custom contest with user input.
     *
     * @param c class for path resolution
     * @throws RuntimeException if invalid input is provided multiple times
     */
    public static void customContest(Class<?> c) {
        Objects.requireNonNull(c, "Class parameter cannot be null");
        Scanner sc = new Scanner(System.in);
        int problems = 0;
        String dirPrefix = "";
        String dir = "";
        int count = 0;

        while (true) {
            if (count > 10) {
                break;
            }
            System.out.print("Please input a valid number as problems: ");
            try {
                problems = sc.nextInt();
                if (problems <= 0) {
                    count++;
                    continue;
                }
                break;
            } catch (Exception ignored) {
                count++;
                sc.next(); // Clear input buffer
            }
        }

        if (count > 10) {
            throw new RuntimeException("Maximum input attempts (10) exceeded");
        }

        System.out.print("Please input a valid string as dirPrefix: ");
        dirPrefix = sc.next();
        System.out.print("Please input a valid string as dir: ");
        dir = sc.next();
        Problem.customContest(problems, dirPrefix, dir, c);
    }

    /**
     * Creates a custom contest with full date specification.
     *
     * @param problems   number of problems
     * @param year       year for directory naming
     * @param month      month for directory naming
     * @param day        day for directory naming
     * @param dirPrefix  directory prefix
     * @param dirName    base directory name
     * @param c          class for path resolution (defaults to LCContest if null)
     */
    public static void customContest(int problems, int year, int month, int day, String dirPrefix, String dirName, Class<?> c) {
        if (c == null) {
            c = LCContest.class;
        }
        if (!dirPrefix.endsWith("_")) {
            dirPrefix = dirPrefix + "_";
        }
        dirName = IoUtil.wrapperAbsolutePath(c, dirName);

        StringBuilder sb = new StringBuilder();
        sb.append(dirName);
        sb.append(File.separator);
        sb.append(dirPrefix);
        sb.append(year).append("_");
        sb.append(month).append("_").append(day);
        sb.append(File.separator);

        String wrapperDir = sb.toString();
        createProblems(problems, wrapperDir);
    }
}
