package helpers;

import helpers.throwables.FalseActionExecutionException;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.Arrays;

import static helpers.Common.getElement;
import static helpers.Common.handleWebDriverExceptions;
import static helpers.Common.reporter;

@Log4j
public class CommonActions {


    public static void scrollToElement(WebElement element) {
        try {
            if (element == null) {
                reporter().logError("Nie można scrollować do elementu: null");
            } else if (!element.isDisplayed()) executeJavascript("arguments[0].scrollIntoView(true);", element);
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, Common.getElementDescription(element));
        }
    }

    public static Object executeJavascript(String script, Object... arguments) {
        if (!(Common.driver instanceof JavascriptExecutor)) {
            reporter().logError("Obecny Webdriver nie ma zdolności uruchomienia Javascriptu");
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

    @Step("Wpisanie wartości {value} w elemeny {desc}")
    public static void enterIntoElement(By by, String value, String desc, boolean checkEnterValue) {
        enterIntoElement(getElement(by), value, desc, checkEnterValue);
    }

    @Step("Wpisanie wartości {value} w element {desc}")
    public static void enterIntoElement(WebElement element, String value, String desc, boolean checkEnteredValue) {
        try {
            if (!element.isEnabled()) {
                reporter().logFail("Element " + desc + " jest wyłączony");
                Common.handleWebDriverExceptions(new ElementNotInteractableException("Element " + desc + " jest wyłączony"), desc);
            }
            scrollToElement(element);
            element.clear();
            Common.pauseFor(1);
            element.sendKeys(value);
            Common.pauseFor(1);
            if (checkEnteredValue &&
                    (!(element.getAttribute("value").equals(value.trim()) || element.getText().equals(value.trim())))) {
                if (!(element.getAttribute("value").equals(value.trim())))
                    reporter().logFail("Nie wpisano tekstu '" + value + "' w element [" + desc + "] (wpisano: " + element.getAttribute("value") + ")", new FalseActionExecutionException("enterIntoElement(" + desc + ")"));
                else {
                    reporter().logFail("Nie wpisano tekstu '" + value + "' w element [" + desc + "] (wpisano: " + element.getText() + "]", new FalseActionExecutionException("enterIntoElement(" + desc + ")"));
                }
            }
            if (element.getTagName().equals("input") && element.getAttribute("type").equals("password")) {
                reporter().logPass("Uzupełniono hasło w polu '" + desc + "'");
            } else {
                reporter().logPass("W element: '" + desc + "' wpisano wartość: '" + value + "'");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void enterIntoTextField(By by, String value, String desc) {
        enterIntoTextField(getElement(by), value, desc, false);
    }

    @Step("Wpisanie wartości {value} w pole tekstowe {desc}")
    public static void enterIntoTextField(WebElement element, String value, String desc, boolean requiredNotEmpty) {
        if (requiredNotEmpty && StringUtils.isEmpty(value))
            reporter().logFail("Wartość do wpisania w pole [" + desc + "] jest pusta");
        try {
            if (!element.getTagName().equals("input") ||
                    !(element.getAttribute("type").equals("text") ||
                            element.getAttribute("type").equals("password") ||
                            element.getAttribute("type").equals("number")))
                handleWebDriverExceptions(new UnexpectedTagNameException(
                        "input with type 'text', 'password' or 'number'",
                        element.getTagName() + "with type '" + element.getAttribute("type" + "'")), desc);
            if (!element.isEnabled()) {
                reporter().logFail("Pole tekstowe [" + desc + "] jest wyłączone");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.clear();
                element.sendKeys(value);
                if (!(element.getAttribute("value").equals(value.trim()) || element.getText().equals(value.trim()))) {
                    if (!(element.getAttribute("value").equals(value.trim()))) {
                        reporter().logWarn("Nie wprowadzono tekstu " + value + " w pole tekstowe [" + desc + "]. Ponawiam...");
                        element.clear();
                        element.sendKeys(value);
                        if (!(element.getAttribute("value").equals(value.trim()) || element.getText().equals(value.trim()))) {
                            if (!(element.getAttribute("value").equals(value.trim())))
                                reporter().logFail("Nie wpisano tekstu '" + value + "' w pole tekstowe [" + desc + "] (wpisano: '" + element.getAttribute("value") + "')",
                                        new FalseActionExecutionException("enterIntoTextField(" + desc + ")"));
                            else
                                reporter().logFail("Nie wpisano tekstu '" + value + "' w pole tekstowe [" + desc + "] (wpisano: '" + element.getText() + "')",
                                        new FalseActionExecutionException("enterIntoTextField(" + desc + ")"));
                        }
                    }
                }
                if (!element.getAttribute("type").equals("password"))
                    reporter().logPass("W pole tekstowe [" + desc + "] wpisano wartość '" + value + "'");
                else
                    reporter().logPass("Uzupełniono hasło w pole [" + desc + "]");
            } else {
                reporter().logFail("Pole tekstowe [" + desc + "] nie jest widoczne na ekranie.");
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
        }
    }

    public static void enterIntoTextArea(By by, String value, String desc) {
        enterIntoTextArea(getElement(by), value, desc);
    }

    @Step("Wpisanie wartości {value} w pole edycyjne {desc}")
    public static void enterIntoTextArea(WebElement element, String value, String desc) {
        try {
            if (!element.getTagName().equals("textarea"))
                handleWebDriverExceptions(new UnexpectedTagNameException("textarea", element.getTagName()), desc);
            if (!element.isEnabled()) {
                reporter().logFail("Pole edycyjne [" + desc + "] jest wyłączone");
                handleWebDriverExceptions(new ElementNotInteractableException("Pole edycyjne [" + desc + "] jest wyłączone"), desc);
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.clear();
                element.sendKeys(value);
                try {
                    if (!(element.getAttribute("value").equals(value.trim()) || element.getText().equals(value.trim()))) {
                        if (!(element.getAttribute("value").equals(value.trim())))
                            reporter().logFail("Nie wpisano tekstu '" + value + "' w pole edycyjne [" + desc + "] (wpisano: '" + element.getAttribute("value") + "')",
                                    new FalseActionExecutionException("enterIntoTextArea(" + desc + ")"));
                        else
                            reporter().logFail("Nie wpisano tekstu '" + value + "' w pole edycyjne [" + desc + "] (wpisano: '" + element.getText() + "')",
                                    new FalseActionExecutionException("enterIntoTextArea(" + desc + ")"));
                    }
                } catch (StaleElementReferenceException e) {
                    log.warn("Nie można zweryfikować wartości, lokator elementu się zmienił");
                }
                reporter().logPass("W pole edycyjne [" + desc + "] nie jest widoczne na ekranie.");
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickElement(WebElement element, String desc) {
        clickElement(element, desc, true);
    }

    @Step("Kliknięcie w element {desc}")
    public static void clickElement(WebElement element, String desc, boolean scroll) {
        try {
            Common.pauseFor(1);
            if (!element.isEnabled()) {
                reporter().logFail("Element [" + desc + "] jest wyłączony");
            }
            if (scroll) {
                scrollToElement(element);
            }
            if (element.isDisplayed()) {
                element.click();
                reporter().logPass("Kliknięto w element [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickElement(By by, String desc) {
        clickElement(by, desc, true);
    }

    @Step("Kliknięcie w element {desc}")
    public static void clickElement(By by, String desc, boolean scroll) {
        try {
            WebElement element = Common.getElement(by);
            Common.pauseFor(1);
            if (!element.isEnabled()) {
                reporter().logFail("Element [" + desc + "] jest wyłączony");
            }
            if (element.isDisplayed()) {
                clickElement(element, desc, scroll);
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }


    @Step("Kliknięcie w przycisk {desc}")
    public static void clickButton(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("button")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("button", element.getTagName()), desc);
            }
            if (!element.isEnabled()) {
                reporter().logFail("Przycisk [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                reporter().logPass("Kliknięto w przycisk [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    @Step("Kliknięcie w checkbox {desc}")
    public static void clickCheckBox(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("input") || !element.getAttribute("type").equals("checkbox")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("tag: input / checkbox", element.getTagName() + "/" + element.getAttribute("type")), desc);
            }
            if (!element.isEnabled()) {
                reporter().logFail("Przycisk wyboru [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                boolean t = element.getAttribute("checked") != null;
                element.click();
                Common.pauseFor(1);
                if (element.getAttribute("checked") != null == t) {
                    reporter().logFail("Nie zaznaczono przycisku wyboru [" + desc + "]");
                }
                reporter().logPass("Kliknięto przycisk wyboru [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    @Step("Wybór opcji {option} z listy rozwijalnej {desc}")
    public static void selectDropdownListOption(WebElement element, String option, String desc) {
        try {
            if (!element.getTagName().equals("select")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("select", element.getTagName()), desc);
            }
            if (!element.isEnabled()) {
                reporter().logFail("Lista rozwijana " + desc + " jest wyłączona");
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
                reporter().logPass("Wybrano opcje " + option + " z listy [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc + " ->" + option);
        }
    }

    @Step("Wybór opcji {option} z listy rozwijalnej {desc}")
    public static void selectDropdownListOption(By by, String option, String desc) {
        try {
            WebElement element = Common.getElement(by);
            if (!element.getTagName().equals("select")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("select", element.getTagName()), desc);
            }
            if (!element.isEnabled()) {
                reporter().logFail("Lista rozwijana " + desc + " jest wyłączona");
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
                reporter().logPass("Wybrano opcje " + option + " z listy [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc + " ->" + option);
        }
    }

    public static void pressKey(CharSequence... chars) {
        performAction(new Actions(Common.driver).sendKeys(chars), "Wpisanie tekstu " + Arrays.toString(chars));
    }

    @Step("{desc}")
    public static void performAction(Actions a, String desc) {
        try {
            a.perform();
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    @Step("Wybór opcji {desc}")
    public static void selectRadioOption(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("input") || !element.getAttribute("type").equals("radio")) {
                Common.handleWebDriverExceptions(new UnexpectedTagNameException("input / radio", element.getTagName() + " / " + element.getAttribute("type")), desc);
            }
            if (!element.isEnabled()) {
                reporter().logFail("Przycisk opcji [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                reporter().logPass("Kliknięto w przycisk opcji [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickElementIfVisible(By by, int timeoutInSeconds, String desc) {
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

    public static void refreshSite() {
        try {
            Common.driver.navigate().refresh();
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, "refresh");
        }
    }
}
