package helpers.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.ConnectionClosedException;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static helpers.common.CommonActions.refreshSite;
import static helpers.common.CommonWaits.waitUntilElementPresent;
import static helpers.common.CommonWaits.waitUntilElementVisible;

public class Common {
    public static WebDriver driver;
    private static StopWatch stopWatch;
    public static int timeoutInSeconds = 60;

    public static WebDriver setUpClass() {
        System.setProperty("webdriver.chrome.driver", ".\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
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
            //reporter pass
            System.out.println("Uruchomienie strony: " + url);
        } else {
            String[] logowanieUrlSplit = url.split("/");
            String aktualnyURL = "https://" + logowanieUrlSplit[2] + url;
            driver.get(aktualnyURL);
            //reporter pass
            System.out.println("Uruchomienie strony: " + aktualnyURL);
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
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, desc);
        }
        return element;
    }

    public static List<WebElement> getElements(By by, String desc) {
        List<WebElement> elementList;
        try {
            elementList = driver.findElements(by);
        } catch (WebDriverException e) {
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

    static String getElementDescription(By by) {
        try {
            WebElement el = driver.findElement(by);
            if (el.getAttribute("text") != null && !"".equals(el.getAttribute("text"))) return el.getAttribute("text");
            if (el.getAttribute("value") != null && !"".equals(el.getAttribute("value")))
                return el.getAttribute("value");
            if (el.getAttribute("aria=label") != null && !"".equals(el.getAttribute("aria-label")))
                return el.getAttribute("aria-label");
        } catch (WebDriverException ignored) {
        }

        String[] s = by.toString().split("->");
        if (s.length >= 2) {
            if (s[1].endsWith("]") && (StringUtils.countMatches(s[1], '[') + 1) == StringUtils.countMatches(s[1], ']')) {
                return s[1].substring(0, s[1].length() - 1);
            } else return s[1];
        } else {
            String el = by.toString();
            if (el.endsWith("]") && (StringUtils.countMatches(el, '[') + 1) == StringUtils.countMatches(el, ']')) {
                return el.substring(0, el.length() - 1);
            } else return el;
        }
    }

    public static void verifyElementDisplayed(WebElement element, String logPassMsg, String logFailMsg) {
        if (waitUntilElementVisible(element, 30) != null) {
            //reporter logpass
            System.out.println(logPassMsg);
        } else {
            //reporter logfail
            System.out.println(logFailMsg);
        }
    }

    public static boolean isElementPresent(By by, int timeoutsInSeconds) {
        return waitUntilElementPresent(by, timeoutsInSeconds) != null;
    }

    public static boolean isElementVisible(WebElement element, int timeoutInSeconds) {
        return waitUntilElementVisible(element, timeoutInSeconds) != null;
    }

    public static void startTimeCounter() {
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    public static void stopTimeCounter() {
        if (!stopWatch.isStarted()) {
            //reporter warn
            System.out.println("Problem z licznikiem czasu");
        } else {
            stopWatch.stop();
            long pageLoadTime_ms = stopWatch.getTime();
            long pageLoadTime_seconds = pageLoadTime_ms / 1000;
            //reporter p[ass
            System.out.println("Całkowity czas wczytywania strony: " + pageLoadTime_seconds + " sek");
        }
    }

    public static void changeTimeout(int implicitlyWait) {
        driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
        //reporter pass
        System.out.println("implicitlyWait zmieniony na: " + implicitlyWait + "s");
    }

    public static void changeTimeout(int implicitlyWait, int pageLoadTimeout, int setScriptTimeout) {
        driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(setScriptTimeout, TimeUnit.SECONDS);
        if (true) {
            System.out.println("implicitlyWait zmieniony na: " + implicitlyWait + "s, pageLoadTimeout zmieniony na: " + pageLoadTimeout + "s, setScriptTimeout zmieniony na: " + setScriptTimeout + "s");
        }
    }

    public static void restoreDefaultTimeouts() {
        driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(timeoutInSeconds, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(timeoutInSeconds, TimeUnit.SECONDS);
        if (true) {
            System.out.println("Przywrócono domyślne czasy implicitlyWait, pageLoadTimeout i setScriptTimeout");
        }
    }

    public static void switchToNewTab(boolean failIfNotOpened) {
        final String currentTabHandle = driver.getWindowHandle();
        //log.info
        System.out.println("Obecnie aktywna karta: " + currentTabHandle);
        String title1 = driver.getTitle();
        try {
            int time = 20;
            for (int i = 0; i < time; i++) {
                ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                pauseFor(1);
                if (tabs.size() > 1) {
                    //log info
                    System.out.println("Nowa zakładka otwarta");
                    String newTabHandle = driver.getWindowHandles()
                            .stream()
                            .filter(handle -> !handle.equals(currentTabHandle))
                            .findFirst()
                            .orElseThrow(IllegalStateException::new);
                    driver.switchTo().window(newTabHandle);
                    //log info
                    System.out.println("Zmiana aktywnej karty na: " + newTabHandle);
                    changeTimeout(60, 60, 60);
                    try {
                        pauseFor(10);
                        if (!driver.getTitle().isEmpty()) {
                            String title2 = driver.getTitle();
                            //reporter pass
                            System.out.println("Zmieniono aktywną kartę z: " + currentTabHandle + " (tytuł: " + title1 + ") na: " + newTabHandle + " (tytuł: " + title2 + ").");
                            changeTimeout(30, 30, 30);
                            break;
                        } else if (i < time - 1) {
                            //reporter warn
                            System.out.println("Pusty tytuł strony, odświeżenie i wycofanie...");
                            refreshSite();
                            driver.switchTo().window(currentTabHandle);
                        }

                    } catch (WebDriverException e) {
                        if (e instanceof TimeoutException || e.getMessage().contains("cannot determine loading status")) {
                            //reporter fail
                            System.out.println("Nowa zakładka otworzyła się niepoprawnie: nie można pobrać " + "tytułu strony. Sprawdź, czy strona załadowała się poprawnie.");
                        }
                        handleWebDriverExceptions(e, null);
                    }
                    changeTimeout(30, 30, 30);
                }
                if (i == time - 1 && (tabs.size() == 1) && failIfNotOpened) {
                    //reporter fail
                    System.out.println("Nowa zakładka nie została otwarta. Czas oczekiwania: " + time + "s");
                }
            }

        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, null);
        }
    }

    public static void switchToNewPopUp() {
        try {
            pauseFor(5);
            String parentWindowHandler = driver.getWindowHandle();
            String subWindowHandler = null;

            Set<String> handles = driver.getWindowHandles();
            Iterator<String> iterator = handles.iterator();
            while (iterator.hasNext()) {
                subWindowHandler = iterator.next();
            }
            driver.switchTo().window(subWindowHandler);
            if (parentWindowHandler.equals(subWindowHandler)) {
                //log info
                System.out.println("Zmieniono aktywność okna z: " + parentWindowHandler + " na: " + subWindowHandler);
            } else {
                //log info
                System.out.println("Aktywność okna nie została zmieniona");
            }
        } catch (Exception e) {
            //log info
            //reporter fail
            System.out.println("Problem ze zmianą aktywności okna...");
        }
    }

    public static int getCountObject(By by) {
        return driver.findElements(by).size();
    }

    public static String getCurrentDate(String format) {
        DateFormat dateFormat;
        switch (format) {
            case "yyyyMMdd":
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case "dd.MM.yyyy":
                dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                break;
            default:
                dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                break;
        }
        Date date = new Date();
        return dateFormat.format(date);
    }
}
