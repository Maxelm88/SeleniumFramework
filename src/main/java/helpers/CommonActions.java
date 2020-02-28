package helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.Arrays;


public class CommonActions {


    public static void scrollToElement(WebElement element) {
        try {
            if (element == null) {
                //reporter
                System.out.println("Nie można scrollować do elementu: null");
            } else if (!element.isDisplayed()) executeJavascript("arguments[0].scrollIntoView(true);", element);
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, Common.getElementDescription(element));
        }
    }

    public static Object executeJavascript(String script, Object... arguments) {
        if (!(Common.driver instanceof JavascriptExecutor)) {
            //reporter
            System.out.println("Obecny Webdriver nie ma zdolności uruchomienia Javascriptu");
            return null;
        }

        System.out.println("Wykonanie javascritpu : " + script + ". args: " + Arrays.asList(arguments));

        try {
            return ((JavascriptExecutor) Common.driver).executeScript(script, arguments);
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, "Javascript");
            System.out.println("Błąd: " + e);
        }
        return ((JavascriptExecutor) Common.driver).executeScript(script, arguments);
    }

    public static void enterIntoElement(WebElement element, String value, String desc) {
        try {
            if (!element.isEnabled()) {
                //reporter
                Common.handleWebDriverExceptions(new ElementNotInteractableException("Element " + desc + " jest wyłączony"), desc);
            }
            scrollToElement(element);
            element.clear();
            Common.pauseFor(1);
            element.sendKeys(value);
            if (element.getTagName().equals("input") && element.getAttribute("type").equals("password")) {
                //reporter
                System.out.println("Uzupełniono hasło w polu '" + desc + "'");
            } else {
                //reporter
                System.out.println("W element: '" + desc + "' wpisano wartość: '" + value + "'");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickElement (WebElement  element, String desc){
        clickElement(element, desc, true);
    }


    public static void clickElement(WebElement element, String desc, boolean scroll) {
        try {
            Common.pauseFor(1);
            if (!element.isEnabled()) {
                //Common.reporter().logFail("Element[" + desc + "] jest wyłączony");
                System.out.println("Element [" + desc + "] jest wyłączony");
            }
            if (scroll) {
                scrollToElement(element);
            }
            if (element.isDisplayed()) {
                element.click();
                //Common.reporter.logPass("Kliknięto w element [" + desc  + "]");
                System.out.println("Kliknięto w element [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickElement(By by, String desc, boolean scroll){
        try {
            WebElement element = Common.getElement(by);
            Common.pauseFor(1);
            if(!element.isEnabled()){
                //reporte fail
                System.out.println("Element [" + desc + "] jest wyłączony");
            }
            if (element.isDisplayed()){
                clickElement(element, desc, scroll);
            }
        }
        catch (WebDriverException e){
            Common.handleWebDriverExceptions(e, desc);
        }
    }



    public static void clickButton(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("button")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("button", element.getTagName()), desc);
            }
            if (!element.isEnabled()) {
                //reporter fail
                System.out.println("Przycisk [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                //reporter pass
                System.out.println("Kliknięto w przycisk [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickCheckBox(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("input") || !element.getAttribute("type").equals("checkbox")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("tag: input / checkbox", element.getTagName() + "/" + element.getAttribute("type")), desc);
            }
            if (!element.isEnabled()) {
                //reporter fail
                System.out.println("Przycisk wyboru [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                boolean t = element.getAttribute("checked") != null;
                element.click();
                Common.pauseFor(1);
                if (element.getAttribute("checked") != null == t) {
                    //reporter fail
                    System.out.println("Nie zaznaczono przycisku wyboru [" + desc + "]");
                }
                //reporter pass
                System.out.println("Kliknięto przycisk wyboru [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void selectDropdownListOption(WebElement element, String option, String desc) {
        try {
            if (!element.getTagName().equals("select")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("select", element.getTagName()), desc);
            }
            if (!element.isEnabled()) {
                //reporter fail
                System.out.println("Lista rozwijana " + desc + " jest wyłączona");
                throw new ElementNotInteractableException("Lista rozwijana " + desc + " jest wyłączonoa");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                Common.pauseFor(1);
                element.findElement(By.xpath("option[contains(text(), \"" + option + "\"")).click();
                if (CommonWaits.waitUntilElementPresent(By.xpath("//div[contains(@class, 'AFNoteWindowShortDesc')]"), 1) != null) {
                    pressKey(Keys.ESCAPE);
                }
                //reporter pass
                System.out.println("Wybrano opcje " + option + " z listy [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc + " ->" + option);
        }
    }

    public static void selectDropdownListOption(By by, String option, String desc) {
        try {
            WebElement element = Common.getElement(by);
            if (!element.getTagName().equals("select")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("select", element.getTagName()), desc);
            }
            if (!element.isEnabled()) {
                //reporter fail
                System.out.println("Lista rozwijana " + desc + " jest wyłączona");
                throw new ElementNotInteractableException("Lista rozwijana " + desc + " jest wyłączonoa");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                Common.pauseFor(1);
                element.findElement(By.xpath("option[contains(text(), \"" + option + "\"")).click();
                if (CommonWaits.waitUntilElementPresent(By.xpath("//div[contains(@class, 'AFNoteWindowShortDesc')]"), 1) != null) {
                    pressKey(Keys.ESCAPE);
                }
                //reporter pass
                System.out.println("Wybrano opcje " + option + " z listy [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc + " ->" + option);
        }
    }

    public static void pressKey(CharSequence... chars) {
        performAction(new Actions(Common.driver).sendKeys(chars), "Wpisanie tekstu " + Arrays.toString(chars));
    }

    public static void performAction(Actions a, String desc) {
        try {
            a.perform();
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void selectRadioOption(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("input") || !element.getAttribute("type").equals("radio")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("input / radio", element.getTagName() + " / " + element.getAttribute("type")), desc);
            }
            if (!element.isEnabled()) {
                //reporter fail
                System.out.println("Przycisk opcji [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                //reporter pass
                System.out.println("Kliknięto w przycisk opcji [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickElementIfVisible(By by, int timeoutInSeconds, String desc){
        clickElementIfVisible(by, timeoutInSeconds, desc, true);
    }

    public static void clickElementIfVisible(By by, int timeoutInSeconds, String desc, boolean scroll) {
        try {
            if (CommonWaits.waitUntilElementPresent(by, timeoutInSeconds) != null) {
                clickElement(by, desc, scroll);
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void refreshSite(){
        try{
            Common.driver.navigate().refresh();
        }
        catch (WebDriverException e)
        {
            Common.handleWebDriverExceptions(e, "refresh");
        }
    }
}