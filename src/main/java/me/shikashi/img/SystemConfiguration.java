package me.shikashi.img;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides configuration information about the system.
 */
public class SystemConfiguration {
    private static final Logger LOGGER = Logger.getLogger(SystemConfiguration.class.getSimpleName());
    private static Properties properties = new Properties();

    private static final SystemConfiguration INSTANCE = new SystemConfiguration();

    /**
     * @return Singleton instance of {@code SystemConfiguration}.
     */
    public static SystemConfiguration getInstance() {
        return INSTANCE;
    }

    private SystemConfiguration() {
        try (FileInputStream inputStream = new FileInputStream("./config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Gets the property value for a configuration.
     * @param key Key for the config.
     * @return Value for the config option, {@code null} if not found.
     */
    public String getProperty(final String key) {
        /*
        String value;
        if ((value = System.getProperty(key)) != null) {
            return value;
        } else if ((value = System.getenv(key)) != null) {
            return value;
        }*/

        return properties.getProperty(key);
    }
}
