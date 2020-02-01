import google.GoogleCommon;
import google.GoogleSearchPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.WebDriver;

import static helpers.common.Common.openUrl;
import static helpers.common.Common.quitDriver;
import static helpers.common.Common.setUpClass;
import static helpers.common.CommonActions.clickElement;
import static helpers.common.CommonActions.enterIntoElement;

public class test {

    protected WebDriver driver;

    @Before
    public void setUp() {
        driver = setUpClass();
        GoogleCommon.initElement(driver);
    }

    @Test
    public void testMethod() {
        krok1OtworzenieStrony();
    }

    @After
    public void tearDown(){
        if(System.getProperty("skip") != null && !System.getProperty("skip").equals("false")) return;
        quitDriver();
    }

    public void krok1OtworzenieStrony(){
        openUrl(true, "https://www.google.pl/");
        enterIntoElement(GoogleSearchPage.poleWyszukania, "lala", "Pole szukania");
        clickElement(GoogleSearchPage.przyciskWyszukaj, "Wyszukaj");
        clickElement(GoogleSearchPage.next, "next");
    }
}
