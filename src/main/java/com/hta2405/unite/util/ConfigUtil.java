package com.hta2405.unite.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static Properties properties;

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties = new Properties();
            if (input == null) {
                throw new IOException("application.properties 파일을 찾을 수 없습니다.");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}