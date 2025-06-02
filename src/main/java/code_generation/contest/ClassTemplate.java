package code_generation.contest;

import code_generation.utils.IoUtil;
import code_generation.utils.StringUtils;
/**
 * A template class for generating Java source code files with standardized formatting,
 * including package declaration, imports, class structure, and metadata.
 *
 * <p>This class provides a builder-style interface for configuring various aspects of
 * the generated code template, including author information, method implementation,
 * class name, and other metadata.</p>
 * @author: wuxin0011
 * @since 1.0
 */
public class ClassTemplate {

    /** Default author name used when none is specified */
    private final static String default_author = "wuxin0011";
    /** Default method name used when none is specified */
    private final static String default_methodName = "IoUtil.DEFAULT_METHOD_NAME";
    /** Default constructor method name used when generating constructor classes */
    private final static String default_constructor_methodName = "ParseCodeInfo.ConstructorClass";
    /** Default import statement for constructor classes */
    private final static String default_constructor_info = "import code_generation.contest.ParseCodeInfo;\n";
    /** Default class name used when none is specified */
    private final static String default_className = "Solution";
    /** MOD constant declaration string */
    private final static String MOD = "private static final int MOD = (int)1e9 + 7;";

    /** Author of the generated code */
    public String author;
    /** Method implementation code */
    public String method;
    /** Name of the method */
    public String methodName;
    /** Name of the class */
    public String className;
    /** Title/description of the problem */
    public String title;
    /** URL reference for the problem */
    public String url;
    /** Name of the text file for I/O operations */
    public String textFileName;
    /** Package declaration */
    public String packageInfo;
    /** Import statements */
    public String importInfo;
    /** Flag indicating whether MOD constant should be included */
    public boolean isNeedMod = false;

    /** Information about parsed code for constructor classes */
    public ParseCodeInfo codeInfo;

    /**
     * Constructs a new ClassTemplate with default values.
     */
    public ClassTemplate() {
        this.method = "";
        this.methodName = default_methodName;
        this.className = default_className;
        this.author = default_author;
        this.textFileName = StringUtils.wrapperKey(IoUtil.DEFAULT_READ_FILE);
        this.title = "";
        this.url = "";
        this.packageInfo = "";
        this.importInfo = "";
    }

    /**
     * Sets the method implementation and automatically detects TreeNode or ListNode imports.
     *
     * @param method The method implementation code
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildMethod(String method) {
        this.method = method;
        int i = StringUtils.kmpSearch(method, "TreeNode");
        if(i!=-1){
            this.buildImportInfo("import code_generation.bean.TreeNode;\n");
            return this;
        }else{
            i = StringUtils.kmpSearch(method, "ListNode");
            if(i!=-1){
                this.buildImportInfo("import code_generation.bean.ListNode;\n");
            }
            return this;
        }
    }

    /**
     * Sets the method name.
     *
     * @param methodName The name of the method
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildMethodName(String methodName) {
        this.methodName = StringUtils.wrapperKey(methodName);
        return this;
    }

    /**
     * Sets the class name, handling various input formats.
     *
     * @param className The name of the class
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildClassName(String className) {
        if (StringUtils.isEmpty(className)) {
            className = default_className;
        }
        if (className.endsWith(".class")) {
            className = className.replace(".class", "");
        }
        if (className.endsWith(".java")) {
            className = className.replace(".java", "");
        }
        this.className = className;
        return this;
    }

    /**
     * Sets the problem title/description.
     *
     * @param title The title or description of the problem
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the reference URL for the problem.
     *
     * @param url The URL reference
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the author name.
     *
     * @param author The author name (uses default if empty)
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildAuthor(String author) {
        this.author = StringUtils.isEmpty(author) ? default_author : author;
        return this;
    }

    /**
     * Sets whether the MOD constant should be included.
     *
     * @param isNeedMod Flag indicating MOD constant inclusion
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildIsNeedMod(boolean isNeedMod) {
        this.isNeedMod = isNeedMod;
        return this;
    }

    /**
     * Sets the parsed code information for constructor classes.
     *
     * @param codeInfo The ParseCodeInfo instance
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildCodeInfo(ParseCodeInfo codeInfo) {
        this.codeInfo = codeInfo;
        return this;
    }

    /**
     * Sets the text file name for I/O operations.
     *
     * @param txtFile The text file name (handles extensions and formatting)
     * @return This ClassTemplate instance for method chaining
     */
    public ClassTemplate buildTextFileName(String txtFile) {
        if (StringUtils.isEmpty(txtFile)) {
            txtFile = IoUtil.DEFAULT_READ_FILE;
        }
        if (!txtFile.endsWith(".txt")) {
            txtFile = txtFile + ".txt";
        }
        this.textFileName = StringUtils.wrapperKey(txtFile);
        this.textFileName = this.textFileName.replace("\\", "\\\\");
        return this;
    }

    /**
     * Sets the package declaration.
     *
     * @param packageInfo The package name
     * @return This ClassTemplate instance for method chaining
     */
    @SuppressWarnings("all")
    public ClassTemplate buildPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
        return this;
    }

    /**
     * Sets the import statements.
     *
     * @param importInfo The import statements
     * @return This ClassTemplate instance for method chaining
     */
    @SuppressWarnings("all")
    public ClassTemplate buildImportInfo(String importInfo) {
        this.importInfo = importInfo;
        return this;
    }

    /** Template pattern for constructor classes */
    final static String CONSTRUCTOR_CLASS = "public static class %s { \n" +
            "    %s" +
            "\n\t}";

    /**
     * Creates a constructor class template string.
     *
     * @param className The name of the constructor class
     * @param method The method implementation
     * @return Formatted constructor class template
     */
    public static String createConstructorClassTemplate(String className, String method) {
        return String.format(CONSTRUCTOR_CLASS, className, method);
    }

    /** Main template pattern for generating Java source files */
    final static String TEMPLATE_PATTERN = "package %s;\n" +
            "\n" +
            "import code_generation.utils.IoUtil;\n" +
            "import code_generation.annotation.TestCaseGroup;\n" +
            "import java.util.*;\n" +
            "%s" +
            "/**\n" +
            " * @author: %s\n" +
            " * @Description:\n" +
            " * @url:   <a href=\"%s\">%s</a>\n" +
            " * @title: %s\n" +
            " */\n" +
            "//@TestCaseGroup(start = 1,end = 0x3fff,use = true)\n" +
            "public class %s {\n\n" +
            "    public static void main(String[] args) {\n" +
            "        IoUtil.testUtil(%s.class,%s,%s);\n" +
            "    }" +
            "\n" +
            "    %s " +
            "\n\n" +
            "    %s" +
            "  \n\n" +
            "}";

    /**
     * Generates the complete Java source code template based on the configuration.
     *
     * @param info The configured ClassTemplate instance
     * @return The complete Java source code as a string
     */
    public static String getTemplate(ClassTemplate info) {
        ParseCodeInfo codeInfo = info.codeInfo;
        String originClassName = info.className;
        boolean isNot = codeInfo == null || !codeInfo.isConstructor();
        String className = isNot ? info.className : codeInfo.getClassName();
        String method = isNot ? info.method : createConstructorClassTemplate(codeInfo.getClassName(), codeInfo.getMethod());
        String importInfo = isNot ? info.importInfo : info.importInfo + "\n" + default_constructor_info;
        String methodName = isNot ? info.methodName : default_constructor_methodName;
        return String.format(
                TEMPLATE_PATTERN,
                info.packageInfo,
                importInfo,
                info.author,
                info.url,
                info.title,
                info.title,
                originClassName,
                className,
                methodName,
                info.textFileName,
                info.isNeedMod ? MOD : "",
                method
        );
    }

    /**
     * Returns the complete Java source code template as a string.
     *
     * @return The generated template string
     */
    @Override
    public String toString() {
        return ClassTemplate.getTemplate(this);
    }
}
