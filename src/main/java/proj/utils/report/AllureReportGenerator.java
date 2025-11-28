package proj.utils.report;

import org.apache.commons.io.FileUtils;
import proj.utils.OperatingSystem;
import proj.utils.TerminalUtils;
import proj.utils.TimeStamp;
import proj.utils.logs.LogsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static proj.utils.datareader.PropertyReader.getProperty;
import static proj.utils.report.AllureConstants.*;

public class AllureReportGenerator {

    public static void generateReports(boolean isSingleFile) {
        LogsManager.info("========== Generating Allure Report ==========");

        try {
            // 1️⃣ Ensure Allure binary is available
            AllureBinaryManager.downloadAndExtract();

            Path outputFolder = isSingleFile ? REPORT_PATH : FULL_REPORT_PATH;
            Files.createDirectories(outputFolder);

            // 2️⃣ Build Allure command
            List<String> command = new ArrayList<>();
            command.add(AllureBinaryManager.getExecutable().toString());
            command.add("generate");
            command.add(RESULTS_FOLDER.toString());
            command.add("-o");
            command.add(outputFolder.toString());
            command.add("--clean");
            if (isSingleFile) command.add("--single-file");

            LogsManager.info("Running Allure command: " + String.join(" ", command));

            // 3️⃣ Run process manually (captures logs automatically)
            try {
                LogsManager.info("Generating Allure report automatically...");

                ProcessBuilder builder = new ProcessBuilder(command);
                builder.redirectErrorStream(true);
                Process process = builder.start();

                // Log every line from process output
                new BufferedReader(new InputStreamReader(process.getInputStream()))
                        .lines()
                        .forEach(LogsManager::info);

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    LogsManager.info("✅ Allure report generated successfully at: " + outputFolder);
                } else {
                    LogsManager.error("❌ Allure generation failed. Exit code: " + exitCode);
                }

            } catch (Exception e) {
                LogsManager.error("⚠️ Failed to generate Allure report automatically: " + e.getMessage());
            }

        } catch (Exception e) {
            LogsManager.error("Failed to generate Allure report", e.getMessage());
        }

        LogsManager.info("==============================================");
    }

    public static String renameReport() {
        String newFileName = REPORT_PREFIX + TimeStamp.getTimestamp() + REPORT_EXTENSION;
        Path oldPath = REPORT_PATH.resolve(INDEX_HTML);
        Path newPath = REPORT_PATH.resolve(newFileName);

        try {
            if (Files.exists(oldPath)) {
                Files.move(oldPath, newPath);
                LogsManager.info("Renamed report to: " + newFileName);
            } else {
                LogsManager.warn("index.html not found, skipping rename.");
            }
        } catch (IOException e) {
            LogsManager.error("Error renaming report", e.getMessage());
        }

        return newFileName;
    }

    public static void openReport(String reportFileName) {
        try {
            if (!getProperty("OpenAllureReportAfterExecution").equalsIgnoreCase("true")) {
                LogsManager.info("Auto-open disabled. Skipping.");
                return;
            }

            Path reportPath = REPORT_PATH.resolve(reportFileName);
            if (!Files.exists(reportPath)) {
                LogsManager.warn("Report file not found: " + reportPath);
                return;
            }

            switch (OperatingSystem.getOperatingSystemType()) {
                case WINDOWS -> TerminalUtils.executeTerminalCommand("cmd.exe", "/c", "start", reportPath.toString());
                case MAC, LINUX -> TerminalUtils.executeTerminalCommand("open", reportPath.toString());
                default -> LogsManager.warn("Opening Allure Report not supported on this OS.");
            }

        } catch (Exception e) {
            LogsManager.error("Error opening Allure Report", e.getMessage());
        }
    }

    public static void copyHistory() {
        try {
            if (!Files.exists(HISTORY_FOLDER) || !Files.exists(RESULTS_HISTORY_FOLDER.getParent())) {
                Files.createDirectories(RESULTS_HISTORY_FOLDER);
            }

            FileUtils.copyDirectory(HISTORY_FOLDER.toFile(), RESULTS_HISTORY_FOLDER.toFile());
            LogsManager.info("Copied history data successfully.");
        } catch (Exception e) {
            LogsManager.error("Error copying history files", e.getMessage());
        }
    }
}
