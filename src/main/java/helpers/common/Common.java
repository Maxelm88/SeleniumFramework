package helpers.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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

    public static void pauseFor(int timeoutInSeconds){
        try {
            timeoutInSeconds = timeoutInSeconds * 1000;
            Thread.sleep(timeoutInSeconds);
        } catch (InterruptedException e) {
            System.out.println("Błąd " + e);
        }
    }
}
