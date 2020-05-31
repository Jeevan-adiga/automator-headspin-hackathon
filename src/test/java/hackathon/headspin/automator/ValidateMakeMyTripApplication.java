package hackathon.headspin.automator;

import hackathon.headspin.automator.functions.LandingPageFunctions;
import org.testng.annotations.Test;

public class ValidateMakeMyTripApplication extends TestBase {

    @Test
    public void testCase() throws InterruptedException {

        Browser.getDriver().get("https://www.makemytrip.com/");

        LandingPageFunctions.login("hajejo4438@tashjw.com", "hackathon@2020");

        
    }
}
