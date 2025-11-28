package proj;

import proj.utils.datareader.PropertyReader;
import proj.utils.logs.LogsManager;

import java.io.File;

public class FileUtils {
    private static final String Userdir = PropertyReader.getProperty("user.dir");
    private FileUtils() {

    }



    public static void createDirectory(String path) {
        try {
            File file = new File(Userdir + path);
            if (!file.exists())
            {
                file.mkdirs();
                LogsManager.info("Directory created: " + path);
            }


}
        catch (Exception e) {
            LogsManager.error("Error creating directory: " + e.getMessage());
        }
    }

    public static void cleanDirectory(File file){

        try {
            org.apache.commons.io.FileUtils.deleteQuietly(file);
        }
        catch (Exception e) {
            LogsManager.error("Error cleaning directory: " + e.getMessage());
        }
    }
}
