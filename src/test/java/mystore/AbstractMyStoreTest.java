package mystore;

import helpers.Common;
import helpers.TestUtils;
import helpers.dictionary.ApplicationName;
import helpers.dictionary.Profile;
import helpers.dictionary.PropertyNames;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import store.StoreCommon;

public class AbstractMyStoreTest {
    protected WebDriver driver;
    protected static final String SKIP = PropertyNames.getSkip();
    protected static final ApplicationName APP_NAME = ApplicationName.STORE;
    protected static final Profile APP_ENV = PropertyNames.getProfile();

    @Before
    public void setUp() {
        driver = Common.setUpClass();
        StoreCommon.initElement(driver);
    }

    @After
    public void tearDown() {
        if (TestUtils.isSkip(SKIP)) {
            return;
        }
        Common.quitDriver();
    }
}
