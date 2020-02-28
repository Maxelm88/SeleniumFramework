import google.GoogleCommon;
import google.GoogleSearchPage;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static helpers.Common.openUrl;
import static helpers.Common.quitDriver;
import static helpers.Common.setUpClass;
import static helpers.CommonActions.clickElement;
import static helpers.CommonActions.enterIntoElement;
import static org.junit.Assert.assertTrue;

@DisplayName("Testowanie raportu")
public class test_Test {

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
        openUrl(true, "https://www.google.pl/");
        enterIntoElement(GoogleSearchPage.poleWyszukania, "lala", "Pole szukania");
        clickElement(GoogleSearchPage.przyciskWyszukaj, "Wyszukaj");
        clickElement(GoogleSearchPage.next, "next");
        String title = driver.getTitle();
        assertTrue(title.contains("lallla"));
    }
}
