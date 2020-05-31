package hackathon.headspin.automator;

import hackathon.headspin.automator.utils.Properties;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * wrapper around WebDriver - for interaction and reporting
 * adds additional capability - ie browser creation based on config
 */
public class Browser {

    // for parellel execution if required in future
    private static ThreadLocal<WebDriver> webDriver = new InheritableThreadLocal<WebDriver>();

    /**
     * to get driver specific to current thread(in parallel execution)
     * @return
     */
    public static WebDriver getDriver() {
        return webDriver.get();
    }

    /**
     * Starts browser w.r.t browser name
     * @param browserName
     * @return
     */
    protected static WebDriver startDriver(String browserName) {

        System.err.println("Create Driver: "+browserName);

        WebDriver driver = null;

        try {
            switch(browserName.trim().toLowerCase()){
                case BrowserType.CHROME:
                    driver = setupChrome();
                    break;
                default:
                    driver = setupChrome();
                    break;
            }
        } catch(Error error){
            System.out.println("Error: "+error.getMessage());
            throw error;
        }

        driver.manage().timeouts().implicitlyWait(Long.valueOf(
                Properties.getPreference("webdriver.timeouts.implicitlyWait")), TimeUnit.SECONDS);
        driver.switchTo().activeElement().sendKeys(Keys.chord(Keys.CONTROL,"0")); //zoom to 100%

        if(Properties.getPreference("webdriver.window.maximize", "false").equals("true"))
            driver.manage().window().maximize();

        return setDriver(driver);
    }

    /**
     * automation is halted if driver creation is failed
     * @param driver
     * @return
     */
    public static WebDriver setDriver(WebDriver driver) {
        try {
            assert(driver!=null);
            webDriver.set(driver);
        } catch(Error error){
            throw new Error("Driver is null");
        }
        return getDriver();
    }

    /**
     * creates and returns driver - based on different config from properties
     * @return
     */
    private static WebDriver setupChrome() {

        WebDriver cdriver ;
        File file = new File("src/main/resources/chromedriver");
        if(!file.exists()) {
            System.err.println("Chrome Driver not found");
        }
        String driverPath = file.getAbsolutePath();
        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreZoomSetting", true);
        options.setCapability("ignoreProtectedModeSettings", true);
        options.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

        if(Boolean.parseBoolean(Properties.getPreference("headless", "false"))) {
            options.addArguments("--headless");
        }

        cdriver = new ChromeDriver(options);
        return cdriver;
    }

    /**
     * ends sessions/closes driver if session is active
     */
    public static void endDriver() {
        if(getDriver()!=null) {
            getDriver().quit();
        }
     }

    /**
     * returns caps - for logging details only
     * @return
     */
    public static Capabilities getCapabilities() {
        return ((RemoteWebDriver) getDriver()).getCapabilities();
    }

    public static WebDriver getRemoteDriver() {
        return (RemoteWebDriver) getDriver();
    }
}
