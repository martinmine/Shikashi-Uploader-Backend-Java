package me.shikashi.img;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by marti_000 on 08.06.2015.
 */
public class SystemConfiguration {
    private static final Logger LOGGER = Logger.getLogger(SystemConfiguration.class.getSimpleName());
    private static Properties properties = new Properties();

    private static final SystemConfiguration INSTANCE = new SystemConfiguration();

    public static SystemConfiguration getInstance() {
        return INSTANCE;
    }

    private SystemConfiguration() {
        LOGGER.info("Reading config");
        try (FileInputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public String getProperty(final String key) {
        return properties.getProperty(key);
    }
}
