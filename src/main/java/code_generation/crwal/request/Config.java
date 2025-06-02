package code_generation.crwal.request;

import code_generation.crwal.Constant;
import code_generation.utils.IoUtil;
import code_generation.utils.ReflectUtils;
import code_generation.utils.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Utility class for handling configuration files and properties.
 * Provides methods to read, parse, and manage configuration files in .properties format,
 * with special handling for cookies and HTTP headers.
 * @author wuxin0011
 * @since 1.0
 */
public class Config {

    /**
     * The suffix for configuration files (.properties)
     */
    public final static String config_suffix = ".properties";

    /**
     * The filename for storing cookies (cookies.txt)
     */
    private static final String cookieFile = Constant.cookies + ".txt";

    /**
     * Default template for configuration files containing common HTTP headers
     */
    private static final String defaultTemplate = "Host=leetcode.cn\n" +
            "Referer=https://leetcode.cn\n" +
            "Origin=https://leetcode.cn/\n" +
            "Connection=keep-alive\n" +
            "Cookie=Cookie\n" +
            "Accept=*/*\n" +
            "Sec-Fetch-Mode=cors\n" +
            "Sec-Ch-Ua-Platform=Windows\n" +
            "User-Agent=\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.62\";\n" +
            "Accept-Language=zh-CN,zh;desc=0.9,en;desc=0.8,en-GB;desc=0.7,en-US;desc=0.6\n" +
            "Cache-Control=max-age=0";

    /**
     * Private constructor to prevent instantiation (utility class)
     */
    private Config() {
    }

    /**
     * Initializes and loads multiple configuration files from the specified absolute path.
     * Reads all configuration files defined in Constant.CONFIGS and loads them into a map.
     *
     * @param absolutePath The absolute directory path containing configuration files
     * @return Map of configuration maps, keyed by configuration name
     * @throws RuntimeException if the path is not absolute
     */
    public static Map<String, Map<String, String>> initConfig(String absolutePath) {
        if (!IoUtil.isAbsolutePath(absolutePath)) {
            throw new RuntimeException("path must be absolute path");
        }
        String[] configs = Constant.CONFIGS;
        Map<String, Map<String, String>> maps = new HashMap<>(configs.length);
        for (String configName : configs) {
            maps.put(configName, configToMap(absolutePath, configName));
        }
        return maps;
    }

    /**
     * Loads a configuration file from the specified path.
     * Handles special case for headers configuration by merging with cookies.txt content.
     *
     * @param path The directory path containing the configuration file
     * @param configName The name of the configuration file (without .properties suffix)
     * @return Properties object containing the configuration, or empty Properties if file doesn't exist
     */
    public static Properties getConfig(String path, String configName) {
        try {
            File file = null;
            Properties properties = new Properties();
            if ((file = createConfigFile(path, configName)) == null) {
                System.out.println("place config " + configName);
                return properties;
            }
            properties.load(new FileReader(file));
            if (file.getName().equals(check(Constant.headers))) {
                String cookieInfo = getCookieContent(path, properties);
                properties.setProperty("Cookie", cookieInfo);
            }
            return properties;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates or verifies existence of a configuration file.
     * If the file doesn't exist, creates it with default template.
     *
     * @param path The directory path for the configuration file
     * @param configName The name of the configuration file
     * @return File object representing the configuration file, or null if creation failed
     */
    private static File createConfigFile(String path, String configName) {
        configName = check(configName);

        // if path contains xxxx.properties 将读取
        if (path.contains(configName)) {
            configName = path;
        } else {
            configName = path + File.separator + configName;
        }
        return createConfigFile(new File(configName));
    }

    /**
     * Creates or verifies existence of a configuration file (class-specific version).
     *
     * @param file The configuration file to create/verify
     * @param c The class associated with the configuration
     * @return File object representing the configuration file, or null if creation failed
     */
    @SuppressWarnings("all")
    private static File createConfigFile(File file, Class<?> c) {
        return createConfigFile(file);
    }

    /**
     * Creates or verifies existence of a configuration file.
     * Also ensures cookies.txt exists in the same directory.
     *
     * @param file The configuration file to create/verify
     * @return File object representing the configuration file, or null if creation failed
     */
    @SuppressWarnings("all")
    private static File createConfigFile(File file) {
        BufferedWriter writer = null;
        try {
            if (file.exists()) {
                return file;
            }
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(defaultTemplate);
            writer.flush();
            writer.close();
            System.out.println("create " + file.getAbsolutePath() + " success !");

            // check cookies.txt
            File cookiesFile = new File(parentFile.getAbsolutePath() + File.separator + Constant.cookies + ".txt");
            if (!cookiesFile.exists()) {
                cookiesFile.createNewFile();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IoUtil.close(writer);
        }
    }

    /**
     * Converts specific properties from a Properties object to a Map.
     *
     * @param config The Properties object to convert
     * @param names The property names to include in the map
     * @return Map containing the specified properties
     */
    public static Map<String, String> configToMap(Properties config, String[] names) {
        Map<String, String> map = new HashMap<>();
        if (config == null) {
            return map;
        }
        for (String name : names) {
            if (!StringUtils.strictIsEmpty(name)) {
                String property = config.getProperty(name);
                if (!StringUtils.strictIsEmpty(property)) {
                    map.put(name, config.getProperty(name));
                }
            }
        }
        return map;
    }

    /**
     * Loads and converts a configuration file to a Map.
     *
     * @param path The directory path containing the configuration file
     * @param configName The name of the configuration file
     * @return Map containing all properties from the configuration file
     */
    public static Map<String, String> configToMap(String path, String configName) {
        Properties config = getConfig(path, configName);
        if (config == null) {
            return new HashMap<>(0);
        }
        return configToMap(config);
    }

    /**
     * Gets cookie content, prioritizing cookies.txt over properties file.
     *
     * @param configPath The directory path containing the cookie files
     * @param properties The Properties object containing configuration
     * @return The cookie string
     * @throws RuntimeException if path is not absolute or cookie is invalid
     */
    private static String getCookieContent(String configPath, final Properties properties) {
        configPath = configPath.contains(cookieFile) ? configPath : (configPath + File.separator + cookieFile);
        if (!IoUtil.isAbsolutePath(configPath)) {
            throw new RuntimeException("place use absolute path !");
        }
        String s = IoUtil.readContent(configPath);
        // from cookies.txt read content is must high
        if (s.length() > 0) {
            s = ReflectUtils.toString(s);
            checkCookie(s, configPath);
            return s;
        }
        s = properties.getProperty("Cookie");
        if (s != null && s.length() > 0) {
            s = ReflectUtils.toString(s);
            checkCookie(s, configPath);
            return s;
        }

        // check cookie name is error try cookies
        s = properties.getProperty(Constant.cookies);
        if (s.length() > 0) {
            s = ReflectUtils.toString(s);
            checkCookie(s, configPath);
            return s;
        }
        // if cookies.txt not exist create it
        IoUtil.createFile(configPath + cookieFile);
        System.out.println("place config your cookie in cookies.txt or !" + Constant.headers + ".properties file !");
        return "";
    }

    /**
     * Validates a cookie string.
     *
     * @param cookie The cookie string to validate
     * @param path The path where the cookie was read from (for error message)
     * @throws RuntimeException if cookie is invalid
     */
    private static void checkCookie(String cookie, String path) {
        if (cookie == null || cookie.length() == 0 || cookie.equalsIgnoreCase("cookie") || cookie.equalsIgnoreCase(Constant.cookies)) {
            throw new RuntimeException("place check your cookie ! in " + path);
        }
    }

    /**
     * Ensures a filename ends with .properties suffix.
     *
     * @param name The filename to check
     * @return The filename with .properties suffix
     */
    private static String check(String name) {
        return name.endsWith(config_suffix) ? name : (name + config_suffix);
    }

    /**
     * Converts all properties in a Properties object to a Map.
     *
     * @param config The Properties object to convert
     * @return Map containing all non-empty properties
     */
    public static Map<String, String> configToMap(Properties config) {
        if (config == null) {
            return null;
        }
        Set<Map.Entry<Object, Object>> entries = config.entrySet();
        Map<String, String> map = new HashMap<String, String>(config.size());
        for (Map.Entry<Object, Object> entry : entries) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());

            // ignore null str
            if (StringUtils.strictIsEmpty(key) || StringUtils.strictIsEmpty(value)) {
                continue;
            }

            map.put(key, value);
        }
        return map;
    }
}