package mystore;

import abstractBase.AbstractRstWebTest;
import helpers.dictionary.ApplicationName;
import org.junit.Before;
import store.StoreCommon;

public abstract class AbstractMyStoreTest extends AbstractRstWebTest {
    protected static final ApplicationName APP_NAME = ApplicationName.STORE;
    protected static final String USER = "user@user.pl";

    @Before
    public void initApp() {
        StoreCommon.initElement(driver);
    }

}
