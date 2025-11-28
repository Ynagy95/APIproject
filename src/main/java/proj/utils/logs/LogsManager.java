package proj.utils.logs;

import proj.utils.report.AllureConstants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogsManager {

    public static final String LOGS_PATH = AllureConstants.USER_DIR + "/test-output/logs";
    private static Logger logger()
    {
        return Logger.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
    }
    public static void info(String... message)
    {
        logger().info(String.join(" ", message));
    }
    public static void warn(String... message)
    {
        logger().warning(String.join(" ", message));
    }
    public static void error(String... message)
    {
        logger().severe(String.join(" ", message));
    }
    public static void debug(String... message)
    {
        logger().fine(String.join(" ", message));
    }
    public static void fatal(String... message) {
        String joined = String.join(" ", message);
        logger().log(Level.SEVERE, "FATAL: " + joined);
    }
}
