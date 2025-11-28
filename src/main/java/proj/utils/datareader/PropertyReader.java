package proj.utils.datareader;

import proj.utils.logs.LogsManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Properties;

public class PropertyReader {
    // Load Properties function to load all properties files into system properties
    public static Properties loadProperties() {
        Properties properties = new Properties();
        // Specify the directory path for property files
        File[] propertiesFiles = new File("src/main/resources").listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(".properties");
            }
        });

        try {
            for (File file : propertiesFiles) {
                try {
                    properties.load(FileUtils.openInputStream(file));
                } catch (Exception e) {
                    LogsManager.error("Error loading properties file:", file.getName(), e.getMessage());
                }
            }

            // Add all system properties to the properties object
            properties.putAll(System.getProperties());
            System.getProperties().putAll(properties);
        } catch (Exception e) {
            System.out.println("Error loading properties file");
            return null;
        }
        return properties;
    }

    public static String getProperty(String key) {
        try {
            return System.getProperty(key);
        } catch (Exception e){
            LogsManager.error("Error reading property for key:", key, e.getMessage());
            return "";
        }

    }
}
