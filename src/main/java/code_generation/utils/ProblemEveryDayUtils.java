package code_generation.utils;

import code_generation.contest.Constant;

import java.io.File;

/**
 * @author: wuxin0011
 * @Description:
 */
/**
 * A utility class for handling file paths and operations related to problem files.
 * Provides methods for building file paths, checking input validity, counting files,
 * and converting directory names.
 * @author wuxin0011
 * @since 1.0
 */
public class ProblemEveryDayUtils {

    /**
     * Builds a text file path by combining directory and name, ensuring proper formatting.
     *
     * @param dir The base directory path
     * @param name The file name (without extension)
     * @return The complete formatted text file path
     */
    public static String buildTxtFilePath(String dir, String name) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        if (name.contains(dir)) {
            name = name.replace(dir, "");
        }
        if (!name.endsWith(Constant.TXT_FILE_SUFFIX)) {
            name = name + Constant.TXT_FILE_SUFFIX;
        }
        if (!dir.contains(Constant.TXT_FILE_PREFIX)) {
            dir = dir + Constant.TXT_FILE_PREFIX + File.separator;
        }
        return dir + name;
    }

    /**
     * Builds a Java file path by combining directory and name, ensuring proper extension.
     *
     * @param dir The base directory path
     * @param name The file name (without extension)
     * @return The complete formatted Java file path
     */
    public static String buildJavaFilePath(String dir, String name) {
        if (!name.endsWith(Constant.JAVA_FILE_SUFFIX)) {
            name = name + Constant.JAVA_FILE_SUFFIX;
        }
        if (dir == null || dir.length() == 0) {
            return name;
        }
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        return dir + name;
    }

    /**
     * Checks if the input string is a valid integer number.
     *
     * @param id The string to validate
     * @return true if the input is a valid integer, false otherwise
     */
    public static boolean check(String id) {
        try {
            Integer.valueOf(id);
        } catch (Exception e) {
            System.out.println("place input a valid Number !");
            return false;
        }
        return true;
    }

    /**
     * Counts the number of Java files in the directory of the specified class.
     *
     * @param c The class whose directory will be scanned
     * @return The count of Java files found
     */
    public static int getJavaFileCount(Class<?> c) {
        String path = IoUtil.buildAbsolutePath(c);
        int count = countDirJavaFile(new File(path));
        return (int) count;
    }

    /**
     * Determines if a file is a valid Java file based on naming conventions.
     *
     * @param f The file to check
     * @return true if the file matches Java file naming conventions, false otherwise
     */
    public static boolean isJavaFile(File f) {
        if (f == null || !f.isFile()) return false;
        return f.getName().startsWith(Constant.FIlE_PREFIX) && f.getName().endsWith(Constant.JAVA_FILE_SUFFIX);
    }

    /**
     * Recursively counts Java files in a directory and its subdirectories.
     *
     * @param file The directory to scan
     * @return The total count of Java files found
     */
    public static int countDirJavaFile(File file) {
        if (file == null) {
            return 0;
        }
        int res = 0;
        if (isJavaFile(file)) {
            res++;
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            }
            for (File f : files) {
                if (isJavaFile(f)) {
                    res++;
                } else if (f.isDirectory()) {
                    res += countDirJavaFile(f);
                }
            }
        }
        return res;
    }

    /**
     * Converts a count number into a standardized directory name format.
     *
     * @param count The number to convert
     * @return A 4-digit string representation of the number (with leading zeros)
     */
    public static String convertDir(int count) {
        if (count >= 1000) {
            return String.valueOf(count);
        }
        if (count >= 100) {
            return "0" + count;
        }
        if (count >= 10) {
            return "00" + count;
        }
        return "000" + count;
    }

    /**
     * Creates a directory prefix based on the count, grouping files in hundreds.
     *
     * @param count The current count number
     * @return A formatted directory prefix string
     */
    public static String createPrefix(int count) {
        int baseDir = Math.max(0, count / 100 * 100);
        return Constant.DIR_PREFIX + (count < 100 ? "000" : String.valueOf(baseDir)) + File.separator;
    }
}