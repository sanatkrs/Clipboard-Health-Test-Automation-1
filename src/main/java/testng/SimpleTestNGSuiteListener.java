package testng;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import common.TestResultConstants;
import config.OS;
import config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
public class SimpleTestNGSuiteListener extends TestListenerAdapter implements ISuiteListener {

    ExtentSparkReporter htmlReporter;
    ExtentReports reports;
    ExtentTest test;


    @Override
    public void onTestFailure(ITestResult testResult) {
        recordResults(testResult);
        super.onTestFailure(testResult);
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        recordResults(testResult);
        super.onTestSkipped(testResult);
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
        recordResults(testResult);
        super.onTestSuccess(testResult);
    }

    public void configureReport() {
        htmlReporter = new ExtentSparkReporter("report.html");
        reports = new ExtentReports();
        reports.attachReporter(htmlReporter);

        reports.setSystemInfo("OS Name:", OS.NAME);
        reports.setSystemInfo("Browser:", TestConfig.PLATFORM_NAME);
        reports.setSystemInfo("Test Automation Performed on:", TestConfig.URL);
        reports.setSystemInfo("Tested By:", "Sanat Kr Singh");

        htmlReporter.config().setDocumentTitle("Clipboard Health Test Automation Report");
        htmlReporter.config().setReportName("Clipboard Health");
        htmlReporter.config().setTheme(Theme.DARK);
    }

    private void recordResults(ITestResult testResult) {
        Instant start = Instant.now();

        test = reports.createTest(testResult.getName());
        if (statusIsFailed(testResult))
            test.log(Status.FAIL, MarkupHelper.createLabel(testResult.getName(), ExtentColor.RED));
        else if (statusIsPassed(testResult))
            test.log(Status.PASS, MarkupHelper.createLabel(testResult.getName(), ExtentColor.GREEN));
        else if (statusIsSkipped(testResult))
            test.log(Status.SKIP, MarkupHelper.createLabel(testResult.getName(), ExtentColor.ORANGE));



        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.debug("[{}ms] Recorded Test Result", duration.toMillis());
    }

    @Override
    public void onStart(ISuite suite) {
        configureReport();
        printHeaderForSuite(suite, "START");
        List<ITestNGMethod> methods = suite.getAllMethods();
        methods.forEach(iInvokedMethod -> log.info(
                "[START][{}] - {}",
                iInvokedMethod.getMethodName(),
                iInvokedMethod.getConstructorOrMethod().getParameterTypes()));

      //  AutomationStateManager.getTestResultsContainer().startTestWithCount(methods.size());
    }

    @Override
    public void onFinish(ISuite suite) {
        printTestResultsWithTimer(suite);
        printHeaderForSuite(suite, "DONE");
        reports.flush();
    }

    private Integer getTotalTestCount() {
        return getFailedTests().size() + getPassedTests().size() + getSkippedTests().size();
    }

    private void printTestResultsWithTimer(ISuite suite) {
        Instant start = Instant.now();
        log.info("{} completed testing -- now processing results...", suite.getName());

        printTestResults(suite);

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.info("{} completed results generation in {} min. ({}s)", suite.getName(), duration.toMinutes(), duration.getSeconds());
    }

    public void printHeaderForSuite(ISuite suite, String start) {
        String startMessage = String.format("[%s] - %s Suite", start, suite.getName());
        log.info("\n\n========="
                + "\n{}\n"
                + "=========\n", startMessage);
    }

    public void printTestResults(ISuite suite) {
        suite.getResults().forEach((testName, testResult) -> {
            logTestResultsIfPresent(
                    "Tests that Passed",
                    testResult.getTestContext().getPassedTests());

            logTestResultsIfPresent(
                    "Tests that were skipped:",
                    testResult.getTestContext().getSkippedTests());

            logForFailedTestsIfPresent(testResult);
        });
    }

    public void logForFailedTestsIfPresent(ISuiteResult testResult) {
        IResultMap results = testResult.getTestContext().getFailedTests();

        if (results.size() == 0)
            return;

        logTestResultsIfPresent(
                "Tests that Failed:", results);

    }

    public void logTestResultsIfPresent(String header, IResultMap passedTests) {
        if (passedTests.size() != 0) {
            log.info("\n\n---\n{} [{}]:\n---", header, passedTests.size());
            printTestResultsAndArgs(passedTests);
        }
    }

    private void printFailedTestCauses(IResultMap testResultsMap) {
        testResultsMap.getAllResults().forEach(this::logFailedTestCause);
    }

    private void logFailedTestCause(ITestResult iTestResult) {
        String message = getArgsAsString(iTestResult);

        if (statusIsFailed(iTestResult))
            logMessageForTest(iTestResult, message + (" - " + iTestResult.getThrowable().getMessage()));
    }

    private void printTestResultsAndArgs(IResultMap testResultsMap) {
        testResultsMap.getAllResults().forEach(this::logTestResult);
    }

    private void logTestResult(ITestResult iTestResult) {
        String message = getArgsAsString(iTestResult);
        logMessageForTest(iTestResult, message);
    }

    private boolean statusIsFailed(ITestResult iTestResult) {
        String status = getStatusType(iTestResult.getStatus());
        return StringUtils.equals(TestResultConstants.TEST_RESULT_NAME_FAILED, status);
    }

    private boolean statusIsPassed(ITestResult iTestResult) {
        String status = getStatusType(iTestResult.getStatus());
        return StringUtils.equals(TestResultConstants.TEST_RESULT_NAME_PASSED, status);
    }

    private boolean statusIsSkipped(ITestResult iTestResult) {
        String status = getStatusType(iTestResult.getStatus());
        return StringUtils.equals(TestResultConstants.TEST_RESULT_NAME_SKIPPED, status);
    }

    private void logMessageForTest(ITestResult iTestResult, String message) {
        log.info(
                "[{}][{}]{}",
                getStatusType(iTestResult.getStatus()),
                iTestResult.getMethod().getMethodName(),
                message);
    }

    private String getArgsAsString(ITestResult iTestResult) {
        StringBuilder args = new StringBuilder();
        Object[] params = iTestResult.getParameters();
        if (params.length == 0)
            return "";

        args.append("[");
       // args.append(CommonUtils.getCSVForStrings(params));
        args.append("]");

        return args.toString();
    }

    private String getStatusType(int status) {
        switch (status) {
            case 1:
                return TestResultConstants.TEST_RESULT_NAME_PASSED;
            case 2:
                return TestResultConstants.TEST_RESULT_NAME_FAILED;
            case 3:
                return TestResultConstants.TEST_RESULT_NAME_SKIPPED;
            default:
                return "UNKNOWN";
        }
    }
}
