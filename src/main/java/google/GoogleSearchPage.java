package google;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleSearchPage {
    @FindBy(name = "q")
    public static WebElement poleWyszukania;
    @FindBy(name = "btnK")
    public static WebElement przyciskWyszukaj;
    @FindBy(xpath = "//span[contains(text(), 'Następna')]")
    public static WebElement next;
}


