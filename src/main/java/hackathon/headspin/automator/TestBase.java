package hackathon.headspin.automator;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import hackathon.headspin.automator.report.ExtentManager;
import hackathon.headspin.automator.utils.Properties;
import org.openqa.selenium.Capabilities;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.IOException;

/**
 * base class to be extended by all testcases - takes care of browser launch and close
 */
public class TestBase extends TestListenerAdapter {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal();

    public static ExtentTest getReportInstance() {
        return test.get();
    }

    @BeforeSuite
    public void beforeSuite() {
        extent = ExtentManager.createInstance("extent.html");
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent.html");
        extent.attachReporter(htmlReporter);
    }

    @BeforeTest
    public void startTest(ITestContext context) throws IOException {

        ExtentTest testCase = extent.createTest(getClass().getName());
        test.set(testCase);

        String browserName = Properties.getPreference("browser", "chrome");
        System.err.println("Test Run mode");
        Browser.startDriver(browserName);

        Capabilities caps = Browser.getCapabilities();
        System.out.println("Running: [" + browserName + "] " + caps.getBrowserName());
    }

    @AfterTest
    public static void endTest(ITestContext context) throws InterruptedException {
        extent.flush();
        Browser.endDriver();
    }

}
