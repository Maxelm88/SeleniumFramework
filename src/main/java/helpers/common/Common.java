package helpers.common;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.ConnectionClosedException;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

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
}
