package code_generation.crwal;

import code_generation.crwal.leetcode.LCTestCase;
import code_generation.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing and processing test cases from various sources.
 * Provides methods to handle HTML content, extract test cases, and format them consistently.
 * @author wuxin0011
 * @since 1.0
 */
public class TestCaseUtil {

    /**
     * Builds HTML start and end tags for a given tag name.
     * Constructs tags without closing  to handle attributes in opening tags.
     *
     * @param tagName The HTML tag name (e.g., "div", "span")
     * @return String array containing [startTag, endTag]
     */
    public static String[] buildStartTagAndEngTag(String tagName) {
        String start = "<" + tagName;
        String end = "</" + tagName + ">";
        return new String[]{start, end};
    }

    /**
     * Parses contest test cases from input content.
     * Handles both simple and complex test case formats with various delimiters.
     *
     * @param input The input content containing test cases
     * @param startFlag The starting delimiter for test cases
     * @param endFlag The ending delimiter for test cases
     * @param ans The list to store parsed test cases
     */
    public static void startParseContestTestCase(String input, String startFlag, String endFlag, List<String> ans) {
        // System.out.println("input======>\n"+input);
        input = input.replace("&quot;", "");
        input = input.replace("<em>;", "").replace("</em>;", "");
        input = input.replace("\\n", "");
        input = input.replace("\n", "");
        char[] charArray = input.toCharArray();
        int deep = 0;
        int line = 0;
        StringBuilder sb = null;
        if (StringUtils.kmpSearch(input, "=") == -1 && StringUtils.kmpSearch(input, ":") == -1) {
            // ans.add(input);

            sb = new StringBuilder();
            for (char c : charArray) {
                if (StringUtils.isIgnore(c)) {
                    continue;
                }
                switch (c) {
                    case '[':
                    case '{':
                    case '\u3010':
                        if (deep == 0 && sb == null) {
                            sb = new StringBuilder();
                        }
                        if (sb != null) {
                            sb.append(c);
                        }
                        deep++;
                        break;
                    case ']':
                    case '}':
                    case '\u3011':
                        deep--;
                        if (sb != null) {
                            sb.append(c);
                            if (deep == 0) {
                                ans.add(sb.toString());
                                sb = null;
                            }
                        }

                        break;
                    case ',':
                    case '\uFF0C':
                        if (sb != null) {
                            if (deep == 0) {
                                ans.add(sb.toString());
                                sb = new StringBuilder();
                            } else {
                                sb.append(c);
                            }
                        }
                        break;
                    default:
                        if (sb != null) {
                            sb.append(c);
                        }
                }


            }
            if(sb != null){
                ans.add(sb.toString());
            }
            ans.add("\n");
        } else {
            for (char c : charArray) {
                if (c != '\\' && StringUtils.isIgnore(c)) {
                    continue;
                }
                switch (c) {
                    case '=':
                    case ':':
                    case '\uFF1A':
                        sb = new StringBuilder();
                        break;
                    case '[':
                    case '{':
                    case '\u3010':
                        if (deep == 0 && sb == null) {
                            sb = new StringBuilder();
                        }
                        if (sb != null) {
                            sb.append(c);
                        }
                        deep++;
                        break;
                    case ']':
                    case '}':
                    case '\u3011':
                        deep--;
                        if (sb != null) {
                            sb.append(c);
                            if (deep == 0) {
                                ans.add(sb.toString());
                                sb = null;
                            }
                        }
                        break;
                    case ',':
                    case '\uFF0C':
                        if (sb != null) {
                            if (deep == 0 && line == 0) {
                                ans.add(sb.toString());
                                sb = new StringBuilder();
                            } else {
                                sb.append(c);
                            }
                        }
                        break;
                    case '\\': // 特殊情况 https://leetcode.cn/problems/verify-preorder-serialization-of-a-binary-tree/description/
                        line++;
                        if(deep==0){ // 如果不加这个会解析失败  https://leetcode.cn/problems/nearest-exit-from-entrance-in-maze/
                            if (line == 1) {
                                sb = new StringBuilder();
                            } else if (line == 2 && sb != null) {
                                ans.add(sb.toString());
                                line = 0;
                                sb = null;
                            }
                        }else {
                            break;
                        }

                        break;
                    default:
                        if (sb != null) {
                            sb.append(c);
                        }
                }


            }
            if (sb != null) {
                ans.add(sb.toString());
            }
        }
    }


    /**
     * Gets content from HTML tag with specified class selector.
     *
     * @param input The HTML content to search
     * @param classSelectorFlag The class name to match
     * @param start The starting position to search from
     * @param tagName The HTML tag name to match
     * @return The content between matching tags
     * @throws RuntimeException if input or tags are null or invalid
     */
    public static String getTagContent(String input, String classSelectorFlag, int start, String tagName) {
        String[] strings = buildStartTagAndEngTag(tagName);
        return getTagContent(input, classSelectorFlag, start, strings[0], strings[1]);
    }

    /**
     * Gets content between specified HTML tags with class selector.
     *
     * @param input The HTML content to search
     * @param classSelectorFlag The class name to match
     * @param start The starting position to search from
     * @param startTag The opening tag pattern
     * @param endTag The closing tag pattern
     * @return The content between matching tags
     * @throws RuntimeException if input or tags are null or invalid
     */
    public static String getTagContent(String input, String classSelectorFlag, int start, String startTag, String endTag) {
        if (input == null || startTag == null || endTag == null) {
            throw new RuntimeException("input content is null or tag is null");
        }
        if (input.length() < startTag.length() || input.length() < endTag.length()) {
            throw new RuntimeException("input length not ");
        }
        int stIdx = StringUtils.kmpSearch(input, start, classSelectorFlag);
        if (stIdx == -1) {
            //throw new RuntimeException("input content Not find " + classSelectorFlag);
            return input;
        }
        int slen = startTag.length();
        int elen = endTag.length();
        int deep = 0;
        boolean findTag = false;
        int findSt = stIdx;

        // 检查是否包含标签名称
        if (!classSelectorFlag.contains(startTag)) {
            for (int i = stIdx; i >= 0; i--) {
                String s1 = input.substring(i, i + slen);
                if (s1.equals(startTag)) {
                    findSt = i;
                    deep++;
                    break;
                }
            }
        }
        for (int i = findSt; i < input.length(); i++) {
            String s1 = input.substring(i, i + slen);
            if (startTag.equals(s1)) {
                deep++;
                findTag = true;
                continue;
            }
            String s2 = input.substring(i, i + elen);
            if (endTag.equals(s2)) {
                deep--;
            }
            if (deep == 0 && findTag) {
                return input.substring(findSt, i);
            }
        }
        return "";
    }

    /**
     * Parses default test case format from input string.
     *
     * @param input The input string containing test cases
     * @param ans The list to store parsed test cases
     */
    public static void parseDefaultTextCase(String input, List<String> ans) {
        input = input.replaceAll("\\n", "");
        input = input.replaceAll("&quot;", "");
        input = input.replaceAll("<code>", "").replaceAll("</code>", "");
        input = input.replace("<em>", "").replace("</em>", "");
        // 混合模式
        // https://leetcode.cn/problems/earliest-second-to-mark-indices-i/description/
        input = StringUtils.inputOrOutputConvertUnicode(input);
        if (checkHasUnicodeInputOutput(input)) {
            unicodeParseInputOutPut(input, ans);
        } else {
            int i1 = StringUtils.kmpSearch(input, "=");
            int i2 = StringUtils.kmpSearch(input, ":");
            if (i1 == -1 && i2 == -1) {
                ans.add(input);
                return;
            }
            List<String> temp = new ArrayList<>();
            int end = Math.min(Math.max(input.length() - LCTestCase.pre_end.length(), 1), input.length());
            StringBuilder sb = null;
            int deep = 0, args = 0;
            int line = 0;
            for (int i = i1; i < end; i++) {
                char c = input.charAt(i);
                if (c != '\\' && StringUtils.isIgnore(c)) {
                    if (sb != null && i == end - 1) {
                        temp.add(sb.toString());
                        sb = null;
                    }
                    continue;
                }

                switch (c) {
                    case '<':
                        if (args > 0 && sb != null) {
                            temp.add(sb.toString());
                            args--;
                            sb = null;
                        }
                    case '=':
                    case '>':
                    case ':':
                        if (c == '=') {
                            args++;
                        }
                        sb = new StringBuilder();
                        break;
                    case '[':
                        deep++;
                        if (sb != null) {
                            sb.append(c);
                        }
                        break;
                    case ']':
                        deep--;
                        if (sb != null) {
                            sb.append(c);
                        }
                        if (deep == 0 && sb != null) {
                            temp.add(sb.toString());
                            if (args > 0) args--;
                            sb = null;
                        }
                        break;
                    case '\\':
                        line++;
                        if(deep==0){
                            if (line == 1) {
                                sb = new StringBuilder();
                            } else if (line == 2 && sb != null) {
                                ans.add(sb.toString());
                                line = 0;
                            }
                        }
                        break;
                    case ',':
                        if (sb != null) {
                            if (deep == 0 && line == 0) {
                                temp.add(sb.toString());
                                if (args > 0) args--;
                                sb = null;
                            } else {
                                sb.append(c);
                            }
                        }
                        break;
                    default:
                        if (sb != null) {
                            sb.append(c);
                        }
                        break;
                }
            }
            temp.add("\n");
            ans.addAll(temp);
        }

    }


    /**
     * Converts list of test cases to formatted string.
     *
     * @param ans The list of test cases to format
     * @return Formatted string representation of test cases
     */
    public static String testCaseToString(List<String> ans) {
        StringBuilder sb = new StringBuilder();
        if (ans == null || ans.isEmpty()) {
            return sb.toString();
        }
        for (int i = 0; i < ans.size(); i++) {
            String s = ans.get(i);
            if (s == null || s.contains("font-family") || s.contains("example")) {
                continue;
            }
            if (StringUtils.isEmpty(s)) {
                sb.append("\"\"");
            }
            sb.append(s);
            if (i != ans.size() - 1 && !s.equals("\n")) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    /**
     * Handles Unicode input/output markers in test cases.
     *
     * @param input The input string to process
     * @param newStr The new format marker
     * @param oldStr The old format marker
     * @param isAdd Whether to add marker length to index
     * @return The position index after handling
     */
    public static int handlerUnicodeInputAndOutPut(String input, String newStr, String oldStr, boolean isAdd) {
        int idx = StringUtils.kmpSearch(input, newStr);
        if (idx != -1) {
            return isAdd ? idx + newStr.length() : idx;
        } else {
            idx = StringUtils.kmpSearch(input, oldStr);
            if (idx == -1) {
                return -1;
            }
            return isAdd ? idx + oldStr.length() : idx;
        }
    }


    /**
     * Parses test cases with Unicode input/output markers.
     *
     * @param input The input string containing test cases
     * @param ans The list to store parsed test cases
     */
    public static void unicodeParseInputOutPut(String input, List<String> ans) {
        int a = handlerUnicodeInputAndOutPut(input, StringUtils.inputUnicode, StringUtils.inputUnicodeOld, true);
        int b = handlerUnicodeInputAndOutPut(input, StringUtils.outputUnicode, StringUtils.outputUnicodeOld, false);
        if (a == -1 || b == -1) {
            System.err.println("Not find test case !");
            return;
        }
        int c = handlerUnicodeInputAndOutPut(input, StringUtils.explainUnicode, StringUtils.explainUnicodeOld, false);
        String inputStr = handlerIgnoreStr(input.substring(a, b));
        String outputStr = handlerIgnoreStr(input.substring(b, c == -1 ? input.length() : c));
        startParseContestTestCase(inputStr, LCTestCase.EqualFlag, LCTestCase.interFlag, ans);
        StringUtils.handlerResult(ans);
        startParseContestTestCase(outputStr, LCTestCase.EqualFlag, LCTestCase.interFlag, ans);
    }

    /**
     * Checks if input contains Unicode formatted test cases.
     *
     * @param input The input string to check
     * @return true if Unicode markers are found, false otherwise
     */
    public static boolean checkHasUnicodeInputOutput(String input) {
        int a = StringUtils.kmpSearch(input, StringUtils.inputUnicodeOld);
        int b = StringUtils.kmpSearch(input, StringUtils.outputUnicodeOld);
        return a != -1 && b != -1 && a < b;
    }

    /**
     * Removes HTML tags and formatting from test case string.
     *
     * @param s The string to clean
     * @return Cleaned string without HTML tags
     */

    public static String handlerIgnoreStr(String s) {
        if (StringUtils.isEmpty(s)) return s;
        s = s.replace(StringUtils.inputUnicode, "").replace(StringUtils.outputUnicode, "").replace(StringUtils.explainUnicode, "");
        s = s.replace(StringUtils.inputUnicodeOld, "").replace(StringUtils.outputUnicodeOld, "");
        s = s.replace("<b>", "").replace("</b>", "");
        s = s.replace("<p>", "").replace("</p>", "");
        s = s.replace("<span>", "").replace("</span>", "");
        s = s.replace("<strong>", "").replace("</strong>", "");
        s = s.replace("<div>", "").replace("</div>", "");
        s = s.replace("<br>", "").replace("</br>", "");
        s = s.replace("<pre>", "").replace("</pre>", "");
        s = s.replace("<em>", "").replace("/em>", "");
        return s;
    }
}