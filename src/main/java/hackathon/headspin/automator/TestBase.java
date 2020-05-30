package hackathon.headspin.automator;

import org.openqa.selenium.Capabilities;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.IOException;

/**
 * base class to be extended by all testcases - takes care of browser launch and close
 */
public class TestBase extends TestListenerAdapter {

    @BeforeTest
    public void startTest(ITestContext context) throws IOException {

        String browserName = Properties.getPreference("browser", "chrome");
        System.err.println("Test Run mode");
        Browser.startDriver(browserName);

        Capabilities caps = Browser.getCapabilities();
        System.out.println("Running: [" + browserName + "] " + caps.getBrowserName());
    }

    @AfterTest
    public static void endTest(ITestContext context) throws InterruptedException {
        Browser.endDriver();
    }

}
