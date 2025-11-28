package proj.utils.report;

import com.google.common.collect.ImmutableMap;
import proj.utils.logs.LogsManager;

import java.io.File;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static proj.utils.datareader.PropertyReader.getProperty;

public class AllureEnvironmentManager {

    public static void setEnvironmentVariables() {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("OS", getProperty("os.name"))
                        .put("Java version", getProperty("java.runtime.version"))
                        .put("Execution Type", getProperty("executionType"))
                        .put("Base URL", getProperty("baseUrl"))
                        .build(),
                String.valueOf(AllureConstants.RESULTS_FOLDER) + File.separator
        );

        LogsManager.info("Allure environment variables set.");
        AllureBinaryManager.downloadAndExtract();
    }
}
