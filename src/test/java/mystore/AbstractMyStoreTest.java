package mystore;

import helpers.Common;
import helpers.TestUtils;
import helpers.datebase.TestDataManager;
import helpers.datebase.dto.CustomTestDTO;
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
    protected static boolean status = false;
    protected CustomTestDTO daneTestowe;
    protected TestDataManager manager;

    @Before
    public void setUp() {
        driver = Common.setUpClass();
        StoreCommon.initElement(driver);
        manager = new TestDataManager();
    }

    @After
    public void tearDown() {
        if (TestUtils.isSkip(SKIP)) {
            return;
        }
        Common.quitDriver();
    }
}
