import google.GoogleCommon;
import google.GoogleSearchPage;
import helpers.Common;
import helpers.CommonActions;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class test2_Test {
    protected WebDriver driver;

    @Before
    public void setUp() {
        driver = Common.setUpClass();
        GoogleCommon.initElement(driver);
    }

    @DisplayName("Wyszukiwanie google")
    @Test
    public void testMethod() {
        krok1OtworzenieStrony();
    }

    @After
    public void tearDown() {
        if (System.getProperty("skip") != null && !System.getProperty("skip").equals("false")) return;
        Common.quitDriver();
    }

    public void krok1OtworzenieStrony() {
        Common.openUrl(true, "https://www.wp.pl/");
        CommonActions.enterIntoElement(GoogleSearchPage.poleWyszukania, "lala", false);
        CommonActions.clickElement(GoogleSearchPage.przyciskWyszukaj);
        CommonActions.clickElement(GoogleSearchPage.next);
        String title = driver.getTitle();
        if(title.contains("lalla")) {
            Common.reporter().logPass("Prawidłowy tytuł sttrony");
        } else {
            Common.reporter().logFail("Nieprawidłowy tytuł strony");
        }
    }
}
