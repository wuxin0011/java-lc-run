package code_generation.config;

import code_generation.utils.IoUtil;
import code_generation.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * The LocalConfig class provides a mechanism for loading and managing configuration settings
 * from a properties file named "config.properties". This class is designed to initialize static
 * configuration values that can be used throughout the application. It supports overriding default
 * values with those specified in the configuration file, ensuring flexibility and adaptability
 * for different environments or user preferences.
 *
 * Upon initialization, the class attempts to load the configuration file from the classpath. If
 * successful, it reads specific properties such as root directories, username, and request
 * configuration directory, and updates the corresponding static fields. If a property is not
 * defined or contains invalid values (e.g., null or empty strings), the default values are retained.
 *
 * The class also ensures that the request configuration directory is an absolute path. If a relative
 * path is provided, it is resolved relative to the working directory of the application.
 *
 * Note: Any IOExceptions encountered during the loading of the configuration file are ignored,
 * and the default values remain unchanged in such cases.
 */
public class LocalConfig {

    /**
     * The name of the configuration file used by the application to load properties.
     * This file is expected to be located in the classpath and contains key-value pairs
     * that define various configuration settings such as root directories, username, and
     * request configuration directory. If the file is not found or cannot be loaded, default
     * values are retained. The file is loaded during the static initialization of the containing
     * class, and its properties are used to override default settings where applicable.
     */
    private static final String CONFIG_NAME =  "config.properties";
    /**
     * Represents the username used for authentication or identification purposes.
     * This value is typically utilized in configurations where user-specific information
     * is required. The username is static and shared across all instances of the containing class.
     */
    public static  String USER_NAME =  "wuxin0011";
    /**
     * An array of strings representing the root directory names used in the project structure.
     * These directories serve as the foundational folders for organizing source code and related resources.
     * The order and naming of these directories are significant for maintaining consistency across the project.
     */
    public static  String[] ROOT_DIRS =  {"src", "main", "java"};
    /**
     * Represents the directory path where request configuration files are stored.
     * This variable holds the location from which request-specific configurations
     * are loaded or managed. The default value is set to the current directory (".").
     */
    public static  String REQUEST_CONFIG_DIR =  ".";
    /**
     * Represents a static collection of properties used for configuration purposes.
     * This variable holds key-value pairs that can be utilized to manage application settings.
     * The properties can be loaded from various sources such as files, streams, or manually set.
     * If not initialized, it remains null, indicating no configuration data is available.
     */
    public static Properties PROPERTIES = null ;
    /**
     * Indicates whether a README file should be created.
     * If set to true, the system will generate a README file during the appropriate process.
     * If set to false, the creation of the README file will be skipped.
     */
    public static boolean CREATE_README_FILE = true ;
    static {
        InputStream resourceAsStream = null;
        try {
            // user config
            resourceAsStream = LocalConfig.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
            if(resourceAsStream != null) {
                PROPERTIES = new Properties();
                PROPERTIES.load(resourceAsStream);

                // merge
                if(valueIsNotEmpty(PROPERTIES, "root_dir")) {
                    ROOT_DIRS = PROPERTIES.getProperty("root_dir").replace("[","").replace("]","").split(",");
                }

                if(valueIsNotEmpty(PROPERTIES, "username")) {
                    USER_NAME = PROPERTIES.getProperty("username");
                }

                if(valueIsNotEmpty(PROPERTIES, "request_config")) {
                    REQUEST_CONFIG_DIR = PROPERTIES.getProperty("request_config");
                }
                if(valueIsNotEmpty(PROPERTIES, "create_contest_readme")) {
                    CREATE_README_FILE = Boolean.getBoolean(PROPERTIES.getProperty("create_contest_readme"));
                }
            }
        }catch (IOException ignore){

        }finally {
            IoUtil.close(resourceAsStream);
        }
        if(!IoUtil.isAbsolutePath(REQUEST_CONFIG_DIR)) {
            REQUEST_CONFIG_DIR = IoUtil.getWorkDir() + File.separator + REQUEST_CONFIG_DIR + File.separator + "request_config" + File.separator;
        }
    }


    /**
     * Checks if the specified key in the given Properties object is null or represents a null value.
     * A key is considered null if its value is empty, contains only whitespace, or is the string "null".
     *
     * @param properties the Properties object to check for the key
     * @param Key the key whose value needs to be checked
     * @return true if the key is null or represents a null value, false otherwise
     */
    public static boolean valueIsNotEmpty(Properties properties,String Key) {
        String value = properties.getProperty(Key);
        return !StringUtils.isEmpty(value) && !Objects.equals(value, "null");
    }

}
