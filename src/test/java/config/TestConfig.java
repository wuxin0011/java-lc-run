package config;

import code_generation.config.LocalConfig;
import code_generation.utils.IoUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

public class TestConfig {
    public static void main(String[] args) throws Exception{
        System.out.println(LocalConfig.REQUEST_CONFIG_DIR);
        System.out.println(LocalConfig.USER_NAME);
        System.out.println(Arrays.toString(LocalConfig.ROOT_DIRS));
    }
}
