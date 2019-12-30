package helpers.common;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class CommonWaits {

    public static WebElement waitUntilElementPresent(By by, int timeoutInSeconds){
        WebElement found;
        Common.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        try{
            found = new WebDriverWait(Common.driver, timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(by));
        }
        catch (NoSuchElementException e) {
            Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            return null;
        }
        Common.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return found;
    }
}
