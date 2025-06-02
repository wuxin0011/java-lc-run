package code_generation.crwal.request;

import code_generation.crwal.Constant;
import code_generation.utils.IoUtil;
import code_generation.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A utility class for making HTTP requests with configurable headers and connection settings.
 * Supports GET and POST methods with various content types and parameter handling.
 * @author wuxin0011
 * @since 1.0
 */
public class Request {

    /**
     * Content-Type for JSON requests
     */
    public static final String applicationJSON = "application/json";

    /**
     * Content-Type for form-urlencoded requests
     */
    public static final String applicationFORM = "application/x-www-form-urlencoded";

    /**
     * Content-Type for plain text requests
     */
    public static final String applicationText = "application/text; charset=utf-8";

    /**
     * Content-Type for HTML requests
     */
    public static final String applicationHtml = "text/html; charset=utf-8";

    /**
     * Buffer size for reading responses (1MB)
     */
    public static final byte[] buff = new byte[1024 * 1024];

    /**
     * Default configuration directory name
     */
    private static final String IGNORE_DIR = File.separator + "request_config" + File.separator;

    /**
     * HTTP headers to include in requests
     */
    private Map<String, String> headers;

    /**
     * Associated class for configuration
     */
    private Class<?> aClass;

    /**
     * Path to configuration files
     */
    private String configPath;

    /**
     * Constructs a Request with default configuration (using Request.class)
     */
    public Request() {
        this(Request.class);
    }

    /**
     * Constructs a Request with configuration based on the specified class.
     * @param aClass The class used to determine configuration path (cannot be null)
     * @throws NullPointerException if aClass is null
     */
    public Request(Class<?> aClass) {
        this.aClass = Objects.requireNonNull(aClass, "class not allow null");
        this.configPath = System.getProperty("user.dir") + File.separator + IGNORE_DIR;
        checkConfig();
    }

    /**
     * Constructs a Request with custom configuration path.
     * @param configPath Absolute path to configuration directory
     */
    public Request(String configPath) {
        this.configPath = configPath;
        checkConfig();
    }

    /**
     * Validates and loads configuration from the configPath.
     * @throws RuntimeException if configPath is not absolute
     */
    public void checkConfig() {
        if (!IoUtil.isAbsolutePath(configPath)) {
            throw new RuntimeException("place use absolutePath !");
        }
        Map<String, Map<String, String>> maps = Config.initConfig(configPath);
        this.headers = maps == null || maps.size() == 0 ? null : maps.get(Constant.headers);
    }

    /**
     * Gets the associated class for this Request.
     * @return The associated class
     */
    public Class<?> getaClass() {
        return aClass;
    }

    /**
     * Gets the current request headers.
     * @return Map of header name-value pairs
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets custom request headers.
     * @param headers Map of header name-value pairs
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Gets the configuration path.
     * @return Absolute path to configuration directory
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * Initializes an HttpURLConnection with default headers.
     * @param connection The connection to initialize
     */
    public void initHttpURLConnection(HttpURLConnection connection) {
        initHttpURLConnection(connection, false);
    }

    /**
     * Initializes an HttpURLConnection with optional cookie removal.
     * @param connection The connection to initialize
     * @param removeCookie Whether to remove cookie headers
     * @throws NullPointerException if connection is null
     */
    public void initHttpURLConnection(HttpURLConnection connection, boolean removeCookie) {
        Objects.requireNonNull(connection, "HttpURLConnection connection not allow null !");
        if (removeCookie) {
            headers.remove("cookie");
            headers.remove("Cookie");
        }
        if (headers != null && headers.size() > 0) {
            this.headers.forEach(connection::setRequestProperty);
        }
    }

    /**
     * Creates a new HttpURLConnection for the given URL.
     * @param url The URL to connect to
     * @return New HttpURLConnection instance, or null if connection fails
     */
    public static HttpURLConnection getConnection(String url) {
        try {
            if (!(url.contains("api/info/") || url.contains("https://leetcode.cn/graphql/"))) {
                System.out.println("access url : " + url);
            }
            URL apiUrl = new URL(url);
            return (HttpURLConnection) apiUrl.openConnection();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Performs a GET request with default content type.
     * @param url The URL to request
     * @return Response body as String
     */
    public String requestGet(String url) {
        return requestGet(url, null, null);
    }

    /**
     * Performs a GET request with specified Accept header.
     * @param url The URL to request
     * @param Accept The Accept header value
     * @return Response body as String
     */
    public String requestGet(String url, String Accept) {
        return requestGet(url, Accept, null);
    }

    /**
     * Performs a GET request with parameters.
     * @param url The URL to request
     * @param Accept The Accept header value
     * @param params Query parameters as name-value pairs
     * @return Response body as String
     */
    public String requestGet(String url, String Accept, Map<String, String> params) {
        url = wrapperUrl(url, params);
        HttpURLConnection connection = getConnection(url);
        initHttpURLConnection(connection);
        if (connection != null) {
            connection.setRequestProperty("Content-Type", Accept == null ? applicationHtml : Accept);
            return requestGet(connection);
        }
        return "";
    }

    /**
     * Performs a GET request using a pre-configured connection.
     * @param connection The configured HttpURLConnection
     * @return Response body as String
     * @throws RuntimeException if response code is not HTTP_OK (200)
     */
    public String requestGet(HttpURLConnection connection) {
        try {
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException(" response error ! code = " + connection.getResponseCode());
            }
            return response(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Performs a JSON POST request.
     * @param url The URL to request
     * @param jsonStr The JSON body content
     * @return Response body as String
     */
    public String requestPost(String url, String jsonStr) {
        return requestPost(url, applicationJSON, jsonStr, null);
    }

    /**
     * Performs a POST request with custom content type and parameters.
     * @param url The URL to request
     * @param ContentType The Content-Type header value
     * @param jsonStr The request body content
     * @param params Additional parameters
     * @return Response body as String
     */
    public String requestPost(String url, String ContentType, String jsonStr, Map<String, String> params) {
        return response(requestBefore(url, ContentType, jsonStr, null, params, false));
    }

    /**
     * Builds a URL with query parameters.
     * @param url Base URL
     * @param params Query parameters as name-value pairs
     * @return URL with query string
     */
    public static String wrapperUrl(String url, Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append("?");
        sb.append(buildString(params, "=", "&"));
        return sb.toString();
    }

    /**
     * Builds a cookie header string.
     * @param cookies Cookie name-value pairs
     * @return Formatted cookie string
     */
    public static String buildCookies(Map<String, String> cookies) {
        return buildString(cookies, "=", ";");
    }

    /**
     * Builds a form-urlencoded request body.
     * @param body Form field name-value pairs
     * @return Formatted request body
     */
    public static String buildBody(Map<String, String> body) {
        return buildString(body, "=", "&");
    }

    /**
     * Builds a formatted string from a map.
     * @param map Name-value pairs
     * @param connectionTag Separator between names and values
     * @param endTag Separator between pairs
     * @return Formatted string
     * @throws NullPointerException if any parameter is null
     */
    public static String buildString(Map<String, String> map, String connectionTag, String endTag) {
        Objects.requireNonNull(map, "map not allow null");
        Objects.requireNonNull(connectionTag, "connectionTag not allow null");
        Objects.requireNonNull(endTag, "endTag not allow null");
        try {
            int size = 0;
            int end = map.size();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : map.entrySet()) {
                sb.append(java.net.URLEncoder.encode(item.getKey(), "UTF-8"));
                sb.append(connectionTag);
                sb.append(java.net.URLEncoder.encode(item.getValue(), "UTF-8"));
                if (size != end - 1) {
                    sb.append(endTag);
                }
                size++;
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Reads the response from a connection.
     * @param connection The HttpURLConnection to read from
     * @return Response body as String
     */
    public static String response(HttpURLConnection connection) {
        if (connection == null) {
            return "";
        }
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            int l = -1;
            while ((l = bis.read(buff)) != -1) {
                response.append(new String(buff, 0, l)).append("\n");
            }
            connection.disconnect();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            IoUtil.close(bis);
        }
    }

    /**
     * Writes data to a connection's output stream.
     * @param connection The HttpURLConnection to write to
     * @param jsonStr The data to write
     */
    public static void handlerDataOutPutStream(HttpURLConnection connection, String jsonStr) {
        if (StringUtils.isEmpty(jsonStr) || connection == null) {
            return;
        }
        try {
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonStr);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepares a request before sending.
     * @param url The URL to request
     * @param ContentType The Content-Type header value
     * @param jsonStr The request body content
     * @param methodType The HTTP method (defaults to POST)
     * @param params Additional parameters
     * @param removeCookie Whether to remove cookies
     * @return Configured HttpURLConnection
     * @throws RuntimeException if connection fails
     */
    public HttpURLConnection requestBefore(String url, String ContentType, String jsonStr, String methodType, Map<String, String> params, boolean removeCookie) {
        if (StringUtils.isEmpty(methodType)) {
            methodType = "POST";
        }

        if (methodType.equals("GET")) {
            url = wrapperUrl(url, params);
        }

        try {
            HttpURLConnection connection = getConnection(url);
            if (connection == null) {
                throw new RuntimeException("response Error");
            }
            connection.setRequestProperty("Content-Type", ContentType == null ? applicationJSON : ContentType);
            connection.setRequestMethod(methodType);
            initHttpURLConnection(connection, removeCookie);
            handlerDataOutPutStream(connection, jsonStr);
            if (params != null && params.size() > 0) {
                handlerDataOutPutStream(connection, buildString(params, "=", "&"));
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Prepares and returns a connection for a request.
     * @param url The URL to request
     * @param ContentType The Content-Type header value
     * @param jsonStr The request body content
     * @param params Additional parameters
     * @param removeCookie Whether to remove cookies
     * @return Configured HttpURLConnection
     */
    public HttpURLConnection requestResponse(String url, String ContentType, String jsonStr, Map<String, String> params, boolean removeCookie) {
        return requestBefore(url, ContentType, jsonStr, null, params, removeCookie);
    }

    /**
     * Performs a POST request and returns response headers.
     * @param url The URL to request
     * @param ContentType The Content-Type header value
     * @param jsonStr The request body content
     * @param params Additional parameters
     * @param removeCookie Whether to remove cookies
     * @return Map of response headers
     */
    public Map<String, List<String>> requestPostResponse(String url, String ContentType, String jsonStr, Map<String, String> params, boolean removeCookie) {
        HttpURLConnection connection = requestBefore(url, ContentType, jsonStr, null, params, removeCookie);
        if (connection == null) {
            return null;
        }
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        connection.disconnect();
        return headerFields;
    }

    /**
     * Returns a string representation of this Request.
     * @return String showing headers and associated class
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Request.class.getSimpleName() + "[", "]")
                .add("headers=" + headers)
                .add("aClass=" + aClass)
                .toString();
    }
}