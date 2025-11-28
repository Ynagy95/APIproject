package proj.utils.report;

import io.qameta.allure.Allure;
import proj.utils.logs.LogsManager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;

public class AllureAttachmentManager {

    /**
     * Attach API request and response to Allure report.
     *
     * @param testMethodName Name of the test method
     * @param requestBody    Request payload (JSON/XML/etc.)
     * @param responseBody   Response payload (JSON/XML/etc.)
     * @param requestHeaders Optional request headers
     * @param responseStatus Optional HTTP status code
     */
    public static void attachAPILog(String testMethodName,
                                    String requestBody,
                                    String responseBody,
                                    String requestHeaders,
                                    int responseStatus) {

        try {
            // Attach request
            String requestContent =
                    "Request Headers:\n" + (requestHeaders != null ? requestHeaders : "") +
                            "\n\nRequest Body:\n" + (requestBody != null ? requestBody : "");

            Allure.addAttachment(testMethodName + " - Request", requestContent);

            // Attach response
            String responseContent =
                    "HTTP Status: " + responseStatus +
                            "\n\nResponse Body:\n" + (responseBody != null ? responseBody : "");

            Allure.addAttachment(testMethodName + " - Response", responseContent);

            LogsManager.info("Attached API logs for test: " + testMethodName);

        } catch (Exception e) {
            LogsManager.error("Error attaching API logs", e.getMessage());
        }
    }

    /**
     * Attach log file to Allure report.
     */
    public static void attachLogs() {
        try {
            File logFile = Path.of(LogsManager.LOGS_PATH, "logs.log").toFile();

            if (logFile.exists()) {
                Allure.addAttachment(
                        "logs.log",
                        Files.readString(logFile.toPath(), StandardCharsets.UTF_8)
                );

                LogsManager.info("Attached logs successfully.");
            } else {
                LogsManager.warn("Log file not found: " + logFile.getAbsolutePath());
            }

        } catch (Exception e) {
            LogsManager.error("Error attaching logs", e.getMessage());
        }
    }
}
