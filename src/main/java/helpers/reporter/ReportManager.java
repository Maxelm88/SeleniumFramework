package helpers.reporter;

import helpers.reporter.screenshot.ScreenshotManager;
import helpers.reporter.screenshot.SeleniumScreenshotManager;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Date;

@Log4j
public abstract class ReportManager {

    protected ScreenshotManager manager;

    public ReportManager(WebDriver driver) {
        if (driver != null) {
            manager = new SeleniumScreenshotManager(driver);
        }
    }

    public abstract void logPass(String text, boolean takesScreenshot);

    public void logPass(String text) {
        logPass(text, false);
    }

    public void logPass(String valueName, String value){
        logPass(String.format("%s: [%s]", valueName, value), false);
    }

    public void logPassIsNotNull(String valueName, Object value){
        if(value != null){
            logPass(valueName, value.toString());
        }
    }

    public abstract void logFail(String text, Throwable t);

    public void logFail(String text) {
        logFail(text, null);
    }

    public abstract void logWarn(String text, boolean takesScreenshot);

    public void logWarn(String text) {
        logWarn(text, false);
    }

    public abstract void logError(String text, Throwable t);

    public void logError(String text) {
        logError(text, null);
    }

    public abstract void logSkip(String text, String details);

    public void logSkip(String text) {
        logSkip(text, null);
    }

    public abstract byte[] saveScreenshot();

    public abstract void skipTest(String text);

    public ScreenshotManager getScreenshotManager() {
        return manager;
    }

    public static void getBrowserLogs(WebDriver driver) {
        try {
            if (driver == null || (driver instanceof RemoteWebDriver && ((RemoteWebDriver) driver).getSessionId() == null)) {
                return;
            }

            LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
            int count = logEntries.getAll().size();
            int j = count > 3 ? 3 : 1;
            for (int i = 1; i < j; i++) {
                log.error("=========================Browser log==============");
                log.error(new Date(logEntries.getAll().get(count - i).getTimestamp()) + " " + logEntries.getAll().get(count - i).getLevel() + " " + logEntries.getAll().get(count - 1).getMessage());
                log.error("==================================================");
            }
        } catch (UnsupportedOperationException e) {
            log.warn("Nie udało się pobrać logów przeglądarki");
        }
    }
}
