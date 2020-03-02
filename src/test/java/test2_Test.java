import google.GoogleCommon;
import google.GoogleSearchPage;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static helpers.Common.*;
import static helpers.Common.reporter;
import static helpers.CommonActions.clickElement;
import static helpers.CommonActions.enterIntoElement;

public class test2_Test {
    protected WebDriver driver;

    @Before
    public void setUp() {
        driver = setUpClass();
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
        quitDriver();
    }

    public void krok1OtworzenieStrony() {
        openUrl(true, "https://www.wp.pl/");
        enterIntoElement(GoogleSearchPage.poleWyszukania, "lala", "Pole szukania", false);
        clickElement(GoogleSearchPage.przyciskWyszukaj, "Wyszukaj");
        clickElement(GoogleSearchPage.next, "next");
        String title = driver.getTitle();
        if(title.contains("lalla")) {
            reporter().logPass("Prawidłowy tytuł sttrony");
        } else {
            reporter().logFail("Nieprawidłowy tytuł strony");
        }
    }
}