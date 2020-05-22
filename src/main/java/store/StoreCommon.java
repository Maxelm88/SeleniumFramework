package store;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class StoreCommon {

    public static void initElement(WebDriver driver) {
        PageFactory.initElements(driver, store.po.StoreHomepage.class);
        PageFactory.initElements(driver, store.po.StoreSignInPage.class);
        PageFactory.initElements(driver, store.po.StoreMyAccountPage.class);
    }
}
