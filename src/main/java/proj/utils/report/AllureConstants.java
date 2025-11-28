package proj.utils.report;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureConstants {
    // Paths > final - static
    public static final Path USER_DIR = Paths.get(System.getProperty("user.dir"));
    public static final Path USER_HOME = Paths.get(System.getProperty("user.home"));

    // Allure paths
    public static final Path RESULTS_FOLDER = USER_DIR.resolve("test-output").resolve("allure-results");
    public static final Path REPORT_PATH = USER_DIR.resolve("test-output").resolve("reports"); // single-file report
    public static final Path FULL_REPORT_PATH = USER_DIR.resolve("test-output").resolve("full-report");

    // History folders
    public static final Path HISTORY_FOLDER = FULL_REPORT_PATH.resolve("history"); // after Allure generates report
    public static final Path RESULTS_HISTORY_FOLDER = RESULTS_FOLDER.resolve("history"); // copy generated results

    // Report naming
    public static final String INDEX_HTML = "index.html";
    public static final String REPORT_PREFIX = "AllureReport_";
    public static final String REPORT_EXTENSION = ".html";

    // Allure binary constants
    public static final String ALLURE_ZIP_BASE_URL =
            "https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/";
    public static final Path EXTRACTION_DIR = USER_HOME.resolve(".m2/repository/allure");
}
