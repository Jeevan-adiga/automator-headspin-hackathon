package hackathon.headspin.automator.web;

import com.aventstack.extentreports.Status;
import hackathon.headspin.automator.Browser;
import hackathon.headspin.automator.report.Reporter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class Navigator implements ISearchContext {

    protected String name;
    protected Navigator parent;
    protected By by;

    public Navigator(String name, By by, Navigator parent) {
        this.name = name;
        this.by = by;
        this.parent = parent;
    }

    public Navigator(String name, By by) {
        this.name = name;
        this.by = by;
    }

    public Navigator(By by, Navigator navigator) {
        this.name = "Navigator";
        this.by = by;
        this.parent = navigator;
    }

    public String getName() {
        return name;
    }

    // interface methods
    public Navigator findElement(String name, By by) {
        return new Navigator(name, by, this);
    }

    public Navigator findElement(By by) {
        return new Navigator(by, this);
    }

    public ISearchContext getParent() {
        if (parent != null) return parent;
        return null;//Page;
    }

    /**
     * to get WebElement representation of Navigator
     * @return
     */
    public WebElement toWebElement() {

        SearchContext searchContext = Browser.getDriver();

        if (this.parent != null && this.parent instanceof Navigator) {
            Navigator parentElement = (Navigator) this.parent;
            searchContext = parentElement.toWebElement();
        }

        List<WebElement> elements = searchContext.findElements(by);
        Iterator<WebElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            WebElement element = iterator.next();
            if (element.isDisplayed()) return element;
        }
        return searchContext.findElement(by);
    }

    protected Boolean isInteractive() {
        try {
            this.waitForDisplay(true, 10);
            return this.isDisplayed() && this.isEnabled();
        } catch (Error error) {
            return false;
        }
    }

    /**
     * to be used to wait for element to be displayed
     * @param expected
     * @param timeOutInSeconds
     * @return
     */
    public Boolean waitForDisplay(final Boolean expected, long timeOutInSeconds) {
        Boolean success = true;
        try {
            Wait wait = new FluentWait<WebDriver>(Browser.getDriver())
                    .withTimeout(Duration.ofSeconds(timeOutInSeconds))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class);;
        } catch(Exception e){
            success = false;
        }
        Reporter.log(success, "[" + name + "] Wait for element to " + (expected ? "display" : "finish displaying") + " (" + timeOutInSeconds + ")");
        return success;
    }

    /**
     * to be used to wait for element to be displayed
     * @param expected
     * @param timeOutInSeconds
     * @return
     */
    public Boolean waitForEnable(final Boolean expected, long timeOutInSeconds) {
        Boolean success = true;
        try {
            WebDriverWait wait = new WebDriverWait(Browser.getDriver(), timeOutInSeconds);
            wait.until(ExpectedConditions.elementToBeClickable(toWebElement()));
        } catch(Exception e){
            success = false;
        }
        Reporter.log(success, "[" + name + "] Wait for element to " + (expected ? "display" : "finish displaying") + " (" + timeOutInSeconds + ")");
        return success;
    }

    public Boolean click() {
        Boolean prereq = isInteractive();
        Boolean success = false;

        if (prereq) {
            try {
                toWebElement().click();
                success = true;
            } catch (StaleElementReferenceException | Error e) {
                Reporter.log(false, "[" + name + "] Click element");
                throw e;
            } catch (Exception e) {
                throw e;
            }
        }
        Reporter.log(success, "[" + name + "] Click element");
        return success;
    }

    public Boolean sendKeys(String string) {
        Boolean prereq = isInteractive();
        Boolean success = false;
        if (prereq) {
            WebElement webElement = toWebElement();
            webElement.sendKeys(string);
            success = true;
        }
        Reporter.log(success, "[" + name + "] Type \"" + string + "\"");
        return success;
    }

    //clear
    public void clear() {
        Reporter.log(Status.PASS, "[" + name + "] Clear");
        toWebElement().clear();
    }

    //tagName
    public String getTagName() {
        return toWebElement().getTagName();
    }

    //attribute
    public String getAttribute(String name) {
        return toWebElement().getAttribute(name);
    }

    //selected
    public boolean isSelected() {
        return toWebElement().isSelected();
    }

    public boolean isClickable(int timeOut) {
        boolean exists = false;
        WebElement element = toWebElement();
        try {
            final WebDriverWait wait = new WebDriverWait(Browser.getRemoteDriver(), timeOut);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.getTagName();
            exists = true;
        } catch (final NoSuchElementException | TimeoutException e) {
        }
        return exists;
    }


    //editable
    public boolean isEditable() {
        switch (getTagName()) {
            case "input":
            case "select":
            case "textarea":
                return isInteractive();
        }
        return false;
    }

    //enabled
    public Boolean isEnabled() {
        return toWebElement().isEnabled();
    }

    //text
    public String getText() {
        Boolean prereq = isDisplayed();
        if (prereq) {
            try {
                return toWebElement().getText();
            } catch (Error error) {
                return "";
            }
        }
        return "";
    }

    //display
    public Boolean isDisplayed() {
        try {
            Boolean iDisplayed = toWebElement().isDisplayed();
            return iDisplayed;

        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        } catch (WebDriverException e) {
            if (e.getMessage().contains("Error determining if element is displayed")) return false;
            throw e;
        }
    }

    public void clickByJS() {
        try {
            JavascriptExecutor exec = (JavascriptExecutor) Browser.getDriver();
            exec.executeScript("arguments[0].click();", this.toWebElement());
            Reporter.log(true, "[" + this.name + "] Click element");
        } catch (TimeoutException e) {
            Reporter.log(false, "[" + this.name + "] Click element");
        }
    }

}
