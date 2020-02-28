package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class CommonWaits {

    public static WebElement waitUntilElementPresent(By by, int timeoutInSeconds) {
        WebElement found;
        Common.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        try {
            found = new WebDriverWait(Common.driver, timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (NoSuchElementException | TimeoutException e) {
            Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            return null;
        }
        Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return found;
    }

    public static WebElement waitUntilElementVisible(WebElement element, int timeoutInSeconds) {
        if (element == null) {
            //reporter error
            System.out.println("Cannot wait  for visibility of null element");
        }
        WebElement found;
        Common.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        try {
            found = new WebDriverWait(Common.driver, timeoutInSeconds).until(ExpectedConditions.visibilityOf(element));
        } catch (NoSuchElementException | TimeoutException e) {
            return null;
        }
        Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return found;
    }

    public static boolean waitUntilElementStale(WebElement element, int timeoutInSeconds) {
        if (element == null) {
            //reporter error
            System.out.println("Cannot wait  for staleness of null element");
        }
        Common.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        boolean isStale;
        try {
            isStale = new WebDriverWait(Common.driver, timeoutInSeconds).until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            return false;
        }
        Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return isStale;
    }

    public static boolean waitUntilElementNotVisible(WebElement element, int timeoutInSeconds) {
        if (element == null) {
            //reporter error
            System.out.println("Cannot wait  for disappearance of null element");
        }
        Common.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        boolean isNotVisible;
        try {
            isNotVisible = new WebDriverWait(Common.driver, timeoutInSeconds).until(ExpectedConditions.invisibilityOf(element));

        } catch (TimeoutException e) {
            Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            return false;
        }
        Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return isNotVisible;
    }

    public static boolean waitUntilElementNotVisible(By by, int timeoutInSeconds) {
        Common.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        boolean isNotVisible;
        try {
            isNotVisible = new WebDriverWait(Common.driver, timeoutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(by));

        } catch (TimeoutException e) {
            Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            return false;
        }
        Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return isNotVisible;
    }
}
