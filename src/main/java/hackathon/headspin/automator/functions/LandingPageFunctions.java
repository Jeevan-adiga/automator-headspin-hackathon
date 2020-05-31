package hackathon.headspin.automator.functions;

import hackathon.headspin.automator.page.LandingPage;

public class LandingPageFunctions {

    public static void login(String username, String password) {
        LandingPage.loginOrCreateAccount.waitForDisplay(true, 30);
        LandingPage.loginOrCreateAccount.click();

        LandingPage.loginPopup.emailInput.waitForDisplay(true, 60);
        LandingPage.loginPopup.emailInput.sendKeys(username);

        LandingPage.loginPopup.continueButton.waitForEnable(true, 10);
        LandingPage.loginPopup.continueButton.click();

        LandingPage.loginPopup.passwordInput.waitForDisplay(true, 60);
        LandingPage.loginPopup.passwordInput.sendKeys(password);
        LandingPage.loginPopup.closeCrossButton.click();
    }
}
