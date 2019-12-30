package helpers.common;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.Arrays;

import static helpers.common.Common.*;
import static helpers.common.CommonWaits.waitUntilElementPresent;


public class CommonActions {


    public static void scrollToElement(WebElement element) {
        try {
            if (element == null) {
                //reporter
                System.out.println("Nie można scrollować do elementu: null");
            } else if (!element.isDisplayed()) executeJavascript("arguments[0].scrollIntoView(true);", element);
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, getElementDescription(element));
        }
    }

    public static Object executeJavascript(String script, Object... arguments) {
        if (!(driver instanceof JavascriptExecutor)) {
            //reporter
            System.out.println("Obecny Webdriver nie ma zdolności uruchomienia Javascriptu");
            return null;
        }

        System.out.println("Wykonanie javascritpu : " + script + ". args: " + Arrays.asList(arguments));

        try {
            return ((JavascriptExecutor) driver).executeScript(script, arguments);
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, "Javascript");
            System.out.println("Błąd: " + e);
        }
        return ((JavascriptExecutor) driver).executeScript(script, arguments);
    }

    public static void enterIntoElement(WebElement element, String value, String desc) {
        try {
            if (!element.isEnabled()) {
                //reporter
                handleWebDriverExceptions(new ElementNotInteractableException("Element " + desc + " jest wyłączony"), desc);
            }
            scrollToElement(element);
            element.clear();
            pauseFor(1);
            element.sendKeys(value);
            if (element.getTagName().equals("input") && element.getAttribute("type").equals("password")) {
                //reporter
                System.out.println("Uzupełniono hasło w polu '" + desc + "'");
            } else {
                //reporter
                System.out.println("W element: '" + desc + "' wpisano wartość: '" + value + "'");
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickElement(WebElement element, String desc) {
        try {
            pauseFor(1);
            if (!element.isEnabled()) {
                //common.reporter
                System.out.println("Element [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                //common.reporter
                System.out.println("Kliknięto w element [" + desc + "]");
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
        }
    }

    static String getElementDescription(WebElement element) {
        try {
            if (element == null) return null;
            if (element.getAttribute("text") != null && !"".equals(element.getAttribute("text"))) {
                return element.getAttribute("text");
            }
            if (element.getAttribute("value") != null && !"".equals(element.getAttribute("value"))) {
                return element.getAttribute("value");
            }
            if (element.getAttribute("aria-label") != null && !"".equals(element.getAttribute("aria-label"))) {
                return element.getAttribute("aria-label");
            }
            String[] s = element.toString().split("->");
            if (s.length >= 2) {
                if (s[1].endsWith("]") && (StringUtils.countMatches(s[1], '[') + 1) == StringUtils.countMatches(s[1], ']')) {
                    return s[1].substring(0, s[1].length() - 1);
                } else return s[1];
            } else {
                String el = element.toString();
                if (el.endsWith("]") && (StringUtils.countMatches(el, ']') + 1) == StringUtils.countMatches(el, ']')) {
                    return el.substring(0, el.length() - 1);
                } else return el;
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, element.toString());
            return null;
        }
    }

    public static void clickButton(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("button")) {
                handleWebDriverExceptions(new UnexpectedTagNameException("button", element.getTagName()), desc);
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
            handleWebDriverExceptions(e, desc);
        }
    }

    public static void clickCheckBox(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("input") || !element.getAttribute("type").equals("checkbox")) {
                handleWebDriverExceptions(new UnexpectedTagNameException("tag: input / checkbox", element.getTagName() + "/" + element.getAttribute("type")), desc);
            }
            if (!element.isEnabled()) {
                //reporter fail
                System.out.println("Przycisk wyboru [" + desc + "] jest wyłączony");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                boolean t = element.getAttribute("checked") != null;
                element.click();
                pauseFor(1);
                if (element.getAttribute("checked") != null == t) {
                    //reporter fail
                    System.out.println("Nie zaznaczono przycisku wyboru [" + desc + "]");
                }
                //reporter pass
                System.out.println("Kliknięto przycisk wyboru [" + desc + "]");
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
        }
    }

    public static void selectDropdownListOption(WebElement element, String option, String desc) {
        try {
            if (!element.getTagName().equals("select")) {
                handleWebDriverExceptions(new UnexpectedTagNameException("select", element.getTagName()), desc);
            }
            if (!element.isEnabled()) {
                //reporter fail
                System.out.println("Lista rozwijana " + desc + " jest wyłączona");
                throw new ElementNotInteractableException("Lista rozwijana " + desc + " jest wyłączonoa");
            }
            scrollToElement(element);
            if (element.isDisplayed()) {
                element.click();
                pauseFor(1);
                element.findElement(By.xpath("option[contains(text(), \"" + option + "\"")).click();
                if (waitUntilElementPresent(By.xpath("//div[contains(@class, 'AFNoteWindowShortDesc')]"), 1) != null) {
                    pressKey(Keys.ESCAPE);
                }
                //reporter pass
                System.out.println("Wybrano opcje " + option + " z listy [" + desc + "]");
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc + " ->" + option);
        }
    }

    public static void pressKey(CharSequence... chars) {
        performAction(new Actions(driver).sendKeys(chars), "Wpisanie tekstu " + Arrays.toString(chars));
    }

    public static void performAction(Actions a, String desc) {
        try {
            a.perform();
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
        }
    }

    public static void selectRadioOption(WebElement element, String desc) {
        try {
            if (!element.getTagName().equals("input") || !element.getAttribute("type").equals("radio")) {
                handleWebDriverExceptions(new UnexpectedTagNameException("input / radio", element.getTagName() + " / " + element.getAttribute("type")), desc);
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
            handleWebDriverExceptions(e, desc);
        }
    }
}
