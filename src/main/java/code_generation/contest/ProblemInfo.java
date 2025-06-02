package code_generation.contest;

import code_generation.utils.IoUtil;
import code_generation.utils.ReflectUtils;
import code_generation.utils.StringUtils;

import java.io.File;

/**
 * A container class that holds all information needed to create a coding problem,
 * including file paths, test cases, and class templates.
 * @author: wuxin0011
 * @since 1.0
 */
public class ProblemInfo {

    /** Path to the Java source file */
    private String javaFile;

    /** Path to the associated text file (typically for test cases) */
    private String txtFile;

    /** Test case content */
    private String testCase;

    /** Template for class generation */
    private ClassTemplate classTemplate;

    /** Reference class used for path resolution */
    private Class<?> aClass;

    /** Directory prefix for file organization */
    private String dirPrefix;

    /**
     * Updates and normalizes file paths based on current settings.
     *
     * @param javaFile the Java file path to process
     * @param txtFile the text file path to process
     */
    private void updateDir(String javaFile, String txtFile) {
        // Normalize Java file extension
        if (javaFile.endsWith(".txt")) {
            javaFile = javaFile.replace(".txt", ".java");
        }
        if (!javaFile.endsWith(".java")) {
            javaFile = javaFile + ".java";
        }

        // Normalize text file extension
        if (txtFile.endsWith(".java")) {
            txtFile = txtFile.replace(".java", ".txt");
        }
        if (!txtFile.endsWith(".txt")) {
            txtFile = txtFile + ".txt";
        }

        // Update class name in template if needed
        if (classTemplate != null) {
            String tempJavaName = javaFile.replace(".java", "");
            if (tempJavaName.contains("\\")) {
                String[] split = tempJavaName.split("\\\\");
                tempJavaName = split[split.length - 1]; // Use last segment as class name
            }
            if (StringUtils.isEmpty(classTemplate.className) || !tempJavaName.equals(classTemplate.className)) {
                classTemplate.buildClassName(tempJavaName);
            }
        }

        // Update paths with directory prefix and absolute paths
        this.javaFile = IoUtil.wrapperAbsolutePath(aClass,
                StringUtils.isEmpty(dirPrefix) ? javaFile : (dirPrefix + File.separator + javaFile));
        this.txtFile = IoUtil.wrapperAbsolutePath(aClass,
                StringUtils.isEmpty(dirPrefix) ? txtFile : (dirPrefix + File.separator + txtFile));

        // Update package info if not set
        if (classTemplate != null && StringUtils.isEmpty(classTemplate.packageInfo)) {
            classTemplate.buildPackageInfo(ReflectUtils.getPackageInfo(this.javaFile));
        }
    }

    /**
     * Constructs a ProblemInfo with all parameters.
     *
     * @param javaFile path to Java file
     * @param txtFile path to text file
     * @param dirPrefix directory prefix
     * @param testCase test case content
     * @param classTemplate class template
     * @param aClass reference class for path resolution
     */
    public ProblemInfo(String javaFile, String txtFile, String dirPrefix, String testCase,
                       ClassTemplate classTemplate, Class<?> aClass) {
        this.aClass = aClass;
        this.dirPrefix = dirPrefix;
        this.testCase = testCase;
        this.classTemplate = classTemplate;
        updateDir(javaFile, txtFile);
    }

    /** Default constructor */
    public ProblemInfo() {}

    /**
     * Constructs a ProblemInfo without directory prefix.
     *
     * @param javaFile path to Java file
     * @param txtFile path to text file
     * @param testCase test case content
     * @param classTemplate class template
     * @param aClass reference class for path resolution
     */
    public ProblemInfo(String javaFile, String txtFile, String testCase,
                       ClassTemplate classTemplate, Class<?> aClass) {
        this(javaFile, txtFile, "", testCase, classTemplate, aClass);
    }

    // Property accessors with appropriate documentation

    /**
     * Sets the Java file path and updates related paths.
     * @param javaFile new Java file path
     */
    public void setJavaFile(String javaFile) {
        updateDir(javaFile, txtFile);
    }

    /**
     * Sets the text file path and updates related paths.
     * @param txtFile new text file path
     */
    public void setTxtFile(String txtFile) {
        updateDir(javaFile, txtFile);
    }

    /**
     * Sets the test case content.
     * @param testCase test case content
     */
    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    /**
     * Sets the class template.
     * @param classTemplate class template to use
     */
    public void setClassTemplate(ClassTemplate classTemplate) {
        this.classTemplate = classTemplate;
    }

    /**
     * Sets the reference class for path resolution.
     * @param aClass reference class
     */
    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    /**
     * @return current directory prefix
     */
    public String getDirPrefix() {
        return dirPrefix;
    }

    /**
     * Sets the directory prefix and updates paths.
     * @param dirPrefix new directory prefix
     */
    public void setDirPrefix(String dirPrefix) {
        this.dirPrefix = dirPrefix;
        updateDir(javaFile, txtFile);
    }

    /**
     * @return current Java file path
     */
    public String getJavaFile() {
        return javaFile;
    }

    /**
     * @return current text file path
     */
    public String getTxtFile() {
        return txtFile;
    }

    /**
     * @return current test case content
     */
    public String getTestCase() {
        return testCase;
    }

    /**
     * @return current class template
     */
    public ClassTemplate getClassTemplate() {
        return classTemplate;
    }

    /**
     * @return current reference class
     */
    public Class<?> getaClass() {
        return aClass;
    }

    /**
     * Displays all problem information in a formatted way.
     * @return empty string (legacy behavior)
     */
    @Override
    public String toString() {
        System.out.println("\n====================================java file=========================\n");
        System.out.println(javaFile);
        System.out.println("\n====================================txt file=========================\n");
        System.out.println(txtFile);
        System.out.println("\n====================================template=========================\n");
        System.out.println(classTemplate);
        System.out.println("\n====================================template=========================\n");
        System.out.println(testCase);
        return "";
    }
}