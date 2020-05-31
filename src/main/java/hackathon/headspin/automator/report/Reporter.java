package hackathon.headspin.automator.report;

import com.aventstack.extentreports.Status;
import hackathon.headspin.automator.TestBase;

public class Reporter {

    public static void log(Status status, String message) {
        TestBase.getReportInstance().log(status, message);
    }

    public static void log(boolean success, String message) {
        if (success) {
            TestBase.getReportInstance().pass(message);
        } else {
            TestBase.getReportInstance().fail(message);
        }
    }
}
