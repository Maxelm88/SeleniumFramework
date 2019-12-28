package google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class GoogleCommon {

    public static void initElement(WebDriver driver)
    {
        PageFactory.initElements(driver, google.GoogleSearchPage.class);
    }
}
