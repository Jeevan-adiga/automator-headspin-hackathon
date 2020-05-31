package hackathon.headspin.automator.utils;

import org.testng.Reporter;

import java.io.File;
import java.io.FileInputStream;

/**
 * logic to read and decide on final value of properties to use
 * below hierarchy followed
 * system-property > testng.xml > default.properties > default value passed
 */
public class Properties {

    /**
     * Default value is passed as null
     * @param property
     * @return
     */
    public static String getPreference(final String property){
        return getPreference(property, null);
    }

    /**
     * returns preference to be used based on below order
     * system-property > testng.xml > default.properties > default value passed
     * @param property
     * @param defaultValue
     * @return
     */
    public static String getPreference(final String property, final String defaultValue) {

        // read property from mvn commandline
        if (System.getProperties().containsKey(property)) {
            return System.getProperty(property);
        }

        // else read property from testng.xml
        try {
            String parameterValue;
            if ((parameterValue = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(property)) != null) {
                return parameterValue;
            }
        } catch (final NullPointerException e) {
            // do nothing
        }

        // else read value from default config file
        String configValue;
        if ((configValue = readConfig(property)) != null) {
            return configValue;
        }

        return defaultValue;
    }

    /**
     * default config file is read and value is returned based on property key
     * @param property
     * @return
     */
    private static String readConfig(final String property) {

        final String configPath = "./src/test/resources/config/defaults.properties";
        final File configFile = new File(configPath);
        if (configFile.exists()) {
            try {
                final FileInputStream fileInput = new FileInputStream(configFile);
                final java.util.Properties properties = new java.util.Properties();
                properties.load(fileInput);
                return properties.getProperty(property);
            } catch (final Exception e) {
            }
        }
        return null;
    }
}
