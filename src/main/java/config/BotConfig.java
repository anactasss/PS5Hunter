package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotConfig {
    private static Properties properties;
    public static String BOT_TOKEN = null;
    public static String BOT_USER = null;

    public BotConfig() {
        Properties properties = getProperties();
        BOT_TOKEN = properties.getProperty("BOT_TOKEN");
        BOT_USER = properties.getProperty("BOT_USER");
    }

    private Properties getProperties() {
        InputStream inputStream = getClass().getResourceAsStream("application.properties");
        try {
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            return new Properties();
        }
    }
}
