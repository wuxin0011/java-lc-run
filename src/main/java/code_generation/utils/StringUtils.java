package code_generation.utils;

import code_generation.crwal.leetcode.BuildUrl;
import code_generation.crwal.leetcode.LCTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing various static methods for string manipulation and processing.
 * This class offers functionalities ranging from basic string checks to advanced parsing,
 * including operations like ignoring specific characters, searching substrings using KMP algorithm,
 * JSON key-value extraction, and Unicode transformations.
 */

public class StringUtils {

    /**
     * A constant array of tag names that are targeted for removal during HTML or XML processing.
     * These tags represent a variety of common HTML elements, including structural tags, formatting
     * tags, and media-related tags. The primary purpose of this array is to serve as a reference
     * for identifying and removing specific tags from a given input string or document.
     *
     * The list includes both block-level and inline elements, ensuring broad coverage for various
     * use cases in text sanitization or transformation tasks. Duplicate entries in the array are
     * intentional and may be used to emphasize certain tags or handle edge cases during processing.
     *
     * This array is typically utilized in methods that involve parsing or modifying HTML/XML content,
     * where selective removal of tags is required to achieve desired output formats or compliance
     * with specific standards.
     */
    static final String[] removeTagName = {"strong",
            "abbr", "address", "br", "cite", "div", "ul", "span", "pre", "em", "b", "u", "li", "code", "data", "dd", "del", "p", "img", "src", "video",
            "details", "dl", "dd", "html", "body", "footer", "h1", "h2", "h3", "h4", "h5", "p", "i", "link", "main", "map", "mark", "meta", "nav", "desc"
    };

    // 后续可以添加
    //


    /**
     * Represents a constant string containing Unicode escape sequences.
     * This variable holds the Unicode representation of specific characters,
     * which can be converted to their corresponding graphical representation
     * using appropriate decoding methods. The value is immutable and intended
     * for use in operations requiring Unicode-based transformations or validations.
     */
    public static final String mod_unicode = "\\u53d6\\u4f59";
    /**
     * Represents a constant string containing Unicode escape sequences that correspond to the phrase
     * "答案可能很大" in Chinese. This variable is used to store a predefined Unicode representation
     * of the text, which can be utilized for operations involving Unicode-to-text conversion or
     * other string manipulations.
     *
     * The value is immutable and is intended for scenarios where consistent handling of specific
     * Unicode strings is required within the StringUtils class.
     */
    public static final String mod_ans_unicode = "\\u7b54\\u6848\\u53ef\\u80fd\\u5f88\\u5927";


    /**
     * Represents the Unicode string for the Chinese phrase "输入：" which translates to "Input:" in English.
     * This constant is used to denote input labels in Unicode format, primarily for display or processing purposes.
     * The value is stored as a Java-escaped Unicode string to ensure compatibility and readability within code.
     */
    // 输入 unicode
    public static final String inputUnicode = "\\u8f93\\u5165\\uff1a";
    /**
     * Represents the old Unicode representation of an input string.
     * This constant stores a Unicode-escaped version of a specific input,
     * which can be converted to its corresponding character representation
     * using appropriate decoding methods.
     * The value is intended for use in scenarios where legacy Unicode handling
     * or compatibility with older systems is required.
     */
    public static final String inputUnicodeOld = "\\u8f93\\u5165";

    /**
     * Represents the Unicode string for "输出：" (which translates to "Output:" in English).
     * This constant is used to denote output-related content in a format that supports Unicode escaping.
     * The value is expressed as a sequence of Unicode escape sequences to ensure compatibility across different systems and encodings.
     */
    public static final String outputUnicode = "\\u8f93\\u51fa\\uff1a";

    /**
     * Represents the Unicode escape sequence for the Chinese characters "输出"
     * (meaning "output" in English). This constant stores the older representation
     * of the Unicode string using escape sequences. It is intended for use in contexts
     * where Unicode needs to be expressed in its escaped form rather than as direct
     * characters. The value corresponds to the literal Unicode representation of the
     * characters "\\u8f93\\u51fa".
     */
    public static final String outputUnicodeOld = "\\u8f93\\u51fa";

    /**
     * A constant string representing the Unicode-encoded explanation label.
     * The value is a sequence of Unicode escape sequences that translates to "解释：" in Chinese,
     * which means "Explanation:" in English. This constant can be used as a label or prefix
     * for providing explanations in contexts where Unicode representation is required or preferred.
     */
    public static final String explainUnicode = "\\u89e3\\u91ca\\uff1a";
    /**
     * A constant string representing the Unicode escape sequence for the Chinese characters "解释".
     * This variable holds the old or original representation of the explanation text in Unicode format.
     * It is intended for use in contexts where legacy Unicode handling is required, particularly
     * when dealing with systems or processes that rely on escaped Unicode sequences rather than
     * direct character representation. The value corresponds to the literal string "\\u89e3\\u91ca".
     */
    public static final String explainUnicodeOld = "\\u89e3\\u91ca";


    /**
     * A constant string representing a colon character.
     * This symbol is primarily used as a delimiter in various string manipulation operations within the StringUtils class.
     * It serves as a separator for joining or splitting strings, particularly in contexts where a distinct boundary marker is required.
     * The value of this constant is ":".
     */
    public static final String DOT = ":";
    /**
     * A constant string representing the label "Input".
     * This field is used to denote input-related identifiers or markers within the context of string manipulation utilities.
     * It serves as a standardized reference for input operations or annotations in various methods throughout the class.
     */
    public static final String Input = "Input";
    /**
     * A constant string representing the label "Output".
     * This is used as a standardized identifier or marker within the StringUtils class
     * to denote output-related operations or distinctions in processing.
     */
    public static final String Output = "Output";
    /**
     * A constant string that serves as a descriptive label or identifier.
     * This field is intended to provide a human-readable explanation or clarification
     * in various contexts where a named placeholder or marker is required.
     * The value of this constant is fixed and cannot be modified.
     */
    public static final String Explanation = "Explanation";
    /**
     * A constant string representing the concatenation of the "Output" string and a dot (".") character.
     * This field is used to denote output-related identifiers or markers in a standardized format.
     * The value is derived from the static field "Output" concatenated with the static field "DOT".
     */
    public static final String OutputDot = Output + DOT;
    /**
     * Represents the concatenation of the "Input" string with a dot (".") separator.
     * This constant is used to denote input-related identifiers or keys in a standardized format.
     * The value is derived from the static field "Input" combined with the static field "DOT".
     */
    public static final String InputDot = Input + DOT;
    /**
     * A constant string representing the concatenation of the "Explanation" string
     * with the "DOT" string. This value is intended to provide a formatted representation
     * of an explanation marker, typically used for generating or identifying explanatory
     * content within strings.
     * The value is derived from predefined constants and is immutable.
     */
    public static final String ExplanationDot = Explanation + DOT;

    /**
     * Checks if the provided string is empty or null.
     *
     * @param s the string to check, may be null
     * @return true if the string is null or has a length of zero, false otherwise
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * Checks if the provided string is strictly empty, null, or matches specific ignored values.
     * A string is considered strictly empty if it is null, has zero length, or equals "null" or "#".
     *
     * @param s the string to check, may be null
     * @return true if the string is null, has zero length, or matches "null" or "#", false otherwise
     */
    public static boolean strictIsEmpty(String s) {
        return s == null || s.length() == 0 || "null".equals(s) || "#".equals(s);
    }

    /**
     * Checks if the provided string is strictly empty, null, or matches any of the specified ignored values.
     * A string is considered strictly empty if it is null, has zero length, or equals any of the values in the nullstr array.
     *
     * @param s the string to check, may be null
     * @param nullstr an array of strings representing ignored values, may be null or empty
     * @return true if the string is null, has zero length, or matches any of the ignored values, false otherwise
     */
    public static boolean strictIsEmpty(String s, String... nullstr) {
        if (isEmpty(s)) {
            return true;
        }
        for (String s1 : nullstr) {
            if (!isEmpty(s1) && s1.equals(s)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the specified character is considered an ignorable character.
     * Ignorable characters include whitespace characters, escape characters, and specific special characters.
     *
     * @param c the character to check
     * @return true if the character is ignorable, false otherwise
     */
    public static boolean isIgnore(char c) {
        return c == '\r' || c == '\n' || c == '\t' || c == '\b' || c == '\f' || c == '\0' || c == '\\' || c == ' ' || c == '\'' || c == '\"';
    }

    /**
     * Checks if the specified character is considered ignorable based on an additional control character.
     * A character is considered ignorable if the control character is '#' and the character matches stricter ignore criteria,
     * or if the control character is not '#' and matches general ignore criteria.
     *
     * @param c the character to check
     * @param st the control character that determines the type of ignore check to perform
     * @return true if the character is ignorable under the specified conditions, false otherwise
     */
    public static boolean isIgnore(char c, char st) {
        if (st == '#' && isIgnoreStrict(c)) {
            return true;
        }
        return st != '#' && isIgnore(c);
    }

    /**
     * Checks if the specified character is considered strictly ignorable.
     * A character is considered strictly ignorable if it is identified as ignorable by the {@link #isIgnore(char)} method
     * or if it matches stricter ignore criteria defined by the {@link #isIgnore(char, char)} method with the control character '#'.
     *
     * @param c the character to check
     * @return true if the character is strictly ignorable, false otherwise
     */
    public static boolean isIgnoreStrict(char c) {
        return isIgnore(c) || isIgnoreStrict(c, '#');
    }

    /**
     * Checks if the specified character is considered strictly ignorable, either by matching predefined ignorable characters
     * or by being included in the provided list of additional characters to ignore.
     * A character is considered strictly ignorable if it is identified as ignorable by the {@link #isIgnore(char)} method
     * or if it matches any of the characters in the provided array.
     *
     * @param c the character to check
     * @param chars an optional array of additional characters to consider as ignorable, may be null or empty
     * @return true if the character is strictly ignorable, false otherwise
     */
    public static boolean isIgnoreStrict(char c, char... chars) {
        if (isIgnore(c)) {
            return true;
        }
        for (char c1 : chars) {
            if (c1 == c) {
                return true;
            }
        }
        return false;
    }


    /**
     * Replaces or removes specific content and formatting from the input string while ignoring certain patterns.
     * This method performs multiple operations, including removing excessive newlines, special characters,
     * specific Unicode sequences, HTML entities, and unwanted tags. It also trims whitespace and other ignorable content.
     *
     * @param s the input string to process, may be null
     * @return the processed string with unwanted content removed or replaced; returns "\"\"" if the input is null, empty,
     *         or becomes empty after processing
     */
    public static String replaceIgnoreContent(String s) {
        if (isEmpty(s)) {
            return "\"\"";
        }
        // 迭代删除所以多余的换行
        while (s.contains("\n\n\n")) {
            s = s.replace("\n\n\n", "\n\n");
        }
        s = s.replace("`", "");
        s = s.replace(inputUnicodeOld, "");
        s = s.replace(outputUnicodeOld, "");
        s = s.replace(explainUnicodeOld, "");
        s = s.replace("uff1a", "");
        s = s.replace(InputDot, "").replace(Input, "");
        s = s.replace(OutputDot, "").replace(Output, "");
        for (String tag : removeTagName) {
            s = removeTag(s, tag);
        }
        s = s.replace("&lt;", "");
        s = s.replace("\\\\n;", "").replace("\\\\n", "");
        s = s.replace("\\n;", "").replace("\\n", "");
        s = s.replace("\\", "");
        s = s.replace("&gt;", "");
        s = s.replace("&amp;", "");
        s = s.replace("&quot;", "");
        s = s.replace("&nbsp;", "");
        s = s.replace(":", "");
        s = s.replace(" ", "");
        s = s.replace("<", "").replace(">", "");
        if (isEmpty(s)) {
            return "\"\"";
        }
        return s;
    }


    /**
     * Searches for all occurrences of a specified pattern within a given text using the Knuth-Morris-Pratt (KMP) algorithm.
     * This method repeatedly invokes the KMP search to find multiple matches of the pattern in the text and returns their starting indices.
     *
     * @param text the text in which to search for the pattern, may not be null
     * @param pattern the pattern to search for within the text, may not be null
     * @return a list of integers representing the starting indices of all occurrences of the pattern in the text,
     *         or an empty list if the pattern is not found
     */
    public static List<Integer> kmpSearchList(String text, String pattern) {
        int id = -1;
        List<Integer> ans = new ArrayList<>();
        while (true) {
            id = kmpSearch(text, id == -1 ? 0 : id + pattern.length(), pattern);
            if (id == -1) {
                break;
            }
            ans.add(id);
        }
        return ans;
    }


    /**
     *
     */
    public static String ingoreString(String input) {
        if (input == null || input.length() == 0) {
            //throw new NullPointerException("input content is null");
            return input;
        }
        input = input.replace("&quot;", "");
        char[] charArray = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            if (StringUtils.isIgnoreStrict(c)) continue;
            sb.append(c);
        }
        return sb.toString();
    }


    /**
     * Searches for the first occurrence of the specified pattern within the given text using the Knuth-Morris-Pratt (KMP) algorithm.
     * The search starts from the beginning of the text.
     *
     * @param text the text in which to search for the pattern, may be null
     * @param pattern the pattern to search for, may be null
     * @return the index of the first occurrence of the pattern in the text, or -1 if the pattern is not found,
     *         or if either the text or pattern is null, or if the pattern length/**
    exceeds the text length
     */
    public static int kmpSearch(String text, String pattern) {
        return kmpSearch(text, 0, pattern);
    }

    /**
     * Searches for the first occurrence of the specified pattern within the given text using the Knuth-Morris-Pratt (KMP) algorithm.
     * The search starts from the specified starting index.
     *
     * @param text the text in which to search for the pattern, may be null
     * @param st the starting index in the text from which the search begins, must be non-negative and less than the text length
     * @param pattern the pattern to search for, may be null
     * @return the index of the first occurrence of the pattern in the text starting from the specified index,
     *         or -1 if the pattern is not found, or if the input parameters are invalid
     */
    public static int kmpSearch(String text, int st, String pattern) {
        if (pattern == null || text == null || pattern.length() > text.length()) {
            return -1;
        }
        if (st < 0 || st >= text.length()) {
            return -1;
        }
        int[] next = new int[pattern.length()];
        for (int i = 1, cnt = 0; i < pattern.length(); i++) {
            while (cnt > 0 && pattern.charAt(i) != pattern.charAt(cnt)) {
                cnt = next[cnt - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(cnt)) {
                cnt++;
            }
            next[i] = cnt;
        }
        for (int i = st, cnt = 0; i < text.length(); i++) {
            while (cnt > 0 && text.charAt(i) != pattern.charAt(cnt)) {
                cnt = next[cnt - 1];
            }
            if (text.charAt(i) == pattern.charAt(cnt)) {
                cnt++;
            }
            if (cnt == pattern.length()) {
                return i - pattern.length() + 1; // 找到第一个内容
            }
        }
        return -1;
    }



    /**
     * Wraps the provided key in double quotes if it is not already quoted.
     * If the key is null or empty, it is returned as-is without modification.
     * This method ensures that the key starts and ends with a double quote character ("),
     * adding them where necessary.
     *
     * @param key the string to wrap in double quotes, may be null
     * @return the wrapped key with enclosing double quotes, or the original key if it is null or empty
     */
    public static String wrapperKey(String key) {
        if (isEmpty(key)) {
            return key;
        }
        if (!key.startsWith("\"")) {
            key = "\"" + key;
        }
        if (!key.endsWith("\"")) {
            key = key + "\"";
        }
        return key;
    }

    /**
     * Retrieves the value associated with the specified key from a JSON-formatted string.
     * The method optionally wraps the key in double quotes before searching for it within the JSON string.
     * If the key is not found or the JSON string is improperly formatted, the result may be null or inaccurate.
     *
     * @param jsonStr the JSON-formatted string to search within, may be null
     * @param key the key whose associated value is to be retrieved, may be null
     * @return the value corresponding to the specified key, or null if the key is not found or the JSON string is invalid
     */
    public static String jsonStrGetValueByKey(String jsonStr, String key) {
        return jsonStrGetValueByKey(jsonStr, key, true);
    }

    /**
     * Retrieves the value associated with the specified key from a JSON string.
     *
     * @param jsonStr the JSON string from which the value is to be extracted
     * @param key the key whose associated value is to be retrieved
     * @param isWrapper a boolean flag indicating whether the JSON string is wrapped in an object
     * @return the value associated with the specified key, or null if the key is not found or the JSON string is invalid
     */
    public static String jsonStrGetValueByKey(String jsonStr, String key, boolean isWrapper) {
        return jsonStrGetValueByKey(jsonStr,key,isWrapper,true);
    }

    /**
     * Retrieves the value associated with a specified key from a JSON string.
     * The method supports optional wrapping of the key and replacement of certain characters in the result.
     *
     * @param jsonStr the JSON string from which the value is to be extracted
     * @param key the key whose associated value is to be retrieved
     * @param isWrapper if true, the key will be wrapped using a custom wrapper method before searching
     * @param isReplace if true, the returned value will have all double quotes and colons removed
     * @return the value associated with the specified key as a string, or an empty string if the key is not found or an exception occurs
     */
    public static String jsonStrGetValueByKey(String jsonStr, String key, boolean isWrapper,boolean isReplace) {
        try {
            if (isWrapper) {
                key = wrapperKey(key);
            }
            int find = kmpSearch(jsonStr, key);
            if (find == -1) {
                System.out.println("Not find key " + key);
                return "";
            }
            int startIndex = find + key.length();

            StringBuilder sb = new StringBuilder();
            boolean foundValue = false;
            boolean inQuotes = false;
            Stack<Character> stack = new Stack<>();

            for (int i = startIndex; i < jsonStr.length(); i++) {
                char c = jsonStr.charAt(i);
                if (c == ' ') {
                    sb.append(c);
                    continue;
                }
                if (c == '\"' && jsonStr.charAt(i - 1) != '\\') {
                    inQuotes = !inQuotes;
                }
                if (!inQuotes) {
                    if (c == '{' || c == '[' || c == '(') {
                        stack.push(c);
                    } else if (c == '}' || c == ']' || c == ')') {
                        sb.append(c);
                        if (stack.isEmpty() || !isMatchingPair(stack.peek(), c)) {
                            break;
                        }
                        stack.pop();
                    } else if (c == ':' && stack.isEmpty()) {
                        foundValue = true;
                    } else if (c == ',' && stack.isEmpty()) {
                        break;
                    }
                }
                if (foundValue && !Character.isWhitespace(c)) {
                    sb.append(c);
                }
            }
            if(!isReplace) return sb.toString();
            return sb.toString().replaceAll("\"", "").replace(":", "");
        } catch (Exception ignore) {
            return "";
        }
    }


    /**
     * Checks if the given pair of characters form a matching pair of brackets.
     *
     * @param left the left character, which should be an opening bracket ('{', '[', or '(')
     * @param right the right character, which should be a closing bracket ('}', ']', or ')')
     * @return true if the characters form a matching pair of brackets, false otherwise
     */
    private static boolean isMatchingPair(char left, char right) {
        return (left == '{' && right == '}') || (left == '[' && right == ']') || (left == '(' && right == ')');
    }


    /**
     * Parses the ID from the given URL by extracting the first sequence of digits found.
     *
     * @param url the URL string from which the ID needs to be extracted
     * @return the first sequence of digits found in the URL, or an empty string if no digits are found
     */
    public static String parseId(String url) {
        Pattern compile = Pattern.compile("\\d+");
        Matcher matcher = compile.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";

    }




    /**
     * Processes a given string by removing special Unicode sequences and unwanted characters.
     * This method performs multiple replacements to handle escaped Unicode characters, HTML entities,
     * and newline characters. It also trims leading ignored characters from the input*/
    // 处理方法中的特殊unicode
    public static String handerMethodString(String input){
        if (isEmpty(input)) return input;
        input = input.replace("\\u000A", "");
        input = input.replace("\\u003C", "<");
        input = input.replace("\\u003E", ">");
        input = input.replace("\\u0028", "(");
        input = input.replace("\\u0029", ")");
        input = input.replace("&lt;", "<");
        input = input.replace("&gt;", ">");
        input = input.replace("&amp;", "&");
        input = input.replace("&quot;", "");
        input = input.replace("&nbsp;", "");
        input = input.replace("\\n", "");
        input = input.replace("\n", "");
        int i = 0;
        // 移出前面的空格
        while (i < input.length()) {
            if (!StringUtils.isIgnore(input.charAt(i))) {
                break;
            }
            i++;
        }
        input = input.substring(i);
        return input;
    }


    /**
     * Processes the input string to extract a substring starting from the index
     * where the word "Solution" is found. If "Solution" is not found, the original
     * input string is returned unchanged.
     *
     * @param input the input string to be processed
     * @return a substring starting from the occurrence of "Solution" if found,
     *         otherwise the original input string
     */
    public static String handlerAnnotationTemplate(String input){
        int classIdx = kmpSearch(input,"Solution");
        if(classIdx != -1){
            return input.substring(classIdx);
        }
        return input;
    }


    /**
     * Processes the provided list of strings by removing any trailing newline characters
     * from the end of the list. The method iteratively checks the last element of the list,
     * and if it is equal to a newline character ("\n"), it removes that element.
     * This continues until the list is either empty or the last element is not a newline.
     *
     * @param ans the list of strings to be processed; may be null
     */
    public static void handlerResult(List<String> ans) {
        while (ans != null && ans.size() > 0 && "\n".equals(ans.get(ans.size() - 1))) {
            ans.remove(ans.size() - 1);
        }
    }


    /**
     * Extracts the name of the method from the provided code content string.
     * The method name is identified by searching backward from the first occurrence
     * of an opening parenthesis '(' until a space character or the start of the string is encountered.
     *
     * @param codeContent the string containing the code snippet from which the method name is to be extracted
     * @return the extracted method name as a string, or "IoUtil.DEFAULT_METHOD_NAME" if no valid method name is found
     */
    public static String getMethodName(String codeContent) {
        int st = kmpSearch(codeContent, "(");
        if (st == -1) {
            return "IoUtil.DEFAULT_METHOD_NAME";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = st - 1; i >= 0; i--) {
            char c = codeContent.charAt(i);
            if (c == ' ' && !StringUtils.isEmpty(sb.toString())) {
                //return sb.toString();
                break;
            }
            if (c == ' ') {
                continue;
            }
            sb.append(c);
        }
        sb.reverse();
        return sb.toString();
    }

    /**
     * Retrieves the method name from the provided class string using a default pattern.
     *
     * @param classStr the string representation of the class from which to extract the method name
     * @return the extracted method name as a String
     */
    public static String getMethod(String classStr) {
        return getMethod(classStr, LCTemplate.JAVA_CODE_PATTERN);
    }


    /**
     * Extracts and processes a method string from the provided class string based on the given Java code flag.
     *
     * @param classStr the string representation of the class containing the method to be extracted
     * @param javaCodeFlag the flag or marker used to identify the starting point of the method within the class string
     * @return a processed string representation of the method, prefixed with "//", or an empty string if the method cannot be found or extracted
     */
    public static String getMethod(String classStr,String javaCodeFlag) {
        if (isEmpty(javaCodeFlag) || isEmpty(classStr)) return classStr;

        // 查找含有Java code 的标志
        int i = StringUtils.kmpSearch(classStr, javaCodeFlag);
        if (i == -1) {
            return "";
        }
        classStr = classStr.substring(i+javaCodeFlag.length());
        // 如果有注释先处理注释
        classStr = StringUtils.handlerAnnotationTemplate(classStr);
        int deep = 0;
        StringBuilder sb = null;
        for (i = 0; i < classStr.length(); i++) {
            char c = classStr.charAt(i);
            if (c == '{') {
                deep++;
                if (deep==1 && sb == null){
                    sb = new StringBuilder();
                } else if( deep == 2 && sb != null){
                    sb.append(c);
                }
            } else if (c == '}') {
                deep--;
                if (deep == 0) {
                    break;
                }
                if (sb != null) {
                    sb.append(c);
                }

            } else {
                if (sb != null) sb.append(c);
            }
        }
        classStr = sb != null ? sb.toString() : null;
        classStr = StringUtils.handerMethodString(classStr);
        return "// " + classStr;
    }


    /**
     * Determines if the given description requires MOD based on specific search patterns.
     *
     * @param desc the description string to be analyzed
     * @return true if the description contains any of the predefined MOD patterns, false otherwise
     */
    public static boolean isNeedMOD(String desc) {
        if (isEmpty(desc)) {
            return false;
        }
        int a = -1;
        a = kmpSearch(desc, mod_unicode);
        if (a != -1) {
            return true;
        }
        a = kmpSearch(desc, mod_ans_unicode);
        return a != -1;
    }

    /**
     * Converts a given URL into a case name format by replacing hyphens with underscores.
     * This method first generates a title slug from the URL using BuildUrl.buildTitleSlug,
     * then replaces all hyphens in the slug with underscores to produce the final case name.
     *
     * @param url the input URL string to be converted into a case name
     * @return a String representing the case name with underscores instead of hyphens
     */
    public static String toCaseName(String url) {
        String titleSlug = BuildUrl.buildTitleSlug(url);
        return titleSlug.replace("-", "_");
    }


    /**
     * Extracts all URLs from the given input string.
     * The method uses a regular expression to identify URLs starting with "https://"
     * and ending before a whitespace or closing parenthesis.
     *
     * @param s the input string from which URLs are to be extracted
     * @return a list of URLs found in the input string; returns an empty list if no URLs are found
     */
    public static List<String> matchUrls(String s) {
        List<String> urls = new ArrayList<>();
        try {

            Pattern pattern = Pattern.compile("https://(.*)/[^\\s\\)]+");
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                String url = matcher.group();
                urls.add(url);
                // System.out.println("URL: " + url);
            }
        } catch (Exception ignore) {

        }

        return urls;
    }


    /**
     * Converts a Unicode escape sequence string into its corresponding Chinese characters.
     * The input string should consist of Unicode escape sequences prefixed with "\\u".
     * If the input is invalid or an error occurs during conversion, an empty string is returned.
     *
     * @param unicodeStr the Unicode escape sequence string to be converted
     * @return the resulting string containing the Chinese characters, or an empty string if conversion fails
     */
    public static String unicodeToChinese(String unicodeStr) {
        StringBuilder sb = new StringBuilder();

        try {
            String[] hex = unicodeStr.split("\\\\u");
            for (int i = 1; i < hex.length; i++) {
                int data = Integer.parseInt(hex[i], 16);
                sb.append((char) data);
            }
        } catch (Exception e) {
            return "";
        }

        return sb.toString();
    }


    /**
     * Counts the number of directories within the absolute path of the given class
     * that start with the specified prefix and*/
    public static String getDirCount(Class<?> aclass,String prefix)  {
        String s = IoUtil.buildAbsolutePath(aclass);
        File file = new File(s);
        int cnt = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                if (file1 != null && file1.isDirectory() && file1.getName().startsWith(prefix)) {
                    cnt++;
                }
            }
        }
        if (cnt < 10) {
            return "000" + cnt;
        } else if (cnt < 100) {
            return "00" + cnt;
        } else if (cnt < 1000) {
            return "0" + cnt;
        } else {
            return String.valueOf(cnt);
        }
    }


    /**
     * Converts specific Unicode strings in the input to their corresponding English representations.
     * This method replaces predefined Unicode patterns with English labels such as "Input", "Output",
     * and "Explanation". If the input is null or empty, it is returned unchanged.
     *
     * @param input the string containing Unicode patterns to be converted to English
     * @return the modified string with Unicode patterns replaced by their English equivalents
     */
    public static String inputOrOutputUnicodeConvertEnglish(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        input = input.replace(inputUnicodeOld, Input);
        input = input.replace(outputUnicodeOld, Output);
        input = input.replace(inputUnicode, InputDot);
        input = input.replace(outputUnicode, OutputDot);
        input = input.replace(explainUnicodeOld, Explanation);
        input = input.replace(explainUnicode, ExplanationDot);
        return input;
    }
    /**
     * Converts specific substrings within the input string to their corresponding Unicode representations.
     * The method performs a series of replacements to handle various cases of input and output terms,
     * ensuring consistent Unicode conversion.
     *
     * @param input the original string that may contain substrings to be converted to Unicode
     * @return the modified string with specific substrings replaced by their Unicode equivalents,
     *         or the original string if it is null or empty
     */
    public static String inputOrOutputConvertUnicode(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        input = input.replace("input", Input);
        input = input.replace("output", Output);
        input = input.replace(Input, inputUnicodeOld);
        input = input.replace(Output, outputUnicodeOld);
        input = input.replace(InputDot, inputUnicode);
        input = input.replace(OutputDot, outputUnicode);
        input = input.replace(Explanation, explainUnicodeOld);
        input = input.replace(ExplanationDot, explainUnicode);
        return input;
    }

    /**
     * Removes all occurrences of the specified HTML tag from the input string.
     * The method uses a regular expression to identify and remove both opening and closing tags,
     * including any attributes within the opening tag. If the specified tag is not found in the input,
     * the original input string is returned unchanged.
     *
     * @param input the input string containing HTML content
     * @param tagName the name of the HTML tag to be removed (e.g., "div", "span")
     * @return the modified string with the specified tag removed, or the original string if the tag is not found
     */
    public static String removeTag(String input, String tagName) {
        if (StringUtils.kmpSearch(input, "<" + tagName) == -1 && StringUtils.kmpSearch(input, tagName + ">") == -1) {
            return input;
        }
        Pattern pattern = Pattern.compile(String.format("</?%s[^>]*>", tagName));
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }


}
