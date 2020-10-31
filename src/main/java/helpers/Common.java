package helpers;

import helpers.data.provider.AbstractTestCaseData;
import helpers.datebase.TestDataManager;
import helpers.datebase.dto.CustomTestDTO;
import helpers.dictionary.Browser;
import helpers.dictionary.BrowserArg;
import helpers.dictionary.DataRowStatus;
import helpers.dictionary.Profile;
import helpers.dictionary.StaticStrings;
import helpers.reporter.ReportManager;
import helpers.reporter.ReportManagerFactory;
import helpers.throwables.ReportSummaryParams;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.mortbay.log.Log;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.ConnectionClosedException;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.KeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Log4j
@UtilityClass
public class Common {
    public static WebDriver driver;
    private static StopWatch stopWatch;
    public static int timeoutInSeconds = 15;
    private static final String CHROMEPATH = ".\\driver\\chromedriver.exe";

    private static ReportManager reporter;

    private static final String key;
    private static final String salt = "y^[$\\t,;";

    static {
        if (System.getProperty("key") == null) {
            throw new NullPointerException("Failed to load default key");
        } else {
            key = System.getProperty("key");
        }
    }

    @Step("Inicjalizacja Webdriver oraz przekazanie testu do realizacji")
    public static WebDriver setUpClass() {
        driver = initLocalDriver(Browser.CHROME, BrowserArg.INCOGNITO, ReportManagerFactory.ReporterType.ALLURE);
        return driver;
    }

    public static void quitDriver() {
        log.info("Zamykanie Webdrivera...");
        try {
            if (driver != null && (!(driver instanceof RemoteWebDriver) || ((RemoteWebDriver) driver).getSessionId() != null)) {
                driver.quit();
            }
        } catch (WebDriverException e) {
            if (e.getMessage().matches("^Session \\[a-f0-9]* was terminated .*$"))
                log.warn("Driver zamknięty niestandradowo \n", e);
            else handleWebDriverExceptions(e, "quitDriver");
        }
    }

    public static WebDriver initLocalDriver(Browser browser, BrowserArg arg, ReportManagerFactory.ReporterType reporterType) {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            switch (arg) {
                case HEADLESS:
                    log.info("######### MODE HEADLESS ##########");
                    switch (browser) {
                        case CHROME:
                            System.setProperty("webdriver.chrome.silentOutput", "true"); //ukrycie błędu "Timed out receiving message from renderer: 0.100"
                            System.setProperty("webdriver.chrome.driver", CHROMEPATH);
                            ChromeOptions chromeOptions = new ChromeOptions().merge(caps);
                            chromeOptions.addArguments("-incognito");
                            chromeOptions.addArguments("--headless");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            chromeOptions.addArguments("--lang=" + "pl_PL");
                            driver = new ChromeDriver(chromeOptions);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown browser " + browser);
                    }
                    break;
                case INCOGNITO:
                    log.info("######### MODE INCOGNITO ##########");
                    switch (browser) {
                        case CHROME:
                            System.setProperty("webdriver.chrome.silentOutput", "true"); //ukrycie błędu "Timed out receiving message from renderer: 0.100"
                            System.setProperty("webdriver.chrome.driver", CHROMEPATH);
                            ChromeOptions chromeOptions = new ChromeOptions().merge(caps);
                            chromeOptions.addArguments("-incognito");
                            driver = new ChromeDriver(chromeOptions);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown browser " + browser);
                    }
                    break;
            }
        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, null);
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(timeoutInSeconds, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(timeoutInSeconds, TimeUnit.SECONDS);
        reporter = ReportManagerFactory.buildReporter(reporterType, driver);

        return driver;
    }

    public static void initReporter(ReportManagerFactory.ReporterType reporterType, Object driver) {
        reporter = ReportManagerFactory.buildReporter(reporterType, driver);
    }

    public static void initSkippingReporter(ReportManagerFactory.ReporterType reporterType) {
        reporter = ReportManagerFactory.buildSkippingReporter(reporterType);
    }

    @Step("Przejście do url /  endpoint {url}")
    public static void openUrl(boolean czyKompletnyUrl, String url) {
        if (czyKompletnyUrl) {
            driver.get(url);
            reporter().logPass("Uruchomienie strony: " + url);
        } else {
            String[] logowanieUrlSplit = url.split("/");
            String aktualnyURL = "https://" + logowanieUrlSplit[2] + url;
            driver.get(aktualnyURL);
            reporter().logPass("Uruchomienie strony: " + aktualnyURL);
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

        if (reporter == null) initSkippingReporter(ReportManagerFactory.ReporterType.ALLURE);

        if (e instanceof ConnectionClosedException) {
            reporter().logFail("Utracono połączenie ze sterownikiem Safari.", e);
        } else if (e instanceof ElementClickInterceptedException || e.getMessage().contains("Other element void receive the click")) {
            reporter().logFail("Kliknięcie w " + desc + " zostało przechwycone przez inny element. " + "Upewnij się, że lement w momencie kliknięcia nie jest zasłonięty.", e);
        } else if (e instanceof ElementNotVisibleException) {
            reporter().logFail("Element " + desc + " nie jest widoczny na ekranie.", e);
        } else if (e instanceof ElementNotInteractableException) {
            reporter().logFail("Interakcja z elementem " + desc + " nie jest możliwa.", e);
        } else if (e instanceof ElementNotSelectableException) {
            reporter().logFail("Nie jest możliwe wybranie elementu " + desc + ".", e);
        } else if (e instanceof InvalidSelectorException) {
            reporter().logFail("Selektor elementu " + desc + "jest nieprawidłowy.", e);
        } else if (e instanceof NoSuchElementException) {
            reporter().logFail("Element " + desc + " nie istnieje.", e);
        } else if (e instanceof NoAlertPresentException) {
            reporter().logFail("Oczekiwano ostrzeżenia, ale żadnego nie znaleziono.", e);
        } else if (e instanceof JavascriptExecutor) {
            reporter().logFail("Błąd Javascriptu. Rozwiń aby zobaczyć szczegóły.", e);
        } else if (e instanceof InvalidElementStateException) {
            reporter().logFail("Stan elementu " + desc + " nie pozwala na wykonanie żądanej akcji. Rozwiń by zobaczyć szczegóły.", e);
        } else if (e instanceof StaleElementReferenceException) {
            reporter().logFail("Stan elementu " + desc + " już nie istnieje. Sprawdź czy nie zmienił się jego lokator.", e);
        } else if (e instanceof UnhandledAlertException) {
            reporter().logFail("Nieobsłużone ostrzeżenie: " + getAlertText(false) + ". Sprawdź log testu.", e);
        } else if (e instanceof TimeoutException) {
            reporter().logFail("Akcja nie została wykonana w podanym czasie. " +
                    "Jeśli oczekiwałeś na zmianę stanu elementu, " +
                    "zweryfikuj warunki zmiany stanu lub przedłuż oczekiwanie.", e);
        } else if (e instanceof UnexpectedTagNameException) {
            reporter().logFail("Element " + desc + " posiada inny tag niż oczekiwano.", e);
        } else if (e instanceof SessionNotCreatedException) {
            reporter().logFail("Nie udało się utworzyć sesji testowej. Rozwiń aby zobaczyć szczegóły.", e);
        } else {
            reporter().logFail("Wystapił  nieoczekiwany błąd WebDrivera. Rozwiń, by zobaczyć szczegóły", e);
        }
    }

    public static String getAlertText(boolean failOnFound) {
        if (driver == null || driver.toString().contains("null")) return null;
        try {
            Alert a = driver.switchTo().alert();
            String alertText = a.getText();
            a.dismiss();
            if (failOnFound) {
                reporter().logFail("Komunikat " + alertText);
            }
            return alertText;
        } catch (NoAlertPresentException e) {
            return null;
        } catch (WebDriverException e) {
            log.warn("Failed to fetch alert message", e);
            return null;
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

    @Step("Sprawdzenie widoczności elementu")
    public static void verifyElementDisplayed(WebElement element, String logPassMsg, String logFailMsg) {
        if (CommonWaits.waitUntilElementVisible(element, 30) != null) {
            reporter.logPass(logPassMsg);
        } else {
            reporter.logFail(logFailMsg);
        }
    }

    public static boolean isElementPresent(By by, int timeoutsInSeconds) {
        return CommonWaits.waitUntilElementPresent(by, timeoutsInSeconds) != null;
    }

    public static boolean isElementVisible(DescribeBy locator, int timeoutInSeconds) {
        return CommonWaits.waitUntilElementVisible(getElement(locator.by), timeoutInSeconds) != null;
    }

    public static boolean isElementVisible(WebElement element, int timeoutInSeconds) {
        return CommonWaits.waitUntilElementVisible(element, timeoutInSeconds) != null;
    }

    public static void startTimeCounter() {
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    public static void stopTimeCounter() {
        if (!stopWatch.isStarted()) {
            reporter.logWarn("Problem z licznikiem czasu");
        } else {
            stopWatch.stop();
            long pageLoadTime_ms = stopWatch.getTime();
            long pageLoadTime_seconds = pageLoadTime_ms / 1000;
            reporter.logPass("Całkowity czas wczytywania strony: " + pageLoadTime_seconds + " sek");
        }
    }

    public static void changeTimeout(int implicitlyWait) {
        driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
        reporter.logPass("implicitlyWait zmieniony na: " + implicitlyWait + "s");
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

    @Step("Zmiana aktywnej karty")
    public static void switchToNewTab(boolean failIfNotOpened) {
        final String currentTabHandle = driver.getWindowHandle();
        log.info("Obecnie aktywna karta: " + currentTabHandle);
        String title1 = driver.getTitle();
        try {
            int time = 20;
            for (int i = 0; i < time; i++) {
                ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                pauseFor(1);
                if (tabs.size() > 1) {
                    log.info("Nowa zakładka otwarta");
                    String newTabHandle = driver.getWindowHandles()
                            .stream()
                            .filter(handle -> !handle.equals(currentTabHandle))
                            .findFirst()
                            .orElseThrow(IllegalStateException::new);
                    driver.switchTo().window(newTabHandle);
                    log.info("Zmiana aktywnej karty na: " + newTabHandle);
                    changeTimeout(60, 60, 60);
                    try {
                        pauseFor(10);
                        if (!driver.getTitle().isEmpty()) {
                            String title2 = driver.getTitle();
                            reporter.logPass("Zmieniono aktywną kartę z: " + currentTabHandle + " (tytuł: " + title1 + ") na: " + newTabHandle + " (tytuł: " + title2 + ").");
                            changeTimeout(30, 30, 30);
                            break;
                        } else if (i < time - 1) {
                            reporter.logWarn("Pusty tytuł strony, odświeżenie i wycofanie...");
                            CommonActions.refreshSite();
                            driver.switchTo().window(currentTabHandle);
                        }

                    } catch (WebDriverException e) {
                        if (e instanceof TimeoutException || e.getMessage().contains("cannot determine loading status")) {
                            reporter.logFail("Nowa zakładka otworzyła się niepoprawnie: nie można pobrać " + "tytułu strony. Sprawdź, czy strona załadowała się poprawnie.");
                        }
                        handleWebDriverExceptions(e, null);
                    }
                    changeTimeout(30, 30, 30);
                }
                if (i == time - 1 && (tabs.size() == 1) && failIfNotOpened) {
                    reporter.logFail("Nowa zakładka nie została otwarta. Czas oczekiwania: " + time + "s");
                }
            }

        } catch (WebDriverException e) {
            handleWebDriverExceptions(e, null);
        }
    }

    @Step("Zmiana aktywnej karty")
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
                log.info("Zmieniono aktywność okna z: " + parentWindowHandler + " na: " + subWindowHandler);
            } else {
                log.info("Aktywność okna nie została zmieniona");
            }
        } catch (Exception e) {
            if (e instanceof WebDriverException) handleWebDriverExceptions((WebDriverException) e, "window handles");
            else reporter.logError("Problem ze zmianą aktywności karty", e);
        }
    }

    public static void closeTab(int tab) {
        try {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tab));
            driver.close();
            driver.switchTo().window(tabs.get(tab == 0 ? 0 : tab - 1));
        } catch (Exception e) {
            reporter().logFail("Problem z zakładkami..." + e);
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

    public static ReportManager reporter() {
        if (reporter == null) initSkippingReporter(ReportManagerFactory.ReporterType.ALLURE);
        return reporter;
    }

    public static String getFileContentString(File f) {
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new FileReader(f);
            int c;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void reportSummaryAndSendResults(ReportSummaryParams params) {
        reportSummaryAndSendResults(params.getTestName(), params.getAppName().getDescription(), params.getEnv(),
                params.getStatus(), params.getStage(), params.isPassed(), params.isFinalReport(), params.getVarArgs());
    }

    public static void reportSummaryAndSendResults(AbstractTestCaseData testCaseData, Profile env){
        if(testCaseData != null){
            ReportManager reporter = reporter();
            initHeaderReport(testCaseData, reporter);
            testCaseData.printInReporter(reporter);
            testCaseData.printDetailsInReporter(reporter);
            initFooterReport(testCaseData, reporter);

            setToDb(testCaseData, env);
        }
    }


    public static void reportSummaryAndSendResults(String testName, String appName, Profile envs, DataRowStatus status,
                                                   String stage, boolean passed, boolean finalRaport, String... params) {
        if (reporter == null) {
            initSkippingReporter(ReportManagerFactory.ReporterType.ALLURE);
        }
        if (finalRaport) {
            reporter().logPass("=##################################=");
            reporter().logPass("PODSUMOWANIE");
        } else {
            reporter().logPass("=====================================");
            reporter().logPass("PODSUMOWANIE CZĘSCIOWE");
        }
        reporter().logPass("Data: " + getCurrentDate(""));
        reporter().logPass("Nazwa testu: " + testName);
        reporter().logPass("Aplikacja: " + appName);
        reporter().logPass("Środowisko: " + envs.name());
        reporter().logPass("Etap: " + stage);
        if (params != null) {
            for (String param : params) {
                reporter.logPass(param);
            }
        }
        if (finalRaport) {
            reporter().logPass("=##################################=\n");
        } else {
            reporter().logPass("=====================================\n");
        }
        if (passed) {
            log.info(">>>>> [DB] Zapisywanie do danych testowych <<<<<<");
            CustomTestDTO data = CustomTestDTO.builder()
                    .nazwaTestu(testName)
                    .nazwaAplikacji(appName)
                    .env(envs.name())
                    .status(status)
                    .etap(stage)
                    .param1(params != null && params.length > 0 ? params[0] : "NULL")
                    .param2(params != null && params.length > 1 ? params[1] : "NULL")
                    .param3(params != null && params.length > 2 ? params[2] : "NULL")
                    .param4(params != null && params.length > 3 ? params[3] : "NULL")
                    .param5(params != null && params.length > 4 ? params[4] : "NULL")
                    .param6(params != null && params.length > 5 ? params[5] : "NULL")
                    .param7(params != null && params.length > 6 ? params[6] : "NULL")
                    .param8(params != null && params.length > 7 ? params[7] : "NULL")
                    .param9(params != null && params.length > 8 ? params[8] : "NULL")
                    .param10(params != null && params.length > 9 ? params[9] : "NULL")
                    .param11(params != null && params.length > 10 ? params[10] : "NULL")
                    .param12(params != null && params.length > 11 ? params[11] : "NULL")
                    .build();
            new TestDataManager().getCustomDataManager().sendCustomTestData(data, envs);
        }
    }

//    TODO przerobić aby nie wyskakiwał błąd java.lang.IndexOutOfBoundsException: Index: 2, Size: 2
//    public static void reportSummaryAndSendResults(ReportSummaryParams params) {
//        if (reporter == null) {
//            initSkippingReporter(ReportManagerFactory.ReporterType.ALLURE);
//        }
//        if (params.isFinalReport()) {
//            reporter().logPass("\n=##################################=");
//            reporter().logPass("PODSUMOWANIE");
//        } else {
//            reporter().logPass("\n=====================================");
//            reporter().logPass("PODSUMOWANIE CZĘSCIOWE");
//        }
//        reporter().logPass("Data: " + getCurrentDate(""));
//        reporter().logPass("Nazwa testu: " + params.getTestName());
//        reporter().logPass("Aplikacja: " + params.getAppName().getDescription());
//        reporter().logPass("Środowisko: " + params.getEnv().name());
//        reporter().logPass("Etap: " + params.getStage());
//        if (params != null) {
//            for (String param : params.getParams()) {
//                reporter.logPass(param);
//            }
//        }
//        if (params.isFinalReport()) {
//            reporter().logPass("=##################################=\n");
//        } else {
//            reporter().logPass("=====================================\n");
//        }
//        if(params.isPassed()){
//            log.info(">>>>> [DB] Zapisywanie do danych testowych <<<<<<");
//            CustomTestDTO data = CustomTestDTO.builder()
//                    .nazwaTestu(params.getTestName().split(" ")[0])
//                    .applicationName(params.getAppName().getDescription())
//                    .env(params.getEnv().name())
//                    .status(params.getStatus())
//                    .etap(params.getStage())
//                    .param1(params.getParams().get(0) != null && params.getParams().get(0).length() > 0 ? params.getParams().get(0) : null)
//                    .param2(params.getParams().get(1) != null && params.getParams().get(1).length() > 0 ? params.getParams().get(1) : null)
//                    .param3(params.getParams().get(2) != null && params.getParams().get(2).length() > 0 ? params.getParams().get(2) : null)
//                    .param4(params.getParams().get(3) != null && params.getParams().get(3).length() > 0 ? params.getParams().get(3) : null)
//                    .param5(params.getParams().get(4) != null && params.getParams().get(4).length() > 0 ? params.getParams().get(4) : null)
//                    .param6(params.getParams().get(5) != null && params.getParams().get(5).length() > 0 ? params.getParams().get(5) : null)
//                    .param7(params.getParams().get(6) != null && params.getParams().get(6).length() > 0 ? params.getParams().get(6) : null)
//                    .param8(params.getParams().get(7) != null && params.getParams().get(7).length() > 0 ? params.getParams().get(7) : null)
//                    .param9(params.getParams().get(8) != null && params.getParams().get(8).length() > 0 ? params.getParams().get(8) : null)
//                    .param10(params.getParams().get(9) != null && params.getParams().get(9).length() > 0 ? params.getParams().get(9) : null)
//                    .param11(params.getParams().get(10) != null && params.getParams().get(10).length() > 0 ? params.getParams().get(10) : null)
//                    .param12(params.getParams().get(11) != null && params.getParams().get(11).length() > 0 ? params.getParams().get(11) : null)
//                    .build();
//            new TestDataManager().getCustomDataManager().sendCustomTestData(data, params.getEnv());
//        }
//    }

    public static String encryptAES(String message) {
        return encryptAES(message, key);
    }

    public static String encryptAES(String message, String key) {
        try {
            if (message == null || key == null) {
                return null;
            }

            char[] hexArray = "0123456789ABCDEF".toCharArray();

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65535, 256);
            SecretKey tmp = factory.generateSecret(spec);
            Key assKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, assKey);

            byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            char[] ivHexChars = new char[iv.length * 2];
            for (int i = 0; i < iv.length; i++) {
                int v = iv[i] & 0xFF;
                ivHexChars[i * 2] = hexArray[v >>> 4];
                ivHexChars[i * 2 + 1] = hexArray[v & 0x0F];
            }

            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

            char[] hexChars = new char[encrypted.length * 2];
            for (int i = 0; i < encrypted.length; i++) {
                int v = encrypted[i] & 0xFF;
                hexChars[i * 2] = hexArray[v >>> 4];
                hexChars[i * 2 + 1] = hexArray[v & 0x0F];
            }

            return new String(hexChars) + "\n" + new String(ivHexChars);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt text", e);
        }
    }

    public static String decryptAES(String hexMessage) {
        return descryptAES(hexMessage, key);
    }

    public static String descryptAES(String hexMessage, String key) {
        if (hexMessage == null || hexMessage.equals("")) {
            return "";
        }
        try {
            String[] parts = hexMessage.split("\n");

            if (parts[0].matches(".*\r$")) {
                parts[0] = parts[0].substring(0, parts[0].length() - 1);
            }

            int cipherLen = parts[0].length(), ivLen = parts[1].length();
            byte[] encrypted = new byte[cipherLen / 2];
            for (int i = 0; i < cipherLen; i += 2) {
                encrypted[i / 2] = (byte) ((Character.digit(parts[0].charAt(i), 16) << 4)
                        + Character.digit(parts[0].charAt(i + 1), 16));
            }

            byte[] iv = new byte[ivLen / 2];
            for (int i = 0; i < ivLen; i += 2) {
                iv[i / 2] = (byte) ((Character.digit(parts[1].charAt(i), 16) << 4)
                        + Character.digit(parts[1].charAt(i + 1), 16));
            }

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65535, 256);
            SecretKey tmp = factory.generateSecret(spec);
            Key assKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, assKey, new IvParameterSpec(iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt file", e);
        }
    }

    public static boolean checkSkip(String skip) {
        if (skip != null) {
            if (skip.equals("true")) {
                reporter().skipTest(StaticStrings.TEST_SKIP);
            } else if (skip.matches("^NO_REQ_DATA: .*$")) {
                reporter().skipTest(StaticStrings.NO_REQ_DATA + skip.substring(13));
            } else if (skip.startsWith("NO_DATA_")) {
                if (skip.startsWith("NO_DATA_FAIL")) {
                    reporter().logFail("Brak danych testowych: testy zasilające zakończone niepowodzeniem " +
                            "(" + skip.split(" ")[1] + "; oczekiwano " + skip.split(" ")[2] + ", znaleziono "
                            + skip.split(" ")[3] + ").");
                }
                return true;
            } else if (skip.contains("JIRA")) {
                reporter().skipTest(StaticStrings.BUG_FAIL + skip);
            } else if (skip.equals("ProcesBiznesowy")) {
                reporter().skipTest(StaticStrings.CHANGE_PROCES);
            } else {
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    private static void initHeaderReport(AbstractTestCaseData testCaseData, ReportManager reporter){
        if(testCaseData.isFinalReport()) {
            reporter.logPass("-=##########################=-");
            reporter.logPass("PODSUMOWANIE");
        } else {
            reporter.logPass("===================");
            reporter.logPass("PODSUMOWANIE CZĘŚCIOWE");
        }
    }

    private static void initFooterReport(AbstractTestCaseData testCaseData, ReportManager reporter){
        if(testCaseData.isFinalReport()){
            reporter.logPass("-=#####################=-");
        } else {
            reporter.logPass("=========================");
        }
    }

    private static void setToDb(AbstractTestCaseData testCaseData, Profile env){
        if(testCaseData.isSendToDb()){
            Log.info(">>>>>> [DB] Zapisywanie danych testowych <<<<<<<<");
            new TestDataManager().getCustomDataManager().sendCustomTestData(testCaseData.castToCustomTestDto(), env);
        }
    }
}