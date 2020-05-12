package mystore;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import store.StoreCommon;

import static helpers.Common.quitDriver;
import static helpers.Common.setUpClass;

public class AbstractMyStoreTest {
    protected WebDriver driver;

    @Before
    public void setUp() {
        driver = setUpClass();
        StoreCommon.initElement(driver);
    }

    @After
    public void tearDown() {
        if (System.getProperty("skip") != null && !System.getProperty("skip").equals("false")) return;
        quitDriver();
    }
}
