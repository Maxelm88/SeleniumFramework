package helpers.common;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.ConnectionClosedException;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.Collections;
import java.util.List;

public class Common {
    public static WebDriver driver;

    public static WebDriver setUpClass() {
        System.setProperty("webdriver.chrome.driver", ".\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void openUrl(boolean czyKompletnyUrl, String url) {
        if (czyKompletnyUrl) {
            driver.get(url);
        } else {
            String[] logowanieUrlSplit = url.split("/");
            String aktualnyURL = "https://" + logowanieUrlSplit[2] + url;
            driver.get(aktualnyURL);
        }
    }

    public static void pauseFor(int timeoutInSeconds) {
        try {
            timeoutInSeconds = timeoutInSeconds * 1000;
            Thread.sleep(timeoutInSeconds);
        } catch (InterruptedException e) {
            System.out.println("Błąd " + e);
        }
    }

    public static void handleWebDriverExceptions(RuntimeException e, String desc) {
        if (!(e instanceof WebDriverException)) throw e;

        //reporter null

        if (e instanceof ConnectionClosedException) {
            //reporter fail
            System.out.println("Utracono połączenie ze sterownikiem Safari.");
        } else if (e instanceof ElementClickInterceptedException || e.getMessage().contains("Other element void receive the click")) {
            //reporter fail
            System.out.println("Kliknięcie w " + desc + " zostało przechwycone przez inny element. " + "Upewnij się, że lement w momencie kliknięcia nie jest zasłonięty.");
        } else if (e instanceof ElementNotVisibleException) {
            //reporter fail
            System.out.println("Element " + desc + " nie jest widoczny na ekranie.");
        } else if (e instanceof ElementNotInteractableException) {
            //reporter fail
            System.out.println("Interakcja z elementem " + desc + " nie jest możliwa.");
        } else if (e instanceof ElementNotSelectableException) {
            //reporter fail
            System.out.println("Nie jest możliwe wybranie elementu " + desc + ".");
        } else if (e instanceof InvalidSelectorException) {
            //reporter fial
            System.out.println("Selektor elementu " + desc + "jest nieprawidłowy.");
        } else if (e instanceof NoSuchElementException) {
            //reporter fail
            System.out.println("Element " + desc + "nie istnieje.");
        } else if (e instanceof NoAlertPresentException) {
            //reporter fail
            System.out.println("Oczekiwano ostrzeżenia, ale żadnego nie znaleziono.");
        } else if (e instanceof JavascriptExecutor) {
            //reporter fail
            System.out.println("Błąd Javascriptu. Rozwiń aby zobaczyć szczegóły.");
        } else if (e instanceof InvalidElementStateException) {
            //reporter fail
            System.out.println("Stan elementu " + desc + "nie pozwala na wykonanie żądanej akcji. Rozwiń by zobaczyć szczegóły.");
        } else if (e instanceof StaleElementReferenceException) {
            //reporter fail
            System.out.println("Stan elementu " + desc + "już nie istnieje. Sprawdź czy nie zmienił się jego lokator.");
        } else if (e instanceof UnhandledAlertException) {
            //reporter fail
            System.out.println("Nieobsłużone ostrzeżenie: " + /*getAllertText() +*/ ". Sprawdź log testu.");
        } else if (e instanceof TimeoutException) {
            //reporter fail
            System.out.println("Akcja nie została wykonana w podanym czasie. " +
                    "Jeśli oczekiwałeś na zmianę stanu elementu, " +
                    "zweryfikuj warunki zmiany stanu lub przedłuż oczekiwanie.");
        } else if (e instanceof UnexpectedTagNameException) {
            //reporter fail
            System.out.println("Element " + desc + " posiada inny tag niż oczekiwano.");
        } else if (e instanceof SessionNotCreatedException) {
            //reporter fail
            System.out.println("Nie udało się utworzyć sesji testowej. Rozwiń aby zobaczyć szczegóły.");
        } else {
            //reporter fail
            System.out.println("Wystapił  nieoczekiwany błąd WebDrivera. Rozwiń, by zobaczyć szczegóły");
        }
    }

    public static WebElement getElement(By by) {
        return getElement(by, getElementDescription(by));
    }

    public static WebElement getElement(By by, String desc) {
        WebElement element = null;
        try {
            element = driver.findElement(by);
        } catch (WebDriverException e){
            handleWebDriverExceptions(e, desc);
        }
        return element;
    }

    public static List<WebElement> getElements(By by, String desc){
        List<WebElement> elementList;
        try{
            elementList = driver.findElements(by);
        }
        catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
            return Collections.emptyList();
        }
        return elementList;
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

    static String getElementDescription(By by){
        try{
            WebElement el = driver.findElement(by);
            if(el.getAttribute("text") != null && !"".equals(el.getAttribute("text"))) return el.getAttribute("text");
            if(el.getAttribute("value") != null && !"".equals(el.getAttribute("value"))) return el.getAttribute("value");
            if(el.getAttribute("aria=label") !=null && !"".equals(el.getAttribute("aria-label"))) return el.getAttribute("aria-label");
        }
        catch (WebDriverException ignored) {
        }

        String[] s = by.toString().split("->");
        if(s.length >= 2) {
            if(s[1].endsWith("]") && (StringUtils.countMatches(s[1], '[') + 1) == StringUtils.countMatches(s[1], ']')) {
                return s[1].substring(0, s[1].length()-1);
            }
            else return s[1];
        }
        else {
            String el = by.toString();
            if(el.endsWith("]") && (StringUtils.countMatches(el, '[') + 1) == StringUtils.countMatches(el, ']')){
                return el.substring(0, el.length()-1);
            } else return el;
        }
    }
}
