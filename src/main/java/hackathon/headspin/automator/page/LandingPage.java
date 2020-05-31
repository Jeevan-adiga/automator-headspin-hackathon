package hackathon.headspin.automator.page;

import hackathon.headspin.automator.web.Navigator;
import org.openqa.selenium.By;

//    hajejo4438@tashjw.com
//    hackathon@2020
public class LandingPage {

    public static Navigator loginOrCreateAccount = new Navigator("loginOrCreateAccount", By.xpath("//li[@data-cy='account']"));
    public static LoginPopup loginPopup = new LoginPopup("loginPopup", By.xpath("//section[contains(@class,'modalMain')]"));

    public static class LoginPopup extends Navigator {

        public Navigator emailInput = new Navigator("emailInput", By.xpath("//input[@id='username']"));
        public Navigator continueButton = new Navigator("continueButton", By.xpath("//button[@data-cy='continueBtn']//parent::div"));
        public Navigator passwordInput = new Navigator("passwordInput", By.xpath("//input[@id='password']"));
        public Navigator closeCrossButton = new Navigator("closeCrossButton", By.xpath("//span[contains(@class,'popupCrossIcon')]"));

        public LoginPopup(String name, By by) {
            super(name, by);
        }
    }
}
