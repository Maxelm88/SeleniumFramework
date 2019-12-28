package helpers.common;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

import static helpers.common.Common.*;


public class CommonActions {


    public static void scrollToElement(WebElement element) {
        try {
            if (element == null) {
                //reporter
                System.out.println("Nie można scrollować do elementu: null");
            } else if (!element.isDisplayed()) executeJavascript("arguments[0].scrollIntoView(true);", element);
        } catch (WebDriverException e) {
            //common.handleWebDriverExceptions
            System.out.println("Błąd: " + e);
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
            //common.handleWebDriverExceptions
            System.out.println("Błąd: " + e);
        }
        return ((JavascriptExecutor) driver).executeScript(script, arguments);
    }

    public static void enterIntoElement(WebElement element, String value, String desc) {
        try {
            if (!element.isEnabled()) {
                //reporter
                //driver exception
                System.out.println("Element " + desc + " jest wyłącozny");
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
            //common.handleWebDriverExceptions
            System.out.println("Błąd: " + e);
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
            //common.handleWebDriverException
            System.out.println("Błąd: " + e);
        }
    }
}
