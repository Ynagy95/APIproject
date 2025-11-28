package proj.listeners;

import io.qameta.allure.Allure;
import org.testng.*;
import proj.FileUtils;
import proj.utils.datareader.PropertyReader;
import proj.utils.logs.LogsManager;
import proj.utils.report.AllureAttachmentManager;
import proj.utils.report.AllureConstants;
import proj.utils.report.AllureEnvironmentManager;
import proj.utils.report.AllureReportGenerator;

import java.io.File;

public class TestNGListeners implements ITestNGListener, ISuiteListener, IExecutionListener, IInvokedMethodListener, ITestListener {

    public void onStart(ISuite suite) {
        suite.getXmlSuite().setName("API Test Suite");
    }

    public void onExecutionStart() {
        LogsManager.info("Test Execution started");

        cleanTestOutputDirectories();
        LogsManager.info("Directories cleaned");

        createTestOutputDirectories();
        LogsManager.info("Directories created");

        PropertyReader.loadProperties();
        LogsManager.info("Properties loaded");

        AllureEnvironmentManager.setEnvironmentVariables();
        LogsManager.info("Allure environment set");
    }

    public void onExecutionFinish() {
        AllureReportGenerator.generateReports(false);
        AllureReportGenerator.copyHistory();
        LogsManager.info("History copied");
        AllureReportGenerator.generateReports(true);
        String newFileName = AllureReportGenerator.renameReport();
        AllureReportGenerator.openReport(newFileName);
        LogsManager.info("Test Execution Finished");
    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            LogsManager.info("API Test Case " + testResult.getName() + " started");
        }
    }

    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {

            String fullLog = (String) result.getAttribute("requestResponseLog");
            String responseBody = (String) result.getAttribute("responseBody");
            int status = result.getAttribute("statusCode") != null ? (int) result.getAttribute("statusCode") : 0;

            Allure.addAttachment(result.getName() + " - Request/Response", fullLog);
            AllureAttachmentManager.attachAPILog(
                    result.getName(),
                    fullLog,          // request + raw log
                    responseBody,     // extracted body
                    "",               // headers if needed
                    status
            );
            AllureAttachmentManager.attachLogs();
        }
    }


    public void onTestSuccess(ITestResult result) {
        LogsManager.info("API Test case passed: " + result.getName());
    }

    public void onTestFailure(ITestResult result) {
        LogsManager.info("API Test case failed: " + result.getName());
    }

    public void onTestSkipped(ITestResult result) {
        LogsManager.info("API Test case skipped: " + result.getName());
    }

    public void onFinish(ISuite suite) { }

    private void cleanTestOutputDirectories() {
        FileUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());
        FileUtils.cleanDirectory(new File(LogsManager.LOGS_PATH));
    }

    private void createTestOutputDirectories() {
        // Just create logs folder for API tests
        FileUtils.createDirectory(LogsManager.LOGS_PATH);
    }
}
